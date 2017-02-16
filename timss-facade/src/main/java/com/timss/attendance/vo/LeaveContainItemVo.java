package com.timss.attendance.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 包含请假 + 子项的vo
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveContainItemVo.java
 * @author: fengzt
 * @createDate: 2015年6月12日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class LeaveContainItemVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8482940157214294746L;

    /**
     * id
     */
    private int id;
    
    /**
     * 编号
     */
    private String num;
    
    
    /**
     * 姓名
     */
    private String userName;
    
    
    /**
     * 创建人
     */
    private String createBy;
    
    
    /**
     * 类别
     */
    private String category;
    
    /**
     * 请假天数
     */
    private double leaveDays;
    /**
     * 请假天数（原始数据）特殊用途
     */
    private double originalLeaveDays;
    /**
     * 请假开始时间
     */
    private Date startDate;
    
    /**
     * 请假结束时间
     */
    private Date endDate;
    
    /**
     * 站点
     */
    private String siteId;

    
    public double getOriginalLeaveDays() {
		return originalLeaveDays;
	}

	public void setOriginalLeaveDays(double originalLeaveDays) {
		this.originalLeaveDays = originalLeaveDays;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((createBy == null) ? 0 : createBy.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + id;
        long temp;
        temp = Double.doubleToLongBits( leaveDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
        LeaveContainItemVo other = (LeaveContainItemVo) obj;
        if ( category == null ) {
            if ( other.category != null )
                return false;
        } else if ( !category.equals( other.category ) )
            return false;
        if ( createBy == null ) {
            if ( other.createBy != null )
                return false;
        } else if ( !createBy.equals( other.createBy ) )
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
        if ( startDate == null ) {
            if ( other.startDate != null )
                return false;
        } else if ( !startDate.equals( other.startDate ) )
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
        return "LeaveContainItemVo [id=" + id + ", num=" + num + ", userName=" + userName + ", createBy=" + createBy
                + ", category=" + category + ", leaveDays=" + leaveDays + ", startDate=" + startDate + ", endDate="
                + endDate + ", siteId=" + siteId + "]";
    }
    
    
    
}
