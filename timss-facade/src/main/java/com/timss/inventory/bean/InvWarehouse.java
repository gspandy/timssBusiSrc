package com.timss.inventory.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 仓库表
 * @description: 仓库表
 * @company: gdyd
 * @className: InvWarehouse.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvWarehouse extends ItcMvcBean {

    private static final long serialVersionUID = -6269671933887932349L;

    @AutoGen(value = "INV_WAREHOUSE_ID", requireType = GenerationType.REQUIRED_NULL)
    private String warehouseid; // 仓库ID
    private String warehousename; // 仓库名称
    private String descriptions; // 描述
    private String active; // 是否启用
    private String siteId; // 站点ID
    private String createuser; // 创建人
    private String modifyuser; // 修改人
    private String warehousecode;// 仓库

    private Date createdate; // 创建时间
    private Date modifydate; // 修改时间

    public String getWarehousecode() {
        return warehousecode;
    }

    public void setWarehousecode(String warehousecode) {
        this.warehousecode = warehousecode;
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
     * @return the descriptions
     */
    public String getDescriptions() {
        return descriptions;
    }

    /**
     * @param descriptions the descriptions to set
     */
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
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
