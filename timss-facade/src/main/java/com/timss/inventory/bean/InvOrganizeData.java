package com.timss.inventory.bean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvOrganizeData.java
 * @author: 890166
 * @createDate: 2015-5-28
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvOrganizeData {

    private String warehouseName; // 仓库名称
    private String containerName; // 货柜名称
    private String categroyO; // 物资分类（一级）
    private String categroyS; // 物资分类（二级）
    private String itemName; // 物资名称
    private String cusmodel; // 型号规格
    private String qty; // 数量
    private String unitName; // 单位
    private String price; // 价格（元）
    private String status; // 状态（默认为0是正常状态，为1是出错状态）
    private String remark; // 出错备注
    private String siteId; // 站点id
    private String createUser; // 创建人

    /**
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
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
     * @return the containerName
     */
    public String getContainerName() {
        return containerName;
    }

    /**
     * @param containerName the containerName to set
     */
    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    /**
     * @return the categroyO
     */
    public String getCategroyO() {
        return categroyO;
    }

    /**
     * @param categroyO the categroyO to set
     */
    public void setCategroyO(String categroyO) {
        this.categroyO = categroyO;
    }

    /**
     * @return the categroyS
     */
    public String getCategroyS() {
        return categroyS;
    }

    /**
     * @param categroyS the categroyS to set
     */
    public void setCategroyS(String categroyS) {
        this.categroyS = categroyS;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the cusmodel
     */
    public String getCusmodel() {
        return cusmodel;
    }

    /**
     * @param cusmodel the cusmodel to set
     */
    public void setCusmodel(String cusmodel) {
        this.cusmodel = cusmodel;
    }

    /**
     * @return the unitName
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * @param unitName the unitName to set
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
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
     * @return the createUser
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

}
