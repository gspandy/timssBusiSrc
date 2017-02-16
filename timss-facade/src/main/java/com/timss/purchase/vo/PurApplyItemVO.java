package com.timss.purchase.vo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyItemVO.java
 * @author: 890166
 * @createDate: 2014-6-23
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurApplyItemVO {
    private String itemid;
    private String itemname;
    private String cusmodel;
    private String orderunitname;
    private String orderunitid;
    private String itemnum;
    private String olditemnum;
    private String storenum;
    private String averprice;
    private String priceTotal; // price_total
    private String classname;
    private String claze; // class
    private String commitcommecnetwk;
    private String descriptions;

    private String istool;
    private String prestore;
    private String storedate;
    private String siteid;
    private String repliednum; // 批复数量
    private String status;
    private String remark;

    private String warehouseid;
    private String warehouse;// 仓库名称

    private Long eamPrlineId;

    private String listId;
    private String invcateid;//物资分类id
    
    private String active;//绑定物资分类的物资是否启用 Y启用 N禁用
    /**
     * @return the listId
     */
    public String getListId() {
        return listId;
    }

    /**
     * @param listId the listId to set
     */
    public void setListId(String listId) {
        this.listId = listId;
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
     * @return the eamPrlineId
     */
    public Long getEamPrlineId() {
        return eamPrlineId;
    }

    /**
     * @param eamPrlineId the eamPrlineId to set
     */
    public void setEamPrlineId(Long eamPrlineId) {
        this.eamPrlineId = eamPrlineId;
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
     * @return the repliednum
     */
    public String getRepliednum() {
        return repliednum;
    }

    /**
     * @param repliednum the repliednum to set
     */
    public void setRepliednum(String repliednum) {
        this.repliednum = repliednum;
    }

    /**
     * @return the istool
     */
    public String getIstool() {
        return istool;
    }

    /**
     * @param istool the istool to set
     */
    public void setIstool(String istool) {
        this.istool = istool;
    }

    /**
     * @return the prestore
     */
    public String getPrestore() {
        return prestore;
    }

    /**
     * @param prestore the prestore to set
     */
    public void setPrestore(String prestore) {
        this.prestore = prestore;
    }

    /**
     * @return the storedate
     */
    public String getStoredate() {
        return storedate;
    }

    /**
     * @param storedate the storedate to set
     */
    public void setStoredate(String storedate) {
        this.storedate = storedate;
    }

    /**
     * @return the siteid
     */
    public String getSiteid() {
        return siteid;
    }

    /**
     * @param siteid the siteid to set
     */
    public void setSiteid(String siteid) {
        this.siteid = siteid;
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
     * @return the orderunitname
     */
    public String getOrderunitname() {
        return orderunitname;
    }

    /**
     * @param orderunitname the orderunitname to set
     */
    public void setOrderunitname(String orderunitname) {
        this.orderunitname = orderunitname;
    }

    /**
     * @return the orderunitid
     */
    public String getOrderunitid() {
        return orderunitid;
    }

    /**
     * @param orderunitid the orderunitid to set
     */
    public void setOrderunitid(String orderunitid) {
        this.orderunitid = orderunitid;
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
     * @return the olditemnum
     */
    public String getOlditemnum() {
        return olditemnum;
    }

    /**
     * @param olditemnum the olditemnum to set
     */
    public void setOlditemnum(String olditemnum) {
        this.olditemnum = olditemnum;
    }

    /**
     * @return the storenum
     */
    public String getStorenum() {
        return storenum;
    }

    /**
     * @param storenum the storenum to set
     */
    public void setStorenum(String storenum) {
        this.storenum = storenum;
    }

    /**
     * @return the averprice
     */
    public String getAverprice() {
        return averprice;
    }

    /**
     * @param averprice the averprice to set
     */
    public void setAverprice(String averprice) {
        this.averprice = averprice;
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
     * @return the classname
     */
    public String getClassname() {
        return classname;
    }

    /**
     * @param classname the classname to set
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }

    /**
     * @return the claze
     */
    public String getClaze() {
        return claze;
    }

    /**
     * @param claze the claze to set
     */
    public void setClaze(String claze) {
        this.claze = claze;
    }

    /**
     * @return the commitcommecnetwk
     */
    public String getCommitcommecnetwk() {
        return commitcommecnetwk;
    }

    /**
     * @param commitcommecnetwk the commitcommecnetwk to set
     */
    public void setCommitcommecnetwk(String commitcommecnetwk) {
        this.commitcommecnetwk = commitcommecnetwk;
    }

    /**
     * @return the descriptions
     */
    public String getDescriptions() {
        return descriptions;
    }

    /**
     * @param descriptions the descriptions to set
     */
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getInvcateid() {
        return invcateid;
    }

    public void setInvcateid(String invcateid) {
        this.invcateid = invcateid;
    }

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
    
    
    
}
