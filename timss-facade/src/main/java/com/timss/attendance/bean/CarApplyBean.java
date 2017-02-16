package com.timss.attendance.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @description:用车申请
 * @author: 
 * @createDate: 
 * @param 
 * @return:
 */
public class CarApplyBean extends ItcMvcBean{

	private static final long serialVersionUID = 1053079857737078205L;
	
	@UUIDGen(requireType = GenerationType.REQUIRED_NEW) // 自动生成id
	private String caId;//id
	
	@AutoGen(value = "ATD_CARAPPLY_CODE" ,requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW)
	private String caNum;//申请单编号
	
	private String destinationType;//目的地类别
	private String destination;//目的地
	private String phone;//用车人电话
	private Date startTime;//用车开始时间
	private Date endTime;//用车结束时间
	private String reason;//用车理由
	private String togethers;//同车人姓名
	private String carType;//用车种类（车牌号）
	private String driver;//驾驶员
	private String createUser;//申请人
	private Date createDate;//申请时间
	private String modifyUser;//修改人
	private Date modifyDate;//修改时间
	private String siteId;//站点
	private String depId;//部门
	private String workflowId;//流程ID
	private String status;//状态
	private String delInd;//删除标识
	private String currHandUser;//当前处理人
	
	private String[] uploadIds;//附件id
	
	public String[] getUploadIds() {
		return uploadIds;
	}
	public void setUploadIds(String[] uploadIds) {
		this.uploadIds = uploadIds;
	}
	public String getCurrHandUser() {
		return currHandUser;
	}
	public void setCurrHandUser(String currHandUser) {
		this.currHandUser = currHandUser;
	}
	private String createUserName;//申请人名
	private String deptName;//部门名称
	
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getCaId() {
		return caId;
	}
	public void setCaId(String caId) {
		this.caId = caId;
	}
	public String getCaNum() {
		return caNum;
	}
	public void setCaNum(String caNum) {
		this.caNum = caNum;
	}
	public String getDestinationType() {
		return destinationType;
	}
	public void setDestinationType(String destinationType) {
		this.destinationType = destinationType;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTogethers() {
		return togethers;
	}
	public void setTogethers(String togethers) {
		this.togethers = togethers;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDelInd() {
		return delInd;
	}
	public void setDelInd(String delInd) {
		this.delInd = delInd;
	}
	
	
}
