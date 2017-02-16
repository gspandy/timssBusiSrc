package com.timss.asset.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

public class AstBorrowRecordBean extends ItcMvcBean{
	int borrowRecordId;
	String assetId;
	String borrowUserId;
	String borrowUserDeptId;
	String returnUserDeptId;
	Date borrowDate;
	String returnUserId;
	Date returnDate;
	String memo;
	String imrdid;
	public int getBorrowRecordId() {
		return borrowRecordId;
	}
	public void setBorrowRecordId(int borrowRecordId) {
		this.borrowRecordId = borrowRecordId;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getBorrowUserId() {
		return borrowUserId;
	}
	public void setBorrowUserId(String borrowUserId) {
		this.borrowUserId = borrowUserId;
	}
	public Date getBorrowDate() {
		return borrowDate;
	}
	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}
	public String getReturnUserId() {
		return returnUserId;
	}
	public void setReturnUserId(String returnUserId) {
		this.returnUserId = returnUserId;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getBorrowUserDeptId() {
		return borrowUserDeptId;
	}
	public void setBorrowUserDeptId(String borrowUserDeptId) {
		this.borrowUserDeptId = borrowUserDeptId;
	}
	public String getReturnUserDeptId() {
		return returnUserDeptId;
	}
	public void setReturnUserDeptId(String returnUserDeptId) {
		this.returnUserDeptId = returnUserDeptId;
	}
	public String getImrdid() {
		return imrdid;
	}
	public void setImrdid(String imrdid) {
		this.imrdid = imrdid;
	}
	
}
