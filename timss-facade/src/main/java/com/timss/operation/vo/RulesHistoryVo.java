package com.timss.operation.vo;

import java.io.Serializable;

/**
 * 
 * @title: 规则历史vo
 * @description: {desc}
 * @company: gdyd
 * @className: RulesHistoryVo.java
 * @author: fengzt
 * @createDate: 2014年6月13日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class RulesHistoryVo implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1963494150884420230L;


    /**
     * id
     */
    private int id;


    /**
     * uuid
     */
    private String uuid;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 开始时间
     */
    private String startTime;
    
    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 岗位
     */
    private String stationId;
    
    /**
     * 岗位名称
     */
    private String stationName;
    
    /**
     * 开始的下标
     */
    private int startFlag;
    
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

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(int startFlag) {
        this.startFlag = startFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + startFlag;
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
        result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
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
        RulesHistoryVo other = (RulesHistoryVo) obj;
        if ( endTime == null ) {
            if ( other.endTime != null )
                return false;
        } else if ( !endTime.equals( other.endTime ) )
            return false;
        if ( id != other.id )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( startFlag != other.startFlag )
            return false;
        if ( startTime == null ) {
            if ( other.startTime != null )
                return false;
        } else if ( !startTime.equals( other.startTime ) )
            return false;
        if ( stationId == null ) {
            if ( other.stationId != null )
                return false;
        } else if ( !stationId.equals( other.stationId ) )
            return false;
        if ( stationName == null ) {
            if ( other.stationName != null )
                return false;
        } else if ( !stationName.equals( other.stationName ) )
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
        return "RulesHistoryVo [id=" + id + ", uuid=" + uuid + ", name=" + name + ", startTime=" + startTime
                + ", endTime=" + endTime + ", stationId=" + stationId + ", stationName=" + stationName + ", startFlag="
                + startFlag + "]";
    }
    
    
    
}
