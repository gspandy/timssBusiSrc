package com.timss.itsm.vo;

import java.io.Serializable;
import java.util.Date;

public class ItsmMaintainPlanVO  implements Serializable{
  
	private static final long serialVersionUID = 4335684793757684448L;
	
	private int id; 
	private String maintainPlanCode;  //编号
	private String description;  //描述（名字）
	private String specialty;  //专业
	private String equipId;  //专业
	private Date nextDate;  //下一个日期

	private String  workTeam;//负责班组
	private String principal;//负责人
 
	private String status; //状态

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMaintainPlanCode() {
		return maintainPlanCode;
	}

	public void setMaintainPlanCode(String maintainPlanCode) {
		this.maintainPlanCode = maintainPlanCode;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public String getWorkTeam() {
		return workTeam;
	}

	public void setWorkTeam(String workTeam) {
		this.workTeam = workTeam;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MaintainPlanVO [id=" + id + ", maintainPlanCode="
				+ maintainPlanCode + ", description=" + description
				+ ", specialty=" + specialty + ", nextDate=" + nextDate
				+ ", workTeam=" + workTeam + ", principal=" + principal
				+ ", status=" + status + "]";
	}

	
	 
	
	 
}
