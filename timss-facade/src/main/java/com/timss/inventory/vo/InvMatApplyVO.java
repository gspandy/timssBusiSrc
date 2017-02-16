package com.timss.inventory.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyVO.java
 * @author: 890166
 * @createDate: 2014-7-27
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatApplyVO {

    private String imaid; // 领料ID
    private String sheetno; // 领料申请单号
    private String sheetname; // 领料单名称
    private String status; // 领料状态
    private String applyType; // 领料类型
    private String createuser; // 创建人
    private String modifyuser; // 修改人
    private String siteId; // 站点ID
    private String instanceid; // 流程实例ID
    private String dept;
    private String taskid; // 任务id

    private BigDecimal price;

    private Date createdate; // 创建时间
    private Date modifydate; // 修改时间

    private String remark; // 备注

    private String spmaterial;// 是否特殊物资

    private BigDecimal applyBudget; // 部门领料申请总成本
    private BigDecimal remainBudget; // 年度预算剩余金额
    private String isOver; // 是否超标
    private String workOrderNo;// 关联工单

    private String applyUse;// 领料用途
    
    private String itemCode; //物资编号 用于搜索框搜索
    private String  itemName;//物资名称 用于搜索框搜索
    private String relatePurApplyIdsList;//相关的采购申请id;
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
     * @return the workOrderNo
     */
    public String getWorkOrderNo() {
        return workOrderNo;
    }

    /**
     * @param workOrderNo the workOrderNo to set
     */
    public void setWorkOrderNo(String workOrderNo) {
        this.workOrderNo = workOrderNo;
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

    /**
     * @return the dept
     */
    public String getDept() {
        return dept;
    }

    /**
     * @param dept the dept to set
     */
    public void setDept(String dept) {
        this.dept = dept;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

    public String getRelatePurApplyIdsList() {
        return relatePurApplyIdsList;
    }
    
    public void setRelatePurApplyIdsList(String relatePurApplyIdsList) {
        this.relatePurApplyIdsList = relatePurApplyIdsList;
    }
}
