package com.timss.itsm.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ItsmHomepageCardDao {

	/**
	 * @description:各一级服务目录工单数量
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param beginTime
	 * @param endTime
	 * @return:
	 */
	List<Map<String, BigDecimal>> queryOneLevlFtWoSum(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * @description: 回访了的工单总数量
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param currYearFirst
	 * @param date
	 * @return:
	 */
	int queryFbWoSum(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * @description:回访不满意和投诉的工单数量
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param currYearFirst
	 * @param date
	 * @return:
	 */
	int queryUnOkWoSum(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * @description:团队工单数量
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param currYearFirst
	 * @param date
	 * @return:
	 */
	List<Map<String, Object>> queryteamWoSum(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * @description:团队及时响应工单数量 
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param currYearFirst
	 * @param date
	 * @return:
	 */
	List<Map<String, Object>> queryteamOnTimeRespondWoSum(
			@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * @description:团队及时解决工单数量
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param currYearFirst
	 * @param date
	 * @return:
	 */
	List<Map<String, Object>> queryteamOnTimeSolveWoSum(
			@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * @description:参加计算工单能力的工单总数
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param beginTime
	 * @param endTime
	 * @return:
	 */
	int queryWoSolveAbilitySum(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * @description:超时响应的工单数
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param beginTime
	 * @param endTime
	 * @return:
	 */
	int queryOverTimeRespondSum(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * @description:超时解决的工单数 
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param beginTime
	 * @param endTime
	 * @return:
	 */
	int queryOverTimeSolveSum(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * @description:各级服务目录的平均响应时间
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @param firstDayOfLastMonth
	 * @param firstDayOfCurrMonth
	 * @return:
	 */
	List<Map<String, BigDecimal>> queryeveryFtAvgRespondTime(
			@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
}
