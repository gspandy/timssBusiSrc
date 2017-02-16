package com.timss.itsm.bean;

import java.io.Serializable;
import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmComplainRd extends ItcMvcBean implements Serializable{

	private static final long serialVersionUID = 1023858590807592197L;
	
	@UUIDGen(requireType = GenerationType.REQUIRED_NULL) // 自动生成id
	private String id;//id
	
	@AutoGen(value = "ITSM_COMPLAINRD_CODE" ,requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW)
	private String code;//编号
	
	private String complainant;//投诉人
	private String complainantCode;//投诉人工号
	private String complainantCom;//投诉人公司
	private String complainantDeptId;//投诉人部门
	private String complainantPosition;//投诉人服务
	private String complainantSiteId;//投诉人站点
	private String phone;//电话
	private String complainStyle;//投诉方式
	private String complainType;//投诉类别
	private String content;//投诉内容
	private String itsmWoId;//关联IT服务单ID
	private Date createDate;//投诉时间
	private String currStatus;//状态
	private String complainHandlerUser;//处理人
	private String complainManager;//投诉经理
	private String complainActive;//是否有效投诉
	private String complainClose;//是否投诉关闭
	private Date handlerDate;//处理时间
	private Date handlerConfirm;//结果确认时间
	private String deptId;//部门
	private String active;//有效标识
	
	private String workflowId; //工单对应的流程ID
	
	private String complainTypeName;//投诉类别名称
	
	private String complainAccept;//是否投诉受理
	
	private String currStatusName;//状态名称
	
	private String handleDept;//大埔处理部门
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

	public String getHandleDept() {
		return handleDept;
	}

	public void setHandleDept(String handleDept) {
		this.handleDept = handleDept;
	}

	public String getcurrStatusName() {
		return currStatusName;
	}

	public void setcurrStatusName(String currStatusName) {
		this.currStatusName = currStatusName;
	}

	public String getComplainAccept() {
		return complainAccept;
	}

	public void setComplainAccept(String complainAccept) {
		this.complainAccept = complainAccept;
	}

	public void setComplainantDeptId(String complainantDeptId) {
		this.complainantDeptId = complainantDeptId;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComplainant() {
		return complainant;
	}

	public void setComplainant(String complainant) {
		this.complainant = complainant;
	}

	public String getComplainantCode() {
		return complainantCode;
	}

	public void setComplainantCode(String complainantCode) {
		this.complainantCode = complainantCode;
	}

	public String getComplainantCom() {
		return complainantCom;
	}

	public void setComplainantCom(String complainantCom) {
		this.complainantCom = complainantCom;
	}

	public String getComplainantDeptId() {
		return complainantDeptId;
	}

	public void setComplainantDeptid(String complainantDeptId) {
		this.complainantDeptId = complainantDeptId;
	}

	public String getComplainantPosition() {
		return complainantPosition;
	}

	public void setComplainantPosition(String complainantPosition) {
		this.complainantPosition = complainantPosition;
	}

	public String getComplainantSiteId() {
		return complainantSiteId;
	}

	public void setComplainantSiteId(String complainantSiteId) {
		this.complainantSiteId = complainantSiteId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getComplainStyle() {
		return complainStyle;
	}

	public void setComplainStyle(String complainStyle) {
		this.complainStyle = complainStyle;
	}

	public String getComplainType() {
		return complainType;
	}

	public void setComplainType(String complainType) {
		this.complainType = complainType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getItsmWoId() {
		return itsmWoId;
	}

	public void setItsmWoId(String itsmWoId) {
		this.itsmWoId = itsmWoId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getcurrStatus() {
		return currStatus;
	}

	public void setcurrStatus(String currStatus) {
		this.currStatus = currStatus;
	}

	public String getComplainHandlerUser() {
		return complainHandlerUser;
	}

	public void setComplainHandlerUser(String complainHandlerUser) {
		this.complainHandlerUser = complainHandlerUser;
	}

	public String getComplainManager() {
		return complainManager;
	}

	public void setComplainManager(String complainManager) {
		this.complainManager = complainManager;
	}

	public String getComplainActive() {
		return complainActive;
	}

	public void setComplainActive(String complainActive) {
		this.complainActive = complainActive;
	}

	public String getComplainClose() {
		return complainClose;
	}

	public void setComplainClose(String complainClose) {
		this.complainClose = complainClose;
	}

	public Date getHandlerDate() {
		return handlerDate;
	}

	public void setHandlerDate(Date handlerDate) {
		this.handlerDate = handlerDate;
	}

	public Date getHandlerConfirm() {
		return handlerConfirm;
	}

	public void setHandlerConfirm(Date handlerConfirm) {
		this.handlerConfirm = handlerConfirm;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getComplainTypeName() {
		return complainTypeName;
	}

	public void setComplainTypeName(String complainTypeName) {
		this.complainTypeName = complainTypeName;
	}

	
}
