package com.timss.finance.flow.itc.commondeptchooseone.v002;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.service.FinanceMainService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class AccountingApprove extends TaskHandlerBase {
	
	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	IAuthorizationManager im;
	
	@Autowired
	private FinanceMainService financeMainService;
	
	private Logger logger = Logger.getLogger(StrFlow.class);
	
	public void init(TaskInfo taskInfo) {
	    logger.info( "------------------AccountingApprove.init() begin---------------" );
	    String fid = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "fid" );
	    financeMainService.updateFinanceMainStatusByFid( "wait_accounting_approve", fid );
		
	}
	
}
