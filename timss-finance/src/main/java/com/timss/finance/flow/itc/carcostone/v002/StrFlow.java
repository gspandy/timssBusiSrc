package com.timss.finance.flow.itc.carcostone.v002;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.service.FinanceMainService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class StrFlow extends TaskHandlerBase {
	
	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	IAuthorizationManager im;
	
	@Autowired
	private FinanceMainService financeMainService;
	
	public void init(TaskInfo taskInfo) {
		
		String pid = taskInfo.getProcessInstanceId();
		List<FinanceMainDetail> mainDtlList = (List<FinanceMainDetail>) wfs.getVariable( pid, "mainDtlList" );
		FinanceMainDetail fmd = mainDtlList.get(0);
		
		String fid = fmd.getFid();
		Map<String, Object> map = financeMainService.queryFinanceMainByFid(fid);
		FinanceMain fm = (FinanceMain) map.get("financeMain");
		
		wfs.setVariable( pid, "amount", fm.getTotal_amount() ); //获取金额以判断会计分支还是总经理分支
		wfs.setVariable( pid, "userId", fmd.getBeneficiaryid() ); //对于他人报销走用户确认时要用到
		
		financeMainService.updateFinanceMainStatusByFid( "finance_flow_str", fid );
		
	}
}
