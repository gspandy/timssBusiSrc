package com.timss.itsm.bean;

import com.yudean.itc.dto.support.AppComment;

/**
 * @description:继承AppComment 获得处理记录人名
 * @author: 
 * @createDate: 2016年8月8日
 * @param 
 * @return:
 */
public class ItsmHandleRd extends AppComment {
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
