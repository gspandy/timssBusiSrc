package com.timss.finance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.finance.bean.FinSubjectShowConfig;
import com.timss.finance.bean.FinancePageConfig;

public interface FinSubjectShowConfigDao {
    
	
  
    /**
     * @description: 根据报销名和站点，查询相关的配置集合
     * @author: 王中华
     * @createDate: 2015-9-7
     * @param finNameEn
     * @param siteid
     * @return:
     */
    List<FinSubjectShowConfig> getFinSubjectShowConfByFlowType(@Param("finNameEn")String finNameEn,@Param("siteid")String siteid);
	
   
}
