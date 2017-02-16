package com.timss.inventory.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 库存盘点流程信息主表
 * @description: 库存盘点流程信息主表
 * @company: gdyd
 * @className: InvStocktaking.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvStocktaking extends ItcMvcBean {

    private static final long serialVersionUID = 933330295985939009L;

    @AutoGen(value = "INV_STOCKTAKING_ID", requireType = GenerationType.REQUIRED_NULL)
    private String istid; // 盘点申请ID
    @AutoGen(value = "INV_STOCKTAKING_SN", requireType = GenerationType.REQUIRED_NULL)
    private String sheetno; // 申请单号
    private String sheetname; // 申请名称
    private String status; // 申请状态
    private String warehouseid; // 仓库ID
    private String siteId; // 站点ID site_id
    private String instanceid; // 流程实例ID
    private String createuser; // 创建人
    private String modifyuser; // 修改人
    private Date createdate; // 创建时间
    private Date modifydate; // 修改时间

    private String checkuser; // 验收人

    private String remark; // 备注

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
     * @return the istid
     */
    public String getIstid() {
        return istid;
    }

    /**
     * @param istid the istid to set
     */
    public void setIstid(String istid) {
        this.istid = istid;
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

}
