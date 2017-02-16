package com.timss.inventory.bean;

import java.math.BigDecimal;

/**
 * @title: 库存交易子表
 * @description: 库存交易子表
 * @company: gdyd
 * @className: InvMatTranDetail.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatTranDetail extends InvItemBaseField {

    private static final long serialVersionUID = 3363795915825123051L;

    private String	    imtdid;				 // 交易明细ID
    private String	    imtid;				  // 交易ID
    private String	    binid;				  // 货柜ID
    private String	    remark;				 // 备注
    private String	    inUnitid;			       // 入库单位
								       // IN_UNITID
    private String	    outUnitid;			      // 出库单位
								       // OUT_UNITID
    private String	    siteId;				 // 站点ID
								       // SITE_ID

    private BigDecimal	lotno;				  // 批次
    private BigDecimal	inQty;				  // 入库数量
								       // IN_QTY
    private BigDecimal	outQty;				 // 出库数量
								       // OUT_QTY
    private BigDecimal	price;				  // 单价（元）

    private String	    puraId;				  // 采购申请ID

    private BigDecimal	noTaxPrice;			      // 不含税单价
    private BigDecimal	tax;				     // 税额
    private BigDecimal	canOutQty;			       // 可出库数量
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
