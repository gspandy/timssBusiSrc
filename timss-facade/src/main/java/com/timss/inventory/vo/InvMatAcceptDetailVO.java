package com.timss.inventory.vo;

import com.timss.inventory.bean.InvMatAcceptDetail;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatAcceptDetailVO.java
 * @author: user
 * @createDate: 2015-10-30
 * @updateUser: user
 * @version: 1.0
 */
@SuppressWarnings ( "serial" )
public class InvMatAcceptDetailVO extends InvMatAcceptDetail {

    private String itemname;
    private String itemcode;
    private String cusmodel;
    private String unit;
    private String unitid;
    private String binid;

    private String applysheetno;

    private String receivenum;
    private String warehouse;

    /**
     * @return the warehouse
     */
    public String getWarehouse () {
	return warehouse;
    }

    /**
     * @param warehouse the warehouse to set
     */
    public void setWarehouse ( String warehouse ) {
	this.warehouse = warehouse;
    }

    /**
     * @return the unitid
     */
    public String getUnitid () {
	return unitid;
    }

    /**
     * @param unitid the unitid to set
     */
    public void setUnitid ( String unitid ) {
	this.unitid = unitid;
    }

    /**
     * @return the binid
     */
    public String getBinid () {
	return binid;
    }

    /**
     * @param binid the binid to set
     */
    public void setBinid ( String binid ) {
	this.binid = binid;
    }

    /**
     * @return the itemname
     */
    public String getItemname () {
	return itemname;
    }

    /**
     * @param itemname the itemname to set
     */
    public void setItemname ( String itemname ) {
	this.itemname = itemname;
    }

    /**
     * @return the itemcode
     */
    public String getItemcode () {
	return itemcode;
    }

    /**
     * @param itemcode the itemcode to set
     */
    public void setItemcode ( String itemcode ) {
	this.itemcode = itemcode;
    }

    /**
     * @return the cusmodel
     */
    public String getCusmodel () {
	return cusmodel;
    }

    /**
     * @param cusmodel the cusmodel to set
     */
    public void setCusmodel ( String cusmodel ) {
	this.cusmodel = cusmodel;
    }

    /**
     * @return the unit
     */
    public String getUnit () {
	return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit ( String unit ) {
	this.unit = unit;
    }

    public String getApplysheetno () {
	return applysheetno;
    }

    public void setApplysheetno ( String applysheetno ) {
	this.applysheetno = applysheetno;
    }

    public String getReceivenum () {
	return receivenum;
    }

    public void setReceivenum ( String receivenum ) {
	this.receivenum = receivenum;
    }

}
