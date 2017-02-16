package com.timss.attendance.bean;

import java.util.Date;
import java.util.List;

import com.yudean.itc.annotation.EntityID;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 考勤机
 * @description: {desc}
 * @company: gdyd
 * @className: MachineBean.java
 * @author: yyn
 * @createDate: 2015年12月22日
 * @updateUser: yyn
 * @version: 1.0
 */
public class MachineBean extends ItcMvcBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3758398840337569046L;

	/**
     * 使用组件的uuid
     */
    @UUIDGen(requireType=GenerationType.REQUIRED_NEW)
    @EntityID
    private String amId;

    /**
     * 考勤机编码
     */
    private String amCode;
    
    /**
     * 考勤机名称
     */
    private String amName;
    
    /**
     * 考勤机描述
     */
    private String amDesc;
    
    /**
     * 通讯实现类
     */
    private String communicationClass;
    
    /**
     * ip
     */
    private String amIp;
    
    /**
     * 端口
     */
    private Integer amPort;
        
    /**
     * 上次同步时间
     */
    private Date lastSync;
    
    /**
     * 导入数据时间戳
     */
    private Date lastImport;
    private String lastImportStr;
    
    private List<CardDataBean> cardDataList;
    
    private String startTime;//考勤机导入数据的开始时间
    private String endTime;//考勤机导入数据的结束时间
    
    private String status;//考勤机状态
    
    private String type;//获取数据的方法，取值machine/smb，考勤机/smb文件
    private String loginName;//登录名
    private String password;//密码
    private String path;//访问路径
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLastImportStr() {
		return lastImportStr;
	}

	public void setLastImportStr(String lastImportStr) {
		this.lastImportStr = lastImportStr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<CardDataBean> getCardDataList() {
		return cardDataList;
	}

	public void setCardDataList(List<CardDataBean> cardDataList) {
		this.cardDataList = cardDataList;
	}

	public String getAmId() {
		return amId;
	}

	public void setAmId(String amId) {
		this.amId = amId;
	}

	public String getAmCode() {
		return amCode;
	}

	public void setAmCode(String amCode) {
		this.amCode = amCode;
	}

	public String getAmName() {
		return amName;
	}

	public void setAmName(String amName) {
		this.amName = amName;
	}

	public String getAmDesc() {
		return amDesc;
	}

	public void setAmDesc(String amDesc) {
		this.amDesc = amDesc;
	}

	public String getCommunicationClass() {
		return communicationClass;
	}

	public void setCommunicationClass(String communicationClass) {
		this.communicationClass = communicationClass;
	}

	public String getAmIp() {
		return amIp;
	}

	public void setAmIp(String amIp) {
		this.amIp = amIp;
	}

	public Integer getAmPort() {
		return amPort;
	}

	public void setAmPort(Integer amPort) {
		this.amPort = amPort;
	}

	public Date getLastSync() {
		return lastSync;
	}

	public void setLastSync(Date lastSync) {
		this.lastSync = lastSync;
	}

	public Date getLastImport() {
		return lastImport;
	}

	public void setLastImport(Date lastImport) {
		this.lastImport = lastImport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amCode == null) ? 0 : amCode.hashCode());
		result = prime * result + ((amDesc == null) ? 0 : amDesc.hashCode());
		result = prime * result + ((amId == null) ? 0 : amId.hashCode());
		result = prime * result + ((amIp == null) ? 0 : amIp.hashCode());
		result = prime * result + ((amName == null) ? 0 : amName.hashCode());
		result = prime * result + ((amPort == null) ? 0 : amPort.hashCode());
		result = prime
				* result
				+ ((communicationClass == null) ? 0 : communicationClass
						.hashCode());
		result = prime * result
				+ ((lastImport == null) ? 0 : lastImport.hashCode());
		result = prime * result
				+ ((lastSync == null) ? 0 : lastSync.hashCode());
		result = prime * result
				+ ((getCreateuser() == null) ? 0 : getCreateuser().hashCode());
		result = prime * result
				+ ((getModifyuser() == null) ? 0 : getModifyuser().hashCode());
		result = prime * result
				+ ((getCreatedate() == null) ? 0 : getCreatedate().hashCode());
		result = prime * result
				+ ((getModifydate() == null) ? 0 : getModifydate().hashCode());
		result = prime * result
				+ ((getSiteid() == null) ? 0 : getSiteid().hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "MachineBean [amId=" + amId + ", amCode=" + amCode + ", amName="
				+ amName + ", amDesc=" + amDesc 
				+ ", communicationClass=" + communicationClass + ", amIp="
				+ amIp + ", amPort=" + amPort + ", lastSync=" + lastSync
				+ ", createuser=" + getCreateuser()+ ", createdate=" + getCreatedate()
				+ ", modifyuser=" + getModifyuser()+ ", modifydate=" + getModifydate()
				+ ", siteid=" + getSiteid()
				+ ", lastImport=" + lastImport + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MachineBean other = (MachineBean) obj;
		if (amCode == null) {
			if (other.amCode != null)
				return false;
		} else if (!amCode.equals(other.amCode))
			return false;
		if (amDesc == null) {
			if (other.amDesc != null)
				return false;
		} else if (!amDesc.equals(other.amDesc))
			return false;
		if (amId == null) {
			if (other.amId != null)
				return false;
		} else if (!amId.equals(other.amId))
			return false;
		if (amIp == null) {
			if (other.amIp != null)
				return false;
		} else if (!amIp.equals(other.amIp))
			return false;
		if (amName == null) {
			if (other.amName != null)
				return false;
		} else if (!amName.equals(other.amName))
			return false;
		if (amPort == null) {
			if (other.amPort != null)
				return false;
		} else if (!amPort.equals(other.amPort))
			return false;
		if (communicationClass == null) {
			if (other.communicationClass != null)
				return false;
		} else if (!communicationClass.equals(other.communicationClass))
			return false;
		if (lastImport == null) {
			if (other.lastImport != null)
				return false;
		} else if (!lastImport.equals(other.lastImport))
			return false;
		if (lastSync == null) {
			if (other.lastSync != null)
				return false;
		} else if (!lastSync.equals(other.lastSync))
			return false;
		
		if (getSiteid() == null) {
			if (other.getSiteid() != null)
				return false;
		} else if (!getSiteid().equals(other.getSiteid()))
			return false;
		
		if (getCreateuser() == null) {
			if (other.getCreateuser() != null)
				return false;
		} else if (!getCreateuser().equals(other.getCreateuser()))
			return false;
		if (getCreatedate() == null) {
			if (other.getCreatedate() != null)
				return false;
		} else if (!getCreatedate().equals(other.getCreatedate()))
			return false;
		if (getModifyuser() == null) {
			if (other.getModifyuser() != null)
				return false;
		} else if (!getModifyuser().equals(other.getModifyuser()))
			return false;
		if (getModifydate() == null) {
			if (other.getModifydate() != null)
				return false;
		} else if (!getModifydate().equals(other.getModifydate()))
			return false;
		
		return true;
	}

}
