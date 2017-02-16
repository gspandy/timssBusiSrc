package com.timss.itsm.flow.dpp.infowo.v001;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.service.ItsmInfoWoService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class ApplicantValidation extends TaskHandlerBase {

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService itsmWoUtilService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmInfoWoService itsmInfoWoService;
    
    private static final Logger LOG = Logger.getLogger( ApplicantValidation.class );

    public void init(TaskInfo taskInfo) {

        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "applicantValidation";
        itsmWoUtilService.updateBusinessStatus(itsmInfoWoService, infoWoId, woStatus );
    }

    public void onComplete(TaskInfo taskInfo) {
        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        itsmWoUtilService.clearBusinessCurrHandlerUser( itsmInfoWoService, infoWoId );
        String woStatus = "end";
        itsmWoUtilService.updateBusinessStatus(itsmInfoWoService, infoWoId, woStatus );

    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        workflowService.setVariable( taskInfo.getProcessInstanceId(), "rollbackFlag", "Y" );
        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmInfoWoService, infoWoId, userInfoScope, "rollback" );
    }

}
