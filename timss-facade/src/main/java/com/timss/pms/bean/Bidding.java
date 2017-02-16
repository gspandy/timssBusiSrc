package com.timss.pms.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * TimPmsBidding entity. @author MyEclipse Persistence Tools
 */

public class Bidding extends ItcMvcBean {

	// Fields
	@AutoGen("PMS_BIDDING_ID")
	private String id;
	private String siteid;
	private String deptid;
	private String delFlag;
	private String BName;
	private String projectId;
	private Double budget;
	private String description;
	private String attach;
	private String createuser;
	private Date createdate;
	private String modifyuser;
	private Date modifydate;
	private String status;
	private String flowStatus;
	private String isGood;
	private Double price;
	private String resultDes;
	private String resultAttach;

	// Constructors

	/** default constructor */
	public Bidding() {
	}

	/** minimal constructor */
	public Bidding(String id) {
		this.id = id;
	}

	/** full constructor */
	public Bidding(String id, String siteid, String deptid,
			String delFlag, String BName, String projectId, Double budget,
			String description, String attach, String createuser,
			Date createdate, String modifyuser, Date modifydate, String status,
			String flowStatus, String isGood, Double price, String resultDes) {
		this.id = id;
		this.siteid = siteid;
		this.deptid = deptid;
		this.delFlag = delFlag;
		this.BName = BName;
		this.projectId = projectId;
		this.budget = budget;
		this.description = description;
		this.attach = attach;
		this.createuser = createuser;
		this.createdate = createdate;
		this.modifyuser = modifyuser;
		this.modifydate = modifydate;
		this.status = status;
		this.flowStatus = flowStatus;
		this.isGood = isGood;
		this.price = price;
		this.resultDes = resultDes;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getBName() {
		return this.BName;
	}

	public void setBName(String BName) {
		this.BName = BName;
	}

	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Double getBudget() {
		return this.budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAttach() {
		return this.attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getModifyuser() {
		return this.modifyuser;
	}

	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}

	public Date getModifydate() {
		return this.modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFlowStatus() {
		return this.flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getIsGood() {
		return this.isGood;
	}

	public void setIsGood(String isGood) {
		this.isGood = isGood;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getResultDes() {
		return this.resultDes;
	}

	public void setResultDes(String resultDes) {
		this.resultDes = resultDes;
	}

	public String getResultAttach() {
		return resultAttach;
	}

	public void setResultAttach(String resultAttach) {
		this.resultAttach = resultAttach;
	}

}