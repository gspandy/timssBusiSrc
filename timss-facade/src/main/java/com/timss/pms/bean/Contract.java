package com.timss.pms.bean;

import java.util.Date;
import java.util.Set;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 合同bean
 * @ClassName:     Contract
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-7 下午4:12:25
 */
public class Contract extends ItcMvcBean {

	// Fields
	

	private Integer id;
	private String contractCode;
	private Integer projectId;
	private String name;
	private String type;
	private String firstParty;
	private String fpLeader;
	private String fpPhone;
	private String secondParty;
	private String spLeader;
	private String spPhone;
	private Integer planId;
	private Double totalSum;
	private Date startTime;
	private Date endTime;
	private Date signTime;
	private String command;
	private String zbq;
	private String attach;
	private Boolean delFlag;
	private String auditStatus;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private String businessStatus;
	private String relatedContract;
	private String siteid;
	private String deptid;
	private Double payedSum;
	private String status;
	private String statusApp;
	private Integer bidId;
    private String firstPartyId;
    private String secondPartyId;
    private String contractCategory;
    private String createuser;
    @AutoGen(value="PMS_PROJECT_ADD",requireType=GenerationType.REQUIRED_NEW)
    private String tipNo;
    
    private String projectName;
    private String belongTo;
    private String bidName;
    private String processInstId;
    private String flowStatus;
    private String qaTime;
    private String taxRate;
    private String nullifyProcInstId;
	// Constructors

	/** default constructor */
	public Contract() {
	}

	/** minimal constructor */
	public Contract(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public Contract(Integer id, String contractCode, Integer projectId,
			String name, String type, String firstParty, String fpLeader,
			String fpPhone, String secondParty, String spLeader,
			String spPhone, Integer planId, Double totalSum, Date startTime,
			Date endTime, Date signTime, String command, String zbq,
			String attach, Boolean delFlag, String auditStatus,
			String createUser, Date createTime, String updateUser,
			Date updateTime, String businessStatus, String relatedContract,
			String siteid, String deptid, Double payedSum, Set invoices,
			Set timPmsPaies,String contractCategory,String taxRate,String qaTime,String nullifyProcInstId) {
		this.id = id;
		this.contractCode = contractCode;
		this.projectId = projectId;
		this.name = name;
		this.type = type;
		this.firstParty = firstParty;
		this.fpLeader = fpLeader;
		this.fpPhone = fpPhone;
		this.secondParty = secondParty;
		this.spLeader = spLeader;
		this.spPhone = spPhone;
		this.planId = planId;
		this.totalSum = totalSum;
		this.startTime = startTime;
		this.endTime = endTime;
		this.signTime = signTime;
		this.command = command;
		this.zbq = zbq;
		this.attach = attach;
		this.delFlag = delFlag;
		this.auditStatus = auditStatus;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.businessStatus = businessStatus;
		this.relatedContract = relatedContract;
		this.siteid = siteid;
		this.deptid = deptid;
		this.payedSum = payedSum;
		this.contractCategory = contractCategory;
		this.taxRate = taxRate;
		this.qaTime = qaTime;
		this.nullifyProcInstId = nullifyProcInstId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContractCode() {
		return this.contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getFpPhone() {
		return this.fpPhone;
	}

	public void setFpPhone(String fpPhone) {
		this.fpPhone = fpPhone;
	}

	public String getSecondParty() {
		return this.secondParty;
	}

	public void setSecondParty(String secondParty) {
		this.secondParty = secondParty;
	}

	public String getSpLeader() {
		return this.spLeader;
	}

	public void setSpLeader(String spLeader) {
		this.spLeader = spLeader;
	}

	public String getSpPhone() {
		return this.spPhone;
	}

	public void setSpPhone(String spPhone) {
		this.spPhone = spPhone;
	}

	public Integer getPlanId() {
		return this.planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	public Double getTotalSum() {
		return this.totalSum;
	}

	public void setTotalSum(Double totalSum) {
		this.totalSum = totalSum;
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

	public Date getSignTime() {
		return this.signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getZbq() {
		return this.zbq;
	}

	public void setZbq(String zbq) {
		this.zbq = zbq;
	}

	public String getAttach() {
		return this.attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public Boolean getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
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

	public String getBusinessStatus() {
		return this.businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public String getRelatedContract() {
		return this.relatedContract;
	}

	public void setRelatedContract(String relatedContract) {
		this.relatedContract = relatedContract;
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

	public Double getPayedSum() {
		return this.payedSum;
	}

	public void setPayedSum(Double payedSum) {
		this.payedSum = payedSum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusApp() {
		return statusApp;
	}

	public void setStatusApp(String statusApp) {
		this.statusApp = statusApp;
	}

	public Integer getBidId() {
		return bidId;
	}

	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}

	public String getFirstPartyId() {
		return firstPartyId;
	}

	public void setFirstPartyId(String firstPartyId) {
		this.firstPartyId = firstPartyId;
	}

	public String getSecondPartyId() {
		return secondPartyId;
	}

	public void setSecondPartyId(String secondPartyId) {
		this.secondPartyId = secondPartyId;
	}

	public String getContractCategory() {
		return contractCategory;
	}

	public void setContractCategory(String contractCategory) {
		this.contractCategory = contractCategory;
	}

	public String getTipNo() {
		return tipNo;
	}

	public void setTipNo(String tipNo) {
		this.tipNo = tipNo;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}

	public String getBidName() {
		return bidName;
	}

	public void setBidName(String bidName) {
		this.bidName = bidName;
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

        public String getQaTime() {
            return qaTime;
        }
    
        public void setQaTime(String qaTime) {
            this.qaTime = qaTime;
        }
    
        public String getTaxRate() {
            return taxRate;
        }
    
        public void setTaxRate(String taxRate) {
            this.taxRate = taxRate;
        }

        public String getNullifyProcInstId() {
            return nullifyProcInstId;
        }

        public void setNullifyProcInstId(String nullifyProcInstId) {
            this.nullifyProcInstId = nullifyProcInstId;
        }
	
}