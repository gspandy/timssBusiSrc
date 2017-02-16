package com.timss.operation.vo;

import java.io.Serializable;

/**
 * 
 * @title: 规则详情Vo
 * @description: {desc}
 * @company: gdyd
 * @className: RulesDetailVo.java
 * @author: fengzt
 * @createDate: 2014年6月27日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class RulesDetailVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4935503319724028334L;
    
    /**
     * id
     */
    private String id;
    
    /**
     * 值别ID
     */
    private int dutyId;

    /**
     * 天次
     */
    private int dayTime;
    
    /**
     * 班次ID
     */
    private int shiftId;
    
    /**
     * 行列表ID
     */
    private int rulesId;
    
    /**
     * 岗位
     */
    private String stationId;
    
    /**
     * UUID
     */
    private String uuid;
    
    /**
     * 名称
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public int getDayTime() {
        return dayTime;
    }

    public void setDayTime(int dayTime) {
        this.dayTime = dayTime;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dayTime;
        result = prime * result + dutyId;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + rulesId;
        result = prime * result + shiftId;
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
        RulesDetailVo other = (RulesDetailVo) obj;
        if ( dayTime != other.dayTime )
            return false;
        if ( dutyId != other.dutyId )
            return false;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( rulesId != other.rulesId )
            return false;
        if ( shiftId != other.shiftId )
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
        return "RulesDetailVo [id=" + id + ", dutyId=" + dutyId + ", dayTime=" + dayTime + ", shiftId=" + shiftId
                + ", rulesId=" + rulesId + ", stationId=" + stationId + ", uuid=" + uuid + ", name=" + name + "]";
    }
    
    
   
    
  
}
