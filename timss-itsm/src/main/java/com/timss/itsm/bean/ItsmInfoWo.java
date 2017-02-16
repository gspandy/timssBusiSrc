package com.timss.itsm.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmInfoWo extends ItcMvcBean{

        private static final long serialVersionUID = -2454005580198542967L;
        @UUIDGen(requireType = GenerationType.REQUIRED_NULL) // 自动生成id
	private String id; // ID
	@AutoGen(value = "ITSM_INFOWO", requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW)
	private String infoWoCode; // 申请单号
	private String name;  //名称
	private String serCata; // 服务目录
	private String serCataName; // 服务目录
	private String serType;//维护类型
	private String businessType; //业务类型
	private String applyUser;  //申请人
	private String applyUserName; //申请人名
	
	private String applyUserPhone; //申请人电话
	private String applyDeptName;  //申请部门
	private Date applyTime;  //申请时间
	private String description;  //情况描述
	private String workflowId;  //工作流实例ID
	private String taskId;  //taskId工作流中参数
	private String currHandler;  //当前处理人
	private String currHandlerName; //当前处理人
	private String status;//状态
	private String uploadIds; //附件ids
	private String delFlag;  //删除标识
	
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getUploadIds() {
        return uploadIds;
    }
    public void setUploadIds(String uploadIds) {
        this.uploadIds = uploadIds;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getInfoWoCode() {
        return infoWoCode;
    }
    public void setInfoWoCode(String infoWoCode) {
        this.infoWoCode = infoWoCode;
    }
    public String getSerCata() {
        return serCata;
    }
    public void setSerCata(String serCata) {
        this.serCata = serCata;
    }
    public String getSerType() {
        return serType;
    }
    public void setSerType(String serType) {
        this.serType = serType;
    }
    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    public String getApplyUser() {
        return applyUser;
    }
    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }
    public String getApplyUserName() {
        return applyUserName;
    }
    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }
    public String getApplyUserPhone() {
        return applyUserPhone;
    }
    public void setApplyUserPhone(String applyUserPhone) {
        this.applyUserPhone = applyUserPhone;
    }
    public String getApplyDeptName() {
        return applyDeptName;
    }
    public void setApplyDeptName(String applyDeptName) {
        this.applyDeptName = applyDeptName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getWorkflowId() {
        return workflowId;
    }
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDelFlag() {
        return delFlag;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSerCataName() {
        return serCataName;
    }
    public void setSerCataName(String serCataName) {
        this.serCataName = serCataName;
    }
    public Date getApplyTime() {
        return applyTime;
    }
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }
    @Override
    public String toString() {
        return "InfoWo [id=" + id + ", infoWoCode=" + infoWoCode + ", serCata=" + serCata + ", serType=" + serType
                + ", businessType=" + businessType + ", applyUser=" + applyUser + ", applyUserName=" + applyUserName
                + ", applyUserPhone=" + applyUserPhone + ", applyDeptName=" + applyDeptName + ", description="
                + description + ", workflowId=" + workflowId + ", currHandler=" + currHandler + ", currHandlerName="
                + currHandlerName + ", status=" + status + ", delFlag=" + delFlag + "]";
    }
	


}
