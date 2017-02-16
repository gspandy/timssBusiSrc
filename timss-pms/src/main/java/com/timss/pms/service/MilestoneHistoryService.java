package com.timss.pms.service;

import java.util.List;

import com.timss.pms.bean.MilestoneHistory;
import com.timss.pms.vo.MilestoneHistoryVo;

public interface MilestoneHistoryService {
	int insertMilestoneHistory(MilestoneHistory milestoneHistory);
	
	int insertMilestoneHistory(List<MilestoneHistory> milestoneHistories);
		
	int deleteMilestoneHistoryByProjectId(String projectId);
		
	List<MilestoneHistoryVo> queryOutsoucingListByProjectId(String projectId);
}
