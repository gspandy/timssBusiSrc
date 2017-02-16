package com.timss.workorder.vo;

import java.io.Serializable;
import java.util.Date;

public class WoStatisVO implements Serializable {
	
	private static final long serialVersionUID = -3445727239665913524L;
	
	  private int id; //ID
	  private String workOrderCode; //工单编号
	  private String  equipId; //设备ID
	  private String  equipNameCode;//设备编号(风机)
	  private String description;//故障描述
	  private Date  discoverTime;// 发现时间
	  private String faultConfrimUser;//故障确认人
	  private int approveStopTime;//批准停机时间
	  private Date  endTime;// 完工时间
	  private int faultStopTime;//故障停机时间
	  private String endReportUser;//完工汇报人
	  private String acceptanceUser; //验收人
	  private String remarks; 
	  private int printNum;  //用来标识第几次查询出来的结果（打印时，以此为标识查询）
	  
	  
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
	public String getEquipId() {
		return equipId;
	}
	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}
	public String getEquipNameCode() {
		return equipNameCode;
	}
	public void setEquipNameCode(String equipNameCode) {
		this.equipNameCode = equipNameCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDiscoverTime() {
		return discoverTime;
	}
	public void setDiscoverTime(Date discoverTime) {
		this.discoverTime = discoverTime;
	}
	public String getFaultConfrimUser() {
		return faultConfrimUser;
	}
	public void setFaultConfrimUser(String faultConfrimUser) {
		this.faultConfrimUser = faultConfrimUser;
	}
	public int getApproveStopTime() {
		return approveStopTime;
	}
	public void setApproveStopTime(int approveStopTime) {
		this.approveStopTime = approveStopTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getFaultStopTime() {
		return faultStopTime;
	}
	public void setFaultStopTime(int faultStopTime) {
		this.faultStopTime = faultStopTime;
	}
	public String getEndReportUser() {
		return endReportUser;
	}
	public void setEndReportUser(String endReportUser) {
		this.endReportUser = endReportUser;
	}
	public String getAcceptanceUser() {
		return acceptanceUser;
	}
	public void setAcceptanceUser(String acceptanceUser) {
		this.acceptanceUser = acceptanceUser;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getPrintNum() {
		return printNum;
	}
	public void setPrintNum(int printNum) {
		this.printNum = printNum;
	}
	@Override
	public String toString() {
		return "WoStatisVO [id=" + id + ", workOrderCode=" + workOrderCode
				+ ", equipId=" + equipId + ", equipNameCode=" + equipNameCode
				+ ", description=" + description + ", discoverTime="
				+ discoverTime + ", faultConfrimUser=" + faultConfrimUser
				+ ", approveStopTime=" + approveStopTime + ", endTime="
				+ endTime + ", faultStopTime=" + faultStopTime
				+ ", endReportUser=" + endReportUser + ", acceptanceUser="
				+ acceptanceUser + ", remarks=" + remarks + ", printNum="
				+ printNum + "]";
	}
	 
	  
	  
	  
}
