package com.timss.inventory.vo;

import com.timss.inventory.bean.InvCategory;

/**
 * @title: 物资分类对外接口传入参数类
 * @description: 物资分类对外接口传入参数类
 * @company: gdyd
 * @className: InvCategoryParam.java
 * @author: yuanzh
 * @createDate: 2016-1-8
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class InvCategoryParam {

    private String warehouseId; // 仓库id
    private String siteId; // 站点id
    private String level; // 物资分类层级
    private InvCategory invCategory;// 物资分类实体

    /**
     * @return the warehouseId
     */
    public String getWarehouseId() {
        return warehouseId;
    }

    /**
     * @param warehouseId the warehouseId to set
     */
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
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
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * @return the invCategory
     */
    public InvCategory getInvCategory() {
        return invCategory;
    }

    /**
     * @param invCategory the invCategory to set
     */
    public void setInvCategory(InvCategory invCategory) {
        this.invCategory = invCategory;
    }

}
