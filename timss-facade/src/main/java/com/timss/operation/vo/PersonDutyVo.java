package com.timss.operation.vo;

import java.io.Serializable;

/**
 * 
 * @title: 人员值别表
 * @description: {desc}
 * @company: gdyd
 * @className: PersonDutyVo.java
 * @author: fengzt
 * @createDate: 2014年7月11日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PersonDutyVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3023637579933514263L;

    /**
     * 工种ID
     */
    private int jobsId;
    
    /**
     * 工种名称
     */
    private String jobsName;
    
    /**
     * 值别ID
     */
    private int dutyId;
    
    /**
     * 值别名称
     */
    private String dutyName;
    
    /**
     * 岗位ID
     */
    private String stationId;
    
    /**
     * 岗位名称
     */
    private String stationName;
    
    /**
     * 站点
     */
    private String siteId;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public int getJobsId() {
        return jobsId;
    }

    public void setJobsId(int jobsId) {
        this.jobsId = jobsId;
    }

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dutyId;
        result = prime * result + ((dutyName == null) ? 0 : dutyName.hashCode());
        result = prime * result + jobsId;
        result = prime * result + ((jobsName == null) ? 0 : jobsName.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
        result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
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
        PersonDutyVo other = (PersonDutyVo) obj;
        if ( dutyId != other.dutyId )
            return false;
        if ( dutyName == null ) {
            if ( other.dutyName != null )
                return false;
        } else if ( !dutyName.equals( other.dutyName ) )
            return false;
        if ( jobsId != other.jobsId )
            return false;
        if ( jobsName == null ) {
            if ( other.jobsName != null )
                return false;
        } else if ( !jobsName.equals( other.jobsName ) )
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
        if ( stationName == null ) {
            if ( other.stationName != null )
                return false;
        } else if ( !stationName.equals( other.stationName ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PersonDutyVo [jobsId=" + jobsId + ", jobsName=" + jobsName + ", dutyId=" + dutyId + ", dutyName="
                + dutyName + ", stationId=" + stationId + ", stationName=" + stationName + ", siteId=" + siteId + "]";
    }
    
    
    
}
