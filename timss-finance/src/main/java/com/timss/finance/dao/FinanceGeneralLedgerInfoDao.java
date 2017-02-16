package com.timss.finance.dao;

import java.util.List;
import java.util.Map;

import com.timss.finance.bean.FinanceGeneralLedgerInfo;

public interface FinanceGeneralLedgerInfoDao {
	
	/** 
	 * @description: 通过报销单编号查询总帐信息 
	 * @author: 890170
	 * @createDate: 2014-12-2
	 */
	List<FinanceGeneralLedgerInfo> queryFinanceGeneralLedgerInfoListByFid2(Map<String, Object> paramsMap);

    /**
     * @description: 通过报销的流程类型（差旅费、……）查询总账信息
     * @author: 王中华
     * @createDate: 2015-9-7
     * @param finNameEn
     * @return:
     */
    List<FinanceGeneralLedgerInfo> queryFinanceGeneralLedgerInfoListByFinType(String finNameEn);
}
