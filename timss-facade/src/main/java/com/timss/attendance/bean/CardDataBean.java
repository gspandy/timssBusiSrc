package com.timss.attendance.bean;

import java.io.Serializable;
import java.util.Date;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;

/**
 * 
 * @title: 考勤机数据
 * @description: {desc}
 * @company: gdyd
 * @className: CarddataBean.java
 * @author: fengzt
 * @createDate: 2015年6月3日
 * @updateUser: yyn
 * @version: 1.0
 */
public class CardDataBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2426788952579431261L;
    
    /**
     * 使用组件的uuid
     */
    @UUIDGen(requireType=GenerationType.REQUIRED_NEW)
    private String id;

    /**
     * 打卡时间
     */
    private String checkDate;
    /**
     * 打卡人
     */
    private String userId;
    /**
     * 打卡人名字
     */
    private String userName;
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
     * 数据来源考勤机id
     */
    private String amId;
    
    private Boolean isOpr;//是否运行人员
    
    private String oprStartTime;//运行人员上班时间（HHmm）
    private Integer oprLongTime;//运行人员上班时长
    private String oprDutyDate;//运行人员上班日期（yyyy-MM-dd）
    private Integer oprScheduleId;//运行人员当班id
    private Integer dutyId;
	private String dutyName;
	private Integer shiftId;
	private String shiftName;
	private String startTime;//班次的开始时间
	private Integer longTime;//班次的持续时间
	private String shiftType;//班次的类型
	
    
    /**
     * 考勤结果（正常/无效/迟到/早退）类型（运行/上午/下午）
     * 来源枚举ATD_MACHINE_WORK_STATUS
     */
    private String workStatus;
    
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
	public String getOprDutyDate() {
		return oprDutyDate;
	}
	public void setOprDutyDate(String oprDutyDate) {
		this.oprDutyDate = oprDutyDate;
	}
	public Boolean getIsOpr() {
		return isOpr;
	}
	public void setIsOpr(Boolean isOpr) {
		this.isOpr = isOpr;
	}
	
	public String getOprStartTime() {
		return oprStartTime;
	}
	public void setOprStartTime(String oprStartTime) {
		this.oprStartTime = oprStartTime;
	}
	public Integer getOprLongTime() {
		return oprLongTime;
	}
	public void setOprLongTime(Integer oprLongTime) {
		this.oprLongTime = oprLongTime;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public String getAmId() {
		return amId;
	}
	public void setAmId(String amId) {
		this.amId = amId;
	}
	public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
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
		result = prime * result + ((amId == null) ? 0 : amId.hashCode());
		result = prime * result
				+ ((checkDate == null) ? 0 : checkDate.hashCode());
		result = prime * result
				+ ((createBy == null) ? 0 : createBy.hashCode());
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
		result = prime * result
				+ ((deptName == null) ? 0 : deptName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
		result = prime * result
				+ ((updateBy == null) ? 0 : updateBy.hashCode());
		result = prime * result
				+ ((updateDate == null) ? 0 : updateDate.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardDataBean other = (CardDataBean) obj;
		if (amId == null) {
			if (other.amId != null)
				return false;
		} else if (!amId.equals(other.amId))
			return false;
		if (checkDate == null) {
			if (other.checkDate != null)
				return false;
		} else if (!checkDate.equals(other.checkDate))
			return false;
		if (createBy == null) {
			if (other.createBy != null)
				return false;
		} else if (!createBy.equals(other.createBy))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (deptId == null) {
			if (other.deptId != null)
				return false;
		} else if (!deptId.equals(other.deptId))
			return false;
		if (deptName == null) {
			if (other.deptName != null)
				return false;
		} else if (!deptName.equals(other.deptName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (siteId == null) {
			if (other.siteId != null)
				return false;
		} else if (!siteId.equals(other.siteId))
			return false;
		if (updateBy == null) {
			if (other.updateBy != null)
				return false;
		} else if (!updateBy.equals(other.updateBy))
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CardDataBean [id=" + id + ", checkDate=" + checkDate
				+ ", userId=" + userId + ", userName=" + userName
				+ ", createBy=" + createBy + ", createDate=" + createDate
				+ ", updateBy=" + updateBy + ", updateDate=" + updateDate
				+ ", siteId=" + siteId + ", deptName=" + deptName + ", deptId="
				+ deptId + ", amId=" + amId + "]";
	}
    
}
