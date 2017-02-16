package com.timss.pms.vo;

import java.util.List;

import com.timss.pms.bean.Project;

public class ProjectVo extends Project{
	private String typeName;
	
	private String statusValue;
	
	private String projectStatus;
	
	private int contractNum;
	
	private int bidResultNum;
	
	private double contractSum;
	
	private double paySum;
	
	private String flowid;
	
	private List<MilestoneVo> milestoneVos;
	
	public String getFlowid() {
		return flowid;
	}
	public void setFlowid(String flowid) {
		this.flowid = flowid;
	}
    
	/**
	 * 
	 * @Title: getTypeName
	 * @Description: 获取项目类型的中文名称
	 * @return
	 */
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public int getContractNum() {
		return contractNum;
	}

	public void setContractNum(int contractNum) {
		this.contractNum = contractNum;
	}

	public int getBidResultNum() {
		return bidResultNum;
	}

	public void setBidResultNum(int bidResultNum) {
		this.bidResultNum = bidResultNum;
	}

	public double getContractSum() {
		return contractSum;
	}

	public void setContractSum(double contractSum) {
		this.contractSum = contractSum;
	}

	public double getPaySum() {
		return paySum;
	}

	public void setPaySum(double paySum) {
		this.paySum = paySum;
	}
	public List<MilestoneVo> getMilestoneVos() {
		return milestoneVos;
	}
	public void setMilestoneVos(List<MilestoneVo> milestoneVos) {
		this.milestoneVos = milestoneVos;
	}
	
	
}
