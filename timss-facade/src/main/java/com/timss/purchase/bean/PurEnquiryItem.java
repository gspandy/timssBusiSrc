package com.timss.purchase.bean;

import java.math.BigDecimal;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurEnquiryItem.java
 * @author: 890166
 * @createDate: 2014-6-14
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurEnquiryItem {

    private String enquiryid;
    private String itemid;
    private BigDecimal itemnumber = new BigDecimal( "0" );

    /**
     * @return the enquiryid
     */
    public String getEnquiryid() {
        return enquiryid;
    }

    /**
     * @param enquiryid the enquiryid to set
     */
    public void setEnquiryid(String enquiryid) {
        this.enquiryid = enquiryid == null ? "" : enquiryid.trim();
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
        this.itemid = itemid == null ? "" : itemid.trim();
    }

    /**
     * @return the itemnumber
     */
    public BigDecimal getItemnumber() {
        return itemnumber;
    }

    /**
     * @param itemnumber the itemnumber to set
     */
    public void setItemnumber(BigDecimal itemnumber) {
        this.itemnumber = itemnumber;
    }

}
