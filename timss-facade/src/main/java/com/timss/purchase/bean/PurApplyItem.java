package com.timss.purchase.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyItem.java
 * @author: 890166
 * @createDate: 2014-6-23
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurApplyItem extends InvItemBaseField{

    private static final long serialVersionUID = 3048279395892507663L;
    private String sheetId;
    private String status;
    private String commitcommecnetwk;

    private BigDecimal averprice = new BigDecimal( "0" );

    private Date statusdate = new Date();

    private double repliednum;
    private double itemnum;
    private String remark;
    private String nullifyStatus;
    /**
     * @return the repliednum
     */
    public double getRepliednum() {
        return repliednum;
    }

    /**
     * @param repliednum the repliednum to set
     */
    public void setRepliednum(double repliednum) {
        this.repliednum = repliednum;
    }

    /**
     * @return the itemnum
     */
    public double getItemnum() {
        return itemnum;
    }

    /**
     * @param itemnum the itemnum to set
     */
    public void setItemnum(double itemnum) {
        this.itemnum = itemnum;
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
     * @return the sheetId
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * @param sheetId the sheetId to set
     */
    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    /**
     * @return the averprice
     */
    public BigDecimal getAverprice() {
        return averprice;
    }

    /**
     * @param averprice the averprice to set
     */
    public void setAverprice(BigDecimal averprice) {
        this.averprice = averprice;
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
     * @return the statusdate
     */
    public Date getStatusdate() {
        return statusdate;
    }

    /**
     * @param statusdate the statusdate to set
     */
    public void setStatusdate(Date statusdate) {
        this.statusdate = statusdate;
    }

    /**
     * @return the commitcommecnetwk
     */
    public String getCommitcommecnetwk() {
        return commitcommecnetwk;
    }

    /**
     * @param commitcommecnetwk the commitcommecnetwk to set
     */
    public void setCommitcommecnetwk(String commitcommecnetwk) {
        this.commitcommecnetwk = commitcommecnetwk;
    }

    public String getNullifyStatus() {
        return nullifyStatus;
    }

    public void setNullifyStatus(String nullifyStatus) {
        this.nullifyStatus = nullifyStatus;
    }
}
