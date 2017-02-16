package com.timss.workorder.service;

import java.util.Date;
import java.util.Map;

import com.timss.workorder.vo.WoStatisVO;
import com.yudean.itc.dto.Page;
/***
 * 工单  Service 操作
 * @author 王中华
 * 2014-6-11
 */
public interface WOStatisService {
	
	/***
	 * 统计所有工单
	 */
	Page<WoStatisVO> queryStatisWO(Page<WoStatisVO> page);
	
	/**
	 * @description:获取最早的一张工单的年份
	 * @author: 王中华
	 * @createDate: 2014-8-4
	 * @return:
	 */
	int queryYearOfFirstWO();
	/**
	 * @description:查询所有的维护班组（key:value）
	 * @author: 王中华
	 * @createDate: 2014-8-4
	 * @param siteId
	 * @return:
	 */
	Map<String, Object> queryAllWorkTeam();
	
	/**
	 * @description:查询每个班组负责的风机中，出现故障的风机总数以及故障停机时间（考核时间）
	 * @author: 王中华
	 * @createDate: 2014-8-4
	 * @param siteId
	 * @param beginDate
	 * @param endDate 
	 * @return:
	 */
	Map<String, Object> getEveryTeamFaultWInfo(String workTeamCode,Date beginDate, Date endDate);

	/**
	 * @description: 查询某个班组的出故障风机次数和停机时间
	 * @author: 王中华
	 * @createDate: 2014-8-12
	 * @param parmas
	 * @return:
	 */
	Map<String, Object> queryCountAndStopTime(Map<String, Object> parmas);
	/**
	 * @description:打印统计
	 * @author: 王中华
	 * @createDate: 2014-8-13
	 * @param parmas
	 * @return:
	 */
	int printWOStatistic(Map<String, Object> parmas);
}
