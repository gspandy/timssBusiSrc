package com.timss.operation.bean;

import java.io.Serializable;

/**
 * 
 * @title: 工种
 * @description: {desc}
 * @company: gdyd
 * @className: Jobs.java
 * @author: fengzt
 * @createDate: 2014年7月9日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class Jobs implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7519866645404377811L;

    /**
     * id
     */
    private int id;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 岗位id
     */
    private String stationId;
    
    /**
     * 等级(枚举表？)
     */
    private int sortType;
    
    /**
     * 站点
     */
    private String siteId;
    /**
     * 是否可用
     */
    private String isActive;
    
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }


    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + sortType;
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
        Jobs other = (Jobs) obj;
        if ( id != other.id )
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
        if ( sortType != other.sortType )
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
        return "Jobs [id=" + id + ", name=" + name + ", stationId=" + stationId + ", sortType=" + sortType
                + ", siteId=" + siteId + "]";
    }
    
    
    
}
