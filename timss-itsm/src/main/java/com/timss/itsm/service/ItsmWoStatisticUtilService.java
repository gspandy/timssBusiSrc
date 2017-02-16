package com.timss.itsm.service;

import java.util.Date;
import java.util.List;

import com.timss.itsm.vo.ItsmWorkTimeVo;
import com.yudean.itc.dto.sec.SecureUserGroup;


/**
 * @title: {title}
 * @description: {desc} 通用service
 * @company: gdyd
 * @className: WoUtilService.java
 * @author: 王中华
 * @createDate: 2014-9-17
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface ItsmWoStatisticUtilService {
	/**
	 * @description: 检索用户组
	 * @author: 王中华
	 * @createDate: 2014-9-17
	 * @param woId
	 * @param woStatus:
	 */
	List<SecureUserGroup> retrieveGroupsByKeyword(String keyword);
	
	/**
	 * @description:设置工作时间
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param startDate
	 * @param endDate
	 * @return:
	 */
	ItsmWorkTimeVo setWorkTimeVo(Date startDate, Date endDate);
}
