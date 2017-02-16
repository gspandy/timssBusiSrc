package com.timss.asset.bean;

public class HwLedgerServerBean extends HwLedgerDeviceBean {
	private static final long serialVersionUID = -1514510413474829306L;

	/** 设备型号，关联硬件类型维护中的服务器型号
	 * 
	 * */
	private String serverModel;
	/** SN编码
	 * 
	 * */
	private String snCode;
	/** 服务器品牌，关联硬件类型维护中的服务器品牌
	 * 
	 * */
	private String serverBrand;
	/** 物理处理器型号，关联硬件类型维护中的处理器型号
	 * 
	 * */
	private String cpuModel;
	/** 处理器数量
	 * 
	 * */
	private Integer cpuNum;
	/** 物理内存型号，关联硬件类型维护中的内存型号
	 * 
	 * */
	private String memModel;
	/** 内存数量
	 * 
	 * */
	private Integer memNum;
	/** 物理硬盘型号，关联硬件类型维护中的硬盘型号
	 * 
	 * */
	private String harddiskModel;
	/** 物理硬盘数量
	 * 
	 * */
	private Integer harddiskNum;
	/** HBA型号，关联硬件类型维护中的HBA型号
	 * 
	 * */
	private String hbaModel;
	/** HBA数量
	 * 
	 * */
	private Integer hbaNum;
	/** 服务器网卡型号，关联硬件类型维护中的网卡型号
	 * 
	 * */
	private String netcardModel;
	/** 网卡数量
	 * 
	 * */
	private Integer netcardNum;
	/** 服务器阵列卡，关联硬件类型维护中的阵列卡型号
	 * 
	 * */
	private String raidModel;
	/** 电源功率(KW)
	 * 
	 * */
	private Double power;
	
	private String serverAttr01;
	private String serverAttr02;	
	private String serverAttr03;	
	private String serverAttr04;	
	private String serverAttr05;	
	private String serverAttr06;	
	private String serverAttr07;	
	private String serverAttr08;	
	private String serverAttr09;	
	private String serverAttr10;	
	private String serverAttr11;	
	private String serverAttr12;	
	private String serverAttr13;	
	private String serverAttr14;	
	private String serverAttr15;	
	private String serverAttr16;	
	private String serverAttr17;	
	private String serverAttr18;	
	private String serverAttr19;	
	private String serverAttr20;	
	private String serverLongAttr01;	
	private String serverLongAttr02;	
	private String serverLongAttr03;	
	private String serverLongAttr04;	
	private String serverLongAttr05;	
	private String serverLongAttr06;	
	private String serverLongAttr07;	
	private String serverLongAttr08;	
	private String serverLongAttr09;	
	private String serverLongAttr10;
	
	public String getServerModel() {
		return serverModel;
	}
	public void setServerModel(String serverModel) {
		this.serverModel = serverModel;
	}
	public String getSnCode() {
		return snCode;
	}
	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}
	public String getServerBrand() {
		return serverBrand;
	}
	public void setServerBrand(String serverBrand) {
		this.serverBrand = serverBrand;
	}
	public String getCpuModel() {
		return cpuModel;
	}
	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}
	public Integer getCpuNum() {
		return cpuNum;
	}
	public void setCpuNum(Integer cpuNum) {
		this.cpuNum = cpuNum;
	}
	public String getMemModel() {
		return memModel;
	}
	public void setMemModel(String memModel) {
		this.memModel = memModel;
	}
	public Integer getMemNum() {
		return memNum;
	}
	public void setMemNum(Integer memNum) {
		this.memNum = memNum;
	}
	public String getHarddiskModel() {
		return harddiskModel;
	}
	public void setHarddiskModel(String harddiskModel) {
		this.harddiskModel = harddiskModel;
	}
	public Integer getHarddiskNum() {
		return harddiskNum;
	}
	public void setHarddiskNum(Integer harddiskNum) {
		this.harddiskNum = harddiskNum;
	}
	public String getHbaModel() {
		return hbaModel;
	}
	public void setHbaModel(String hbaModel) {
		this.hbaModel = hbaModel;
	}
	public Integer getHbaNum() {
		return hbaNum;
	}
	public void setHbaNum(Integer hbaNum) {
		this.hbaNum = hbaNum;
	}
	public String getNetcardModel() {
		return netcardModel;
	}
	public void setNetcardModel(String netcardModel) {
		this.netcardModel = netcardModel;
	}
	public Integer getNetcardNum() {
		return netcardNum;
	}
	public void setNetcardNum(Integer netcardNum) {
		this.netcardNum = netcardNum;
	}
	public String getRaidModel() {
		return raidModel;
	}
	public void setRaidModel(String raidModel) {
		this.raidModel = raidModel;
	}
	public Double getPower() {
		return power;
	}
	public void setPower(Double power) {
		this.power = power;
	}
	public String getServerAttr01() {
		return serverAttr01;
	}
	public void setServerAttr01(String serverAttr01) {
		this.serverAttr01 = serverAttr01;
	}
	public String getServerAttr02() {
		return serverAttr02;
	}
	public void setServerAttr02(String serverAttr02) {
		this.serverAttr02 = serverAttr02;
	}
	public String getServerAttr03() {
		return serverAttr03;
	}
	public void setServerAttr03(String serverAttr03) {
		this.serverAttr03 = serverAttr03;
	}
	public String getServerAttr04() {
		return serverAttr04;
	}
	public void setServerAttr04(String serverAttr04) {
		this.serverAttr04 = serverAttr04;
	}
	public String getServerAttr05() {
		return serverAttr05;
	}
	public void setServerAttr05(String serverAttr05) {
		this.serverAttr05 = serverAttr05;
	}
	public String getServerAttr06() {
		return serverAttr06;
	}
	public void setServerAttr06(String serverAttr06) {
		this.serverAttr06 = serverAttr06;
	}
	public String getServerAttr07() {
		return serverAttr07;
	}
	public void setServerAttr07(String serverAttr07) {
		this.serverAttr07 = serverAttr07;
	}
	public String getServerAttr08() {
		return serverAttr08;
	}
	public void setServerAttr08(String serverAttr08) {
		this.serverAttr08 = serverAttr08;
	}
	public String getServerAttr09() {
		return serverAttr09;
	}
	public void setServerAttr09(String serverAttr09) {
		this.serverAttr09 = serverAttr09;
	}
	public String getServerAttr10() {
		return serverAttr10;
	}
	public void setServerAttr10(String serverAttr10) {
		this.serverAttr10 = serverAttr10;
	}
	public String getServerAttr11() {
		return serverAttr11;
	}
	public void setServerAttr11(String serverAttr11) {
		this.serverAttr11 = serverAttr11;
	}
	public String getServerAttr12() {
		return serverAttr12;
	}
	public void setServerAttr12(String serverAttr12) {
		this.serverAttr12 = serverAttr12;
	}
	public String getServerAttr13() {
		return serverAttr13;
	}
	public void setServerAttr13(String serverAttr13) {
		this.serverAttr13 = serverAttr13;
	}
	public String getServerAttr14() {
		return serverAttr14;
	}
	public void setServerAttr14(String serverAttr14) {
		this.serverAttr14 = serverAttr14;
	}
	public String getServerAttr15() {
		return serverAttr15;
	}
	public void setServerAttr15(String serverAttr15) {
		this.serverAttr15 = serverAttr15;
	}
	public String getServerAttr16() {
		return serverAttr16;
	}
	public void setServerAttr16(String serverAttr16) {
		this.serverAttr16 = serverAttr16;
	}
	public String getServerAttr17() {
		return serverAttr17;
	}
	public void setServerAttr17(String serverAttr17) {
		this.serverAttr17 = serverAttr17;
	}
	public String getServerAttr18() {
		return serverAttr18;
	}
	public void setServerAttr18(String serverAttr18) {
		this.serverAttr18 = serverAttr18;
	}
	public String getServerAttr19() {
		return serverAttr19;
	}
	public void setServerAttr19(String serverAttr19) {
		this.serverAttr19 = serverAttr19;
	}
	public String getServerAttr20() {
		return serverAttr20;
	}
	public void setServerAttr20(String serverAttr20) {
		this.serverAttr20 = serverAttr20;
	}
	public String getServerLongAttr01() {
		return serverLongAttr01;
	}
	public void setServerLongAttr01(String serverLongAttr01) {
		this.serverLongAttr01 = serverLongAttr01;
	}
	public String getServerLongAttr02() {
		return serverLongAttr02;
	}
	public void setServerLongAttr02(String serverLongAttr02) {
		this.serverLongAttr02 = serverLongAttr02;
	}
	public String getServerLongAttr03() {
		return serverLongAttr03;
	}
	public void setServerLongAttr03(String serverLongAttr03) {
		this.serverLongAttr03 = serverLongAttr03;
	}
	public String getServerLongAttr04() {
		return serverLongAttr04;
	}
	public void setServerLongAttr04(String serverLongAttr04) {
		this.serverLongAttr04 = serverLongAttr04;
	}
	public String getServerLongAttr05() {
		return serverLongAttr05;
	}
	public void setServerLongAttr05(String serverLongAttr05) {
		this.serverLongAttr05 = serverLongAttr05;
	}
	public String getServerLongAttr06() {
		return serverLongAttr06;
	}
	public void setServerLongAttr06(String serverLongAttr06) {
		this.serverLongAttr06 = serverLongAttr06;
	}
	public String getServerLongAttr07() {
		return serverLongAttr07;
	}
	public void setServerLongAttr07(String serverLongAttr07) {
		this.serverLongAttr07 = serverLongAttr07;
	}
	public String getServerLongAttr08() {
		return serverLongAttr08;
	}
	public void setServerLongAttr08(String serverLongAttr08) {
		this.serverLongAttr08 = serverLongAttr08;
	}
	public String getServerLongAttr09() {
		return serverLongAttr09;
	}
	public void setServerLongAttr09(String serverLongAttr09) {
		this.serverLongAttr09 = serverLongAttr09;
	}
	public String getServerLongAttr10() {
		return serverLongAttr10;
	}
	public void setServerLongAttr10(String serverLongAttr10) {
		this.serverLongAttr10 = serverLongAttr10;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}