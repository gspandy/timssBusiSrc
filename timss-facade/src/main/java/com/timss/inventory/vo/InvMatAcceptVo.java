package com.timss.inventory.vo;

import com.timss.inventory.bean.InvMatAccept;

public class InvMatAcceptVo extends InvMatAccept{
    private String applyUser;
	private String applySheetNo;
	
	private String deliveryDataString;
	
	private String _status; // 只是用于接收时使用
	private String _acptCnlus;// 只是用于接收时使用
	
	private String itemCode; //物资编号 用于搜索框搜索
    private String  itemName;//物资名称 用于搜索框搜索
	
	public String getApplyUser() {
		return applyUser;
	}
	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}
	public String getApplySheetNo() {
		return applySheetNo;
	}
	public void setApplySheetNo(String applySheetNo) {
		this.applySheetNo = applySheetNo;
	}
	
	public String getDeliveryDataString() {
		return deliveryDataString;
	}
	public void setDeliveryDataString(String deliveryDataString) {
		this.deliveryDataString = deliveryDataString;
	}
	public String get_status() {
		return _status;
	}
	public void set_status(String _status) {
		this._status = _status;
	}
	public String get_acptCnlus() {
		return _acptCnlus;
	}
	public void set_acptCnlus(String _acptCnlus) {
		this._acptCnlus = _acptCnlus;
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
	
	
}
