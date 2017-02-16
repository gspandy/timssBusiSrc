package com.timss.purchase.vo;

import java.math.BigDecimal;


/**
 * @title: PurPayStatVO
 * @description: 采购合同付款统计VO
 * @company: gdyd
 * @className: PurPayStatVO.java
 * @author: 890162
 * @createDate: 2017-01-20
 * @updateUser: 890162
 * @version: 1.0
 */
public class PurPayStatVO {    
    
    private String warehouseId; // 仓库Id
    private String invcateId; // 物资分类Id
    private String month; // 审批年月
    private String siteId; // 站点
    private BigDecimal reimbursedAmount; // 含税报账总额
    private BigDecimal reimbursedAmountNoTax; // 不含税报账总额
    public String getWarehouseId() {
        return warehouseId;
    }
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
    public String getInvcateId() {
        return invcateId;
    }
    public void setInvcateId(String invcateId) {
        this.invcateId = invcateId;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public BigDecimal getReimbursedAmount() {
        return reimbursedAmount;
    }
    public void setReimbursedAmount(BigDecimal reimbursedAmount) {
        this.reimbursedAmount = reimbursedAmount;
    }
    public BigDecimal getReimbursedAmountNoTax() {
        return reimbursedAmountNoTax;
    }
    public void setReimbursedAmountNoTax(BigDecimal reimbursedAmountNoTax) {
        this.reimbursedAmountNoTax = reimbursedAmountNoTax;
    }
}
