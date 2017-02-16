package com.timss.itsm.bean;

import java.io.Serializable;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmWoSkill extends ItcMvcBean implements Serializable {
	   
	private static final long serialVersionUID = -4791202288338004294L;
	private int id;  
	  @AutoGen(value = "ITSM_SKILL",requireType = GenerationType.REQUIRED_NEW)
	  private String skillCode;  //编号
	  private String name;  //名字
	  private String remarks;//说明
	  private int yxbz;
	  
	  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSkillCode() {
		return skillCode;
	}
	public void setSkillCode(String skillCode) {
		this.skillCode = skillCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	@Override
	public String toString() {
		return "Skill [id=" + id + ", skillCode=" + skillCode + ", name="
				+ name + ", remarks=" + remarks + ", yxbz=" + yxbz + "]";
	}
	  
	
	
}
