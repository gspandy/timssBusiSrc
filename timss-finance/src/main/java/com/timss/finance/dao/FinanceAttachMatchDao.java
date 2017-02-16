package com.timss.finance.dao;

import java.util.List;

import com.timss.finance.bean.FinanceAttachMatch;

/**
 * 
 * @title: 值别Dao
 * @description: 
 * @company: gdyd
 * @className: FinanceAttachMatchDao.java
 * @author: wus
 * @createDate: 2014年7月1日
 * @updateUser: wus
 * @version: 1.0
 */
public interface FinanceAttachMatchDao {
    /**
     * 
     * @description:insert结果中返回主键方法
     * @author: wus
     * @createDate: 2014年7月1日
     * @param fid  其中id为自增，不需要设置
     * @return:
     */
	void insertFinanceAttachMatch(FinanceAttachMatch fam) ;

	 /**
		 * 
		 * @description:查询结果中返回FinanceAttachMatch的方法
		 * @author: wus
		 * @createDate: 2014年6月23日
		 * @param 
		 * @return:
		 * @throws Exception 
		 */
	List<FinanceAttachMatch> queryFinanceAttachMatchByFid(String fid);
	
	boolean deleteFinanceAttachMatch(String fid);
	
	
}
