package com.timss.asset.vo;

import com.timss.asset.bean.AstBorrowRecordBean;

public class AstBorrowRecordVo extends AstBorrowRecordBean {
	private String borrowUserName;
	private String borrowUserDeptName;
	private String returnUserName;
	public String getBorrowUserName() {
		return borrowUserName;
	}
	public void setBorrowUserName(String borrowUserName) {
		this.borrowUserName = borrowUserName;
	}
	public String getBorrowUserDeptName() {
		return borrowUserDeptName;
	}
	public void setBorrowUserDeptName(String borrowUserDeptName) {
		this.borrowUserDeptName = borrowUserDeptName;
	}
	public String getReturnUserName() {
		return returnUserName;
	}
	public void setReturnUserName(String returnUserName) {
		this.returnUserName = returnUserName;
	}
}
