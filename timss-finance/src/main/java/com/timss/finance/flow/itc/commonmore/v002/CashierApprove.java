package com.timss.finance.flow.itc.commonmore.v002;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.service.FinanceMainService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

public class CashierApprove extends TaskHandlerBase {
	
	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	IAuthorizationManager im;
	
	@Autowired
	private FinanceMainService financeMainService;
	
	public void init(TaskInfo taskInfo) {
		
		String fid = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID );
		financeMainService.updateFinanceMainStatusByFid( "wait_cashier_approve", fid );
		
	}
	
	public void onComplete(TaskInfo taskInfo) {
		
		String fid = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID );
		financeMainService.updateFinanceMainStatusByFid( "finance_flow_end", fid );
		
	}
	
}
