package com.timss.attendance.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.inventory.vo.InvEipOpinionsEip;
import com.yudean.itc.code.UsualOpinionsType;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.interfaces.eip.mobile.RetAttachmentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean.Type;
import com.yudean.itc.dto.interfaces.eip.mobile.RetFlowsBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetKeyValue;
import com.yudean.itc.dto.interfaces.eip.mobile.RetTask;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.Attachment;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.configs.MvcWebConfig;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.TaskDefInfo;
import com.yudean.workflow.service.HistoryInfoService;
import com.yudean.workflow.service.HistoryInfoService.HistoricTask;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: EipMobileCommon.java
 * @author: 890166
 * @createDate: 2014-9-24
 * @updateUser: 890166
 * @version: 1.0
 */
@Component
public class CommonAtdToEipMobile {
    private Logger log = Logger.getLogger( CommonAtdToEipMobile.class );

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private HistoryInfoService historyInfoService;

    /**
     * @description:组织opinions中的数据
     * @author: 890166
     * @createDate: 2014-9-23
     * @param userInfo
     * @param sheetId
     * @return:
     */
    public RetContentInLineBean assembleOpinions(String processId) throws Exception {
        RetContentInLineBean rcilb = new RetContentInLineBean();
        rcilb.setFoldable( true );
        rcilb.setIsShow( true );
        rcilb.setName( "审批意见" );
        rcilb.setType( Type.Approval );

        List<Object> rkvList = new ArrayList<Object>();
        if ( null != processId && !"".equals( processId ) ) {
            List<Map<String, Object>> list = historyInfoService.getHistoryComment(processId);
            for(Map<String, Object> map :list){
                    InvEipOpinionsEip eob = new InvEipOpinionsEip();
                    //历史信息提供的是员工编码，要转换成中文
                    eob.setWho(String.valueOf(map.get("ASSIGNEENAME")));
                    //时间格式转换
                    eob.setWhen(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)map.get("END")));
                    //审批信息
                    eob.setWhat(String.valueOf(map.get("COMMENT_")));
                    rkvList.add(eob);
            }
            rcilb.setValue(rkvList);

        }
        return rcilb;
    }

    /**
     * @description:组织usualOpinions中的数据
     * @author: 890166
     * @createDate: 2014-9-23
     * @param userInfo
     * @param sheetId
     * @return
     * @throws Exception:
     */
    public List<UsualOpinionsType> assembleUsualOpinions() throws Exception {
        List<UsualOpinionsType> eobList = new ArrayList<UsualOpinionsType>();
        eobList.add( UsualOpinionsType.yes );// 同意
        eobList.add( UsualOpinionsType.no );// 不同意
        return eobList;
    }

    /**
     * @description:组织attachement中的数据
     * @author: 890166
     * @param fileIds
     * @createDate: 2014-9-23
     * @param userInfo
     * @param sheetId
     * @return:
     */
    public List<RetAttachmentBean> assembleAttachements(List<String> fileIds) {
        List<RetAttachmentBean> eabList = new ArrayList<RetAttachmentBean>();
        if ( fileIds != null && fileIds.size() > 0 ) {
            for ( String fileId : fileIds ) {
                Attachment attachment = attachmentMapper.selectById( fileId );
                RetAttachmentBean retAttachmentBean = convertToRetAttachmentBean( attachment );
                eabList.add( retAttachmentBean );
            }

        }
        return eabList;
    }

    /**
     * 将字节转换为用户可识别的信息
     * @Title: getRealFileSizeFromAttachment
     * @Description: 
     * @param size
     * @return String
     */
    private String getRealFileSizeFromAttachment(Integer size){
            String result="";
            int KUnit=1024;
            int MUnit=KUnit*1024;
            if(size<KUnit){
                    result="不到1K";
            }else if(size<MUnit){
                    result=size/KUnit+"K";
            }else{
                    result=size/MUnit+"M";
            }
            return result;
    }

    /**
     * 
     * @description:转化成附件
     * @author: fengzt
     * @createDate: 2015年1月15日
     * @param attachment
     * @return:RetAttachmentBean
     */
    private RetAttachmentBean convertToRetAttachmentBean(Attachment attachment) {
        RetAttachmentBean retAttachmentBean = new RetAttachmentBean();
        retAttachmentBean.setFileName( attachment.getOriginalFileName() );
        retAttachmentBean.setFileSize( getRealFileSizeFromAttachment( attachment.getFilesize() ) );
        retAttachmentBean.setFileSufx( getRealFileTypeFromAttachment( attachment.getFileType() ) );
        // 附件编码信息获取 同时处理url的域名信息
        String url = MvcWebConfig.serverBasePath + "upload?method=downloadFile&id=" + attachment.getId();
        retAttachmentBean.setFileUrl( url );
        return retAttachmentBean;
    }

    /**
     * 
     * @description:处理fileType,将类似“。txt”转换为“txt”
     * @author: fengzt
     * @createDate: 2015年1月15日
     * @param fileType
     * @return:
     */
    private static String getRealFileTypeFromAttachment(String fileType) {
        if ( StringUtils.isNotBlank( fileType ) ) {

            if ( fileType.indexOf( "." ) == 0 ) {
                fileType = fileType.substring( 1 );
            }
        } else {
            fileType = "";
        }
        return fileType;
    }

    /**
     * @description:组织flows中的数据
     * @author: 890166
     * @createDate: 2014-9-23
     * @param userInfo
     * @param sheetId
     * @return:
     */
    public List<RetFlowsBean> assembleFlows(UserInfoScope userInfo, String processId, String processDefKey) {
        Task task = null;
        List<RetFlowsBean> efbList = new ArrayList<RetFlowsBean>();

        List<Task> activities = workflowService.getActiveTasks( processId );
        if ( null != activities && activities.size() > 0 ) {
            task = activities.get( 0 );

            // 获取下一环节信息
            efbList.add( showNextLink( new RetFlowsBean(), processId, task, processDefKey ) );
            // 获取上一环节信息
            efbList.add( showPreviousLink( new RetFlowsBean(), processId ) );

            // 获取流程终止信息
            RetFlowsBean rfbStop = new RetFlowsBean();
            rfbStop.setId( "end" );
            rfbStop.setName( "终止" );
            rfbStop.setTask( new ArrayList<RetTask>() );
            efbList.add( rfbStop );
        }
        return efbList;
    }

    /**
     * @description:获取下一环节信息
     * @author: 890166
     * @createDate: 2014-9-25
     * @param processId
     * @param task
     * @return:
     */
    private RetFlowsBean showNextLink(RetFlowsBean rfb, String processId, Task task, String processDefKey) {
        rfb.setId( "next" );
        rfb.setName( "下一环节" );
        List<RetTask> rtList = new ArrayList<RetTask>();

        // 获取下一节点定义id
        List<String> nkeyList = workflowService.getNextTaskDefKeys( processId, task.getTaskDefinitionKey() );
        if ( null != nkeyList && nkeyList.size() > 0 ) {
            for ( String nextKey : nkeyList ) {
                RetTask rt = new RetTask();
                // 找到下一个节点的具体信息
                ProcessInstance pi = workflowService.getProcessByProcessInstId( processId );
                TaskDefInfo tdi = workflowService.getTaskDefInfoByTaskDefKey( pi.getProcessDefinitionId(), nextKey );
                rt.setTask( new RetKeyValue( tdi.getDefKey(), tdi.getName() ) );

                // 找到下一个节点的人员信息
                List<RetKeyValue> rkvList = new ArrayList<RetKeyValue>();
                List<SecureUser> suList = workflowService.selectUsersByTaskKey( processId, nextKey, task.getId() );
                for ( SecureUser su : suList ) {
                    rkvList.add( new RetKeyValue( su.getId(), su.getName() ) );
                }
                rt.setUser( rkvList );
                rtList.add( rt );
            }
        } else {
            RetTask rt = new RetTask();
            rt.setTask( new RetKeyValue( "end", "审批结束" ) );
            rt.setUser( new ArrayList<RetKeyValue>() );
            rtList.add( rt );
        }
        rfb.setTask( rtList );
        return rfb;
    }

    /**
     * @description:展示上一环节信息
     * @author: 890166
     * @createDate: 2014-9-25
     * @param processId
     * @param task
     * @return:
     */
    private RetFlowsBean showPreviousLink(RetFlowsBean rfb, String processId) {
        rfb.setId( "rollback" );
        rfb.setName( "退回" );
        List<RetTask> rtList = new ArrayList<RetTask>();

        List<HistoricTask> htList = workflowService.getPreviousTasks( processId );
        if ( null != htList && htList.size() > 0 ) {
            // for(HistoricTask ht : htList){
            HistoricTask ht = htList.get( 0 );
            RetTask rt = new RetTask();
            rt.setTask( new RetKeyValue( ht.getTaskDefinitionKey(), ht.getName() ) );

            List<RetKeyValue> rkvList = new ArrayList<RetKeyValue>();
            UserInfo ui = itcMvcService.getUserInfoById( ht.getAssignee() );
            rkvList.add( new RetKeyValue( ht.getAssignee(), ui.getUserName() ) );
            rt.setUser( rkvList );
            rtList.add( rt );
            // }
            rfb.setTask( rtList );
        }
        return rfb;
    }

    /**
     * @description:提交到下一环节
     * @author: 890166
     * @createDate: 2014-9-23
     * @return:
     */
    public boolean commitToNextLink(UserInfoScope userInfo, String opinion, List<String> nuser, String processId)
            throws Exception {
        Task task = null;
        String nextDefKey = null;
        String owner = null;
        String curUser = userInfo.getUserId();
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        boolean flag = false;
        if ( null != processId && !"".equals( processId ) ) {
            // 获取当前任务节点
            List<Task> activities = workflowService.getActiveTasks( processId );
            if ( null != activities && activities.size() > 0 ) {
                task = activities.get( 0 );
                // 获取下一节点定义id
                List<String> nkeyList = workflowService.getNextTaskDefKeys( processId, task.getTaskDefinitionKey() );
                if ( null != nkeyList && nkeyList.size() > 0 ) {
                    nextDefKey = nkeyList.get( 0 );
                    map.put( nextDefKey, nuser );
                }
                
                // 隐式提交到下一环节
                flag = workflowService.complete( task.getId(), curUser, owner, map, opinion, false );
            }
        }
        return flag;
    }

    /**
     * @description:退回到首环节
     * @author: 890166
     * @createDate: 2014-9-23
     * @return:
     */
    public boolean returnToPreviousLink(UserInfoScope userInfo, String opinion, String processId) {
        boolean flag = false;
        // 获取历史环节集合
        List<HistoricTask> htList = workflowService.getPreviousTasks( processId );
        try {
            if ( null != htList && htList.size() > 0 ) {
                // 获取首环节
                HistoricTask ht = htList.get( 0 );
                String destTaskKey = ht.getTaskDefinitionKey();
                String userId = ht.getAssignee();
                String assignee = userInfo.getUserId();
                // 使用回滚操作，将流程退回到首环节
                workflowService.rollback( processId, destTaskKey, opinion, assignee, assignee, userId );
                flag = true;
            }
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return flag;
    }

    /**
     * @description:终止流程
     * @author: 890166
     * @createDate: 2014-9-25
     * @param userInfo
     * @param opinion
     * @param processId
     * @return:
     */
    public boolean toStopCurProcess(UserInfoScope userInfo, String opinion, String processId) {
        boolean flag = false;
        Task task = null;
        String curUser = userInfo.getUserId();
        try {
            List<Task> activities = workflowService.getActiveTasks( processId );
            if ( null != activities && activities.size() > 0 ) {
                task = activities.get( 0 );
                workflowService.stopProcess( task.getId(), curUser, curUser, opinion );
                flag = true;
            }
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return flag;
    }
}
