package com.timss.workorder.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcMvcBean;

public class Workapply extends ItcMvcBean{
    
        private static final long serialVersionUID = -6580501429464300698L;
        @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
        private String id; // ID
	@AutoGen(value = "WO_WORKAPPLY", requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW)
	private String workapplyCode; // 申请编号
	private String woId; // 关联工单ID
	private String woCode; // 关联工单编号
	private String name; // 工程名称
	private String workCom; //施工单位
	private String workPrincipal; // 施工负责
	private String safePrincipal; // 安全负责
	private String workCondition; // 施工条件
	private String safeItems; // 安全措施
	private String checkLevel; // 安全措施
	private Date startTime; //开工日期
	private Date endTime;// 计划竣工日期
	private String applicant;// 申请人
	private String applicantName;// 申请人
	private Date applyTime;// 申请日期
	private String currHandler;// 当前处理人
	private String currHandlerName;// 当前处理人名
	private String applyStatus;// 状态
	private String workflowId;// 工作流实例ID
	private String conditionConfirmUser;// 开工条件确认人
	private String safeConfirmUser;// 安全措施确认人
	private String safeInformUser;// 安全交底人
	private String delFlag;  //删除标识
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the workapplyCode
     */
    public String getWorkapplyCode() {
        return workapplyCode;
    }
    /**
     * @param workapplyCode the workapplyCode to set
     */
    public void setWorkapplyCode(String workapplyCode) {
        this.workapplyCode = workapplyCode;
    }
    /**
     * @return the woId
     */
    public String getWoId() {
        return woId;
    }
    /**
     * @param woId the woId to set
     */
    public void setWoId(String woId) {
        this.woId = woId;
    }
    /**
     * @return the woCode
     */
    public String getWoCode() {
        return woCode;
    }
    /**
     * @param woCode the woCode to set
     */
    public void setWoCode(String woCode) {
        this.woCode = woCode;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the applicantName
     */
    public String getApplicantName() {
        return applicantName;
    }
    /**
     * @param applicantName the applicantName to set
     */
    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }
    /**
     * @return the workCom
     */
    public String getWorkCom() {
        return workCom;
    }
    /**
     * @param workCom the workCom to set
     */
    public void setWorkCom(String workCom) {
        this.workCom = workCom;
    }
    /**
     * @return the workPrincipal
     */
    public String getWorkPrincipal() {
        return workPrincipal;
    }
    /**
     * @param workPrincipal the workPrincipal to set
     */
    public void setWorkPrincipal(String workPrincipal) {
        this.workPrincipal = workPrincipal;
    }
    /**
     * @return the safePrincipal
     */
    public String getSafePrincipal() {
        return safePrincipal;
    }
    /**
     * @param safePrincipal the safePrincipal to set
     */
    public void setSafePrincipal(String safePrincipal) {
        this.safePrincipal = safePrincipal;
    }
    /**
     * @return the workCondition
     */
    public String getWorkCondition() {
        return workCondition;
    }
    /**
     * @param workCondition the workCondition to set
     */
    public void setWorkCondition(String workCondition) {
        this.workCondition = workCondition;
    }
    /**
     * @return the safeItems
     */
    public String getSafeItems() {
        return safeItems;
    }
    /**
     * @param safeItems the safeItems to set
     */
    public void setSafeItems(String safeItems) {
        this.safeItems = safeItems;
    }
    /**
     * @return the checkLevel
     */
    public String getCheckLevel() {
        return checkLevel;
    }
    /**
     * @param checkLevel the checkLevel to set
     */
    public void setCheckLevel(String checkLevel) {
        this.checkLevel = checkLevel;
    }
    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }
    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }
    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    /**
     * @return the applicant
     */
    public String getApplicant() {
        return applicant;
    }
    /**
     * @param applicant the applicant to set
     */
    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }
    /**
     * @return the applyTime
     */
    public Date getApplyTime() {
        return applyTime;
    }
    /**
     * @param applyTime the applyTime to set
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }
    /**
     * @return the currHandler
     */
    public String getCurrHandler() {
        return currHandler;
    }
    /**
     * @param currHandler the currHandler to set
     */
    public void setCurrHandler(String currHandler) {
        this.currHandler = currHandler;
    }
    /**
     * @return the applyStatus
     */
    public String getApplyStatus() {
        return applyStatus;
    }
    /**
     * @param applyStatus the applyStatus to set
     */
    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }
    /**
     * @return the workflowId
     */
    public String getWorkflowId() {
        return workflowId;
    }
    /**
     * @param workflowId the workflowId to set
     */
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }
    
    /**
     * @return the conditionConfirmUser
     */
    public String getConditionConfirmUser() {
        return conditionConfirmUser;
    }
    /**
     * @param conditionConfirmUser the conditionConfirmUser to set
     */
    public void setConditionConfirmUser(String conditionConfirmUser) {
        this.conditionConfirmUser = conditionConfirmUser;
    }
    /**
     * @return the safeConfirmUser
     */
    public String getSafeConfirmUser() {
        return safeConfirmUser;
    }
    /**
     * @param safeConfirmUser the safeConfirmUser to set
     */
    public void setSafeConfirmUser(String safeConfirmUser) {
        this.safeConfirmUser = safeConfirmUser;
    }
    /**
     * @return the delFlag
     */
    public String getDelFlag() {
        return delFlag;
    }
    /**
     * @param delFlag the delFlag to set
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    
    /**
     * @return the currHandlerName
     */
    public String getCurrHandlerName() {
        return currHandlerName;
    }
    /**
     * @param currHandlerName the currHandlerName to set
     */
    public void setCurrHandlerName(String currHandlerName) {
        this.currHandlerName = currHandlerName;
    }
    
    /**
     * @return the safeInformUser
     */
    public String getSafeInformUser() {
        return safeInformUser;
    }
    /**
     * @param safeInformUser the safeInformUser to set
     */
    public void setSafeInformUser(String safeInformUser) {
        this.safeInformUser = safeInformUser;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Workapply [id=" + id + ", workapplyCode=" + workapplyCode + ", woId=" + woId + ", woCode=" + woCode
                + ", name=" + name + ", workCom=" + workCom + ", workPrincipal=" + workPrincipal + ", safePrincipal="
                + safePrincipal + ", workCondition=" + workCondition + ", safeItems=" + safeItems + ", checkLevel="
                + checkLevel + ", startTime=" + startTime + ", endTime=" + endTime + ", applicant=" + applicant
                + ", applyTime=" + applyTime + ", currHandler=" + currHandler + ", applyStatus=" + applyStatus
                + ", workflowId=" + workflowId + ", delFlag=" + delFlag + "]";
    }
   
	

}
