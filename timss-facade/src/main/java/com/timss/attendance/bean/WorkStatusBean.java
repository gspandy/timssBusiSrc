package com.timss.attendance.bean;

import java.util.Date;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 员工出勤情况
 * @description: {desc}
 * @company: gdyd
 * @className: WorkStatusBean.java
 * @author: fengzt
 * @createDate: 2015年6月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class WorkStatusBean extends ItcMvcBean {

    
    /**
     * 
     */
    private static final long serialVersionUID = -7324497995201870726L;

    /**
     * 使用组件的uuid
     */
    @UUIDGen(requireType=GenerationType.REQUIRED_NEW)
    private String id;
    /**
     * 工号
     */
    private String userId;
    /**
     * 名字
     */
    private String userName;

    /**
     * 日期( yyyy-MM-dd )
     */
    private String workDate;
    
    /**
     * 用日期+"_"+用户id标识一条记录
     * 用于map的key
     */
    private String flag;
    
    /**
     * 上午上班打卡情况
     */
    private String mornStartCheck;
    
    /**
     * 上午下班打卡情况
     */
    private String mornEndCheck;
    
    /**
     * 下午上班打卡情况
     */
    private String noonStartCheck;
    
    /**
     * 下午下班打卡情况
     */
    private String noonEndCheck;

    /**
     * 上午上班打卡情况类型
     */
    private String mornStartCheckType;
    
    /**
     * 上午下班打卡情况类型
     */
    private String mornEndCheckType;
    
    /**
     * 下午上班打卡情况类型
     */
    private String noonStartCheckType;
    
    /**
     * 下午下班打卡情况类型
     */
    private String noonEndCheckType;

    /**
     * 上午上班打卡情况数据来源
     */
    private String mornStartCheckSrc;
    
    /**
     * 上午下班打卡情况数据来源
     */
    private String mornEndCheckSrc;
    
    /**
     * 下午上班打卡情况数据来源
     */
    private String noonStartCheckSrc;
    
    /**
     * 下午下班打卡情况数据来源
     */
    private String noonEndCheckSrc;
    
    /**
     * 上午上班打卡时间
     */
    private String mornStartTime;
    
    /**
     * 上午下班打卡时间
     */
    private String mornEndTime;
    
    /**
     * 下午上班打卡时间
     */
    private String noonStartTime;
    
    /**
     * 下午下班打卡时间
     */
    private String noonEndTime;
    
    /**
     * 状态( 0 --- 休息; 1 or null -- 正常上班 )
     */
    private String status;
  
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
     * 站点id
     */
    private String siteId;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 是否运行组人员（ 针对电厂运行人员 ）
     */
    private String isOpr;
    /**
     * 运行人员排班id
     */
    private Integer oprScheduleId;
    private Integer dutyId;
	private String dutyName;
	private Integer shiftId;
	private String shiftName;
	private String startTime;//班次的开始时间
	private Integer longTime;//班次的持续时间
	private String shiftType;//班次的类型
	
    public Integer getDutyId() {
		return dutyId;
	}
	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Integer getLongTime() {
		return longTime;
	}
	public void setLongTime(Integer longTime) {
		this.longTime = longTime;
	}
	public String getShiftType() {
		return shiftType;
	}
	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}
	public Integer getOprScheduleId() {
		return oprScheduleId;
	}
	public void setOprScheduleId(Integer oprScheduleId) {
		this.oprScheduleId = oprScheduleId;
	}
	public String getMornStartCheckType() {
		return mornStartCheckType;
	}
	public void setMornStartCheckType(String mornStartCheckType) {
		this.mornStartCheckType = mornStartCheckType;
	}
	public String getMornEndCheckType() {
		return mornEndCheckType;
	}
	public void setMornEndCheckType(String mornEndCheckType) {
		this.mornEndCheckType = mornEndCheckType;
	}
	public String getNoonStartCheckType() {
		return noonStartCheckType;
	}
	public void setNoonStartCheckType(String noonStartCheckType) {
		this.noonStartCheckType = noonStartCheckType;
	}
	public String getNoonEndCheckType() {
		return noonEndCheckType;
	}
	public void setNoonEndCheckType(String noonEndCheckType) {
		this.noonEndCheckType = noonEndCheckType;
	}
	public String getMornStartCheckSrc() {
		return mornStartCheckSrc;
	}
	public void setMornStartCheckSrc(String mornStartCheckSrc) {
		this.mornStartCheckSrc = mornStartCheckSrc;
	}
	public String getMornEndCheckSrc() {
		return mornEndCheckSrc;
	}
	public void setMornEndCheckSrc(String mornEndCheckSrc) {
		this.mornEndCheckSrc = mornEndCheckSrc;
	}
	public String getNoonStartCheckSrc() {
		return noonStartCheckSrc;
	}
	public void setNoonStartCheckSrc(String noonStartCheckSrc) {
		this.noonStartCheckSrc = noonStartCheckSrc;
	}
	public String getNoonEndCheckSrc() {
		return noonEndCheckSrc;
	}
	public void setNoonEndCheckSrc(String noonEndCheckSrc) {
		this.noonEndCheckSrc = noonEndCheckSrc;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getIsOpr() {
        return isOpr;
    }
    public void setIsOpr(String isOpr) {
        this.isOpr = isOpr;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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
    public String getWorkDate() {
        return workDate;
    }
    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMornStartCheck() {
        return mornStartCheck;
    }
    public void setMornStartCheck(String mornStartCheck) {
        this.mornStartCheck = mornStartCheck;
    }
    public String getMornEndCheck() {
        return mornEndCheck;
    }
    public void setMornEndCheck(String mornEndCheck) {
        this.mornEndCheck = mornEndCheck;
    }
    public String getNoonStartCheck() {
        return noonStartCheck;
    }
    public void setNoonStartCheck(String noonStartCheck) {
        this.noonStartCheck = noonStartCheck;
    }
    public String getNoonEndCheck() {
        return noonEndCheck;
    }
    public void setNoonEndCheck(String noonEndCheck) {
        this.noonEndCheck = noonEndCheck;
    }
    public String getMornStartTime() {
        return mornStartTime;
    }
    public void setMornStartTime(String mornStartTime) {
        this.mornStartTime = mornStartTime;
    }
    public String getMornEndTime() {
        return mornEndTime;
    }
    public void setMornEndTime(String mornEndTime) {
        this.mornEndTime = mornEndTime;
    }
    public String getNoonStartTime() {
        return noonStartTime;
    }
    public void setNoonStartTime(String noonStartTime) {
        this.noonStartTime = noonStartTime;
    }
    public String getNoonEndTime() {
        return noonEndTime;
    }
    public void setNoonEndTime(String noonEndTime) {
        this.noonEndTime = noonEndTime;
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
    public String getDeptId() {
        return deptId;
    }
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createBy == null) ? 0 : createBy.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
        result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isOpr == null) ? 0 : isOpr.hashCode());
        result = prime * result + ((mornEndCheck == null) ? 0 : mornEndCheck.hashCode());
        result = prime * result + ((mornEndTime == null) ? 0 : mornEndTime.hashCode());
        result = prime * result + ((mornStartCheck == null) ? 0 : mornStartCheck.hashCode());
        result = prime * result + ((mornStartTime == null) ? 0 : mornStartTime.hashCode());
        result = prime * result + ((noonEndCheck == null) ? 0 : noonEndCheck.hashCode());
        result = prime * result + ((noonEndTime == null) ? 0 : noonEndTime.hashCode());
        result = prime * result + ((noonStartCheck == null) ? 0 : noonStartCheck.hashCode());
        result = prime * result + ((noonStartTime == null) ? 0 : noonStartTime.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((updateBy == null) ? 0 : updateBy.hashCode());
        result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        result = prime * result + ((workDate == null) ? 0 : workDate.hashCode());
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
        WorkStatusBean other = (WorkStatusBean) obj;
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
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        if ( isOpr == null ) {
            if ( other.isOpr != null )
                return false;
        } else if ( !isOpr.equals( other.isOpr ) )
            return false;
        if ( mornEndCheck == null ) {
            if ( other.mornEndCheck != null )
                return false;
        } else if ( !mornEndCheck.equals( other.mornEndCheck ) )
            return false;
        if ( mornEndTime == null ) {
            if ( other.mornEndTime != null )
                return false;
        } else if ( !mornEndTime.equals( other.mornEndTime ) )
            return false;
        if ( mornStartCheck == null ) {
            if ( other.mornStartCheck != null )
                return false;
        } else if ( !mornStartCheck.equals( other.mornStartCheck ) )
            return false;
        if ( mornStartTime == null ) {
            if ( other.mornStartTime != null )
                return false;
        } else if ( !mornStartTime.equals( other.mornStartTime ) )
            return false;
        if ( noonEndCheck == null ) {
            if ( other.noonEndCheck != null )
                return false;
        } else if ( !noonEndCheck.equals( other.noonEndCheck ) )
            return false;
        if ( noonEndTime == null ) {
            if ( other.noonEndTime != null )
                return false;
        } else if ( !noonEndTime.equals( other.noonEndTime ) )
            return false;
        if ( noonStartCheck == null ) {
            if ( other.noonStartCheck != null )
                return false;
        } else if ( !noonStartCheck.equals( other.noonStartCheck ) )
            return false;
        if ( noonStartTime == null ) {
            if ( other.noonStartTime != null )
                return false;
        } else if ( !noonStartTime.equals( other.noonStartTime ) )
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
        if ( workDate == null ) {
            if ( other.workDate != null )
                return false;
        } else if ( !workDate.equals( other.workDate ) )
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "WorkStatusBean [id=" + id + ", userId=" + userId + ", userName=" + userName + ", workDate=" + workDate
                + ", mornStartCheck=" + mornStartCheck + ", mornEndCheck=" + mornEndCheck + ", noonStartCheck="
                + noonStartCheck + ", noonEndCheck=" + noonEndCheck + ", mornStartTime=" + mornStartTime
                + ", mornEndTime=" + mornEndTime + ", noonStartTime=" + noonStartTime + ", noonEndTime=" + noonEndTime
                + ", status=" + status + ", createBy=" + createBy + ", createDate=" + createDate + ", updateBy="
                + updateBy + ", updateDate=" + updateDate + ", siteId=" + siteId + ", deptName=" + deptName
                + ", deptId=" + deptId + ", isOpr=" + isOpr + "]";
    }
    
    
    
}
