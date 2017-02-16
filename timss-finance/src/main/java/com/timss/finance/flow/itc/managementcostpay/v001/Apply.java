package com.timss.finance.flow.itc.managementcostpay.v001;

import jodd.util.StringUtil;

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

public class Apply extends TaskHandlerBase {
	
	@Autowired
	WorkflowService wfs;
	
	@Autowired
	IAuthorizationManager im;
	
	@Autowired
	private FinanceMainService financeMainService;
	@Autowired
	private FinanceManagementPayService financeManagementPayService;
	
	private static Logger logger = Logger.getLogger(Apply.class);
	
	public void init(TaskInfo taskInfo) {		
		String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID );
		logger.info("初始化Apply流程时id:"+id);
		FinanceManagementPay financeManagementPay= financeManagementPayService.queryFMPtById(id);
		String mainId = financeManagementPay.getMainId();
		if(StringUtil.isNotEmpty(mainId)){
			financeMainService.updateFinanceMainStatusByFid( "fmp_apply", mainId );
		}
	}
	
	public void onComplete(TaskInfo taskInfo) {
		String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID );
		logger.info("完结Apply流程时id:"+id);		
	}
	
}
