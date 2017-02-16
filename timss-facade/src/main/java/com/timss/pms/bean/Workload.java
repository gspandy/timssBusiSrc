package com.timss.pms.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * TimPmsWorkload entity. @author MyEclipse Persistence Tools
 */

public class Workload extends ItcMvcBean{

	// Fields
	@AutoGen("PMS_WORKLOAD_ID")
	private String workloadId;
	private Integer projectId;
	private String userLevel;
	private Double workloadValue;
	private String siteid;
	private String delFlag;
	private String command;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public Workload() {
	}

	

	/** full constructor */
	public Workload(String workloadId, Integer projectId,
			String userLevel, Double workloadValue, String siteid,
			String delFlag, String command, String createUser, Date createTime,
			String updateUser, Date updateTime) {
		this.workloadId = workloadId;
		this.projectId = projectId;
		this.userLevel = userLevel;
		this.workloadValue = workloadValue;
		this.siteid = siteid;
		this.delFlag = delFlag;
		this.command = command;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
	}

	// Property accessors

	public String getWorkloadId() {
		return this.workloadId;
	}

	public void setWorkloadId(String workloadId) {
		this.workloadId = workloadId;
	}

	
	public Integer getProjectId() {
		return projectId;
	}



	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}



	public String getUserLevel() {
		return this.userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public Double getWorkloadValue() {
		return this.workloadValue;
	}

	public void setWorkloadValue(Double workloadValue) {
		this.workloadValue = workloadValue;
	}

	public String getSiteid() {
		return this.siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}