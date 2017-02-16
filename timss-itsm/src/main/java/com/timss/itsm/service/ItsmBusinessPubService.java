package com.timss.itsm.service;

import java.util.Map;


/**
 * @title: {title} 业务相关的公共service
 * @description: {desc}
 * @company: gdyd
 * @className: ItsmBusinessPubService.java
 * @author: 王中华
 * @createDate: 2016-9-29
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface ItsmBusinessPubService {

	/**
	 * @description:更新业务表的当前处理人
	 * @author: 王中华
	 * @createDate: 2016-9-29
	 * @param businessId
	 * @param userIds
	 * @param userNames:
	 */
	void updateBusinessCurrHandler(String businessId, String userIds,String userNames);
	
	/**
	 * @description:更新业务表的业务状态
	 * @author: 王中华
	 * @createDate: 2016-9-29
	 * @param businessId
	 * @param status:
	 */
	void updateBusinessCurrStatus(String businessId, String status);

	
}
