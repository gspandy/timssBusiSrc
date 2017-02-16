package com.timss.purchase.bean;

import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: PurPolicy
 * @description: 合同条款bean
 * @company: gdyd
 * @className: PurPayDtl.java
 * @author: 890162
 * @createDate: 2016-03-17
 * @updateUser: 890162
 * @version: 1.0
 */
public class PurPayDtl extends ItcMvcBean {
    
    private static final long serialVersionUID = -2206847968651085132L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String payDtlId;//
    private String payId;
    private Double notaxTotal;
    private Double taxTotal;
    private Double sendAccount;
    private String status;
    private String imtdId;
    private String warehouseId;
    public String getPayDtlId() {
        return payDtlId;
    }
    public void setPayDtlId(String payDtlId) {
        this.payDtlId = payDtlId;
    }
    public String getPayId() {
        return payId;
    }
    public void setPayId(String payId) {
        this.payId = payId;
    }
    public Double getNotaxTotal() {
        return notaxTotal;
    }
    public void setNotaxTotal(Double notaxTotal) {
        this.notaxTotal = notaxTotal;
    }
    public Double getTaxTotal() {
        return taxTotal;
    }
    public void setTaxTotal(Double taxTotal) {
        this.taxTotal = taxTotal;
    }
    public Double getSendAccount() {
        return sendAccount;
    }
    public void setSendAccount(Double sendAccount) {
        this.sendAccount = sendAccount;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getImtdId() {
        return imtdId;
    }
    public void setImtdId(String imtdId) {
        this.imtdId = imtdId;
    }
    public String getWarehouseId() {
        return warehouseId;
    }
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
