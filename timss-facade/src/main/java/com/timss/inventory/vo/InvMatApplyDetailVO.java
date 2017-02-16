package com.timss.inventory.vo;

import java.math.BigDecimal;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyDetailVO.java
 * @author: 890166
 * @createDate: 2014-7-28
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatApplyDetailVO extends InvItemBaseField {

    private static final long serialVersionUID = 1L;

    private String imadid; // 领料明细ID
    private String imaid; // 领料ID
    private String siteId; // 站点ID
    private String warehouse; // 仓库
    private String bin; // 货柜
    private String binid; // 货柜id

    private String outqtytemp;
    private String unit1; // 单位
    private String unitCode1;

    private BigDecimal price = new BigDecimal( "0.00" ); //价格
    private BigDecimal totalprice = new BigDecimal( "0.00" ); // 价格小计
    private BigDecimal noTaxPrice; //不含税单价（暂时只沙C多经用）
    private BigDecimal totalNoTaxPrice = new BigDecimal( "0.00" ); // 不含税单价小计
    private BigDecimal waitqty; // 待领用量
    private BigDecimal outqty; // 已领数量
    private BigDecimal stockqty; // 现有库存 实际库存
    private BigDecimal nowqty;//可用库存
    private BigDecimal qtyApply; // 申请数量
    private BigDecimal precollarqty; // 预领用

    private BigDecimal outstockqty; // 出库量

    private String status; // 状态信息（领用完毕/部分领取）

    private String sendStatus; // 领料状态
    private String invcate;// 物资类型名称

    /**
     * @return the invcate
     */
    public String getInvcate() {
        return invcate;
    }

    /**
     * @param invcate the invcate to set
     */
    public void setInvcate(String invcate) {
        this.invcate = invcate;
    }

    /**
     * @return the sendStatus
     */
    public String getSendStatus() {
        return sendStatus;
    }

    /**
     * @param sendStatus the sendStatus to set
     */
    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
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
     * @return the waitqty
     */
    public BigDecimal getWaitqty() {
        return waitqty;
    }

    /**
     * @param waitqty the waitqty to set
     */
    public void setWaitqty(BigDecimal waitqty) {
        this.waitqty = waitqty;
    }

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
     * @return the outqtytemp
     */
    public String getOutqtytemp() {
        return outqtytemp == null ? "0" : outqtytemp;
    }

    /**
     * @param outqtytemp the outqtytemp to set
     */
    public void setOutqtytemp(String outqtytemp) {
        this.outqtytemp = outqtytemp;
    }

    private String lotno; // 批次

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
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return the precollarqty
     */
    public BigDecimal getPrecollarqty() {
        return precollarqty;
    }

    /**
     * @param precollarqty the precollarqty to set
     */
    public void setPrecollarqty(BigDecimal precollarqty) {
        this.precollarqty = precollarqty;
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
     * @return the totalprice
     */
    public BigDecimal getTotalprice() {
        return totalprice;
    }

    /**
     * @param totalprice the totalprice to set
     */
    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }

    /**
     * @return the outqty
     */
    public BigDecimal getOutqty() {
        return outqty;
    }

    /**
     * @param outqty the outqty to set
     */
    public void setOutqty(BigDecimal outqty) {
        this.outqty = outqty;
    }
    
    /**
     * @return the nowqty
     */
	public BigDecimal getNowqty() {
		return nowqty;
	}
	
	/**
     * @param nowqty the nowqty to set
     */
	public void setNowqty(BigDecimal nowqty) {
		this.nowqty = nowqty;
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
	 * @return the totalNoTaxPrice
	 */
	public BigDecimal getTotalNoTaxPrice() {
		return totalNoTaxPrice;
	}

	/**
	 * @param totalNoTaxPrice the totalNoTaxPrice to set
	 */
	public void setTotalNoTaxPrice(BigDecimal totalNoTaxPrice) {
		this.totalNoTaxPrice = totalNoTaxPrice;
	}

}
