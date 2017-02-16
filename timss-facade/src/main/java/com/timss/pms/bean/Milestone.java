package com.timss.pms.bean;

import java.util.Date;



import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * TimPmsMilestone entity. @author MyEclipse Persistence Tools
 */

public class Milestone extends ItcMvcBean {

	// Fields
    @AutoGen("PMS_MILESTONE_ID")
	private String milestoneId;
	private Integer projectId;
	private String milestoneName;
	private Date expectedTime;
	private Date actualTime;
	private String siteid;
	private String delFlag;
	private String command;
	private Date createTime;
	private String createUser;
	private Date updateTime;
	private String updateUser;
	private Date originTime;

	// Constructors

	/** default constructor */
	public Milestone() {
	}

	

	/** full constructor */
	public Milestone(String milestoneId, Integer timPmsProject,
			String milestoneName, Date expectedTime, Date actualTime,
			String siteid, String delFlag, String command, Date createTime,
			String createUser, Date updateTime, String updateUser) {
		this.milestoneId = milestoneId;
		this.projectId = projectId;
		this.milestoneName = milestoneName;
		this.expectedTime = expectedTime;
		this.actualTime = actualTime;
		this.siteid = siteid;
		this.delFlag = delFlag;
		this.command = command;
		this.createTime = createTime;
		this.createUser = createUser;
		this.updateTime = updateTime;
		this.updateUser = updateUser;
	}

	// Property accessors

	

	public String getMilestoneName() {
		return this.milestoneName;
	}

	public String getMilestoneId() {
		return milestoneId;
	}



	public void setMilestoneId(String milestoneId) {
		this.milestoneId = milestoneId;
	}



	public Integer getProjectId() {
		return projectId;
	}



	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}



	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
	}

	public Date getExpectedTime() {
		return this.expectedTime;
	}

	public void setExpectedTime(Date expectedTime) {
		this.expectedTime = expectedTime;
	}

	public Date getActualTime() {
		return this.actualTime;
	}

	public void setActualTime(Date actualTime) {
		this.actualTime = actualTime;
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

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}



	public Date getOriginTime() {
		return originTime;
	}



	public void setOriginTime(Date originTime) {
		this.originTime = originTime;
	}
	
	

}