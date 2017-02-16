package com.timss.pms.flow.itc.contractapp.v001;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.bean.Contract;
import com.timss.pms.service.ContractService;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

public class RelatedDepartmentCountersign extends TaskHandlerBase {
	
	private static final Logger LOGGER=Logger.getLogger(RelatedDepartmentCountersign.class);
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	
	public void init(TaskInfo taskInfo){
		List<String> userIds = Collections.emptyList();
		try{
			userIds = (List<String>)workflowService.getVariable(taskInfo.getProcessInstanceId(), WorkFlowConstants.CURRENT_TASK_CANDIDATE_USER);
			if(userIds == null){
	        	userIds =  Collections.emptyList();
	        } 
		}catch(Exception e){
			LOGGER.warn("获取节点候选人异常",e);
		}
        workflowService.setVariable(taskInfo.getProcessInstanceId(), "counterSignUsers", userIds);
	}
	
	public void onComplete(TaskInfo taskInfo) {
		
		LOGGER.info("完成合同审批工作流节点"+taskInfo.getTaskDefKey()+"相关部门会签");
	}

}
