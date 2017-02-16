package com.timss.operation.vo;

import java.io.Serializable;
/**
 * 
 * @title: 规则页面formVo
 * @description: {desc}
 * @company: gdyd
 * @className: RulesFormVo.java
 * @author: fengzt
 * @createDate: 2014年6月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class RulesFormVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2986867516615447763L;

    /**
     * key
     */
    private int id;
    
    /**
     * 编码
     */
    private String num;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 值别总数
     */
    private String dutyCount;
    
    /**
     * 值别
     */
    private String dutyString;
    
    /**
     * 周期
     */
    private int period;
    
    /**
     * 站点
     */
    private String siteId;
    
    /**
     * 是否可用（生效）
     */
    private String available;
    
    
    /**
     * 每个月替换日期（eg: 10 表示：周期替换 5月10号 --> 6月10号）
     */
    private int changeLimit;
    
    /**
     * 轮询班次规则
     */
    private String pollSequence;
    
    /**
     * 排班班次
     */
    private String classesList;
    
    /**
     * 行政班
     */
    private String isXzb;
    
    /**
     * 行政班数
     */
    private String xzCount;
    
    /**
     * 规则详情名称
     */
    private String rulesDetailName;
    
    /**
     * 岗位
     */
    private String stationId;
    
    /**
     * 岗位名称
     */
    private String stationName;

    public String getDutyString() {
		return dutyString;
	}

	public void setDutyString(String dutyString) {
		this.dutyString = dutyString;
	}

	public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    public String getDutyCount() {
        return dutyCount;
    }

    public void setDutyCount(String dutyCount) {
        this.dutyCount = dutyCount;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }



    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public int getChangeLimit() {
        return changeLimit;
    }

    public void setChangeLimit(int changeLimit) {
        this.changeLimit = changeLimit;
    }

    public String getPollSequence() {
        return pollSequence;
    }

    public void setPollSequence(String pollSequence) {
        this.pollSequence = pollSequence;
    }

    public String getClassesList() {
        return classesList;
    }

    public void setClassesList(String classesList) {
        this.classesList = classesList;
    }

    public String getIsXzb() {
        return isXzb;
    }

    public void setIsXzb(String isXzb) {
        this.isXzb = isXzb;
    }

    public String getXzCount() {
        return xzCount;
    }

    public void setXzCount(String xzCount) {
        this.xzCount = xzCount;
    }

    public String getRulesDetailName() {
        return rulesDetailName;
    }

    public void setRulesDetailName(String rulesDetailName) {
        this.rulesDetailName = rulesDetailName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((available == null) ? 0 : available.hashCode());
        result = prime * result + changeLimit;
        result = prime * result + ((classesList == null) ? 0 : classesList.hashCode());
        result = prime * result + ((dutyCount == null) ? 0 : dutyCount.hashCode());
        result = prime * result + id;
        result = prime * result + ((isXzb == null) ? 0 : isXzb.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + period;
        result = prime * result + ((pollSequence == null) ? 0 : pollSequence.hashCode());
        result = prime * result + ((rulesDetailName == null) ? 0 : rulesDetailName.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
        result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
        result = prime * result + ((xzCount == null) ? 0 : xzCount.hashCode());
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
        RulesFormVo other = (RulesFormVo) obj;
        if ( available == null ) {
            if ( other.available != null )
                return false;
        } else if ( !available.equals( other.available ) )
            return false;
        if ( changeLimit != other.changeLimit )
            return false;
        if ( classesList == null ) {
            if ( other.classesList != null )
                return false;
        } else if ( !classesList.equals( other.classesList ) )
            return false;
        if ( dutyCount == null ) {
            if ( other.dutyCount != null )
                return false;
        } else if ( !dutyCount.equals( other.dutyCount ) )
            return false;
        if ( id != other.id )
            return false;
        if ( isXzb == null ) {
            if ( other.isXzb != null )
                return false;
        } else if ( !isXzb.equals( other.isXzb ) )
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
        if ( period != other.period )
            return false;
        if ( pollSequence == null ) {
            if ( other.pollSequence != null )
                return false;
        } else if ( !pollSequence.equals( other.pollSequence ) )
            return false;
        if ( rulesDetailName == null ) {
            if ( other.rulesDetailName != null )
                return false;
        } else if ( !rulesDetailName.equals( other.rulesDetailName ) )
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
        if ( xzCount == null ) {
            if ( other.xzCount != null )
                return false;
        } else if ( !xzCount.equals( other.xzCount ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RulesFormVo [id=" + id + ", num=" + num + ", name=" + name + ", dutyCount=" + dutyCount + ", period="
                + period + ", siteId=" + siteId + ", available=" + available + ", changeLimit=" + changeLimit
                + ", pollSequence=" + pollSequence + ", classesList=" + classesList + ", isXzb=" + isXzb + ", xzCount="
                + xzCount + ", rulesDetailName=" + rulesDetailName + ", stationId=" + stationId + ", stationName="
                + stationName + "]";
    }
    
    
}
