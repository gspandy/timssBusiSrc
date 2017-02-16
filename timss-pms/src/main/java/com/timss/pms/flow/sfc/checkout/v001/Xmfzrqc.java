package com.timss.pms.flow.sfc.checkout.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.bean.Checkout;
import com.timss.pms.service.CheckoutService;
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

public class Xmfzrqc extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("checkoutServiceImpl")
	CheckoutService checkoutService;
	@Autowired
	@Qualifier("checkoutFlowStatusServiceImpl")
	FlowStatusService flowStatusService;
	private static final Logger LOGGER=Logger.getLogger(Xmfzrqc.class);
	
	public void onComplete(TaskInfo taskInfo) {
		
		int businessId=(Integer) workflowService.getVariable(taskInfo.getProcessInstanceId(),"businessId");
		Checkout checkout=GetBeanFromBrowerUtil.getBeanFromBrower("插入的验收信息为", "businessData", Checkout.class, itcMvcService);
		if(checkout!=null){
			checkoutService.tmpUpdateCheckout(checkout);
		}
		checkoutService.updateCheckoutApproving(businessId);
		LOGGER.info("完成验收审批工作流节点"+taskInfo.getTaskDefKey()+"，流程id为"+taskInfo.getProcessInstanceId()+"对验收的更新");
	}
	
	public void init(TaskInfo taskinfo){
		
		FlowStatusUpdateUtil.updateFlowStatus(taskinfo, flowStatusService, workflowService);
    }
	

}
