package com.timss.workorder.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.MaintainPlan;
import com.timss.workorder.dao.MaintainPlanDao;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.JobPlanService;
import com.timss.workorder.service.MaintainPlanService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service("MaintainPlanServiceImpl")
public class MaintainPlanServiceImpl implements MaintainPlanService {
	@Autowired
	private ItcMvcService ItcMvcService;
	@Autowired
	private MaintainPlanDao maintainPlanDao;
	@Autowired
	WorkOrderDao workOrderDao;
//	@Autowired
//	private WorkflowService workflowService;
	@Autowired
	@Qualifier("JobPlanServiceImpl")
	private JobPlanService jobPlanService;
//	@Autowired
//	HomepageService homepageService;
	
	private static Logger logger = Logger.getLogger(MaintainPlanServiceImpl.class);
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public MaintainPlan insertMaintainPlan(Map<String,String> addMTPDataMap) throws Exception {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		String deptId = userInfoScope.getOrgId();
		String userId = userInfoScope.getUserId();
		Integer jpId = null ;
		String mtpSource = addMTPDataMap.get("mtpSource");
		if(!"remainFault_maintainPlan".equals(mtpSource)){  //如果不是从遗留问题来
			/** 插入维护计划的作业方案 */
			jpId = jobPlanService.insertJobPlan(addMTPDataMap);
		}
		
		
		/**  插入维护计划列表 */
		String mtpFormData = addMTPDataMap.get("mtpFormData");  //form表单数据
		MaintainPlan maintainPlan = JsonHelper.fromJsonStringToBean(mtpFormData, MaintainPlan.class);
		
		int mtpId = maintainPlanDao.getNextMTPId(); //获取要插入维护计划的ID
		maintainPlan.setId(mtpId);
		maintainPlan.setJobPlanId(jpId);
		
		if("remainFault_maintainPlan".equals(mtpSource)){  //如果是从遗留问题来,remainFault_maintainPlan
			JSONObject obj = JSONObject.fromObject(mtpFormData);
			int woId = Integer.valueOf((String)obj.get("woId"));
			maintainPlan.setPreWO(woId);
			maintainPlan.setMaintainPlanFrom("remainFault_maintainPlan");
		}else if("cycle_maintainPlan".equals(mtpSource)){
			maintainPlan.setMaintainPlanFrom("cycle_maintainPlan");//cycle_maintainPlan:周期维护计划
		}else if("noHandler_maintainPlan".equals(mtpSource)){
			maintainPlan.setMaintainPlanFrom("noHandler_maintainPlan");//noHandler_maintainPlan:不立即处理单,
		}
		
		maintainPlan.setCreateuser(userId);
		maintainPlan.setCreatedate(new Date());
		maintainPlan.setModifydate(new Date());
		maintainPlan.setModifyuser(userId);
		maintainPlan.setSiteid(siteId);
		maintainPlan.setDeptid(deptId);
		maintainPlan.setYxbz(1);
		
		maintainPlanDao.insertMaintainPlan(maintainPlan);
		
		return maintainPlan;
	}

	@Override
	public Page<MaintainPlan> queryAllMTP(Page<MaintainPlan> page) throws Exception {
		List<MaintainPlan> ret = maintainPlanDao.queryAllMTP(page);
		page.setResults(ret);
		logger.info("查询维护计划列表信息");
		return page;
	}
	
	@Override
	public Page<MaintainPlan> queryAllParentMTP(Page<MaintainPlan> page) {
		
		List<MaintainPlan> ret = maintainPlanDao.queryAllParentMTP(page);
		page.setResults(ret);
		logger.info("查询维护计划列表信息");
		return page;
	}
	
	@Override
	public MaintainPlan queryMTPById(int id) {
		return  maintainPlanDao.queryMTPById(id);
	}
	
	
	@Override
	public int getNextMTPId() {
		return maintainPlanDao.getNextMTPId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateMaintainPlan(Map<String, String> mtpDataMap) throws Exception {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
//		String siteId = userInfoScope.getSiteId();
//		String deptId = userInfoScope.getOrgId();
		String userId = userInfoScope.getUserId();
		 
		String mtpFormData = mtpDataMap.get("mtpFormData");
		MaintainPlan maintainPlan =JsonHelper.fromJsonStringToBean(mtpFormData, MaintainPlan.class);
		
		//int mtpId = maintainPlan.getId(); //获取要修改的维护计划的ID
		maintainPlan.setModifydate(new Date());
		maintainPlan.setModifyuser(userId);
		
		int jpId = jobPlanService.updateJobPlan(mtpDataMap);  //更新维护计划对应的策划信息
		
		maintainPlan.setJobPlanId(jpId);
		maintainPlanDao.updateMaintainPlan(maintainPlan);  //更新维护计划form信息
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateMTPToUnvailable(int maintainPlanId) {
		maintainPlanDao.updateMTPToUnvailable(maintainPlanId); 
	}




}
