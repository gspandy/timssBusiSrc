package com.timss.operation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 角色表(岗位key --> role_id)
 * @description: {desc}
 * @company: gdyd
 * @className: role.java
 * @author: fengzt
 * @createDate: 2014年6月24日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class RoleVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3094696944272892415L;

    /**
     * 角色ID
     */
    private String roleId;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 是否可用
     */
    private String active;
    
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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
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
        RoleVo other = (RoleVo) obj;
        if ( active == null ) {
            if ( other.active != null )
                return false;
        } else if ( !active.equals( other.active ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( roleId == null ) {
            if ( other.roleId != null )
                return false;
        } else if ( !roleId.equals( other.roleId ) )
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
        return "Role [roleId=" + roleId + ", name=" + name + ", active=" + active + ", updateTime=" + updateTime
                + ", updateBy=" + updateBy + ", siteId=" + siteId + "]";
    }
    
    
}
