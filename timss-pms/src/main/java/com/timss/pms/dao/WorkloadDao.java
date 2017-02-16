package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.Workload;
import com.timss.pms.vo.WorkloadVo;

public interface WorkloadDao {
	int insertWorkload(Workload workload);
	
	int updateWorkload(Workload workload);
	
	int deleteWorkload(String workloadId);
	
	int deleteWorkloadByProjectId(Integer workloadId);
	
	List<WorkloadVo> queryWorkloadListByProjectId(Integer projectId);
}
