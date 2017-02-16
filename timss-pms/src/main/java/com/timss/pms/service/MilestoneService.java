package com.timss.pms.service;

import java.util.List;

import com.timss.pms.bean.Milestone;
import com.timss.pms.bean.Project;
import com.timss.pms.vo.MilestoneVo;

public interface MilestoneService {
	int insertMilestoneList(List<Milestone> milestones,Project project);
	
	List<MilestoneVo> queryMilestoneListByProjectId(String projectId);
	
	int updateMilestoneList(List<Milestone> milestones,Project project);
	
	int updateMilestoneAndRecordChange(List<Milestone> milestones,Project project);
	
	int deleteMilestoneListByProjectId(String projectId);
}
