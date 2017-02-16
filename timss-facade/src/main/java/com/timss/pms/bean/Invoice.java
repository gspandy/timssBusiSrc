package com.timss.pms.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;
import com.yudean.itc.annotation.AutoGen;

/**
 * 发票类
 * @ClassName:     Invoice
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-8-11 下午2:18:23
 */
public class Invoice extends ItcMvcBean {

	// Fields
 
	private Integer id;
	private String code;
	private Double rate;
	private Double sum;
	private String command;
	private Double tax;
	private Double withoutTax;
	private Date updateTime;
	private String updateUser;
	private Date createTime;
	private String createUser;
	private String siteid;
	private String deptid;
	private Integer contractId;
	private Integer payplanId;
	private Integer payId;
	private Boolean delFlag;
	private String status;
	
	private String invoiceCode;
	private Date invoiceDate;
	
	private String ischeck;
	private Date checkDate;
	private String checkUser;
	private Date checkTime;

	// Constructors

	/** default constructor */
	public Invoice() {
	}

	/** minimal constructor */
	public Invoice(Integer id, String code, Integer contractId) {
		this.id = id;
		this.code = code;
		this.contractId = contractId;
	}

	/** full constructor */
	public Invoice(Integer id, String code, Double rate, Double sum,
			String command, Double tax, Double withoutTax, Date updateTime,
			String updateUser, Date createTime, String createUser,
			String siteid, String deptid, Integer contractId,
			Integer payplanId, Integer payId, Boolean delFlag, String status) {
		this.id = id;
		this.code = code;
		this.rate = rate;
		this.sum = sum;
		this.command = command;
		this.tax = tax;
		this.withoutTax = withoutTax;
		this.updateTime = updateTime;
		this.updateUser = updateUser;
		this.createTime = createTime;
		this.createUser = createUser;
		this.siteid = siteid;
		this.deptid = deptid;
		this.contractId = contractId;
		this.payplanId = payplanId;
		this.payId = payId;
		this.delFlag = delFlag;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getRate() {
		return this.rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getSum() {
		return this.sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Double getTax() {
		return this.tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getWithoutTax() {
		return this.withoutTax;
	}

	public void setWithoutTax(Double withoutTax) {
		this.withoutTax = withoutTax;
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
		return this.contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getPayplanId() {
		return this.payplanId;
	}

	public void setPayplanId(Integer payplanId) {
		this.payplanId = payplanId;
	}

	public Integer getPayId() {
		return this.payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Boolean getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	
	
}