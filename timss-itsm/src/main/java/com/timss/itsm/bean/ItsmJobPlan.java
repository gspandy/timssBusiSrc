package com.timss.itsm.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmJobPlan extends ItcMvcBean {
	   
	  private static final long serialVersionUID = 4259079955857780539L;
	  private int id; //ID
	  @AutoGen(value = "ITSM_JP_CODE",requireType = GenerationType.REQUIRED_NEW)
	  private String jobPlanCode;  //编号
	
	  private String description;  //描述（名字）
	  private String specialtyId;  //专业
	  private String keywords; //关键字
	  private String  faultTypeId;//故障类型ID
	  private String  faultTypeName;//故障类型名
	  
	  private String remarks;//备注
	  private String crew;
	  private String location;
	  private int jpDuration;
	  private String downtime;
	  private String JobPlanType;//方案类型(STANDARD：标准；PLAN：策划；ACTUAL：实际)
	  private String workOrderId;//工单ID
	  private int yxbz;
	  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJobPlanCode() {
		return jobPlanCode;
	}
	public void setJobPlanCode(String jobPlanCode) {
		this.jobPlanCode = jobPlanCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSpecialtyId() {
		return specialtyId;
	}
	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public String getFaultTypeId() {
		return faultTypeId;
	}
	public void setFaultTypeId(String faultTypeId) {
		this.faultTypeId = faultTypeId;
	}
	public String getFaultTypeName() {
		return faultTypeName;
	}
	public void setFaultTypeName(String faultTypeName) {
		this.faultTypeName = faultTypeName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCrew() {
		return crew;
	}
	public void setCrew(String crew) {
		this.crew = crew;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getJpDuration() {
		return jpDuration;
	}
	public void setJpDuration(int jpDuration) {
		this.jpDuration = jpDuration;
	}
	public String getDowntime() {
		return downtime;
	}
	public void setDowntime(String downtime) {
		this.downtime = downtime;
	}
	public String getJobPlanType() {
		return JobPlanType;
	}
	public void setJobPlanType(String jobPlanType) {
		JobPlanType = jobPlanType;
	}
	
	
	public String getWorkOrderId() {
		return workOrderId;
	}
	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
	
	public int getYxbz() {
		return yxbz;
	}
	public void setYxbz(int yxbz) {
		this.yxbz = yxbz;
	}
	@Override
	public String toString() {
		return "JobPlan [id=" + id + ", jobPlanCode=" + jobPlanCode
				+ ", description=" + description + ", specialtyId="
				+ specialtyId + ", keywords=" + keywords + ", faultTypeId="
				+ faultTypeId + ", faultTypeName=" + faultTypeName
				+ ", remarks=" + remarks + ", crew=" + crew + ", location="
				+ location + ", jpDuration=" + jpDuration + ", downtime="
				+ downtime + ", JobPlanType=" + JobPlanType + ", workOrderId="
				+ workOrderId + ", yxbz=" + yxbz + "]";
	}
	
}
