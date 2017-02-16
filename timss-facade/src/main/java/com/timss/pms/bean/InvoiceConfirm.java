package com.timss.pms.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 发票到款确认bean
 */

public class InvoiceConfirm extends ItcMvcBean {

	// Fields
    @AutoGen("PMS_INVOICE_CFM_ID")
	private String id;
	private Integer invoiceId;
	private String siteid;
	private Date updateTime;
	private String updateUser;
	private Date createTime;
	private String createUser;
	private Double confirmSum;
	private Date confirmTime;
	private Boolean delFlag;
	private String command;

	// Constructors



	/** default constructor */
	public InvoiceConfirm() {
	}

	/** minimal constructor */
	public InvoiceConfirm(String id) {
		this.id = id;
	}

	/** full constructor */
	public InvoiceConfirm(String id, Integer invoiceId, String siteid,
			Date updateTime, String updateUser, Date createTime,
			String createUser, Double confirmSum, Date confirmTime) {
		this.id = id;
		this.invoiceId = invoiceId;
		this.siteid = siteid;
		this.updateTime = updateTime;
		this.updateUser = updateUser;
		this.createTime = createTime;
		this.createUser = createUser;
		this.confirmSum = confirmSum;
		this.confirmTime = confirmTime;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getSiteid() {
		return this.siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
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

	public Double getConfirmSum() {
		return this.confirmSum;
	}

	public void setConfirmSum(Double confirmSum) {
		this.confirmSum = confirmSum;
	}

	public Date getConfirmTime() {
		return this.confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}