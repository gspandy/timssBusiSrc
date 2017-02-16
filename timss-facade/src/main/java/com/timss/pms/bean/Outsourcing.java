package com.timss.pms.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * TimPmsOutsourcing entity. @author MyEclipse Persistence Tools
 */

public class Outsourcing extends ItcMvcBean {

	// Fields
	@AutoGen("PMS_OUTSOURCING_ID")
	private String outsourcingId;
	private Integer projectId;
	private String outsourcingName;
	private String outsourcingType;
	private Double price;
	private Long num;
	private String siteid;
	private String delFlag;
	private String command;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public Outsourcing() {
	}

	

	/** full constructor */
	public Outsourcing(String outsourcingId, Integer projectId,
			String outsourcingName, String outsourcingType, Double price,
			Long num, String siteid, String delFlag, String command,
			String createUser, Date createTime, String updateUser,
			Date updateTime) {
		this.outsourcingId = outsourcingId;
		this.projectId = projectId;
		this.outsourcingName = outsourcingName;
		this.outsourcingType = outsourcingType;
		this.price = price;
		this.num = num;
		this.siteid = siteid;
		this.delFlag = delFlag;
		this.command = command;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
	}

	// Property accessors

	public String getOutsourcingId() {
		return this.outsourcingId;
	}

	public void setOutsourcingId(String outsourcingId) {
		this.outsourcingId = outsourcingId;
	}

	

	public Integer getProjectId() {
		return projectId;
	}



	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}



	public String getOutsourcingName() {
		return this.outsourcingName;
	}

	public void setOutsourcingName(String outsourcingName) {
		this.outsourcingName = outsourcingName;
	}

	public String getOutsourcingType() {
		return this.outsourcingType;
	}

	public void setOutsourcingType(String outsourcingType) {
		this.outsourcingType = outsourcingType;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getNum() {
		return this.num;
	}

	public void setNum(Long num) {
		this.num = num;
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