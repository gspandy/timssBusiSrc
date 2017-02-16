package com.timss.operation.bean;

import java.io.Serializable;

/**
 * 
* @ClassName: Rules 
* @Description: 行列表
* @author: fengzt 
* @date: 2014年5月28日
*
 */
public class Rules implements Serializable {

    private static final long serialVersionUID = 439877747110861539L;
    
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
     * 值别总数
     */
    private int dutyCount;
    
    
    /**
     * 每个月替换日期（eg: 10 表示：周期替换 5月10号 --> 6月10号）
     */
    private int changeLimit;
    
    /**
     * 轮询班次规则
     */
    private String pollSequence;
    
    /**
     * 是否行政班
     */
    private String isXzb;
    
    /**
     * 行政办数
     */
    private int xzCount;
    
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((available == null) ? 0 : available.hashCode());
        result = prime * result + changeLimit;
        result = prime * result + dutyCount;
        result = prime * result + id;
        result = prime * result + ((isXzb == null) ? 0 : isXzb.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + period;
        result = prime * result + ((pollSequence == null) ? 0 : pollSequence.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + xzCount;
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
        Rules other = (Rules) obj;
        if ( available == null ) {
            if ( other.available != null )
                return false;
        } else if ( !available.equals( other.available ) )
            return false;
        if ( changeLimit != other.changeLimit )
            return false;
        if ( dutyCount != other.dutyCount )
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
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
            return false;
        if ( xzCount != other.xzCount )
            return false;
        return true;
    }

    public String getIsXzb() {
        return isXzb;
    }

    public void setIsXzb(String isXzb) {
        this.isXzb = isXzb;
    }

    public int getXzCount() {
        return xzCount;
    }

    public void setXzCount(int xzCount) {
        this.xzCount = xzCount;
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

    public int getDutyCount() {
        return dutyCount;
    }

    public void setDutyCount(int dutyCount) {
        this.dutyCount = dutyCount;
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

    @Override
    public String toString() {
        return "Rules [id=" + id + ", num=" + num + ", name=" + name + ", period=" + period + ", siteId=" + siteId
                + ", available=" + available + ", dutyCount=" + dutyCount + ", changeLimit=" + changeLimit
                + ", pollSequence=" + pollSequence + ", isXzb=" + isXzb + ", xzCount=" + xzCount + "]";
    }
    
    
    
}
