package com.timss.asset.bean;

public class HwLedgerDeviceBean extends HwLedgerEqptBean {
	private static final long serialVersionUID = 3973168906833846626L;

	/** 
	 * 计算机名
	 * */
	private String computerName;
	/** 
	 * 服务器操作系统
	 * */
	private String os;
	/** 
	 * 服务器补丁
	 * */
	private String osPath;
	/** 
	 * 逻辑处理器
	 * */
	private String logicCpu;
	/** 
	 * 逻辑内存
	 * */
	private String logicMem;
	/** 
	 * 逻辑硬盘
	 * */
	private String logicHarddisk;
	/** 
	 * 服务器IP地址
	 * */
	private String ip;
	/** 
	 * 是否为集群IP
	 * */
	private String isClusterIp;
	/** 
	 * MAC地址
	 * */
	private String mac;
	/** 
	 * 服务器VLAN
	 * */
	private String vlan;
	/** 
	 * 服务器网络区域
	 * */
	private String netArea;
	/** 
	 * 服务器公网通讯
	 * */
	private String isPublicNet;
	/** 
	 * 远程备份
	 * */
	private String isRemoteBackup;
	/** 
	 * 虚拟带库备份
	 * */
	private String isVtlBackup;
	/** 
	 * CDP备份
	 * */
	private String isCdpBackup;
	/** 
	 * 物理带库备份
	 * */
	private String isPtlBackup;
	/** 
	 * 服务器手工备份
	 * */
	private String isManualBackup;
	/** 
	 * 存储硬件类型，关联硬件类型维护中的存储硬件型号
	 * */
	private String storageModel;
	/** 
	 * 服务器存储类型，关联硬件类型维护中的存储类型
	 * */
	private String storageType;
	/** 
	 * SAN-LUN
	 * */
	private String sanLun;
	/** 
	 * RAID类型
	 * */
	private String raidType;
	/** 
	 * LUN大小
	 * */
	private String lunNum;
	/** 
	 * 数据变化度
	 * */
	private String dataChangeDegree;
	
	private String deviceAttr01;
	private String deviceAttr02;	
	private String deviceAttr03;	
	private String deviceAttr04;	
	private String deviceAttr05;	
	private String deviceAttr06;	
	private String deviceAttr07;	
	private String deviceAttr08;	
	private String deviceAttr09;	
	private String deviceAttr10;	
	private String deviceAttr11;	
	private String deviceAttr12;	
	private String deviceAttr13;	
	private String deviceAttr14;	
	private String deviceAttr15;	
	private String deviceAttr16;	
	private String deviceAttr17;	
	private String deviceAttr18;	
	private String deviceAttr19;	
	private String deviceAttr20;	
	private String deviceLongAttr01;	
	private String deviceLongAttr02;	
	private String deviceLongAttr03;	
	private String deviceLongAttr04;	
	private String deviceLongAttr05;	
	private String deviceLongAttr06;	
	private String deviceLongAttr07;	
	private String deviceLongAttr08;	
	private String deviceLongAttr09;	
	private String deviceLongAttr10;
	
	public String getComputerName() {
		return computerName;
	}
	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getOsPath() {
		return osPath;
	}
	public void setOsPath(String osPath) {
		this.osPath = osPath;
	}
	public String getLogicCpu() {
		return logicCpu;
	}
	public void setLogicCpu(String logicCpu) {
		this.logicCpu = logicCpu;
	}
	public String getLogicMem() {
		return logicMem;
	}
	public void setLogicMem(String logicMem) {
		this.logicMem = logicMem;
	}
	public String getLogicHarddisk() {
		return logicHarddisk;
	}
	public void setLogicHarddisk(String logicHarddisk) {
		this.logicHarddisk = logicHarddisk;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIsClusterIp() {
		return isClusterIp;
	}
	public void setIsClusterIp(String isClusterIp) {
		this.isClusterIp = isClusterIp;
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
	public String getIsPublicNet() {
		return isPublicNet;
	}
	public void setIsPublicNet(String isPublicNet) {
		this.isPublicNet = isPublicNet;
	}
	public String getIsRemoteBackup() {
		return isRemoteBackup;
	}
	public void setIsRemoteBackup(String isRemoteBackup) {
		this.isRemoteBackup = isRemoteBackup;
	}
	public String getIsVtlBackup() {
		return isVtlBackup;
	}
	public void setIsVtlBackup(String isVtlBackup) {
		this.isVtlBackup = isVtlBackup;
	}
	public String getIsCdpBackup() {
		return isCdpBackup;
	}
	public void setIsCdpBackup(String isCdpBackup) {
		this.isCdpBackup = isCdpBackup;
	}
	public String getIsPtlBackup() {
		return isPtlBackup;
	}
	public void setIsPtlBackup(String isPtlBackup) {
		this.isPtlBackup = isPtlBackup;
	}
	public String getIsManualBackup() {
		return isManualBackup;
	}
	public void setIsManualBackup(String isManualBackup) {
		this.isManualBackup = isManualBackup;
	}
	public String getStorageModel() {
		return storageModel;
	}
	public void setStorageModel(String storageModel) {
		this.storageModel = storageModel;
	}
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public String getSanLun() {
		return sanLun;
	}
	public void setSanLun(String sanLun) {
		this.sanLun = sanLun;
	}
	public String getRaidType() {
		return raidType;
	}
	public void setRaidType(String raidType) {
		this.raidType = raidType;
	}
	public String getLunNum() {
		return lunNum;
	}
	public void setLunNum(String lunNum) {
		this.lunNum = lunNum;
	}
	public String getDataChangeDegree() {
		return dataChangeDegree;
	}
	public void setDataChangeDegree(String dataChangeDegree) {
		this.dataChangeDegree = dataChangeDegree;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDeviceAttr01() {
		return deviceAttr01;
	}
	public void setDeviceAttr01(String deviceAttr01) {
		this.deviceAttr01 = deviceAttr01;
	}
	public String getDeviceAttr02() {
		return deviceAttr02;
	}
	public void setDeviceAttr02(String deviceAttr02) {
		this.deviceAttr02 = deviceAttr02;
	}
	public String getDeviceAttr03() {
		return deviceAttr03;
	}
	public void setDeviceAttr03(String deviceAttr03) {
		this.deviceAttr03 = deviceAttr03;
	}
	public String getDeviceAttr04() {
		return deviceAttr04;
	}
	public void setDeviceAttr04(String deviceAttr04) {
		this.deviceAttr04 = deviceAttr04;
	}
	public String getDeviceAttr05() {
		return deviceAttr05;
	}
	public void setDeviceAttr05(String deviceAttr05) {
		this.deviceAttr05 = deviceAttr05;
	}
	public String getDeviceAttr06() {
		return deviceAttr06;
	}
	public void setDeviceAttr06(String deviceAttr06) {
		this.deviceAttr06 = deviceAttr06;
	}
	public String getDeviceAttr07() {
		return deviceAttr07;
	}
	public void setDeviceAttr07(String deviceAttr07) {
		this.deviceAttr07 = deviceAttr07;
	}
	public String getDeviceAttr08() {
		return deviceAttr08;
	}
	public void setDeviceAttr08(String deviceAttr08) {
		this.deviceAttr08 = deviceAttr08;
	}
	public String getDeviceAttr09() {
		return deviceAttr09;
	}
	public void setDeviceAttr09(String deviceAttr09) {
		this.deviceAttr09 = deviceAttr09;
	}
	public String getDeviceAttr10() {
		return deviceAttr10;
	}
	public void setDeviceAttr10(String deviceAttr10) {
		this.deviceAttr10 = deviceAttr10;
	}
	public String getDeviceAttr11() {
		return deviceAttr11;
	}
	public void setDeviceAttr11(String deviceAttr11) {
		this.deviceAttr11 = deviceAttr11;
	}
	public String getDeviceAttr12() {
		return deviceAttr12;
	}
	public void setDeviceAttr12(String deviceAttr12) {
		this.deviceAttr12 = deviceAttr12;
	}
	public String getDeviceAttr13() {
		return deviceAttr13;
	}
	public void setDeviceAttr13(String deviceAttr13) {
		this.deviceAttr13 = deviceAttr13;
	}
	public String getDeviceAttr14() {
		return deviceAttr14;
	}
	public void setDeviceAttr14(String deviceAttr14) {
		this.deviceAttr14 = deviceAttr14;
	}
	public String getDeviceAttr15() {
		return deviceAttr15;
	}
	public void setDeviceAttr15(String deviceAttr15) {
		this.deviceAttr15 = deviceAttr15;
	}
	public String getDeviceAttr16() {
		return deviceAttr16;
	}
	public void setDeviceAttr16(String deviceAttr16) {
		this.deviceAttr16 = deviceAttr16;
	}
	public String getDeviceAttr17() {
		return deviceAttr17;
	}
	public void setDeviceAttr17(String deviceAttr17) {
		this.deviceAttr17 = deviceAttr17;
	}
	public String getDeviceAttr18() {
		return deviceAttr18;
	}
	public void setDeviceAttr18(String deviceAttr18) {
		this.deviceAttr18 = deviceAttr18;
	}
	public String getDeviceAttr19() {
		return deviceAttr19;
	}
	public void setDeviceAttr19(String deviceAttr19) {
		this.deviceAttr19 = deviceAttr19;
	}
	public String getDeviceAttr20() {
		return deviceAttr20;
	}
	public void setDeviceAttr20(String deviceAttr20) {
		this.deviceAttr20 = deviceAttr20;
	}
	public String getDeviceLongAttr01() {
		return deviceLongAttr01;
	}
	public void setDeviceLongAttr01(String deviceLongAttr01) {
		this.deviceLongAttr01 = deviceLongAttr01;
	}
	public String getDeviceLongAttr02() {
		return deviceLongAttr02;
	}
	public void setDeviceLongAttr02(String deviceLongAttr02) {
		this.deviceLongAttr02 = deviceLongAttr02;
	}
	public String getDeviceLongAttr03() {
		return deviceLongAttr03;
	}
	public void setDeviceLongAttr03(String deviceLongAttr03) {
		this.deviceLongAttr03 = deviceLongAttr03;
	}
	public String getDeviceLongAttr04() {
		return deviceLongAttr04;
	}
	public void setDeviceLongAttr04(String deviceLongAttr04) {
		this.deviceLongAttr04 = deviceLongAttr04;
	}
	public String getDeviceLongAttr05() {
		return deviceLongAttr05;
	}
	public void setDeviceLongAttr05(String deviceLongAttr05) {
		this.deviceLongAttr05 = deviceLongAttr05;
	}
	public String getDeviceLongAttr06() {
		return deviceLongAttr06;
	}
	public void setDeviceLongAttr06(String deviceLongAttr06) {
		this.deviceLongAttr06 = deviceLongAttr06;
	}
	public String getDeviceLongAttr07() {
		return deviceLongAttr07;
	}
	public void setDeviceLongAttr07(String deviceLongAttr07) {
		this.deviceLongAttr07 = deviceLongAttr07;
	}
	public String getDeviceLongAttr08() {
		return deviceLongAttr08;
	}
	public void setDeviceLongAttr08(String deviceLongAttr08) {
		this.deviceLongAttr08 = deviceLongAttr08;
	}
	public String getDeviceLongAttr09() {
		return deviceLongAttr09;
	}
	public void setDeviceLongAttr09(String deviceLongAttr09) {
		this.deviceLongAttr09 = deviceLongAttr09;
	}
	public String getDeviceLongAttr10() {
		return deviceLongAttr10;
	}
	public void setDeviceLongAttr10(String deviceLongAttr10) {
		this.deviceLongAttr10 = deviceLongAttr10;
	}
	
	
}
