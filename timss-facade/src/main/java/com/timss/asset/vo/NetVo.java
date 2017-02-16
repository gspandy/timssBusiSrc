package com.timss.asset.vo;

import java.io.Serializable;

/**'
 * 
 * @title: 网络信息
 * @description: {desc}
 * @company: gdyd
 * @className: NetVo.java
 * @author: fengzt
 * @createDate: 2014年11月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class NetVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2514319964948419719L;

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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + ((isClusterIp == null) ? 0 : isClusterIp.hashCode());
        result = prime * result + ((isPublicNet == null) ? 0 : isPublicNet.hashCode());
        result = prime * result + ((mac == null) ? 0 : mac.hashCode());
        result = prime * result + ((netArea == null) ? 0 : netArea.hashCode());
        result = prime * result + ((vlan == null) ? 0 : vlan.hashCode());
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
        NetVo other = (NetVo) obj;
        if ( ip == null ) {
            if ( other.ip != null )
                return false;
        } else if ( !ip.equals( other.ip ) )
            return false;
        if ( isClusterIp == null ) {
            if ( other.isClusterIp != null )
                return false;
        } else if ( !isClusterIp.equals( other.isClusterIp ) )
            return false;
        if ( isPublicNet == null ) {
            if ( other.isPublicNet != null )
                return false;
        } else if ( !isPublicNet.equals( other.isPublicNet ) )
            return false;
        if ( mac == null ) {
            if ( other.mac != null )
                return false;
        } else if ( !mac.equals( other.mac ) )
            return false;
        if ( netArea == null ) {
            if ( other.netArea != null )
                return false;
        } else if ( !netArea.equals( other.netArea ) )
            return false;
        if ( vlan == null ) {
            if ( other.vlan != null )
                return false;
        } else if ( !vlan.equals( other.vlan ) )
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "NetVo [ip=" + ip + ", isClusterIp=" + isClusterIp + ", mac=" + mac + ", vlan=" + vlan + ", netArea="
                + netArea + ", isPublicNet=" + isPublicNet + "]";
    }
    
    
}
