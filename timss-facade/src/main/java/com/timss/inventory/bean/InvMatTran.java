package com.timss.inventory.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 库存交易表
 * @description: 库存交易表
 * @company: gdyd
 * @className: InvMatTran.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatTran extends ItcMvcBean {

    private static final long serialVersionUID = 7568936191935767901L;

    @AutoGen(value = "INV_MATTRAN_ID", requireType = GenerationType.REQUIRED_NULL)
    private String imtid; // 交易表ID

    @AutoGen(value = "INV_MATTRAN_SHEETNO", requireType = GenerationType.REQUIRED_NULL)
    private String sheetno; // 交易表编码
    private String tranType; // 交易类型 tran_type
    private String warehouseid; // 仓库ID
    private String operuser; // 操作人
    private String checkuser; // 验收人
    private String createuser; // 创建人
    private String modifyuser; // 修改人
    private String remark; // 备注
    private String siteId; // 站点ID site_id

    private Date createdate; // 创建时间
    private Date modifydate; // 修改时间

    private BigDecimal lotno; // 批次

    private String processinstid;// 待办（批次）id

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
}
