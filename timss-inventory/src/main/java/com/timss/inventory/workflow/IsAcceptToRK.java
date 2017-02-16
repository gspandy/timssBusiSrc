package com.timss.inventory.workflow;

import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.service.mutiInstance.MutiInstanceBranchJudgeService;

/**
 * 是否需要自动入库
 * @description: {desc}
 * @company: gdyd
 * @className: IsAcceptToRK.java
 * @author: 890145
 * @createDate: 2015-11-7
 * @updateUser: 890145
 * @version: 1.0
 */
@Service
public class IsAcceptToRK implements MutiInstanceBranchJudgeService{
    @Autowired
    WorkflowService workflowService;
	/* (non-Javadoc)
	 * @see com.yudean.workflow.service.mutiInstance.MutiInstanceBranchJudgeService#isApproval(org.activiti.engine.runtime.Execution, java.lang.String)
	 */
	@Override
	public boolean isApproval(Execution execution, String taskKey) {
		String procssId=execution.getProcessInstanceId();
		String proc_result=(String) workflowService.getVariable(procssId, "PROC_RESULT");
		if("RETURN".equals(proc_result)){
			return false;
		}else{
			return true;
		}
	}

}
