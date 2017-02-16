package com.timss.asset.vo;

import java.io.Serializable;

/**
 * 
 * @title: 硬件台账多条件查询VO
 * @description: {desc}
 * @company: gdyd
 * @className: MultiSearchVo.java
 * @author: fengzt
 * @createDate: 2015年1月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class MultiSearchVo implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 5706627928684049956L;

    
    /**
     * hwId
     */
    private String hwId;
    
    /**
     * 类型
     */
    private String hwType;
    
    /**
     * 类型(高级查询枚举会加上'_')
     */
    private String _hwType;
    
    /**
     * 名称
     */
    private String hwName;
    
    
    /**
     * ip
     */
    private String ip;
    
    /**
     * model型号
     */
    private String model;
    
    /**
     * toUseTime投入运行时间
     */
    private String toUseTime;

    public String getHwId() {
        return hwId;
    }

    public void setHwId(String hwId) {
        this.hwId = hwId;
    }

    public String getHwType() {
        return hwType;
    }

    public void setHwType(String hwType) {
        this.hwType = hwType;
    }

    public String getHwName() {
        return hwName;
    }

    public void setHwName(String hwName) {
        this.hwName = hwName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getToUseTime() {
        return toUseTime;
    }

    public void setToUseTime(String toUseTime) {
        this.toUseTime = toUseTime;
    }

    public String get_hwType() {
        return _hwType;
    }

    public void set_hwType(String _hwType) {
        this._hwType = _hwType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_hwType == null) ? 0 : _hwType.hashCode());
        result = prime * result + ((hwId == null) ? 0 : hwId.hashCode());
        result = prime * result + ((hwName == null) ? 0 : hwName.hashCode());
        result = prime * result + ((hwType == null) ? 0 : hwType.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + ((model == null) ? 0 : model.hashCode());
        result = prime * result + ((toUseTime == null) ? 0 : toUseTime.hashCode());
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
        MultiSearchVo other = (MultiSearchVo) obj;
        if ( _hwType == null ) {
            if ( other._hwType != null )
                return false;
        } else if ( !_hwType.equals( other._hwType ) )
            return false;
        if ( hwId == null ) {
            if ( other.hwId != null )
                return false;
        } else if ( !hwId.equals( other.hwId ) )
            return false;
        if ( hwName == null ) {
            if ( other.hwName != null )
                return false;
        } else if ( !hwName.equals( other.hwName ) )
            return false;
        if ( hwType == null ) {
            if ( other.hwType != null )
                return false;
        } else if ( !hwType.equals( other.hwType ) )
            return false;
        if ( ip == null ) {
            if ( other.ip != null )
                return false;
        } else if ( !ip.equals( other.ip ) )
            return false;
        if ( model == null ) {
            if ( other.model != null )
                return false;
        } else if ( !model.equals( other.model ) )
            return false;
        if ( toUseTime == null ) {
            if ( other.toUseTime != null )
                return false;
        } else if ( !toUseTime.equals( other.toUseTime ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MultiSearchVo [hwId=" + hwId + ", hwType=" + hwType + ", _hwType=" + _hwType + ", hwName=" + hwName
                + ", ip=" + ip + ", model=" + model + ", toUseTime=" + toUseTime + "]";
    }
    
    
}
