package com.timss.attendance.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;
import com.yudean.itc.annotation.AutoGen.GenerationType;

/**
 * @company: gdyd
 * @className: SealApplyBean.java
 * @author: 890199
 * @createDate: 2016-08-29
 * @updateUser: 
 * @version: 1.0
 */
public class SealApplyBean extends ItcMvcBean{

	private static final long serialVersionUID = -5721583522136270850L;
	private String saId;//申请单id
	@AutoGen(value="ATD_SA_NUM_SEQ", requireType=GenerationType.REQUIRED_NULL)
	private String saNo;//申请单号
	private String title;//文件标题
	private String sendCompany;//发往单位
	private String status;//状态
	private String reason;//盖章事由
	private Integer count;//印数
	private String category;//印别
	private String delInd;//删除标志位
	private String instanceId;//流程实例id
	private String approveUserId;
	private String approveUserName;
	private String approveTime;
	private String[] fileIds;
	
	
	public String[] getFileIds() {
		return fileIds;
	}
	public void setFileIds(String[] fileIds) {
		this.fileIds = fileIds;
	}

	//addBy yangk
	private String statusName;//状态名
	
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public String getApproveUserId() {
		return approveUserId;
	}
	public void setApproveUserId(String approveUserId) {
		this.approveUserId = approveUserId;
	}
	public String getApproveUserName() {
		return approveUserName;
	}
	public void setApproveUserName(String approveUserName) {
		this.approveUserName = approveUserName;
	}
	public String getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getSaId() {
		return saId;
	}
	public void setSaId(String saId) {
		this.saId = saId;
	}
	public String getSaNo() {
		return saNo;
	}
	public void setSaNo(String saNo) {
		this.saNo = saNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSendCompany() {
		return sendCompany;
	}
	public void setSendCompany(String sendCompany) {
		this.sendCompany = sendCompany;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDelInd() {
		return delInd;
	}
	public void setDelInd(String delInd) {
		this.delInd = delInd;
	}
	
	@Override
	public String toString(){
		return "SealApply [saId=" + saId + ", saNo=" + saNo
				+ ", title=" + title + ", sendCompany="
				+ sendCompany + ", status=" + status
				+ ", reason=" + reason + ", count="
				+ count + ", category=" + category + ", delInd=" + delInd
				+ ", instanceId=" + instanceId + ", approveUserId="
				+ approveUserId + ", approveUserName=" + approveUserName
				+ ", approveTime=" + approveTime 
				+ "]";
		
	}
}
