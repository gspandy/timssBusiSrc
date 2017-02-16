package com.timss.itsm.bean;

import java.io.Serializable;

import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmCustomerConf extends ItcMvcBean implements Serializable {
	
	private static final long serialVersionUID = 1142946020082280341L;
	private int id;
	private String customerCode;  //客户工号
	private String customerName; 
	private String faultTypeId;  //服务目录ID
	private String faultTypeName; 
	private String initPriorityId; //服务级别初始值
	private String priorityName;
	private int yxbz;
	
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getFaultTypeName() {
		return faultTypeName;
	}
	public void setFaultTypeName(String faultTypeName) {
		this.faultTypeName = faultTypeName;
	}
	public String getPriorityName() {
		return priorityName;
	}
	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getFaultTypeId() {
		return faultTypeId;
	}
	public void setFaultTypeId(String faultTypeId) {
		this.faultTypeId = faultTypeId;
	}
	public String getInitPriorityId() {
		return initPriorityId;
	}
	public void setInitPriorityId(String initPriorityId) {
		this.initPriorityId = initPriorityId;
	}
	public int getYxbz() {
		return yxbz;
	}
	public void setYxbz(int yxbz) {
		this.yxbz = yxbz;
	}
	@Override
	public String toString() {
		return "ItsmCustomerPri [id=" + id + ", customerCode=" + customerCode
				+ ", faultTypeId=" + faultTypeId + ", initPriorityId="
				+ initPriorityId + ", yxbz=" + yxbz + "]";
	}
	
	
	  
	
	
}
