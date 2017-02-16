package com.timss.finance.flow.itc.commonlabourunionone.v001;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.service.FinanceMainService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class LabournionApprove extends TaskHandlerBase {

	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	IAuthorizationManager im;
	@Autowired
	private FinanceMainService financeMainService;
	
	@Override
	public void init(TaskInfo taskInfo) {
		
		String fid = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "fid" );
		financeMainService.updateFinanceMainStatusByFid( "wait_labourunion_approve", fid );
		
	}
	

}
