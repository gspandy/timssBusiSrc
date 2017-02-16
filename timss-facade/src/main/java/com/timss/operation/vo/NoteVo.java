package com.timss.operation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 运行记事
 * @description: {desc}
 * @company: gdyd
 * @className: Note.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class NoteVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8280358029271065025L;

    /**
     * id
     */
    private int id;
    
    /**
     * 记事时间(格式如：10:12)
     */
    private String writeTimeStr;
    
    /**
     * 记事时间
     */
    private Date writeTime;
    
    /**
     * 机组号
     */
    private String crewNum;
    
    /**
     * 记事类型
     */
    private String type;
    
    /**
     * 记事内容
     */
    private String content;
    
    /**
     * 记事人（员工号）
     */
    private String createBy;
    
    
    /**
     * 写入时间
     */
    private Date createTime;
    
    /**
     * 更新人
     */
    private String updateBy;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 值别Id
     */
    private int dutyId;
    
    /**
     * 工种Id
     */
    private int jobsId;
    
    /**
     * 交接班ID
     */
    private int handoverId;
    
    /**
     * 值别名称
     */
    private String dutyName;
    
    /**
     * 工种名称
     */
    private String jobsName;
    
    /**
     * 班次名称
     */
    private String shiftName;
    
    /**
     * 岗位名称
     */
    private String stationName;
    
    

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    public int getJobsId() {
        return jobsId;
    }

    public void setJobsId(int jobsId) {
        this.jobsId = jobsId;
    }

    public int getId() {
        return id;
    }

    public String getWriteTimeStr() {
        return writeTimeStr;
    }

    public void setWriteTimeStr(String writeTimeStr) {
        this.writeTimeStr = writeTimeStr;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(Date writeTime) {
        this.writeTime = writeTime;
    }

    public String getCrewNum() {
        return crewNum;
    }

    public void setCrewNum(String crewNum) {
        this.crewNum = crewNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((createBy == null) ? 0 : createBy.hashCode());
        result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
        result = prime * result + ((crewNum == null) ? 0 : crewNum.hashCode());
        result = prime * result + dutyId;
        result = prime * result + ((dutyName == null) ? 0 : dutyName.hashCode());
        result = prime * result + handoverId;
        result = prime * result + id;
        result = prime * result + jobsId;
        result = prime * result + ((jobsName == null) ? 0 : jobsName.hashCode());
        result = prime * result + ((shiftName == null) ? 0 : shiftName.hashCode());
        result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((updateBy == null) ? 0 : updateBy.hashCode());
        result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
        result = prime * result + ((writeTime == null) ? 0 : writeTime.hashCode());
        result = prime * result + ((writeTimeStr == null) ? 0 : writeTimeStr.hashCode());
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
        NoteVo other = (NoteVo) obj;
        if ( content == null ) {
            if ( other.content != null )
                return false;
        } else if ( !content.equals( other.content ) )
            return false;
        if ( createBy == null ) {
            if ( other.createBy != null )
                return false;
        } else if ( !createBy.equals( other.createBy ) )
            return false;
        if ( createTime == null ) {
            if ( other.createTime != null )
                return false;
        } else if ( !createTime.equals( other.createTime ) )
            return false;
        if ( crewNum == null ) {
            if ( other.crewNum != null )
                return false;
        } else if ( !crewNum.equals( other.crewNum ) )
            return false;
        if ( dutyId != other.dutyId )
            return false;
        if ( dutyName == null ) {
            if ( other.dutyName != null )
                return false;
        } else if ( !dutyName.equals( other.dutyName ) )
            return false;
        if ( handoverId != other.handoverId )
            return false;
        if ( id != other.id )
            return false;
        if ( jobsId != other.jobsId )
            return false;
        if ( jobsName == null ) {
            if ( other.jobsName != null )
                return false;
        } else if ( !jobsName.equals( other.jobsName ) )
            return false;
        if ( shiftName == null ) {
            if ( other.shiftName != null )
                return false;
        } else if ( !shiftName.equals( other.shiftName ) )
            return false;
        if ( stationName == null ) {
            if ( other.stationName != null )
                return false;
        } else if ( !stationName.equals( other.stationName ) )
            return false;
        if ( type == null ) {
            if ( other.type != null )
                return false;
        } else if ( !type.equals( other.type ) )
            return false;
        if ( updateBy == null ) {
            if ( other.updateBy != null )
                return false;
        } else if ( !updateBy.equals( other.updateBy ) )
            return false;
        if ( updateTime == null ) {
            if ( other.updateTime != null )
                return false;
        } else if ( !updateTime.equals( other.updateTime ) )
            return false;
        if ( writeTime == null ) {
            if ( other.writeTime != null )
                return false;
        } else if ( !writeTime.equals( other.writeTime ) )
            return false;
        if ( writeTimeStr == null ) {
            if ( other.writeTimeStr != null )
                return false;
        } else if ( !writeTimeStr.equals( other.writeTimeStr ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "NoteVo [id=" + id + ", writeTimeStr=" + writeTimeStr + ", writeTime=" + writeTime + ", crewNum="
                + crewNum + ", type=" + type + ", content=" + content + ", createBy=" + createBy + ", createTime="
                + createTime + ", updateBy=" + updateBy + ", updateTime=" + updateTime + ", dutyId=" + dutyId
                + ", jobsId=" + jobsId + ", handoverId=" + handoverId + ", dutyName=" + dutyName + ", jobsName="
                + jobsName + ", shiftName=" + shiftName + ", stationName=" + stationName + "]";
    }
    
}
