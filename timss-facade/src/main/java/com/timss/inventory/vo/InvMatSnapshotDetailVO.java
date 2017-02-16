package com.timss.inventory.vo;

import java.math.BigDecimal;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatSnapshotDetailVO.java
 * @author: 890166
 * @createDate: 2014-11-24
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatSnapshotDetailVO extends InvItemBaseField {

    private String imsdid; // 业务ID
    private String imsid; // 父ID
    private String invcatename; // 物资类型名称
    private String warehousename; // 仓库名称
    private String binid; // 货柜ID
    private String binname; // 货柜名
    private String unitid; // 单位ID
    private String unitname; // 单位名称
    private BigDecimal stockQty; // 快照库存数量
    private BigDecimal price; // 快照平均成本（元）
    private BigDecimal noTaxPrice; // 快照平均不含税成本（元）
    private BigDecimal tax; // 快照平均税额（元）
    private BigDecimal stockQtyNow; // 当前库存数量
    private BigDecimal priceNow; // 当前平均成本（元）
    private String quickSearch; // 快速查询
    private String ishis; // 是否历史库存

    /**
     * @return the imsdid
     */
    public String getImsdid() {
        return imsdid;
    }

    /**
     * @param imsdid the imsdid to set
     */
    public void setImsdid(String imsdid) {
        this.imsdid = imsdid;
    }

    /**
     * @return the imsid
     */
    public String getImsid() {
        return imsid;
    }

    /**
     * @param imsid the imsid to set
     */
    public void setImsid(String imsid) {
        this.imsid = imsid;
    }

    /**
     * @return the invcatename
     */
    public String getInvcatename() {
        return invcatename;
    }

    /**
     * @param invcatename the invcatename to set
     */
    public void setInvcatename(String invcatename) {
        this.invcatename = invcatename;
    }

    /**
     * @return the warehousename
     */
    public String getWarehousename() {
        return warehousename;
    }

    /**
     * @param warehousename the warehousename to set
     */
    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
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
     * @return the binname
     */
    public String getBinname() {
        return binname;
    }

    /**
     * @param binname the binname to set
     */
    public void setBinname(String binname) {
        this.binname = binname;
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
     * @return the stockQty
     */
    public BigDecimal getStockQty() {
        return stockQty;
    }

    /**
     * @param stockQty the stockQty to set
     */
    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
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
     * @return the stockQtyNow
     */
    public BigDecimal getStockQtyNow() {
        return stockQtyNow;
    }

    /**
     * @param stockQtyNow the stockQtyNow to set
     */
    public void setStockQtyNow(BigDecimal stockQtyNow) {
        this.stockQtyNow = stockQtyNow;
    }

    /**
     * @return the priceNow
     */
    public BigDecimal getPriceNow() {
        return priceNow;
    }

    /**
     * @param priceNow the priceNow to set
     */
    public void setPriceNow(BigDecimal priceNow) {
        this.priceNow = priceNow;
    }

    /**
     * @return the quickSearch
     */
    public String getQuickSearch() {
        return quickSearch;
    }

    /**
     * @param quickSearch the quickSearch to set
     */
    public void setQuickSearch(String quickSearch) {
        this.quickSearch = quickSearch;
    }

    /**
     * @return the ishis
     */
    public String getIshis() {
        return ishis;
    }

    /**
     * @param ishis the ishis to set
     */
    public void setIshis(String ishis) {
        this.ishis = ishis;
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

	/**
	 * @return the tax
	 */
	public BigDecimal getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

}
