package com.timss.purchase.vo;

import java.io.Serializable;

/**
 * @title: 发票采购清单
 * @description: {desc}
 * @company: gdyd
 * @className: PurInvoiceAssetVo.java
 * @author: fengzt
 * @createDate: 2015年9月18日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PurInvoiceAssetVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6972253153339305176L;

    /**
     * 物资Id
     */
    private String itemId;
    /**
     * 物资编号
     */
    private String itemCode;
    /**
     * 物资名称
     */
    private String purchName;

    /**
     * 物资型号
     */
    private String type;

    /**
     * 单位
     */
    private String unit;

    /**
     * 采购单价
     */
    private double unitPrice;

    /**
     * 采购量
     */
    private double mount;

    /**
     * 已接收数量
     */
    private double receivedMount;

    /**
     * 已接收金额
     */
    private double receivedPrice;

    /**
     * 发票单价
     */
    private double noTaxInvoicePrice;

    /**
     * 含税单价
     */
    private double taxUnitPrice;

    /**
     * 税额
     */
    private double taxSum;

    /**
     * 不含税开票金额
     */
    private double noTaxSumPrice;

    /**
     * 版本号
     */
    private String version;

    /**
     * 物资流水号
     */
    private String imtNo;

    /**
     * 是否已经入库
     */
    private String isReceived;

    /**
     * 是否已经报账 Y：已报账 N：未报账
     */
    private String isrmbrs;

    private String warehouseid;

    /**
     * @return the warehouseid
     */
    public String getWarehouseid() {
        return warehouseid;
    }

    /**
     * @param warehouseid the warehouseid to set
     */
    public void setWarehouseid(String warehouseid) {
        this.warehouseid = warehouseid;
    }

    /**
     * @return the isrmbrs
     */
    public String getIsrmbrs() {
        return isrmbrs;
    }

    /**
     * @param isrmbrs the isrmbrs to set
     */
    public void setIsrmbrs(String isrmbrs) {
        this.isrmbrs = isrmbrs;
    }

    /**
     * @return the isReceived
     */
    public String getIsReceived() {
        return isReceived;
    }

    /**
     * @return the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @param isReceived the isReceived to set
     */
    public void setIsReceived(String isReceived) {
        this.isReceived = isReceived;
    }

    public String getPurchName() {
        return purchName;
    }

    public void setPurchName(String purchName) {
        this.purchName = purchName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getMount() {
        return mount;
    }

    public void setMount(double mount) {
        this.mount = mount;
    }

    public double getReceivedMount() {
        return receivedMount;
    }

    public void setReceivedMount(double receivedMount) {
        this.receivedMount = receivedMount;
    }

    public double getReceivedPrice() {
        return receivedPrice;
    }

    public void setReceivedPrice(double receivedPrice) {
        this.receivedPrice = receivedPrice;
    }

    public double getNoTaxInvoicePrice() {
        return noTaxInvoicePrice;
    }

    public void setNoTaxInvoicePrice(double noTaxInvoicePrice) {
        this.noTaxInvoicePrice = noTaxInvoicePrice;
    }

    public double getTaxUnitPrice() {
        return taxUnitPrice;
    }

    public void setTaxUnitPrice(double taxUnitPrice) {
        this.taxUnitPrice = taxUnitPrice;
    }

    public double getTaxSum() {
        return taxSum;
    }

    public void setTaxSum(double taxSum) {
        this.taxSum = taxSum;
    }

    public double getNoTaxSumPrice() {
        return noTaxSumPrice;
    }

    public void setNoTaxSumPrice(double noTaxSumPrice) {
        this.noTaxSumPrice = noTaxSumPrice;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the imtNo
     */
    public String getImtNo() {
        return imtNo;
    }

    /**
     * @param imtNo the imtNo to set
     */
    public void setImtNo(String imtNo) {
        this.imtNo = imtNo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imtNo == null) ? 0 : imtNo.hashCode());
        result = prime * result + ((isReceived == null) ? 0 : isReceived.hashCode());
        result = prime * result + ((isrmbrs == null) ? 0 : isrmbrs.hashCode());
        result = prime * result + ((itemCode == null) ? 0 : itemCode.hashCode());
        result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
        long temp;
        temp = Double.doubleToLongBits( mount );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( noTaxInvoicePrice );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( noTaxSumPrice );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((purchName == null) ? 0 : purchName.hashCode());
        temp = Double.doubleToLongBits( receivedMount );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( receivedPrice );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( taxSum );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( taxUnitPrice );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        temp = Double.doubleToLongBits( unitPrice );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        PurInvoiceAssetVo other = (PurInvoiceAssetVo) obj;
        if ( imtNo == null ) {
            if ( other.imtNo != null )
                return false;
        } else if ( !imtNo.equals( other.imtNo ) )
            return false;
        if ( isReceived == null ) {
            if ( other.isReceived != null )
                return false;
        } else if ( !isReceived.equals( other.isReceived ) )
            return false;
        if ( isrmbrs == null ) {
            if ( other.isrmbrs != null )
                return false;
        } else if ( !isrmbrs.equals( other.isrmbrs ) )
            return false;
        if ( itemCode == null ) {
            if ( other.itemCode != null )
                return false;
        } else if ( !itemCode.equals( other.itemCode ) )
            return false;
        if ( itemId == null ) {
            if ( other.itemId != null )
                return false;
        } else if ( !itemId.equals( other.itemId ) )
            return false;
        if ( Double.doubleToLongBits( mount ) != Double.doubleToLongBits( other.mount ) )
            return false;
        if ( Double.doubleToLongBits( noTaxInvoicePrice ) != Double.doubleToLongBits( other.noTaxInvoicePrice ) )
            return false;
        if ( Double.doubleToLongBits( noTaxSumPrice ) != Double.doubleToLongBits( other.noTaxSumPrice ) )
            return false;
        if ( purchName == null ) {
            if ( other.purchName != null )
                return false;
        } else if ( !purchName.equals( other.purchName ) )
            return false;
        if ( Double.doubleToLongBits( receivedMount ) != Double.doubleToLongBits( other.receivedMount ) )
            return false;
        if ( Double.doubleToLongBits( receivedPrice ) != Double.doubleToLongBits( other.receivedPrice ) )
            return false;
        if ( Double.doubleToLongBits( taxSum ) != Double.doubleToLongBits( other.taxSum ) )
            return false;
        if ( Double.doubleToLongBits( taxUnitPrice ) != Double.doubleToLongBits( other.taxUnitPrice ) )
            return false;
        if ( type == null ) {
            if ( other.type != null )
                return false;
        } else if ( !type.equals( other.type ) )
            return false;
        if ( unit == null ) {
            if ( other.unit != null )
                return false;
        } else if ( !unit.equals( other.unit ) )
            return false;
        if ( Double.doubleToLongBits( unitPrice ) != Double.doubleToLongBits( other.unitPrice ) )
            return false;
        if ( version == null ) {
            if ( other.version != null )
                return false;
        } else if ( !version.equals( other.version ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PurInvoiceAssetVo [itemId=" + itemId + ", itemCode=" + itemCode + ", purchName=" + purchName
                + ", type=" + type + ", unit=" + unit + ", unitPrice=" + unitPrice + ", mount=" + mount
                + ", receivedMount=" + receivedMount + ", receivedPrice=" + receivedPrice + ", noTaxInvoicePrice="
                + noTaxInvoicePrice + ", taxUnitPrice=" + taxUnitPrice + ", taxSum=" + taxSum + ", noTaxSumPrice="
                + noTaxSumPrice + ", version=" + version + ", imtNo=" + imtNo + ", isReceived=" + isReceived
                + ", isrmbrs=" + isrmbrs + "]";
    }

}
