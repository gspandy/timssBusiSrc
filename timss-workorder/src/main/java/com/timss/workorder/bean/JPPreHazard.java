package com.timss.workorder.bean;

import java.io.Serializable;

import com.yudean.mvc.bean.ItcMvcBean;

public class JPPreHazard extends ItcMvcBean implements Serializable {
	  
	private static final long serialVersionUID = 6766257934376704554L;
	private int id; //ID
	  private int jobPlanID;  //作业方案ID
	  private int hazardId;//安全事项ID
	  private int precautionId; //预控措施ID
	  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getJobPlanID() {
		return jobPlanID;
	}
	public void setJobPlanID(int jobPlanID) {
		this.jobPlanID = jobPlanID;
	}
	public int getHazardId() {
		return hazardId;
	}
	public void setHazardId(int hazardId) {
		this.hazardId = hazardId;
	}
	public int getPrecautionId() {
		return precautionId;
	}
	public void setPrecautionId(int precautionId) {
		this.precautionId = precautionId;
	}
	  
}
