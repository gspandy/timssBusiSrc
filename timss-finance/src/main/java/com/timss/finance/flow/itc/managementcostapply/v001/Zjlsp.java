package com.timss.finance.flow.itc.managementcostapply.v001;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class Zjlsp extends TaskHandlerBase {
	
	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	FinanceManagementApplySpecialService financeManagementApplySpecialService;
	
	
	public void init(TaskInfo taskInfo) {
		
		String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
		financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus(id, "zjlsp");
		
	}
}
