package com.timss.workorder.flow.sbs.wo.v001;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoUtilService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class AssistantToTeamLeader extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WoUtilService woUtilService;
	
	private static Logger logger = Logger.getLogger(AssistantToTeamLeader.class);
	
    public void init(TaskInfo taskInfo){
    	
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String woStatus = "assistantToTeamleader";
    	woUtilService.updateWoStatus(woId, woStatus);
    	
    	
	}
    
    public void onComplete(TaskInfo taskInfo){
    	
    	logger.debug("-------------进入‘助理指派负责人’的onComplete(),开始处理业务逻辑-----------------");
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	
    	//修改当前执行人
    	woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"normal");
   	
    	
	}
    
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	//修改当前执行人
		try {
				String userId = userInfoScope.getParam("userId");
				HashMap<String, String> parmas = new HashMap<String, String>();
				String nextAuditUserNames = itcMvcService.getUserInfoById(userId).getUserName();
				parmas.put("woId", woId);
				parmas.put("currHandlerUser", userId);
				parmas.put("currHandUserName", nextAuditUserNames);
				workOrderDao.updateCurrHandUserById(parmas);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
