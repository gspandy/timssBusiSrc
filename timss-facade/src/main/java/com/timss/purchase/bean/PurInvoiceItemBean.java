package com.timss.purchase.bean;

import com.yudean.mvc.bean.ItcMvcBean;
/**
 * 
 * @title: 发票子项--明细
 * @description: {desc}
 * @company: gdyd
 * @className: PurInvoiceItem.java
 * @author: fengzt
 * @createDate: 2015年9月18日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PurInvoiceItemBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 2051272727858526708L;

    /**
     * id
     */
    private String id;
    
    /**
     * 发票Id
     */
    private String invoiceId;
    
    /**
     * 物资流水号
     */
    private String imtNo;
    
    /**
     * 物资Id
     */
    private String itemId;
    
    /**
     * 不含税开票金额
     */
    private double noTaxSumPrice;
    /**
     * 税额
     */
    private double tax;
    
    

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }



    public double getNoTaxSumPrice() {
        return noTaxSumPrice;
    }

    public void setNoTaxSumPrice(double noTaxSumPrice) {
        this.noTaxSumPrice = noTaxSumPrice;
    }

    
    public String getImtNo() {
        return imtNo;
    }

    public void setImtNo(String imtNo) {
        this.imtNo = imtNo;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((imtNo == null) ? 0 : imtNo.hashCode());
        result = prime * result + ((invoiceId == null) ? 0 : invoiceId.hashCode());
        result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
        long temp;
        temp = Double.doubleToLongBits( noTaxSumPrice );
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        PurInvoiceItemBean other = (PurInvoiceItemBean) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        if ( imtNo == null ) {
            if ( other.imtNo != null )
                return false;
        } else if ( !imtNo.equals( other.imtNo ) )
            return false;
        if ( invoiceId == null ) {
            if ( other.invoiceId != null )
                return false;
        } else if ( !invoiceId.equals( other.invoiceId ) )
            return false;
        if ( itemId == null ) {
            if ( other.itemId != null )
                return false;
        } else if ( !itemId.equals( other.itemId ) )
            return false;
        if ( Double.doubleToLongBits( noTaxSumPrice ) != Double.doubleToLongBits( other.noTaxSumPrice ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PurInvoiceItemBean [id=" + id + ", invoiceId=" + invoiceId + ", imtNo=" + imtNo + ", itemId=" + itemId
                + ", noTaxSumPrice=" + noTaxSumPrice + ", tax=" + tax + "]";
    }
    
    
}
