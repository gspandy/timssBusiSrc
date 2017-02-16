package com.timss.inventory.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.inventory.vo.InvEipOpinionsEip;
import com.yudean.itc.code.UsualOpinionsType;
import com.yudean.itc.dto.interfaces.eip.mobile.RetAttachmentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean.Type;
import com.yudean.itc.dto.interfaces.eip.mobile.RetFlowsBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetKeyValue;
import com.yudean.itc.dto.interfaces.eip.mobile.RetTask;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
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
public class CommonInvToEipMobile {

    private static final Logger LOG = Logger.getLogger( CommonInvToEipMobile.class );

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
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
            // 获取流程历史信息
            List<Map<String, Object>> list = historyInfoService.getHistoryComment( processId );
            for ( Map<String, Object> map : list ) {
                InvEipOpinionsEip eob = new InvEipOpinionsEip();
                // 历史信息提供的是员工编码，要转换成中文
                eob.setWho( String.valueOf( map.get( "ASSIGNEENAME" ) ) );
                // 时间格式转换
                eob.setWhen( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( (Date) map.get( "END" ) ) );
                // 审批信息
                eob.setWhat( String.valueOf( map.get( "COMMENT_" ) ) );
                rkvList.add( eob );
            }
            rcilb.setValue( rkvList );
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
     * @throws Exception :
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
     * @createDate: 2014-9-23
     * @param userInfo
     * @param sheetId
     * @return:
     */
    public List<RetAttachmentBean> assembleAttachements() {
        List<RetAttachmentBean> eabList = new ArrayList<RetAttachmentBean>();
        return eabList;
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
        if ( null != activities && !activities.isEmpty() ) {
            task = activities.get( 0 );

            // 获取下一环节信息
            efbList.add( showNextLink( new RetFlowsBean(), processId, task, processDefKey ) );
            // 获取上一环节信息
            RetFlowsBean retFlowsBean = showPreviousLink( new RetFlowsBean(), processId );
            if ( null != retFlowsBean ) {
                efbList.add( retFlowsBean );
            }

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

        try {
            EipBranchFlowProcessUtil ebfpu = new EipBranchFlowProcessUtil( workflowService );
            rtList = ebfpu.getNextElementsByFlowProperty( processId, task );
        } catch (Exception e) {
            LOG.info( "--------------------------------------------" );
            LOG.info( "- CommonInvToEipMobile 中的 showNextLink 方法抛出异常：" + e.getMessage() + " - " );
            LOG.info( "--------------------------------------------" );
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
        List<Task> activities = workflowService.getActiveTasks( processId );
        Task task = activities.get( 0 );
        if ( null != htList && !htList.isEmpty() ) {
            HistoricTask ht = htList.get( 0 );
            if ( ht.getTaskDefinitionKey().equals( task.getTaskDefinitionKey() ) ) {
                rfb = null;
            } else {
                RetTask rt = new RetTask();
                rt.setTask( new RetKeyValue( ht.getTaskDefinitionKey(), ht.getName() ) );

                List<RetKeyValue> rkvList = new ArrayList<RetKeyValue>();
                UserInfo ui = itcMvcService.getUserInfoById( ht.getAssignee() );
                rkvList.add( new RetKeyValue( ht.getAssignee(), ui.getUserName() ) );
                rt.setUser( rkvList );
                rtList.add( rt );
                rfb.setTask( rtList );
            }
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
            if ( null != activities && !activities.isEmpty() ) {
                task = activities.get( 0 );
                // 获取下一节点定义id
                List<String> nkeyList = workflowService.getNextTaskDefKeys( processId, task.getTaskDefinitionKey() );
                if ( null != nkeyList && !nkeyList.isEmpty() ) {
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
            if ( null != htList && !htList.isEmpty() ) {
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
            LOG.info( "--------------------------------------------" );
            LOG.info( "- CommonInvToEipMobile 中的 returnToPreviousLink 方法抛出异常：" + e.getMessage() + " - " );
            LOG.info( "--------------------------------------------" );
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
            if ( null != activities && !activities.isEmpty() ) {
                task = activities.get( 0 );
                workflowService.stopProcess( task.getId(), curUser, curUser, opinion );
                flag = true;
            }
        } catch (Exception e) {
            LOG.info( "--------------------------------------------" );
            LOG.info( "- CommonInvToEipMobile 中的 toStopCurProcess 方法抛出异常：" + e.getMessage() + " - " );
            LOG.info( "--------------------------------------------" );
        }
        return flag;
    }

    /**
     * @description: 在提交之前设置路由判断
     * @author: 890166
     * @createDate: 2015-4-1
     * @param processId
     * @param taskKey :
     */
    public void setFlowVariables(String processId, String taskKey) {
        try {
            EipBranchFlowProcessUtil ebfpu = new EipBranchFlowProcessUtil( workflowService );
            ebfpu.setFlowVariablesBeforeCommit( processId, taskKey );
        } catch (Exception e) {
            throw new RuntimeException( "---------CommonInvToEipMobile 中的 setFlowVariables 方法抛出异常---------：", e );
        }
    }
}
