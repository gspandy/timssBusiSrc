package com.timss.pms.bean;

import com.yudean.mvc.bean.ItcMvcBean;
import com.yudean.mvc.bean.userinfo.UserInfo;

/**
 * 流程作废接口参数bean
 * @ClassName:     FlowVoidParamBean
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-12-23 上午10:41:49
 */
public class FlowVoidParamBean extends ItcMvcBean{
	String processInstId;
	String taskId;
	String message;
	String owner;
	String assignee;
	UserInfo userInfo;
	String businessId;
	public FlowVoidParamBean(){};
	public FlowVoidParamBean(String processInstId, String taskId,
			String message, String owner, String assignee, UserInfo userInfo,
			String businessId) {
		super();
		this.processInstId = processInstId;
		this.taskId = taskId;
		this.message = message;
		this.owner = owner;
		this.assignee = assignee;
		this.userInfo = userInfo;
		this.businessId = businessId;
	}
	public String getProcessInstId() {
		return processInstId;
	}
	public void setProcessInstId(String processInstId) {
		this.processInstId = processInstId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	
}
