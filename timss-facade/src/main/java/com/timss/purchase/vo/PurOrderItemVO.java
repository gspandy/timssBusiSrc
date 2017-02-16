package com.timss.purchase.vo;

import java.math.BigDecimal;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurOrderItemVO.java
 * @author: 890166
 * @createDate: 2014-6-30
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurOrderItemVO {

    private String sheetId; // sheet_id
    private String applySheetId; // apply_sheet_id
    private String sheetno;
    private String itemid;
    private String itemname;
    private String itemcus;
    private String orderunitname;
    private String orderunitid;
    private String itemnum;
    private String averprice;
    private String tax;
    private String cost;
    private BigDecimal tempCost = new BigDecimal( "0" );
    private String priceTotal; // price_total
    private String combo;
    private String remark = new String( "" );
    private String sheetName; // sheet_name
    private String createUserName;// create_user_name
    private String createaccount;
    private String siteid;
    private String receivestatus;// 接收状态

    private BigDecimal taxRate = new BigDecimal( "0" ); // 税率

    private String applyDept;// 采购申请人部门

    private String projectAscription;// 项目

    private String tmpid;// 临时id

    private String warehouseid;

    private String warehouse; // 仓库名称

    private String listId;
    
    private String itemCode;  //物资编号
    
    private BigDecimal inSum ; //已入库数量
    
    private BigDecimal payedSum ;   //已报账数量
    private String invcateid;//物资分类id
    /**
     * @return the payedSum
     */
    public BigDecimal getPayedSum() {
        return payedSum;
    }

    /**
     * @param payedSum the payedSum to set
     */
    public void setPayedSum(BigDecimal payedSum) {
        this.payedSum = payedSum;
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
     * @return the inSum
     */
    public BigDecimal getInSum() {
        return inSum;
    }

    /**
     * @param inSum the inSum to set
     */
    public void setInSum(BigDecimal inSum) {
        this.inSum = inSum;
    }

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
     * @return the projectAscription
     */
    public String getProjectAscription() {
        return projectAscription;
    }

    /**
     * @param projectAscription the projectAscription to set
     */
    public void setProjectAscription(String projectAscription) {
        this.projectAscription = projectAscription;
    }

    /**
     * @return the tmpid
     */
    public String getTmpid() {
        return tmpid;
    }

    /**
     * @param tmpid the tmpid to set
     */
    public void setTmpid(String tmpid) {
        this.tmpid = tmpid;
    }

    /**
     * @return the applyDept
     */
    public String getApplyDept() {
        return applyDept;
    }

    /**
     * @param applyDept the applyDept to set
     */
    public void setApplyDept(String applyDept) {
        this.applyDept = applyDept;
    }

    /**
     * @return the receivestatus
     */
    public String getReceivestatus() {
        return receivestatus;
    }

    /**
     * @param receivestatus the receivestatus to set
     */
    public void setReceivestatus(String receivestatus) {
        this.receivestatus = receivestatus;
    }

    /**
     * @return the tempCost
     */
    public BigDecimal getTempCost() {
        return tempCost;
    }

    /**
     * @param tempCost the tempCost to set
     */
    public void setTempCost(BigDecimal tempCost) {
        this.tempCost = tempCost;
    }

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
     * @return the sheetName
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * @param sheetName the sheetName to set
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * @return the createUserName
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * @param createUserName the createUserName to set
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**
     * @return the createaccount
     */
    public String getCreateaccount() {
        return createaccount;
    }

    /**
     * @param createaccount the createaccount to set
     */
    public void setCreateaccount(String createaccount) {
        this.createaccount = createaccount;
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
     * @return the tax
     */
    public String getTax() {
        return tax;
    }

    /**
     * @param tax the tax to set
     */
    public void setTax(String tax) {
        this.tax = tax;
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
     * @return the combo
     */
    public String getCombo() {
        return combo;
    }

    /**
     * @param combo the combo to set
     */
    public void setCombo(String combo) {
        this.combo = combo;
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

    public String getInvcateid() {
        return invcateid;
    }

    public void setInvcateid(String invcateid) {
        this.invcateid = invcateid;
    }
    
}
