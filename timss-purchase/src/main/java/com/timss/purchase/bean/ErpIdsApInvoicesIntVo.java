package com.timss.purchase.bean;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author 890160
 * 对应ERP应付 主表
 */
public class ErpIdsApInvoicesIntVo implements Serializable {
	private BigDecimal controlId;
	private String compCode;
	private String invType;
	private String invNum;
	private String invoiceDate;
	private String accountingDate;
	private String vendorCode;
	private String vendorName;
	private String itemType;
	private BigDecimal invoiceAmount;
	private String invoiceDesc;
	private BigDecimal invoiceFmoney;
	private BigDecimal invoiceVat;
	private String contractNum;
	private String projectCode;
	private BigDecimal inspectFuel;
	private BigDecimal inspectVendor;
	private BigDecimal coalAmount;
	private BigDecimal coalPrice;
	private String sourceDocType;
	private String sourceDocNum;
	private String inspectVendorCode;
	private String inspectVendorName;
	private String voyageNum;
	private String budgetCode;
	private String creationDate;
	private BigDecimal createdBy;
	private String lastUpdateDate;
	private BigDecimal lastUpdatedBy;
	private String attribute1;
	private String attribute2;
	private String attribute3;
	private String attribute4;
	private String attribute5;
	private String attribute6;
	private String attribute7;
	private String attribute8;
	private String attribute9;
	private String attribute10;
	private String currencyCode;
	private BigDecimal conversionRate;
	private String shipCode;
	private String vendorSiteCode;
	
	private String erpPaidDate;
	private BigDecimal erpPaidAmount;
	private BigDecimal erpPrepayApp;
	private String erpWritebackStatus;
	public BigDecimal getControlId() {
		return controlId;
	}
	public void setControlId(BigDecimal controlId) {
		this.controlId = controlId;
	}
	public String getCompCode() {
		return compCode;
	}
	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}
	public String getInvType() {
		return invType;
	}
	public void setInvType(String invType) {
		this.invType = invType;
	}
	public String getInvNum() {
		return invNum;
	}
	public void setInvNum(String invNum) {
		this.invNum = invNum;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getAccountingDate() {
		return accountingDate;
	}
	public void setAccountingDate(String accountingDate) {
		this.accountingDate = accountingDate;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getInvoiceDesc() {
		return invoiceDesc;
	}
	public void setInvoiceDesc(String invoiceDesc) {
		this.invoiceDesc = invoiceDesc;
	}
	public BigDecimal getInvoiceFmoney() {
		return invoiceFmoney;
	}
	public void setInvoiceFmoney(BigDecimal invoiceFmoney) {
		this.invoiceFmoney = invoiceFmoney;
	}
	public BigDecimal getInvoiceVat() {
		return invoiceVat;
	}
	public void setInvoiceVat(BigDecimal invoiceVat) {
		this.invoiceVat = invoiceVat;
	}
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public BigDecimal getInspectFuel() {
		return inspectFuel;
	}
	public void setInspectFuel(BigDecimal inspectFuel) {
		this.inspectFuel = inspectFuel;
	}
	public BigDecimal getInspectVendor() {
		return inspectVendor;
	}
	public void setInspectVendor(BigDecimal inspectVendor) {
		this.inspectVendor = inspectVendor;
	}
	public BigDecimal getCoalAmount() {
		return coalAmount;
	}
	public void setCoalAmount(BigDecimal coalAmount) {
		this.coalAmount = coalAmount;
	}
	public BigDecimal getCoalPrice() {
		return coalPrice;
	}
	public void setCoalPrice(BigDecimal coalPrice) {
		this.coalPrice = coalPrice;
	}
	public String getSourceDocType() {
		return sourceDocType;
	}
	public void setSourceDocType(String sourceDocType) {
		this.sourceDocType = sourceDocType;
	}
	public String getSourceDocNum() {
		return sourceDocNum;
	}
	public void setSourceDocNum(String sourceDocNum) {
		this.sourceDocNum = sourceDocNum;
	}
	public String getInspectVendorCode() {
		return inspectVendorCode;
	}
	public void setInspectVendorCode(String inspectVendorCode) {
		this.inspectVendorCode = inspectVendorCode;
	}
	public String getInspectVendorName() {
		return inspectVendorName;
	}
	public void setInspectVendorName(String inspectVendorName) {
		this.inspectVendorName = inspectVendorName;
	}
	public String getVoyageNum() {
		return voyageNum;
	}
	public void setVoyageNum(String voyageNum) {
		this.voyageNum = voyageNum;
	}
	public String getBudgetCode() {
		return budgetCode;
	}
	public void setBudgetCode(String budgetCode) {
		this.budgetCode = budgetCode;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public BigDecimal getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(BigDecimal createdBy) {
		this.createdBy = createdBy;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public BigDecimal getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(BigDecimal lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getAttribute1() {
		return attribute1;
	}
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	public String getAttribute2() {
		return attribute2;
	}
	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	public String getAttribute3() {
		return attribute3;
	}
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	public String getAttribute4() {
		return attribute4;
	}
	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
	public String getAttribute5() {
		return attribute5;
	}
	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}
	public String getAttribute6() {
		return attribute6;
	}
	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}
	public String getAttribute7() {
		return attribute7;
	}
	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}
	public String getAttribute8() {
		return attribute8;
	}
	public void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}
	public String getAttribute9() {
		return attribute9;
	}
	public void setAttribute9(String attribute9) {
		this.attribute9 = attribute9;
	}
	public String getAttribute10() {
		return attribute10;
	}
	public void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public BigDecimal getConversionRate() {
		return conversionRate;
	}
	public void setConversionRate(BigDecimal conversionRate) {
		this.conversionRate = conversionRate;
	}
	public String getShipCode() {
		return shipCode;
	}
	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}
	public String getVendorSiteCode() {
		return vendorSiteCode;
	}
	public void setVendorSiteCode(String vendorSiteCode) {
		this.vendorSiteCode = vendorSiteCode;
	}
	public String getErpPaidDate() {
		return erpPaidDate;
	}
	public void setErpPaidDate(String erpPaidDate) {
		this.erpPaidDate = erpPaidDate;
	}
	public BigDecimal getErpPaidAmount() {
		return erpPaidAmount;
	}
	public void setErpPaidAmount(BigDecimal erpPaidAmount) {
		this.erpPaidAmount = erpPaidAmount;
	}
	public BigDecimal getErpPrepayApp() {
		return erpPrepayApp;
	}
	public void setErpPrepayApp(BigDecimal erpPrepayApp) {
		this.erpPrepayApp = erpPrepayApp;
	}
	public String getErpWritebackStatus() {
		return erpWritebackStatus;
	}
	public void setErpWritebackStatus(String erpWritebackStatus) {
		this.erpWritebackStatus = erpWritebackStatus;
	}
	@Override
	public String toString() {
		return "ErpIdsApInvoicesIntVo [controlId=" + controlId + ", compCode="
				+ compCode + ", invType=" + invType + ", invNum=" + invNum
				+ ", invoiceDate=" + invoiceDate + ", accountingDate="
				+ accountingDate + ", vendorCode=" + vendorCode
				+ ", vendorName=" + vendorName + ", itemType=" + itemType
				+ ", invoiceAmount=" + invoiceAmount + ", invoiceDesc="
				+ invoiceDesc + ", invoiceFmoney=" + invoiceFmoney
				+ ", invoiceVat=" + invoiceVat + ", contractNum=" + contractNum
				+ ", projectCode=" + projectCode + ", inspectFuel="
				+ inspectFuel + ", inspectVendor=" + inspectVendor
				+ ", coalAmount=" + coalAmount + ", coalPrice=" + coalPrice
				+ ", sourceDocType=" + sourceDocType + ", sourceDocNum="
				+ sourceDocNum + ", inspectVendorCode=" + inspectVendorCode
				+ ", inspectVendorName=" + inspectVendorName + ", voyageNum="
				+ voyageNum + ", budgetCode=" + budgetCode + ", creationDate="
				+ creationDate + ", createdBy=" + createdBy
				+ ", lastUpdateDate=" + lastUpdateDate + ", lastUpdatedBy="
				+ lastUpdatedBy + ", attribute1=" + attribute1
				+ ", attribute2=" + attribute2 + ", attribute3=" + attribute3
				+ ", attribute4=" + attribute4 + ", attribute5=" + attribute5
				+ ", attribute6=" + attribute6 + ", attribute7=" + attribute7
				+ ", attribute8=" + attribute8 + ", attribute9=" + attribute9
				+ ", attribute10=" + attribute10 + ", currencyCode="
				+ currencyCode + ", conversionRate=" + conversionRate
				+ ", shipCode=" + shipCode + ", vendorSiteCode="
				+ vendorSiteCode + ", erpPaidDate=" + erpPaidDate
				+ ", erpPaidAmount=" + erpPaidAmount + ", erpPrepayApp="
				+ erpPrepayApp + ", erpWritebackStatus=" + erpWritebackStatus
				+ "]";
	}
	
}
