package com.timss.itsm.flow.core.wo.v001;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class DelayAudit  extends TaskHandlerBase{
	
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ItsmWoUtilService woUtilService;
	@Autowired
	private ItsmWorkOrderService itsmWorkOrderService;
	
	private static final Logger LOG = Logger.getLogger(DelayAudit.class);
	
	
	
	public void init(TaskInfo taskInfo){
		 
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String woStatus = "delayAudit";
    	woUtilService.updateBusinessStatus(itsmWorkOrderService, woId, woStatus);
    	
		
	}
	
  public void onComplete(TaskInfo taskInfo){
	  LOG.debug("-------------进入‘延时开始时间’的onComplete(),开始处理业务逻辑-----------------");
	  
	  UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	  String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
	  
	  String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		}
		JSONObject businessDataobj = JSONObject.fromObject(businessData);
		String  attachmentIds = businessDataobj.getString("attachmentIds");
		//操作附件
		try {
			woUtilService.insertAttachMatch(woId, attachmentIds, "WO", "delayAudit");
		}catch (Exception e) {
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
	  woUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService,woId, userInfoScope,"normal");
	  
    	
	}
	    

  
  
	public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
  		
    	woUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService, woId, userInfoScope,"rollback");
    	
  	
	}    
	
	
	
}
