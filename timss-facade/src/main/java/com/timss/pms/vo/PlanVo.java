package com.timss.pms.vo;

import com.timss.pms.bean.Plan;

/**
 * 
 * @ClassName:     PlanVo
 * @company: gdyd
 * @Description: 年度计划bean的vo类
 * @author:    黄晓岚
 * @date:   2014-6-25 上午9:37:05
 */
public class PlanVo extends Plan{
	
	
	private String typeName;
	
	private String statusValue;
	
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

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}
}
