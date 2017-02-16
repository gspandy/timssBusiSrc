package com.timss.itsm.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmQuestionRd extends ItcMvcBean{
	  
	private static final long serialVersionUID = 7043899549550808552L;
	private int id; //ID
	
	@AutoGen(value = "ITSM_QUESTION_CODE" ,requireType = GenerationType.REQUIRED_NEW)
	private String code; //问题编号
	private String klQuestionCode;//关联知识编号
	private String eventQuestionCode;//关联事件编号
	private String title;//问题标题
	private String resourceid;//来源
	private String category;//分类id
	private String priorityid;//优先级
	private String desp;//问题描述
	private String na;//是否需要解决问题
	private String ns;//是否需要入知识库
	private String reason;//问题原因
	private String opinion;//问题经理审批建议
	private String solve;//问题解决方案
	private String status;//状态
	private String step;//环节(暂时没有用到)
	private String workflowId;
	private String delFlag;
	private String deptName;
	private String createUserName;
	private String categoryName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getKlQuestionCode() {
		return klQuestionCode;
	}
	public void setKlQuestionCode(String klQuestionCode) {
		this.klQuestionCode = klQuestionCode;
	}
	public String getEventQuestionCode() {
		return eventQuestionCode;
	}
	public void setEventQuestionCodeString(String eventQuestionCode) {
		this.eventQuestionCode = eventQuestionCode;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getResourceid() {
		return resourceid;
	}
	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPriorityid() {
		return priorityid;
	}
	public void setPriorityid(String priorityid) {
		this.priorityid = priorityid;
	}
	public String getDesp() {
		return desp;
	}
	public void setDesp(String desp) {
		this.desp = desp;
	}
	public String getNa() {
		return na;
	}
	public void setNa(String na) {
		this.na = na;
	}
	public String getNs() {
		return ns;
	}
	public void setNs(String ns) {
		this.ns = ns;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getSolve() {
		return solve;
	}
	public void setSolve(String solve) {
		this.solve = solve;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public void setEventQuestionCode(String eventQuestionCode) {
		this.eventQuestionCode = eventQuestionCode;
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
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	@Override
	public String toString() {
		return "ItsmQuestionRd [id=" + id + ", code=" + code
				+ ", klQuestionCode=" + klQuestionCode + ", eventQuestionCode="
				+ eventQuestionCode + ", title=" + title + ", resourceid="
				+ resourceid + ", category=" + category + ", priorityid="
				+ priorityid + ", desp=" + desp + ", na=" + na + ", ns=" + ns
				+ ", reason=" + reason + ", opinion=" + opinion + ", solve="
				+ solve + ", status=" + status + ", step=" + step
				+ ", workflowId=" + workflowId + ", delFlag=" + delFlag
				+ ", deptName=" + deptName + ", createUserName="
				+ createUserName + ", categoryName=" + categoryName + "]";
	}
}
