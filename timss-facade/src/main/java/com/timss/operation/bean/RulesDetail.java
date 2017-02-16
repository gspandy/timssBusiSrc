package com.timss.operation.bean;

import java.io.Serializable;

/**
 * 
* @ClassName: RulesDetail 
* @Description: 规则详细表
* @author: fengzt 
* @date: 2014年5月28日
*
 */
public class RulesDetail implements Serializable {

    private static final long serialVersionUID = 1335277726628324011L;
    
    /**
     * key
     */
    private int id;
    
    /**
     * 天次总数
     */
    private int dayTime;
    
    /**
     * 值别表
     */
    private Duty duty;
    
    /**
     * 班次表
     */
    private Shift shift;
    
    /**
     * 排班规则
     */
    private Rules rules;
    
    /**
     * 部门Id
     */
    private String stationId;

    /**
     * 名称
     */
    private String name;
    
    /**
     * uuid
     */
    private String uuid;
    
    /**
     * 站点
     */
    private String siteId;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDayTime() {
        return dayTime;
    }

    public void setDayTime(int dayTime) {
        this.dayTime = dayTime;
    }

    public Duty getDuty() {
        return duty;
    }

    public void setDuty(Duty duty) {
        this.duty = duty;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dayTime;
        result = prime * result + ((duty == null) ? 0 : duty.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((rules == null) ? 0 : rules.hashCode());
        result = prime * result + ((shift == null) ? 0 : shift.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
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
        RulesDetail other = (RulesDetail) obj;
        if ( dayTime != other.dayTime )
            return false;
        if ( duty == null ) {
            if ( other.duty != null )
                return false;
        } else if ( !duty.equals( other.duty ) )
            return false;
        if ( id != other.id )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( rules == null ) {
            if ( other.rules != null )
                return false;
        } else if ( !rules.equals( other.rules ) )
            return false;
        if ( shift == null ) {
            if ( other.shift != null )
                return false;
        } else if ( !shift.equals( other.shift ) )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
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
        return "RulesDetail [id=" + id + ", dayTime=" + dayTime + ", duty=" + duty + ", shift=" + shift + ", rules="
                + rules + ", stationId=" + stationId + ", name=" + name + ", uuid=" + uuid + ", siteId=" + siteId + "]";
    }

        
    
}
