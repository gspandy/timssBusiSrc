package com.timss.itsm.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmKnowledge extends ItcMvcBean{

	private static final long serialVersionUID = -7150725124666674100L;
	private int id; // ID
	@AutoGen(value = "ITSM_KONWLEDGE", requireType = GenerationType.REQUIRED_NEW)
	private String knowledgeCode; // 知识单号
	private String name; // 知识标题
	private String source;//知识来源
	private String typeId; //知识类别id
	private String typeName;
	private String currStatus; //知识状态
	
	private String eventWoCode; //关联事件单号
	private String problemWoCode;  //关联问题单号
	private String keywords;  //关键字
	private String troubleDescription;  //问题描述
	private String solutionDescription;  //解决方案描述
	private String auditUser; //知识审批人
	private String auditUserName;//审批人名
	private Date auditDate; //知识评审日期
	private String workflowId ; //工作流实例ID
	private String currHandlerUserId; //当前处理人Id
	private String currHandlerUserName; //当前处理人名字
	private String createuserName;  //创建人名字
	private Date createdate; //创建日期
	private int yxbz;
	
	@Override
	public String toString() {
		return "ItsmKnowledge [id=" + id + ", knowledgeCode=" + knowledgeCode
				+ ", name=" + name + ", source=" + source + ", typeId=" + typeId
				+ ", eventWoCode=" + eventWoCode + ", problemWoCode="
				+ problemWoCode + ", keywords=" + keywords
				+ ", troubleDescription=" + troubleDescription
				+ ", solutionDescription=" + solutionDescription
				+ ", auditUser=" + auditUser + ", auditDate=" + auditDate + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKnowledgeCode() {
		return knowledgeCode;
	}
	public void setKnowledgeCode(String knowledgeCode) {
		this.knowledgeCode = knowledgeCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrStatus() {
		return currStatus;
	}
	public void setCurrStatus(String currStatus) {
		this.currStatus = currStatus;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getEventWoCode() {
		return eventWoCode;
	}
	public void setEventWoCode(String eventWoCode) {
		this.eventWoCode = eventWoCode;
	}
	public String getProblemWoCode() {
		return problemWoCode;
	}
	public void setProblemWoCode(String problemWoCode) {
		this.problemWoCode = problemWoCode;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getTroubleDescription() {
		return troubleDescription;
	}
	public void setTroubleDescription(String troubleDescription) {
		this.troubleDescription = troubleDescription;
	}
	public String getSolutionDescription() {
		return solutionDescription;
	}
	public void setSolutionDescription(String solutionDescription) {
		this.solutionDescription = solutionDescription;
	}
	public String getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	public String getAuditUserName() {
		return auditUserName;
	}
	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getCurrHandlerUserId() {
		return currHandlerUserId;
	}
	public void setCurrHandlerUserId(String currHandlerUserId) {
		this.currHandlerUserId = currHandlerUserId;
	}
	public String getCurrHandlerUserName() {
		return currHandlerUserName;
	}
	public void setCurrHandlerUserName(String currHandlerUserName) {
		this.currHandlerUserName = currHandlerUserName;
	}
        public String getCreateuserName() {
            return createuserName;
        }
        public void setCreateuserName(String createuserName) {
            this.createuserName = createuserName;
        }
        public int getYxbz() {
		return yxbz;
	}
	public void setYxbz(int yxbz) {
		this.yxbz = yxbz;
	}
    /**
     * @return the createdate
     */
    public Date getCreatedate() {
        return createdate;
    }
    /**
     * @param createdate the createdate to set
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

	


}
