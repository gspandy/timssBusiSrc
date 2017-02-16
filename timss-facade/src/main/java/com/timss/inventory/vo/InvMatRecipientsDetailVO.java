package com.timss.inventory.vo;

import java.math.BigDecimal;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatRecipientsDetailVO.java
 * @author: 890166
 * @createDate: 2014-9-28
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatRecipientsDetailVO extends InvItemBaseField {

    private String imrdid; // 子单id
    private String imrid; // 物资领用id
    private String imtdid; //流水子表id
    private BigDecimal outstockqty; // 领用数量
    private String siteId;
    private BigDecimal qtyApply;
    private String binid;
    private String warehouse;
    private String bin;
    private String unit1;
    private String unitCode1;
    private BigDecimal price;
    private String imadid;
    private String remark;
    private String isled;
    private BigDecimal  noTaxPrice; // 不含税单价
    private BigDecimal  tax;  // 税额
    private BigDecimal  canOutQty;    
    private String  invcatename; //物资类型名称
    private BigDecimal  actualQty; //实际库存
    private String sheetno; // 相关联的发料单单号
    
    public String getImtdid() {
        return imtdid;
    }

    public void setImtdid(String imtdid) {
        this.imtdid = imtdid;
    }

    public BigDecimal getNoTaxPrice() {
        return noTaxPrice;
    }

    public void setNoTaxPrice(BigDecimal noTaxPrice) {
        this.noTaxPrice = noTaxPrice;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getCanOutQty() {
        return canOutQty;
    }

    public void setCanOutQty(BigDecimal canOutQty) {
        this.canOutQty = canOutQty;
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
     * @return the warehouse
     */
    public String getWarehouse() {
        return warehouse;
    }

    /**
     * @param warehouse the warehouse to set
     */
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
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
     * @return the unit1
     */
    public String getUnit1() {
        return unit1;
    }

    /**
     * @param unit1 the unit1 to set
     */
    public void setUnit1(String unit1) {
        this.unit1 = unit1;
    }

    /**
     * @return the unitCode1
     */
    public String getUnitCode1() {
        return unitCode1;
    }

    /**
     * @param unitCode1 the unitCode1 to set
     */
    public void setUnitCode1(String unitCode1) {
        this.unitCode1 = unitCode1;
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
	 * @return the actualQty
	 */
	public BigDecimal getActualQty() {
		return actualQty;
	}

	/**
	 * @param actualQty the actualQty to set
	 */
	public void setActualQty(BigDecimal actualQty) {
		this.actualQty = actualQty;
	}

	/**
	 * @return the sheetno
	 */
	public String getSheetno() {
		return sheetno;
	}

	/**
	 * @param sheetno the sheetno to set
	 */
	public void setSheetno(String sheetno) {
		this.sheetno = sheetno;
	}
	
}
