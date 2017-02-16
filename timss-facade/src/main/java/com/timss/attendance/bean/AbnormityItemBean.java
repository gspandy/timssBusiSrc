package com.timss.attendance.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 考勤异常申请明细
 */
public class AbnormityItemBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -429086090684634739L;

    /**
     * id
     */
    private int id;
    
    /**
     * 考勤异常类型
     */
    private String category;
        
    /**
     * 加班开始时间
     */
    private Date startDate;
    
    /**
     * 加班结束时间
     */
    private Date endDate;
    
    /**
     * 考勤异常ID
     */
    private int abnormityId;
    
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
    
    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getAbnormityId() {
		return abnormityId;
	}

	public void setAbnormityId(int abnormityId) {
		this.abnormityId = abnormityId;
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
        AbnormityItemBean other = (AbnormityItemBean) obj;
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
        return "OvertimeItemBean [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", userName="
                + userName + ", userId=" + userId + ", deptId=" + deptId + ", deptName=" + deptName + "]";
    }

    
    
}
