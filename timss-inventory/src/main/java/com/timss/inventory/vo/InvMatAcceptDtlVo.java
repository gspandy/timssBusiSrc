package com.timss.inventory.vo;

import java.util.List;

import com.timss.inventory.bean.InvMatAccept;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatAcceptDtlVo.java
 * @author: 890145
 * @createDate: 2015-11-2
 * @updateUser: 890145
 * @version: 1.0
 */
@SuppressWarnings("serial")
public class InvMatAcceptDtlVo extends InvMatAccept{
	
	public List<InvMatAcceptDetailVO> invMatAcceptDetails;
	
	private String sheetType;
	
	private String sheetName;
	
	public List<InvMatAcceptDetailVO> getInvMatAcceptDetails() {
		return invMatAcceptDetails;
	}

	public void setInvMatAcceptDetails(List<InvMatAcceptDetailVO> invMatAcceptDetails) {
		this.invMatAcceptDetails = invMatAcceptDetails;
	}

	public String getSheetType() {
		return sheetType;
	}

	public void setSheetType(String sheetType) {
		this.sheetType = sheetType;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	
}
