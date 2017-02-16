package com.timss.pms.flow.itc.receipt.v004;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.bean.Pay;
import com.timss.pms.service.PayService;
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
public class Apply extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	PayService payService;
	private static final Logger LOGGER=Logger.getLogger(Apply.class);
	
	public void onComplete(TaskInfo taskInfo) {
		UserInfoScope userInfoScope=(UserInfoScope) itcMvcService.getUserInfoScopeDatas();
		Pay pay=GetBeanFromBrowerUtil.getBeanFromBrower("插入的收款信息为", "businessData", Pay.class, itcMvcService);
		if(pay!=null){
			ChangeStatusUtil.changeSToValue(pay, ChangeStatusUtil.approvingCode);
			InitUserAndSiteIdUtil.initUpdate(pay, itcMvcService);
			payService.updatePayApproving(pay);
		}else{
			String workflowId=taskInfo.getProcessInstanceId();
			int businessId=(Integer) workflowService.getVariable(workflowId,"businessId");
			payService.updatePayApproving(businessId);
		}
		
		LOGGER.info("完成项目收款工作流节点"+taskInfo.getTaskDefKey()+"对收款信息的更新");
	}
	

}
