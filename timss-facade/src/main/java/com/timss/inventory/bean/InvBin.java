package com.timss.inventory.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 货柜表
 * @description: 货柜表
 * @company: gdyd
 * @className: InvBin.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890147
 * @version: 1.0
 */
public class InvBin extends ItcMvcBean {

    private static final long serialVersionUID = -1936355664119916596L;

    @AutoGen(value = "INV_BIN_ID", requireType = GenerationType.REQUIRED_NULL)
    private String binid; // 货柜ID
    private String binname; // 货柜名
    private String warehouseid; // 仓库ID
    private String descriptions; // 描述
    private String active; // 有效标志，ACTIVE有效NO无效
    private String siteId; // 站点ID
    private String warehousename;// 仓库名称

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }

    /**
     * @return the binid
     */
    public String getBinid() {
        return binid;
    }

    /**
     * @param binid the binid to set
     */
    public void setBinid(String binid) {
        this.binid = binid;
    }

    /**
     * @return the binname
     */
    public String getBinname() {
        return binname;
    }

    /**
     * @param binname the binname to set
     */
    public void setBinname(String binname) {
        this.binname = binname;
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

}
