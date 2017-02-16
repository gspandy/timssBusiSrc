package com.timss.workorder.bean;

import java.io.Serializable;

import com.yudean.mvc.bean.ItcMvcBean;



public class JPWorker extends ItcMvcBean implements Serializable {
	  
	private static final long serialVersionUID = -8147598783065358519L;
	private int id; //ID
	  private int jpTask;  //
	  private int jobPlanId;  //作业方案ID
	  private String workerList;
	  private String managerInfo;
	  private String item;
	  private String proj;
	  private String remarks;  //备注
	  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getJpTask() {
		return jpTask;
	}
	public void setJpTask(int jpTask) {
		this.jpTask = jpTask;
	}
	public int getJobPlanId() {
		return jobPlanId;
	}
	public void setJobPlanId(int jobPlanId) {
		this.jobPlanId = jobPlanId;
	}
	public String getWorkerList() {
		return workerList;
	}
	public void setWorkerList(String workerList) {
		this.workerList = workerList;
	}
	public String getManagerInfo() {
		return managerInfo;
	}
	public void setManagerInfo(String managerInfo) {
		this.managerInfo = managerInfo;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getProj() {
		return proj;
	}
	public void setProj(String proj) {
		this.proj = proj;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	  
	  
}
