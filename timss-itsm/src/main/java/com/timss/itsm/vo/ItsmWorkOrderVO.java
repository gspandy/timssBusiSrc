package com.timss.itsm.vo;

import java.io.Serializable;
import java.util.Date;

public class ItsmWorkOrderVO implements Serializable {
	
 
	  private static final long serialVersionUID = -5417827989788497108L;
	  private int id; //ID
	  private String workOrderCode; //工单编号
	  
	  private String description;//故障描述
	  
	  private String epuipId; //设备编号
	  
	  private Date  createDate;// 报告日期	
	  
	  private String principal;// 工单负责人（当前环节的负责人）
	  
	  private String  woSpec;//工单专业编号
	  
	  private String woStatus; //工单当前状态
	  
	  private String endReport; //完工汇报情况

	  
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWorkOrderCode() {
		return workOrderCode;
	}

	public void setWorkOrderCode(String workOrderCode) {
		this.workOrderCode = workOrderCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getWoSpec() {
		return woSpec;
	}

	public void setWoSpec(String woSpec) {
		this.woSpec = woSpec;
	}

	public String getEndReport() {
		return endReport;
	}

	public void setEndReport(String endReport) {
		this.endReport = endReport;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getWoStatus() {
		return woStatus;
	}

	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}

	public String getEpuipId() {
		return epuipId;
	}

	public void setEpuipId(String epuipId) {
		this.epuipId = epuipId;
	}

	@Override
	public String toString() {
		return "WorkOrderVO [id=" + id + ", workOrderCode=" + workOrderCode
				+ ", description=" + description + ", epuipId=" + epuipId
				+ ", createDate=" + createDate + ", principal=" + principal
				+ ", woSpec=" + woSpec + ", woStatus=" + woStatus
				+ ", endReport=" + endReport + "]";
	}

	 
}
