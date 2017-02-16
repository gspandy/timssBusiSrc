package com.timss.inventory.bean;

import java.math.BigDecimal;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatSnapshot.java
 * @author: 890166
 * @createDate: 2014-11-20
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatSnapshotDetail extends InvItemBaseField {

    private static final long serialVersionUID = 1L;

    private String imsdid; // 业务ID
    private String imsid; // 父ID
    private String invcatename; // 物资类型名称
    private String cusmodel; // 物资参数
    private String warehousename; // 仓库名称
    private String binid; // 货柜ID
    private String binname; // 货柜名
    private String unitid; // 单位ID
    private String unitname; // 单位名称
    private BigDecimal stockQty; // 当前库存数量
    private BigDecimal price; // 平均含税成本（元）
    private BigDecimal noTaxPrice; // 平均不含税成本（元）
    private BigDecimal tax; // 平均税额（元）

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
