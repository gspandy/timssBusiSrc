package com.timss.pms.service;

import java.util.List;

import com.timss.pms.bean.Project;
import com.timss.pms.bean.Workload;
import com.timss.pms.vo.WorkloadVo;

public interface WorkloadService {
    int insertWorkload(Workload outsourcing);
	
	int updateWorkload(Workload outsourcing);
	
	int updateWorklaodList(List<Workload> workloads,Project project);
	
	int deleteWorkloadByProjectId(String projectId);
	
	List<WorkloadVo> queryOutsoucingListByProjectId(String projectId);
}
