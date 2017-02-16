package com.timss.workorder.flow.sbs.wo.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.service.WoUtilService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class CommitWo  extends TaskHandlerBase{
	
	@Autowired
	private ItcMvcService itcMvcService;

	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WoUtilService woUtilService;
	
	private static Logger logger = Logger.getLogger(CommitWo.class);
	
	
	
	public void init(TaskInfo taskInfo){
		 
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String woStatus = "newWo";
    	woUtilService.updateWoStatus(woId, woStatus);
	}
	
  public void onComplete(TaskInfo taskInfo){
	  logger.debug("-------------进入‘提交工单’的onComplete(),开始处理业务逻辑-----------------");
	  
	  UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	  Object flag  = itcMvcService.getLocalAttribute("isFromScheduler");  //判断是否从定时任务来
	  String flagString = "no";
	  if(flag != null){
		  flagString = flag.toString();
	  }
	  if(!"yes".equals(flagString)){  //当是定时任务引起的新建工单时，什么都不做，直接返回
		  String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
		  woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"normal");
	  }
	  
	}
	    
	
	
}
