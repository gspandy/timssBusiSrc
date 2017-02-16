package com.timss.workorder.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

public class WoFaultType extends ItcMvcBean  {
	   
	private static final long serialVersionUID = -2021071475232745842L;
	private int id; //ID
	 // @AutoGen(value = "FAULT_TYPE",requireType = GenerationType.REQUIRED_NEW)
	  private String faultTypeCode;  //编号类型（SD:服务目录，SC：服务性质，SCROOT：服务性质的根）
	  private String name;  //故障类型名字
	  private Integer parentId;  //父类型ID
	  private String  parentTypeName;  //父类型名
	  private int defaultScore;  //默认积分（现在用于做排序字段）
	  private String remarks;//说明
	  private String keywords; //关键字
	  private String principalGroup; //服务目录对应的维护组（用户组）
	  
	  private String createUser;
	  private String modifyUser;
	  private Date createDate;
	  private Date modifyDate;
	 
	  private int yxbz;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFaultTypeCode() {
		return faultTypeCode;
	}
	public void setFaultTypeCode(String faultTypeCode) {
		this.faultTypeCode = faultTypeCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getParentTypeName() {
		return parentTypeName;
	}
	public void setParentTypeName(String parentTypeName) {
		this.parentTypeName = parentTypeName;
	}
	public int getDefaultScore() {
		return defaultScore;
	}
	public void setDefaultScore(int defaultScore) {
		this.defaultScore = defaultScore;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getPrincipalGroup() {
		return principalGroup;
	}
	public void setPrincipalGroup(String principalGroup) {
		this.principalGroup = principalGroup;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public int getYxbz() {
		return yxbz;
	}
	public void setYxbz(int yxbz) {
		this.yxbz = yxbz;
	}
	@Override
	public String toString() {
		return "WoFaultType [id=" + id + ", faultTypeCode=" + faultTypeCode
				+ ", name=" + name + ", parentId=" + parentId
				+ ", parentTypeName=" + parentTypeName + ", defaultScore="
				+ defaultScore + ", remarks=" + remarks + ", keywords="
				+ keywords + ", principalGroup=" + principalGroup
				+ ", createUser=" + createUser + ", modifyUser=" + modifyUser
				+ ", createDate=" + createDate + ", modifyDate=" + modifyDate
				+ ", yxbz="+ yxbz + "]";
	}
	 
	
	
}
