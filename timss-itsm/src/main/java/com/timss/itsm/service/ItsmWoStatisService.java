package com.timss.itsm.service;

import java.util.Date;
import java.util.Map;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: ItWOStatisService.java
 * @author: 王中华
 * @createDate: 2014-11-25
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface ItsmWoStatisService {
	
	
	/**
	 * @description: 根据起止日期和站点信息，查询统计信息，并插入统计信息临时表，最后返回一个统计的批次数给前台
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @param beginTime
	 * @param endTime
	 * @param siteid
	 * @return:
	 * @throws Exception 
	 */
	Map<String, Object> queryItWoStatisticVO(Date beginTime,Date endTime,String siteid,String statisticType) throws Exception;
	
	
}
