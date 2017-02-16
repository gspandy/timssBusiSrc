package com.timss.purchase.bean;

import java.util.Date;

import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: PurPolicy
 * @description: 合同条款bean
 * @company: gdyd
 * @className: PurPay.java
 * @author: 890162
 * @createDate: 2016-03-17
 * @updateUser: 890162
 * @version: 1.0
 */
public class PurPay extends ItcMvcBean {
    
    private static final long serialVersionUID = 7764664094750110302L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String payId;//
    private String sheetId;
    private Double pay;
    private String usage;
    private String payType;
    private Double qaPay;
    private Double refusePay;
    private String relatePayId;
    private Date qaDeadLine;
    private Date excludeDate;
    private Date auditDate;
    private String invoiceNos;
    private String payNo;
    private String transactor;
    private String status;
    private String remark;
    private String procInstId;
    private String erpStatus;
    private Date erpDate;
    public String getPayId() {
        return payId;
    }
    public void setPayId(String payId) {
        this.payId = payId;
    }
    public String getSheetId() {
        return sheetId;
    }
    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }
    public Double getPay() {
        return pay;
    }
    public void setPay(Double pay) {
        this.pay = pay;
    }
    public String getUsage() {
        return usage;
    }
    public void setUsage(String usage) {
        this.usage = usage;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public Double getQaPay() {
        return qaPay;
    }
    public void setQaPay(Double qaPay) {
        this.qaPay = qaPay;
    }
    public Double getRefusePay() {
        return refusePay;
    }
    public void setRefusePay(Double refusePay) {
        this.refusePay = refusePay;
    }
    public String getRelatePayId() {
        return relatePayId;
    }
    public void setRelatePayId(String relatePayId) {
        this.relatePayId = relatePayId;
    }
    public Date getQaDeadLine() {
        return qaDeadLine;
    }
    public void setQaDeadLine(Date qaDeadLine) {
        this.qaDeadLine = qaDeadLine;
    }
    public Date getExcludeDate() {
        return excludeDate;
    }
    public void setExcludeDate(Date excludeDate) {
        this.excludeDate = excludeDate;
    }
    public Date getAuditDate() {
        return auditDate;
    }
    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }
    public String getInvoiceNos() {
        return invoiceNos;
    }
    public void setInvoiceNos(String invoiceNos) {
        this.invoiceNos = invoiceNos;
    }
    public String getPayNo() {
        return payNo;
    }
    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }
    public String getTransactor() {
        return transactor;
    }
    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getProcInstId() {
        return procInstId;
    }
    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }
	public String getErpStatus() {
		return erpStatus;
	}
	public void setErpStatus(String erpStatus) {
		this.erpStatus = erpStatus;
	}
	public Date getErpDate() {
		return erpDate;
	}
	public void setErpDate(Date erpDate) {
		this.erpDate = erpDate;
	}
    
}
