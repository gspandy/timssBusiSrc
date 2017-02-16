package com.timss.attendance.bean;

import java.util.List;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.EntityID;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 合理化建议
 * @description: {desc}
 * @company: gdyd
 * @className: RationalzationBean.java
 * @author: liuk
 * @createDate: 2016年10月13日
 * @updateUser: 13
 * @version: 1.0
 */
public class RationalizationBean extends ItcMvcBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7717580265622602930L;
	
	 /**
     * 工作流实例id
     */
	private String instanceId;
	
	/**
     * 使用组件的uuid
     */
    @UUIDGen(requireType=GenerationType.REQUIRED_NEW)
    @EntityID
    //private String rationalizationId;
    private String rationalId;
    /**
     * 申请编号
     */
    @AutoGen(value="ATD_RATION_NUM_SEQ")
   // private String rationalizationNo;
    private String rationalNo;
     
    /**
     * 申请部门
     */
    private String deptName;
    
    
    /**
     * 处理人 姓名
     */
    private String  handleName;
    /**
     * 申请人姓名
     */
    private String userId;
    /**
     * 申请人姓名
     */
    private String userName;
    /**
     * 专业分类
     */
   // private String rationalzationType;
    private String rationalType;
    
    public String getRationalId() {
		return rationalId;
	}

	public void setRationalId(String rationalId) {
		this.rationalId = rationalId;
	}

	public String getRationalNo() {
		return rationalNo;
	}

	public void setRationalNo(String rationalNo) {
		this.rationalNo = rationalNo;
	}

	public String getRationalType() {
		return rationalType;
	}

	public void setRationalType(String rationalType) {
		this.rationalType = rationalType;
	}

	/*创建人*/
	private String createUser;
		public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/*
     * 建议人Id
     */
    private String  recomIds;
    
    /*
     * 建议人姓名
     */
    private String  recomName;
    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * 建议(改进)项目 
     */
    private String impCon;
    
    /**
     * 效益预测计算 
     */
    private String benCalc;
    
    /**
     * 合理化建议状态
     */
    private String status;
    /**
     * 当前任务id
     */
    private String taskId;

    
    /*处理人*/
    private String curPerson;
    
    public String getCurPerson() {
		return curPerson;
	}

	public void setCurPerson(String curPerson) {
		this.curPerson = curPerson;
	}




	/**
     * 是否审批状态
     */
    private Boolean isAudit;
    
    
    /**
     * 建议(改进)项目Id 
     */
    private Integer impId;
    
   
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

 

	public String getRecomIds() {
		return recomIds;
	}

	public void setRecomIds(String recomIds) {
		this.recomIds = recomIds;
	}

	public String getRecomName() {
		return recomName;
	}

	public void setRecomName(String recomName) {
		this.recomName = recomName;
	}
 
	public String atdAttchid;

	
	public String getAtdAttchid() {
		return atdAttchid;
	}

	public void setAtdAttchid(String atdAttchid) {
		this.atdAttchid = atdAttchid;
	}



	private String[]fileIds; 
    
    public String[] getFileIds() {
		return fileIds;
	}

	public void setFileIds(String[] fileIds) {
		this.fileIds = fileIds;
	}

	public Integer getImpId() {
		return impId;
	}

	public void setImpId(Integer impId) {
		this.impId = impId;
	}

	public String getImpCon() {
		return impCon;
	}

	public void setImpCon(String impCon) {
		this.impCon = impCon;
	}

	public String getBenCalc() {
		return benCalc;
	}

	public void setBenCalc(String benCalc) {
		this.benCalc = benCalc;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
 
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	 

	public String getHandleName() {
		return handleName;
	}

	public void setHandleName(String handleName) {
		this.handleName = handleName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	 

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Boolean getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(Boolean isAudit) {
		this.isAudit = isAudit;
	}
    
    
	/*部门建议人审定是否通过*/
	private String isFile ;
	
	/*对口专业*/
	private String counterPartType; 
 
	public String getCounterPartType() {
		return counterPartType;
	}
		public void setCounterPartType(String counterPartType) {
		this.counterPartType = counterPartType;
	}

	/*实施简况*/	
	private String 	brief;
	
	/*主要实施部门*/
	private String 	majorDept;
	
	/*运行部门*/
	private String OPER_DEPT;
	
	/*建议人奖励*/
	private String bonusSplit;
	/*实施部门奖励*/
	private String impDept;
	
	public String getBonusSplit() {
		return bonusSplit;
	}

	public void setBonusSplit(String bonusSplit) {
		this.bonusSplit = bonusSplit;
	}

	public String getImpDept() {
		return impDept;
	}

	public void setImpDept(String impDept) {
		this.impDept = impDept;
	}

	public String getOPER_DEPT() {
		return OPER_DEPT;
	}

	public void setOPER_DEPT(String oPER_DEPT) {
		OPER_DEPT = oPER_DEPT;
	}

	/*对口专业评审小组是否通过*/
	public String isFile2;


	public String getIsFile2() {
		return isFile2;
	}

	public void setIsFile2(String isFile2) {
		this.isFile2 = isFile2;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getMajorDept() {
		return majorDept;
	}

	public void setMajorDept(String majorDept) {
		this.majorDept = majorDept;
	}

 
	public String getAppraiSal() {
		return appraiSal;
	}

	public void setAppraiSal(String appraiSal) {
		this.appraiSal = appraiSal;
	}

	/*鉴定、评价、评奖或评分*/
	private String appraiSal;
	
	public String getIsFile() {
		return isFile;
	}

	public void setIsFile(String isFile) {
		this.isFile = isFile;
	}

}
