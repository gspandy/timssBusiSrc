package com.timss.pms.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.bean.MilestoneHistory;
import com.timss.pms.dao.MilestoneHistoryDao;
import com.timss.pms.service.MilestoneHistoryService;
import com.timss.pms.vo.MilestoneHistoryVo;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;
@Service
public class MilestoneHistoryServiceImpl implements MilestoneHistoryService{
	@Autowired
	MilestoneHistoryDao milestoneHistoryDao;
	@Autowired
	ItcMvcService itcMvcService;
	
	Logger LOGGER=Logger.getLogger(MilestoneHistoryServiceImpl.class);
	@Override
	public int insertMilestoneHistory(MilestoneHistory milestoneHistory) {
		LOGGER.info("插入里程碑变更历史记录");
		milestoneHistoryDao.insertMilestoneHistory(milestoneHistory);
		return 0;
	}

	@Override
	public int insertMilestoneHistory(List<MilestoneHistory> milestoneHistories) {
		LOGGER.info("插入里程碑变更历史记录列表");
		if(milestoneHistories!=null){
			for(int i=0;i<milestoneHistories.size();i++){
				insertMilestoneHistory(milestoneHistories.get(i));
			}
		}
		return 0;
	}

	@Override
	public int deleteMilestoneHistoryByProjectId(String projectId) {
		milestoneHistoryDao.deleteMilestoneHistoryByProjectId(Integer.valueOf(projectId));
		return 0;
	}

	@Override
	public List<MilestoneHistoryVo> queryOutsoucingListByProjectId(
			String projectId) {
		List<MilestoneHistoryVo> milestoneHistoryVos=milestoneHistoryDao.queryMilestoneHistoryListByProjectId(Integer.valueOf(projectId));
		
		if(milestoneHistoryVos!=null){
			for(int i=0;i<milestoneHistoryVos.size();i++){
				MilestoneHistoryVo milestoneHistoryVo=milestoneHistoryVos.get(i);
				UserInfo userInfo=itcMvcService.getUserInfoById(milestoneHistoryVo.getMilestoneHistoryUser());
				milestoneHistoryVo.setUserName(userInfo.getUserName());
			}
		}
		return milestoneHistoryVos;
	}

}
