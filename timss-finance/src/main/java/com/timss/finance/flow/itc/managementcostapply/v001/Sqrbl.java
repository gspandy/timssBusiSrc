package com.timss.finance.flow.itc.managementcostapply.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class Sqrbl extends TaskHandlerBase {
	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	FinanceManagementApplySpecialService financeManagementApplySpecialService;
	
	private Logger logger = Logger.getLogger(Xzbnbhq.class);
	
	public void init(TaskInfo taskInfo) {
		
		String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
		financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus(id, "sqrbl");
		
	}
	
	public void onComplete(TaskInfo taskInfo) {
		String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
		financeManagementApplySpecialService.updateFinanceManagementApplyApproved(id);
		logger.info("完成管理费用申请工作流节点"+taskInfo.getTaskDefKey()+"对信息的更新");
	}
}
