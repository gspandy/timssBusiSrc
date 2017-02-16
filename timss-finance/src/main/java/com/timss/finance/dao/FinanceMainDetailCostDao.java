package com.timss.finance.dao;

import org.apache.ibatis.annotations.Param;

import com.timss.finance.bean.FinanceMainDetailCost;

public interface FinanceMainDetailCostDao {

	/**
	 * @description:增加
	 * @author: liangzl
	 * @createDate: 2014-8-19 
	 * @param 
	 */
	 void insertFinanceMainDetailCost(FinanceMainDetailCost financeMainDetailCost);

	void deleteFinanceMainDetailCostByFid(String fid);

    /**
     * @description:修改某一项的金额
     * @author: 王中华
     * @createDate: 2016-9-26
     * @param id
     * @param costId
     * @param amount:
     */
    void updateAFinMainDetailAmount(@Param("id")String id, @Param("costId")String costId, @Param("amount")double amount);
}
