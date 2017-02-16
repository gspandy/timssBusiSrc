package com.timss.asset.vo;

import java.util.Date;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.bean.AstBorrowRecordBean;

public class AssetVo extends AssetBean{
	/**
	 * @title: 
	 * @company: gdyd
	 * @author: 890199
	 * @createDate: 2016-12-01
	 * @updateUser: 890199
	 * @version:1.0
	 */
	private Date assetDate;
	private String itemName;
	private String imtId;
	private String sheetNo;
	private String poId;
	private String itemCode;
	private String poNo;
	private String sheetName;
	private String createName;
	private String itemId;
	private String cusmodel;
	private String attr1;
	
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getCusmodel() {
		return cusmodel;
	}

	public void setCusmodel(String cusmodel) {
		this.cusmodel = cusmodel;
	}

	public String getAttr1() {
		return attr1;
	}

	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
	}

	public String getImtId() {
		return imtId;
	}

	public void setImtId(String imtId) {
		this.imtId = imtId;
	}

	public String getSheetNo() {
		return sheetNo;
	}

	public void setSheetNo(String sheetNo) {
		this.sheetNo = sheetNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Date getAssetDate() {
		return assetDate;
	}

	public void setAssetDate(Date assetDate) {
		this.assetDate = assetDate;
	} 
}
