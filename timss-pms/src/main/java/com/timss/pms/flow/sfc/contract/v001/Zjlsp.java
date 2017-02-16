package com.timss.pms.flow.sfc.contract.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.service.ContractService;
import com.timss.pms.service.FlowStatusService;
import com.timss.pms.util.FlowStatusUpdateUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * 项目立项 提出申请节点类
 * @ClassName:     Apply
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-7-9 上午11:42:11
 */
public class Zjlsp extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;

	@Autowired
	@Qualifier("contractFlowStatusServiceImpl")
	FlowStatusService flowStatusService;

	private static final Logger LOGGER=Logger.getLogger(Zjlsp.class);
	
	public void onComplete(TaskInfo taskInfo) {
		int businessId=(Integer) workflowService.getVariable(taskInfo.getProcessInstanceId(),"businessId");
		contractService.updateContractApproved(businessId);
		LOGGER.info("完成合同审批工作流节点"+taskInfo.getTaskDefKey()+"，流程id为"+taskInfo.getProcessInstanceId()+"对合同的更新");
	}
	
	
	public void init(TaskInfo taskinfo){
		
		FlowStatusUpdateUtil.updateFlowStatus(taskinfo, flowStatusService, workflowService);
    }

}
