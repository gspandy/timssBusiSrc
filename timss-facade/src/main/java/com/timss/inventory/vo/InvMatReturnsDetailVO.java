package com.timss.inventory.vo;

import java.math.BigDecimal;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatReturnsDetailVO.java
 * @author: 890166
 * @createDate: 2015-3-16
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatReturnsDetailVO extends InvItemBaseField {
    // [{"imrsdid":null,"itemid":"III1362","itemname":"笔刨","cusmodel":null,"itemcode":"M070110","imtno":null,"curReturnQty":null,"returnQty":0,"createuser":null,"createdate":null,"modifyuser":null,"modifydate":null,"siteid":null,"imrsid":null,"unitid":null,"unitname":"个","warehouseid":null,"warehousename":"科技公司仓库","binid":null,"binname":"3-3-1","purchorderId":null,"purchorderNo":null,"purchorderName":null,"purchorderType":null,"price":null,"imtdid":null,"outqty":2,"refundableqty":2,"refundqty":"1"},
    // {"imrsdid":null,"itemid":"III1361","itemname":"墨水","cusmodel":"PARKER  标准黑","itemcode":"M070109","imtno":null,"curReturnQty":null,"returnQty":0,"createuser":null,"createdate":null,"modifyuser":null,"modifydate":null,"siteid":null,"imrsid":null,"unitid":null,"unitname":"盒","warehouseid":null,"warehousename":"科技公司仓库","binid":null,"binname":"3-1-3","purchorderId":null,"purchorderNo":null,"purchorderName":null,"purchorderType":null,"price":null,"imtdid":null,"outqty":1,"refundableqty":1,"refundqty":"1"}]
    private String imrsdid;
    private String imtno;
    private BigDecimal curReturnQty;
    private BigDecimal returnQty;
    private String imrsid;
    private String unitid;
    private String unitname;
    private String warehousename;
    private String binid;
    private String binname;

    private String purchorderId;
    private String purchorderNo;
    private String purchorderName;
    private String purchorderType;
    private BigDecimal price;
    private String imtdid;

    private BigDecimal outqty;
    private BigDecimal refundableqty; // 可退库数量
    private BigDecimal refundqty;// 退货数量
    private BigDecimal assetQty;// 资产化数量

    private String imadid;// 领料申请明细id
    private String puraId;// 采购申请id
    private BigDecimal noTaxPrice;  //税前单价
    private BigDecimal tax;  //税额

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

    /**
     * @return the imrsdid
     */
    public String getImrsdid() {
        return imrsdid;
    }

    /**
     * @param imrsdid the imrsdid to set
     */
    public void setImrsdid(String imrsdid) {
        this.imrsdid = imrsdid;
    }

    /**
     * @return the imtno
     */
    public String getImtno() {
        return imtno;
    }

    /**
     * @param imtno the imtno to set
     */
    public void setImtno(String imtno) {
        this.imtno = imtno;
    }

    /**
     * @return the curReturnQty
     */
    public BigDecimal getCurReturnQty() {
        return curReturnQty;
    }

    /**
     * @param curReturnQty the curReturnQty to set
     */
    public void setCurReturnQty(BigDecimal curReturnQty) {
        this.curReturnQty = curReturnQty;
    }

    /**
     * @return the returnQty
     */
    public BigDecimal getReturnQty() {
        return returnQty;
    }

    /**
     * @param returnQty the returnQty to set
     */
    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    /**
     * @return the imrsid
     */
    public String getImrsid() {
        return imrsid;
    }

    /**
     * @param imrsid the imrsid to set
     */
    public void setImrsid(String imrsid) {
        this.imrsid = imrsid;
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
     * @return the purchorderId
     */
    public String getPurchorderId() {
        return purchorderId;
    }

    /**
     * @param purchorderId the purchorderId to set
     */
    public void setPurchorderId(String purchorderId) {
        this.purchorderId = purchorderId;
    }

    /**
     * @return the purchorderNo
     */
    public String getPurchorderNo() {
        return purchorderNo;
    }

    /**
     * @param purchorderNo the purchorderNo to set
     */
    public void setPurchorderNo(String purchorderNo) {
        this.purchorderNo = purchorderNo;
    }

    /**
     * @return the purchorderName
     */
    public String getPurchorderName() {
        return purchorderName;
    }

    /**
     * @param purchorderName the purchorderName to set
     */
    public void setPurchorderName(String purchorderName) {
        this.purchorderName = purchorderName;
    }

    /**
     * @return the purchorderType
     */
    public String getPurchorderType() {
        return purchorderType;
    }

    /**
     * @param purchorderType the purchorderType to set
     */
    public void setPurchorderType(String purchorderType) {
        this.purchorderType = purchorderType;
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
     * @return the imtdid
     */
    public String getImtdid() {
        return imtdid;
    }

    /**
     * @param imtdid the imtdid to set
     */
    public void setImtdid(String imtdid) {
        this.imtdid = imtdid;
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
     * @return the refundableqty
     */
    public BigDecimal getRefundableqty() {
        return refundableqty;
    }

    /**
     * @param refundableqty the refundableqty to set
     */
    public void setRefundableqty(BigDecimal refundableqty) {
        this.refundableqty = refundableqty;
    }

    /**
     * @return the refundqty
     */
    public BigDecimal getRefundqty() {
        return refundqty;
    }

    /**
     * @param refundqty the refundqty to set
     */
    public void setRefundqty(BigDecimal refundqty) {
        this.refundqty = refundqty;
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
	 * @return the puraId
	 */
	public String getPuraId() {
		return puraId;
	}

	/**
	 * @param puraId the puraId to set
	 */
	public void setPuraId(String puraId) {
		this.puraId = puraId;
	}

	/**
	 * @return the assetQty
	 */
	public BigDecimal getAssetQty() {
		return assetQty;
	}

	/**
	 * @param assetQty the assetQty to set
	 */
	public void setAssetQty(BigDecimal assetQty) {
		this.assetQty = assetQty;
	}
   
    
}
