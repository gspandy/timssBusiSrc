package com.timss.operation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 构造日历VO
 * @description: {desc}
 * @company: gdyd
 * @className: CalendarVo.java
 * @author: fengzt
 * @createDate: 2014年6月16日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class CalendarVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5178537385745778760L;
    
    /**
     * id
     */
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 天次
     */
    private int dayTime;
    
    /**
     * 值别ID
     */
    private int dutyId;
    
    /**
     * 班次ID
     */
    private int shiftId;
    
    /**
     * 行列表ID
     */
    private int rulesId;
    
    /**
     * 规则名字
     */
    private String name;
    
    /**
     * 行列表 uuid
     */
    private String uuid;
    
    /**
     * 值别名
     */
    private String dutyName;
    
    /**
     * 班次名称
     */
    private String shiftName;
    
    /**
     * 工种名称
     */
    private String deptName;
    
    /**
     * 班次类型
     */
    private String shiftType;
    
    /**
     * 排序
     */
    private int sortType;
    
    /**
     * 日期
     */
    private Date dateTime;
    
    /**
     * 岗位ID
     */
    private String stationId;
    
    /**
     * 班次startTime
     */
    private String startTime;
    
    /**
     * 班次longTime
     */
    private int longTime;
    
    /**
     * 下标
     */
    private int startFlag;


    public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getLongTime() {
        return longTime;
    }

    public void setLongTime(int longTime) {
        this.longTime = longTime;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public int getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(int startFlag) {
        this.startFlag = startFlag;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public int getDayTime() {
        return dayTime;
    }

    public void setDayTime(int dayTime) {
        this.dayTime = dayTime;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
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

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }


    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
        result = prime * result + dayTime;
        result = prime * result + dutyId;
        result = prime * result + ((dutyName == null) ? 0 : dutyName.hashCode());
        result = prime * result + id;
        result = prime * result + longTime;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + rulesId;
        result = prime * result + shiftId;
        result = prime * result + ((shiftName == null) ? 0 : shiftName.hashCode());
        result = prime * result + ((shiftType == null) ? 0 : shiftType.hashCode());
        result = prime * result + sortType;
        result = prime * result + startFlag;
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
        CalendarVo other = (CalendarVo) obj;
        if ( dateTime == null ) {
            if ( other.dateTime != null )
                return false;
        } else if ( !dateTime.equals( other.dateTime ) )
            return false;
        if ( dayTime != other.dayTime )
            return false;
        if ( dutyId != other.dutyId )
            return false;
        if ( dutyName == null ) {
            if ( other.dutyName != null )
                return false;
        } else if ( !dutyName.equals( other.dutyName ) )
            return false;
        if ( id != other.id )
            return false;
        if ( longTime != other.longTime )
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
        if ( shiftName == null ) {
            if ( other.shiftName != null )
                return false;
        } else if ( !shiftName.equals( other.shiftName ) )
            return false;
        if ( shiftType == null ) {
            if ( other.shiftType != null )
                return false;
        } else if ( !shiftType.equals( other.shiftType ) )
            return false;
        if ( sortType != other.sortType )
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
        return true;
    }

    @Override
    public String toString() {
        return "CalendarVo [id=" + id + ", dayTime=" + dayTime + ", dutyId=" + dutyId + ", shiftId=" + shiftId
                + ", rulesId=" + rulesId + ", name=" + name + ", uuid=" + uuid + ", dutyName=" + dutyName
                + ", shiftName=" + shiftName + ", shiftType=" + shiftType + ", sortType=" + sortType + ", dateTime="
                + dateTime + ", stationId=" + stationId + ", startTime=" + startTime + ", longTime=" + longTime
                + ", startFlag=" + startFlag + "]";
    }
    
    
    
}
