package com.timss.asset.bean;

import java.util.Date;

/** 硬件台账的所有设备的共有的信息，包括位置、备注、服务信息、关联情况
 * 
 **/
public class HwLedgerEqptBean extends HwLedgerBean {
	private static final long serialVersionUID = 6385830675503926982L;
	/** 位置
	 * 
	 **/
	private String location;
	/** 备注
	 * 
	 **/
	private String remarks;
	/** 资产编号
	 * 
	 **/
	private String assetCode;
	/** 所属单位
	 * 
	 **/
	private String ownOrg;
	/** 上线日期
	 * 
	 **/
	private Date toUseTime;
	/** 过保日期
	 * 
	 **/
	private Date elapsedTime;
	/** 使用状态
	 * 
	 **/
	private String status;
	/** 维修记录
	 * 
	 **/
	private String repairRecard;
	/** 运维负责人id
	 * 
	 **/
	private String principal;
	/** 供货商id
	 * 
	 **/
	private String supplier;
	/** 关联设备
	 * 
	 **/
	private String relatedEqpt;
	/** 外联业务
	 * 
	 **/
	private String relatedBusiness;
	/**
	 * 运维负责人名字
	 */
	private String eqptAttr01;
	/**  **/
	private String eqptAttr02;
	/**  **/
	private String eqptAttr03;
	/**  **/
	private String eqptAttr04;
	/**  **/
	private String eqptAttr05;
	/**  **/
	private String eqptAttr06;
	/**  **/
	private String eqptAttr07;
	/**  **/
	private String eqptAttr08;
	/**  **/
	private String eqptAttr09;
	/**  **/
	private String eqptAttr10;
	/**  **/
	private String eqptAttr11;
	/**  **/
	private String eqptAttr12;
	/**  **/
	private String eqptAttr13;
	/**  **/
	private String eqptAttr14;
	/**  **/
	private String eqptAttr15;
	/**  **/
	private String eqptAttr16;
	/**  **/
	private String eqptAttr17;
	/**  **/
	private String eqptAttr18;
	/**  **/
	private String eqptAttr19;
	/**  **/
	private String eqptAttr20;
	/**  **/
	private String eqptLongAttr01;
	/**  **/
	private String eqptLongAttr02;
	/**  **/
	private String eqptLongAttr03;
	/**  **/
	private String eqptLongAttr04;
	/**  **/
	private String eqptLongAttr05;
	/**  **/
	private String eqptLongAttr06;
	/**  **/
	private String eqptLongAttr07;
	/**  **/
	private String eqptLongAttr08;
	/**  **/
	private String eqptLongAttr09;
	/**  **/
	private String eqptLongAttr10;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAssetCode() {
		return assetCode;
	}
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	public String getOwnOrg() {
		return ownOrg;
	}
	public void setOwnOrg(String ownOrg) {
		this.ownOrg = ownOrg;
	}
	public Date getToUseTime() {
		return toUseTime;
	}
	public void setToUseTime(Date toUseTime) {
		this.toUseTime = toUseTime;
	}
	public Date getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(Date elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRepairRecard() {
		return repairRecard;
	}
	public void setRepairRecard(String repairRecard) {
		this.repairRecard = repairRecard;
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
	public String getRelatedEqpt() {
		return relatedEqpt;
	}
	public void setRelatedEqpt(String relatedEqpt) {
		this.relatedEqpt = relatedEqpt;
	}
	public String getRelatedBusiness() {
		return relatedBusiness;
	}
	public void setRelatedBusiness(String relatedBusiness) {
		this.relatedBusiness = relatedBusiness;
	}
	public String getEqptAttr01() {
		return eqptAttr01;
	}
	public void setEqptAttr01(String eqptAttr01) {
		this.eqptAttr01 = eqptAttr01;
	}
	public String getEqptAttr02() {
		return eqptAttr02;
	}
	public void setEqptAttr02(String eqptAttr02) {
		this.eqptAttr02 = eqptAttr02;
	}
	public String getEqptAttr03() {
		return eqptAttr03;
	}
	public void setEqptAttr03(String eqptAttr03) {
		this.eqptAttr03 = eqptAttr03;
	}
	public String getEqptAttr04() {
		return eqptAttr04;
	}
	public void setEqptAttr04(String eqptAttr04) {
		this.eqptAttr04 = eqptAttr04;
	}
	public String getEqptAttr05() {
		return eqptAttr05;
	}
	public void setEqptAttr05(String eqptAttr05) {
		this.eqptAttr05 = eqptAttr05;
	}
	public String getEqptAttr06() {
		return eqptAttr06;
	}
	public void setEqptAttr06(String eqptAttr06) {
		this.eqptAttr06 = eqptAttr06;
	}
	public String getEqptAttr07() {
		return eqptAttr07;
	}
	public void setEqptAttr07(String eqptAttr07) {
		this.eqptAttr07 = eqptAttr07;
	}
	public String getEqptAttr08() {
		return eqptAttr08;
	}
	public void setEqptAttr08(String eqptAttr08) {
		this.eqptAttr08 = eqptAttr08;
	}
	public String getEqptAttr09() {
		return eqptAttr09;
	}
	public void setEqptAttr09(String eqptAttr09) {
		this.eqptAttr09 = eqptAttr09;
	}
	public String getEqptAttr10() {
		return eqptAttr10;
	}
	public void setEqptAttr10(String eqptAttr10) {
		this.eqptAttr10 = eqptAttr10;
	}
	public String getEqptAttr11() {
		return eqptAttr11;
	}
	public void setEqptAttr11(String eqptAttr11) {
		this.eqptAttr11 = eqptAttr11;
	}
	public String getEqptAttr12() {
		return eqptAttr12;
	}
	public void setEqptAttr12(String eqptAttr12) {
		this.eqptAttr12 = eqptAttr12;
	}
	public String getEqptAttr13() {
		return eqptAttr13;
	}
	public void setEqptAttr13(String eqptAttr13) {
		this.eqptAttr13 = eqptAttr13;
	}
	public String getEqptAttr14() {
		return eqptAttr14;
	}
	public void setEqptAttr14(String eqptAttr14) {
		this.eqptAttr14 = eqptAttr14;
	}
	public String getEqptAttr15() {
		return eqptAttr15;
	}
	public void setEqptAttr15(String eqptAttr15) {
		this.eqptAttr15 = eqptAttr15;
	}
	public String getEqptAttr16() {
		return eqptAttr16;
	}
	public void setEqptAttr16(String eqptAttr16) {
		this.eqptAttr16 = eqptAttr16;
	}
	public String getEqptAttr17() {
		return eqptAttr17;
	}
	public void setEqptAttr17(String eqptAttr17) {
		this.eqptAttr17 = eqptAttr17;
	}
	public String getEqptAttr18() {
		return eqptAttr18;
	}
	public void setEqptAttr18(String eqptAttr18) {
		this.eqptAttr18 = eqptAttr18;
	}
	public String getEqptAttr19() {
		return eqptAttr19;
	}
	public void setEqptAttr19(String eqptAttr19) {
		this.eqptAttr19 = eqptAttr19;
	}
	public String getEqptAttr20() {
		return eqptAttr20;
	}
	public void setEqptAttr20(String eqptAttr20) {
		this.eqptAttr20 = eqptAttr20;
	}
	public String getEqptLongAttr01() {
		return eqptLongAttr01;
	}
	public void setEqptLongAttr01(String eqptLongAttr01) {
		this.eqptLongAttr01 = eqptLongAttr01;
	}
	public String getEqptLongAttr02() {
		return eqptLongAttr02;
	}
	public void setEqptLongAttr02(String eqptLongAttr02) {
		this.eqptLongAttr02 = eqptLongAttr02;
	}
	public String getEqptLongAttr03() {
		return eqptLongAttr03;
	}
	public void setEqptLongAttr03(String eqptLongAttr03) {
		this.eqptLongAttr03 = eqptLongAttr03;
	}
	public String getEqptLongAttr04() {
		return eqptLongAttr04;
	}
	public void setEqptLongAttr04(String eqptLongAttr04) {
		this.eqptLongAttr04 = eqptLongAttr04;
	}
	public String getEqptLongAttr05() {
		return eqptLongAttr05;
	}
	public void setEqptLongAttr05(String eqptLongAttr05) {
		this.eqptLongAttr05 = eqptLongAttr05;
	}
	public String getEqptLongAttr06() {
		return eqptLongAttr06;
	}
	public void setEqptLongAttr06(String eqptLongAttr06) {
		this.eqptLongAttr06 = eqptLongAttr06;
	}
	public String getEqptLongAttr07() {
		return eqptLongAttr07;
	}
	public void setEqptLongAttr07(String eqptLongAttr07) {
		this.eqptLongAttr07 = eqptLongAttr07;
	}
	public String getEqptLongAttr08() {
		return eqptLongAttr08;
	}
	public void setEqptLongAttr08(String eqptLongAttr08) {
		this.eqptLongAttr08 = eqptLongAttr08;
	}
	public String getEqptLongAttr09() {
		return eqptLongAttr09;
	}
	public void setEqptLongAttr09(String eqptLongAttr09) {
		this.eqptLongAttr09 = eqptLongAttr09;
	}
	public String getEqptLongAttr10() {
		return eqptLongAttr10;
	}
	public void setEqptLongAttr10(String eqptLongAttr10) {
		this.eqptLongAttr10 = eqptLongAttr10;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}