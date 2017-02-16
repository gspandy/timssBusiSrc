package com.timss.attendance.vo;

import java.io.Serializable;

/**
 * 
 * @title: 请假申请附表
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveItemBean.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class LeaveItemVo implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 7838643213965613284L;

    /**
     * id
     */
    private int id;
    
    /**
     * 请假类别
     */
    private String category;
    
    /**
     * 请假天数
     */
    private double leaveDays;
    
    /**
     * 请假开始时间
     */
    private String startDate;
    
    /**
     * 请假结束时间
     */
    private String endDate;
    
    /**
     * 请假申请ID
     */
    private int leaveId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(double leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + id;
        long temp;
        temp = Double.doubleToLongBits( leaveDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + leaveId;
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
        LeaveItemVo other = (LeaveItemVo) obj;
        if ( category == null ) {
            if ( other.category != null )
                return false;
        } else if ( !category.equals( other.category ) )
            return false;
        if ( endDate == null ) {
            if ( other.endDate != null )
                return false;
        } else if ( !endDate.equals( other.endDate ) )
            return false;
        if ( id != other.id )
            return false;
        if ( Double.doubleToLongBits( leaveDays ) != Double.doubleToLongBits( other.leaveDays ) )
            return false;
        if ( leaveId != other.leaveId )
            return false;
        if ( startDate == null ) {
            if ( other.startDate != null )
                return false;
        } else if ( !startDate.equals( other.startDate ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LeaveItemVo [id=" + id + ", category=" + category + ", leaveDays=" + leaveDays + ", startDate="
                + startDate + ", endDate=" + endDate + ", leaveId=" + leaveId + "]";
    }

    
}
