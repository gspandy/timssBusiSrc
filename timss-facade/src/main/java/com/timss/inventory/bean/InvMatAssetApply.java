package com.timss.inventory.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class InvMatAssetApply extends ItcMvcBean{
	/**
	 * @title: 资产化申请bean
	 * @company: gdyd
	 * @author: 890199
	 * @createDate: 2016-10-10
	 * @updateUser: 890199
	 * @version:1.0
	 */
	@AutoGen(value="AST_APPLY_NUM_SEQ", requireType=GenerationType.REQUIRED_NULL)
	private String flowNo;
	private String astApplyId;
	private String imadId;
	private String itemName;
	private String poId;
	private String cusmodel;
	private String companyName;
	private String companyTel;
	private Date purchaseDate;
	private String memo;
	private String status;
	private String imtdId;
	private String itemId;
	private String itemCode;
	//关联主项目名称
	private String iitemName;
	private String financialCode;
	private String logo;
	private String equipmentId;
	public String getIitemName() {
		return iitemName;
	}
	public void setIitemName(String iitemName) {
		this.iitemName = iitemName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getFinancialCode() {
		return financialCode;
	}
	public void setFinancialCode(String financialCode) {
		this.financialCode = financialCode;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getImtdId() {
		return imtdId;
	}
	public void setImtdId(String imtdId) {
		this.imtdId = imtdId;
	}
	public String getFlowNo() {
		return flowNo;
	}
	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAstApplyId() {
		return astApplyId;
	}
	public void setAstApplyId(String astApplyId) {
		this.astApplyId = astApplyId;
	}
	public String getImadId() {
		return imadId;
	}
	public void setImadId(String imadId) {
		this.imadId = imadId;
	}
	public String getPoId() {
		return poId;
	}
	public void setPoId(String poId) {
		this.poId = poId;
	}
	public String getCusmodel() {
		return cusmodel;
	}
	public void setCusmodel(String cusmodel) {
		this.cusmodel = cusmodel;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyTel() {
		return companyTel;
	}
	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
