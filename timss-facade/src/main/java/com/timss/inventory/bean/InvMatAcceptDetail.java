package com.timss.inventory.bean;

import java.math.BigDecimal;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;

/**
 * @title: 物资验收物资类
 * @description: 物资验收物资类
 * @company: gdyd
 * @className: InvMatAcceptDetail.java
 * @author: yuanzh
 * @createDate: 2015-10-30
 * @updateUser: yuanzh
 * @version: 1.0
 */
@SuppressWarnings ( "serial" )
public class InvMatAcceptDetail extends InvItemBaseField {
    @UUIDGen ( requireType = GenerationType.REQUIRED_NULL )
    private String     inacdId;

    private String     inacId;
    private BigDecimal itemnum;
    private BigDecimal acceptnum;
    private String     attr1;
    private String     attr10;
    private String     attr9;
    private String     attr8;
    private String     attr7;
    private String     attr6;
    private String     attr5;
    private String     attr4;
    private String     attr3;
    private String     attr2;
    private String     delFlag;
    private String     puraId;
    private String     purapplyUser;
    private String     purapplyUsercode;

    private BigDecimal price;
    private String     binid;
    private BigDecimal taxRate;

    /**
     * @return the taxRate
     */
    public BigDecimal getTaxRate () {
	return taxRate;
    }

    /**
     * @param taxRate the taxRate to set
     */
    public void setTaxRate ( BigDecimal taxRate ) {
	this.taxRate = taxRate;
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

    public BigDecimal getPrice () {
	return price;
    }

    public void setPrice ( BigDecimal price ) {
	this.price = price;
    }

    /**
     * @return the inacdId
     */
    public String getInacdId () {
	return inacdId;
    }

    /**
     * @param inacdId the inacdId to set
     */
    public void setInacdId ( String inacdId ) {
	this.inacdId = inacdId;
    }

    /**
     * @return the inacId
     */
    public String getInacId () {
	return inacId;
    }

    /**
     * @param inacId the inacId to set
     */
    public void setInacId ( String inacId ) {
	this.inacId = inacId;
    }

    /**
     * @return the itemnum
     */
    public BigDecimal getItemnum () {
	return itemnum;
    }

    /**
     * @param itemnum the itemnum to set
     */
    public void setItemnum ( BigDecimal itemnum ) {
	this.itemnum = itemnum;
    }

    /**
     * @return the acceptnum
     */
    public BigDecimal getAcceptnum () {
	return acceptnum;
    }

    /**
     * @param acceptnum the acceptnum to set
     */
    public void setAcceptnum ( BigDecimal acceptnum ) {
	this.acceptnum = acceptnum;
    }

    /**
     * @return the attr1
     */
    public String getAttr1 () {
	return attr1;
    }

    /**
     * @param attr1 the attr1 to set
     */
    public void setAttr1 ( String attr1 ) {
	this.attr1 = attr1;
    }

    /**
     * @return the attr10
     */
    public String getAttr10 () {
	return attr10;
    }

    /**
     * @param attr10 the attr10 to set
     */
    public void setAttr10 ( String attr10 ) {
	this.attr10 = attr10;
    }

    /**
     * @return the attr9
     */
    public String getAttr9 () {
	return attr9;
    }

    /**
     * @param attr9 the attr9 to set
     */
    public void setAttr9 ( String attr9 ) {
	this.attr9 = attr9;
    }

    /**
     * @return the attr8
     */
    public String getAttr8 () {
	return attr8;
    }

    /**
     * @param attr8 the attr8 to set
     */
    public void setAttr8 ( String attr8 ) {
	this.attr8 = attr8;
    }

    /**
     * @return the attr7
     */
    public String getAttr7 () {
	return attr7;
    }

    /**
     * @param attr7 the attr7 to set
     */
    public void setAttr7 ( String attr7 ) {
	this.attr7 = attr7;
    }

    /**
     * @return the attr6
     */
    public String getAttr6 () {
	return attr6;
    }

    /**
     * @param attr6 the attr6 to set
     */
    public void setAttr6 ( String attr6 ) {
	this.attr6 = attr6;
    }

    /**
     * @return the attr5
     */
    public String getAttr5 () {
	return attr5;
    }

    /**
     * @param attr5 the attr5 to set
     */
    public void setAttr5 ( String attr5 ) {
	this.attr5 = attr5;
    }

    /**
     * @return the attr4
     */
    public String getAttr4 () {
	return attr4;
    }

    /**
     * @param attr4 the attr4 to set
     */
    public void setAttr4 ( String attr4 ) {
	this.attr4 = attr4;
    }

    /**
     * @return the attr3
     */
    public String getAttr3 () {
	return attr3;
    }

    /**
     * @param attr3 the attr3 to set
     */
    public void setAttr3 ( String attr3 ) {
	this.attr3 = attr3;
    }

    /**
     * @return the attr2
     */
    public String getAttr2 () {
	return attr2;
    }

    /**
     * @param attr2 the attr2 to set
     */
    public void setAttr2 ( String attr2 ) {
	this.attr2 = attr2;
    }

    /**
     * @return the delFlag
     */
    public String getDelFlag () {
	return delFlag;
    }

    /**
     * @param delFlag the delFlag to set
     */
    public void setDelFlag ( String delFlag ) {
	this.delFlag = delFlag;
    }

    /**
     * @return the puraId
     */
    public String getPuraId () {
	return puraId;
    }

    /**
     * @param puraId the puraId to set
     */
    public void setPuraId ( String puraId ) {
	this.puraId = puraId;
    }

    /**
     * @return the purapplyUser
     */
    public String getPurapplyUser () {
	return purapplyUser;
    }

    /**
     * @param purapplyUser the purapplyUser to set
     */
    public void setPurapplyUser ( String purapplyUser ) {
	this.purapplyUser = purapplyUser;
    }

    /**
     * @return the purapplyUsercode
     */
    public String getPurapplyUsercode () {
	return purapplyUsercode;
    }

    /**
     * @param purapplyUsercode the purapplyUsercode to set
     */
    public void setPurapplyUsercode ( String purapplyUsercode ) {
	this.purapplyUsercode = purapplyUsercode;
    }

}
