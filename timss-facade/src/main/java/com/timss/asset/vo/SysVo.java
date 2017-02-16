package com.timss.asset.vo;

import java.io.Serializable;

/**
 * 
 * @title:系统信息VO
 * @description: {desc}
 * @company: gdyd
 * @className: SysVo.java
 * @author: fengzt
 * @createDate: 2014年11月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class SysVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6962737632610155770L;

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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((computerName == null) ? 0 : computerName.hashCode());
        result = prime * result + ((logicCpu == null) ? 0 : logicCpu.hashCode());
        result = prime * result + ((logicHarddisk == null) ? 0 : logicHarddisk.hashCode());
        result = prime * result + ((logicMem == null) ? 0 : logicMem.hashCode());
        result = prime * result + ((os == null) ? 0 : os.hashCode());
        result = prime * result + ((osPath == null) ? 0 : osPath.hashCode());
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
        SysVo other = (SysVo) obj;
        if ( computerName == null ) {
            if ( other.computerName != null )
                return false;
        } else if ( !computerName.equals( other.computerName ) )
            return false;
        if ( logicCpu == null ) {
            if ( other.logicCpu != null )
                return false;
        } else if ( !logicCpu.equals( other.logicCpu ) )
            return false;
        if ( logicHarddisk == null ) {
            if ( other.logicHarddisk != null )
                return false;
        } else if ( !logicHarddisk.equals( other.logicHarddisk ) )
            return false;
        if ( logicMem == null ) {
            if ( other.logicMem != null )
                return false;
        } else if ( !logicMem.equals( other.logicMem ) )
            return false;
        if ( os == null ) {
            if ( other.os != null )
                return false;
        } else if ( !os.equals( other.os ) )
            return false;
        if ( osPath == null ) {
            if ( other.osPath != null )
                return false;
        } else if ( !osPath.equals( other.osPath ) )
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "SysVo [computerName=" + computerName + ", os=" + os + ", osPath=" + osPath + ", logicCpu=" + logicCpu
                + ", logicMem=" + logicMem + ", logicHarddisk=" + logicHarddisk + "]";
    }
    
    
    
    
}
