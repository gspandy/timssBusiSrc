package com.timss.finance.flow.swf.travelapply.v001;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.finance.dao.FinanceManagementApplyDao;
import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
/*
 * 出差后续办理
 */
public class OtherleaderAudit extends TaskHandlerBase {
	@Autowired
	private WorkflowService wfs; //by type
	@Autowired
    private ItcMvcService itcMvcService;
	@Autowired
	FinanceManagementApplyDao financeManagementApplyDao;
	@Autowired
	@Qualifier("FinanceManagementApplySpecialServiceImpl")
	private FinanceManagementApplySpecialService financeManagementApplySpecialService;
	
	private static Logger logger = Logger.getLogger(OtherleaderAudit.class);
	
	public void init(TaskInfo taskInfo) {
		String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
		financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus(id, "qtldsp");
	}
	
	 public void onComplete(TaskInfo taskInfo) {
	     String id = wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
         Map<String, String> parmas = new HashMap<String, String>();
         parmas.put("id", id);
         parmas.put("currHandlerUser", "");
         parmas.put("currHandUserName", "");
         financeManagementApplyDao.updateCurrHandUserById(parmas);
         
         financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus(id,"AE");
         
         financeManagementApplySpecialService.updateFinanceManagementApplyApproved(id); 
         }

         public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
             logger.debug( "-------------进入‘总经理审批回退’的beforeRollBack(),开始处理业务逻辑-----------------" );
             UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
             String id = wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
             financeManagementApplySpecialService.updateFinApplyCurrHandlerUser( id, userInfoScope, "rollback" );

         }
}
