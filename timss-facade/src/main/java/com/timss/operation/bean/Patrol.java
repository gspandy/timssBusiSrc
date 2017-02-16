package com.timss.operation.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 点检日志
 * @description: {desc}
 * @company: gdyd
 * @className: Patrol.java
 * @author: fengtw
 * @createDate: 2015年10月30日
 * @updateUser: fengtw
 * @version: 1.0
 */
public class Patrol extends ItcMvcBean {

    private static final long serialVersionUID = 7519866645404377811L;

    /**
     * 主键ID UUID
     */
    private String patrolId;
    
    /**
     * 点检时间
     */
    private Date checkDate;

    /**
     * 专业   枚举:电气、热控、燃料、锅炉、汽机
     */
    private String speciality;
    
    /**
     * 点检情况
     */
    private String patrolSituation;
    
    /**
     * 缺陷情况
     */
    private String defectSituation;
    
    /**
     * 设备启停情况
     */
    private String startStopSituation;
    
    /**
     * 工作流实例id
     */
    private String instantId;
    
    /**
     * 流程状态
     */
    private String status;
    
    /**
     * 是否作废，使用公共枚举Y/N
     */
    private String isCancel;
    
    /**
     * 是否删除，使用公共枚举Y/N
     */
    private String isDelete;
    
    /**
     * 创建人名称
     */
    private String createUserName;
    
    /**
     * 修改人名称
     */
    private String modifyUserName;
    
    /**
     * 审批人ID
     */
    private String approveUser;
    
    /**
     * 审批人名称
     */
    private String approveUserName;
    
    /**
     * 审批时间
     */
    private Date approveTime;
    
 

	/**
     * 待办流水号
     */
    @AutoGen(value="OPR_PATROL_NUM_SEQ", requireType=GenerationType.REQUIRED_NULL)
    private String seqNum;

	public String getPatrolId() {
		return patrolId;
	}

	public void setPatrolId(String patrolId) {
		this.patrolId = patrolId;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getPatrolSituation() {
		return patrolSituation;
	}

	public void setPatrolSituation(String patrolSituation) {
		this.patrolSituation = patrolSituation;
	}

	public String getDefectSituation() {
		return defectSituation;
	}

	public void setDefectSituation(String defectSituation) {
		this.defectSituation = defectSituation;
	}

	public String getStartStopSituation() {
		return startStopSituation;
	}

	public void setStartStopSituation(String startStopSituation) {
		this.startStopSituation = startStopSituation;
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

	public String getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getModifyUserName() {
		return modifyUserName;
	}

	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}

	public String getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getApproveUser() {
		return approveUser;
	}

	public void setApproveUser(String approveUser) {
		this.approveUser = approveUser;
	}

	public String getApproveUserName() {
		return approveUserName;
	}

	public void setApproveUserName(String approveUserName) {
		this.approveUserName = approveUserName;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approveTime == null) ? 0 : approveTime.hashCode());
		result = prime * result
				+ ((approveUser == null) ? 0 : approveUser.hashCode());
		result = prime * result
				+ ((approveUserName == null) ? 0 : approveUserName.hashCode());
		result = prime * result
				+ ((checkDate == null) ? 0 : checkDate.hashCode());
		result = prime * result
				+ ((createUserName == null) ? 0 : createUserName.hashCode());
		result = prime * result
				+ ((defectSituation == null) ? 0 : defectSituation.hashCode());
		result = prime * result
				+ ((instantId == null) ? 0 : instantId.hashCode());
		result = prime * result
				+ ((isCancel == null) ? 0 : isCancel.hashCode());
		result = prime * result
				+ ((isDelete == null) ? 0 : isDelete.hashCode());
		result = prime * result
				+ ((modifyUserName == null) ? 0 : modifyUserName.hashCode());
		result = prime * result
				+ ((patrolId == null) ? 0 : patrolId.hashCode());
		result = prime * result
				+ ((patrolSituation == null) ? 0 : patrolSituation.hashCode());
		result = prime * result + ((seqNum == null) ? 0 : seqNum.hashCode());
		result = prime * result
				+ ((speciality == null) ? 0 : speciality.hashCode());
		result = prime
				* result
				+ ((startStopSituation == null) ? 0 : startStopSituation
						.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Patrol other = (Patrol) obj;
		if (approveTime == null) {
			if (other.approveTime != null)
				return false;
		} else if (!approveTime.equals(other.approveTime))
			return false;
		if (approveUser == null) {
			if (other.approveUser != null)
				return false;
		} else if (!approveUser.equals(other.approveUser))
			return false;
		if (approveUserName == null) {
			if (other.approveUserName != null)
				return false;
		} else if (!approveUserName.equals(other.approveUserName))
			return false;
		if (checkDate == null) {
			if (other.checkDate != null)
				return false;
		} else if (!checkDate.equals(other.checkDate))
			return false;
		if (createUserName == null) {
			if (other.createUserName != null)
				return false;
		} else if (!createUserName.equals(other.createUserName))
			return false;
		if (defectSituation == null) {
			if (other.defectSituation != null)
				return false;
		} else if (!defectSituation.equals(other.defectSituation))
			return false;
		if (instantId == null) {
			if (other.instantId != null)
				return false;
		} else if (!instantId.equals(other.instantId))
			return false;
		if (isCancel == null) {
			if (other.isCancel != null)
				return false;
		} else if (!isCancel.equals(other.isCancel))
			return false;
		if (isDelete == null) {
			if (other.isDelete != null)
				return false;
		} else if (!isDelete.equals(other.isDelete))
			return false;
		if (modifyUserName == null) {
			if (other.modifyUserName != null)
				return false;
		} else if (!modifyUserName.equals(other.modifyUserName))
			return false;
		if (patrolId == null) {
			if (other.patrolId != null)
				return false;
		} else if (!patrolId.equals(other.patrolId))
			return false;
		if (patrolSituation == null) {
			if (other.patrolSituation != null)
				return false;
		} else if (!patrolSituation.equals(other.patrolSituation))
			return false;
		if (seqNum == null) {
			if (other.seqNum != null)
				return false;
		} else if (!seqNum.equals(other.seqNum))
			return false;
		if (speciality == null) {
			if (other.speciality != null)
				return false;
		} else if (!speciality.equals(other.speciality))
			return false;
		if (startStopSituation == null) {
			if (other.startStopSituation != null)
				return false;
		} else if (!startStopSituation.equals(other.startStopSituation))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Patrol [patrolId=" + patrolId + ", checkDate=" + checkDate
				+ ", speciality=" + speciality + ", patrolSituation="
				+ patrolSituation + ", defectSituation=" + defectSituation
				+ ", startStopSituation=" + startStopSituation + ", instantId="
				+ instantId + ", status=" + status + ", isCancel=" + isCancel
				+ ", isDelete=" + isDelete + ", createUserName="
				+ createUserName + ", modifyUserName=" + modifyUserName
				+ ", approveUser=" + approveUser + ", approveUserName="
				+ approveUserName + ", approveTime=" + approveTime
				+ ", seqNum=" + seqNum + "]";
	}


    
}
