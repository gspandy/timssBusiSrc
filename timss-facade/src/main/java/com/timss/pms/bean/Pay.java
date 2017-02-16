package com.timss.pms.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

public class Pay extends ItcMvcBean {

    // Fields

    private Integer id;
    private Integer contractId;
    private Integer payplanId;
    private Integer planId;
    private Integer projectId;
    private String ptype;
    private String fcondition;
    private Double bepay;
    private Double zbj;
    private Double othercost;
    private Double actualpay;
    private String precent;
    private String explain;
    private String eattachment;
    private String createUser;
    private Date createTime;
    private String status;
    private String inputDept;
    private String dutyDept;
    private Date payDate;
    private String siteid;
    private String deptid;
    private String type;
    private String updateUser;
    private Date updateTime;
    private Boolean delFlag;
    private String attach;

    private String payway;

    private String sendedtoerp; // 是否已经发送erp凭证数据
    // Constructors
    private String undoFlowId;
    private String undoFlowCode;
    private String undoRemark;
    private String undoStatus;
    private String payCondition;
    //SJW流水号
    private String paySpNo;
    /** default constructor */
    public Pay() {
    }

    /** minimal constructor */
    public Pay(Integer id) {
        this.id = id;

    }

    /** full constructor */
    public Pay(Integer id, Integer planId, Integer projectId, String ptype, String fcondition, Double bepay,
            Double zbj, Double othercost, Double actualpay, String precent, String explain, String eattachment,
            String createUser, Date createTime, String status, String inputDept, String dutyDept, Date payDate,
            String siteid, String deptid, String type, String updateUser, Date updateTime, Boolean delFlag,
            String attach) {
        this.id = id;

        this.planId = planId;
        this.projectId = projectId;
        this.ptype = ptype;
        this.fcondition = fcondition;
        this.bepay = bepay;
        this.zbj = zbj;
        this.othercost = othercost;
        this.actualpay = actualpay;
        this.precent = precent;
        this.explain = explain;
        this.eattachment = eattachment;
        this.createUser = createUser;
        this.createTime = createTime;
        this.status = status;
        this.inputDept = inputDept;
        this.dutyDept = dutyDept;
        this.payDate = payDate;
        this.siteid = siteid;
        this.deptid = deptid;
        this.type = type;
        this.updateUser = updateUser;
        this.updateTime = updateTime;
        this.delFlag = delFlag;
        this.attach = attach;
    }

    // Property accessors

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlanId() {
        return this.planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getPtype() {
        return this.ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getFcondition() {
        return this.fcondition;
    }

    public void setFcondition(String fcondition) {
        this.fcondition = fcondition;
    }

    public Double getBepay() {
        return this.bepay;
    }

    public void setBepay(Double bepay) {
        this.bepay = bepay;
    }

    public Double getZbj() {
        return this.zbj;
    }

    public void setZbj(Double zbj) {
        this.zbj = zbj;
    }

    public Double getOthercost() {
        return this.othercost;
    }

    public void setOthercost(Double othercost) {
        this.othercost = othercost;
    }

    public Double getActualpay() {
        return this.actualpay;
    }

    public void setActualpay(Double actualpay) {
        this.actualpay = actualpay;
    }

    public String getPrecent() {
        return this.precent;
    }

    public void setPrecent(String precent) {
        this.precent = precent;
    }

    public String getExplain() {
        return this.explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getEattachment() {
        return this.eattachment;
    }

    public void setEattachment(String eattachment) {
        this.eattachment = eattachment;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInputDept() {
        return this.inputDept;
    }

    public void setInputDept(String inputDept) {
        this.inputDept = inputDept;
    }

    public String getDutyDept() {
        return this.dutyDept;
    }

    public void setDutyDept(String dutyDept) {
        this.dutyDept = dutyDept;
    }

    public Date getPayDate() {
        return this.payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getSiteid() {
        return this.siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getDeptid() {
        return this.deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public String getAttach() {
        return this.attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getPayplanId() {
        return payplanId;
    }

    public void setPayplanId(Integer payplanId) {
        this.payplanId = payplanId;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getSendedtoerp() {
        return sendedtoerp;
    }

    public void setSendedtoerp(String sendedtoerp) {
        this.sendedtoerp = sendedtoerp;
    }

    public String getUndoFlowId() {
        return undoFlowId;
    }

    public void setUndoFlowId(String undoFlowId) {
        this.undoFlowId = undoFlowId;
    }

    public String getUndoFlowCode() {
        return undoFlowCode;
    }

    public void setUndoFlowCode(String undoFlowCode) {
        this.undoFlowCode = undoFlowCode;
    }

    public String getUndoRemark() {
        return undoRemark;
    }

    public void setUndoRemark(String undoRemark) {
        this.undoRemark = undoRemark;
    }

    public String getUndoStatus() {
        return undoStatus;
    }

    public void setUndoStatus(String undoStatus) {
        this.undoStatus = undoStatus;
    }

    public String getPayCondition() {
        return payCondition;
    }

    public void setPayCondition(String payCondition) {
        this.payCondition = payCondition;
    }

    public String getPaySpNo() {
        return paySpNo;
    }

    public void setPaySpNo(String paySpNo) {
        this.paySpNo = paySpNo;
    }
    
}