package com.timss.pms.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

public class MilestoneHistory extends ItcMvcBean{

	// Fields
    @AutoGen("PMS_MH_ID")
	private String milestoneHistoryId;
	private Integer projectId;
	private String milestoneHistoryUser;
	private Date time;
	private String operator;
	private String siteid;
	private String delFlag;
	private String content;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public MilestoneHistory() {
	}



	/** full constructor */
	public MilestoneHistory(String milestoneHistoryId,
			Integer projectId, String milestoneHistoryUser,
			Date time, String operator, String siteid, String delFlag,
			String content, String createUser, Date createTime,
			String updateUser, Date updateTime) {
		this.milestoneHistoryId = milestoneHistoryId;
		this.projectId = projectId;
		this.milestoneHistoryUser = milestoneHistoryUser;
		this.time = time;
		this.operator = operator;
		this.siteid = siteid;
		this.delFlag = delFlag;
		this.content = content;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
	}

	public String getMilestoneHistoryUser() {
		return this.milestoneHistoryUser;
	}

	public String getMilestoneHistoryId() {
		return milestoneHistoryId;
	}

	public void setMilestoneHistoryId(String milestoneHistoryId) {
		this.milestoneHistoryId = milestoneHistoryId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setMilestoneHistoryUser(String milestoneHistoryUser) {
		this.milestoneHistoryUser = milestoneHistoryUser;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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