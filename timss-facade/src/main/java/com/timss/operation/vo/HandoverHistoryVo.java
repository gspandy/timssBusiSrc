package com.timss.operation.vo;

import java.io.Serializable;

/**
 * 
* @ClassName: HandoverHistoryVo 
* @Description: 查询日志的交接班记录
* @author: huanglw 
* @updateby:fengzhutai
* @date: 2014年7月28日
* @updateDate : 2015年11月6日
*
 */
/**
 * @author 890200
 *
 */
public class HandoverHistoryVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 779537182368830337L;
    
    /**
     * 值别名称
     */
    private String dutyName;
    
    /**
     * 值别ID
     */
    private int dutyId;
    
    /**
     * 班次ID
     */
    private int shiftId;
    
    /**
     * 班次名称
     */
    private String shiftName;
    
    /**
     * 工种名称
     */
    private String deptName;
    
    /**
     * 值别人员（串）
     */
    private String persons;
    
    /**
     * 交接班状态
     */
    private String isOver;
    
    /**
     * 交班人
     */
    private String lastUserName;
    
    /**
     * 接班人
     */
    private String nextUserName;
    
    /**
     * 交班情况
     */
    private String nextContent;
    
    /**
     * 交班交待
     */
    private String nextRemark;
    
    /**
     * 交接班ID
     */
    private int handoverId;
    
    /**
     * 班前会
     */
    private String preShiftMeeting;
    
    /**
     * 班后会
     */
    private String postShiftMeeting;
    
    /**
     * 当前班日历Id
     */
    private int nowScheduleId;
    

    public int getNowScheduleId() {
		return nowScheduleId;
	}

	public void setNowScheduleId(int nowScheduleId) {
		this.nowScheduleId = nowScheduleId;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getHandoverId() {
        return handoverId;
    }

    public void setHandoverId(int handoverId) {
        this.handoverId = handoverId;
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

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    public String getIsOver() {
        return isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

    public String getLastUserName() {
        return lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public String getNextUserName() {
        return nextUserName;
    }

    public void setNextUserName(String nextUserName) {
        this.nextUserName = nextUserName;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dutyId;
        result = prime * result + ((dutyName == null) ? 0 : dutyName.hashCode());
        result = prime * result + handoverId;
        result = prime * result + ((isOver == null) ? 0 : isOver.hashCode());
        result = prime * result + ((lastUserName == null) ? 0 : lastUserName.hashCode());
        result = prime * result + ((nextContent == null) ? 0 : nextContent.hashCode());
        result = prime * result + ((nextRemark == null) ? 0 : nextRemark.hashCode());
        result = prime * result + ((nextUserName == null) ? 0 : nextUserName.hashCode());
        result = prime * result + ((persons == null) ? 0 : persons.hashCode());
        result = prime * result + shiftId;
        result = prime * result + ((shiftName == null) ? 0 : shiftName.hashCode());
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
        HandoverHistoryVo other = (HandoverHistoryVo) obj;
        if ( dutyId != other.dutyId )
            return false;
        if ( dutyName == null ) {
            if ( other.dutyName != null )
                return false;
        } else if ( !dutyName.equals( other.dutyName ) )
            return false;
        if ( handoverId != other.handoverId )
            return false;
        if ( isOver == null ) {
            if ( other.isOver != null )
                return false;
        } else if ( !isOver.equals( other.isOver ) )
            return false;
        if ( lastUserName == null ) {
            if ( other.lastUserName != null )
                return false;
        } else if ( !lastUserName.equals( other.lastUserName ) )
            return false;
        if ( nextContent == null ) {
            if ( other.nextContent != null )
                return false;
        } else if ( !nextContent.equals( other.nextContent ) )
            return false;
        if ( nextRemark == null ) {
            if ( other.nextRemark != null )
                return false;
        } else if ( !nextRemark.equals( other.nextRemark ) )
            return false;
        if ( nextUserName == null ) {
            if ( other.nextUserName != null )
                return false;
        } else if ( !nextUserName.equals( other.nextUserName ) )
            return false;
        if ( persons == null ) {
            if ( other.persons != null )
                return false;
        } else if ( !persons.equals( other.persons ) )
            return false;
        if ( shiftId != other.shiftId )
            return false;
        if ( shiftName == null ) {
            if ( other.shiftName != null )
                return false;
        } else if ( !shiftName.equals( other.shiftName ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "HandoverHistoryVo [dutyName=" + dutyName + ", dutyId=" + dutyId + ", shiftId=" + shiftId
                + ", shiftName=" + shiftName + ", persons=" + persons + ", isOver=" + isOver + ", lastUserName="
                + lastUserName + ", nextUserName=" + nextUserName + ", nextContent=" + nextContent + ", nextRemark="
                + nextRemark + ", handoverId=" + handoverId + "]";
    }

    
}
