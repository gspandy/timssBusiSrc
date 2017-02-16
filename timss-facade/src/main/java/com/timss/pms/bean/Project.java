package com.timss.pms.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * TimPmsProject entity. @author MyEclipse Persistence Tools
 */

public class Project extends ItcMvcBean {

	// Fields

	private Integer id;
	private String pyear;
	private Boolean iseasy;
	private String ptype;
	private int planId;
	private String projectCode;
	private String projectName;
	private String property;
	private Boolean bhzc;
	private Integer period;
	private String ydnum;
	private String dept;
	private String projectLeader;
	private String businessLeader;
	private String domain;
	private String unit;
	private String level;
	private String referInfo;
	private Double applyBudget;
	private Double apprBudget;
	private String command;
	private String rpAttach;
	private String techCommand;
	private String techAttach;
	private Boolean isRs;
	private String rsAttach;
	private String projectExplain;
	private String peAttach;
	private String budgetExplain;
	private String beAttach;
	private Date startTime;
	private Date endTime;
	private String attach;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private String auditStatus;
	private String businessStatus;
	private Boolean delFlag;
	private String customManager;
	private String siteid;
	private String deptid;
	private String status;
	
	private String bidCompName;
	private String bidCompId;
	private String companyId;
	
	private String contractingMode;
	private String contractAward;
	
	private String needDSZ;
	private String flowNo;

	// Constructors

	/** default constructor */
	public Project() {
	}

	/** minimal constructor */
	public Project(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public Project(Integer id, String pyear, Boolean iseasy,
			String ptype, int planId, String projectCode,
			String projectName, String property, Boolean bhzc, Integer period,
			String ydnum, String dept, String projectLeader,
			String businessLeader, String domain, String unit, String level,
			String referInfo, Double applyBudget, Double apprBudget,
			String command, String rpAttach, String techCommand,
			String techAttach, Boolean isRs, String rsAttach,
			String projectExplain, String peAttach, String budgetExplain,
			String beAttach, Date startTime, Date endTime, String attach,
			String createUser, Date createTime, String updateUser,
			Date updateTime, String auditStatus, String businessStatus,
			Boolean delFlag, String customManager, String siteid,
			String deptid) {
		this.id = id;
		this.pyear = pyear;
		this.iseasy = iseasy;
		this.ptype = ptype;
		this.planId = planId;
		this.projectCode = projectCode;
		this.projectName = projectName;
		this.property = property;
		this.bhzc = bhzc;
		this.period = period;
		this.ydnum = ydnum;
		this.dept = dept;
		this.projectLeader = projectLeader;
		this.businessLeader = businessLeader;
		this.domain = domain;
		this.unit = unit;
		this.level = level;
		this.referInfo = referInfo;
		this.applyBudget = applyBudget;
		this.apprBudget = apprBudget;
		this.command = command;
		this.rpAttach = rpAttach;
		this.techCommand = techCommand;
		this.techAttach = techAttach;
		this.isRs = isRs;
		this.rsAttach = rsAttach;
		this.projectExplain = projectExplain;
		this.peAttach = peAttach;
		this.budgetExplain = budgetExplain;
		this.beAttach = beAttach;
		this.startTime = startTime;
		this.endTime = endTime;
		this.attach = attach;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.auditStatus = auditStatus;
		this.businessStatus = businessStatus;
		this.delFlag = delFlag;
		this.customManager = customManager;
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

	public String getPyear() {
		return this.pyear;
	}

	public void setPyear(String pyear) {
		this.pyear = pyear;
	}

	public Boolean getIseasy() {
		return this.iseasy;
	}

	public void setIseasy(Boolean iseasy) {
		this.iseasy = iseasy;
	}

	public String getPtype() {
		return this.ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public int getPlanId() {
		return this.planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public String getProjectCode() {
		return this.projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProperty() {
		return this.property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Boolean getBhzc() {
		return this.bhzc;
	}

	public void setBhzc(Boolean bhzc) {
		this.bhzc = bhzc;
	}

	public Integer getPeriod() {
		return this.period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getYdnum() {
		return this.ydnum;
	}

	public void setYdnum(String ydnum) {
		this.ydnum = ydnum;
	}

	public String getDept() {
		return this.dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getProjectLeader() {
		return this.projectLeader;
	}

	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}

	public String getBusinessLeader() {
		return this.businessLeader;
	}

	public void setBusinessLeader(String businessLeader) {
		this.businessLeader = businessLeader;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getReferInfo() {
		return this.referInfo;
	}

	public void setReferInfo(String referInfo) {
		this.referInfo = referInfo;
	}

	public Double getApplyBudget() {
		return this.applyBudget;
	}

	public void setApplyBudget(Double applyBudget) {
		this.applyBudget = applyBudget;
	}

	public Double getApprBudget() {
		return this.apprBudget;
	}

	public void setApprBudget(Double apprBudget) {
		this.apprBudget = apprBudget;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getRpAttach() {
		return this.rpAttach;
	}

	public void setRpAttach(String rpAttach) {
		this.rpAttach = rpAttach;
	}

	public String getTechCommand() {
		return this.techCommand;
	}

	public void setTechCommand(String techCommand) {
		this.techCommand = techCommand;
	}

	public String getTechAttach() {
		return this.techAttach;
	}

	public void setTechAttach(String techAttach) {
		this.techAttach = techAttach;
	}

	public Boolean getIsRs() {
		return this.isRs;
	}

	public void setIsRs(Boolean isRs) {
		this.isRs = isRs;
	}

	public String getRsAttach() {
		return this.rsAttach;
	}

	public void setRsAttach(String rsAttach) {
		this.rsAttach = rsAttach;
	}

	public String getProjectExplain() {
		return this.projectExplain;
	}

	public void setProjectExplain(String projectExplain) {
		this.projectExplain = projectExplain;
	}

	public String getPeAttach() {
		return this.peAttach;
	}

	public void setPeAttach(String peAttach) {
		this.peAttach = peAttach;
	}

	public String getBudgetExplain() {
		return this.budgetExplain;
	}

	public void setBudgetExplain(String budgetExplain) {
		this.budgetExplain = budgetExplain;
	}

	public String getBeAttach() {
		return this.beAttach;
	}

	public void setBeAttach(String beAttach) {
		this.beAttach = beAttach;
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

	public String getAttach() {
		return this.attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}


	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getBusinessStatus() {
		return this.businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public Boolean getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public String getCustomManager() {
		return this.customManager;
	}

	public void setCustomManager(String customManager) {
		this.customManager = customManager;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getBidCompName() {
		return bidCompName;
	}

	public void setBidCompName(String bidCompName) {
		this.bidCompName = bidCompName;
	}

	public String getBidCompId() {
		return bidCompId;
	}

	public void setBidCompId(String bidCompId) {
		this.bidCompId = bidCompId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getContractingMode() {
		return contractingMode;
	}

	public void setContractingMode(String contractingMode) {
		this.contractingMode = contractingMode;
	}

	public String getContractAward() {
		return contractAward;
	}

	public void setContractAward(String contractAward) {
		this.contractAward = contractAward;
	}

	public String getNeedDSZ() {
		return needDSZ;
	}

	public void setNeedDSZ(String needDSZ) {
		this.needDSZ = needDSZ;
	}

        public String getFlowNo() {
            return flowNo;
        }
    
        public void setFlowNo(String flowNo) {
            this.flowNo = flowNo;
        }
	
}