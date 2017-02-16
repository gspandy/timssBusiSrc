package com.timss.finance.service;

import org.apache.ibatis.annotations.Param;

import net.sf.json.JSONObject;

/**
 * @title: Service
 * @description:
 * @company: gdyd
 * @className: FinanceMainDetailCostService.java
 * @author: liangzl
 * @createDate: 2014年8月19日
 * @updateUser: liangzl
 * @version: 1.0
 */
public interface FinanceMainDetailCostService {
	/**
	 * @description:通用插入明细费用表Service
	 * @author: liangzl
	 * @createDate: 2014年8月22日
	 * @param mainDtlList, detail
	 * @return: void
	 */
//	void insertFinanceMainDetailCost(List<FinanceMainDetail> mainDtlList,
//			String detail) ;
	
	/**
	 * @description:根据fid删除明细费用表
	 * @author: liangzl
	 * @createDate: 2014年8月20日 
	 * @param string
	 * @return: void
	 */
	void deleteFinanceMainDetailCostByFid(String fid);

    /**
     * @description:插入某一条报销明细对应的所有报销费用
     * @author: 王中华
     * @createDate: 2015-9-10
     * @param aDetailJson
     * @param id
     * @param siteid:
     */
    void insertAFinMainDetailAllCost(JSONObject aDetailJson, String id, String siteid);
    
    void updateAFinMainDetailAmount(@Param("id")String id,@Param("costId")String costId,@Param("amount")double amount);
}
