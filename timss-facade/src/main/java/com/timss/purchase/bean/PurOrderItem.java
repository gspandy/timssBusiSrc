package com.timss.purchase.bean;

import java.math.BigDecimal;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurOrderItem.java
 * @author: 890166
 * @createDate: 2014-6-30
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurOrderItem extends InvItemBaseField{

    private static final long serialVersionUID = -7772327840511434104L;
    private String sheetId; // sheet_id
    private String applySheetId; // apply_sheet_id

    private String status;
    private String remark;

    private BigDecimal itemnum = new BigDecimal( "0" );
    private BigDecimal price = new BigDecimal( "0" );
    private BigDecimal tax = new BigDecimal( "0" );
    private BigDecimal cost = new BigDecimal( "0" );
    private BigDecimal receivenum = new BigDecimal( "0" );
    private BigDecimal taxRate = new BigDecimal( "0" ); // 税率

    /**
     * @return the taxRate
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    /**
     * @param taxRate the taxRate to set
     */
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * @return the sheetId
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * @param sheetId the sheetId to set
     */
    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    /**
     * @return the applySheetId
     */
    public String getApplySheetId() {
        return applySheetId;
    }

    /**
     * @param applySheetId the applySheetId to set
     */
    public void setApplySheetId(String applySheetId) {
        this.applySheetId = applySheetId;
    }

    /**
     * @return the itemnum
     */
    public BigDecimal getItemnum() {
        return itemnum;
    }

    /**
     * @param itemnum the itemnum to set
     */
    public void setItemnum(BigDecimal itemnum) {
        this.itemnum = itemnum;
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
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the receivenum
     */
    public BigDecimal getReceivenum() {
        return receivenum;
    }

    /**
     * @param receivenum the receivenum to set
     */
    public void setReceivenum(BigDecimal receivenum) {
        this.receivenum = receivenum;
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
    
}
