package com.timss.pms.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 招标与招标单位关系表bean
 * @ClassName:     BidCon
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-23 上午10:13:31
 */
public class BidCon extends ItcMvcBean {

	// Fields

	private Integer id;
	private Integer bidId;
	private String companyNo;
	private String siteid;
	private Date createTime;
	private String createUser;
	private Date updateTime;
	private String updateUser;
	private Boolean iswinner;
	private Double price;
	private Boolean isrecommand;
	private String status;
	private String deptid;
	private String command;
	private String delFlag;

	// Constructors

	/** default constructor */
	public BidCon() {
	}

	/** minimal constructor */
	public BidCon(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public BidCon(Integer id, String companyNo,
			String siteid, Date createTime, String createUser, Date updateTime,
			String updateUser, Boolean iswinner, Double price,
			Boolean isrecommand, String status, String deptid) {
		this.id = id;
		this.companyNo = companyNo;
		this.siteid = siteid;
		this.createTime = createTime;
		this.createUser = createUser;
		this.updateTime = updateTime;
		this.updateUser = updateUser;
		this.iswinner = iswinner;
		this.price = price;
		this.isrecommand = isrecommand;
		this.status = status;
		this.deptid = deptid;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getBidId() {
		return bidId;
	}

	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}

	public String getCompanyNo() {
		return this.companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getSiteid() {
		return this.siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
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

	public Boolean getIswinner() {
		return this.iswinner;
	}

	public void setIswinner(Boolean iswinner) {
		this.iswinner = iswinner;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Boolean getIsrecommand() {
		return this.isrecommand;
	}

	public void setIsrecommand(Boolean isrecommand) {
		this.isrecommand = isrecommand;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeptid() {
		return this.deptid;
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

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	
}