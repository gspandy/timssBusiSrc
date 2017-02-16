package com.timss.operation.vo;

import java.io.Serializable;

/**
 * 
 * @title: 排班规则详情页面 VO
 * @description: {desc}
 * @company: gdyd
 * @className: RulesDetailForListVo.java
 * @author: fengzt
 * @createDate: 2014年6月12日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class RulesDetailForListVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2504717782903448052L;

    /**
     * UUID
     */
    private String uuid;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 所在岗位值别
     */
    private String dutyString;
    
    /**
     * 值别数
     */
    private int dutyCount;
    
    /**
     * 班次
     */
    private String shiftString;
    
    /**
     * 班次总数
     */
    private int shiftCount;
    
    /**
     * 岗位
     */
    private String stationId;
    
    /**
     * 行列表ID
     */
    private int rulesId;
    


    public int getRulesId() {
        return rulesId;
    }

    public void setRulesId(int rulesId) {
        this.rulesId = rulesId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDutyString() {
        return dutyString;
    }

    public void setDutyString(String dutyString) {
        this.dutyString = dutyString;
    }

    public int getDutyCount() {
        return dutyCount;
    }

    public void setDutyCount(int dutyCount) {
        this.dutyCount = dutyCount;
    }

    public String getShiftString() {
        return shiftString;
    }

    public void setShiftString(String shiftString) {
        this.shiftString = shiftString;
    }

    public int getShiftCount() {
        return shiftCount;
    }

    public void setShiftCount(int shiftCount) {
        this.shiftCount = shiftCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dutyCount;
        result = prime * result + ((dutyString == null) ? 0 : dutyString.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + rulesId;
        result = prime * result + shiftCount;
        result = prime * result + ((shiftString == null) ? 0 : shiftString.hashCode());
        result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
        RulesDetailForListVo other = (RulesDetailForListVo) obj;
        if ( dutyCount != other.dutyCount )
            return false;
        if ( dutyString == null ) {
            if ( other.dutyString != null )
                return false;
        } else if ( !dutyString.equals( other.dutyString ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( rulesId != other.rulesId )
            return false;
        if ( shiftCount != other.shiftCount )
            return false;
        if ( shiftString == null ) {
            if ( other.shiftString != null )
                return false;
        } else if ( !shiftString.equals( other.shiftString ) )
            return false;
        if ( stationId == null ) {
            if ( other.stationId != null )
                return false;
        } else if ( !stationId.equals( other.stationId ) )
            return false;
        if ( uuid == null ) {
            if ( other.uuid != null )
                return false;
        } else if ( !uuid.equals( other.uuid ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RulesDetailForListVo [uuid=" + uuid + ", name=" + name + ", dutyString=" + dutyString + ", dutyCount="
                + dutyCount + ", shiftString=" + shiftString + ", shiftCount=" + shiftCount + ", stationId="
                + stationId + ", rulesId=" + rulesId + "]";
    }
    
    
    
}
