package com.timss.attendance.bean;

import java.util.Date;
import java.util.List;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title:请假申请表
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveBean.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class LeaveBean extends ItcMvcBean{

    /**
     * 
     */
    private static final long serialVersionUID = 551188795065788388L;

    /**
     * id
     */
    private int id;
    
    /**
     * 编号
     */
    @AutoGen("ATD_LE_NUM_SEQ")
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
     * 部门名称
     */
    private String deptName;
    
    /**
     * 申请日期
     */
    private Date createDay;
    
    /**
     * 请假事由
     */
    private String reason;
    
    
    /**
     * 请假合计天数
     */
    private double leaveDays;
    
    /**
     * 站点
     */
    private String siteId;
    
    /**
     * 可享受年假天数
     */
    private double annualDays;
    
    /**
     * 结转年假天数
     */
    private double compensateAnnualDays;
    
    /**
     *剩余年假天数
     */
    private double remainAnnualDays;
    
    /**
     * 已休年假天数
     */
    private double hasAnnualDays;
    
    /**
     * 加班天数
     */
    private double overtimeDays;
    
    /**
     * 结转补休天数
     */
    private double overtimeComDays;
    
    /**
     * 已补休天数
     */
    private double hasOvertimeDays;
    
    /**
     * 入职时间
     */
    private String entryDate;
    
    /**
     * 剩余补休天数
     */
    private double remainOvertimeDays;
    
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
     * 流程ID
     */
    private String instantId;
    
    /**
     * 流程状态
     */
    private String status;
    
    /**
     * 流程当前处理人
     */
    private String currentDealUser;
    
    /**
     * 类别
     */
    private String category;
    /**
     * 所有类别的名称
     */
    private String leaveCategory;
    
    private List<LeaveItemBean>itemList;
    
    public String getLeaveCategory() {
		return leaveCategory;
	}

	public void setLeaveCategory(String leaveCategory) {
		this.leaveCategory = leaveCategory;
	}

	public List<LeaveItemBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<LeaveItemBean> itemList) {
		this.itemList = itemList;
	}

	public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Date getCreateDay() {
        return createDay;
    }

    public void setCreateDay(Date createDay) {
        this.createDay = createDay;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(double leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public double getAnnualDays() {
        return annualDays;
    }

    public void setAnnualDays(double annualDays) {
        this.annualDays = annualDays;
    }

    public double getCompensateAnnualDays() {
        return compensateAnnualDays;
    }

    public void setCompensateAnnualDays(double compensateAnnualDays) {
        this.compensateAnnualDays = compensateAnnualDays;
    }

    public double getRemainAnnualDays() {
        return remainAnnualDays;
    }

    public void setRemainAnnualDays(double remainAnnualDays) {
        this.remainAnnualDays = remainAnnualDays;
    }

    public double getHasAnnualDays() {
        return hasAnnualDays;
    }

    public void setHasAnnualDays(double hasAnnualDays) {
        this.hasAnnualDays = hasAnnualDays;
    }

    public double getOvertimeDays() {
        return overtimeDays;
    }

    public void setOvertimeDays(double overtimeDays) {
        this.overtimeDays = overtimeDays;
    }

    public double getOvertimeComDays() {
        return overtimeComDays;
    }

    public void setOvertimeComDays(double overtimeComDays) {
        this.overtimeComDays = overtimeComDays;
    }

    public double getHasOvertimeDays() {
        return hasOvertimeDays;
    }

    public void setHasOvertimeDays(double hasOvertimeDays) {
        this.hasOvertimeDays = hasOvertimeDays;
    }


    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public double getRemainOvertimeDays() {
        return remainOvertimeDays;
    }

    public void setRemainOvertimeDays(double remainOvertimeDays) {
        this.remainOvertimeDays = remainOvertimeDays;
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

    public String getInstantId() {
        return instantId;
    }

    public void setInstantId(String instantId) {
        this.instantId = instantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentDealUser() {
        return currentDealUser;
    }

    public void setCurrentDealUser(String currentDealUser) {
        this.currentDealUser = currentDealUser;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits( annualDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        temp = Double.doubleToLongBits( compensateAnnualDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((createBy == null) ? 0 : createBy.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((createDay == null) ? 0 : createDay.hashCode());
        result = prime * result + ((currentDealUser == null) ? 0 : currentDealUser.hashCode());
        result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
        result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
        result = prime * result + ((entryDate == null) ? 0 : entryDate.hashCode());
        temp = Double.doubleToLongBits( hasAnnualDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( hasOvertimeDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + id;
        result = prime * result + ((instantId == null) ? 0 : instantId.hashCode());
        temp = Double.doubleToLongBits( leaveDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        temp = Double.doubleToLongBits( overtimeComDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( overtimeDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        temp = Double.doubleToLongBits( remainAnnualDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits( remainOvertimeDays );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((updateBy == null) ? 0 : updateBy.hashCode());
        result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
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
        LeaveBean other = (LeaveBean) obj;
        if ( Double.doubleToLongBits( annualDays ) != Double.doubleToLongBits( other.annualDays ) )
            return false;
        if ( category == null ) {
            if ( other.category != null )
                return false;
        } else if ( !category.equals( other.category ) )
            return false;
        if ( Double.doubleToLongBits( compensateAnnualDays ) != Double.doubleToLongBits( other.compensateAnnualDays ) )
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
        if ( createDay == null ) {
            if ( other.createDay != null )
                return false;
        } else if ( !createDay.equals( other.createDay ) )
            return false;
        if ( currentDealUser == null ) {
            if ( other.currentDealUser != null )
                return false;
        } else if ( !currentDealUser.equals( other.currentDealUser ) )
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
        if ( entryDate == null ) {
            if ( other.entryDate != null )
                return false;
        } else if ( !entryDate.equals( other.entryDate ) )
            return false;
        if ( Double.doubleToLongBits( hasAnnualDays ) != Double.doubleToLongBits( other.hasAnnualDays ) )
            return false;
        if ( Double.doubleToLongBits( hasOvertimeDays ) != Double.doubleToLongBits( other.hasOvertimeDays ) )
            return false;
        if ( id != other.id )
            return false;
        if ( instantId == null ) {
            if ( other.instantId != null )
                return false;
        } else if ( !instantId.equals( other.instantId ) )
            return false;
        if ( Double.doubleToLongBits( leaveDays ) != Double.doubleToLongBits( other.leaveDays ) )
            return false;
        if ( num == null ) {
            if ( other.num != null )
                return false;
        } else if ( !num.equals( other.num ) )
            return false;
        if ( Double.doubleToLongBits( overtimeComDays ) != Double.doubleToLongBits( other.overtimeComDays ) )
            return false;
        if ( Double.doubleToLongBits( overtimeDays ) != Double.doubleToLongBits( other.overtimeDays ) )
            return false;
        if ( reason == null ) {
            if ( other.reason != null )
                return false;
        } else if ( !reason.equals( other.reason ) )
            return false;
        if ( Double.doubleToLongBits( remainAnnualDays ) != Double.doubleToLongBits( other.remainAnnualDays ) )
            return false;
        if ( Double.doubleToLongBits( remainOvertimeDays ) != Double.doubleToLongBits( other.remainOvertimeDays ) )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
            return false;
        if ( status == null ) {
            if ( other.status != null )
                return false;
        } else if ( !status.equals( other.status ) )
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
        if ( userName == null ) {
            if ( other.userName != null )
                return false;
        } else if ( !userName.equals( other.userName ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LeaveBean [id=" + id + ", num=" + num + ", userName=" + userName + ", deptId=" + deptId + ", deptName="
                + deptName + ", createDay=" + createDay + ", reason=" + reason + ", leaveDays=" + leaveDays
                + ", siteId=" + siteId + ", annualDays=" + annualDays + ", compensateAnnualDays="
                + compensateAnnualDays + ", remainAnnualDays=" + remainAnnualDays + ", hasAnnualDays=" + hasAnnualDays
                + ", overtimeDays=" + overtimeDays + ", overtimeComDays=" + overtimeComDays + ", hasOvertimeDays="
                + hasOvertimeDays + ", entryDate=" + entryDate + ", remainOvertimeDays=" + remainOvertimeDays
                + ", createBy=" + createBy + ", createDate=" + createDate + ", updateBy=" + updateBy + ", updateDate="
                + updateDate + ", instantId=" + instantId + ", status=" + status + ", currentDealUser="
                + currentDealUser + ", category=" + category + "]";
    }

    
}
