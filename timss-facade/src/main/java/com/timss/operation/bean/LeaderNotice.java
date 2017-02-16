package com.timss.operation.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 上级领导通知
 * @description: {desc}
 * @company: gdyd
 * @className: LeaderNotice.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class LeaderNotice implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5900585574208943561L;

    /**
     * id
     */
    private int id;
    
    /**
     * 编号，后台自动生成
     */
    private String num;
    
    /**
     * 发布时间
     */
    private Date writeDate;
    
    /**
     * 接话人工号
     */
    private String executeUserId;
    
    /**
     * 领导工号
     */
    private String leaderUserId;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 落实情况
     */
    private String feedBackContent;
    
    /**
     * 站点
     */
    private String siteId;
    
    /**
     * 通知内容更新时间
     */
    private Date updateTime;
    
    /**
     * 落实更新时间
     */
    private Date finishTime;
    
    /**
     * 新建上级通知的工号
     */
    private String writeUserId;
    
    /**
     * 是否已落实的标记
     */
    private String isFinished;
    
    /**
     * 是否已落实的标记
     */
    private String updateUserId;

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public String getWriteUserId() {
        return writeUserId;
    }

    public void setWriteUserId(String writeUserId) {
        this.writeUserId = writeUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Date getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
    }

    public String getExecuteUserId() {
        return executeUserId;
    }

    public void setExecuteUserId(String executeUserId) {
        this.executeUserId = executeUserId;
    }


    public String getLeaderUserId() {
        return leaderUserId;
    }

    public void setLeaderUserId(String leaderUserId) {
        this.leaderUserId = leaderUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getfeedBackContent() {
        return feedBackContent;
    }

    public void setfeedBackContent(String feedBackContent) {
        this.feedBackContent = feedBackContent;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((executeUserId == null) ? 0 : executeUserId.hashCode());
        result = prime * result + ((feedBackContent == null) ? 0 : feedBackContent.hashCode());
        result = prime * result + ((finishTime == null) ? 0 : finishTime.hashCode());
        result = prime * result + id;
        result = prime * result + ((isFinished == null) ? 0 : isFinished.hashCode());
        result = prime * result + ((leaderUserId == null) ? 0 : leaderUserId.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
        result = prime * result + ((updateUserId == null) ? 0 : updateUserId.hashCode());
        result = prime * result + ((writeDate == null) ? 0 : writeDate.hashCode());
        result = prime * result + ((writeUserId == null) ? 0 : writeUserId.hashCode());
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
        LeaderNotice other = (LeaderNotice) obj;
        if ( content == null ) {
            if ( other.content != null )
                return false;
        } else if ( !content.equals( other.content ) )
            return false;
        if ( executeUserId == null ) {
            if ( other.executeUserId != null )
                return false;
        } else if ( !executeUserId.equals( other.executeUserId ) )
            return false;
        if ( feedBackContent == null ) {
            if ( other.feedBackContent != null )
                return false;
        } else if ( !feedBackContent.equals( other.feedBackContent ) )
            return false;
        if ( finishTime == null ) {
            if ( other.finishTime != null )
                return false;
        } else if ( !finishTime.equals( other.finishTime ) )
            return false;
        if ( id != other.id )
            return false;
        if ( isFinished == null ) {
            if ( other.isFinished != null )
                return false;
        } else if ( !isFinished.equals( other.isFinished ) )
            return false;
        if ( leaderUserId == null ) {
            if ( other.leaderUserId != null )
                return false;
        } else if ( !leaderUserId.equals( other.leaderUserId ) )
            return false;
        if ( num == null ) {
            if ( other.num != null )
                return false;
        } else if ( !num.equals( other.num ) )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
            return false;
        if ( updateTime == null ) {
            if ( other.updateTime != null )
                return false;
        } else if ( !updateTime.equals( other.updateTime ) )
            return false;
        if ( updateUserId == null ) {
            if ( other.updateUserId != null )
                return false;
        } else if ( !updateUserId.equals( other.updateUserId ) )
            return false;
        if ( writeDate == null ) {
            if ( other.writeDate != null )
                return false;
        } else if ( !writeDate.equals( other.writeDate ) )
            return false;
        if ( writeUserId == null ) {
            if ( other.writeUserId != null )
                return false;
        } else if ( !writeUserId.equals( other.writeUserId ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LeaderNotice [id=" + id + ", num=" + num + ", writeDate=" + writeDate + ", executeUserId="
                + executeUserId + ", leaderUserId=" + leaderUserId + ", content=" + content + ", feedBackContent="
                + feedBackContent + ", siteId=" + siteId + ", updateTime=" + updateTime + ", finishTime=" + finishTime
                + ", writeUserId=" + writeUserId + ", isFinished=" + isFinished + ", updateUserId=" + updateUserId
                + "]";
    }

    
    
    
    
}
