package com.timss.finance.service;

import java.util.Map;

import com.timss.finance.bean.FinanceFlowMatch;

/**
 * 
 * @title: 值别Service
 * @description: 
 * @company: gdyd
 * @className: FinanceFlowMatchService.java
 * @author: 吴圣
 * @createDate: 2014年6月24日
 * @updateUser: 吴圣
 * @version: 1.0
 */
public interface FinanceFlowMatchService {
	

	 /**
	 * 
	 * @description:通过业务id查询返回financeFlowMatch匹配数据
	 * @author: 吴圣
	 * @createDate: 2014年6月23日
	 * @param 
	 * @return:FinanceFlowMatch
	 * @ 
	 */
	 FinanceFlowMatch queryFinanceFlowMatchByFid(String fid);
	 
	/**
	* 
	* @description:通过流程实例id返回financeFlowMatch匹配数据
	* @author: 吴圣
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @ 
	*/
	
	FinanceFlowMatch queryFinanceFlowMatchByPid(String pid) ;
	
	
	/**
	* 
	* @description:增加financeFlowMatch数据
	* @author: 吴圣
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @ 
	*/
	
	FinanceFlowMatch insertFinanceFlowMatch(FinanceFlowMatch financeFlowMatch) ;
	
	
	/**
	* 
	* @description:删除financeFlowMatch数据,通过fid删除
	* @author: 吴圣
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @ 
	*/
	/*
	FinanceFlowMatch deleteFinanceFlowMatchByFid(String fid) ;
	*/
	/**
	* 
	* @description:删除financeFlowMatch数据,通过pid删除
	* @author: 吴圣
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @ 
	*/
	/*
	FinanceFlowMatch deleteFinanceFlowMatchByPid(String pid) ;
	*/
	/**
	* 
	* @description:更新financeFlowMatch数据,通过fid更新
	* @author: 吴圣
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @ 
	*/
	/*
	FinanceFlowMatch updateFinanceFlowMatchByFid(String fid) ;
	*/
	/**
	* 
	* @description:更新financeFlowMatch数据,通过pid更新
	* @author: 吴圣
	* @createDate: 2014年6月23日
	* @param 
	* @return:FinanceFlowMatch
	* @ 
	*/
	/*
	FinanceFlowMatch updateFinanceFlowMatchByPid(String pid) ;
	*/
}
