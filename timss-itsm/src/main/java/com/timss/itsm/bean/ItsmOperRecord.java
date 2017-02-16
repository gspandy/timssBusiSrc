package com.timss.itsm.bean;

import java.io.Serializable;
import java.util.Date;


/**
 * @title: {title} 操作记录
 * @description: {desc}
 * @company: gdyd
 * @className: OperRecord.java
 * @author: 王中华
 * @createDate: 2014-12-30
 * @updateUser: 王中华
 * @version: 1.0
 */
public class ItsmOperRecord  implements Serializable{
	  
	private static final long serialVersionUID = 8930124716469784700L;
	private String woId; 
	  private String flowId; 
	  private String operUser;  
	  private String operUserTeam;
	  private String operType;
	  private String operContent;
	  private Date operDate;
	  private String siteid;
	  
	public String getWoId() {
		return woId;
	}
	public void setWoId(String woId) {
		this.woId = woId;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getOperUser() {
		return operUser;
	}
	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}
	public String getOperUserTeam() {
		return operUserTeam;
	}
	public void setOperUserTeam(String operUserTeam) {
		this.operUserTeam = operUserTeam;
	}
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public String getOperContent() {
		return operContent;
	}
	public void setOperContent(String operContent) {
		this.operContent = operContent;
	}
	
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getSiteid() {
		return siteid;
	}
	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}
	@Override
	public String toString() {
		return "OperRecord [woId=" + woId + ", flowId=" + flowId
				+ ", operUser=" + operUser + ", operUserTeam=" + operUserTeam
				+ ", operType=" + operType + ", operContent=" + operContent
				+ ", operDate=" + operDate + ", siteid=" + siteid + "]";
	}
	
	  
	
	 
	  
}
