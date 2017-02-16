package com.timss.pms.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.timss.pms.bean.Pay;

public class PayDtlVo extends Pay{
	private String projectName;
	private String contractName;
	private Double totalSum;
	private String xmhzf;
	private String contractType;
	private String contractCode;
	
	private String bepayPercent;
	private String actualpayPercent;
	
	private List<PayplanVo> payplanVos;
	
	private List<InvoiceVo> invoices;
	
	private  ArrayList<HashMap<String,Object>> attachMap;
	
	//付款阶段名称
	private String payStage;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Double getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(Double totalSum) {
		this.totalSum = totalSum;
	}

	public String getXmhzf() {
		return xmhzf;
	}

	public void setXmhzf(String xmhzf) {
		this.xmhzf = xmhzf;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public List<PayplanVo> getPayplanVos() {
		return payplanVos;
	}

	public void setPayplanVos(List<PayplanVo> payplanVos) {
		this.payplanVos = payplanVos;
	}

	public ArrayList<HashMap<String, Object>> getAttachMap() {
		return attachMap;
	}

	public void setAttachMap(ArrayList<HashMap<String, Object>> attachMap) {
		this.attachMap = attachMap;
	}

	public String getBepayPercent() {
		return bepayPercent;
	}

	public void setBepayPercent(String bepayPercent) {
		this.bepayPercent = bepayPercent;
	}

	public String getActualpayPercent() {
		return actualpayPercent;
	}

	public void setActualpayPercent(String actualpayPercent) {
		this.actualpayPercent = actualpayPercent;
	}

	public List<InvoiceVo> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<InvoiceVo> invoices) {
		this.invoices = invoices;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getPayStage() {
		return payStage;
	}

	public void setPayStage(String payStage) {
		this.payStage = payStage;
	}
	
	
	
}
