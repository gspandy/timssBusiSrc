package com.timss.pms.flow.itc.bidresult.v002;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.bean.BidResult;
import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.service.BidResultService;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanService;
import com.timss.pms.service.PayplanTmpService;
import com.timss.pms.util.GetBeanFromBrowerUtil;
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
	@Autowired
	PayplanService payplanService;
	@Autowired
	WorkflowBusinessDao workflowBusinessDao;

	
	@Autowired
	@Qualifier("bidResultServiceImpl")
	BidResultService bidResultService;
	private static final Logger LOGGER=Logger.getLogger(ManagerApprove.class);
	
	public void onComplete(TaskInfo taskInfo) {
		BidResult bidResult=GetBeanFromBrowerUtil.getBeanFromBrower("审批招标结果信息：", "businessData", BidResult.class, itcMvcService);
		if(bidResult!=null){
			bidResultService.updateBidResultApproved(bidResult);
		}else{
			String workflowId=taskInfo.getProcessInstanceId();
			int businessId=(Integer) workflowService.getVariable(workflowId,"businessId");
			bidResultService.updateBidResultApproved(businessId);
		}
		
		LOGGER.info("完成招标结果审批工作流节点"+taskInfo.getTaskDefKey()+"对招标结果的更新");
	}
	

}
