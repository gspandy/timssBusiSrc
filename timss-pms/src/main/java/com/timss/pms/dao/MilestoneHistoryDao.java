package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.MilestoneHistory;
import com.timss.pms.vo.MilestoneHistoryVo;

public interface MilestoneHistoryDao {
    int insertMilestoneHistory(MilestoneHistory milestoneHistory);
	
	int deleteMilestoneHistoryByProjectId(Integer projectId);
	
	List<MilestoneHistoryVo> queryMilestoneHistoryListByProjectId(Integer projectId);
}
