package com.timss.pms.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 结算计划信息
 * @ClassName:     Payplan
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-14 下午2:41:25
 */
public class Payplan extends ItcMvcBean {

	// Fields

	private Integer id;
	private Integer timId;
	private Integer contractId;
	private String payType;
	private Double paySum;
	private String percent;
	private Date payDate;
	private String command;
	private String createUser;
	private Date createTime;
	private String status;
	private Boolean needchecked;
    
	private String payStatus;
	private String checkStatus;
	private String siteid;
	private String deptid;

	// Constructors

	/** default constructor */
	public Payplan() {
	}

	/** minimal constructor */
	public Payplan(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public Payplan(Integer id, Integer timId, Integer contractId,
			String payType, Double paySum, String percent, Date payDate,
			String command, String createUser, Date createTime, String status,
			Boolean check, Boolean ischecked, Boolean ispayed, String siteid,
			String deptid) {
		this.id = id;
		this.timId = timId;
		this.contractId = contractId;
		this.payType = payType;
		this.paySum = paySum;
		this.percent = percent;
		this.payDate = payDate;
		this.command = command;
		this.createUser = createUser;
		this.createTime = createTime;
		this.status = status;
		this.needchecked = check;

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

	public Integer getTimId() {
		return this.timId;
	}

	public void setTimId(Integer timId) {
		this.timId = timId;
	}

	public Integer getContractId() {
		return this.contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
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

	public void setNeedchecked(Boolean check) {
		this.needchecked = check;
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

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	

}