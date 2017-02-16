package com.timss.workorder.bean;

import java.io.Serializable;

import com.yudean.mvc.bean.ItcMvcBean;

public class Precaution extends ItcMvcBean implements Serializable {
	 
	private static final long serialVersionUID = 86056660226392948L;
	private int id; //ID
	  private String name;  //名字
	  private String precautionDescription;  //描述
	  private int yxbz;
	  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	 
	public String getPrecautionDescription() {
		return precautionDescription;
	}
	public void setPrecautionDescription(String precautionDescription) {
		this.precautionDescription = precautionDescription;
	}
	public int getYxbz() {
		return yxbz;
	}
	public void setYxbz(int yxbz) {
		this.yxbz = yxbz;
	}
	  
	  
}
