package com.timss.operation.bean;

import java.io.Serializable;

/**
 * 
 * @title: 值别表 
 * @description: 
 * @company: gdyd
 * @className: Duty.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class Duty implements Serializable {

    private static final long serialVersionUID = -9217378289289271792L;
    
    /**
     * Id
     */
    private int id;
    
    /**
     * 值别编码
     */
    private String num;
    
    /**
     * 值别名称
     */
    private String name;
    
    /**
     * 排序
     */
    private int sortType;
    
    /**
     * 部门 (如果部门表有了，替换部门实体)
     */
    private int deptId;
    
    /**
     * 站点（如果有站点表，则替换站点实体）
     */
    private String siteId;
    
    /**
     * 岗位（部门是大范围，非这个模块使用）
     */
    private String stationId;
    
    /**
     * 状态信息，用于表示是否删除
     */
    private String state;

    /**
     * 是否可用标志位
     */
    private String isActive;
    
    public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
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
        result = prime * result + deptId;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + sortType;
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
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
        Duty other = (Duty) obj;
        if ( deptId != other.deptId )
            return false;
        if ( id != other.id )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
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
        if ( sortType != other.sortType )
            return false;
        if ( state == null ) {
            if ( other.state != null )
                return false;
        } else if ( !state.equals( other.state ) )
            return false;
        if ( stationId == null ) {
            if ( other.stationId != null )
                return false;
        } else if ( !stationId.equals( other.stationId ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Duty [id=" + id + ", num=" + num + ", name=" + name + ", sortType=" + sortType + ", deptId=" + deptId
                + ", siteId=" + siteId + ", stationId=" + stationId + ", state=" + state + "]";
    }
    
}
