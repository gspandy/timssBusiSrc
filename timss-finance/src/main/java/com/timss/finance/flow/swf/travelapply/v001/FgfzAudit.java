package com.timss.finance.flow.swf.travelapply.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
/*
 * 分管副总审批
 */
public class FgfzAudit extends TaskHandlerBase {
    @Autowired
    private WorkflowService wfs; // by type
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    @Qualifier("FinanceManagementApplySpecialServiceImpl")
    private FinanceManagementApplySpecialService financeManagementApplySpecialService;
    
    
    private Logger logger = Logger.getLogger( FgfzAudit.class );

    public void init(TaskInfo taskInfo) {
        String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
        financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus( id, "fgfzsp" );
    }

    public void onComplete(TaskInfo taskInfo) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        financeManagementApplySpecialService.updateFinApplyCurrHandlerUser( id, userInfoScope, "normal" );
    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        logger.debug( "-------------进入‘部门经理审批回退’的beforeRollBack(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        financeManagementApplySpecialService.updateFinApplyCurrHandlerUser( id, userInfoScope, "rollback" );

    }
    
   
}
