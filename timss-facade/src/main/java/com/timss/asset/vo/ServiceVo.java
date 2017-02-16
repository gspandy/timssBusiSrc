package com.timss.asset.vo;

import java.io.Serializable;

/**
 * 
 * @title: 服务器硬件信息Vo
 * @description: {desc}
 * @company: gdyd
 * @className: ServiceVo.java
 * @author: fengzt
 * @createDate: 2014年11月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class ServiceVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4577066863541721366L;

    /** 设备型号，关联硬件类型维护中的服务器型号
     * 
     * */
    private String serverModel;
    /** 设备型号，关联硬件类型维护中的服务器型号
     * 
     * */
    private String serverModelName;
    /** SN编码
     * 
     * */
    private String snCode;
    /** 服务器品牌，关联硬件类型维护中的服务器品牌
     * 
     * */
    private String serverBrand;
    /** 服务器品牌，关联硬件类型维护中的服务器品牌
     * 
     * */
    private String serverBrandName;
    /** 物理处理器型号，关联硬件类型维护中的处理器型号
     * 
     * */
    private String cpuModel;
    
    /** 物理处理器型号，关联硬件类型维护中的处理器型号
     * 
     * */
    private String cpuModelName;
    /** 处理器数量
     * 
     * */
    private Integer cpuNum;
    /** 物理内存型号，关联硬件类型维护中的内存型号
     * 
     * */
    private String memModel;
    /** 物理内存型号，关联硬件类型维护中的内存型号
     * 
     * */
    private String memModelName;
    /** 内存数量
     * 
     * */
    private Integer memNum;
    /** 物理硬盘型号，关联硬件类型维护中的硬盘型号
     * 
     * */
    private String harddiskModel;
    /** 物理硬盘型号，关联硬件类型维护中的硬盘型号
     * 
     * */
    private String harddiskModelName;
    /** 物理硬盘数量
     * 
     * */
    private Integer harddiskNum;
    /** HBA型号，关联硬件类型维护中的HBA型号
     * 
     * */
    private String hbaModel;
    /** HBA型号，关联硬件类型维护中的HBA型号
     * 
     * */
    private String hbaModelName;
    /** HBA数量
     * 
     * */
    private Integer hbaNum;
    /** 服务器网卡型号，关联硬件类型维护中的网卡型号
     * 
     * */
    private String netcardModel;
    /** 服务器网卡型号，关联硬件类型维护中的网卡型号
     * 
     * */
    private String netcardModelName;
    /** 网卡数量
     * 
     * */
    private Integer netcardNum;
    /** 服务器阵列卡，关联硬件类型维护中的阵列卡型号
     * 
     * */
    private String raidModel;
    /** 服务器阵列卡，关联硬件类型维护中的阵列卡型号
     * 
     * */
    private String raidModelName;
    /** 电源功率(KW)
     * 
     * */
    private Double power;
    


    public String getServerModel() {
        return serverModel;
    }

    public String getCpuModelName() {
        return cpuModelName;
    }

    public void setCpuModelName(String cpuModelName) {
        this.cpuModelName = cpuModelName;
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

 

    public String getServerModelName() {
        return serverModelName;
    }

    public void setServerModelName(String serverModelName) {
        this.serverModelName = serverModelName;
    }

    public String getServerBrandName() {
        return serverBrandName;
    }

    public void setServerBrandName(String serverBrandName) {
        this.serverBrandName = serverBrandName;
    }

    public String getMemModelName() {
        return memModelName;
    }

    public void setMemModelName(String memModelName) {
        this.memModelName = memModelName;
    }

    public String getHarddiskModelName() {
        return harddiskModelName;
    }

    public void setHarddiskModelName(String harddiskModelName) {
        this.harddiskModelName = harddiskModelName;
    }

    public String getHbaModelName() {
        return hbaModelName;
    }

    public void setHbaModelName(String hbaModelName) {
        this.hbaModelName = hbaModelName;
    }

    public String getNetcardModelName() {
        return netcardModelName;
    }

    public void setNetcardModelName(String netcardModelName) {
        this.netcardModelName = netcardModelName;
    }

    public String getRaidModelName() {
        return raidModelName;
    }

    public void setRaidModelName(String raidModelName) {
        this.raidModelName = raidModelName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cpuModel == null) ? 0 : cpuModel.hashCode());
        result = prime * result + ((cpuModelName == null) ? 0 : cpuModelName.hashCode());
        result = prime * result + ((cpuNum == null) ? 0 : cpuNum.hashCode());
        result = prime * result + ((harddiskModel == null) ? 0 : harddiskModel.hashCode());
        result = prime * result + ((harddiskModelName == null) ? 0 : harddiskModelName.hashCode());
        result = prime * result + ((harddiskNum == null) ? 0 : harddiskNum.hashCode());
        result = prime * result + ((hbaModel == null) ? 0 : hbaModel.hashCode());
        result = prime * result + ((hbaModelName == null) ? 0 : hbaModelName.hashCode());
        result = prime * result + ((hbaNum == null) ? 0 : hbaNum.hashCode());
        result = prime * result + ((memModel == null) ? 0 : memModel.hashCode());
        result = prime * result + ((memModelName == null) ? 0 : memModelName.hashCode());
        result = prime * result + ((memNum == null) ? 0 : memNum.hashCode());
        result = prime * result + ((netcardModel == null) ? 0 : netcardModel.hashCode());
        result = prime * result + ((netcardModelName == null) ? 0 : netcardModelName.hashCode());
        result = prime * result + ((netcardNum == null) ? 0 : netcardNum.hashCode());
        result = prime * result + ((power == null) ? 0 : power.hashCode());
        result = prime * result + ((raidModel == null) ? 0 : raidModel.hashCode());
        result = prime * result + ((raidModelName == null) ? 0 : raidModelName.hashCode());
        result = prime * result + ((serverBrand == null) ? 0 : serverBrand.hashCode());
        result = prime * result + ((serverBrandName == null) ? 0 : serverBrandName.hashCode());
        result = prime * result + ((serverModel == null) ? 0 : serverModel.hashCode());
        result = prime * result + ((serverModelName == null) ? 0 : serverModelName.hashCode());
        result = prime * result + ((snCode == null) ? 0 : snCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        ServiceVo other = (ServiceVo) obj;
        if ( cpuModel == null ) {
            if ( other.cpuModel != null )
                return false;
        } else if ( !cpuModel.equals( other.cpuModel ) )
            return false;
        if ( cpuModelName == null ) {
            if ( other.cpuModelName != null )
                return false;
        } else if ( !cpuModelName.equals( other.cpuModelName ) )
            return false;
        if ( cpuNum == null ) {
            if ( other.cpuNum != null )
                return false;
        } else if ( !cpuNum.equals( other.cpuNum ) )
            return false;
        if ( harddiskModel == null ) {
            if ( other.harddiskModel != null )
                return false;
        } else if ( !harddiskModel.equals( other.harddiskModel ) )
            return false;
        if ( harddiskModelName == null ) {
            if ( other.harddiskModelName != null )
                return false;
        } else if ( !harddiskModelName.equals( other.harddiskModelName ) )
            return false;
        if ( harddiskNum == null ) {
            if ( other.harddiskNum != null )
                return false;
        } else if ( !harddiskNum.equals( other.harddiskNum ) )
            return false;
        if ( hbaModel == null ) {
            if ( other.hbaModel != null )
                return false;
        } else if ( !hbaModel.equals( other.hbaModel ) )
            return false;
        if ( hbaModelName == null ) {
            if ( other.hbaModelName != null )
                return false;
        } else if ( !hbaModelName.equals( other.hbaModelName ) )
            return false;
        if ( hbaNum == null ) {
            if ( other.hbaNum != null )
                return false;
        } else if ( !hbaNum.equals( other.hbaNum ) )
            return false;
        if ( memModel == null ) {
            if ( other.memModel != null )
                return false;
        } else if ( !memModel.equals( other.memModel ) )
            return false;
        if ( memModelName == null ) {
            if ( other.memModelName != null )
                return false;
        } else if ( !memModelName.equals( other.memModelName ) )
            return false;
        if ( memNum == null ) {
            if ( other.memNum != null )
                return false;
        } else if ( !memNum.equals( other.memNum ) )
            return false;
        if ( netcardModel == null ) {
            if ( other.netcardModel != null )
                return false;
        } else if ( !netcardModel.equals( other.netcardModel ) )
            return false;
        if ( netcardModelName == null ) {
            if ( other.netcardModelName != null )
                return false;
        } else if ( !netcardModelName.equals( other.netcardModelName ) )
            return false;
        if ( netcardNum == null ) {
            if ( other.netcardNum != null )
                return false;
        } else if ( !netcardNum.equals( other.netcardNum ) )
            return false;
        if ( power == null ) {
            if ( other.power != null )
                return false;
        } else if ( !power.equals( other.power ) )
            return false;
        if ( raidModel == null ) {
            if ( other.raidModel != null )
                return false;
        } else if ( !raidModel.equals( other.raidModel ) )
            return false;
        if ( raidModelName == null ) {
            if ( other.raidModelName != null )
                return false;
        } else if ( !raidModelName.equals( other.raidModelName ) )
            return false;
        if ( serverBrand == null ) {
            if ( other.serverBrand != null )
                return false;
        } else if ( !serverBrand.equals( other.serverBrand ) )
            return false;
        if ( serverBrandName == null ) {
            if ( other.serverBrandName != null )
                return false;
        } else if ( !serverBrandName.equals( other.serverBrandName ) )
            return false;
        if ( serverModel == null ) {
            if ( other.serverModel != null )
                return false;
        } else if ( !serverModel.equals( other.serverModel ) )
            return false;
        if ( serverModelName == null ) {
            if ( other.serverModelName != null )
                return false;
        } else if ( !serverModelName.equals( other.serverModelName ) )
            return false;
        if ( snCode == null ) {
            if ( other.snCode != null )
                return false;
        } else if ( !snCode.equals( other.snCode ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ServiceVo [serverModel=" + serverModel + ", serverModelName=" + serverModelName + ", snCode=" + snCode
                + ", serverBrand=" + serverBrand + ", serverBrandName=" + serverBrandName + ", cpuModel=" + cpuModel
                + ", cpuModelName=" + cpuModelName + ", cpuNum=" + cpuNum + ", memModel=" + memModel
                + ", memModelName=" + memModelName + ", memNum=" + memNum + ", harddiskModel=" + harddiskModel
                + ", harddiskModelName=" + harddiskModelName + ", harddiskNum=" + harddiskNum + ", hbaModel="
                + hbaModel + ", hbaModelName=" + hbaModelName + ", hbaNum=" + hbaNum + ", netcardModel=" + netcardModel
                + ", netcardModelName=" + netcardModelName + ", netcardNum=" + netcardNum + ", raidModel=" + raidModel
                + ", raidModelName=" + raidModelName + ", power=" + power + "]";
    }
    
    
}
