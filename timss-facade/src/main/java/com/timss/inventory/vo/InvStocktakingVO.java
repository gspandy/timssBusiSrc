package com.timss.inventory.vo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvStocktakingVO.java
 * @author: 890166
 * @createDate: 2014-9-30
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvStocktakingVO {

    private String istid; // 盘点申请ID
    private String sheetno; // 申请单号
    private String sheetname; // 申请名称
    private String status; // 申请状态
    private String warehouseid; // 仓库ID
    private String siteId; // 站点ID site_id
    private String instanceid; // 流程实例ID
    private String createuser; // 创建人
    private String modifyuser; // 修改人
    private String createdate; // 创建时间
    private String modifydate; // 修改时间

    private String warehousename;
    private String createusername;

    private String checkuser; // 验收人
    private String checkusername; // 验收人
    private String remark; // 备注
    private String enStatus;

    /**
     * @return the enStatus
     */
    public String getEnStatus() {
        return enStatus;
    }

    /**
     * @param enStatus the enStatus to set
     */
    public void setEnStatus(String enStatus) {
        this.enStatus = enStatus;
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
     * @return the warehousename
     */
    public String getWarehousename() {
        return warehousename;
    }

    /**
     * @param warehousename the warehousename to set
     */
    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }

    /**
     * @return the createusername
     */
    public String getCreateusername() {
        return createusername;
    }

    /**
     * @param createusername the createusername to set
     */
    public void setCreateusername(String createusername) {
        this.createusername = createusername;
    }

}
