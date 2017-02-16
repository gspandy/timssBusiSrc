package com.timss.finance.vo;

import java.util.List;
import java.util.HashMap;

import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinanceManagementPay;

public class FinanceManagementPayDtlVo extends FinanceManagementPay{
	private static final long serialVersionUID = 6794223272952640227L;
	private List<FinanceMainDetail> financeMainDetails;
	private List<HashMap<String, Object>> attachMap;
	private String applyId;
	private String applyName;
	private String subject;
	private String budget;
	private String status;
	private String fmstatus;
	public List<FinanceMainDetail> getFinanceMainDetails() {
		return financeMainDetails;
	}
	public void setFinanceMainDetails(List<FinanceMainDetail> financeMainDetails) {
		this.financeMainDetails = financeMainDetails;
	}
	public List<HashMap<String, Object>> getAttachMap() {
		return attachMap;
	}
	public void setAttachMap(List<HashMap<String, Object>> attachMap) {
		this.attachMap = attachMap;
	}
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getFmstatus() {
		return fmstatus;
	}
	public void setFmstatus(String fmstatus) {
		this.fmstatus = fmstatus;
	}
}
