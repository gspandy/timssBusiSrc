package com.timss.workorder.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.vo.WoStatisVO;
import com.yudean.itc.dto.Page;

public interface WOStatisDao {
	
	List<WoStatisVO> queryStatisWO(Page<WoStatisVO> page);
	/**
	 * @description:查找最早一张工单的年份
	 * @author: 王中华
	 * @createDate: 2014-8-4 
	 * @param siteId
	 * @return:
	 */
	int queryYearOfFirstWO(@Param("siteId") String siteId);

	/**
	 * @description:根据班组编号，查询此班组负责的风机台数
	 * @author: 王中华
	 * @createDate: 2014-8-4
	 * @param workteamCode
	 * @return:
	 */
	int queryWorkTeamWindSum(@Param("siteId") String siteId, @Param("workTeamCode")String workTeamCode);
	/**
	 * @description:查询某班组的风机故障次数 
	 * @author: 王中华
	 * @createDate: 2014-8-12
	 * @param parmas
	 * @return:
	 */
	int queryWorkTeamFaultSum(Map<String, Object> parmas);
	/**
	 * @description:查询某班组的故障停机时间 
	 * @author: 王中华
	 * @createDate: 2014-8-12
	 * @param parmas
	 * @return:
	 */
	Float queryWorkTeamFaultStopTime(Map<String, Object> parmas);
	/**
	 * @description: 打印统计查询 
	 * @author: 王中华
	 * @createDate: 2014-8-13
	 * @param parmas 
	 * @return:
	 */
	List<WoStatisVO> printWOStatistic(Map<String, Object> parmas);
	/**
	 * @description:为便于打印，将查询的数据先插入临时表中，然后再查表打印出来
	 * @author: 王中华
	 * @createDate: 2014-8-13
	 * @param ret: 
	 */
	void insertBatchWoStatisData(List<WoStatisVO> ret);
	/**
	 * @description:为将一次查询的结果插入到表中，用一个字段PrintNum来标识是某次查询结果
	 * @author: 王中华
	 * @createDate: 2014-8-13
	 * @return:
	 */
	int getNextPrintNum();
}
