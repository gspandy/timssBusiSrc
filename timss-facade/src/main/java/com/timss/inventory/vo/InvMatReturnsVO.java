package com.timss.inventory.vo;

import java.util.Date;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatReturnsVO.java
 * @author: 890166
 * @createDate: 2015-3-12
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatReturnsVO {
//{"imaid":"IMAI0736","sheetname":"yzh物资预留测试20150923004","sheetno":"SA20150923013","applyType":"office_supplies",
    //"remark":"yzh物资预留测试20150923004","refundReason":"退库原因001","createuser":"890166"}
    private String imrsid;
    private String imrsno;
    private String purchOrderNo;
    private String purchOrderName;
    private String purOrderType;
    private Date returnDate;
    private String operType;
    private String remark;
    private String createuser;
    private Date createdate;
    private String modifyuser;
    private Date modifydate;
    private String siteid;
    private String deptid;
    private String schfield;
    private String imtid; // 物资接收单ID 
    private String imaid; //物质申请单ID
    private String sheetno;  //物资申请单code或者物资接收单code
    private String returnReason;   // 退货原因
    private String refundReason;  //退库原因
    
    /**
     * @return the sheetno物资申请单code或者物资接收单code
     */
    public String getSheetno() {
        return sheetno;
    }

    /**
     * @param sheetno 物资申请单code或者物资接收单code
     */
    public void setSheetno(String sheetno) {
        this.sheetno = sheetno;
    }

    /**
     * @return the refundReason 退库原因
     */
    public String getRefundReason() {
        return refundReason;
    }

    /**
     * @param refundReason 退库原因
     */
    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    /**
     * @return the imaid 物资申请单ID
     */
    public String getImaid() {
        return imaid;
    }

    /**
     * @param imaid 物资申请单ID
     */
    public void setImaid(String imaid) {
        this.imaid = imaid;
    }

    /**
     * @return the purOrderType
     */
    public String getPurOrderType() {
        return purOrderType;
    }

    /**
     * @param purOrderType the purOrderType to set
     */
    public void setPurOrderType(String purOrderType) {
        this.purOrderType = purOrderType;
    }

    /**
     * @return the schfield
     */
    public String getSchfield() {
        return schfield;
    }

    /**
     * @param schfield the schfield to set
     */
    public void setSchfield(String schfield) {
        this.schfield = schfield;
    }

    /**
     * @return the purchOrderName
     */
    public String getPurchOrderName() {
        return purchOrderName;
    }

    /**
     * @param purchOrderName the purchOrderName to set
     */
    public void setPurchOrderName(String purchOrderName) {
        this.purchOrderName = purchOrderName;
    }

    /**
     * @return the imrsid
     */
    public String getImrsid() {
        return imrsid;
    }

    /**
     * @param imrsid the imrsid to set
     */
    public void setImrsid(String imrsid) {
        this.imrsid = imrsid;
    }

    /**
     * @return the imrsno
     */
    public String getImrsno() {
        return imrsno;
    }

    /**
     * @param imrsno the imrsno to set
     */
    public void setImrsno(String imrsno) {
        this.imrsno = imrsno;
    }

    /**
     * @return the purchOrderNo
     */
    public String getPurchOrderNo() {
        return purchOrderNo;
    }

    /**
     * @param purchOrderNo the purchOrderNo to set
     */
    public void setPurchOrderNo(String purchOrderNo) {
        this.purchOrderNo = purchOrderNo;
    }

    /**
     * @return the returnDate
     */
    public Date getReturnDate() {
        return returnDate;
    }

    /**
     * @param returnDate the returnDate to set
     */
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * @return the operType
     */
    public String getOperType() {
        return operType;
    }

    /**
     * @param operType the operType to set
     */
    public void setOperType(String operType) {
        this.operType = operType;
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
     * @return the siteId
     */
    public String getSiteid() {
        return siteid;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteid(String siteid) {
        this.siteid = siteid;
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
     * @return the returnReason
     */
    public String getReturnReason() {
        return returnReason;
    }

    /**
     * @param returnReason the returnReason to set
     */
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    /**
     * @return the imtId
     */
    public String getImtid() {
        return imtid;
    }

    /**
     * @param imtId the imtId to set
     */
    public void setImtid(String imtid) {
        this.imtid = imtid;
    }

}
