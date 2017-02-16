package com.timss.workorder.bean;

import java.io.Serializable;

import com.yudean.mvc.bean.ItcMvcBean;


public class Hazard extends ItcMvcBean implements Serializable{
	  
	private static final long serialVersionUID = 1563756560204462137L;
	private int id; //ID
	  private String name;  //名字
	  private String hazardDescription;  //描述
	  private String hazardType;
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
	 
	public String getHazardDescription() {
		return hazardDescription;
	}
	public void setHazardDescription(String hazardDescription) {
		this.hazardDescription = hazardDescription;
	}
	public String getHazardType() {
		return hazardType;
	}
	public void setHazardType(String hazardType) {
		this.hazardType = hazardType;
	}
	public int getYxbz() {
		return yxbz;
	}
	public void setYxbz(int yxbz) {
		this.yxbz = yxbz;
	}
	 
	  
}
