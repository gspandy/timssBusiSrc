package com.timss.finance.dao;


/**
 * 
 * @title: FinanceExpensesDao
 * @description: 
 * @company: gdyd
 * @className: FinanceExpensesDao.java
 * @author: zhuw
 * @createDate: 2016年6月28日
 * @updateUser: zhuw
 * @version: 1.0
 */
public interface FinanceExpensesDao {
    /**
     * 
     * @description:根据站点id，查询最早的报销起始年份
     * @author: zhuw
     * @createDate: 2016年6月28日
     * @param siteId
     * @return: String
     */
	String queryBeginYearBySiteId(String siteId) ;
}
