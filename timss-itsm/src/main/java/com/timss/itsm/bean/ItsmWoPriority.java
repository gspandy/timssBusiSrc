package com.timss.itsm.bean;

import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmWoPriority extends ItcMvcBean{

	private static final long serialVersionUID = -5633943007185707965L;
	private int id; // ID
	//@AutoGen(value = "WO_PRIORITY", requireType = GenerationType.REQUIRED_NEW)
	private String priorityCode; // 编号
	private String name; // 服务级别名
	private double respondLength; //响应时间长度(分钟)
	private double releaseLength; // 释放时间长度（小时）
	private double solveLength; //解决问题的时间长度（小时）
	private String remarks;// 说明
	
	private int yxbz;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public double getRespondLength() {
		return respondLength;
	}

	public void setRespondLength(double respondLength) {
		this.respondLength = respondLength;
	}

	public double getReleaseLength() {
		return releaseLength;
	}

	public void setReleaseLength(double releaseLength) {
		this.releaseLength = releaseLength;
	}

	public double getSolveLength() {
		return solveLength;
	}

	public void setSolveLength(double solveLength) {
		this.solveLength = solveLength;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPriorityCode() {
		return priorityCode;
	}

	public void setPriorityCode(String priorityCode) {
		this.priorityCode = priorityCode;
	}

	

	@Override
	public String toString() {
		return "WoPriority [id=" + id + ", priorityCode=" + priorityCode
				+ ", name=" + name + ", respondLength=" + respondLength
				+ ", releaseLength=" + releaseLength + ", solveLength="
				+ solveLength + ", remarks=" + remarks
				+ ", yxbz=" + yxbz + "]";
	}


}
