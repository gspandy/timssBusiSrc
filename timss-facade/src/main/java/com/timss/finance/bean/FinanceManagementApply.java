package com.timss.finance.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * FinManagementApply entity. @author MyEclipse Persistence Tools
 */

public class FinanceManagementApply extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -5787616386272710016L;
    // Fields
    @AutoGen(value = "FIN_MA_SEQ", requireType = GenerationType.REQUIRED_NEW)
    private String id;// 主键
    private String deptid;
    private String siteid;
    private String createuser;
    private Date createdate;
    private String modifyuser;
    private Date modifydate;
    private String name;// 申请名称
    private String applyType; // 申请类型（出差申请/管理费用申请）
    private String type;  //类型（例如出差申请中表示商务出差、一般出差、市内出差中的一种）
    private String subject;// 科目
    private Double budget;// 申请预算
    private String description;// 备注
    private String delFlag;
    private String status;// 申请单状态，审批中，审批通过等
    private String applyUser;// 申请人
    private String proInstId;// 流程id
    private String applyUsername;// 申请人名称
    private String attach;// 附件id列表
    private String flowStatus;// 流程状态，每个流程节点都是一个状态
    private String flowStatusName; // 流程状态名
    private String deptname;// 部门

    private String currHandUser;
    private String currHandUserName;
    private String needZJL;// 需要总经理审批条件
    private String needHQ;// 需要董事长审批
    private String participantIds; //本公司参与人（例如出差申请,业务招待费的经办人）
    private String participantName;  //本公司参与人名
    private String participateDeptName; //参与部门
    private String outComName;  //外部单位
    private int outPersonNum;  //外部单位参与人数
    private int ourPersonNum;   //本公司参与接待人数
    private Date strDate;  //开始时间
    private Date endDate;  //结束时间
    private Date occurDate; //发生日期（例如：接待日期）
    private String occurAddress; ////发生地点（例如：出差地点）
    private String isTravel;//实际是否出差
    private String vehicle;//交通工具
    
    
    public String getVehicle() {
		return vehicle;
	}
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	public String getIsTravel() {
		return isTravel;
	}
	public void setIsTravel(String isTravel) {
		this.isTravel = isTravel;
	}
	public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getOccurAddress() {
        return occurAddress;
    }
    public void setOccurAddress(String occurAddress) {
        this.occurAddress = occurAddress;
    }
    public Date getOccurDate() {
        return occurDate;
    }
    public void setOccurDate(Date occurDate) {
        this.occurDate = occurDate;
    }
    public int getOurPersonNum() {
        return ourPersonNum;
    }
    public void setOurPersonNum(int ourPersonNum) {
        this.ourPersonNum = ourPersonNum;
    }
    public Date getStrDate() {
        return strDate;
    }
    public void setStrDate(Date strDate) {
        this.strDate = strDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDeptid() {
        return deptid;
    }
    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }
    public String getSiteid() {
        return siteid;
    }
    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }
    public String getCreateuser() {
        return createuser;
    }
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }
    public Date getCreatedate() {
        return createdate;
    }
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
    public String getModifyuser() {
        return modifyuser;
    }
    public void setModifyuser(String modifyuser) {
        this.modifyuser = modifyuser;
    }
    public Date getModifydate() {
        return modifydate;
    }
    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getApplyType() {
        return applyType;
    }
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public Double getBudget() {
        return budget;
    }
    public void setBudget(Double budget) {
        this.budget = budget;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDelFlag() {
        return delFlag;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getApplyUser() {
        return applyUser;
    }
    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }
    public String getProInstId() {
        return proInstId;
    }
    public void setProInstId(String proInstId) {
        this.proInstId = proInstId;
    }
    public String getApplyUsername() {
        return applyUsername;
    }
    public void setApplyUsername(String applyUsername) {
        this.applyUsername = applyUsername;
    }
    public String getAttach() {
        return attach;
    }
    public void setAttach(String attach) {
        this.attach = attach;
    }
    public String getFlowStatus() {
        return flowStatus;
    }
    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }
    public String getFlowStatusName() {
        return flowStatusName;
    }
    public void setFlowStatusName(String flowStatusName) {
        this.flowStatusName = flowStatusName;
    }
    public String getDeptname() {
        return deptname;
    }
    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }
    public String getCurrHandUser() {
        return currHandUser;
    }
    public void setCurrHandUser(String currHandUser) {
        this.currHandUser = currHandUser;
    }
    public String getCurrHandUserName() {
        return currHandUserName;
    }
    public void setCurrHandUserName(String currHandUserName) {
        this.currHandUserName = currHandUserName;
    }
    public String getNeedZJL() {
        return needZJL;
    }
    public void setNeedZJL(String needZJL) {
        this.needZJL = needZJL;
    }
    public String getNeedHQ() {
        return needHQ;
    }
    public void setNeedHQ(String needHQ) {
        this.needHQ = needHQ;
    }
    public String getParticipantIds() {
        return participantIds;
    }
    public void setParticipantIds(String participantIds) {
        this.participantIds = participantIds;
    }
    public String getParticipantName() {
        return participantName;
    }
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }
    public String getParticipateDeptName() {
        return participateDeptName;
    }
    public void setParticipateDeptName(String participateDeptName) {
        this.participateDeptName = participateDeptName;
    }
    public String getOutComName() {
        return outComName;
    }
    public void setOutComName(String outComName) {
        this.outComName = outComName;
    }
    public int getOutPersonNum() {
        return outPersonNum;
    }
    public void setOutPersonNum(int outPersonNum) {
        this.outPersonNum = outPersonNum;
    }
    @Override
    public String toString() {
        return "FinanceManagementApply [id=" + id + ", deptid=" + deptid + ", siteid=" + siteid + ", createuser="
                + createuser + ", createdate=" + createdate + ", modifyuser=" + modifyuser + ", modifydate="
                + modifydate + ", name=" + name + ", applyType=" + applyType + ", subject=" + subject + ", budget="
                + budget + ", description=" + description + ", delFlag=" + delFlag + ", status=" + status
                + ", applyUser=" + applyUser + ", proInstId=" + proInstId + ", applyUsername=" + applyUsername
                + ", attach=" + attach + ", flowStatus=" + flowStatus + ", flowStatusName=" + flowStatusName
                + ", deptname=" + deptname + ", currHandUser=" + currHandUser + ", currHandUserName="
                + currHandUserName + ", needZJL=" + needZJL + ", needHQ=" + needHQ + ", participantIds="
                + participantIds + ", participantName=" + participantName + ", participateDeptName="
                + participateDeptName + ", outComName=" + outComName + ", outPersonNum=" + outPersonNum + "]";
    }
   

}