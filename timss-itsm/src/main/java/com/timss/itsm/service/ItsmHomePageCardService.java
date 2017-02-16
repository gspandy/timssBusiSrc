package com.timss.itsm.service;

import java.util.Map;


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
public interface ItsmHomePageCardService {
	
	/**
	 * @description:各服务目录工单所占比率卡片
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @return
	 * @throws Exception:
	 */
	public Map<String, Object> sdCardStatistic() throws Exception;

	/**
	 * @description: 本年度服务不满意率
	 * @author: 王中华 
	 * @createDate: 2015-3-13
	 * @return:
	 */
	public Map<String, Object> itsmUnOkWostatistic();

	/**
	 * @description: 本年度IT团队及时响应率与解决率 
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @return:
	 */
	public Map<String, Object> itsmTeamRespondSolvestatistic();

	/**
	 * @description:本月工单解决能力统计
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @return:
	 */
	public Map<String, Object> itsmWoSolveAbilitystatistic();

	/**
	 * @description:本月工单的平均响应时间统计
	 * @author: 王中华
	 * @createDate: 2015-3-13
	 * @return:
	 */
	public Map<String, Object> woAvgRespondTimesCard();
	
	
}
