package com.timss.attendance.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 预定会议室Bean
 * @description: {desc}
 * @company: gdyd
 * @className: MeetingBean.java
 * @author: fengzt
 * @createDate: 2015年3月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class MeetingBean extends ItcMvcBean  {

    /**
     * 
     */
    private static final long serialVersionUID = -6069349038178980306L;
    
    /**
     * ID
     */
    private int id;
    
    /**
     * 编号
     */
    @AutoGen("ATD_MT_NUM_SEQ")
    private String num;
    
    /**
     * 姓名
     */
    private String userName;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 会议室编号
     */
    private String meetingNo;
    
    /**
     * 申请理由
     */
    private String reason;
    
    /**
     * 站点id
     */
    private String siteId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 开始时间
     */
    private Date startDate;
    
    /**
     * 结束时间
     */
    private Date endDate;
    
    /**
     * 创建人
     */
    private String createBy;
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 更新人
     */
    private String updateBy;
    
    /**
     * 更新时间
     */
    private Date updateDate;
    
    /**
     * 申请时长
     */
    private double countTime;
    
    /**
     * 使用部门
     */
    private String useDept;
    
    /**
     * 会议名称
     */
    private String meetingName;
    
    /**
     * 状态
     */
    private String activities;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 是否有领导参加会议 （Y/N）
     */
    private String isLeader;
    

    public String getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(String isLeader) {
        this.isLeader = isLeader;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getMeetingNo() {
        return meetingNo;
    }

    public void setMeetingNo(String meetingNo) {
        this.meetingNo = meetingNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public double getCountTime() {
        return countTime;
    }

    public void setCountTime(double countTime) {
        this.countTime = countTime;
    }

    public String getUseDept() {
        return useDept;
    }

    public void setUseDept(String useDept) {
        this.useDept = useDept;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    @Override
    public String toString() {
        return "MeetingBean [id=" + id + ", num=" + num + ", userName=" + userName + ", deptId=" + deptId
                + ", meetingNo=" + meetingNo + ", reason=" + reason + ", siteId=" + siteId + ", deptName=" + deptName
                + ", startDate=" + startDate + ", endDate=" + endDate + ", createBy=" + createBy + ", createDate="
                + createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate + ", countTime=" + countTime
                + ", useDept=" + useDept + ", meetingName=" + meetingName + ", activities=" + activities + ", remark="
                + remark + ", isLeader=" + isLeader + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activities == null) ? 0 : activities.hashCode());
        long temp;
        temp = Double.doubleToLongBits( countTime );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((createBy == null) ? 0 : createBy.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
        result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + id;
        result = prime * result + ((isLeader == null) ? 0 : isLeader.hashCode());
        result = prime * result + ((meetingName == null) ? 0 : meetingName.hashCode());
        result = prime * result + ((meetingNo == null) ? 0 : meetingNo.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((remark == null) ? 0 : remark.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((updateBy == null) ? 0 : updateBy.hashCode());
        result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
        result = prime * result + ((useDept == null) ? 0 : useDept.hashCode());
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
        MeetingBean other = (MeetingBean) obj;
        if ( activities == null ) {
            if ( other.activities != null )
                return false;
        } else if ( !activities.equals( other.activities ) )
            return false;
        if ( Double.doubleToLongBits( countTime ) != Double.doubleToLongBits( other.countTime ) )
            return false;
        if ( createBy == null ) {
            if ( other.createBy != null )
                return false;
        } else if ( !createBy.equals( other.createBy ) )
            return false;
        if ( createDate == null ) {
            if ( other.createDate != null )
                return false;
        } else if ( !createDate.equals( other.createDate ) )
            return false;
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
        if ( isLeader == null ) {
            if ( other.isLeader != null )
                return false;
        } else if ( !isLeader.equals( other.isLeader ) )
            return false;
        if ( meetingName == null ) {
            if ( other.meetingName != null )
                return false;
        } else if ( !meetingName.equals( other.meetingName ) )
            return false;
        if ( meetingNo == null ) {
            if ( other.meetingNo != null )
                return false;
        } else if ( !meetingNo.equals( other.meetingNo ) )
            return false;
        if ( num == null ) {
            if ( other.num != null )
                return false;
        } else if ( !num.equals( other.num ) )
            return false;
        if ( reason == null ) {
            if ( other.reason != null )
                return false;
        } else if ( !reason.equals( other.reason ) )
            return false;
        if ( remark == null ) {
            if ( other.remark != null )
                return false;
        } else if ( !remark.equals( other.remark ) )
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
        if ( updateBy == null ) {
            if ( other.updateBy != null )
                return false;
        } else if ( !updateBy.equals( other.updateBy ) )
            return false;
        if ( updateDate == null ) {
            if ( other.updateDate != null )
                return false;
        } else if ( !updateDate.equals( other.updateDate ) )
            return false;
        if ( useDept == null ) {
            if ( other.useDept != null )
                return false;
        } else if ( !useDept.equals( other.useDept ) )
            return false;
        if ( userName == null ) {
            if ( other.userName != null )
                return false;
        } else if ( !userName.equals( other.userName ) )
            return false;
        return true;
    }
    
    
    
}
