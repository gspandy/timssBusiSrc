package com.timss.attendance.vo;

import com.timss.attendance.bean.SealApplyBean;

/**
 * 
 * @title: 用章申请VO
 * @company: gdyd
 * @author: 890199
 * @createDate: 2016年8月29日
 * @version: 1.0
 */
public class SealApplyVo extends SealApplyBean{
	private String createUserName;
	private String deptName;
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

}
