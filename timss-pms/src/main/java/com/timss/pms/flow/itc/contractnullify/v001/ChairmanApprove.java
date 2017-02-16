package com.timss.pms.flow.itc.contractnullify.v001;

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
 * @ClassName: ChairmanApprove
 * @company: gdyd
 * @Description:合同创建 提出申请节点类
 * @author:    gucw
 * @date:   2015-3-14 
 */
public class ChairmanApprove extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	PayplanTmpService payplanTmpService;
	private static final Logger LOGGER=Logger.getLogger(ChairmanApprove.class);
	
	public void onComplete(TaskInfo taskInfo) {
	    //执行作废处理
	    String processInstId = taskInfo.getProcessInstanceId();
            String businessId = null!=workflowService.getVariable( processInstId, "businessId" )?workflowService.getVariable( processInstId, "businessId" ).toString():"";
            contractService.updateContractToVoided(businessId, processInstId);
	    LOGGER.info("oncomplete nullifyContract task "+taskInfo.getTaskDefKey());
	}
}
