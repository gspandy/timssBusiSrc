package com.timss.purchase.vo;

import com.timss.purchase.bean.PurPay;

/**
 * @title: PurPayVO
 * @description: 采购合同付款VO
 * @company: gdyd
 * @className: PurPayVO.java
 * @author: 890162
 * @createDate: 2016-03-17
 * @updateUser: 890162
 * @version: 1.0
 */
public class PurPayVO extends PurPay{    
    private static final long serialVersionUID = 7527906297656864004L;
    private String sheetNo; // 合同编号(在生物质是流水号)
    private String sheetName; // 合同名称
    private Double sheetTotal; // 合同金额
    private Double payRatio; // 付款比例
    private String supplierName;// 供应商名称
    private Double noTaxTotal ; // 不含税金额
    private Double taxTotal; // 税额
    private Double total; //含税金额
    private Double actualPayTotal; //含税金额
    private String relatePayNo;// 关联付款单单号(质保金付款关联到货款)
    private String sheetClassId;//流程--关联采购合同的采购类型
    private String itemClassid; //流程--关联采购申请物资类型
    private String createrName;
    private String transactorName;//待办人
    private String taskId;        //流程任务ID
    private String supplierCode;//供应商编码
    private String spNo;//生物质-合同编号
    public String getSheetNo() {
        return sheetNo;
    }
    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }
    public String getSheetName() {
        return sheetName;
    }
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
    public Double getSheetTotal() {
        return sheetTotal;
    }
    public void setSheetTotal(Double sheetTotal) {
        this.sheetTotal = sheetTotal;
    }
    public Double getPayRatio() {
        return payRatio;
    }
    public void setPayRatio(Double payRatio) {
        this.payRatio = payRatio;
    }
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    public Double getNoTaxTotal() {
        return noTaxTotal;
    }
    public void setNoTaxTotal(Double noTaxTotal) {
        this.noTaxTotal = noTaxTotal;
    }
    public Double getTaxTotal() {
        return taxTotal;
    }
    public void setTaxTotal(Double taxTotal) {
        this.taxTotal = taxTotal;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }
    public Double getActualPayTotal() {
        return actualPayTotal;
    }
    public void setActualPayTotal(Double actualPayTotal) {
        this.actualPayTotal = actualPayTotal;
    }
    public String getRelatePayNo() {
        return relatePayNo;
    }
    public void setRelatePayNo(String relatePayNo) {
        this.relatePayNo = relatePayNo;
    }
    public String getSheetClassId() {
        return sheetClassId;
    }
    public void setSheetClassId(String sheetClassId) {
        this.sheetClassId = sheetClassId;
    }
    public String getItemClassid() {
        return itemClassid;
    }
    public void setItemClassid(String itemClassid) {
        this.itemClassid = itemClassid;
    }
    public String getCreaterName() {
        return createrName;
    }
    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }
    public String getTransactorName() {
        return transactorName;
    }
    public void setTransactorName(String transactorName) {
        this.transactorName = transactorName;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getSupplierCode() {
        return supplierCode;
    }
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }
    public String getSpNo() {
        return spNo;
    }
    public void setSpNo(String spNo) {
        this.spNo = spNo;
    }
}
