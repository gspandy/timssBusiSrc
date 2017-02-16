package com.timss.pms.vo;

import org.codehaus.jackson.map.jsontype.impl.TypeNameIdResolver;

import com.timss.pms.bean.BidResult;

public class BidResultVo extends BidResult{
	private String typeName;
	
	private String  statusName;
	
	private String projectName;
	
	private String flowid;
	
	
	public String getFlowid() {
		return flowid;
	}
	public void setFlowid(String flowid) {
		this.flowid = flowid;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	
}
