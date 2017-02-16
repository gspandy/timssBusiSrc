package com.timss.inventory.vo;

import java.math.BigDecimal;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvStocktakingDetailVO.java
 * @author: 890166
 * @createDate: 2014-10-8
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvStocktakingDetailVO extends InvItemBaseField {

    private String istdid; // 盘点明细ID
    private String istid; // 盘点审批ID
    private String remark; // 备注
    private String binid; // 货柜ID
    private String siteId; // 站点ID

    private BigDecimal qtyBefore; // 盘点前数量
    private BigDecimal qtyAfter; // 盘点后数量

    private BigDecimal price; // 单价（元）

    private String unitname; // 单位名称
    private String unitid; // 单位id
    private String bin;
    private String breakeven;
    private String nowqty;//可用库存
    private String stockqty;//实际库存

    /**
     * @return the istdid
     */
    public String getIstdid() {
        return istdid;
    }

    /**
     * @param istdid the istdid to set
     */
    public void setIstdid(String istdid) {
        this.istdid = istdid;
    }

    /**
     * @return the istid
     */
    public String getIstid() {
        return istid;
    }

    /**
     * @param istid the istid to set
     */
    public void setIstid(String istid) {
        this.istid = istid;
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
     * @return the qtyBefore
     */
    public BigDecimal getQtyBefore() {
        return qtyBefore;
    }

    /**
     * @param qtyBefore the qtyBefore to set
     */
    public void setQtyBefore(BigDecimal qtyBefore) {
        this.qtyBefore = qtyBefore;
    }

    /**
     * @return the qtyAfter
     */
    public BigDecimal getQtyAfter() {
        return qtyAfter;
    }

    /**
     * @param qtyAfter the qtyAfter to set
     */
    public void setQtyAfter(BigDecimal qtyAfter) {
        this.qtyAfter = qtyAfter;
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
     * @return the unitname
     */
    public String getUnitname() {
        return unitname;
    }

    /**
     * @param unitname the unitname to set
     */
    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    /**
     * @return the unitid
     */
    public String getUnitid() {
        return unitid;
    }

    /**
     * @param unitid the unitid to set
     */
    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    /**
     * @return the bin
     */
    public String getBin() {
        return bin;
    }

    /**
     * @param bin the bin to set
     */
    public void setBin(String bin) {
        this.bin = bin;
    }

    /**
     * @return the breakeven
     */
    public String getBreakeven() {
        return breakeven;
    }

    /**
     * @param breakeven the breakeven to set
     */
    public void setBreakeven(String breakeven) {
        this.breakeven = breakeven;
    }

    /**
     * @return the nowqty
     */
	public String getNowqty() {
		return nowqty;
	}
	
	/**
     * @param nowqty the nowqty to set
     */
	public void setNowqty(String nowqty) {
		this.nowqty = nowqty;
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
    
    

}
