package com.timss.pms.flow.itc.contract.v001;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanTmpService;
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
public class Apply extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	PayplanTmpService payplanTmpService;
	private static final Logger LOGGER=Logger.getLogger(Apply.class);
	
	public void onComplete(TaskInfo taskInfo) {
		Contract contract=GetBeanFromBrowerUtil.getBeanFromBrower("变更合同信息：", "businessData", Contract.class, itcMvcService);
		if(contract!=null){
			List<PayplanTmp> payplanTmps= getPayplanTmps();
			String flowId=taskInfo.getProcessInstanceId();
			//对提交按钮提交的数据特殊处理
			String notPayplan=getParam("notPayplan");
			if(notPayplan==null){
				payplanTmpService.updatePayplanTmp(payplanTmps, contract, flowId);
			}
		}
		
		
		LOGGER.info("完成结算计划变更工作流节点"+taskInfo.getTaskDefKey()+"对结算计划信息的更新");
	}
	
	private List<PayplanTmp> getPayplanTmps(){
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new PmsBasicException("获取前端的businessData出错",e);
		}
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		JSONArray jsonArray=(JSONArray) jsonObject.get("payplans");
		List<PayplanTmp> lists=JsonUtil.fromJsonStringToList(jsonArray.toString(), PayplanTmp.class);
		return lists;
	}
	
	private String getParam(String param){
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new PmsBasicException("获取前端的businessData出错",e);
		}
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		String result=(String)jsonObject.get(param);
		return result;
	}
	

}
