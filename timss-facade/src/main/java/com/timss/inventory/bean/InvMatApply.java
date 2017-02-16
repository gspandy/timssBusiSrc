package com.timss.inventory.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 物资领料流程信息主表
 * @description: 物资领料流程信息主表
 * @company: gdyd
 * @className: InvMatApply.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
@SuppressWarnings("serial")
public class InvMatApply extends ItcMvcBean {

    @AutoGen(value = "INV_MATAPPLY_ID", requireType = GenerationType.REQUIRED_NULL)
    private String imaid; // 领料ID

    @AutoGen(value = "INV_MATAPPLY_SHEETNO", requireType = GenerationType.REQUIRED_NULL)
    private String sheetno; // 领料申请单号
    private String sheetname; // 领料单名称
    private String status; // 领料状态
    private String applyType; // 领料类型
    private String createuser; // 创建人
    private String modifyuser; // 修改人
    private String siteId; // 站点ID
    private String instanceid; // 流程实例ID
    private String deptid; // 申请部门id
    private String remark; // 备注
    private String taskid; // 任务id

    private Date createdate; // 创建时间
    private Date modifydate; // 修改时间

    private String spmaterial;// 是否特殊物资

    private BigDecimal applyBudget; // 部门领料申请总成本
    private BigDecimal remainBudget; // 年度预算剩余金额
    private String isOver; // 是否超标
    private String applyUse;// 领料用途

    /**
     * @return the applyUse
     */
    public String getApplyUse() {
        return applyUse;
    }

    /**
     * @param applyUse the applyUse to set
     */
    public void setApplyUse(String applyUse) {
        this.applyUse = applyUse;
    }

    /**
     * @return the applyBudget
     */
    public BigDecimal getApplyBudget() {
        return applyBudget;
    }

    /**
     * @param applyBudget the applyBudget to set
     */
    public void setApplyBudget(BigDecimal applyBudget) {
        this.applyBudget = applyBudget;
    }

    /**
     * @return the remainBudget
     */
    public BigDecimal getRemainBudget() {
        return remainBudget;
    }

    /**
     * @param remainBudget the remainBudget to set
     */
    public void setRemainBudget(BigDecimal remainBudget) {
        this.remainBudget = remainBudget;
    }

    /**
     * @return the isOver
     */
    public String getIsOver() {
        return isOver;
    }

    /**
     * @param isOver the isOver to set
     */
    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

    /**
     * @return the spmaterial
     */
    public String getSpmaterial() {
        return spmaterial;
    }

    /**
     * @param spmaterial the spmaterial to set
     */
    public void setSpmaterial(String spmaterial) {
        this.spmaterial = spmaterial;
    }

    /**
     * @return the taskid
     */
    public String getTaskid() {
        return taskid;
    }

    /**
     * @param taskid the taskid to set
     */
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the deptid
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * @param deptid the deptid to set
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    /**
     * @return the sheetname
     */
    public String getSheetname() {
        return sheetname;
    }

    /**
     * @param sheetname the sheetname to set
     */
    public void setSheetname(String sheetname) {
        this.sheetname = sheetname;
    }

    /**
     * @return the applyType
     */
    public String getApplyType() {
        return applyType;
    }

    /**
     * @param applyType the applyType to set
     */
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    /**
     * @return the siteId
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the imaid
     */
    public String getImaid() {
        return imaid;
    }

    /**
     * @param imaid the imaid to set
     */
    public void setImaid(String imaid) {
        this.imaid = imaid;
    }

    /**
     * @return the sheetno
     */
    public String getSheetno() {
        return sheetno;
    }

    /**
     * @param sheetno the sheetno to set
     */
    public void setSheetno(String sheetno) {
        this.sheetno = sheetno;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the createuser
     */
    public String getCreateuser() {
        return createuser;
    }

    /**
     * @param createuser the createuser to set
     */
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
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

    /**
     * @return the modifyuser
     */
    public String getModifyuser() {
        return modifyuser;
    }

    /**
     * @param modifyuser the modifyuser to set
     */
    public void setModifyuser(String modifyuser) {
        this.modifyuser = modifyuser;
    }

    /**
     * @return the modifydate
     */
    public Date getModifydate() {
        return modifydate;
    }

    /**
     * @param modifydate the modifydate to set
     */
    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

    /**
     * @return the instanceid
     */
    public String getInstanceid() {
        return instanceid;
    }

    /**
     * @param instanceid the instanceid to set
     */
    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

}
