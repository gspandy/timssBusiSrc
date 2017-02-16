package com.timss.operation.vo;

import java.io.Serializable;

public class ModeListVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -9111986644893524080L;

    /**
     * 工种ID
     */
    private int jobId ;
    
    /**
     * 工种
     */
    private String jobName;
    
    /**
     * 岗位Id
     */
    private String deptId;
    
    /**
     * 岗位
     */
    private String deptName;
    
    /**
     * 站点
     */
    private String siteId;
    
    /**
     * 设置点
     */
    private int checkPoint;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public int getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(int checkPoint) {
        this.checkPoint = checkPoint;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + checkPoint;
        result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
        result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
        result = prime * result + jobId;
        result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
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
        ModeListVo other = (ModeListVo) obj;
        if ( checkPoint != other.checkPoint )
            return false;
        if ( deptId == null ) {
            if ( other.deptId != null )
                return false;
        } else if ( !deptId.equals( other.deptId ) )
            return false;
        if ( deptName == null ) {
            if ( other.deptName != null )
                return false;
        } else if ( !deptName.equals( other.deptName ) )
            return false;
        if ( jobId != other.jobId )
            return false;
        if ( jobName == null ) {
            if ( other.jobName != null )
                return false;
        } else if ( !jobName.equals( other.jobName ) )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ModeListVo [jobId=" + jobId + ", jobName=" + jobName + ", deptId=" + deptId + ", deptName=" + deptName
                + ", siteId=" + siteId + ", checkPoint=" + checkPoint + "]";
    }
    
    
}
