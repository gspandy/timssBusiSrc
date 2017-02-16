package com.timss.purchase.vo;

import java.math.BigDecimal;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurBuisCalaVO.java
 * @author: user
 * @createDate: 2016-1-20
 * @updateUser: user
 * @version: 1.0
 */
public class PurBuisCalaVO {

    private BigDecimal paItemnum;// 采购申请数量
    private BigDecimal poItemnum;// 采购合同数量
    private String itemId;
    private String sheetId;
    private String warehouseid;
    private String invcateid;
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
     * @return the paItemnum
     */
    public BigDecimal getPaItemnum() {
        return paItemnum;
    }

    /**
     * @param paItemnum the paItemnum to set
     */
    public void setPaItemnum(BigDecimal paItemnum) {
        this.paItemnum = paItemnum;
    }

    /**
     * @return the poItemnum
     */
    public BigDecimal getPoItemnum() {
        return poItemnum;
    }

    /**
     * @param poItemnum the poItemnum to set
     */
    public void setPoItemnum(BigDecimal poItemnum) {
        this.poItemnum = poItemnum;
    }

    public String getInvcateid() {
        return invcateid;
    }

    public void setInvcateid(String invcateid) {
        this.invcateid = invcateid;
    }
    
    

}
