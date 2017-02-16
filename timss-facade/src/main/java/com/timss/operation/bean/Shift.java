package com.timss.operation.bean;

import java.io.Serializable;

/**
 * 
* @ClassName: Shift 
* @Description: 班次表
* @author: huanglw 
* @date: 2014年5月28日
*
 */
public class Shift implements Serializable {

    private static final long serialVersionUID = -3626172284742316516L;
    
    /**
     * key
     */
    private int id;
    
    /**
     * 班次编码
     */
    private String num;
    
    /**
     * 班次名称
     */
    private String name;
    
    /**
     * 开始时间
     */
    private String startTime;
    
    /**
     * 上班时长
     */
    private int longTime;
    
    /**
     * 排序
     */
    private int sortType;
    
    /**
     * 别名
     */
    private String abbName;
    
    /**
     * 站点(如果有站点表换成实体)
     */
    private String siteId;
    
    /**
     * 是否可用
     */
    private String state;

    /**
     * 是否可用
     */
    private String isActive;
    
    /**
     * 部门ID
     */
    private int deptId;
    
    /**
     * 岗位ID
     */
    private String stationId;
    
    /**
     * 班次类型
     */
    private String type;
    

    public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getLongTime() {
        return longTime;
    }

    public void setLongTime(int longTime) {
        this.longTime = longTime;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public String getAbbName() {
        return abbName;
    }

    public void setAbbName(String abbName) {
        this.abbName = abbName;
    }


    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((abbName == null) ? 0 : abbName.hashCode());
        result = prime * result + deptId;
        result = prime * result + id;
        result = prime * result + longTime;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + sortType;
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Shift other = (Shift) obj;
        if ( abbName == null ) {
            if ( other.abbName != null )
                return false;
        } else if ( !abbName.equals( other.abbName ) )
            return false;
        if ( deptId != other.deptId )
            return false;
        if ( id != other.id )
            return false;
        if ( longTime != other.longTime )
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
        if ( startTime == null ) {
            if ( other.startTime != null )
                return false;
        } else if ( !startTime.equals( other.startTime ) )
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
        if ( type == null ) {
            if ( other.type != null )
                return false;
        } else if ( !type.equals( other.type ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Shift [id=" + id + ", num=" + num + ", name=" + name + ", startTime=" + startTime + ", longTime="
                + longTime + ", sortType=" + sortType + ", abbName=" + abbName + ", siteId=" + siteId + ", state="
                + state + ", deptId=" + deptId + ", stationId=" + stationId + ", type=" + type + "]";
    }
    
}
