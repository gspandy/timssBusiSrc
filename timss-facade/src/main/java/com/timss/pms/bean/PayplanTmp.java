package com.timss.pms.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 结算计划临时表
 * @ClassName:  PayplanTmp
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-8-13 上午11:35:32
 */
public class PayplanTmp extends ItcMvcBean {

	// Fields

	private Integer id;
	private Integer contractId;
	private Integer payplanId;
	private String payType;
	private Double paySum;
	private String percent;
	private Date payDate;
	private String command;
	private String createUser;
	private Date createTime;
	private String status;
	private Boolean needchecked;
	private String checkStatus;
	private String payStatus;
	private String siteid;
	private String deptid;
	private String flowId;

	// Constructors

	/** default constructor */
	public PayplanTmp() {
	}

	/** minimal constructor */
	public PayplanTmp(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public PayplanTmp(Integer id, 
			Integer payplanId, String payType, Double paySum, String percent,
			Date payDate, String command, String createUser, Date createTime,
			String status, Boolean needchecked, String checkStatus,
			String payStatus, String siteid, String deptid) {
		this.id = id;
		this.payplanId = payplanId;
		this.payType = payType;
		this.paySum = paySum;
		this.percent = percent;
		this.payDate = payDate;
		this.command = command;
		this.createUser = createUser;
		this.createTime = createTime;
		this.status = status;
		this.needchecked = needchecked;
		this.checkStatus = checkStatus;
		this.payStatus = payStatus;
		this.siteid = siteid;
		this.deptid = deptid;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getpayplanId() {
		return this.payplanId;
	}

	public void setpayplanId(Integer payplanId) {
		this.payplanId = payplanId;
	}

	public String getPayType() {
		return this.payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Double getPaySum() {
		return this.paySum;
	}

	public void setPaySum(Double paySum) {
		this.paySum = paySum;
	}

	public String getPercent() {
		return this.percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public Date getPayDate() {
		return this.payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getNeedchecked() {
		return this.needchecked;
	}

	public void setNeedchecked(Boolean needchecked) {
		this.needchecked = needchecked;
	}

	public String getCheckStatus() {
		return this.checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getPayStatus() {
		return this.payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
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

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	
}