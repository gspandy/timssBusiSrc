package com.timss.operation.vo;

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
public class LeaderNoticeVo implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = -807637939285214188L;

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
     * 接话人名字
     */
    private String executeUserName;
    
    /**
     * 领导工号
     */
    private String leaderUserId;
    
    /**
     * 领导名字
     */
    private String leaderUserName;
    
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
     * 创建人userId
     */
    private String writeUserId;
    
    /**
     * 创建人名字
     */
    private String writeUserName;
    
    /**
     * 是否已落实的标记
     */
    private String isFinished;
    
    /**
     * 是否已落实的标记
     */
    private String updateUserId;
    
    

    public String getWriteUserName() {
        return writeUserName;
    }

    public void setWriteUserName(String writeUserName) {
        this.writeUserName = writeUserName;
    }

    public String getExecuteUserName() {
        return executeUserName;
    }

    public void setExecuteUserName(String executeUserName) {
        this.executeUserName = executeUserName;
    }

    public String getLeaderUserName() {
        return leaderUserName;
    }

    public void setLeaderUserName(String leaderUserName) {
        this.leaderUserName = leaderUserName;
    }

    public String getFeedBackContent() {
        return feedBackContent;
    }

    public void setFeedBackContent(String feedBackContent) {
        this.feedBackContent = feedBackContent;
    }

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
        result = prime * result + ((executeUserName == null) ? 0 : executeUserName.hashCode());
        result = prime * result + ((feedBackContent == null) ? 0 : feedBackContent.hashCode());
        result = prime * result + ((finishTime == null) ? 0 : finishTime.hashCode());
        result = prime * result + id;
        result = prime * result + ((isFinished == null) ? 0 : isFinished.hashCode());
        result = prime * result + ((leaderUserId == null) ? 0 : leaderUserId.hashCode());
        result = prime * result + ((leaderUserName == null) ? 0 : leaderUserName.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
        result = prime * result + ((updateUserId == null) ? 0 : updateUserId.hashCode());
        result = prime * result + ((writeDate == null) ? 0 : writeDate.hashCode());
        result = prime * result + ((writeUserId == null) ? 0 : writeUserId.hashCode());
        result = prime * result + ((writeUserName == null) ? 0 : writeUserName.hashCode());
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
        LeaderNoticeVo other = (LeaderNoticeVo) obj;
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
        if ( executeUserName == null ) {
            if ( other.executeUserName != null )
                return false;
        } else if ( !executeUserName.equals( other.executeUserName ) )
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
        if ( leaderUserName == null ) {
            if ( other.leaderUserName != null )
                return false;
        } else if ( !leaderUserName.equals( other.leaderUserName ) )
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
        if ( writeUserName == null ) {
            if ( other.writeUserName != null )
                return false;
        } else if ( !writeUserName.equals( other.writeUserName ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LeaderNoticeVo [id=" + id + ", num=" + num + ", writeDate=" + writeDate + ", executeUserId="
                + executeUserId + ", executeUserName=" + executeUserName + ", leaderUserId=" + leaderUserId
                + ", leaderUserName=" + leaderUserName + ", content=" + content + ", feedBackContent="
                + feedBackContent + ", siteId=" + siteId + ", updateTime=" + updateTime + ", finishTime=" + finishTime
                + ", writeUserId=" + writeUserId + ", writeUserName=" + writeUserName + ", isFinished=" + isFinished
                + ", updateUserId=" + updateUserId + "]";
    }

    
    
    
    
}
