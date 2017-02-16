package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.Milestone;
import com.timss.pms.vo.MilestoneVo;

public interface MilestoneDao {
	int insertMilestone(Milestone milestone);
	
	int updateMilestone(Milestone milestone);
	
	int deleteMilestoneByProjectId(Integer projectId);
	
	List<MilestoneVo> queryMilestoneListByProjectId(Integer projectId);
}
