package com.timss.pms.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Plan;
import com.timss.pms.bean.PlanHistory;
import com.timss.pms.dao.PlanDao;
import com.timss.pms.dao.PlanHistoryDao;
import com.timss.pms.dao.ProjectDao;
import com.timss.pms.service.PlanService;
import com.timss.pms.util.AttachUtil;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.PlanDtlVo;
import com.timss.pms.vo.PlanVo;
import com.timss.pms.vo.ProjectVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @ClassName:     PlanServiceImpl
 * @company: gdyd
 * @Description: 年度计划的service接口的实现类（core版）
 * @author:    黄晓岚
 * @date:   2014-6-18 上午11:49:56
 */
@Service
public class PlanServiceImpl implements PlanService{
	@Autowired
	private PlanDao planDao;
	@Autowired
    private ItcMvcService itcMvcService;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	HomepageService homepageService;
	@Autowired
	ProjectDao projectDao;
	@Autowired
	PlanHistoryDao planHistoryDao;
    
    private static final Logger LOGGER = Logger.getLogger(PlanServiceImpl.class);
    
    private String prefixForHome="pms_plan_";

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertPlan(Plan plan){
		LOGGER.info("开始插入年度计划数据");
		//修改状态为审批通过
		ChangeStatusUtil.changeSToValue(plan, ChangeStatusUtil.approvalCode);
		//附加站点和人员等信息
		InitUserAndSiteIdUtil.initCreate(plan, itcMvcService);
		planDao.insertPlan(plan);
		//附加信息持久化
		AttachUtil.bindAttach(attachmentMapper, null, plan.getPlanAttach());
		LOGGER.info("插入年度计划数据成功");
	}

	@Override
	public Page<PlanVo> queryPlanList(Page<PlanVo> page, UserInfoScope userInfo) {
		LOGGER.info("开始查询年度计划数据");
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
	    List<PlanVo> plans=planDao.queryPlanList(page);
	    InitVoEnumUtil.initPlanVoList(plans, itcMvcService);
	    page.setResults(plans);
	    LOGGER.info("查询年度计划数据成功");
	    return page;
	}

	@Override
	@Transactional
	public void updatePlan(Plan plan) {
		LOGGER.info("开始修改并提交年度计划数据");
		ChangeStatusUtil.changeSToValue(plan, ChangeStatusUtil.approvalCode);
		//附加更新信息，人员和时间
		InitUserAndSiteIdUtil.initUpdate(plan, itcMvcService);
		//为获取旧的附件信息
		PlanDtlVo planDtlVo=planDao.queryPlanById(plan.getId());
		planDao.updatePlan(plan);
		//更改后台附件状态
		AttachUtil.bindAttach(attachmentMapper, planDtlVo.getPlanAttach(), plan.getPlanAttach());
		
		homepageService.Delete(createIdInHome(plan.getId()), itcMvcService.getUserInfoScopeDatas());
		LOGGER.info("完成修改并提交年度计划数据");
		
	}
	
	@Override
	@Transactional
	public void tmpUpdatePlan(Plan plan) {
		LOGGER.info("开始修改年度计划数据");
		ChangeStatusUtil.changeSToValue(plan, ChangeStatusUtil.draftCode);
		//附加更新信息，人员和时间
		InitUserAndSiteIdUtil.initUpdate(plan, itcMvcService);
		PlanDtlVo planDtlVo=planDao.queryPlanById(plan.getId());
		planDao.updatePlan(plan);
		AttachUtil.bindAttach(attachmentMapper, planDtlVo.getPlanAttach(), plan.getPlanAttach());
		LOGGER.info("修改年度计划数据成功");
	}

	@Override
	public PlanDtlVo queryPlanById(String id) {
		LOGGER.info("查询id为"+id+"的年度计划");
		PlanDtlVo plan=planDao.queryPlanById(Integer.parseInt(id));
		ArrayList<HashMap<String,Object>> attachMap=AttachUtil.generatAttach(plan.getPlanAttach());
		plan.setAttachMap(attachMap);
		
		//设置项目的查询条件
		List<ProjectVo> projectVos=projectDao.queryProjectListByPlanId(Integer.valueOf(id));
		InitVoEnumUtil.initProjectVoList(projectVos, itcMvcService);
		plan.setProjectVos(projectVos);
		return plan;
	}

	@Override
	@Transactional
	public int deletePlan(String id) {
		LOGGER.info("删除id为"+id+"的年度计划");
		//清除附件
		PlanDtlVo planDtlVo=planDao.queryPlanById(Integer.parseInt(id));
		AttachUtil.bindAttach(attachmentMapper, planDtlVo.getPlanAttach(),null);
		
		int count=planDao.deletePlan(Integer.parseInt(id));
		//删除首页草稿信息
		homepageService.Delete(createIdInHome(Integer.valueOf(id)), itcMvcService.getUserInfoScopeDatas());
		return count;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void tmpInsertPlan(Plan plan) {
		LOGGER.info("开始暂存年度计划数据");
		//转成草稿状态
		ChangeStatusUtil.changeSToValue(plan,ChangeStatusUtil.draftCode);
		//附加站点信息和创建人员等信息
		InitUserAndSiteIdUtil.initCreate(plan, itcMvcService);
		planDao.insertPlan(plan);
		AttachUtil.bindAttach(attachmentMapper, null, plan.getPlanAttach());
		String url="pms/plan/editPlanJsp.do?id="+plan.getId();
		//20151222 zhx 在待办名称中，增加模块名
		homepageService.createProcess("plan"+plan.getId(), createIdInHome(plan.getId()), "年度计划", "计划-" + plan.getPlanName(), "草稿", url, (UserInfo)itcMvcService.getUserInfoScopeDatas(), null);
		
		LOGGER.info("完成暂存年度计划数据");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> queryPlanListByKeyWord(String kw) {
		LOGGER.info("查询关键字为"+kw+"的年度计划");
		Page<PlanVo> page=new Page<PlanVo>(1,11);
		page.setFuzzyParameter("status", ChangeStatusUtil.approvalCode);
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		if(kw!=null && !"".equals(kw) ){
			page.setFuzzyParameter("plan_name", kw);
		}
		List<PlanVo> plans=planDao.queryPlanList(page);
		
		List<Map> maps=new ArrayList<Map>();
		if(plans!=null){
			for(int i=0;i<plans.size();i++){
				HashMap map=new HashMap();
				map.put("id", plans.get(i).getId());
				map.put("name", plans.get(i).getPlanName());
				map.put("ss", "hello");
				maps.add(map);
			}
		}
		return maps;
	}
	
	private String createIdInHome(int id){
		return prefixForHome+id;
	}

	@Override
	@Transactional
	public int changePlan(Plan plan) {
		LOGGER.info("开始变更年度计划数据");
		ChangeStatusUtil.changeSToValue(plan, ChangeStatusUtil.approvalCode);
		//附加更新信息，人员和时间
		InitUserAndSiteIdUtil.initUpdate(plan, itcMvcService);
		PlanDtlVo planDtlVo=planDao.queryPlanById(plan.getId());
		planDao.updatePlan(plan);
		//更新年度计划的历史信息，结转次数加一
		updateHistInfo(plan);
		AttachUtil.bindAttach(attachmentMapper, planDtlVo.getPlanAttach(), plan.getPlanAttach());
		//记录变更历史记录
		PlanHistory planHistory=convert2PlanHistory(plan);
		planHistoryDao.insertPlanHistory(planHistory);
		LOGGER.info("变更计划数据成功");
		return 0;
	}
    
	/**
	 * 更新年度计划的历史信息，结转次数加一
	 * @Title: updateHistInfo
	 * @param plan
	 */
	@Transactional
	private void updateHistInfo(Plan plan) {
		//初始化历年信息
		String id=plan.getId().toString();
		Double histIncome=getActualIncome(id);
		Double histCost=getActualCost(id);
		Double histProfit=histIncome-histCost;
		Double histPercent= (histIncome+histCost)/(plan.getProjectIncome()+plan.getProjectCost()) *100;
		plan.setHistCost(histCost);
		plan.setHistIncome(histIncome);
		plan.setHistPercent(histPercent);
		plan.setHistProfit(histProfit);
		//更新年度计划历年信息
		planDao.updateHistInfo(plan);
		//更新结转次数
		planDao.increaseCarryOverTimes(plan.getId());
	}

	private PlanHistory convert2PlanHistory(Plan plan) {
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		String userid=userInfo.getUserId();
		PlanHistory planHistory=new PlanHistory();
		planHistory.setAnnualCost(plan.getAnnualCost());
		planHistory.setAnnualIncome(plan.getAnnualIncome());
		planHistory.setChangingTime(new Date());
		planHistory.setChangingUser(userid);
		planHistory.setCommand(plan.getCommand());
		planHistory.setCreateTime(plan.getCreateTime());
		planHistory.setCreateUser(plan.getCreateUser());
		planHistory.setCustomManager(plan.getCustomManager());
		planHistory.setYear(plan.getYear());
		planHistory.setUpdateUser(plan.getUpdateUser());
		planHistory.setUpdateTime(plan.getUpdateTime());
		planHistory.setStartTime(plan.getStartTime());
		planHistory.setSiteid(plan.getSiteid());
		planHistory.setProjectLeader(plan.getProjectLeader());
		planHistory.setProjectIncome(plan.getProjectIncome());
		planHistory.setProjectCost(plan.getProjectCost());
		planHistory.setPlanType(plan.getType());
		planHistory.setPlanPercent(plan.getPlanPercent());
		planHistory.setPlanName(plan.getPlanName());
		planHistory.setPlanId(plan.getId());
		planHistory.setEndTime(plan.getEndTime());
		planHistory.setDeptid(plan.getDeptid());
		
		planHistory.setAnnualPercent(plan.getAnnualPercent());
		
		planHistory.setHistCost(plan.getHistCost());
		planHistory.setHistIncome(plan.getHistIncome());
		planHistory.setHistPercent(plan.getHistPercent());
		planHistory.setHistProfit(plan.getHistProfit());
		planHistory.setCarryOverTimes(plan.getCarryOverTimes());
		planHistory.setActualEndTime(plan.getActualEndTime());
		return planHistory;
	}

	@Override
	public double getActualCost(String planId) {
		Double cost=planDao.queryActualCost(planId);
		if(cost==null){
			cost=0.0;
		}
		return cost;
	}

	@Override
	public double getActualIncome(String planId) {
		Double income=planDao.queryActualIncome(planId);
		if(income==null){
			income=0.0;
		}
		return income;
	}

	@Override
	@Transactional
	public int changePlan(Plan plan, String processInstId) {
		changePlan(plan);
		if(StringUtils.isNotBlank(processInstId) && !"null".equals(processInstId)){
			LOGGER.info("删除待办提醒,其processInstId为"+processInstId);
			homepageService.Delete(processInstId, itcMvcService.getUserInfoScopeDatas());
		}
		
		return 0;
	}

	

}
