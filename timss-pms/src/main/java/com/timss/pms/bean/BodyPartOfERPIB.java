package com.timss.pms.bean;

public class BodyPartOfERPIB {
	private String subject;//科目
	private String subjectremark;//科目描述
	private String debitamt;//借方金额
	private String creditamt;//贷方金额
	private String cashitem;//现金流项目
	private String intervalunit;//内部单位
	private String certrowdesc;//凭证行描述
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubjectremark() {
		return subjectremark;
	}
	public void setSubjectremark(String subjectremark) {
		this.subjectremark = subjectremark;
	}
	public String getDebitamt() {
		return debitamt;
	}
	public void setDebitamt(String debitamt) {
		this.debitamt = debitamt;
	}
	public String getCreditamt() {
		return creditamt;
	}
	public void setCreditamt(String creditamt) {
		this.creditamt = creditamt;
	}
	public String getCashitem() {
		return cashitem;
	}
	public void setCashitem(String cashitem) {
		this.cashitem = cashitem;
	}
	public String getIntervalunit() {
		return intervalunit;
	}
	public void setIntervalunit(String intervalunit) {
		this.intervalunit = intervalunit;
	}
	public String getCertrowdesc() {
		return certrowdesc;
	}
	public void setCertrowdesc(String certrowdesc) {
		this.certrowdesc = certrowdesc;
	}
	@Override
	public String toString() {
		return "BodyPartOfERPIB [subject=" + subject + ", subjectremark="
				+ subjectremark + ", debitamt=" + debitamt + ", creditamt="
				+ creditamt + ", cashitem=" + cashitem + ", intervalunit="
				+ intervalunit + ", certrowdesc=" + certrowdesc + "]";
	}
	
	
}
