package com.timss.workorder.bean;

import java.io.Serializable;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class WoLabel extends ItcMvcBean implements Serializable {
	   
	 
	private static final long serialVersionUID = -1826127514139071136L;
	private int id; //ID
	  @AutoGen(value = "WO_LABEL",requireType = GenerationType.REQUIRED_NEW)
	  private String labelCode;  //编号
	  private String name;  //标识名称
	  private int weight;  //权重
	  private String remarks;//说明
	 
	  private int yxbz;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLabelCode() {
		return labelCode;
	}
	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	public int getYxbz() {
		return yxbz;
	}
	public void setYxbz(int yxbz) {
		this.yxbz = yxbz;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "WoLabel [id=" + id + ", labelCode=" + labelCode + ", name="
				+ name + ", weight=" + weight + ", remarks=" + remarks
				+ ", yxbz="+ yxbz + "]";
	}
	  
	
	
}
