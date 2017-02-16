package com.timss.inventory.vo;

import java.math.BigDecimal;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranVO.java
 * @author: 890166
 * @createDate: 2014-7-23
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatTranVO {
    private String imtid; // 交易表ID
    private String sheetno; // 交易表编码
    private String tranType; // 交易类型 tran_type
    private String warehouseid; // 仓库ID
    private String operuser; // 操作人
    private String checkuser; // 验收人
    private String operusername; // 操作人
    private String checkusername; // 验收人
    private String createuser; // 创建人
    private String modifyuser; // 修改人
    private String remark; // 备注
    private String siteId;
    private String tranTypeName; // 交易类型名称
    private String warehouseName; // 仓库名称
    private String pruorderno; // 采购单号
    private String pruordername; // 采购单名称

    private String outsheetno; // 出库单号
    private String outsheetname; // 出库名称
    private String status; // 状态

    private BigDecimal totalPrice; // 总成本
    private BigDecimal lotno; // 批次

    private String createdate; // 创建时间
    private String modifydate; // 修改时间

    private String processinstid;// 待办（批次）id

    private String schfield;

    private String applyTypeName; // 采购申请类型

    /**
     * @return the applyTypeName
     */
    public String getApplyTypeName() {
        return applyTypeName;
    }

    /**
     * @param applyTypeName the applyTypeName to set
     */
    public void setApplyTypeName(String applyTypeName) {
        this.applyTypeName = applyTypeName;
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
     * @return the outsheetname
     */
    public String getOutsheetname() {
        return outsheetname;
    }

    /**
     * @param outsheetname the outsheetname to set
     */
    public void setOutsheetname(String outsheetname) {
        this.outsheetname = outsheetname;
    }

    /**
     * @return the outsheetno
     */
    public String getOutsheetno() {
        return outsheetno;
    }

    /**
     * @param outsheetno the outsheetno to set
     */
    public void setOutsheetno(String outsheetno) {
        this.outsheetno = outsheetno;
    }

    /**
     * @return the processinstid
     */
    public String getProcessinstid() {
        return processinstid;
    }

    /**
     * @param processinstid the processinstid to set
     */
    public void setProcessinstid(String processinstid) {
        this.processinstid = processinstid;
    }

    /**
     * @return the operusername
     */
    public String getOperusername() {
        return operusername;
    }

    /**
     * @param operusername the operusername to set
     */
    public void setOperusername(String operusername) {
        this.operusername = operusername;
    }

    /**
     * @return the checkusername
     */
    public String getCheckusername() {
        return checkusername;
    }

    /**
     * @param checkusername the checkusername to set
     */
    public void setCheckusername(String checkusername) {
        this.checkusername = checkusername;
    }

    /**
     * @return the pruorderno
     */
    public String getPruorderno() {
        return pruorderno;
    }

    /**
     * @param pruorderno the pruorderno to set
     */
    public void setPruorderno(String pruorderno) {
        this.pruorderno = pruorderno;
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
     * @return the createdate
     */
    public String getCreatedate() {
        return createdate;
    }

    /**
     * @param createdate the createdate to set
     */
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    /**
     * @return the modifydate
     */
    public String getModifydate() {
        return modifydate;
    }

    /**
     * @param modifydate the modifydate to set
     */
    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    /**
     * @return the imtid
     */
    public String getImtid() {
        return imtid;
    }

    /**
     * @param imtid the imtid to set
     */
    public void setImtid(String imtid) {
        this.imtid = imtid;
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
     * @return the tranType
     */
    public String getTranType() {
        return tranType;
    }

    /**
     * @param tranType the tranType to set
     */
    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    /**
     * @return the warehouseid
     */
    public String getWarehouseid() {
        return warehouseid;
    }

    /**
     * @param warehouseid the warehouseid to set
     */
    public void setWarehouseid(String warehouseid) {
        this.warehouseid = warehouseid;
    }

    /**
     * @return the operuser
     */
    public String getOperuser() {
        return operuser;
    }

    /**
     * @param operuser the operuser to set
     */
    public void setOperuser(String operuser) {
        this.operuser = operuser;
    }

    /**
     * @return the checkuser
     */
    public String getCheckuser() {
        return checkuser;
    }

    /**
     * @param checkuser the checkuser to set
     */
    public void setCheckuser(String checkuser) {
        this.checkuser = checkuser;
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
     * @return the lotno
     */
    public BigDecimal getLotno() {
        return lotno;
    }

    /**
     * @param lotno the lotno to set
     */
    public void setLotno(BigDecimal lotno) {
        this.lotno = lotno;
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
     * @return the tranTypeName
     */
    public String getTranTypeName() {
        return tranTypeName;
    }

    /**
     * @param tranTypeName the tranTypeName to set
     */
    public void setTranTypeName(String tranTypeName) {
        this.tranTypeName = tranTypeName;
    }

    /**
     * @return the warehouseName
     */
    public String getWarehouseName() {
        return warehouseName;
    }

    /**
     * @param warehouseName the warehouseName to set
     */
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    /**
     * @return the totalPrice
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

	/**
	 * @return the pruordername
	 */
	public String getPruordername() {
		return pruordername;
	}

	/**
	 * @param pruordername the pruordername to set
	 */
	public void setPruordername(String pruordername) {
		this.pruordername = pruordername;
	}

}
