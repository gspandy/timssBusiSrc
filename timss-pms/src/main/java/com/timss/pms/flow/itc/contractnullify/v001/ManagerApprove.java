package com.timss.pms.flow.itc.contractnullify.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanTmpService;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.ContractVo;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * @ClassName: ManagerApprove
 * @company: gdyd
 * @Description:合同创建 提出申请节点类
 * @author:    gucw
 * @date:   2015-3-14 
 */
public class ManagerApprove extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	PayplanTmpService payplanTmpService;
	private static final Logger LOGGER=Logger.getLogger(ManagerApprove.class);
	
	public void init(TaskInfo taskInfo) {
	    String processInstId = taskInfo.getProcessInstanceId();
	    String businessId = null!=workflowService.getVariable( processInstId, "businessId" )?workflowService.getVariable( processInstId, "businessId" ).toString():"";
	    ContractDtlVo contractVo = contractService.queryContractById( businessId );
	    if ( contractVo.getTotalSum()>=1000000 ) {
	        workflowService.setVariable( processInstId, "isLarge", "true" );
            }
	};
	public void onComplete(TaskInfo taskInfo) {
	    String processInstId = taskInfo.getProcessInstanceId();
            String isLarge = null!=workflowService.getVariable( processInstId, "isLarge" )?workflowService.getVariable( processInstId, "isLarge" ).toString():"";
            if ( !"true".equals( isLarge ) ) {
                String businessId = null!=workflowService.getVariable( processInstId, "businessId" )?workflowService.getVariable( processInstId, "businessId" ).toString():"";
                contractService.updateContractToVoided(businessId, processInstId);
            }
	    LOGGER.info("oncomplete nullifyContract task "+taskInfo.getTaskDefKey());
	}
}
