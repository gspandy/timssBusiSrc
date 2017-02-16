package com.timss.pms.flow.itc.contract.v001;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.Payplan;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.PayplanService;
import com.timss.pms.service.PayplanTmpService;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.timss.pms.util.JsonUtil;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.PayplanTmpVo;
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
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	PayplanTmpService payplanTmpService;
	@Autowired
	PayplanService payplanService;
	@Autowired
	WorkflowBusinessDao workflowBusinessDao;
	private static final Logger LOGGER=Logger.getLogger(Zjlsp.class);
	
	public void onComplete(TaskInfo taskInfo) {
		Contract contract=GetBeanFromBrowerUtil.getBeanFromBrower("变更合同信息：", "businessData", Contract.class, itcMvcService);
		if(contract!=null){
			List<PayplanTmp> payplans= getPayplanTmps();
			payplanService.updatePayplanByPayplanTmp(payplans, contract);
			contractService.updateContractApproved(contract);
			workflowBusinessDao.deleteWorkflwoBusinessByWFId(taskInfo.getProcessInstanceId());
		}else{
			String workflowId=taskInfo.getProcessInstanceId();
			int businessId=(Integer) workflowService.getVariable(workflowId,"businessId");
			List<PayplanTmpVo> payplanTmps=payplanTmpService.queryPayplanTmpByFlowId(workflowId);
			ContractDtlVo contractDtlVo=contractService.queryContractById(String.valueOf(businessId));
			payplanService.updatePayplanByPayplanTmp(convertToPayplanTmps(payplanTmps), contractDtlVo);
			contractService.updateContractApproved(contractDtlVo);
			workflowBusinessDao.deleteWorkflwoBusinessByWFId(taskInfo.getProcessInstanceId());
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
	
	private List<Payplan> getPayplans(){
		UserInfoScope userInfoScope=itcMvcService.getUserInfoScopeDatas();
		String businessDataString=null;
		try {
			businessDataString=userInfoScope.getParam("businessData");
		} catch (Exception e) {
			throw new PmsBasicException("获取前端的businessData出错",e);
		}
		JSONObject jsonObject=JSONObject.fromObject(businessDataString);
		JSONArray jsonArray=(JSONArray) jsonObject.get("payplans");
		List<Payplan> lists=JsonUtil.fromJsonStringToList(jsonArray.toString(), Payplan.class);
		return lists;
	}
	
	private List<PayplanTmp> convertToPayplanTmps(List<PayplanTmpVo> payplanTmpVos){
		List<PayplanTmp> list=new ArrayList<PayplanTmp>();
		for(int i=0;i<payplanTmpVos.size();i++){
			PayplanTmp payplanTmp=payplanTmpVos.get(i);
			list.add(payplanTmp);
		}
		return list;
	}
	

}
