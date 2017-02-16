package com.timss.pms.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.timss.pms.bean.Plan;
import com.timss.pms.dao.PlanDao;
import com.timss.pms.service.PlanService;
import com.timss.pms.vo.PlanVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.handler.ThreadLocalHandler;
import com.yudean.mvc.service.ItcMvcService;

@Component
@Lazy(false)
public class PlanChangePerYearSchedule {
	@Autowired
	PlanService planService;
	@Autowired
	PlanDao planDao;
	@Autowired
	HomepageService homepageService;
	@Autowired
	ItcMvcService itcMvcService;
	
	Logger LOGGER=Logger.getLogger(PlanChangePerYearSchedule.class);
	//记录一次定时任务中生成多少个提醒待办
	static long num=0;
	
	@Scheduled(cron="0 0 1,3,5 1 1 *")//每年1月1日早上1，3,5点执行
	public void changePlanTodoList(){
		LOGGER.info("开始年度计划结转任务提醒");
		Long total=planDao.countPlan();
		LOGGER.info("年度计划总数"+total);
		int length=10;
		num=0;
		for(int i=0;i<total;i+=length){
			//获取需要创建的待办年度计划列表
			List<Plan> plans=getPlanList(i/length,length);
			//创建待办
			createTodoByPlan(plans);
		}
		LOGGER.info("完成年度计划结转任务提醒,生成记录数："+num);
	}
	private void createTodoByPlan(List<Plan> plans) {
		if(plans!=null){
			for(int i=0;i<plans.size();i++){
				Plan plan=plans.get(i);
				
				if(isPlanExisted(plan)){
					if(!isPlanEnd(plan)){
						try{
							LOGGER.info("为年度计划：id"+plan.getId()+" name:"+plan.getPlanName()+",生成结转待办提醒");
							HomepageWorkTask homepageWorkTask=convert2HomepageWorkTask(plan);
							List<String> users=new ArrayList<String>();
							users.add(plan.getCustomManager());
							
							UserInfo userinfo=itcMvcService.getUserInfo(plan.getCustomManager(),plan.getSiteid());
							ThreadLocalHandler.createNewVarableOweUserInfo(userinfo);
							//
							homepageService.createNotice(homepageWorkTask, users, userinfo);
							num++;
						}catch (Exception e) {
							LOGGER.warn("生成待办提醒时，失败",e);
						}
					}
				}
			}
		}
		
	}
	private boolean isPlanExisted(Plan plan) {
		if(plan!=null){
			return true;
		}
		return false;
	}
	/**
	 * 将年度计划信息转换为待办任务信息
	 * @Title: convert2HomepageWorkTask
	 * @param plan
	 * @return
	 */
	private HomepageWorkTask convert2HomepageWorkTask(Plan plan) {
		HomepageWorkTask homepageWorkTask=new HomepageWorkTask();
		if(plan!=null){
			int year=new Date().getYear();
			String siteid=plan.getSiteid();
			String id=plan.getId().toString();
			String processInstId=plan.getSiteid()+"_"+year+"_"+plan.getId();
			homepageWorkTask.setProcessInstId(processInstId);
			homepageWorkTask.setName(plan.getPlanName());
			homepageWorkTask.setType(HomepageWorkTask.TaskType.Process);
			homepageWorkTask.setTypeName("结转提醒");
			homepageWorkTask.setUrl("pms/plan/editPlanJsp.do?id="+id+"&notice=1&processInstId="+processInstId);
			homepageWorkTask.setFlow("plan_"+id);
		}
		
		return homepageWorkTask;
	}
	/**
	 * 年度计划是否结算完毕，如果否，则需要生产待办
	 * @Title: isPlanEnd
	 * @param plan
	 * @return
	 */
	private boolean isPlanEnd(Plan plan) {
		String id=plan.getId().toString();
		double cost=planService.getActualCost(id);
		double income=planService.getActualIncome(id);
		if(equalsDouble(plan.getProjectCost(),cost) && equalsDouble(plan.getProjectIncome(),income)){
			return true;
		}
		return false;
	}
	
	private boolean equalsDouble(double a,double b){
		if(Math.abs(a-b)<0.0001){
			return true;
		}
		return false;
	}
	public List<Plan> getPlanList(int start,int pageSize) {
		Page<PlanVo> page=new Page<PlanVo>(start, pageSize);
	    List<PlanVo> planVos=planDao.queryPlanList(page);
	    List<Plan> plans=new ArrayList<Plan>();
	    if(planVos!=null){
	    	for(int i=0;i<planVos.size();i++){
	    		plans.add(planVos.get(i));
	    	}
	    }
		return plans;
	}
}
