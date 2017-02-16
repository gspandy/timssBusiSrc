package com.timss.ptw.bean;

import com.yudean.mvc.bean.ItcMvcBean;


/**
 * @title: {title} 隔离方法
 * @description: {desc} 隔离方法的维护
 * @company: gdyd
 * @className: PtwIslMethodDefine.java
 * @author: 王中华
 * @createDate: 2014-10-14
 * @updateUser: 王中华
 * @version: 1.0
 */
public class PtwKeyBox extends ItcMvcBean{
    
	private static final long serialVersionUID = -7819550832486160773L;
	private int id;
    private String keyBoxNo;
    private String useType;
    private String purpose;
    private String curStatus;
    private Integer yxbz;
    

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKeyBoxNo() {
		return keyBoxNo;
	}
	public void setKeyBoxNo(String keyBoxNo) {
		this.keyBoxNo = keyBoxNo;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getCurStatus() {
		return curStatus;
	}
	public void setCurStatus(String curStatus) {
		this.curStatus = curStatus;
	}
	public Integer getYxbz() {
		return yxbz;
	}
	public void setYxbz(Integer yxbz) {
		this.yxbz = yxbz;
	}
	@Override
	public String toString() {
		return "PtwKeyBox [id=" + id + ", keyBoxNo=" + keyBoxNo + ", useType="
				+ useType + ", purpose=" + purpose + ", curStatus=" + curStatus
				+ ", yxbz=" + yxbz + "]";
	}
	
    
    
}
