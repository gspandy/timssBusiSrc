package com.timss.pms.flow.itc.contractapp.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.bean.Contract;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanTmpService;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * @ClassName:     AccountManagerApply
 * @company: gdyd
 * @Description:合同创建 提出申请节点类
 * @author:    gucw
 * @date:   2015-3-14 
 */
public class AccountManagerApply extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	PayplanTmpService payplanTmpService;
	private static final Logger LOGGER=Logger.getLogger(AccountManagerApply.class);
	
	public void init(TaskInfo taskInfo){
	}
	public void onComplete(TaskInfo taskInfo) {
		Contract contract=GetBeanFromBrowerUtil.getBeanFromBrower("客户经理申请合同信息：", "businessData", Contract.class, itcMvcService);
		if(null!=contract){
			//合同审批状态设置为 审批中
			contractService.updateContractApprovingWithSuffix(contract,"App");
			//提交的时候，就判断是否超过百万元
			if(1000000>=contract.getTotalSum()){
				workflowService.setVariable(taskInfo.getProcessInstanceId(), "isOverHundred", "N");
			}else{
				workflowService.setVariable(taskInfo.getProcessInstanceId(), "isOverHundred", "Y");
			}
		}else{
			String workflowId = taskInfo.getProcessInstanceId();
			int businessId=(Integer)workflowService.getVariable(workflowId,"businessId");
			contractService.updateContractApproving(businessId);
		}
		LOGGER.info("完成合同审批工作流节点"+taskInfo.getTaskDefKey()+"客户经理提交");
	}
}
