package com.timss.itsm.flow.dpp.infowo.v001;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.bean.ItsmInfoWo;
import com.timss.itsm.service.ItsmInfoWoService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class Apply extends TaskHandlerBase {

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService itsmWoUtilService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmInfoWoService itsmInfoWoService;
    
    private static final Logger LOG = Logger.getLogger( Apply.class );

    public void init(TaskInfo taskInfo) {
        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String rollbackFlag = workflowService.getVariable( taskInfo.getProcessInstanceId(), "rollbackFlag" ).toString();
       if( "Y".equals( rollbackFlag )){
           String woStatus = "newApply";
           itsmWoUtilService.updateBusinessStatus(itsmInfoWoService, infoWoId, woStatus );
       }
       workflowService.setVariable( taskInfo.getProcessInstanceId(), "rollbackFlag", "N" );
    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘信息工单填写申请’的onComplete(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "newApply";
        itsmWoUtilService.updateBusinessStatus(itsmInfoWoService, infoWoId, woStatus );
        ItsmInfoWo infoWo = itsmInfoWoService.queryItsmInfoWoById( infoWoId );
        //更新申请时间，如果是退回的单则不更新
        if(infoWo.getApplyTime()==null){
            infoWo.setApplyTime( new Date() );
            try {
                itsmInfoWoService.updateItsmInfoWo( infoWo );
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException( e );
            }
        }
        
        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject businessDataobj = JSONObject.fromObject( businessData );
        String attachmentIds = businessDataobj.getString( "attachmentIds" );
        String infoWoFormDate = businessDataobj.getString( "infoWoForm" );
        
        try {
            ItsmInfoWo itsmInfoWo = JsonHelper.toObject( infoWoFormDate, ItsmInfoWo.class );
            itsmInfoWoService.updateItsmInfoWo(itsmInfoWo);
        } catch (Exception e1) {
            LOG.error( e1.getMessage() );
            e1.printStackTrace();
            throw new RuntimeException( e1 );
        } 
        
        //附件添加
        try {
            itsmWoUtilService.insertAttachMatch( infoWoId, attachmentIds,"INFOWO", "newApply" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            e.printStackTrace();
            throw new RuntimeException( e );
        }

        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmInfoWoService, infoWoId, userInfoScope, "normal" );
        
        LOG.debug( "-------------进入‘信息工单填写申请’的onComplete(),业务逻辑处理结束-----------------" );

    }

}
