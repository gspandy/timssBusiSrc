package com.timss.finance.service;


/**
 * @ClassName:     FinanceExpensesService
 * @company: gdyd
 * @author:    zhuw
 * @date:   2016-6-28
 */
public interface FinanceExpensesService {
	/**
	 * @Title: queryBeginYearBySiteId
	 * @Description: 根据站点id，查询最早的报销起始年份
	 * @param siteId
	 * @return String
	 */
	public String queryBeginYearBySiteId(String siteId);
}
