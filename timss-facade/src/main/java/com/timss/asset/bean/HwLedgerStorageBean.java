package com.timss.asset.bean;

/** 存储设备 **/
public class HwLedgerStorageBean extends HwLedgerEqptBean{

	private static final long serialVersionUID = -2429709380767131429L;
	/** 设备型号，关联硬件类型维护中的存储设备型号 **/
	private String storageModel;
	private String storageModelName;
	/** SN  **/
	private String snCode;
	/** 品牌，关联硬件类型维护中的存储设备品牌 **/
	private String storageBrand;
	private String storageBrandName;
	/** 控制器 **/
	private String controller;
	/** 缓存 **/
	private String cache;
	/** 硬盘 **/
	private String harddisk;
	/** 前端主机接口情况 **/
	private String frontInterface;
	/** 后端接口情况 **/
	private String backInterface;
	/** 额定电源功率 **/
	private String power;
	/** 统一虚拟化情况 **/
	private String virtualSituation;
	/** 管理IP **/
	private String ip;
	/** 管理MAC **/
	private String mac;
	/** 管理vlan **/
	private String vlan;
	/** 网络区域，关联枚举表 **/
	private String netArea;
	/** 虚拟化部署位置，关联枚举表 **/
	private String virtualDeployLocation;
	/** 存储类型，关联硬件类型维护中的存储类型 **/
	private String storageType;
	private String storageTypeName;
	/** RAID类型，关联硬件类型维护中的RAID类型 **/
	private String raidType;
	private String raidTypeName;
	/** 有效容量 **/
	private String effectiveCapacity;
	/** 热备盘数量 **/
	public Integer spareDrivesNum;
	/** LUN名称 **/
	private String lunName;
	/** LUN大小 **/
	private String lunNum;
	/** LUN使用主机 **/
	private String lunHost;
	/**  **/
	private String storageAttr01;
	/**  **/
	private String storageAttr02;
	/**  **/
	private String storageAttr03;
	/**  **/
	private String storageAttr04;
	/**  **/
	private String storageAttr05;
	/**  **/
	private String storageAttr06;
	/**  **/
	private String storageAttr07;
	/**  **/
	private String storageAttr08;
	/**  **/
	private String storageAttr09;
	/**  **/
	private String storageAttr10;
	/**  **/
	private String storageAttr11;
	/**  **/
	private String storageAttr12;
	/**  **/
	private String storageAttr13;
	/**  **/
	private String storageAttr14;
	/**  **/
	private String storageAttr15;
	/**  **/
	private String storageAttr16;
	/**  **/
	private String storageAttr17;
	/**  **/
	private String storageAttr18;
	/**  **/
	private String storageAttr19;
	/**  **/
	private String storageAttr20;
	/**  **/
	private String storageLongAttr01;
	/**  **/
	private String storageLongAttr02;
	/**  **/
	private String storageLongAttr03;
	/**  **/
	private String storageLongAttr04;
	/**  **/
	private String storageLongAttr05;
	/**  **/
	private String storageLongAttr06;
	/**  **/
	private String storageLongAttr07;
	/**  **/
	private String storageLongAttr08;
	/**  **/
	private String storageLongAttr09;
	/**  **/
	private String storageLongAttr10;
	
	public String getStorageModel() {
		return storageModel;
	}
	public void setStorageModel(String storageModel) {
		this.storageModel = storageModel;
	}
	public String getSnCode() {
		return snCode;
	}
	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}
	public String getStorageBrand() {
		return storageBrand;
	}
	public void setStorageBrand(String storageBrand) {
		this.storageBrand = storageBrand;
	}
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
	}
	public String getCache() {
		return cache;
	}
	public void setCache(String cache) {
		this.cache = cache;
	}
	public String getHarddisk() {
		return harddisk;
	}
	public void setHarddisk(String harddisk) {
		this.harddisk = harddisk;
	}
	public String getFrontInterface() {
		return frontInterface;
	}
	public void setFrontInterface(String frontInterface) {
		this.frontInterface = frontInterface;
	}
	public String getBackInterface() {
		return backInterface;
	}
	public void setBackInterface(String backInterface) {
		this.backInterface = backInterface;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getVirtualSituation() {
		return virtualSituation;
	}
	public void setVirtualSituation(String virtualSituation) {
		this.virtualSituation = virtualSituation;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getVlan() {
		return vlan;
	}
	public void setVlan(String vlan) {
		this.vlan = vlan;
	}
	public String getNetArea() {
		return netArea;
	}
	public void setNetArea(String netArea) {
		this.netArea = netArea;
	}
	public String getVirtualDeployLocation() {
		return virtualDeployLocation;
	}
	public void setVirtualDeployLocation(String virtualDeployLocation) {
		this.virtualDeployLocation = virtualDeployLocation;
	}
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public String getRaidType() {
		return raidType;
	}
	public void setRaidType(String raidType) {
		this.raidType = raidType;
	}
	public String getEffectiveCapacity() {
		return effectiveCapacity;
	}
	public void setEffectiveCapacity(String effectiveCapacity) {
		this.effectiveCapacity = effectiveCapacity;
	}
	public Integer getSpareDrivesNum() {
		return spareDrivesNum;
	}
	public void setSpareDrivesNum(Integer spareDrivesNum) {
		this.spareDrivesNum = spareDrivesNum;
	}
	public String getLunName() {
		return lunName;
	}
	public void setLunName(String lunName) {
		this.lunName = lunName;
	}
	public String getLunNum() {
		return lunNum;
	}
	public void setLunNum(String lunNum) {
		this.lunNum = lunNum;
	}
	public String getLunHost() {
		return lunHost;
	}
	public void setLunHost(String lunHost) {
		this.lunHost = lunHost;
	}
	public String getStorageAttr01() {
		return storageAttr01;
	}
	public void setStorageAttr01(String storageAttr01) {
		this.storageAttr01 = storageAttr01;
	}
	public String getStorageAttr02() {
		return storageAttr02;
	}
	public void setStorageAttr02(String storageAttr02) {
		this.storageAttr02 = storageAttr02;
	}
	public String getStorageAttr03() {
		return storageAttr03;
	}
	public void setStorageAttr03(String storageAttr03) {
		this.storageAttr03 = storageAttr03;
	}
	public String getStorageAttr04() {
		return storageAttr04;
	}
	public void setStorageAttr04(String storageAttr04) {
		this.storageAttr04 = storageAttr04;
	}
	public String getStorageAttr05() {
		return storageAttr05;
	}
	public void setStorageAttr05(String storageAttr05) {
		this.storageAttr05 = storageAttr05;
	}
	public String getStorageAttr06() {
		return storageAttr06;
	}
	public void setStorageAttr06(String storageAttr06) {
		this.storageAttr06 = storageAttr06;
	}
	public String getStorageAttr07() {
		return storageAttr07;
	}
	public void setStorageAttr07(String storageAttr07) {
		this.storageAttr07 = storageAttr07;
	}
	public String getStorageAttr08() {
		return storageAttr08;
	}
	public void setStorageAttr08(String storageAttr08) {
		this.storageAttr08 = storageAttr08;
	}
	public String getStorageAttr09() {
		return storageAttr09;
	}
	public void setStorageAttr09(String storageAttr09) {
		this.storageAttr09 = storageAttr09;
	}
	public String getStorageAttr10() {
		return storageAttr10;
	}
	public void setStorageAttr10(String storageAttr10) {
		this.storageAttr10 = storageAttr10;
	}
	public String getStorageAttr11() {
		return storageAttr11;
	}
	public void setStorageAttr11(String storageAttr11) {
		this.storageAttr11 = storageAttr11;
	}
	public String getStorageAttr12() {
		return storageAttr12;
	}
	public void setStorageAttr12(String storageAttr12) {
		this.storageAttr12 = storageAttr12;
	}
	public String getStorageAttr13() {
		return storageAttr13;
	}
	public void setStorageAttr13(String storageAttr13) {
		this.storageAttr13 = storageAttr13;
	}
	public String getStorageAttr14() {
		return storageAttr14;
	}
	public void setStorageAttr14(String storageAttr14) {
		this.storageAttr14 = storageAttr14;
	}
	public String getStorageAttr15() {
		return storageAttr15;
	}
	public void setStorageAttr15(String storageAttr15) {
		this.storageAttr15 = storageAttr15;
	}
	public String getStorageAttr16() {
		return storageAttr16;
	}
	public void setStorageAttr16(String storageAttr16) {
		this.storageAttr16 = storageAttr16;
	}
	public String getStorageAttr17() {
		return storageAttr17;
	}
	public void setStorageAttr17(String storageAttr17) {
		this.storageAttr17 = storageAttr17;
	}
	public String getStorageAttr18() {
		return storageAttr18;
	}
	public void setStorageAttr18(String storageAttr18) {
		this.storageAttr18 = storageAttr18;
	}
	public String getStorageAttr19() {
		return storageAttr19;
	}
	public void setStorageAttr19(String storageAttr19) {
		this.storageAttr19 = storageAttr19;
	}
	public String getStorageAttr20() {
		return storageAttr20;
	}
	public void setStorageAttr20(String storageAttr20) {
		this.storageAttr20 = storageAttr20;
	}
	public String getStorageLongAttr01() {
		return storageLongAttr01;
	}
	public void setStorageLongAttr01(String storageLongAttr01) {
		this.storageLongAttr01 = storageLongAttr01;
	}
	public String getStorageLongAttr02() {
		return storageLongAttr02;
	}
	public void setStorageLongAttr02(String storageLongAttr02) {
		this.storageLongAttr02 = storageLongAttr02;
	}
	public String getStorageLongAttr03() {
		return storageLongAttr03;
	}
	public void setStorageLongAttr03(String storageLongAttr03) {
		this.storageLongAttr03 = storageLongAttr03;
	}
	public String getStorageLongAttr04() {
		return storageLongAttr04;
	}
	public void setStorageLongAttr04(String storageLongAttr04) {
		this.storageLongAttr04 = storageLongAttr04;
	}
	public String getStorageLongAttr05() {
		return storageLongAttr05;
	}
	public void setStorageLongAttr05(String storageLongAttr05) {
		this.storageLongAttr05 = storageLongAttr05;
	}
	public String getStorageLongAttr06() {
		return storageLongAttr06;
	}
	public void setStorageLongAttr06(String storageLongAttr06) {
		this.storageLongAttr06 = storageLongAttr06;
	}
	public String getStorageLongAttr07() {
		return storageLongAttr07;
	}
	public void setStorageLongAttr07(String storageLongAttr07) {
		this.storageLongAttr07 = storageLongAttr07;
	}
	public String getStorageLongAttr08() {
		return storageLongAttr08;
	}
	public void setStorageLongAttr08(String storageLongAttr08) {
		this.storageLongAttr08 = storageLongAttr08;
	}
	public String getStorageLongAttr09() {
		return storageLongAttr09;
	}
	public void setStorageLongAttr09(String storageLongAttr09) {
		this.storageLongAttr09 = storageLongAttr09;
	}
	public String getStorageLongAttr10() {
		return storageLongAttr10;
	}
	public void setStorageLongAttr10(String storageLongAttr10) {
		this.storageLongAttr10 = storageLongAttr10;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getStorageModelName() {
		return storageModelName;
	}
	public void setStorageModelName(String storageModelName) {
		this.storageModelName = storageModelName;
	}
	public String getStorageBrandName() {
		return storageBrandName;
	}
	public void setStorageBrandName(String storageBrandName) {
		this.storageBrandName = storageBrandName;
	}
	public String getStorageTypeName() {
		return storageTypeName;
	}
	public void setStorageTypeName(String storageTypeName) {
		this.storageTypeName = storageTypeName;
	}
	public String getRaidTypeName() {
		return raidTypeName;
	}
	public void setRaidTypeName(String raidTypeName) {
		this.raidTypeName = raidTypeName;
	}
	
}