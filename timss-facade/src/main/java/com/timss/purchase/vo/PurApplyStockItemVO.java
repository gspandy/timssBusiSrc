package com.timss.purchase.vo;

import java.util.Date;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyStockItemVO.java
 * @author: user
 * @createDate: 2016-1-22
 * @updateUser: user
 * @version: 1.0
 */
public class PurApplyStockItemVO {

    private String itemid; // 物资id
    private String itemcode;// 物资编号
    private String itemname;// 物资名称
    private String cusmodel;// 型号规格
    private String imtid;// 接收id
    private String sheetno;// 入库单号
    private Date createdate;// 入库日期
    private String warehousename;// 仓库
    private String warehouseid;
    private String binname;// 货柜

    private String sheetId;
    private String siteId;
    //新增补字段--供执行情况列表用
    //批复数量
    private String repliednum;
    //采购数量
    private String itemnum;
    //预算价格
    private String applyPrice;
    //税后价格
    private String orderPrice;
    //已入库
    private String invNum;
    //是否经商务网
    private String isToBusiness;
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
     * @return the itemcode
     */
    public String getItemcode() {
        return itemcode;
    }

    /**
     * @param itemcode the itemcode to set
     */
    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
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
     * @return the cusmodel
     */
    public String getCusmodel() {
        return cusmodel;
    }

    /**
     * @param cusmodel the cusmodel to set
     */
    public void setCusmodel(String cusmodel) {
        this.cusmodel = cusmodel;
    }

    /**
     * @return the imtid
     */
    public String getImtid() {
        return imtid;
    }

    /**
     * @param imtid the imtid to set
     */
    public void setImtid(String imtid) {
        this.imtid = imtid;
    }

    /**
     * @return the sheetno
     */
    public String getSheetno() {
        return sheetno;
    }

    /**
     * @param sheetno the sheetno to set
     */
    public void setSheetno(String sheetno) {
        this.sheetno = sheetno;
    }

    /**
     * @return the createdate
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * @param createdate the createdate to set
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
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

    public String getRepliednum() {
        return repliednum;
    }

    public void setRepliednum(String repliednum) {
        this.repliednum = repliednum;
    }

    public String getItemnum() {
        return itemnum;
    }

    public void setItemnum(String itemnum) {
        this.itemnum = itemnum;
    }

    public String getApplyPrice() {
        return applyPrice;
    }

    public void setApplyPrice(String applyPrice) {
        this.applyPrice = applyPrice;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getInvNum() {
        return invNum;
    }

    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }

    public String getIsToBusiness() {
        return isToBusiness;
    }

    public void setIsToBusiness(String isToBusiness) {
        this.isToBusiness = isToBusiness;
    }
    
}
