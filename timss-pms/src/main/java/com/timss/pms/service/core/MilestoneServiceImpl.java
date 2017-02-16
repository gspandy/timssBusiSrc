package com.timss.pms.service.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Milestone;
import com.timss.pms.bean.MilestoneHistory;
import com.timss.pms.bean.Project;
import com.timss.pms.dao.MilestoneDao;
import com.timss.pms.service.MilestoneHistoryService;
import com.timss.pms.service.MilestoneService;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.vo.MilestoneVo;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;
@Service
public class MilestoneServiceImpl implements MilestoneService {
    @Autowired
    MilestoneDao milestoneDao;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    MilestoneHistoryService milestoneHistoryService;
    
    final String addOperator="add";
    final String updateOperator="update";
    final String deleteOperator="delete";
    Logger LOGGER=Logger.getLogger(MilestoneServiceImpl.class);

    
	@Override
	@Transactional
	public int insertMilestoneList(List<Milestone> milestones, Project project) {
		LOGGER.info("开始插入里程碑信息");
		if(milestones!=null){
			initMilestoneList(milestones,project);
			for(int i=0;i<milestones.size();i++){
				milestoneDao.insertMilestone(milestones.get(i));
			}
		}
		LOGGER.info("完成插入里程碑信息");
		return 0;
	}

	private void initMilestoneList(List<Milestone> milestones, Project project) {
		if(milestones!=null){
			for(int i=0;i<milestones.size();i++){
				Milestone milestone=milestones.get(i);
				milestone.setProjectId(project.getId());
				//初始化计划完成时间
				if(milestone.getExpectedTime()==null){
					milestone.setExpectedTime(milestone.getOriginTime());
				}
				InitUserAndSiteIdUtil.initCreate(milestone, itcMvcService);
			}
		}
		
	}

	@Override
	public List<MilestoneVo> queryMilestoneListByProjectId(String projectId) {
		LOGGER.info("查询里程碑信息，其项目id为"+projectId);
		List<MilestoneVo> milestoneVos=milestoneDao.queryMilestoneListByProjectId(Integer.valueOf(projectId));
		return milestoneVos;
	}

	@Override
	@Transactional
	public int updateMilestoneList(List<Milestone> milestones, Project project) {
		LOGGER.info("开始修改里程碑信息");
		milestoneDao.deleteMilestoneByProjectId(project.getId());
		insertMilestoneList(milestones, project);
		
		LOGGER.info("完成修改里程碑信息");
		return 0;
	}

	@Override
	@Transactional
	public int updateMilestoneAndRecordChange(List<Milestone> milestones,
			Project project) {
		LOGGER.info("开始变更里程碑信息,并保持变更记录");
		//获取旧的里程碑信息
		List<MilestoneVo> milestoneVos=milestoneDao.queryMilestoneListByProjectId(project.getId());
	    updateMilestoneList(milestones, project);
	    recordChange(milestones,milestoneVos,project.getId());
		LOGGER.info("完成修改里程碑信息");
		return 0;
	}
    /**
     * 记录里程碑的变更历史
     * @Title: recordChange
     * @param milestones
     * @param milestoneVos
     * @param id
     */
	private void recordChange(List<Milestone> newMilestones,
			List<MilestoneVo> oldMilestoneVos, Integer id) {
		List<Milestone> oldMilestones=convert2MilestoneList(oldMilestoneVos);
		//解析得到变更历史记录
		List<MilestoneHistory> milestoneHistories=produceMilestoneHistory(newMilestones,oldMilestones,id);
		milestoneHistoryService.insertMilestoneHistory(milestoneHistories);
	}

	/**
	 * 解析获取里程碑的变化
	 * @Title: produceMilestoneHistory
	 * @param newMilestones
	 * @param oldMilestones
	 * @param id
	 * @return
	 */
	private List<MilestoneHistory> produceMilestoneHistory(
			List<Milestone> newMilestones, List<Milestone> oldMilestones,
			Integer id) {
		List<MilestoneHistory> milestoneHistories=new ArrayList<MilestoneHistory>();
		if(oldMilestones==null){
			oldMilestones=new ArrayList<Milestone>();
		}
		if(newMilestones==null){
			newMilestones=new ArrayList<Milestone>();
		}
		
		//获取删除及修改的里程碑变化
		for(int i=0;i<oldMilestones.size();i++){
			Milestone oldMilestone=oldMilestones.get(i);
			Milestone milestone=isExistedInList(oldMilestone, newMilestones);
			if(milestone==null){
				milestoneHistories.add(createDeleteMilestoneHistory(oldMilestone, id));
			}else{
				milestoneHistories.addAll(createUpdateMilestoneHistory(oldMilestone, milestone, id));
			}
			
		}
		//获取新增的里程碑变化
		for(int i=0;i<newMilestones.size();i++){
			Milestone newMilestone=newMilestones.get(i);
			Milestone milestone=isExistedInList(newMilestone, oldMilestones);
			if(milestone==null){
				milestoneHistories.add(createAddMilestoneHistory(newMilestone, id));
			}
		}
		
		return milestoneHistories;
	}

	private List<Milestone> convert2MilestoneList(
			List<MilestoneVo> oldMilestoneVos) {
		List<Milestone> oldMilestones=null;
		if(oldMilestoneVos!=null){
			oldMilestones=new ArrayList<Milestone>();
			for(int i=0;i<oldMilestoneVos.size();i++){
				oldMilestones.add((Milestone)oldMilestoneVos.get(i));
			}
		}
		return oldMilestones;
	}
	
	//判断两个里程碑是否相同
	private boolean isEqual(Milestone milestone,Milestone milestone2){
		String name1=milestone.getMilestoneName().trim();
		String name2=milestone2.getMilestoneName().trim();
		return name1.equals(name2);
	}
	
	private boolean isEqual(Date date1,Date date2){
		
		if(date1==null || date2==null){
			if(date2==date1){
				return true;
			}
		}else {
			if(date1.getDay()==date2.getDay() && date1.getMonth()==date2.getMonth() && date1.getYear()==date2.getYear()){
				return true;
			}
		}
		return false;
	}
	
	private String convert2String(Date date){
		if(date==null){
			return "空";
		}
		DateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd");
		return dateFormat.format(date);
	}
	
	private String convert2String(String string){
		if(StringUtils.isBlank(string)){
			return "空";
		}
		return string;
	}
	
	//判断里程碑是否列表上,是则返回列表上的milestone
	private Milestone isExistedInList(Milestone milestone,List<Milestone> milestones){
		Milestone r=null;
		if(milestones!=null){
			for(int i=0;i<milestones.size();i++){
				if(isEqual(milestone, milestones.get(i))){
					r=milestones.get(i);
					break;
				}
			}
		}
		return r;
	}
	
	private MilestoneHistory createAddMilestoneHistory(Milestone milestone,Integer projectId){
		MilestoneHistory milestoneHistory=createBaseMilestoneHistory(projectId);
		milestoneHistory.setOperator(addOperator);
		String content="新增里程碑"+milestone.getMilestoneName();
		milestoneHistory.setContent(content);
		return milestoneHistory;
	}
	
	private MilestoneHistory createDeleteMilestoneHistory(Milestone milestone,Integer projectId){
		MilestoneHistory milestoneHistory=createBaseMilestoneHistory(projectId);
		milestoneHistory.setOperator(deleteOperator);
		String content="删除里程碑"+milestone.getMilestoneName();
		milestoneHistory.setContent(content);
		return milestoneHistory;
	}
	
	private List<MilestoneHistory> createUpdateMilestoneHistory(Milestone oldMilestone,Milestone newMilestone,Integer projectId){
		List<MilestoneHistory> milestoneHistories=new ArrayList<MilestoneHistory>();
		MilestoneHistory milestoneHistory=createUpdateHistoryExpectedTime(oldMilestone, newMilestone, projectId);
		if(milestoneHistory!=null){
			milestoneHistories.add(milestoneHistory);
		}
		
		milestoneHistory=createUpdateHistoryActualTime(oldMilestone, newMilestone, projectId);
		if(milestoneHistory!=null){
			milestoneHistories.add(milestoneHistory);
		}
		
		milestoneHistory=createUpdateHistoryCommand(oldMilestone, newMilestone, projectId);
		if(milestoneHistory!=null){
			milestoneHistories.add(milestoneHistory);
		}
		return milestoneHistories;
	}
	//判断计划时间是否变更，并生产里程碑变更历史
	private MilestoneHistory createUpdateHistoryExpectedTime(Milestone oldMilestone,Milestone newMilestone,Integer projectId){
		MilestoneHistory milestoneHistory=null;
		if(!isEqual(oldMilestone.getExpectedTime(),newMilestone.getExpectedTime())){
			milestoneHistory=createBaseMilestoneHistory(projectId);
			milestoneHistory.setOperator(updateOperator);
			String content="修改里程碑"+oldMilestone.getMilestoneName()+"的计划日期，从"+convert2String(oldMilestone.getExpectedTime())+
					"变为"+convert2String(newMilestone.getExpectedTime());
			milestoneHistory.setContent(content);
		}
		return milestoneHistory;
	}
	
	private MilestoneHistory createUpdateHistoryActualTime(Milestone oldMilestone,Milestone newMilestone,Integer projectId){
		MilestoneHistory milestoneHistory=null;
		if(!isEqual(oldMilestone.getActualTime(), newMilestone.getActualTime())){
			milestoneHistory=createBaseMilestoneHistory(projectId);
			milestoneHistory.setOperator(updateOperator);
			String content="修改里程碑"+oldMilestone.getMilestoneName()+"的完成日期，从"+convert2String(oldMilestone.getActualTime())+
					"变为"+convert2String(newMilestone.getActualTime());
			milestoneHistory.setContent(content);
		}
		return milestoneHistory;
	}
	
	private MilestoneHistory createUpdateHistoryCommand(Milestone oldMilestone,Milestone newMilestone,Integer projectId){
		MilestoneHistory milestoneHistory=null;
		String newCommand=newMilestone.getCommand();
		String oldCommand=oldMilestone.getCommand();
		boolean isequal=false;
		if(StringUtils.isBlank(newCommand)){
			if(StringUtils.isBlank(oldCommand)){
				isequal=true;
			}
		}else if(newCommand.equals(oldCommand)){
			isequal=true;
		}
		if(!isequal){
			milestoneHistory=createBaseMilestoneHistory(projectId);
			milestoneHistory.setOperator(updateOperator);
			String content="修改里程碑"+oldMilestone.getMilestoneName()+"的备注，从"+convert2String(oldCommand)+"变为"+convert2String(newCommand);
			milestoneHistory.setContent(content);
		}
		return milestoneHistory;
	}
	private MilestoneHistory createBaseMilestoneHistory(Integer projectId){
		MilestoneHistory milestoneHistory=new MilestoneHistory();
		milestoneHistory.setProjectId(projectId);
		milestoneHistory.setCreateTime(new Date());
		milestoneHistory.setTime(new Date());
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		String userId=userInfo.getUserId();
		milestoneHistory.setCreateUser(userId);
		milestoneHistory.setSiteid(userInfo.getSiteId());
		milestoneHistory.setMilestoneHistoryUser(userId);
		return milestoneHistory;
	}

	@Override
	public int deleteMilestoneListByProjectId(String projectId) {
		milestoneDao.deleteMilestoneByProjectId(Integer.valueOf(projectId));
		return 0;
	}

}
