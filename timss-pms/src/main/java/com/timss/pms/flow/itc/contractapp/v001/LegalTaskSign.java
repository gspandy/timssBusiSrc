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

public class LegalTaskSign extends  TaskHandlerBase {
	
	private static final Logger LOGGER=Logger.getLogger(RelatedDepartmentCountersign.class);
	@Autowired
	WorkflowService workflowService;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	
	public void init(TaskInfo taskInfo){
		List<String> userIds=Collections.emptyList();
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
	public void onComplete(TaskInfo taskInfo){
		Contract contract=GetBeanFromBrowerUtil.getBeanFromBrower("变更合同信息：", "businessData", Contract.class, itcMvcService);
		if(contract!=null){
			contractService.saveOrUpdateContractAttach(contract);
		}
		LOGGER.info("完成结算计划变更工作流节点"+taskInfo.getTaskDefKey()+"对结算计划信息的更新");
	}
		
	

}
