package com.timss.pms.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.yudean.mvc.bean.ItcMvcBean;



/**
 * 
 * @ClassName:     Bid
 * @company: gdyd
 * @Description: 招标申请bean
 * @author:    黄晓岚
 * @date:   2014-7-2 下午3:04:07
 */
public class Bid extends ItcMvcBean {

	// Fields

	private Integer bidId;
	private Integer projectId;
	private String name;
	private String code;
	
	private Double budget;
	private String type;

	private String BAttach;
	private String status;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private Boolean delFlag;
	private String siteid;
	private String deptid;
    
	private String command;

	// Constructors

	/** default constructor */
	public Bid() {
	}


	public Integer getBidId() {
		return bidId;
	}


	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}


	public Integer getProjectId() {
		return projectId;
	}


	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public Double getBudget() {
		return budget;
	}


	public void setBudget(Double budget) {
		this.budget = budget;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getBAttach() {
		return BAttach;
	}


	public void setBAttach(String bAttach) {
		BAttach = bAttach;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getCreateUser() {
		return createUser;
	}


	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getUpdateUser() {
		return updateUser;
	}


	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public Boolean getDelFlag() {
		return delFlag;
	}


	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}


	public String getSiteid() {
		return siteid;
	}


	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}


	public String getDeptid() {
		return deptid;
	}


	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	

}