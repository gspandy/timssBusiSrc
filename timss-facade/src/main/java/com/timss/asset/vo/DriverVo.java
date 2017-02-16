package com.timss.asset.vo;

import java.io.Serializable;

/**
 * 
 * @title:存储信息Vo
 * @description: {desc}
 * @company: gdyd
 * @className: DriverVo.java
 * @author: fengzt
 * @createDate: 2014年11月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class DriverVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -663804924602036654L;

    /** 
     * 存储硬件类型，关联硬件类型维护中的存储硬件型号
     * */
    private String storageModel;
    /** 
     * 存储硬件类型，关联硬件类型维护中的存储硬件型号
     * */
    private String storageModelName;
    /** 
     * 服务器存储类型，关联硬件类型维护中的存储类型
     * */
    private String storageType;
    /** 
     * 服务器存储类型，关联硬件类型维护中的存储类型
     * */
    private String storageTypeName;
    /** 
     * SAN-LUN
     * */
    private String sanLun;
    /** 
     * RAID类型
     * */
    private String raidType;
    /** 
     * RAID类型
     * */
    private String raidTypeName;
    /** 
     * LUN大小
     * */
    private String lunNum;
    /** 
     * 数据变化度
     * */
    private String dataChangeDegree;
    
    
    public String getStorageModelName() {
        return storageModelName;
    }
    public void setStorageModelName(String storageModelName) {
        this.storageModelName = storageModelName;
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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataChangeDegree == null) ? 0 : dataChangeDegree.hashCode());
        result = prime * result + ((lunNum == null) ? 0 : lunNum.hashCode());
        result = prime * result + ((raidType == null) ? 0 : raidType.hashCode());
        result = prime * result + ((raidTypeName == null) ? 0 : raidTypeName.hashCode());
        result = prime * result + ((sanLun == null) ? 0 : sanLun.hashCode());
        result = prime * result + ((storageModel == null) ? 0 : storageModel.hashCode());
        result = prime * result + ((storageModelName == null) ? 0 : storageModelName.hashCode());
        result = prime * result + ((storageType == null) ? 0 : storageType.hashCode());
        result = prime * result + ((storageTypeName == null) ? 0 : storageTypeName.hashCode());
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
        DriverVo other = (DriverVo) obj;
        if ( dataChangeDegree == null ) {
            if ( other.dataChangeDegree != null )
                return false;
        } else if ( !dataChangeDegree.equals( other.dataChangeDegree ) )
            return false;
        if ( lunNum == null ) {
            if ( other.lunNum != null )
                return false;
        } else if ( !lunNum.equals( other.lunNum ) )
            return false;
        if ( raidType == null ) {
            if ( other.raidType != null )
                return false;
        } else if ( !raidType.equals( other.raidType ) )
            return false;
        if ( raidTypeName == null ) {
            if ( other.raidTypeName != null )
                return false;
        } else if ( !raidTypeName.equals( other.raidTypeName ) )
            return false;
        if ( sanLun == null ) {
            if ( other.sanLun != null )
                return false;
        } else if ( !sanLun.equals( other.sanLun ) )
            return false;
        if ( storageModel == null ) {
            if ( other.storageModel != null )
                return false;
        } else if ( !storageModel.equals( other.storageModel ) )
            return false;
        if ( storageModelName == null ) {
            if ( other.storageModelName != null )
                return false;
        } else if ( !storageModelName.equals( other.storageModelName ) )
            return false;
        if ( storageType == null ) {
            if ( other.storageType != null )
                return false;
        } else if ( !storageType.equals( other.storageType ) )
            return false;
        if ( storageTypeName == null ) {
            if ( other.storageTypeName != null )
                return false;
        } else if ( !storageTypeName.equals( other.storageTypeName ) )
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "DriverVo [storageModel=" + storageModel + ", storageModelName=" + storageModelName + ", storageType="
                + storageType + ", storageTypeName=" + storageTypeName + ", sanLun=" + sanLun + ", raidType="
                + raidType + ", raidTypeName=" + raidTypeName + ", lunNum=" + lunNum + ", dataChangeDegree="
                + dataChangeDegree + "]";
    }
    
    
}
