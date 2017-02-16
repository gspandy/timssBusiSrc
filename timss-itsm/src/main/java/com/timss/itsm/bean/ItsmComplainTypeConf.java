package com.timss.itsm.bean;

import java.io.Serializable;


import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmComplainTypeConf extends ItcMvcBean implements Serializable{

	private static final long serialVersionUID = 6435896637355247061L;
	
	@UUIDGen(requireType = GenerationType.REQUIRED_NULL) // 自动生成id
	private String id; 
	private String typename;  //投诉类别
	private String remarks;  //投诉内容
	private String active;  //有效标识
	private String createUserName;//创建人
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Override
	public String toString() {
		return "ComplainType [id=" + id + ",typename=" + typename + ",remarks=" + remarks + ",active=" + active +"]";
	}
}
