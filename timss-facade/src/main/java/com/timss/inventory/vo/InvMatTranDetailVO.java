package com.timss.inventory.vo;

import java.math.BigDecimal;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranDetailVO.java
 * @author: 890166
 * @createDate: 2014-7-15
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatTranDetailVO extends InvItemBaseField {

    private String     imtdid;       // 交易明细ID
    private String     imtid;	// 交易ID
    private String     binid;	// 货柜ID
    private String     remark;       // 备注
    private String     inUnitid;     // 入库单位 IN_UNITID
    private String     outUnitid;    // 出库单位 OUT_UNITID
    private String     siteId;       // 站点ID SITE_ID
    private String     stockQty;     // 库存数量 stock_qty
    private String     warehousename; // 仓库名称
    private String     binname;      // 货柜名称
    private String     opertype;     // 操作类型
    private String     trantype;     // 操作类型(原始)
    private String     outterSheetno; // 外部关联单号outter_sheetno
    private String     sheetno;
    private String     sheetname;     // 单据名称
    private String     status;       // 状态

    private BigDecimal lotno;	// 批次
    private BigDecimal inQty;	// 入库数量 IN_QTY
    private BigDecimal outQty;       // 出库数量 OUT_QTY
    private BigDecimal price;	// 单价（元）
    private BigDecimal totalPrice;   // 小计（元） total_price

    private String     nowqty;       // 库存数量 stock_qty

    private BigDecimal noTaxPrice;   // 不含税单价
    private BigDecimal tax;	  // 税额
    private BigDecimal canOutQty;    // 可出库数量
    private String	priceFlag;			       // 价格标志位

    /**
     * @return the noTaxPrice
     */
    public BigDecimal getNoTaxPrice () {
	return noTaxPrice;
    }

    /**
     * @param noTaxPrice the noTaxPrice to set
     */
    public void setNoTaxPrice ( BigDecimal noTaxPrice ) {
	this.noTaxPrice = noTaxPrice;
    }

    /**
     * @return the tax
     */
    public BigDecimal getTax () {
	return tax;
    }

    /**
     * @param tax the tax to set
     */
    public void setTax ( BigDecimal tax ) {
	this.tax = tax;
    }

    /**
     * @return the canOutQty
     */
    public BigDecimal getCanOutQty () {
	return canOutQty;
    }

    /**
     * @param canOutQty the canOutQty to set
     */
    public void setCanOutQty ( BigDecimal canOutQty ) {
	this.canOutQty = canOutQty;
    }

    /**
     * @return the imtdid
     */
    public String getImtdid () {
	return imtdid;
    }

    /**
     * @param imtdid the imtdid to set
     */
    public void setImtdid ( String imtdid ) {
	this.imtdid = imtdid;
    }

    /**
     * @return the imtid
     */
    public String getImtid () {
	return imtid;
    }

    /**
     * @param imtid the imtid to set
     */
    public void setImtid ( String imtid ) {
	this.imtid = imtid;
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
     * @return the remark
     */
    public String getRemark () {
	return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark ( String remark ) {
	this.remark = remark;
    }

    /**
     * @return the inUnitid
     */
    public String getInUnitid () {
	return inUnitid;
    }

    /**
     * @param inUnitid the inUnitid to set
     */
    public void setInUnitid ( String inUnitid ) {
	this.inUnitid = inUnitid;
    }

    /**
     * @return the outUnitid
     */
    public String getOutUnitid () {
	return outUnitid;
    }

    /**
     * @param outUnitid the outUnitid to set
     */
    public void setOutUnitid ( String outUnitid ) {
	this.outUnitid = outUnitid;
    }

    /**
     * @return the siteId
     */
    public String getSiteId () {
	return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId ( String siteId ) {
	this.siteId = siteId;
    }

    /**
     * @return the stockQty
     */
    public String getStockQty () {
	return stockQty;
    }

    /**
     * @param stockQty the stockQty to set
     */
    public void setStockQty ( String stockQty ) {
	this.stockQty = stockQty;
    }

    /**
     * @return the warehousename
     */
    public String getWarehousename () {
	return warehousename;
    }

    /**
     * @param warehousename the warehousename to set
     */
    public void setWarehousename ( String warehousename ) {
	this.warehousename = warehousename;
    }

    /**
     * @return the binname
     */
    public String getBinname () {
	return binname;
    }

    /**
     * @param binname the binname to set
     */
    public void setBinname ( String binname ) {
	this.binname = binname;
    }

    /**
     * @return the opertype
     */
    public String getOpertype () {
	return opertype;
    }

    /**
     * @param opertype the opertype to set
     */
    public void setOpertype ( String opertype ) {
	this.opertype = opertype;
    }

    /**
     * @return the outterSheetno
     */
    public String getOutterSheetno () {
	return outterSheetno;
    }

    /**
     * @param outterSheetno the outterSheetno to set
     */
    public void setOutterSheetno ( String outterSheetno ) {
	this.outterSheetno = outterSheetno;
    }

    /**
     * @return the sheetno
     */
    public String getSheetno () {
	return sheetno;
    }

    /**
     * @param sheetno the sheetno to set
     */
    public void setSheetno ( String sheetno ) {
	this.sheetno = sheetno;
    }

    /**
     * @return the status
     */
    public String getStatus () {
	return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus ( String status ) {
	this.status = status;
    }

    /**
     * @return the lotno
     */
    public BigDecimal getLotno () {
	return lotno;
    }

    /**
     * @param lotno the lotno to set
     */
    public void setLotno ( BigDecimal lotno ) {
	this.lotno = lotno;
    }

    /**
     * @return the inQty
     */
    public BigDecimal getInQty () {
	return inQty;
    }

    /**
     * @param inQty the inQty to set
     */
    public void setInQty ( BigDecimal inQty ) {
	this.inQty = inQty;
    }

    /**
     * @return the outQty
     */
    public BigDecimal getOutQty () {
	return outQty;
    }

    /**
     * @param outQty the outQty to set
     */
    public void setOutQty ( BigDecimal outQty ) {
	this.outQty = outQty;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice () {
	return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice ( BigDecimal price ) {
	this.price = price;
    }

    /**
     * @return the totalPrice
     */
    public BigDecimal getTotalPrice () {
	return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice ( BigDecimal totalPrice ) {
	this.totalPrice = totalPrice;
    }

    /**
     * @return the nowqty
     */
    public String getNowqty () {
	return nowqty;
    }

    /**
     * @param nowqty the nowqty to set
     */
    public void setNowqty ( String nowqty ) {
	this.nowqty = nowqty;
    }

    public String getSheetname () {
	return sheetname;
    }

    public void setSheetname ( String sheetname ) {
	this.sheetname = sheetname;
    }

    public String getTrantype () {
	return trantype;
    }

    public void setTrantype ( String trantype ) {
	this.trantype = trantype;
    }

	/**
	 * @return the priceFlag
	 */
	public String getPriceFlag() {
		return priceFlag;
	}

	/**
	 * @param priceFlag the priceFlag to set
	 */
	public void setPriceFlag(String priceFlag) {
		this.priceFlag = priceFlag;
	}

}
