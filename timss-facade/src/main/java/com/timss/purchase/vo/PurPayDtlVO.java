package com.timss.purchase.vo;

import com.timss.purchase.bean.PurPayDtl;

/**
 * @title: PurPayVO
 * @description: 采购合同付款VO
 * @company: gdyd
 * @className: PurPayDtlVO.java
 * @author: 890162
 * @createDate: 2016-03-17
 * @updateUser: 890162
 * @version: 1.0
 */
public class PurPayDtlVO extends PurPayDtl{    
    private static final long serialVersionUID = -8638935122108854593L;
    private String itemId;   // 物资id
    private String itemCode; // 物资编号
    private String itemName; // 物资名称
    private String customModel; // 型号规格
    private String unit; // 单位
    private Double applyNum; // 采购量
    private Double invNum; // 已入库量
    private Double noSendAccount; // 未报账
    private Double noTaxPrice; // 不含税单价
    private Double taxPrice; // 含税单价
    private String invcateid;//物资分类id
    private String invcateName; //物资分类名称
    
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getCustomModel() {
        return customModel;
    }
    public void setCustomModel(String customModel) {
        this.customModel = customModel;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Double getApplyNum() {
        return applyNum;
    }
    public void setApplyNum(Double applyNum) {
        this.applyNum = applyNum;
    }
    public Double getInvNum() {
        return invNum;
    }
    public void setInvNum(Double invNum) {
        this.invNum = invNum;
    }
    public Double getNoSendAccount() {
        return noSendAccount;
    }
    public void setNoSendAccount(Double noSendAccount) {
        this.noSendAccount = noSendAccount;
    }
    public Double getNoTaxPrice() {
        return noTaxPrice;
    }
    public void setNoTaxPrice(Double noTaxPrice) {
        this.noTaxPrice = noTaxPrice;
    }
    public Double getTaxPrice() {
        return taxPrice;
    }
    public void setTaxPrice(Double taxPrice) {
        this.taxPrice = taxPrice;
    }
	public String getInvcateid() {
		return invcateid;
	}
	public void setInvcateid(String invcateid) {
		this.invcateid = invcateid;
	}
	public String getInvcateName() {
		return invcateName;
	}
	public void setInvcateName(String invcateName) {
		this.invcateName = invcateName;
	}

    
}
