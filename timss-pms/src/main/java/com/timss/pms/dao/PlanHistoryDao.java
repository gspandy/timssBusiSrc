package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.PlanHistory;
import com.timss.pms.vo.PlanHistoryVo;

public interface PlanHistoryDao {
	 int insertPlanHistory(PlanHistory planHistory);
		
	 int deletePlanHistoryByPlanId(Integer planId);
		
	 List<PlanHistoryVo> queryPlanHistoryListByPlanId(Integer planId);
}
