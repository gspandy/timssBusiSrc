package com.timss.operation.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 交接班
 * @description: {desc}
 * @company: gdyd
 * @className: Handover.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
public class Handover implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8519992145737229436L;

    /**
     * id
     */
    private int id;
    
    /**
     *内容
     */
    private String content;
    
    /**
     *交班交待
     */
    private String remark;
    
    /**
     * 交班人
     */
    private String currentPerson;
    
    /**
     * 接班人
     */
    private String nextPerson;
    
    /**
     * 插入时间
     */
    private Date writeDate;
    
    /**
     * 更新ID
     */
    private int updateDate;
    
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

    public String getIsOver() {
        return isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

    public int getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(int updateDate) {
        this.updateDate = updateDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Date getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
    }

    public int getJobsId() {
        return jobsId;
    }

    public void setJobsId(int jobsId) {
        this.jobsId = jobsId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((currentPerson == null) ? 0 : currentPerson.hashCode());
        result = prime * result + id;
        result = prime * result + ((isOver == null) ? 0 : isOver.hashCode());
        result = prime * result + jobsId;
        result = prime * result + ((nextPerson == null) ? 0 : nextPerson.hashCode());
        result = prime * result + nextScheduleId;
        result = prime * result + nowScheduleId;
        result = prime * result + ((remark == null) ? 0 : remark.hashCode());
        result = prime * result + updateDate;
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
        Handover other = (Handover) obj;
        if ( content == null ) {
            if ( other.content != null )
                return false;
        } else if ( !content.equals( other.content ) )
            return false;
        if ( currentPerson == null ) {
            if ( other.currentPerson != null )
                return false;
        } else if ( !currentPerson.equals( other.currentPerson ) )
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
        if ( nextPerson == null ) {
            if ( other.nextPerson != null )
                return false;
        } else if ( !nextPerson.equals( other.nextPerson ) )
            return false;
        if ( nextScheduleId != other.nextScheduleId )
            return false;
        if ( nowScheduleId != other.nowScheduleId )
            return false;
        if ( remark == null ) {
            if ( other.remark != null )
                return false;
        } else if ( !remark.equals( other.remark ) )
            return false;
        if ( updateDate != other.updateDate )
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
        return "Handover [id=" + id + ", content=" + content + ", remark=" + remark + ", currentPerson="
                + currentPerson + ", nextPerson=" + nextPerson + ", writeDate=" + writeDate + ", updateDate="
                + updateDate + ", jobsId=" + jobsId + ", nowScheduleId=" + nowScheduleId + ", nextScheduleId="
                + nextScheduleId + ", isOver=" + isOver + "]";
    }
    
    
    
    
}
