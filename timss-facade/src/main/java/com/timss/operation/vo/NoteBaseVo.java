package com.timss.operation.vo;

import java.io.Serializable;

/**
 * 
 * @title: 基础数据form
 * @description: {desc}
 * @company: gdyd
 * @className: NoteBaseVo.java
 * @author: fengzt
 * @createDate: 2015年11月5日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class NoteBaseVo implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4324788894532160665L;

    /**
     * 日期
     */
    private String dateTime;
    
    /**
     * 当值
     */
    private String dutyName;
    
    /**
     * 当前值别Id
     */
    private int currentDutyId;
    
    /**
     * 当前班次Id
     */
    private int currenShiftId;
    
    /**
     * 当前班次Id
     */
    private int nowScheduleId;
    
    /**
     * 当前交接班id
     */
    private Integer currentHandoverId;
    
    /**
     * 动态表单KEY
     */
    private String keyword;
    
    /**
     * 值班人员
     */
    private String dutyPerson;
    
    /**
     * 上一班情况
     */
    private String lastContent_base;
    
    /**
     * 上一班交待
     */
    private String lastRemark_base;

    /**
     * 班前会
     */
    private String preShiftMeeting;
    
    /**
     * 班后会
     */
    private String postShiftMeeting;
    
    public Integer getCurrentHandoverId() {
		return currentHandoverId;
	}

	public void setCurrentHandoverId(Integer currentHandoverId) {
		this.currentHandoverId = currentHandoverId;
	}

	public String getPreShiftMeeting() {
		return preShiftMeeting;
	}

	public void setPreShiftMeeting(String preShiftMeeting) {
		this.preShiftMeeting = preShiftMeeting;
	}

	public String getPostShiftMeeting() {
		return postShiftMeeting;
	}

	public void setPostShiftMeeting(String postShiftMeeting) {
		this.postShiftMeeting = postShiftMeeting;
	}

	public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public int getCurrentDutyId() {
        return currentDutyId;
    }

    public void setCurrentDutyId(int currentDutyId) {
        this.currentDutyId = currentDutyId;
    }

    public int getCurrenShiftId() {
        return currenShiftId;
    }

    public void setCurrenShiftId(int currenShiftId) {
        this.currenShiftId = currenShiftId;
    }

    public int getNowScheduleId() {
        return nowScheduleId;
    }

    public void setNowScheduleId(int nowScheduleId) {
        this.nowScheduleId = nowScheduleId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDutyPerson() {
        return dutyPerson;
    }

    public void setDutyPerson(String dutyPerson) {
        this.dutyPerson = dutyPerson;
    }

    public String getLastContent_base() {
        return lastContent_base;
    }

    public void setLastContent_base(String lastContent_base) {
        this.lastContent_base = lastContent_base;
    }

    public String getLastRemark_base() {
        return lastRemark_base;
    }

    public void setLastRemark_base(String lastRemark_base) {
        this.lastRemark_base = lastRemark_base;
    }
    
    
}
