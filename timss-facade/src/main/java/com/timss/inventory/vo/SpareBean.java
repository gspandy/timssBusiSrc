package com.timss.inventory.vo;

import java.math.BigDecimal;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: ApareBean.java
 * @author: 890166
 * @createDate: 2014-7-23
 * @updateUser: 890166
 * @version: 1.0
 */
public class SpareBean {

    private String itemid;
    private String itemcode; // 系统编号
    private String itemname; // 备件名称
    private String cusmodel; // 型号
    private String warehouse; // 仓库
    private String sparecode; // 备件编号
    private String manufacturer; // 生产厂家
    private String assetId; // 资产id

    private BigDecimal stockqty; // 库存数量

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
     * @return the itemname
     */
    public String getItemname() {
        return itemname;
    }

    /**
     * @param itemname the itemname to set
     */
    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    /**
     * @return the warehouse
     */
    public String getWarehouse() {
        return warehouse;
    }

    /**
     * @param warehouse the warehouse to set
     */
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * @return the assetId
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * @param assetId the assetId to set
     */
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    /**
     * @return the itemcode
     */
    public String getItemcode() {
        return itemcode;
    }

    /**
     * @param itemcode the itemcode to set
     */
    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
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
     * @return the stockqty
     */
    public BigDecimal getStockqty() {
        return stockqty;
    }

    /**
     * @param stockqty the stockqty to set
     */
    public void setStockqty(BigDecimal stockqty) {
        this.stockqty = stockqty;
    }

    /**
     * @return the sparecode
     */
    public String getSparecode() {
        return sparecode;
    }

    /**
     * @param sparecode the sparecode to set
     */
    public void setSparecode(String sparecode) {
        this.sparecode = sparecode;
    }

    /**
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @param manufacturer the manufacturer to set
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

}
