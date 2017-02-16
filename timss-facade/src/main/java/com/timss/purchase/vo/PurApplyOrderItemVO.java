package com.timss.purchase.vo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyOrderItemVO.java
 * @author: user
 * @createDate: 2016-1-22
 * @updateUser: user
 * @version: 1.0
 */
public class PurApplyOrderItemVO {

    private String itemid; // 物资编号
    private String itemname; // 物资名称
    private String itemcus; // 型号规格
    private String orderSheetNo;// 采购合同
    private String businessno; // 商务网编码
    private String itemnum; // 采购数量
    private String cost; // 税后单价
    private String priceTotal; // 小计
    private String remark; // 备注
    private String sheetId;
    private String siteId;
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
     * @return the itemid
     */
    public String getItemid() {
        return itemid;
    }

    /**
     * @param itemid the itemid to set
     */
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    /**
     * @return the itemname
     */
    public String getItemname() {
        return itemname;
    }

    /**
     * @param itemname the itemname to set
     */
    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    /**
     * @return the itemcus
     */
    public String getItemcus() {
        return itemcus;
    }

    /**
     * @param itemcus the itemcus to set
     */
    public void setItemcus(String itemcus) {
        this.itemcus = itemcus;
    }

    /**
     * @return the orderSheetNo
     */
    public String getOrderSheetNo() {
        return orderSheetNo;
    }

    /**
     * @param orderSheetNo the orderSheetNo to set
     */
    public void setOrderSheetNo(String orderSheetNo) {
        this.orderSheetNo = orderSheetNo;
    }

    /**
     * @return the businessno
     */
    public String getBusinessno() {
        return businessno;
    }

    /**
     * @param businessno the businessno to set
     */
    public void setBusinessno(String businessno) {
        this.businessno = businessno;
    }

    /**
     * @return the itemnum
     */
    public String getItemnum() {
        return itemnum;
    }

    /**
     * @param itemnum the itemnum to set
     */
    public void setItemnum(String itemnum) {
        this.itemnum = itemnum;
    }

    /**
     * @return the cost
     */
    public String getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(String cost) {
        this.cost = cost;
    }

    /**
     * @return the priceTotal
     */
    public String getPriceTotal() {
        return priceTotal;
    }

    /**
     * @param priceTotal the priceTotal to set
     */
    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
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
