package com.timss.pms.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.bean.Project;
import com.timss.pms.bean.Workload;
import com.timss.pms.dao.WorkloadDao;
import com.timss.pms.service.WorkloadService;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.vo.WorkloadVo;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class WorkloadServiceImpl implements WorkloadService{
	@Autowired
	WorkloadDao workloadDao;
	@Autowired
	ItcMvcService itcMvcService;
	
	public int insertWorkload(Workload workload){
		InitUserAndSiteIdUtil.initCreate(workload, itcMvcService);
		int r=workloadDao.insertWorkload(workload);
		
		return r;
	}

	@Override
	public int updateWorkload(Workload workload) {
		int r=workloadDao.updateWorkload(workload);
		return r;
	}

	@Override
	public int deleteWorkloadByProjectId(String projectId) {
		workloadDao.deleteWorkloadByProjectId(Integer.valueOf(projectId));
		return 0;
	}

	@Override
	public List<WorkloadVo> queryOutsoucingListByProjectId(String projectId) {
		List<WorkloadVo> workloadVos=workloadDao.queryWorkloadListByProjectId(Integer.valueOf(projectId));
		return workloadVos;
	}

	@Override
	public int updateWorklaodList(List<Workload> workloads, Project project) {
		deleteWorkloadByProjectId(project.getId().toString());
		if(workloads!=null){
			initWorkloadsList(workloads,project);
			for(int i=0;i<workloads.size();i++){
				insertWorkload(workloads.get(i));
			}
		}
		
		return 0;
	}

	private void initWorkloadsList(List<Workload> workloads, Project project) {
		if(workloads!=null){
			for(int i=0;i<workloads.size();i++){
				Workload workload=workloads.get(i);
				workload.setProjectId(project.getId());
			}
		}
		
	}
}
