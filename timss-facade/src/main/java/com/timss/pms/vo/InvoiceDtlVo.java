package com.timss.pms.vo;

import java.util.List;

import com.timss.pms.bean.Invoice;

/**
 * 发票详细信息vo类
 * @ClassName:     InvoiceDtlVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-8-11 下午2:22:38
 */
public class InvoiceDtlVo extends Invoice{
	private String name;
	private String contractCode;
	private String ctype;
	
	private List<InvoiceConfirmVo> invoiceConfirmVos;
	
	
	public String getCtype() {
		return ctype;
	}
	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public List<InvoiceConfirmVo> getInvoiceConfirmVos() {
		return invoiceConfirmVos;
	}
	public void setInvoiceConfirmVos(List<InvoiceConfirmVo> invoiceConfirmVos) {
		this.invoiceConfirmVos = invoiceConfirmVos;
	}

	
	
}
