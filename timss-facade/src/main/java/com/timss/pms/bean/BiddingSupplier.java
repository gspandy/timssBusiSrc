package com.timss.pms.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * TimPmsBiddingSupplier entity. @author MyEclipse Persistence Tools
 */

public class BiddingSupplier extends ItcMvcBean {

	// Fields
    @AutoGen("PMS_BS_ID")
	private String id;
	private String BId;
	private String code;
	private String name;
	private String contactPerson;
	private String contactPhone;
	private String siteid;
	private String delFlag;
	private String deptid;
	private String createuser;
	private Date createdate;
	private String modifyuser;
	private Date modifydate;

	// Constructors

	/** default constructor */
	public BiddingSupplier() {
	}

	/** minimal constructor */
	public BiddingSupplier(String id) {
		this.id = id;
	}

	/** full constructor */
	public BiddingSupplier(String id, String BId, String code,
			String name, String contactPerson, String contactPhone,
			String siteid, String delFlag, String deptid, String createuser,
			Date createdate, String modifyuser, Date modifydate) {
		this.id = id;
		this.BId = BId;
		this.code = code;
		this.name = name;
		this.contactPerson = contactPerson;
		this.contactPhone = contactPhone;
		this.siteid = siteid;
		this.delFlag = delFlag;
		this.deptid = deptid;
		this.createuser = createuser;
		this.createdate = createdate;
		this.modifyuser = modifyuser;
		this.modifydate = modifydate;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBId() {
		return this.BId;
	}

	public void setBId(String BId) {
		this.BId = BId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
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

	public String getDeptid() {
		return this.deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
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

}