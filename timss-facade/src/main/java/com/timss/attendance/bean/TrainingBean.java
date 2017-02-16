package com.timss.attendance.bean;

import java.util.Date;
import java.util.List;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.EntityID;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 培训申请
 * @description: {desc}
 * @company: gdyd
 * @className: TrainingBean.java
 * @author: yyn
 * @createDate: 2016年9月8日
 * @updateUser: yyn
 * @version: 1.0
 */
public class TrainingBean extends ItcMvcBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6717580265622602930L;
	/**
     * 使用组件的uuid
     */
    @UUIDGen(requireType=GenerationType.REQUIRED_NEW)
    @EntityID
    private String trainingId;
    /**
     * 培训申请单号
     */
    @AutoGen(value="ATD_TRAINING_NUM_SEQ")
    private String trainingNo;
    /**
     * 工作流实例id
     */
    private String instanceId;
    /**
     * 申请单状态
     */
    private String status;
    /**
     * 培训开始日期
     */
    private Date startDate;
    /**
     * 培训结束日期
     */
    private Date endDate;
    /**
     * 培训学时
     */
    private Double hour;
    /**
     * 培训内容
     */
    private String content;
    /**
     * 培训费用
     */
    private Double cost;
    /**
     * 培训类别
     */
    private String trainingType;
    /**
     * 申请理由
     */
    private String reason;
    /**
     * 评价方式
     */
    private String evaluateType;
    /**
     * 主办单位
     */
    private String sponsor;
    /**
     * 地点
     */
    private String address;
    /**
     * 所属部门
     */
    private String deptName;
    /**
     * 申请人姓名
     */
    private String userName;
    private List<TrainingItemBean>itemList;
    private List<TrainingItemBean>addItemList;
    private List<TrainingItemBean>delItemList;
    private List<TrainingItemBean>updateItemList;
    private String[]fileIds; 
    
    /**
     * 当前任务id
     */
    private String taskId;
    /**
     * 是否审批状态
     */
    private Boolean isAudit;
    /**
     * 培训成绩
     */
    private String grades;
    /**
     * 培训证书
     */
    private String certificate;
    /**
     * 是否附培训申请表
     */
    private String isFile;
    /**
     * 资料清单
     */
    private String fileList;
    
	public String getGrades() {
		return grades;
	}
	public void setGrades(String grades) {
		this.grades = grades;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getIsFile() {
		return isFile;
	}
	public void setIsFile(String isFile) {
		this.isFile = isFile;
	}
	public String getFileList() {
		return fileList;
	}
	public void setFileList(String fileList) {
		this.fileList = fileList;
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
	public List<TrainingItemBean> getItemList() {
		return itemList;
	}
	public void setItemList(List<TrainingItemBean> itemList) {
		this.itemList = itemList;
	}
	public List<TrainingItemBean> getAddItemList() {
		return addItemList;
	}
	public void setAddItemList(List<TrainingItemBean> addItemList) {
		this.addItemList = addItemList;
	}
	public List<TrainingItemBean> getDelItemList() {
		return delItemList;
	}
	public void setDelItemList(List<TrainingItemBean> delItemList) {
		this.delItemList = delItemList;
	}
	public List<TrainingItemBean> getUpdateItemList() {
		return updateItemList;
	}
	public void setUpdateItemList(List<TrainingItemBean> updateItemList) {
		this.updateItemList = updateItemList;
	}
	public String[] getFileIds() {
		return fileIds;
	}
	public void setFileIds(String[] fileIds) {
		this.fileIds = fileIds;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTrainingId() {
		return trainingId;
	}
	public void setTrainingId(String trainingId) {
		this.trainingId = trainingId;
	}
	public String getTrainingNo() {
		return trainingNo;
	}
	public void setTrainingNo(String trainingNo) {
		this.trainingNo = trainingNo;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Double getHour() {
		return hour;
	}
	public void setHour(Double hour) {
		this.hour = hour;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public String getTrainingType() {
		return trainingType;
	}
	public void setTrainingType(String trainingType) {
		this.trainingType = trainingType;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getEvaluateType() {
		return evaluateType;
	}
	public void setEvaluateType(String evaluateType) {
		this.evaluateType = evaluateType;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
