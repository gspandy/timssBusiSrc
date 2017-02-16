package com.timss.attendance.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 加班申请明细
 */
public class OvertimeItemBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -429086090684634739L;

    /**
     * id
     */
    private int id;
    
    /**
     * 实际加班小时
     */
    private double realOverHours;
    
    /**
     * 计划加班小时
     */
    private double planOverHours;

    /**
     * 转补休小时
     */
    private double transferCompensate;
    
    /**
     * 加班开始时间
     */
    private Date startDate;
    
    /**
     * 加班结束时间
     */
    private Date endDate;
    
    /**
     * 加班申请ID
     */
    private int overtimeId;
    
    /**
     * 申请人姓名
     */
    private String userName;
    
    /**
     * 申请人ID
     */
    private String userId;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 备注
     */
    private String remarks;
    
    /**
     * 该用户在开始时间当月加班时长（动态计算）
     */
    private double totalHours;
    
    public double getTransferCompensate() {
		return transferCompensate;
	}

	public void setTransferCompensate(double transferCompensate) {
		this.transferCompensate = transferCompensate;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRealOverHours() {
        return realOverHours;
    }

    public void setRealOverHours(double realOverHours) {
        this.realOverHours = realOverHours;
    }

    public double getPlanOverHours() {
        return planOverHours;
    }

    public void setPlanOverHours(double planOverHours) {
        this.planOverHours = planOverHours;
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

    public int getOvertimeId() {
        return overtimeId;
    }

    public void setOvertimeId(int overtimeId) {
        this.overtimeId = overtimeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
        result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + id;
        result = prime * result + overtimeId;
        long temp;
        temp = Double.doubleToLongBits( planOverHours );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( realOverHours );
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        OvertimeItemBean other = (OvertimeItemBean) obj;
        if ( deptId == null ) {
            if ( other.deptId != null )
                return false;
        } else if ( !deptId.equals( other.deptId ) )
            return false;
        if ( deptName == null ) {
            if ( other.deptName != null )
                return false;
        } else if ( !deptName.equals( other.deptName ) )
            return false;
        if ( endDate == null ) {
            if ( other.endDate != null )
                return false;
        } else if ( !endDate.equals( other.endDate ) )
            return false;
        if ( id != other.id )
            return false;
        if ( overtimeId != other.overtimeId )
            return false;
        if ( Double.doubleToLongBits( planOverHours ) != Double.doubleToLongBits( other.planOverHours ) )
            return false;
        if ( Double.doubleToLongBits( realOverHours ) != Double.doubleToLongBits( other.realOverHours ) )
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
        return "OvertimeItemBean [id=" + id + ", realOverHours=" + realOverHours + ", planOverHours=" + planOverHours
                + ", startDate=" + startDate + ", endDate=" + endDate + ", overtimeId=" + overtimeId + ", userName="
                + userName + ", userId=" + userId + ", deptId=" + deptId + ", deptName=" + deptName + "]";
    }

    
    
}
