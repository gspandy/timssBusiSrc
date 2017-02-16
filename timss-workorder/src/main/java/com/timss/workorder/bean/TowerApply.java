package com.timss.workorder.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class TowerApply extends ItcMvcBean{
    
	
	private static final long serialVersionUID = -5387427917736676296L;
	
	@UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String id; // ID
	@AutoGen(value = "WO_TOWERAPPLY", requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW)
	private String applyCode; // 申请单编号
	private String applyCompany; // 单位名称
	private String towerPeople; // 登塔人员
	private String cabinRoof; // 出机舱顶人员
	private String windGuardian; // 风电场监护人
	private String safetyTrainer; // 安全带培训人
	private String slideBlockTrainer; // 防坠滑块培训人
	private String climbTrainer; // 助爬器培训人
	private String applyReason; // 登塔、出机舱顶原因
	private String currHandler;//当前处理人
	private String currHandlerName;//当前处理人
	
	private String applicantNo;// 申请人
	private String applicant;//申请人
	private Date applyTime;// 申请日期
	private String applyStatus;
	
	private String createuser;// 创建人
	private String createuserName;//创建人
	private Date createDate;// 创建日期
	private String modifyuser;// 最近更新人
	private String modifyuserName;// 最近更新人
	private Date modifyDate;// 最近更新日期
	private String workflowId;// 工作流实例ID
	private String delFlag;  //删除标识
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getApplyCode() {
		return applyCode;
	}


	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}


	public String getApplyCompany() {
		return applyCompany;
	}


	public void setApplyCompany(String applyCompany) {
		this.applyCompany = applyCompany;
	}


	public String getTowerPeople() {
		return towerPeople;
	}


	public void setTowerPeople(String towerPeople) {
		this.towerPeople = towerPeople;
	}


	public String getCabinRoof() {
		return cabinRoof;
	}


	public void setCabinRoof(String cabinRoof) {
		this.cabinRoof = cabinRoof;
	}


	public String getWindGuardian() {
		return windGuardian;
	}


	public void setWindGuardian(String windGuardian) {
		this.windGuardian = windGuardian;
	}


	public String getSafetyTrainer() {
		return safetyTrainer;
	}


	public void setSafetyTrainer(String safetyTrainer) {
		this.safetyTrainer = safetyTrainer;
	}


	public String getSlideBlockTrainer() {
		return slideBlockTrainer;
	}


	public void setSlideBlockTrainer(String slideBlockTrainer) {
		this.slideBlockTrainer = slideBlockTrainer;
	}


	public String getClimbTrainer() {
		return climbTrainer;
	}


	public void setClimbTrainer(String climbTrainer) {
		this.climbTrainer = climbTrainer;
	}


	public String getApplyReason() {
		return applyReason;
	}


	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	
	

	public String getCurrHandler() {
		return currHandler;
	}


	public void setCurrHandler(String currHandler) {
		this.currHandler = currHandler;
	}
	
	

	public String getCurrHandlerName() {
		return currHandlerName;
	}


	public void setCurrHandlerName(String currHandlerName) {
		this.currHandlerName = currHandlerName;
	}


	public String getApplicantNo() {
		return applicantNo;
	}


	public void setApplicantNo(String applicantNo) {
		this.applicantNo = applicantNo;
	}


	public String getApplicant() {
		return applicant;
	}


	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}


	public Date getApplyTime() {
		return applyTime;
	}


	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	
	
	public String getApplyStatus() {
		return applyStatus;
	}


	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}


	public String getCreateuser() {
		return createuser;
	}


	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}


	public String getCreateuserName() {
		return createuserName;
	}


	public void setCreateuserName(String createuserName) {
		this.createuserName = createuserName;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public String getModifyuser() {
		return modifyuser;
	}


	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}


	public String getModifyuserName() {
		return modifyuserName;
	}


	public void setModifyuserName(String modifyuserName) {
		this.modifyuserName = modifyuserName;
	}


	public Date getModifyDate() {
		return modifyDate;
	}


	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}


	public String getWorkflowId() {
		return workflowId;
	}


	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}


	public String getDelFlag() {
		return delFlag;
	}


	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}


	@Override
	public String toString() {
		return "TowerApply [id=" + id + ", applyCode=" + applyCode
				+ ", applyCompany=" + applyCompany + ", towerPeople="
				+ towerPeople + ", cabinRoof=" + cabinRoof + ", windGuardian="
				+ windGuardian + ", safetyTrainer=" + safetyTrainer
				+ ", slideBlockTrainer=" + slideBlockTrainer
				+ ", climbTrainer=" + climbTrainer + ", applyReason="
				+ applyReason + ", createuser=" + createuser
				+ ", createuserName=" + createuserName + ", createDate="
				+ createDate + ", modifyuser=" + modifyuser
				+ ", modifyuserName=" + modifyuserName + ", modifyDate="
				+ modifyDate + ", workflowId=" + workflowId + ", delFlag="
				+ delFlag + "]";
	}
	
	
   
	

}
