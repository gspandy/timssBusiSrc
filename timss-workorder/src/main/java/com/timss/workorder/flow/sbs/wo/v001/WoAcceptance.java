package com.timss.workorder.flow.sbs.wo.v001;

import java.util.Date;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoUtilService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class WoAcceptance extends TaskHandlerBase {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WoUtilService woUtilService;
	
	private static Logger logger = Logger.getLogger(WoAcceptance.class);
	
    public void init(TaskInfo taskInfo){
    	
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String woStatus = "woAcceptance";
    	woUtilService.updateWoStatus(woId, woStatus);
    	
	}
    
    public void onComplete(TaskInfo taskInfo){
    	
    	logger.debug("-------------进入‘工单验收’的onComplete(),开始处理业务逻辑-----------------");
    	
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	
    	
    	String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		//TODO 获取验收时的批准停机时间，损失电量，然后去修改数据库中的工单表信息
		JSONObject businessDataObj = JSONObject.fromObject(businessData);
		String  woAcceptanceFormData = businessDataObj.getString("woAcceptanceForm");
		JSONObject woAcceptanceFormObj = JSONObject.fromObject(woAcceptanceFormData);
		
		HashMap<String, Object> parmas = new HashMap<String, Object>();
		parmas.put("approveStopTime", woAcceptanceFormObj.get("approveStopTime"));   
		parmas.put("loseElectricPower", woAcceptanceFormObj.get("loseElectricPower"));
		parmas.put("id", Integer.valueOf(woId));  
		parmas.put("modifyuser", userId);
		parmas.put("modifydate", new Date());
		
		workOrderDao.updateWOOnAcceptance(parmas);
		
		HashMap<String, Object> parmas2 = new HashMap<String, Object>();
		parmas2.put("woStatus", "woFiling");
		parmas2.put("id", Integer.valueOf(woId));
		parmas2.put("modifydate", new Date());
		//修改工单的状态
		workOrderDao.updateWOStatus(parmas2);
		
    	//修改验收人信息
    	HashMap<String, String> parmas1 = new HashMap<String, String>();
		parmas1.put("woId", woId);
		parmas1.put("acceptanceUser", userId);
		workOrderDao.updateOperUserById(parmas1);
		
		//回访完之后，进入已结束状态，需要清空当前处理人信息
		woUtilService.clearWoCurrHandlerUser(woId);
		
	}
    
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	//修改当前执行人
		try {
				String userId = userInfoScope.getParam("userId");
				HashMap<String, String> parmas = new HashMap<String, String>();
				parmas.put("woId", woId);
				parmas.put("currHandlerUser", userId);
				workOrderDao.updateOperUserById(parmas);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
