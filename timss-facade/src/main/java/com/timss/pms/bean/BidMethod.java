package com.timss.pms.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @ClassName:  TimPmsBidMethod
 * @company: gdyd
 * @Description: 评标方法bean
 * @author:    黄晓岚
 * @date:   2014-7-2 下午3:05:48
 */
public class BidMethod extends ItcMvcBean {

	// Fields

	private Integer bidMethodId;
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

	// Constructors

	/** default constructor */
	public BidMethod() {
	}

	

	// Property accessors

	public Integer getBidMethodId() {
		return this.bidMethodId;
	}

	public void setBidMethodId(Integer bidMethodId) {
		this.bidMethodId = bidMethodId;
	}



	public Integer getBidId() {
		return bidId;
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

	
}