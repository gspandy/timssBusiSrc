package com.timss.finance.flow.itc.carcostmore.v001;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.service.FinanceMainService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

public class ApplyApprove {
	
	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	IAuthorizationManager im;
	
	@Autowired
	private FinanceMainService financeMainService;
	
	public void init(TaskInfo taskInfo) {
		
		String fid = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "fid" );
		financeMainService.updateFinanceMainStatusByFid( "apply_approve", fid );
		
	}
	
	
}
