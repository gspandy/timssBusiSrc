package com.timss.pms.flow.sfc.bidresult.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.bean.BidResult;
import com.timss.pms.service.BidResultService;
import com.timss.pms.service.FlowStatusService;
import com.timss.pms.util.FlowStatusUpdateUtil;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * 招标结果审批 提出申请节点类
 * @ClassName:     Apply
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-7-9 上午11:42:11
 */

public class Ybsq extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("bidResultServiceImpl")
	BidResultService bidResultService;
	@Autowired
	@Qualifier("bidResultFlowStatusServiceImpl")
	FlowStatusService flowStatusService;
	private static final Logger LOGGER=Logger.getLogger(Ybsq.class);
	
	public void onComplete(TaskInfo taskInfo) {
		int businessId=(Integer) workflowService.getVariable(taskInfo.getProcessInstanceId(),"businessId");
		BidResult bidResult=GetBeanFromBrowerUtil.getBeanFromBrower("审批招标结果信息：", "businessData", BidResult.class, itcMvcService);
		//如果bidResult不为空，则先保存
		if(bidResult!=null){
			bidResultService.tmpUpdateBidResult(bidResult);
		}
		bidResultService.updateBidResultApproving(businessId);
		
		
		
		LOGGER.info("完成招标结果审批工作流节点"+taskInfo.getTaskDefKey()+"，流程id为"+taskInfo.getProcessInstanceId()+"对招标结果的更新");
	}
	
	public void init(TaskInfo taskinfo){
		
		FlowStatusUpdateUtil.updateFlowStatus(taskinfo, flowStatusService, workflowService);
    }
	

}
