package com.timss.inventory.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 设备与物资映射关系表
 * @description: 设备与物资映射关系表
 * @company: gdyd
 * @className: InvEquipItemMapping.java
 * @author: 890166
 * @createDate: 2014-7-17
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvEquipItemMapping extends ItcMvcBean {

    private static final long serialVersionUID = -3376265571056912937L;

    @AutoGen(value = "INV_EIMAPPING_ID", requireType = GenerationType.REQUIRED_NULL)
    private String ieimid; // 流水号ID

    private String itemid; // 物资ID
    private String equipid; // 设备ID
    private String siteId; // 站点ID site_id
    private String equipname; // 设备名称（不用再查设备表）

    /**
     * @return the ieimid
     */
    public String getIeimid() {
        return ieimid;
    }

    /**
     * @param ieimid the ieimid to set
     */
    public void setIeimid(String ieimid) {
        this.ieimid = ieimid;
    }

    /**
     * @return the itemid
     */
    public String getItemid() {
        return itemid;
    }

    /**
     * @param itemid the itemid to set
     */
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    /**
     * @return the equipid
     */
    public String getEquipid() {
        return equipid;
    }

    /**
     * @param equipid the equipid to set
     */
    public void setEquipid(String equipid) {
        this.equipid = equipid;
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
     * @return the equipname
     */
    public String getEquipname() {
        return equipname;
    }

    /**
     * @param equipname the equipname to set
     */
    public void setEquipname(String equipname) {
        this.equipname = equipname;
    }

}
