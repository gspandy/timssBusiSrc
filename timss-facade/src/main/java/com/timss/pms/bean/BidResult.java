package com.timss.pms.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * TimPmsBidResult entity. @author MyEclipse Persistence Tools
 */

public class BidResult extends ItcMvcBean {

	// Fields

	private Integer bidResultId;
	private Integer bidId;
	private String attach;
	private String command;
	private String status;
	private Boolean delFlag;
	private String siteid;
	private String deptid;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private Integer projectId;
	private String type;
	private Double budget;
	private String supplier;
	private String name;
	private String code;
	private String supplierId;
	
	private String needFZSH;
	
	private String projectName;
	private String supplier2Name;
	private String supplier2Id;
	private String flowId;
	private String processInstId;
	
	private String flowStatus;

	// Constructors

	/** default constructor */
	public BidResult() {
	}

	/** minimal constructor */
	public BidResult(Integer bidResultId) {
		this.bidResultId = bidResultId;
	}

	/** full constructor */
	public BidResult(Integer bidResultId, 
			String attach, String command, String status, Boolean delFlag,
			String siteid, String deptid, String createUser, Date createTime,
			String updateUser, Date updateTime, Integer projectId, String type,
			Double budget, String supplier, String name) {
		this.bidResultId = bidResultId;
	
		this.attach = attach;
		this.command = command;
		this.status = status;
		this.delFlag = delFlag;
		this.siteid = siteid;
		this.deptid = deptid;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.projectId = projectId;
		this.type = type;
		this.budget = budget;
		this.supplier = supplier;
		this.name = name;
	}

	// Property accessors

	public Integer getBidResultId() {
		return this.bidResultId;
	}

	public void setBidResultId(Integer bidResultId) {
		this.bidResultId = bidResultId;
	}

	public Integer getBidId() {
		return this.bidId;
	}

	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}

	public String getAttach() {
		return this.attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public String getSiteid() {
		return this.siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getDeptid() {
		return this.deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
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

	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getBudget() {
		return this.budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getName() {
		return this.name;
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

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getNeedFZSH() {
		return needFZSH;
	}

	public void setNeedFZSH(String needFZSH) {
		this.needFZSH = needFZSH;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSupplier2Name() {
		return supplier2Name;
	}

	public void setSupplier2Name(String supplier2Name) {
		this.supplier2Name = supplier2Name;
	}

	public String getSupplier2Id() {
		return supplier2Id;
	}

	public void setSupplier2Id(String supplier2Id) {
		this.supplier2Id = supplier2Id;
	}

	
	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getProcessInstId() {
		return processInstId;
	}

	public void setProcessInstId(String processInstId) {
		this.processInstId = processInstId;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	
	

}