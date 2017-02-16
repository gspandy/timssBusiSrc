package com.timss.pms.vo;

import com.timss.pms.bean.Invoice;

/**
 * 发票列表vo类
 * @ClassName:     InvoiceVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-8-11 下午2:22:57
 */
public class InvoiceVo extends Invoice{
	private String name;
	private String contractCode;
	private String ctype;
	private String firstParty;
	
	
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
	public String getFirstParty() {
		return firstParty;
	}
	public void setFirstParty(String firstParty) {
		this.firstParty = firstParty;
	}
	
}
