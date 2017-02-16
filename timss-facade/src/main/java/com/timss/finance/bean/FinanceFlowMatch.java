package com.timss.finance.bean;

import com.yudean.mvc.bean.ItcMvcBean;


/**
 * 
 * @title: 财务报销业务id 与 工作流实例id的关联表 
 * @description: 
 * @company: gdyd
 * @className: FinanceFlowMatch.java
 * @author: wus
 * @createDate: 2014年6月24日
 * @updateUser: wus
 * @version: 1.0
 */

public class FinanceFlowMatch extends ItcMvcBean{
	
	/***
	 * 业务id
	 */
	private String fid;
	
	/**
	 * 流程实例id
	 * */
	private String pid;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fid == null) ? 0 : fid.hashCode());
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
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
		FinanceFlowMatch other = (FinanceFlowMatch) obj;
		if (fid == null) {
			if (other.fid != null)
				return false;
		} else if (!fid.equals(other.fid))
			return false;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FinanceFlowMatch [fid=" + fid + ", pid=" + pid + "]";
	}
	
	
}
