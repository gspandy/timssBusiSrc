package com.timss.inventory.workflow;

import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.service.mutiInstance.MutiInstanceBranchJudgeService;

/**
 * 判断是否需要专家验收
 * @description: {desc}
 * @company: gdyd
 * @className: IsAcceptToZJYS.java
 * @author: 890145
 * @createDate: 2015-11-7
 * @updateUser: 890145
 * @version: 1.0
 */
@Service
public class IsAcceptToZJYS implements MutiInstanceBranchJudgeService {
    @Autowired
    WorkflowService workflowService;
	/* (non-Javadoc)
	 * @see com.yudean.workflow.service.mutiInstance.MutiInstanceBranchJudgeService#isApproval(org.activiti.engine.runtime.Execution, java.lang.String)
	 */
	@Override
	public boolean isApproval(Execution execution, String taskKey) {
		String procssId=execution.getProcessInstanceId();
		String sp_meta=(String) workflowService.getVariable(procssId, "SP_MATERIAL");
		if("NONE".equals(sp_meta)){
			return false;
		}else{
			return true;
		}
	}

}
