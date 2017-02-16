package com.timss.pms.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Outsourcing;
import com.timss.pms.bean.Project;
import com.timss.pms.dao.OutsourcingDao;
import com.timss.pms.service.OutsourcingService;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.vo.OutsourcingVo;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class OutsourcingServiceImpl implements OutsourcingService{
	@Autowired
	OutsourcingDao outsourcingDao;
	@Autowired
	ItcMvcService itcMvcService;
	
	public int insertOutsourcing(Outsourcing outsourcing){
		InitUserAndSiteIdUtil.initCreate(outsourcing, itcMvcService);
		int r=outsourcingDao.insertOutsourcing(outsourcing);
		
		return r;
	}

	@Override
	public int updateOutsourcing(Outsourcing outsourcing) {
		int r=outsourcingDao.updateOutsourcing(outsourcing);
		return r;
	}

	@Override
	public int deleteOutsourcingByProjectId(String projectId) {
		outsourcingDao.deleteOutsourcingByProjectId(Integer.valueOf(projectId));
		return 0;
	}

	@Override
	public List<OutsourcingVo> queryOutsoucingListByProjectId(String projectId) {
		List<OutsourcingVo> outsourcingVos=outsourcingDao.queryOutsourcingListByProjectId(Integer.valueOf(projectId));
		return outsourcingVos;
	}

	@Override
	@Transactional
	public int updateOutsourcingList(List<Outsourcing> outsourcings, Project project) {
		deleteOutsourcingByProjectId(project.getId().toString());
		if(outsourcings!=null){
			initOutsourcingList(outsourcings,project);
			for(int i=0;i<outsourcings.size();i++){
				insertOutsourcing(outsourcings.get(i));
			}
		}
		
		return 0;
	}

	private void initOutsourcingList(List<Outsourcing> outsourcings,
			Project project) {
		if(outsourcings!=null){
			for(int i=0;i<outsourcings.size();i++){
				Outsourcing outsourcing=outsourcings.get(i);
				outsourcing.setProjectId(project.getId());
			}
		}
		
	}
	
	
}
