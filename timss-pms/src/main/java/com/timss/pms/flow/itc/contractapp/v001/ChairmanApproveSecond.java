package com.timss.pms.flow.itc.contractapp.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanTmpService;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * @ClassName:ChairmanApproveSecond
 * @company: gdyd
 * @Description:合同创建 提出申请节点类
 * @author:    gucw
 * @date:   2015-3-14 
 */
public class ChairmanApproveSecond extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	PayplanTmpService payplanTmpService;
	private static final Logger LOGGER=Logger.getLogger(ChairmanApproveSecond.class);
	
	public void onComplete(TaskInfo taskInfo) {
		LOGGER.info("完成合同审批工作流节点"+taskInfo.getTaskDefKey()+"董事长审批_SEC");
	}
}
