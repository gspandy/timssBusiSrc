package com.timss.purchase.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 发票主表bean
 * @description: {desc}
 * @company: gdyd
 * @className: PurInvoiceBean.java
 * @author: fengzt
 * @createDate: 2015年9月18日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PurInvoiceBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 7391657909563111925L;

    /**
     * ID
     */
    private String id;
    
    /**
     * 业务流水号
     */
    @AutoGen("PUR_INVOICE_SEQ")
    private String sheetNo;
    
    /**
     * 发票号
     */
    private String invoiceNo;
    
    /**
     * 开票日期
     */
    private Date invoiceCreateDate;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 报账日期
     */
    private Date reimDate;
    
    /**
     * 合同ID
     */
    private String contractId;
    
    /**
     * 合同流水号
     */
    private String contractNo;
    
    /**
     * 供应商Id
     */
    private String supplierId;
    
    /**
     * 供应商
     */
    private String supplier;
    
    /**
     * 商务网编号
     */
    private String businessNo;
    
    /**
     * 税率
     */
    private double tax;
    
    /**
     * 发票有效天数
     */
    private int effectiveDate;
    
    /**
     * 不含税金额
     */
    private double noTaxSumPrice;
    
    /**
     * 到期日期
     */
    private Date endDate;
    
    /**
     * 是否删除
     */
    private String isDelete;
    
    /**
     * 创建人名称
     */
    private String createUserName;
    
    /**
     * 最近更新人名称
     */
    private String modifyUserName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Date getInvoiceCreateDate() {
        return invoiceCreateDate;
    }

    public void setInvoiceCreateDate(Date invoiceCreateDate) {
        this.invoiceCreateDate = invoiceCreateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getReimDate() {
        return reimDate;
    }

    public void setReimDate(Date reimDate) {
        this.reimDate = reimDate;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public int getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(int effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public double getNoTaxSumPrice() {
        return noTaxSumPrice;
    }

    public void setNoTaxSumPrice(double noTaxSumPrice) {
        this.noTaxSumPrice = noTaxSumPrice;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }
    
    
}
