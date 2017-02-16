package com.timss.finance.service;

import java.util.List;

import com.timss.finance.bean.FinancePageConfig;

public interface FinancePageConfigService {
	
	
	/**
	 * @description:通过流程类型，获取对应的三种报销类型的页面配置数据
	 * @author: 王中华
	 * @createDate: 2015-8-24
	 * @param flowType
	 * @param siteid
	 * @return:
	 */
	public List<FinancePageConfig> getFinPageConfByFlowType(String flowType,String siteid);
}
