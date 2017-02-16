package com.timss.asset.vo;

import java.io.Serializable;

/**
 * 
 * @title: 备份信息VO
 * @description: {desc}
 * @company: gdyd
 * @className: BackupVo.java
 * @author: fengzt
 * @createDate: 2014年11月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class BackupVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7807621328949228656L;

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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((isCdpBackup == null) ? 0 : isCdpBackup.hashCode());
        result = prime * result + ((isManualBackup == null) ? 0 : isManualBackup.hashCode());
        result = prime * result + ((isPtlBackup == null) ? 0 : isPtlBackup.hashCode());
        result = prime * result + ((isRemoteBackup == null) ? 0 : isRemoteBackup.hashCode());
        result = prime * result + ((isVtlBackup == null) ? 0 : isVtlBackup.hashCode());
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
        BackupVo other = (BackupVo) obj;
        if ( isCdpBackup == null ) {
            if ( other.isCdpBackup != null )
                return false;
        } else if ( !isCdpBackup.equals( other.isCdpBackup ) )
            return false;
        if ( isManualBackup == null ) {
            if ( other.isManualBackup != null )
                return false;
        } else if ( !isManualBackup.equals( other.isManualBackup ) )
            return false;
        if ( isPtlBackup == null ) {
            if ( other.isPtlBackup != null )
                return false;
        } else if ( !isPtlBackup.equals( other.isPtlBackup ) )
            return false;
        if ( isRemoteBackup == null ) {
            if ( other.isRemoteBackup != null )
                return false;
        } else if ( !isRemoteBackup.equals( other.isRemoteBackup ) )
            return false;
        if ( isVtlBackup == null ) {
            if ( other.isVtlBackup != null )
                return false;
        } else if ( !isVtlBackup.equals( other.isVtlBackup ) )
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "BackupVo [isRemoteBackup=" + isRemoteBackup + ", isVtlBackup=" + isVtlBackup + ", isCdpBackup="
                + isCdpBackup + ", isPtlBackup=" + isPtlBackup + ", isManualBackup=" + isManualBackup + "]";
    }
    
    
    
}
