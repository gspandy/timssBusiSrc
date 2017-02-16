package com.timss.finance.dao;

import java.util.List;

import com.timss.finance.bean.FinanceMain;
import com.yudean.itc.dto.Page;

public interface FinanceInfoDao {
    /**
     * 
     * @description:查找
     * @author: wus
     * @createDate: 2014-6-16
     * @param FinanceMain
     */
	
	 List<FinanceMain> findFinanceMainList(Page<FinanceMain> page);
}
