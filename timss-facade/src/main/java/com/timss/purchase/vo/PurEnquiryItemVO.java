package com.timss.purchase.vo;

import java.math.BigDecimal;

/**
 * @description: 询价单vo类
 * @company: gdyd
 * @className: PurEnquiryItemVO.java
 * @author: 890166
 * @createDate: 2014-6-13
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurEnquiryItemVO {
    private String itemid;

    private String itemname;

    private String orderunitname;

    private String classname;

    private BigDecimal itemnum;

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
     * @return the itemnum
     */
    public BigDecimal getItemnum() {
        return itemnum;
    }

    /**
     * @param itemnum the itemnum to set
     */
    public void setItemnum(BigDecimal itemnum) {
        this.itemnum = itemnum;
    }
}
