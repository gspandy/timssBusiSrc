package com.timss.attendance.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 考勤统计表
 * @description: {desc}
 * @company: gdyd
 * @className: StatBean.java
 * @author: fengzt
 * @createDate: 2014年9月12日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class StatVo implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = -556307543822522251L;

    /**
     * id
     */
    private int id;
    
    /**
     * 工号
     */
    private String userId;
    
    /**
     * 姓名
     */
    private String userName;
    
    /**
     * 统计年份
     */
    private int yearLeave;
    
    /**
     * 年假
     */
    private double annualLevel;
    
    /**
     * 给转上一年年假天数
     */
    private double annualRemain;
    
    /**
     * 给转上一年补休天数
     */
    private double compensateRemain;
    
    /**
     * 事假
     */
    private double enventLeave;
    
    /**
     * 病假
     */
    private double sickLeave;
    
    /**
     * 婚假
     */
    private double marryLeave;
    
    /**
     * 产假
     */
    private double birthLeave;
    
    /**
     * 其他
     */
    private double otherLeave;
    
    /**
     * 加班天数合计
     */
    private double overTime;
    
    /**
     * 已补休
     */
    private double compensateLeave;
    
    /**
     * 未补休
     */
    private double noCompensateLeave;
    
    /**
     * 创建日期
     */
    private Date createDate;
    
    /**
     * 站点id
     */
    private String siteId;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 请假天数合计
     */
    private double countDays;
    
    /**
     * 流程状态
     */
    private String  status;
    
    /**
     * 用户状态
     */
    private String userStatus;
    
    /**
     * 可享受年假
     */
    private double annual;
    
    /**
     * 开始时间
     */
    private Date startDate;
    
    /**
     * 结束时间
     */
    private Date endDate;
    
    /**
     * 核减年假
     */
    private double subAnualLeave;
    
    /**
     * 核减备注
     */
    private String remark1;
    
    /**
     * 结转备注
     */
    private String remark2;
    
    /**
     * 请假类型8
     */
    private double category_8;
    
    /**
     * 请假类型9
     */
    private double category_9;
    
    /**
     * 请假类型10
     */
    private double category_10;
    
    /**
     * 请假类型11
     */
    private double category_11;
    
    /**
     * 请假类型12
     */
    private double category_12;
    
    /**
     * 请假类型13
     */
    private double category_13;
    
    /**
     * 请假类型14
     */
    private double category_14;
    
    /**
     * 请假类型15
     */
    private double category_15;
    
    /**
     * 请假类型16
     */
    private double category_16;
    
    /**
     * 请假类型17
     */
    private double category_17;
    
    
    

    public double getCategory_8() {
        return category_8;
    }

    public void setCategory_8(double category_8) {
        this.category_8 = category_8;
    }

    public double getCategory_9() {
        return category_9;
    }

    public void setCategory_9(double category_9) {
        this.category_9 = category_9;
    }

    public double getCategory_10() {
        return category_10;
    }

    public void setCategory_10(double category_10) {
        this.category_10 = category_10;
    }

    public double getCategory_11() {
        return category_11;
    }

    public void setCategory_11(double category_11) {
        this.category_11 = category_11;
    }

    public double getCategory_12() {
        return category_12;
    }

    public void setCategory_12(double category_12) {
        this.category_12 = category_12;
    }

    public double getCategory_13() {
        return category_13;
    }

    public void setCategory_13(double category_13) {
        this.category_13 = category_13;
    }

    public double getCategory_14() {
        return category_14;
    }

    public void setCategory_14(double category_14) {
        this.category_14 = category_14;
    }

    public double getCategory_15() {
        return category_15;
    }

    public void setCategory_15(double category_15) {
        this.category_15 = category_15;
    }

    public double getCategory_16() {
        return category_16;
    }

    public void setCategory_16(double category_16) {
        this.category_16 = category_16;
    }

    public double getCategory_17() {
        return category_17;
    }

    public void setCategory_17(double category_17) {
        this.category_17 = category_17;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public double getSubAnualLeave() {
        return subAnualLeave;
    }

    public void setSubAnualLeave(double subAnualLeave) {
        this.subAnualLeave = subAnualLeave;
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

    public double getAnnual() {
        return annual;
    }

    public void setAnnual(double annual) {
        this.annual = annual;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getNoCompensateLeave() {
        return noCompensateLeave;
    }

    public void setNoCompensateLeave(double noCompensateLeave) {
        this.noCompensateLeave = noCompensateLeave;
    }

    public double getCountDays() {
        return countDays;
    }

    public void setCountDays(double countDays) {
        this.countDays = countDays;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getYearLeave() {
        return yearLeave;
    }

    public void setYearLeave(int yearLeave) {
        this.yearLeave = yearLeave;
    }

    public double getAnnualLevel() {
        return annualLevel;
    }

    public void setAnnualLevel(double annualLevel) {
        this.annualLevel = annualLevel;
    }

    public double getAnnualRemain() {
        return annualRemain;
    }

    public void setAnnualRemain(double annualRemain) {
        this.annualRemain = annualRemain;
    }

    public double getCompensateRemain() {
        return compensateRemain;
    }

    public void setCompensateRemain(double compensateRemain) {
        this.compensateRemain = compensateRemain;
    }

    public double getEnventLeave() {
        return enventLeave;
    }

    public void setEnventLeave(double enventLeave) {
        this.enventLeave = enventLeave;
    }

    public double getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(double sickLeave) {
        this.sickLeave = sickLeave;
    }

    public double getMarryLeave() {
        return marryLeave;
    }

    public void setMarryLeave(double marryLeave) {
        this.marryLeave = marryLeave;
    }

    public double getBirthLeave() {
        return birthLeave;
    }

    public void setBirthLeave(double birthLeave) {
        this.birthLeave = birthLeave;
    }

    public double getOtherLeave() {
        return otherLeave;
    }

    public void setOtherLeave(double otherLeave) {
        this.otherLeave = otherLeave;
    }

    public double getOverTime() {
        return overTime;
    }

    public void setOverTime(double overTime) {
        this.overTime = overTime;
    }

    public double getCompensateLeave() {
        return compensateLeave;
    }

    public void setCompensateLeave(double compensateLeave) {
        this.compensateLeave = compensateLeave;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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
        long temp;
        temp = Double.doubleToLongBits( annual );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( annualLevel );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( annualRemain );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( birthLeave );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( category_10 );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( category_11 );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( category_12 );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( category_13 );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( category_14 );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( category_15 );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( category_16 );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( category_17 );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( category_8 );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( category_9 );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( compensateLeave );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( compensateRemain );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( countDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
        result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        temp = Double.doubleToLongBits( enventLeave );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + id;
        temp = Double.doubleToLongBits( marryLeave );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( noCompensateLeave );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( otherLeave );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( overTime );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((remark1 == null) ? 0 : remark1.hashCode());
        result = prime * result + ((remark2 == null) ? 0 : remark2.hashCode());
        temp = Double.doubleToLongBits( sickLeave );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        temp = Double.doubleToLongBits( subAnualLeave );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        result = prime * result + ((userStatus == null) ? 0 : userStatus.hashCode());
        result = prime * result + yearLeave;
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
        StatVo other = (StatVo) obj;
        if ( Double.doubleToLongBits( annual ) != Double.doubleToLongBits( other.annual ) )
            return false;
        if ( Double.doubleToLongBits( annualLevel ) != Double.doubleToLongBits( other.annualLevel ) )
            return false;
        if ( Double.doubleToLongBits( annualRemain ) != Double.doubleToLongBits( other.annualRemain ) )
            return false;
        if ( Double.doubleToLongBits( birthLeave ) != Double.doubleToLongBits( other.birthLeave ) )
            return false;
        if ( Double.doubleToLongBits( category_10 ) != Double.doubleToLongBits( other.category_10 ) )
            return false;
        if ( Double.doubleToLongBits( category_11 ) != Double.doubleToLongBits( other.category_11 ) )
            return false;
        if ( Double.doubleToLongBits( category_12 ) != Double.doubleToLongBits( other.category_12 ) )
            return false;
        if ( Double.doubleToLongBits( category_13 ) != Double.doubleToLongBits( other.category_13 ) )
            return false;
        if ( Double.doubleToLongBits( category_14 ) != Double.doubleToLongBits( other.category_14 ) )
            return false;
        if ( Double.doubleToLongBits( category_15 ) != Double.doubleToLongBits( other.category_15 ) )
            return false;
        if ( Double.doubleToLongBits( category_16 ) != Double.doubleToLongBits( other.category_16 ) )
            return false;
        if ( Double.doubleToLongBits( category_17 ) != Double.doubleToLongBits( other.category_17 ) )
            return false;
        if ( Double.doubleToLongBits( category_8 ) != Double.doubleToLongBits( other.category_8 ) )
            return false;
        if ( Double.doubleToLongBits( category_9 ) != Double.doubleToLongBits( other.category_9 ) )
            return false;
        if ( Double.doubleToLongBits( compensateLeave ) != Double.doubleToLongBits( other.compensateLeave ) )
            return false;
        if ( Double.doubleToLongBits( compensateRemain ) != Double.doubleToLongBits( other.compensateRemain ) )
            return false;
        if ( Double.doubleToLongBits( countDays ) != Double.doubleToLongBits( other.countDays ) )
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
        if ( Double.doubleToLongBits( enventLeave ) != Double.doubleToLongBits( other.enventLeave ) )
            return false;
        if ( id != other.id )
            return false;
        if ( Double.doubleToLongBits( marryLeave ) != Double.doubleToLongBits( other.marryLeave ) )
            return false;
        if ( Double.doubleToLongBits( noCompensateLeave ) != Double.doubleToLongBits( other.noCompensateLeave ) )
            return false;
        if ( Double.doubleToLongBits( otherLeave ) != Double.doubleToLongBits( other.otherLeave ) )
            return false;
        if ( Double.doubleToLongBits( overTime ) != Double.doubleToLongBits( other.overTime ) )
            return false;
        if ( remark1 == null ) {
            if ( other.remark1 != null )
                return false;
        } else if ( !remark1.equals( other.remark1 ) )
            return false;
        if ( remark2 == null ) {
            if ( other.remark2 != null )
                return false;
        } else if ( !remark2.equals( other.remark2 ) )
            return false;
        if ( Double.doubleToLongBits( sickLeave ) != Double.doubleToLongBits( other.sickLeave ) )
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
        if ( status == null ) {
            if ( other.status != null )
                return false;
        } else if ( !status.equals( other.status ) )
            return false;
        if ( Double.doubleToLongBits( subAnualLeave ) != Double.doubleToLongBits( other.subAnualLeave ) )
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
        if ( userStatus == null ) {
            if ( other.userStatus != null )
                return false;
        } else if ( !userStatus.equals( other.userStatus ) )
            return false;
        if ( yearLeave != other.yearLeave )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StatVo [id=" + id + ", userId=" + userId + ", userName=" + userName + ", yearLeave=" + yearLeave
                + ", annualLevel=" + annualLevel + ", annualRemain=" + annualRemain + ", compensateRemain="
                + compensateRemain + ", enventLeave=" + enventLeave + ", sickLeave=" + sickLeave + ", marryLeave="
                + marryLeave + ", birthLeave=" + birthLeave + ", otherLeave=" + otherLeave + ", overTime=" + overTime
                + ", compensateLeave=" + compensateLeave + ", noCompensateLeave=" + noCompensateLeave + ", createDate="
                + createDate + ", siteId=" + siteId + ", deptId=" + deptId + ", deptName=" + deptName + ", countDays="
                + countDays + ", status=" + status + ", userStatus=" + userStatus + ", annual=" + annual
                + ", startDate=" + startDate + ", endDate=" + endDate + ", subAnualLeave=" + subAnualLeave
                + ", remark1=" + remark1 + ", remark2=" + remark2 + ", category_8=" + category_8 + ", category_9="
                + category_9 + ", category_10=" + category_10 + ", category_11=" + category_11 + ", category_12="
                + category_12 + ", category_13=" + category_13 + ", category_14=" + category_14 + ", category_15="
                + category_15 + ", category_16=" + category_16 + ", category_17=" + category_17 + "]";
    }
    
    
    
}
