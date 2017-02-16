package com.timss.operation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 交接班VO
 * @description: {desc}
 * @company: gdyd
 * @className: Handover.java
 * @author: huanglw
 * @createDate: 2014年7月22日
 * @updateUser: huanglw
 * @version: 1.0
 */
public class HandoverVo implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = -6251010267071228226L;

    /**
     * id
     */
    private int id;
    
    /**
     *交班情况
     */
    private String nextContent;
    
    /**
     *交班交待
     */
    private String nextRemark;
    
    /**
     *上一班情况(只用于接收表单数据）
     */
    private String lastContent;
    
    /**
     *上一班交待(只用于接收表单数据）
     */
    private String lastRemark;
    
    /**
     * 交班人工号
     */
    private String currentPerson;
    private String currentPersonName;
    /**
     * 接班人工号
     */
    private String nextPerson;
    private String nextPersonName;
    /**
     * 交班人密码
     */
    private String currentUserPassword;
    
    /**
     * 接班人密码
     */
    private String nextUserPassword;
    
    /**
     * 插入时间
     */
    private Date writeDate;
    
    /**
     * 更新时间
     */
    private Date updateDate;
    
    /**
     * 交班值别ID
     */
    private int currentDutyId;
    
    /**
     * 交班班次ID
     */
    private int currentShiftId;
    
    /**
     * 接班值别ID
     */
    private int nextDutyId;
    
    /**
     * 接班班次ID
     */
    private int nextShiftId;
    
    /**
     * 交班班次名称
     */
    private String currentShiftName;
    
    /**
     * 交班值别名称
     */
    private String currentDutyName;
    
    /**
     * 实际当前工种名称
     */
    private String currentDeptName;
    
    /**
     * 接班班次名称
     */
    private String nextShiftName;
    
    /**
     * 下一班班次开始时间
     */
    private String nextShiftStartTime;
    
    /**
     * 当前班开始时间
     */
    private String currentShiftStartTime;
    
    /**
     * 接班值别名称
     */
    private String nextDutyName;
    
    /**
     * 实际下班工种名称
     */
    private String nextDeptName;
    
    /**
     * 工种ID
     */
    private int jobsId;
    
    /**
     * 当前班日历Id
     */
    private int nowScheduleId;
    
    /**
     * 下一班班日历Id
     */
    private int nextScheduleId;
    
    /**
     * 是否已交接
     */
    private String isOver;
    
    /**
     * 下一班次的日期（对应日历）
     */
    private Date nextShiftDate;
    
    /**
     * 当前班次的日期（对应日历）
     */
    private Date nowShiftDate;
    
    /**
     * 进行交接班的岗位（部门）
     */
    private String stationId;
    
    /**
     * 班前会
     */
    private String preShiftMeeting;
    
    /**
     * 班后会
     */
    private String postShiftMeeting;

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

	public String getCurrentPersonName() {
		return currentPersonName;
	}

	public void setCurrentPersonName(String currentPersonName) {
		this.currentPersonName = currentPersonName;
	}

	public String getNextPersonName() {
		return nextPersonName;
	}

	public void setNextPersonName(String nextPersonName) {
		this.nextPersonName = nextPersonName;
	}

	public String getNextShiftStartTime() {
		return nextShiftStartTime;
	}

	public void setNextShiftStartTime(String nextShiftStartTime) {
		this.nextShiftStartTime = nextShiftStartTime;
	}

	public String getCurrentShiftStartTime() {
		return currentShiftStartTime;
	}

	public void setCurrentShiftStartTime(String currentShiftStartTime) {
		this.currentShiftStartTime = currentShiftStartTime;
	}

	public Date getNowShiftDate() {
        return nowShiftDate;
    }

    public void setNowShiftDate(Date nowShiftDate) {
        this.nowShiftDate = nowShiftDate;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Date getNextShiftDate() {
        return nextShiftDate;
    }

    public String getCurrentDeptName() {
		return currentDeptName;
	}

	public void setCurrentDeptName(String currentDeptName) {
		this.currentDeptName = currentDeptName;
	}

	public String getNextDeptName() {
		return nextDeptName;
	}

	public void setNextDeptName(String nextDeptName) {
		this.nextDeptName = nextDeptName;
	}

	public void setNextShiftDate(Date nextShiftDate) {
        this.nextShiftDate = nextShiftDate;
    }

    public String getIsOver() {
        return isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNextContent() {
        return nextContent;
    }

    public void setNextContent(String nextContent) {
        this.nextContent = nextContent;
    }

    public String getNextRemark() {
        return nextRemark;
    }

    public void setNextRemark(String nextRemark) {
        this.nextRemark = nextRemark;
    }

    public String getLastContent() {
        return lastContent;
    }

    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }

    public String getLastRemark() {
        return lastRemark;
    }

    public void setLastRemark(String lastRemark) {
        this.lastRemark = lastRemark;
    }

    public String getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(String currentPerson) {
        this.currentPerson = currentPerson;
    }

    public String getNextPerson() {
        return nextPerson;
    }

    public void setNextPerson(String nextPerson) {
        this.nextPerson = nextPerson;
    }

    public String getCurrentUserPassword() {
        return currentUserPassword;
    }

    public void setCurrentUserPassword(String currentUserPassword) {
        this.currentUserPassword = currentUserPassword;
    }

    public String getNextUserPassword() {
        return nextUserPassword;
    }

    public void setNextUserPassword(String nextUserPassword) {
        this.nextUserPassword = nextUserPassword;
    }

    public Date getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
    }

    public int getCurrentDutyId() {
        return currentDutyId;
    }

    public void setCurrentDutyId(int currentDutyId) {
        this.currentDutyId = currentDutyId;
    }

    public int getCurrentShiftId() {
        return currentShiftId;
    }

    public void setCurrentShiftId(int currentShiftId) {
        this.currentShiftId = currentShiftId;
    }

    public int getNextDutyId() {
        return nextDutyId;
    }

    public void setNextDutyId(int nextDutyId) {
        this.nextDutyId = nextDutyId;
    }

    public int getNextShiftId() {
        return nextShiftId;
    }

    public void setNextShiftId(int nextShiftId) {
        this.nextShiftId = nextShiftId;
    }

    public String getCurrentShiftName() {
        return currentShiftName;
    }

    public void setCurrentShiftName(String currentShiftName) {
        this.currentShiftName = currentShiftName;
    }

    public String getCurrentDutyName() {
        return currentDutyName;
    }

    public void setCurrentDutyName(String currentDutyName) {
        this.currentDutyName = currentDutyName;
    }

    public String getNextShiftName() {
        return nextShiftName;
    }

    public void setNextShiftName(String nextShiftName) {
        this.nextShiftName = nextShiftName;
    }

    public String getNextDutyName() {
        return nextDutyName;
    }

    public void setNextDutyName(String nextDutyName) {
        this.nextDutyName = nextDutyName;
    }

    public int getJobsId() {
        return jobsId;
    }

    public void setJobsId(int jobsId) {
        this.jobsId = jobsId;
    }

    public int getNowScheduleId() {
        return nowScheduleId;
    }

    public void setNowScheduleId(int nowScheduleId) {
        this.nowScheduleId = nowScheduleId;
    }

    public int getNextScheduleId() {
        return nextScheduleId;
    }

    public void setNextScheduleId(int nextScheduleId) {
        this.nextScheduleId = nextScheduleId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentDutyId;
        result = prime * result + ((currentDutyName == null) ? 0 : currentDutyName.hashCode());
        result = prime * result + ((currentPerson == null) ? 0 : currentPerson.hashCode());
        result = prime * result + currentShiftId;
        result = prime * result + ((currentShiftName == null) ? 0 : currentShiftName.hashCode());
        result = prime * result + ((currentUserPassword == null) ? 0 : currentUserPassword.hashCode());
        result = prime * result + id;
        result = prime * result + ((isOver == null) ? 0 : isOver.hashCode());
        result = prime * result + jobsId;
        result = prime * result + ((lastContent == null) ? 0 : lastContent.hashCode());
        result = prime * result + ((lastRemark == null) ? 0 : lastRemark.hashCode());
        result = prime * result + ((nextContent == null) ? 0 : nextContent.hashCode());
        result = prime * result + nextDutyId;
        result = prime * result + ((nextDutyName == null) ? 0 : nextDutyName.hashCode());
        result = prime * result + ((nextPerson == null) ? 0 : nextPerson.hashCode());
        result = prime * result + ((nextRemark == null) ? 0 : nextRemark.hashCode());
        result = prime * result + nextScheduleId;
        result = prime * result + ((nextShiftDate == null) ? 0 : nextShiftDate.hashCode());
        result = prime * result + nextShiftId;
        result = prime * result + ((nextShiftName == null) ? 0 : nextShiftName.hashCode());
        result = prime * result + ((nextUserPassword == null) ? 0 : nextUserPassword.hashCode());
        result = prime * result + nowScheduleId;
        result = prime * result + ((nowShiftDate == null) ? 0 : nowShiftDate.hashCode());
        result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
        result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
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
        HandoverVo other = (HandoverVo) obj;
        if ( currentDutyId != other.currentDutyId )
            return false;
        if ( currentDutyName == null ) {
            if ( other.currentDutyName != null )
                return false;
        } else if ( !currentDutyName.equals( other.currentDutyName ) )
            return false;
        if ( currentPerson == null ) {
            if ( other.currentPerson != null )
                return false;
        } else if ( !currentPerson.equals( other.currentPerson ) )
            return false;
        if ( currentShiftId != other.currentShiftId )
            return false;
        if ( currentShiftName == null ) {
            if ( other.currentShiftName != null )
                return false;
        } else if ( !currentShiftName.equals( other.currentShiftName ) )
            return false;
        if ( currentUserPassword == null ) {
            if ( other.currentUserPassword != null )
                return false;
        } else if ( !currentUserPassword.equals( other.currentUserPassword ) )
            return false;
        if ( id != other.id )
            return false;
        if ( isOver == null ) {
            if ( other.isOver != null )
                return false;
        } else if ( !isOver.equals( other.isOver ) )
            return false;
        if ( jobsId != other.jobsId )
            return false;
        if ( lastContent == null ) {
            if ( other.lastContent != null )
                return false;
        } else if ( !lastContent.equals( other.lastContent ) )
            return false;
        if ( lastRemark == null ) {
            if ( other.lastRemark != null )
                return false;
        } else if ( !lastRemark.equals( other.lastRemark ) )
            return false;
        if ( nextContent == null ) {
            if ( other.nextContent != null )
                return false;
        } else if ( !nextContent.equals( other.nextContent ) )
            return false;
        if ( nextDutyId != other.nextDutyId )
            return false;
        if ( nextDutyName == null ) {
            if ( other.nextDutyName != null )
                return false;
        } else if ( !nextDutyName.equals( other.nextDutyName ) )
            return false;
        if ( nextPerson == null ) {
            if ( other.nextPerson != null )
                return false;
        } else if ( !nextPerson.equals( other.nextPerson ) )
            return false;
        if ( nextRemark == null ) {
            if ( other.nextRemark != null )
                return false;
        } else if ( !nextRemark.equals( other.nextRemark ) )
            return false;
        if ( nextScheduleId != other.nextScheduleId )
            return false;
        if ( nextShiftDate == null ) {
            if ( other.nextShiftDate != null )
                return false;
        } else if ( !nextShiftDate.equals( other.nextShiftDate ) )
            return false;
        if ( nextShiftId != other.nextShiftId )
            return false;
        if ( nextShiftName == null ) {
            if ( other.nextShiftName != null )
                return false;
        } else if ( !nextShiftName.equals( other.nextShiftName ) )
            return false;
        if ( nextUserPassword == null ) {
            if ( other.nextUserPassword != null )
                return false;
        } else if ( !nextUserPassword.equals( other.nextUserPassword ) )
            return false;
        if ( nowScheduleId != other.nowScheduleId )
            return false;
        if ( nowShiftDate == null ) {
            if ( other.nowShiftDate != null )
                return false;
        } else if ( !nowShiftDate.equals( other.nowShiftDate ) )
            return false;
        if ( stationId == null ) {
            if ( other.stationId != null )
                return false;
        } else if ( !stationId.equals( other.stationId ) )
            return false;
        if ( updateDate == null ) {
            if ( other.updateDate != null )
                return false;
        } else if ( !updateDate.equals( other.updateDate ) )
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
        return "HandoverVo [id=" + id + ", nextContent=" + nextContent + ", nextRemark=" + nextRemark
                + ", lastContent=" + lastContent + ", lastRemark=" + lastRemark + ", currentPerson=" + currentPerson
                + ", nextPerson=" + nextPerson + ", currentUserPassword=" + currentUserPassword + ", nextUserPassword="
                + nextUserPassword + ", writeDate=" + writeDate + ", updateDate=" + updateDate + ", currentDutyId="
                + currentDutyId + ", currentShiftId=" + currentShiftId + ", nextDutyId=" + nextDutyId
                + ", nextShiftId=" + nextShiftId + ", currentShiftName=" + currentShiftName + ", currentDutyName="
                + currentDutyName + ", nextShiftName=" + nextShiftName + ", nextDutyName=" + nextDutyName + ", jobsId="
                + jobsId + ", nowScheduleId=" + nowScheduleId + ", nextScheduleId=" + nextScheduleId + ", isOver="
                + isOver + ", nextShiftDate=" + nextShiftDate + ", nowShiftDate=" + nowShiftDate + ", stationId="
                + stationId + "]";
    }
    
    
    
    
}
