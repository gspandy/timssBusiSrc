package com.timss.purchase.vo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurVendorItemVO.java
 * @author: 890166
 * @createDate: 2014-6-17
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurVendorItemVO {
    private String itemid;
    private String itemname;
    private String orderunitname;
    private String classname;
    private String cusmodel;

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
     * @return the orderunitname
     */
    public String getOrderunitname() {
        return orderunitname;
    }

    /**
     * @param orderunitname the orderunitname to set
     */
    public void setOrderunitname(String orderunitname) {
        this.orderunitname = orderunitname;
    }

    /**
     * @return the classname
     */
    public String getClassname() {
        return classname;
    }

    /**
     * @param classname the classname to set
     */
    public void setClassname(String classname) {
        this.classname = classname;
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

}
