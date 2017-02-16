package com.timss.finance.flow.itc.managementcostpay.v001;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.bean.FinanceManagementPay;
import com.timss.finance.service.FinanceMainService;
import com.timss.finance.service.FinanceManagementPayService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;
public class AccountingApprove extends TaskHandlerBase {

	
	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	IAuthorizationManager im;
	
	@Autowired
	private FinanceMainService financeMainService;
	
	@Autowired
	private FinanceManagementPayService financeManagementPayService;
	
	private Logger logger = Logger.getLogger(AccountingApprove.class);
	
	public void init(TaskInfo taskInfo) {
		
		// 预留环节初始化时执行的方法
		String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID );
		logger.info("开始CashierPay流程时id:"+id);
		financeManagementPayService.updateFMPApproving(id);
		FinanceManagementPay financeManagementPay= financeManagementPayService.queryFMPtById(id);
		String mainId = financeManagementPay.getMainId();
		if(StringUtils.isNotEmpty(mainId)){
			financeMainService.updateFinanceMainStatusByFid( "fmp_accounting_approve", mainId );
		}
		
	}
	
}
