package com.timss.itsm.flow.dpp.infowo.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.service.ItsmInfoWoService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class ApplicantReturn extends TaskHandlerBase {

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService itsmWoUtilService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmInfoWoService itsmInfoWoService;
    
    private static final Logger LOG = Logger.getLogger( ApplicantReturn.class );

    public void init(TaskInfo taskInfo) {

        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "applicantReturn";
        itsmWoUtilService.updateBusinessStatus(itsmInfoWoService, infoWoId, woStatus );
    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘管理员审批’的onComplete(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmInfoWoService, infoWoId, userInfoScope, "normal" );
        LOG.debug( "-------------进入‘管理员审批’的onComplete(),业务逻辑处理结束-----------------" );

    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        workflowService.setVariable( taskInfo.getProcessInstanceId(), "rollbackFlag", "Y" );
        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmInfoWoService, infoWoId, userInfoScope, "rollback" );
    }

}
