package com.timss.inventory.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;

/**
 * @title: 库存实时数据表
 * @description: 库存实时数据表
 * @company: gdyd
 * @className: InvRealTimeData.java
 * @author: 890151
 * @createDate: 2016年5月5日
 * @updateUser: 890151
 * @version: 1.0
 */
public class InvRealTimeData extends InvItemBaseField {

    private static final long serialVersionUID = -1609141610028699980L;

    @UUIDGen ( requireType = GenerationType.REQUIRED_NULL )
    private String	    irtdId;				  	// 实时数据表主键ID
    private BigDecimal	canUseQty;			  	// 可用库存数量
    private BigDecimal	canUseQtyOld;			// 可用库存数量（旧）
    private BigDecimal	actualQty;			  	// 实际库存数量
    private BigDecimal	canOutQtyTotal;			// 物资可出库数量总和
    private BigDecimal	withTaxPrice;			// 含税金额单价
    private BigDecimal	noTaxPrice;			    // 不含税金额单价
    private BigDecimal	tax;				    // 税额
    private BigDecimal	withTaxPriceOld;		// 含税金额单价（旧）
    private BigDecimal	noTaxPriceOld;			// 不含税金额单价（旧）
    private BigDecimal	taxOld;				    // 税额  （旧）
    private Date 		lastInTime; 			// 最近入库时间
    private BigDecimal	lastInQty;			    // 最近入库数量
    private BigDecimal	lastInPrice;			// 最近入库含税单价
    private BigDecimal	lastInNoTaxPrice;		// 最近入库不含税单价
    private BigDecimal	lastInTax;				// 最近入库税额

    /**
     * @return the irtdId
     */
    public String getIrtdId () {
	return irtdId;
    }

    /**
     * @param irtdId the irtdId to set
     */
    public void setIrtdId ( String irtdId ) {
	this.irtdId = irtdId;
    }

    /**
     * @return the canUseQty
     */
    public BigDecimal getCanUseQty () {
	return canUseQty;
    }

    /**
     * @param canUseQty the canUseQty to set
     */
    public void setCanUseQty ( BigDecimal canUseQty ) {
	this.canUseQty = canUseQty;
    }

    /**
	 * @return the canUseQtyOld
	 */
	public BigDecimal getCanUseQtyOld() {
		return canUseQtyOld;
	}

	/**
	 * @param canUseQtyOld the canUseQtyOld to set
	 */
	public void setCanUseQtyOld(BigDecimal canUseQtyOld) {
		this.canUseQtyOld = canUseQtyOld;
	}

	/**
     * @return the actualQty
     */
    public BigDecimal getActualQty () {
	return actualQty;
    }

    /**
     * @param actualQty the actualQty to set
     */
    public void setActualQty ( BigDecimal actualQty ) {
	this.actualQty = actualQty;
    }

    /**
     * @return the withTaxPrice
     */
    public BigDecimal getWithTaxPrice () {
	return withTaxPrice;
    }

    /**
     * @param withTaxPrice the withTaxPrice to set
     */
    public void setWithTaxPrice ( BigDecimal withTaxPrice ) {
	this.withTaxPrice = withTaxPrice;
    }

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
     * @return the lastInQty
     */
    public BigDecimal getLastInQty () {
	return lastInQty;
    }

    /**
     * @param lastInQty the lastInQty to set
     */
    public void setLastInQty ( BigDecimal lastInQty ) {
	this.lastInQty = lastInQty;
    }

    /**
     * @return the lastInPrice
     */
    public BigDecimal getLastInPrice () {
	return lastInPrice;
    }

    /**
     * @param lastInPrice the lastInPrice to set
     */
    public void setLastInPrice ( BigDecimal lastInPrice ) {
	this.lastInPrice = lastInPrice;
    }

	/**
	 * @return the canOutQtyTotal
	 */
	public BigDecimal getCanOutQtyTotal() {
		return canOutQtyTotal;
	}

	/**
	 * @param canOutQtyTotal the canOutQtyTotal to set
	 */
	public void setCanOutQtyTotal(BigDecimal canOutQtyTotal) {
		this.canOutQtyTotal = canOutQtyTotal;
	}

	/**
	 * @return the withTaxPriceOld
	 */
	public BigDecimal getWithTaxPriceOld() {
		return withTaxPriceOld;
	}

	/**
	 * @param withTaxPriceOld the withTaxPriceOld to set
	 */
	public void setWithTaxPriceOld(BigDecimal withTaxPriceOld) {
		this.withTaxPriceOld = withTaxPriceOld;
	}

	/**
	 * @return the noTaxPriceOld
	 */
	public BigDecimal getNoTaxPriceOld() {
		return noTaxPriceOld;
	}

	/**
	 * @param noTaxPriceOld the noTaxPriceOld to set
	 */
	public void setNoTaxPriceOld(BigDecimal noTaxPriceOld) {
		this.noTaxPriceOld = noTaxPriceOld;
	}

	/**
	 * @return the taxOld
	 */
	public BigDecimal getTaxOld() {
		return taxOld;
	}

	/**
	 * @param taxOld the taxOld to set
	 */
	public void setTaxOld(BigDecimal taxOld) {
		this.taxOld = taxOld;
	}

	/**
	 * @return the lastInTime
	 */
	public Date getLastInTime() {
		return lastInTime;
	}

	/**
	 * @param lastInTime the lastInTime to set
	 */
	public void setLastInTime(Date lastInTime) {
		this.lastInTime = lastInTime;
	}

	/**
	 * @return the lastInNoTaxPrice
	 */
	public BigDecimal getLastInNoTaxPrice() {
		return lastInNoTaxPrice;
	}

	/**
	 * @param lastInNoTaxPrice the lastInNoTaxPrice to set
	 */
	public void setLastInNoTaxPrice(BigDecimal lastInNoTaxPrice) {
		this.lastInNoTaxPrice = lastInNoTaxPrice;
	}

	/**
	 * @return the lastInTax
	 */
	public BigDecimal getLastInTax() {
		return lastInTax;
	}

	/**
	 * @param lastInTax the lastInTax to set
	 */
	public void setLastInTax(BigDecimal lastInTax) {
		this.lastInTax = lastInTax;
	}
    
    

}
