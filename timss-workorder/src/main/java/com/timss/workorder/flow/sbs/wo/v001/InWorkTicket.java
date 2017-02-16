package com.timss.workorder.flow.sbs.wo.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.service.WoUtilService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class InWorkTicket extends TaskHandlerBase {
	
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WoUtilService woUtilService;
	
	private static Logger logger = Logger.getLogger(InWorkTicket.class);
	
    public void init(TaskInfo taskInfo){
    	
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String woStatus = "inPTWing";
    	woUtilService.updateWoStatus(woId, woStatus);
    	
	}
    
    public void onComplete(TaskInfo taskInfo){
    	
    	logger.debug("-------------进入‘工作票流程中’的onComplete(),开始处理业务逻辑-----------------");
    
	}
    
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
		
	}
}
