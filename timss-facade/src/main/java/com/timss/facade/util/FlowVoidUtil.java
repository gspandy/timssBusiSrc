package com.timss.facade.util;

import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.timss.pms.bean.FlowVoidParamBean;
import com.yudean.homepage.service.HomepageService;
import com.yudean.workflow.service.WorkflowService;

//流程作废辅助类
public class FlowVoidUtil {
	private WorkflowService workflowService;
	private HomepageService homepageService;
	
	private String processInstId;
	
	Logger logger=Logger.getLogger(FlowVoidUtil.class);
	public FlowVoidUtil(WorkflowService workflowService,
			HomepageService homepageService) {
		this.workflowService = workflowService;
		this.homepageService = homepageService;
	}
	
	/**
	 * 删除流程，并且将待办变为办毕
	 * @Title: initFlowVoid
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * 
	 * @param params
	 * @return
	 */
	public boolean initFlowVoid(FlowVoidParamBean params){
		String taskId=params.getTaskId();
		if(StringUtils.isBlank(taskId)){
			logger.warn("作废流程时没有找到taskId");
			return false;
		}
		processInstId=params.getProcessInstId();
		if(StringUtils.isBlank(processInstId)){
			Task task=workflowService.getTaskByTaskId(taskId);
			processInstId=task.getProcessInstanceId();
		}
		
		String message=params.getMessage();
		//设置message的默认值
		if(StringUtils.isBlank(message)){
		      message="作废";
		}
		logger.info("作废流程"+processInstId);
		//终止流程
		workflowService.stopProcess(taskId,params.getAssignee(),params.getOwner(),message);
		//首页信息变为办毕
		homepageService.complete(processInstId,params.getUserInfo() , "已作废");
		return true;
	}

	public String getProcessInstId() {
		return processInstId;
	}

	
}
