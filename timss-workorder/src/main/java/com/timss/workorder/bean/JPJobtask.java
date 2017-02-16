package com.timss.workorder.bean;

import java.io.Serializable;

import com.yudean.mvc.bean.ItcMvcBean;


public class JPJobtask extends ItcMvcBean implements Serializable {
	   
	private static final long serialVersionUID = 6271516897513119165L;
	private int id;
	  private int jobPlanId;
	  private String name;  //名称
	  private String description; //描述
	  private String taskDuration;
	  private String proj; //大类
	  private String item; //项目 
	  private String apply;	  //要求
	  private String remarks;  //记录
	  private int yxbz;
	  
	  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getJobPlanId() {
		return jobPlanId;
	}
	public void setJobPlanId(int jobPlanId) {
		this.jobPlanId = jobPlanId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTaskDuration() {
		return taskDuration;
	}
	public void setTaskDuration(String taskDuration) {
		this.taskDuration = taskDuration;
	}
	public String getProj() {
		return proj;
	}
	public void setProj(String proj) {
		this.proj = proj;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getApply() {
		return apply;
	}
	public void setApply(String apply) {
		this.apply = apply;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getYxbz() {
		return yxbz;
	}
	public void setYxbz(int yxbz) {
		this.yxbz = yxbz;
	}
	  
	  
	 
}
