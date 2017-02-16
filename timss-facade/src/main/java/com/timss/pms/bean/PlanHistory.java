package com.timss.pms.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * TimPmsPlanHistory entity. @author MyEclipse Persistence Tools
 */

public class PlanHistory extends ItcMvcBean {

	// Fields
    @AutoGen("PMS_PH_ID")
	private String planHistoryId;
	private Integer planId;
	private String planName;
	private String year;
	private String planType;
	private String projectLeader;
	private String customManager;
	private Date startTime;
	private Date endTime;
	private Double planPercent;
	private Double projectIncome;
	private Double projectCost;
	private Double annualIncome;
	private Double annualCost;
	private Date changingTime;
	private String changingUser;
	private String command;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	
	private Double histIncome;
	private Double histCost;
	private Double histProfit;
	private Double histPercent;
	private Date actualEndTime;
	private int carryOverTimes;
	
	private Double annualPercent;

	// Constructors

	/** default constructor */
	public PlanHistory() {
	}


	/** full constructor */
	public PlanHistory(String planHistoryId, Integer planId,
			String planName, String year, String planType,
			String projectLeader, String customManager, Date startTime,
			Date endTime, Double planPercent, Double projectIncome,
			Double projectCost, Double annualIncome, Double annualCost,
			Date changingTime, String changingUser, String command,
			String createUser, Date createTime, String updateUser,
			Date updateTime) {
		this.planHistoryId = planHistoryId;
		this.planId = planId;
		this.planName = planName;
		this.year = year;
		this.planType = planType;
		this.projectLeader = projectLeader;
		this.customManager = customManager;
		this.startTime = startTime;
		this.endTime = endTime;
		this.planPercent = planPercent;
		this.projectIncome = projectIncome;
		this.projectCost = projectCost;
		this.annualIncome = annualIncome;
		this.annualCost = annualCost;
		this.changingTime = changingTime;
		this.changingUser = changingUser;
		this.command = command;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
	}

	// Property accessors

	

	public String getPlanName() {
		return this.planName;
	}

	public String getPlanHistoryId() {
		return planHistoryId;
	}


	public void setPlanHistoryId(String planHistoryId) {
		this.planHistoryId = planHistoryId;
	}


	public Integer getPlanId() {
		return planId;
	}


	public void setPlanId(Integer planId) {
		this.planId = planId;
	}


	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPlanType() {
		return this.planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getProjectLeader() {
		return this.projectLeader;
	}

	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}

	public String getCustomManager() {
		return this.customManager;
	}

	public void setCustomManager(String customManager) {
		this.customManager = customManager;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Double getPlanPercent() {
		return this.planPercent;
	}

	public void setPlanPercent(Double planPercent) {
		this.planPercent = planPercent;
	}

	public Double getProjectIncome() {
		return this.projectIncome;
	}

	public void setProjectIncome(Double projectIncome) {
		this.projectIncome = projectIncome;
	}

	public Double getProjectCost() {
		return this.projectCost;
	}

	public void setProjectCost(Double projectCost) {
		this.projectCost = projectCost;
	}

	public Double getAnnualIncome() {
		return this.annualIncome;
	}

	public void setAnnualIncome(Double annualIncome) {
		this.annualIncome = annualIncome;
	}

	public Double getAnnualCost() {
		return this.annualCost;
	}

	public void setAnnualCost(Double annualCost) {
		this.annualCost = annualCost;
	}

	public Date getChangingTime() {
		return this.changingTime;
	}

	public void setChangingTime(Date changingTime) {
		this.changingTime = changingTime;
	}

	public String getChangingUser() {
		return this.changingUser;
	}

	public void setChangingUser(String changingUser) {
		this.changingUser = changingUser;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public Double getHistIncome() {
		return histIncome;
	}


	public void setHistIncome(Double histIncome) {
		this.histIncome = histIncome;
	}


	public Double getHistCost() {
		return histCost;
	}


	public void setHistCost(Double histCost) {
		this.histCost = histCost;
	}


	public Double getHistProfit() {
		return histProfit;
	}


	public void setHistProfit(Double histProfit) {
		this.histProfit = histProfit;
	}


	public Double getHistPercent() {
		return histPercent;
	}


	public void setHistPercent(Double histPercent) {
		this.histPercent = histPercent;
	}


	public Date getActualEndTime() {
		return actualEndTime;
	}


	public void setActualEndTime(Date actualEndTime) {
		this.actualEndTime = actualEndTime;
	}


	public int getCarryOverTimes() {
		return carryOverTimes;
	}


	public void setCarryOverTimes(int carryOverTimes) {
		this.carryOverTimes = carryOverTimes;
	}


	public Double getAnnualPercent() {
		return annualPercent;
	}


	public void setAnnualPercent(Double annualPercent) {
		this.annualPercent = annualPercent;
	}
	
	

}