package com.timss.itsm.bean;

import java.io.Serializable;

import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmCustomerLoc extends ItcMvcBean implements Serializable {
	
	private static final long serialVersionUID = -6198665570813363050L;
	private String customerCode;  //客户工号
	private String customerLoc;  //客户位置
	private int yxbz;
	
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getCustomerLoc() {
		return customerLoc;
	}
	public void setCustomerLoc(String customerLoc) {
		this.customerLoc = customerLoc;
	}
	public int getYxbz() {
		return yxbz;
	}
	public void setYxbz(int yxbz) {
		this.yxbz = yxbz;
	}
	@Override
	public String toString() {
		return "ItsmCustomerLoc [customerCode=" + customerCode + ", customerLoc="
				+ customerLoc + ", yxbz=" + yxbz + "]";
	}
	  
	
	
}
