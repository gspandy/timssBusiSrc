package com.timss.inventory.bean;

import java.math.BigDecimal;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatRecipientsDetail.java
 * @author: 890166
 * @createDate: 2014-9-26
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatRecipientsDetail extends InvItemBaseField {

    private static final long serialVersionUID = -6135166285939848805L;

    @AutoGen(value = "INV_MATRECDETAIL_ID", requireType = GenerationType.REQUIRED_NULL)
    private String imrdid; // 子单id
    private String imrid; // 物资领用id
    private BigDecimal outstockqty; // 领用数量
    private String siteId;
    private BigDecimal qtyApply;
    private String binid;
    private String imadid;

    private String isled;
    private BigDecimal price;//价格（含税或不含税）
    private BigDecimal noTaxPrice;//不含税单价（暂时只只沙C多经用）

    /**
     * @return the isled
     */
    public String getIsled() {
        return isled;
    }

    /**
     * @param isled the isled to set
     */
    public void setIsled(String isled) {
        this.isled = isled;
    }

    /**
     * @return the imadid
     */
    public String getImadid() {
        return imadid;
    }

    /**
     * @param imadid the imadid to set
     */
    public void setImadid(String imadid) {
        this.imadid = imadid;
    }

    /**
     * @return the imrdid
     */
    public String getImrdid() {
        return imrdid;
    }

    /**
     * @param imrdid the imrdid to set
     */
    public void setImrdid(String imrdid) {
        this.imrdid = imrdid;
    }

    /**
     * @return the imrid
     */
    public String getImrid() {
        return imrid;
    }

    /**
     * @param imrid the imrid to set
     */
    public void setImrid(String imrid) {
        this.imrid = imrid;
    }

    /**
     * @return the outstockqty
     */
    public BigDecimal getOutstockqty() {
        return outstockqty;
    }

    /**
     * @param outstockqty the outstockqty to set
     */
    public void setOutstockqty(BigDecimal outstockqty) {
        this.outstockqty = outstockqty;
    }

    /**
     * @return the qtyApply
     */
    public BigDecimal getQtyApply() {
        return qtyApply;
    }

    /**
     * @param qtyApply the qtyApply to set
     */
    public void setQtyApply(BigDecimal qtyApply) {
        this.qtyApply = qtyApply;
    }

    /**
     * @return the binid
     */
    public String getBinid() {
        return binid;
    }

    /**
     * @param binid the binid to set
     */
    public void setBinid(String binid) {
        this.binid = binid;
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
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the noTaxPrice
	 */
	public BigDecimal getNoTaxPrice() {
		return noTaxPrice;
	}

	/**
	 * @param noTaxPrice the noTaxPrice to set
	 */
	public void setNoTaxPrice(BigDecimal noTaxPrice) {
		this.noTaxPrice = noTaxPrice;
	}
	
}
