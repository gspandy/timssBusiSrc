package com.timss.operation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 日历页面传入
 * @description: {desc}
 * @company: gdyd
 * @className: CalendarPageVo.java
 * @author: fengzt
 * @createDate: 2014年6月18日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class CalendarPageVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1495750160947619915L;

    /**
     * 值别ID
     */
    private int dutyId;
    
    /**
     * 班次ID
     */
    private int shiftId;
    
    /**
     * 岗位ID
     */
    private String stationId;
    
    /**
     * 开始时间
     */
    private Date startDate;
    
    /**
     * 结束时间
     */
    private Date endDate;
    
    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }


    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dutyId;
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + shiftId;
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
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
        CalendarPageVo other = (CalendarPageVo) obj;
        if ( dutyId != other.dutyId )
            return false;
        if ( endDate == null ) {
            if ( other.endDate != null )
                return false;
        } else if ( !endDate.equals( other.endDate ) )
            return false;
        if ( shiftId != other.shiftId )
            return false;
        if ( startDate == null ) {
            if ( other.startDate != null )
                return false;
        } else if ( !startDate.equals( other.startDate ) )
            return false;
        if ( stationId == null ) {
            if ( other.stationId != null )
                return false;
        } else if ( !stationId.equals( other.stationId ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CalendarPageVo [dutyId=" + dutyId + ", shiftId=" + shiftId + ", stationId=" + stationId
                + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }
    
    
}
