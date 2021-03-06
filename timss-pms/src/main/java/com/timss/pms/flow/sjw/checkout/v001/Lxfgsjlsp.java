package com.timss.pms.flow.sjw.checkout.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.bean.Checkout;
import com.timss.pms.service.CheckoutService;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * 项目立项 提出申请节点类
 * @ClassName:     Apply
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-9 上午11:42:11
 */
public class Lxfgsjlsp extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("checkoutServiceImpl")
	CheckoutService checkoutService;
	private static final Logger LOGGER=Logger.getLogger(Lxfgsjlsp.class);
	
	public void onComplete(TaskInfo taskInfo) {
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		Checkout checkout=GetBeanFromBrowerUtil.getBeanFromBrower("插入的验收信息为", "businessData", Checkout.class, itcMvcService);
		ChangeStatusUtil.changeSToValue(checkout, ChangeStatusUtil.approvingCode);
		InitUserAndSiteIdUtil.initUpdate(checkout, itcMvcService);
		checkoutService.updateCheckoutApproved(checkout);
		LOGGER.info("完成验收工作流节点"+taskInfo.getTaskDefKey()+"对验收信息的更新");
	}
	

}
