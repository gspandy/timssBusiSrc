package com.timss.inventory.bean;

import java.math.BigDecimal;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;

/**
 * @title: 物资领料明细流程信息子表
 * @description: 物资领料明细流程信息子表
 * @company: gdyd
 * @className: InvMatApplyDetail.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatApplyDetail extends InvItemBaseField {

    private static final long serialVersionUID = 8529154253687082914L;

    @AutoGen(value = "INV_MATAPPLYD_ID", requireType = GenerationType.REQUIRED_NULL)
    private String imadid; // 领料明细ID
    private String imaid; // 领料ID
    private String siteId; // 站点ID

    private BigDecimal price; // 价格（含税或不含税）
    private BigDecimal noTaxPrice; //不含税单价（暂时只沙C多经用）
    private BigDecimal qtyApply; // 申请数量
    private BigDecimal stockqty; // 申请时库存数量

    private String status; // 领料状态

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
     * @return the imaid
     */
    public String getImaid() {
        return imaid;
    }

    /**
     * @param imaid the imaid to set
     */
    public void setImaid(String imaid) {
        this.imaid = imaid;
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
