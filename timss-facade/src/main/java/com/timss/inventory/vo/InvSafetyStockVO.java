package com.timss.inventory.vo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvSafetyStock.java
 * @author: 890166
 * @createDate: 2015-2-16
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvSafetyStockVO {

    private String itemname; // 物资名称
    private String cusmodel; // 物资参数
    private String stockqty; // 已入库数量
    private String differ;// 差异数据
    private String unit;// 单位
    private String lowinv; // 安全库存量

    /**
     * @return the lowinv
     */
    public String getLowinv() {
        return lowinv;
    }

    /**
     * @param lowinv the lowinv to set
     */
    public void setLowinv(String lowinv) {
        this.lowinv = lowinv;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
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
    public String getStockqty() {
        return stockqty;
    }

    /**
     * @param stockqty the stockqty to set
     */
    public void setStockqty(String stockqty) {
        this.stockqty = stockqty;
    }

    /**
     * @return the differ
     */
    public String getDiffer() {
        return differ;
    }

    /**
     * @param differ the differ to set
     */
    public void setDiffer(String differ) {
        this.differ = differ;
    }

}
