package com.timss.operation.bean;

import java.io.Serializable;

/**
 * 
 * @title: 人员值别表
 * @description: {desc}
 * @company: gdyd
 * @className: PersonJobs.java
 * @author: fengzt
 * @createDate: 2014年7月9日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PersonJobs implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5490958698326186995L;

    /**
     * id
     */
    private int id;
    
    /**
     * 工种ID
     */
    private int jobsId;
    
    /**
     * 员工号
     */
    private String userId;
    
    /**
     * 值别表ID
     */
    private int dutyId;
    
    /**
     * 岗位ID
     */
    private String stationId;
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 站点
     */
    private String siteId;
    
    /**
     * 是否出勤
     */
    private String isPresent;

    public String getSiteId() {
        return siteId;
    }

    public String getIsPresent() {
		return isPresent;
	}

	public void setIsPresent(String isPresent) {
		this.isPresent = isPresent;
	}

	public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobsId() {
        return jobsId;
    }

    public void setJobsId(int jobsId) {
        this.jobsId = jobsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dutyId;
        result = prime * result + id;
        result = prime * result + jobsId;
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
        PersonJobs other = (PersonJobs) obj;
        if ( dutyId != other.dutyId )
            return false;
        if ( id != other.id )
            return false;
        if ( jobsId != other.jobsId )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
            return false;
        if ( stationId == null ) {
            if ( other.stationId != null )
                return false;
        } else if ( !stationId.equals( other.stationId ) )
            return false;
        if ( userId == null ) {
            if ( other.userId != null )
                return false;
        } else if ( !userId.equals( other.userId ) )
            return false;
        if ( userName == null ) {
            if ( other.userName != null )
                return false;
        } else if ( !userName.equals( other.userName ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PersonJobs [id=" + id + ", jobsId=" + jobsId + ", userId=" + userId + ", dutyId=" + dutyId
                + ", stationId=" + stationId + ", userName=" + userName + ", siteId=" + siteId + "]";
    }
    
    
    
    
}
