package com.timss.pms.flow.sjw.contractapp.v002;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.Payplan;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanService;
import com.timss.pms.service.ProjectService;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.timss.pms.util.JsonUtil;
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
public class Zjlsp extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	ProjectService projectService;
	@Autowired
	PayplanService payplanService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	private static final Logger LOGGER=Logger.getLogger(Zjlsp.class);
	
	public void onComplete(TaskInfo taskInfo) {
		Contract contract=GetBeanFromBrowerUtil.getBeanFromBrower("合同信息：", "businessData", Contract.class, itcMvcService);
		if(contract!=null){
			//合同审批状态设置为 审批完成
                        contractService.updateContractApprovedWithSuffix(contract,"App");
			List<Payplan> payplans= getPayplans();
			String flowId=taskInfo.getProcessInstanceId();
			//对提交按钮提交的数据特殊处理
			payplanService.updatePayplan(payplans, contract);
			
		}
		LOGGER.info("完成合同审批工作流节点"+taskInfo.getTaskDefKey()+"对合同信息的更新");
	}
	
	
	
	private List<Payplan> getPayplans(){
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new PmsBasicException("获取前端的businessData出错",e);
		}
		JSONObject jsonObject=JSONObject.parseObject(businessDataString);
		JSONArray jsonArray=JSONArray.parseArray( String.valueOf( jsonObject.get("payplans") ) ) ;
		List<Payplan> lists=JsonUtil.fromJsonStringToList(jsonArray.toString(), Payplan.class);
		return lists;
	}

}
