package com.timss.finance.bean;

import com.yudean.mvc.bean.ItcMvcBean;


public class FinanceAttachMatch extends ItcMvcBean {

	/**
	 * 财务报销的业务id
	 * */
	private String fid;
	
	
	/**
	 * 附件上传的文件id
	 * */
	private String attachid;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getAttachid() {
		return attachid;
	}

	public void setAttachid(String attachid) {
		this.attachid = attachid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attachid == null) ? 0 : attachid.hashCode());
		result = prime * result + ((fid == null) ? 0 : fid.hashCode());
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
		FinanceAttachMatch other = (FinanceAttachMatch) obj;
		if (attachid == null) {
			if (other.attachid != null)
				return false;
		} else if (!attachid.equals(other.attachid))
			return false;
		if (fid == null) {
			if (other.fid != null)
				return false;
		} else if (!fid.equals(other.fid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FinanceAttachMatch [fid=" + fid + ", attachid=" + attachid
				+ "]";
	}
	
	
}
