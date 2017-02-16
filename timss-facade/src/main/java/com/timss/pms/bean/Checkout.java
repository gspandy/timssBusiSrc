package com.timss.pms.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 项目验收bean
 * @ClassName:     Checkout
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-18 上午11:24:20
 */
public class Checkout extends ItcMvcBean {

	// Fields

	private Integer id;
	private String level;
	private String type;
	private Date time;
	private String address;
	private String condition;
	private String conditionAttach;
	private String standard;
	private String standardAttach;
	private String expert;
	private String expertAttach;
	private String report;
	private String reportAttach;
	private String completion;
	private String completionAttach;
	private String valuation;
	private String valuationAttach;
	private String createUser;
	private Date createTime;
	private String status;
	private String command;
	private Boolean delFlag;
	private String projectName;
	private String siteid;
	private String deptid;
	private String projectLeader;
	private String contractName;
	private String checkStage;
	private Date updateTime;
	private String updateUser;
	private String firstParty;
	private String fpLeader;
	private Integer contractId;
	private String attach;
	private Integer payplanId;
	
	private String checkUser;
	private Date startDate;
	private Date endDate;
	private String isProjectChange; //Y  or N
	private String payDecription;
	
	private String flowId;
	private String processInstId;
	private String flowStatus;
	private double budget;

	// Constructors

	/** default constructor */
	public Checkout() {
	}

	/** minimal constructor */
	public Checkout(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public Checkout(Integer id, String level, String type, Date time,
			String address, String condition, String conditionAttach,
			String standard, String standardAttach, String expert,
			String expertAttach, String report, String reportAttach,
			String completion, String completionAttach, String valuation,
			String valuationAttach, String createUser, Date createTime,
			String status, String command, Boolean delFlag, String projectName,
			String siteid, String deptid, String projectLeader,
			String contractName, String checkStage, Date updateTime,
			String updateUser, String firstParty, String fpLeader) {
		this.id = id;
		this.level = level;
		this.type = type;
		this.time = time;
		this.address = address;
		this.condition = condition;
		this.conditionAttach = conditionAttach;
		this.standard = standard;
		this.standardAttach = standardAttach;
		this.expert = expert;
		this.expertAttach = expertAttach;
		this.report = report;
		this.reportAttach = reportAttach;
		this.completion = completion;
		this.completionAttach = completionAttach;
		this.valuation = valuation;
		this.valuationAttach = valuationAttach;
		this.createUser = createUser;
		this.createTime = createTime;
		this.status = status;
		this.command = command;
		this.delFlag = delFlag;
		this.projectName = projectName;
		this.siteid = siteid;
		this.deptid = deptid;
		this.projectLeader = projectLeader;
		this.contractName = contractName;
		this.checkStage = checkStage;
		this.updateTime = updateTime;
		this.updateUser = updateUser;
		this.firstParty = firstParty;
		this.fpLeader = fpLeader;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCondition() {
		return this.condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getConditionAttach() {
		return this.conditionAttach;
	}

	public void setConditionAttach(String conditionAttach) {
		this.conditionAttach = conditionAttach;
	}

	public String getStandard() {
		return this.standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getStandardAttach() {
		return this.standardAttach;
	}

	public void setStandardAttach(String standardAttach) {
		this.standardAttach = standardAttach;
	}

	public String getExpert() {
		return this.expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public String getExpertAttach() {
		return this.expertAttach;
	}

	public void setExpertAttach(String expertAttach) {
		this.expertAttach = expertAttach;
	}

	public String getReport() {
		return this.report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getReportAttach() {
		return this.reportAttach;
	}

	public void setReportAttach(String reportAttach) {
		this.reportAttach = reportAttach;
	}

	public String getCompletion() {
		return this.completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
	}

	public String getCompletionAttach() {
		return this.completionAttach;
	}

	public void setCompletionAttach(String completionAttach) {
		this.completionAttach = completionAttach;
	}

	public String getValuation() {
		return this.valuation;
	}

	public void setValuation(String valuation) {
		this.valuation = valuation;
	}

	public String getValuationAttach() {
		return this.valuationAttach;
	}

	public void setValuationAttach(String valuationAttach) {
		this.valuationAttach = valuationAttach;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Boolean getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

	public String getProjectLeader() {
		return this.projectLeader;
	}

	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}

	public String getContractName() {
		return this.contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getCheckStage() {
		return this.checkStage;
	}

	public void setCheckStage(String checkStage) {
		this.checkStage = checkStage;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getFirstParty() {
		return this.firstParty;
	}

	public void setFirstParty(String firstParty) {
		this.firstParty = firstParty;
	}

	public String getFpLeader() {
		return this.fpLeader;
	}

	public void setFpLeader(String fpLeader) {
		this.fpLeader = fpLeader;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public Integer getPayplanId() {
		return payplanId;
	}

	public void setPayplanId(Integer payplanId) {
		this.payplanId = payplanId;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getIsProjectChange() {
		return isProjectChange;
	}

	public void setIsProjectChange(String isProjectChange) {
		this.isProjectChange = isProjectChange;
	}

	public String getPayDecription() {
		return payDecription;
	}

	public void setPayDecription(String payDecription) {
		this.payDecription = payDecription;
	}

	

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getProcessInstId() {
		return processInstId;
	}

	public void setProcessInstId(String processInstId) {
		this.processInstId = processInstId;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}
	
	
	
}