package com.timss.operation.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 日历表项/排班详情
 */
public class ScheduleDetail implements Serializable {

    private static final long serialVersionUID = 9142033055470086555L;
    
    /**
     * key
     */
    private int id;
    
    /**
     * 时间 (具体到天yyyy-MM-dd)
     */
    private Date dateTime;
    
    private Integer dutyId;
    /**
     * 值别表
     */
    private Duty duty;
    
    private Integer shiftId;
    /**
     * 班次表
     */
    private Shift shift;
    
    private String stationId;
    /**
     * 规则UUID(日历修改的时候用当前时间覆盖)
     */
    private String uuid;
    private String siteId;
    /**
     * 下标
     */
    private int startFlag;

    public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public Integer getDutyId() {
		return dutyId;
	}

	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public int getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(int startFlag) {
        this.startFlag = startFlag;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
        result = prime * result + ((duty == null) ? 0 : duty.hashCode());
        result = prime * result + id;
        result = prime * result + ((shift == null) ? 0 : shift.hashCode());
        result = prime * result + startFlag;
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
        ScheduleDetail other = (ScheduleDetail) obj;
        if ( dateTime == null ) {
            if ( other.dateTime != null )
                return false;
        } else if ( !dateTime.equals( other.dateTime ) )
            return false;
        if ( duty == null ) {
            if ( other.duty != null )
                return false;
        } else if ( !duty.equals( other.duty ) )
            return false;
        if ( id != other.id )
            return false;
        if ( shift == null ) {
            if ( other.shift != null )
                return false;
        } else if ( !shift.equals( other.shift ) )
            return false;
        if ( startFlag != other.startFlag )
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
        return "ScheduleDetail [id=" + id + ", dateTime=" + dateTime + ", duty=" + duty + ", shift=" + shift
                + ", uuid=" + uuid + ", startFlag=" + startFlag + "]";
    }

    
    
    
}
