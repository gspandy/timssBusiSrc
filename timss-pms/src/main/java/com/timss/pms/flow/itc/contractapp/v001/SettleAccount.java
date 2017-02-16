package com.timss.pms.flow.itc.contractapp.v001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.Payplan;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanTmpService;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.util.JsonUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * @ClassName: SettleAccount
 * @company: gdyd
 * @Description:合同创建 录入结算信息节点类
 * @author:    gucw
 * @date:   2015-3-14 
 */
public class SettleAccount extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	PayplanTmpService payplanTmpService;
	private static final Logger LOGGER=Logger.getLogger(SettleAccount.class);
	
	public void onComplete(TaskInfo taskInfo) {
		Contract contract=GetBeanFromBrowerUtil.getBeanFromBrower("合同结算信息：", "businessData", Contract.class, itcMvcService);
		if(null!=contract){
			ChangeStatusUtil.changeSToValue(contract, ChangeStatusUtil.approvalCode,"App");
			
			UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
			String payPlanString = "";
			List<Payplan> payplans = new ArrayList<Payplan>(0);
			try {
				String businessString = userInfoScope.getParam("businessData");
				HashMap<String,String> map = JsonHelper.fromJsonStringToBean(businessString, HashMap.class);
				payPlanString = map.get("payplans").toString();
				LOGGER.info("payplans 信息为："+payPlanString);
				payplans=JsonUtil.fromJsonStringToList(payPlanString, Payplan.class);
			} catch (Exception e) {
				//本方法不存在throws Exception的声明 如何处理此处的异常？
				LOGGER.warn("获取结算计划信息出现异常",e);
			}
			InitUserAndSiteIdUtil.initUpdate(contract, itcMvcService);
			contractService.updateContractWithWorkFlow(contract, payplans);
		}else{
			String workflowId = taskInfo.getProcessInstanceId();
			int businessId=(Integer)workflowService.getVariable(workflowId,"businessId");
			contractService.updateContractApproving(businessId);
		}
		LOGGER.info("完成合同审批工作流节点"+taskInfo.getTaskDefKey()+"合同结算");
	}
}
