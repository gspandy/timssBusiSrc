package com.timss.operation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 用于存储人员、值班、班次关系的vo
 * 表示一个日期某个值别中人员的排班情况
 * @author 890147
 */
public class DutyPersonShiftVo implements Serializable {
	private static final long serialVersionUID = 1748481454238236976L;
	
	private String userId;
	private String userName;
	private Integer dutyId;
	private String dutyName;
	private Date dateTime;
	private Integer shiftId;
	private String shiftName;
	private String startTime;//班次的开始时间
	private Integer longTime;//班次的持续时间
	private String shiftType;//班次的类型
	private String siteId;
	private String flag;//日期_用户id 为map的key
	private Integer scheduleId;//当班id
	
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getShiftType() {
		return shiftType;
	}
	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
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
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
}