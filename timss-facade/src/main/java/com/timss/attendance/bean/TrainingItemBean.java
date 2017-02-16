package com.timss.attendance.bean;

import com.yudean.itc.annotation.EntityID;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 培训申请人员项
 * @description: {desc}
 * @company: gdyd
 * @className: TrainingItemBean.java
 * @author: yyn
 * @createDate: 2016年9月8日
 * @updateUser: yyn
 * @version: 1.0
 */
public class TrainingItemBean extends ItcMvcBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4236977688218810455L;
	/**
     * 使用组件的uuid
     */
    @UUIDGen(requireType=GenerationType.REQUIRED_NEW)
    @EntityID
    private String trainingItemId;
    /**
     * 培训人员姓名
     */
    private String userName;
    /**
     * 学历
     */
    private String education;
    /**
     * 现从事工作或岗位
     */
    private String job;
    /**
     * 所属培训申请单id
     */
    private String trainingId;
    /**
     * 培训人员工号
     */
    private String userId;
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTrainingItemId() {
		return trainingItemId;
	}
	public void setTrainingItemId(String trainingItemId) {
		this.trainingItemId = trainingItemId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getTrainingId() {
		return trainingId;
	}
	public void setTrainingId(String trainingId) {
		this.trainingId = trainingId;
	}
}
