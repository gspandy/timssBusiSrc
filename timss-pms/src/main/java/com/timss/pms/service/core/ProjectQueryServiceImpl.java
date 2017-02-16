package com.timss.pms.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.dao.ProjectDao;
import com.timss.pms.service.MilestoneService;
import com.timss.pms.service.ProjectQueryService;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.ProjectVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class ProjectQueryServiceImpl implements ProjectQueryService{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	ProjectDao projectDao;
	@Autowired
	MilestoneService milestoneService;
	private static final Logger LOGGER = Logger.getLogger(ProjectQueryServiceImpl.class);
	@Override
	public Page<ProjectVo> queryProjectListAndFilter(Page<ProjectVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询项目立项数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", userInfo.getSiteId());
		List<ProjectVo> projects = projectDao.queryProjectListAndFilter(page);
		InitVoEnumUtil.initProjectVoList(projects, itcMvcService);
		attachProjoctListWithMilestone(projects);
		attachProjoctListWithPaySum(projects);
		page.setResults(projects);
		LOGGER.info("查询项目立项数据成功");
		
		return page;
	}
	
	private void attachProjoctListWithPaySum(List<ProjectVo> projects) {
		if(projects!=null){
			for(int i=0;i<projects.size();i++){
				ProjectVo projectVo=projects.get(i);
				Double paySum=projectDao.queryProjectPaySumById(projectVo.getId());
				if(paySum==null){
					paySum=0.0;
				}
				projectVo.setPaySum(paySum);
			}
		}
		
	}


	private void attachProjoctListWithMilestone(List<ProjectVo> projects) {
		if(projects!=null){
			for(int i=0;i<projects.size();i++){
				ProjectVo projectVo=projects.get(i);
				projectVo.setMilestoneVos(milestoneService.queryMilestoneListByProjectId(String.valueOf(projectVo.getId())));
			}
		}
		
	}

}
