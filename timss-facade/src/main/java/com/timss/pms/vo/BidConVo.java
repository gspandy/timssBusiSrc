package com.timss.pms.vo;

import com.timss.pms.bean.BidCon;

public class BidConVo extends BidCon{
	private String type;
	private String name;
	private String contact;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	
}
