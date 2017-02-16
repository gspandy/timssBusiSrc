package com.timss.finance.flow.itc.commondeptchooseone.v002;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.service.FinanceMainService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class MainAccountingApprove extends TaskHandlerBase{
	
	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	IAuthorizationManager im;
	
	@Autowired
	private FinanceMainService financeMainService;
	
	public void init(TaskInfo taskInfo) {
		
		String fid = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "fid" );
		financeMainService.updateFinanceMainStatusByFid( "main_accounting_approve", fid );
		
	}

}
