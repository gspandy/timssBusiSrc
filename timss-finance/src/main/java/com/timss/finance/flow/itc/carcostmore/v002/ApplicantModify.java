package com.timss.finance.flow.itc.carcostmore.v002;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.service.FinanceMainService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

public class ApplicantModify extends TaskHandlerBase {
	
	@Autowired
	WorkflowService wfs; 
	
	@Autowired
	private FinanceMainService financeMainService;
	
	public void init(TaskInfo taskInfo) {
		
		String fid = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID );
		financeMainService.updateFinanceMainStatusByFid( "applicant_modify", fid );
		
	}
	
}
