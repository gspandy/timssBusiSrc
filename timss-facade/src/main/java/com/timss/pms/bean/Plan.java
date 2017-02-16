package com.timss.pms.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @ClassName:     Plan
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-6-30 下午2:31:37
 */
public class Plan extends ItcMvcBean {
    
	// Fields

	private Integer id;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plan other = (Plan) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	private String planCode;
	private String planName;
	private String subjectCode;
	private String year;
	private String type;
	private String planAttach;
	private String command1;
	private String dept;
	private Double budget;
	private String status;
	private String createUser;
	private Date createTime;
	private Boolean delFlag;
	private String updateUser;
	private Date updateTime;
	private Double planPercent;
	private Double projectIncome;
	private Double projectCost;
	private Double projectProfit;
	private Double annualIncome;
	private Double annualCost;
	private Double annualProfit;
	private Double receivable;
	private Double recived;
	private Double payable;
	private Double payed;
	private String projectLeader;
	private String customManager;
	private String command;
	private Date startTime;
	private Date endTime;
	private String siteid;
	private String deptid;
	
	private Double histIncome;
	private Double histCost;
	private Double histProfit;
	private Double histPercent;
	private Date actualEndTime;
	private int carryOverTimes;
	
	private Double annualPercent;

	// Constructors

	/** default constructor */
	public Plan() {
	}

	/** minimal constructor */
	public Plan(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public Plan(Integer id, String planCode, String planName,
			String subjectCode, String year, String type, String planAttach,
			String command1, String dept, Double budget, String status,
			String createUser, Date createTime, Boolean delFlag,
			String updateUser, Date updateTime, Double planPercent,
			Double projectIncome, Double projectCost, Double projectProfit,
			Double annualIncome, Double annualCost, Double annualProfit,
			Double receivable, Double recived, Double payable, Double payed,
			String projectLeader, String customManager, String command,
			Date startTime, Date endTime, String siteid, String deptid) {
		this.id = id;
		this.planCode = planCode;
		this.planName = planName;
		this.subjectCode = subjectCode;
		this.year = year;
		this.type = type;
		this.planAttach = planAttach;
		this.command1 = command1;
		this.dept = dept;
		this.budget = budget;
		this.status = status;
		this.createUser = createUser;
		this.createTime = createTime;
		this.delFlag = delFlag;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.planPercent = planPercent;
		this.projectIncome = projectIncome;
		this.projectCost = projectCost;
		this.projectProfit = projectProfit;
		this.annualIncome = annualIncome;
		this.annualCost = annualCost;
		this.annualProfit = annualProfit;
		this.receivable = receivable;
		this.recived = recived;
		this.payable = payable;
		this.payed = payed;
		this.projectLeader = projectLeader;
		this.customManager = customManager;
		this.command = command;
		this.startTime = startTime;
		this.endTime = endTime;
		this.siteid = siteid;
		this.deptid = deptid;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlanCode() {
		return this.planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getPlanName() {
		return this.planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getSubjectCode() {
		return this.subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlanAttach() {
		return this.planAttach;
	}

	public void setPlanAttach(String planAttach) {
		this.planAttach = planAttach;
	}

	public String getCommand1() {
		return this.command1;
	}

	public void setCommand1(String command1) {
		this.command1 = command1;
	}

	public String getDept() {
		return this.dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public Double getBudget() {
		return this.budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Boolean getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
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

	public Double getProjectProfit() {
		return this.projectProfit;
	}

	public void setProjectProfit(Double projectProfit) {
		this.projectProfit = projectProfit;
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

	public Double getAnnualProfit() {
		return this.annualProfit;
	}

	public void setAnnualProfit(Double annualProfit) {
		this.annualProfit = annualProfit;
	}

	public Double getReceivable() {
		return this.receivable;
	}

	public void setReceivable(Double receivable) {
		this.receivable = receivable;
	}

	public Double getRecived() {
		return this.recived;
	}

	public void setRecived(Double recived) {
		this.recived = recived;
	}

	public Double getPayable() {
		return this.payable;
	}

	public void setPayable(Double payable) {
		this.payable = payable;
	}

	public Double getPayed() {
		return this.payed;
	}

	public void setPayed(Double payed) {
		this.payed = payed;
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

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
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

	public String getSiteid() {
		return this.siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getDeptid() {
		return this.deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
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