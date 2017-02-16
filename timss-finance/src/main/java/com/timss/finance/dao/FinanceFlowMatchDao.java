package com.timss.finance.dao;

import com.timss.finance.bean.FinanceFlowMatch;


/**
 * 
 * @title: 值别Dao
 * @description: 
 * @company: gdyd
 * @className: FinanceFlowMatchDao.java
 * @author: wus
 * @createDate: 2014年6月13日
 * @updateUser: wus
 * @version: 1.0
 */
public interface FinanceFlowMatchDao {

	 /**
	 * 
	 * @description:通过业务id查询返回financeFlowMatch匹配数据
	 * @author: wus
	 * @createDate: 2014年6月23日
	 * @param 
	 * @return:FinanceFlowMatch
	 * @throws Exception 
	 */
	 FinanceFlowMatch queryFinanceFlowMatchByFid(String fid);
	 
	/**
	* 
	* @description:通过流程实例id返回financeFlowMatch匹配数据
	* @author: wus
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @throws Exception 
	*/
	FinanceFlowMatch queryFinanceFlowMatchByPid(String pid) ;
	
	
	/**
	* 
	* @description:增加financeFlowMatch数据
	* @author: wus
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @throws Exception 
	*/
	void insertFinanceFlowMatch(FinanceFlowMatch financeFlowMatch) ;

	/** 
	 * @description: 删除报销流程映射
	 * @author: 890170
	 * @createDate: 2015-1-5
	 */
	void deleteFinanceFlowMatch(FinanceFlowMatch financeFlowMatch);
	
	/**
	* 
	* @description:删除financeFlowMatch数据,通过fid删除
	* @author: wus
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @throws Exception 
	
	FinanceFlowMatch deleteFinanceFlowMatchByFid(String fid) throws Exception;
	*/
	/**
	* 
	* @description:删除financeFlowMatch数据,通过pid删除
	* @author: wus
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @throws Exception 
	
	FinanceFlowMatch deleteFinanceFlowMatchByPid(String pid) throws Exception;
	*/
	/**
	* 
	* @description:更新financeFlowMatch数据,通过fid更新
	* @author: wus
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @throws Exception 
	
	FinanceFlowMatch updateFinanceFlowMatchByFid(String fid) throws Exception;
	*/
	/**
	* 
	* @description:更新financeFlowMatch数据,通过pid更新
	* @author: wus
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @throws Exception 
	
	FinanceFlowMatch updateFinanceFlowMatchByPid(String pid) throws Exception;
	*/
}
