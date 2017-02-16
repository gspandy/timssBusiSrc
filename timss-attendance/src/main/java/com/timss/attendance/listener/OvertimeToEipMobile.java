package com.timss.attendance.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.bean.OvertimeFileBean;
import com.timss.attendance.dao.OvertimeDao;
import com.timss.attendance.service.OvertimeService;
import com.timss.attendance.util.CommonAtdToEipMobile;
import com.timss.attendance.util.CommonInvokeUtil;
import com.timss.attendance.util.PropertyUtil;
import com.yudean.interfaces.interfaces.EipMobileInterface;
import com.yudean.itc.annotation.EipAnnotation;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetAttachmentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean.Type;
import com.yudean.itc.dto.interfaces.eip.mobile.RetFlowsBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetKeyValue;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: 加班申请eip接口
 * @description: {desc}
 * @company: gdyd
 * @className: OvertimeToEipMobile.java
 * @author: fengzt
 * @createDate: 2014年10月15日
 * @updateUser: fengzt
 * @version: 1.0
 */
@EipAnnotation("overtime")
@Component
public class OvertimeToEipMobile implements EipMobileInterface {
    
    private Logger log = Logger.getLogger( OvertimeToEipMobile.class );

    @Autowired
    private OvertimeService overtimeService;
    @Autowired
    private OvertimeDao overtimeDao;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private CommonAtdToEipMobile emc;
    @Autowired
    private WorkflowService workflowService;

    /**
     * 根据参数获取流程表单详情
     * 
     * @description:
     * @author: fengzt
     * @createDate: 2014-10-15
     * @param bean
     * @return:
     */
    @Override
    public RetContentBean retrieveWorkflowFormDetails(ParamDetailBean eipmobileparambean) {
        RetContentBean efdb = new RetContentBean();

        // 表单的sheetNo
        String flowNo = eipmobileparambean.getFlowNo();
        // 流程实例id
        String processId = eipmobileparambean.getProcessId();
        try {
            UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
            // 通过flowNo找到对应的id
            int id = overtimeService.queryIdByFlowNo( flowNo, userInfo.getSiteId() );

            // 组装forms中数据
            List<RetContentInLineBean> forms = assembleContent( id, processId );
            efdb.setContent( forms );

            // 组装附件中数据
            List<OvertimeFileBean> ovList = overtimeDao.queryFileByOvertimeId( Integer.toString( id ) );
            List<String> fileIds = new ArrayList<String>();
            for( OvertimeFileBean vo : ovList ){
                fileIds.add( vo.getFileId() );
            }
            List<RetAttachmentBean> attachements = emc.assembleAttachements( fileIds );
            efdb.setAttachments( attachements );

            // 组装flows中数据
            String processDefKey = getPropertiesVal( "overtimeDefKey" );
            String[] keyArr = processDefKey.split( "," );
            for ( String key : keyArr ) {
                if ( key.indexOf( userInfo.getSiteId().toLowerCase() ) > -1 ) {
                    processDefKey = key;
                }
            }
            // 组装flows中数据
            List<RetFlowsBean> flows = emc.assembleFlows( userInfo, processId, processDefKey );
            efdb.setFlows( flows );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return efdb;
    }

    /**
     * @description:组织form中的数据
     * @author: 890166
     * @createDate: 2014-9-23
     * @param formId
     * @return:
     */
    private List<RetContentInLineBean> assembleContent(int id, String processId)
            throws Exception {
        List<RetContentInLineBean> epbList = new ArrayList<RetContentInLineBean>();
        // 主单
        RetContentInLineBean rcilbForm = new RetContentInLineBean();
        // 明细
        // RetContentInLineBean rcilbList = new RetContentInLineBean();
        // 流程信息
        RetContentInLineBean rcilbOpi = new RetContentInLineBean();

        // 收集主单信息
        OvertimeBean vo  = overtimeDao.queryOvertimeById( id );
        rcilbForm = settingFormData( vo );
        epbList.add( rcilbForm );

        // 组装opions中数据
        rcilbOpi = emc.assembleOpinions( processId );
        epbList.add( rcilbOpi );

        return epbList;
    }
    

    /**
     * @description:转换form数据信息到接口实体
     * @author: 890166
     * @createDate: 2014-9-25
     * @param paList
     * @return:
     */
    private RetContentInLineBean settingFormData(Object vo) throws Exception {

        RetContentInLineBean pafem = new RetContentInLineBean();

        pafem.setFoldable( true );// 是否可以折叠标志
        pafem.setIsShow( true ); // 是否显示标志
        pafem.setName( getPropertiesVal( "eip2OvertimeFormGroupName" ) );// 名称
        pafem.setType( Type.KeyValue );// 类别，这里固定keyValue

        // 通过properties文件中定义的字段反射获取价值
        List<Object> rkvList = new ArrayList<Object>();
        String[] formField = getPropertiesVal( "eip2OvertimeForm" ).split( "," );
        for ( String ffield : formField ) {
            String[] fieldContent = ffield.split( "#@#" );
            String fieldKey = fieldContent[0];
            String fieldVal = CommonInvokeUtil.getFieldValueByFieldName( vo, fieldContent[1],"yyyy-MM-dd HH:mm" );

            rkvList.add( new RetKeyValue( fieldKey, fieldVal ) );
        }
        pafem.setValue( rkvList );
        return pafem;
    }

    /**
     * @description:获取properties文件中配置信息
     * @author: 890166
     * @createDate: 2014-9-25
     * @param typeName
     * @return
     * @throws Exception:
     */
    private String getPropertiesVal(String key) {
        PropertyUtil propertyUtil = new PropertyUtil( "attendanceEip.properties" );
        return propertyUtil.getProperty( key );
    }

    /**
     * 处理流程
     * 
     * @description:
     * @author: fengzt
     * @createDate: 2014-10-15
     * @param bean
     * @return:
     */
    @Override
    public RetProcessBean processWorkflow(ParamProcessBean eipmobileparambean) {
        RetProcessBean emrb = new RetProcessBean();

        String flowId = eipmobileparambean.getFlowID();// 用户返回选择id
        String opinion = eipmobileparambean.getOpinion();// 用户填写意见
        String processId = eipmobileparambean.getProcessId();// 流程id

        List<String> nuser = eipmobileparambean.getNextUser();// 下一环节处理人员

        boolean flag = false;
        try {
            UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
            if ( "next".equals( flowId ) ) {// 提交下一环节
                flag = emc.commitToNextLink( userInfo, opinion, nuser, processId );
            } else if ( "rollback".equals( flowId ) ) {// 回退
                flag = emc.returnToPreviousLink( userInfo, opinion, processId );
            } else {// 终止流程
                flag = emc.toStopCurProcess( userInfo, opinion, processId );
            }
            // 若流程提交操作成功，返回成功状态
            if ( flag ) {
                emrb.setRetcode( 1 );
                emrb.setRetmsg( "success" );
            } else {
                emrb.setRetcode( -1 );
            }
        } catch (Exception e) {
            log.error( e.getMessage(), e );
            emrb.setRetcode( -1 );
        }
        return emrb;
    }

}
