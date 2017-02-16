package com.timss.attendance.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 请假或者加班 明细
 * @description: {desc}
 * @company: gdyd
 * @className: StatDetailVo.java
 * @author: fengzt
 * @createDate: 2014年11月11日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class StatDetailVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1913183801044406509L;

    /**
     * 工号
     */
    private String userId;
    
    /**
     * 姓名
     */
    private String userName;
    
    /**
     * 开始时间
     */
    private Date startDate;
    
    /**
     * 结束时间
     */
    private Date endDate;
    
    /**
     * 合计天数
     */
    private double countDays;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getCountDays() {
        return countDays;
    }

    public void setCountDays(double countDays) {
        this.countDays = countDays;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits( countDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
        StatDetailVo other = (StatDetailVo) obj;
        if ( Double.doubleToLongBits( countDays ) != Double.doubleToLongBits( other.countDays ) )
            return false;
        if ( endDate == null ) {
            if ( other.endDate != null )
                return false;
        } else if ( !endDate.equals( other.endDate ) )
            return false;
        if ( startDate == null ) {
            if ( other.startDate != null )
                return false;
        } else if ( !startDate.equals( other.startDate ) )
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
        return "StatDetailVo [userId=" + userId + ", userName=" + userName + ", startDate=" + startDate + ", endDate="
                + endDate + ", countDays=" + countDays + "]";
    }
    
    
    
}
