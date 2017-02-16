package com.timss.operation.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 生成日历使用的规则历史
 * @description: {desc}
 * @company: gdyd
 * @className: RulesHistory.java
 * @author: fengzt
 * @createDate: 2014年6月13日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class RulesHistory implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7399961289180556644L;
    
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
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 岗位
     */
    private String stationId;
    
    /**
     * 插入时间（现在的时间）
     */
    private Date writeDate;
    
    /**
     * 开始下标（决定datagrid循环的开始）
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

    public int getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(int startFlag) {
        this.startFlag = startFlag;
    }

    public Date getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
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


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + startFlag;
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        result = prime * result + ((writeDate == null) ? 0 : writeDate.hashCode());
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
        RulesHistory other = (RulesHistory) obj;
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
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
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
        if ( uuid == null ) {
            if ( other.uuid != null )
                return false;
        } else if ( !uuid.equals( other.uuid ) )
            return false;
        if ( writeDate == null ) {
            if ( other.writeDate != null )
                return false;
        } else if ( !writeDate.equals( other.writeDate ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RulesHistory [id=" + id + ", uuid=" + uuid + ", name=" + name + ", startTime=" + startTime
                + ", endTime=" + endTime + ", stationId=" + stationId + ", writeDate=" + writeDate + ", startFlag="
                + startFlag + ", siteId=" + siteId + "]";
    }
    
    
    
}
