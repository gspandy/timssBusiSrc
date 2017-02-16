package com.timss.finance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.finance.bean.FinancePageConfig;

public interface FinancePageConfigDao {
    
	
    /**
     * @description:通过流程类型，获取对应的三种报销类型的页面配置数据
     * @author: 王中华
     * @createDate: 2015-8-24
     * @param flowType
     * @param siteid
     * @return:
     */
    List<FinancePageConfig> getFinPageConfByFlowType(@Param("flowType")String flowType,@Param("siteid")String siteid);
	
    FinancePageConfig getFinPageConf(@Param("flowType")String flowType,@Param("finType")String finType, @Param("siteid")String siteid);
}
