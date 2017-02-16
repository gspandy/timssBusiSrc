package com.timss.asset.bean;

import java.util.Date;
import java.util.List;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.EntityID;
import com.yudean.mvc.bean.ItcMvcBean;

public class SwLedgerBean extends ItcMvcBean {
	private static final long serialVersionUID = -4683474474395240465L;
	
	/** 软件台账id
	 * 
	 * */
	@AutoGen("AST_SW_SEQ")
	@EntityID
	private String swId;
	/** 系统名称
	 * 
	 * */
	private String swName;
	/** 所属单位，关联到单位的表
	 * 
	 * */
	private String ownerOrg;
	/** 业务关联
	 * 
	 * */
	private String relatedBusiness;
	/** 负责人
	 * 
	 * */
	private String principal;
	/** 供应商
	 * 
	 * */
	private String supplier;
	/** 投入时间
	 * 
	 * */
	private Date toUseTime;
	/** 备注
	 * 
	 * */
	private String remarks;
	/** 来源的项目id
	 * 
	 * */
	private String projectId;
	
	/**
	 * 应用列表
	 */
	private List<SwLedgerAppBean> apps;
	/**
	 * 应用的ip的字符串，用于ip的表头搜索
	 */
	private String ip;
	
	private String attr01;
	private String attr02;	
	private String attr03;	
	private String attr04;	
	private String attr05;	
	private String attr06;	
	private String attr07;	
	private String attr08;	
	private String attr09;	
	private String attr10;	
	private String attr11;	
	private String attr12;	
	private String attr13;	
	private String attr14;	
	private String attr15;	
	private String attr16;	
	private String attr17;	
	private String attr18;	
	private String attr19;	
	private String attr20;	
	private String longAttr01;	
	private String longAttr02;	
	private String longAttr03;	
	private String longAttr04;	
	private String longAttr05;	
	private String longAttr06;	
	private String longAttr07;	
	private String longAttr08;	
	private String longAttr09;	
	private String longAttr10;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSwId() {
		return swId;
	}
	public void setSwId(String swId) {
		this.swId = swId;
	}
	public String getSwName() {
		return swName;
	}
	public void setSwName(String swName) {
		this.swName = swName;
	}
	public String getOwnerOrg() {
		return ownerOrg;
	}
	public void setOwnerOrg(String ownerOrg) {
		this.ownerOrg = ownerOrg;
	}
	public String getRelatedBusiness() {
		return relatedBusiness;
	}
	public void setRelatedBusiness(String relatedBusiness) {
		this.relatedBusiness = relatedBusiness;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public Date getToUseTime() {
		return toUseTime;
	}
	public void setToUseTime(Date toUseTime) {
		this.toUseTime = toUseTime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public List<SwLedgerAppBean> getApps() {
		return apps;
	}
	public void setApps(List<SwLedgerAppBean> apps) {
		this.apps = apps;
	}
	public String getAttr01() {
		return attr01;
	}
	public void setAttr01(String attr01) {
		this.attr01 = attr01;
	}
	public String getAttr02() {
		return attr02;
	}
	public void setAttr02(String attr02) {
		this.attr02 = attr02;
	}
	public String getAttr03() {
		return attr03;
	}
	public void setAttr03(String attr03) {
		this.attr03 = attr03;
	}
	public String getAttr04() {
		return attr04;
	}
	public void setAttr04(String attr04) {
		this.attr04 = attr04;
	}
	public String getAttr05() {
		return attr05;
	}
	public void setAttr05(String attr05) {
		this.attr05 = attr05;
	}
	public String getAttr06() {
		return attr06;
	}
	public void setAttr06(String attr06) {
		this.attr06 = attr06;
	}
	public String getAttr07() {
		return attr07;
	}
	public void setAttr07(String attr07) {
		this.attr07 = attr07;
	}
	public String getAttr08() {
		return attr08;
	}
	public void setAttr08(String attr08) {
		this.attr08 = attr08;
	}
	public String getAttr09() {
		return attr09;
	}
	public void setAttr09(String attr09) {
		this.attr09 = attr09;
	}
	public String getAttr10() {
		return attr10;
	}
	public void setAttr10(String attr10) {
		this.attr10 = attr10;
	}
	public String getAttr11() {
		return attr11;
	}
	public void setAttr11(String attr11) {
		this.attr11 = attr11;
	}
	public String getAttr12() {
		return attr12;
	}
	public void setAttr12(String attr12) {
		this.attr12 = attr12;
	}
	public String getAttr13() {
		return attr13;
	}
	public void setAttr13(String attr13) {
		this.attr13 = attr13;
	}
	public String getAttr14() {
		return attr14;
	}
	public void setAttr14(String attr14) {
		this.attr14 = attr14;
	}
	public String getAttr15() {
		return attr15;
	}
	public void setAttr15(String attr15) {
		this.attr15 = attr15;
	}
	public String getAttr16() {
		return attr16;
	}
	public void setAttr16(String attr16) {
		this.attr16 = attr16;
	}
	public String getAttr17() {
		return attr17;
	}
	public void setAttr17(String attr17) {
		this.attr17 = attr17;
	}
	public String getAttr18() {
		return attr18;
	}
	public void setAttr18(String attr18) {
		this.attr18 = attr18;
	}
	public String getAttr19() {
		return attr19;
	}
	public void setAttr19(String attr19) {
		this.attr19 = attr19;
	}
	public String getAttr20() {
		return attr20;
	}
	public void setAttr20(String attr20) {
		this.attr20 = attr20;
	}
	public String getLongAttr01() {
		return longAttr01;
	}
	public void setLongAttr01(String longAttr01) {
		this.longAttr01 = longAttr01;
	}
	public String getLongAttr02() {
		return longAttr02;
	}
	public void setLongAttr02(String longAttr02) {
		this.longAttr02 = longAttr02;
	}
	public String getLongAttr03() {
		return longAttr03;
	}
	public void setLongAttr03(String longAttr03) {
		this.longAttr03 = longAttr03;
	}
	public String getLongAttr04() {
		return longAttr04;
	}
	public void setLongAttr04(String longAttr04) {
		this.longAttr04 = longAttr04;
	}
	public String getLongAttr05() {
		return longAttr05;
	}
	public void setLongAttr05(String longAttr05) {
		this.longAttr05 = longAttr05;
	}
	public String getLongAttr06() {
		return longAttr06;
	}
	public void setLongAttr06(String longAttr06) {
		this.longAttr06 = longAttr06;
	}
	public String getLongAttr07() {
		return longAttr07;
	}
	public void setLongAttr07(String longAttr07) {
		this.longAttr07 = longAttr07;
	}
	public String getLongAttr08() {
		return longAttr08;
	}
	public void setLongAttr08(String longAttr08) {
		this.longAttr08 = longAttr08;
	}
	public String getLongAttr09() {
		return longAttr09;
	}
	public void setLongAttr09(String longAttr09) {
		this.longAttr09 = longAttr09;
	}
	public String getLongAttr10() {
		return longAttr10;
	}
	public void setLongAttr10(String longAttr10) {
		this.longAttr10 = longAttr10;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}