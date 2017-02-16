package com.timss.operation.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @title: 工种
 * @description: {desc}
 * @company: gdyd
 * @className: Dept.java
 * @author: fengzt
 * @createDate: 2014年7月7日
 * @updateUser: fengzt
 * @version: 1.0
 * @update: yyn 20160219
 */
public class Dept implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String deptId;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 是否可用，废弃
     */
    private String active;
    
    /**
     * 是否可用
     */
    private String isActive;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 更新人
     */
    private String updateBy;
    
    /**
     * 站点
     */
    private String siteId;

    /**
     * 属于该工种的岗位列表
     * @return
     */
    private List<Jobs>jobsList;
    
    public List<Jobs> getJobsList() {
		return jobsList;
	}

	public void setJobsList(List<Jobs> jobsList) {
		this.jobsList = jobsList;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((updateBy == null) ? 0 : updateBy.hashCode());
        result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
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
        Dept other = (Dept) obj;
        if ( active == null ) {
            if ( other.active != null )
                return false;
        } else if ( !active.equals( other.active ) )
            return false;
        if ( deptId == null ) {
            if ( other.deptId != null )
                return false;
        } else if ( !deptId.equals( other.deptId ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
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
        return true;
    }

    @Override
    public String toString() {
        return "Dept [deptId=" + deptId + ", name=" + name + ", active=" + active + ", updateTime=" + updateTime
                + ", updateBy=" + updateBy + ", siteId=" + siteId + "]";
    }
    
    
    
}
