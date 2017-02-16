package com.timss.itsm.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmMaintainPlan;
import com.timss.itsm.dao.ItsmMaintainPlanDao;
import com.timss.itsm.service.ItsmWoUtilService;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.handler.ThreadLocalHandler;
import com.yudean.mvc.service.ItcMvcService;

@Component
@Lazy(false)
public class ItsmMtpToNewTodo {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	HomepageService homepageService;
	@Autowired
	private ItsmMaintainPlanDao maintainPlanDao;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	ItsmWoUtilService woUtilService;
	
	private static final Logger LOG = Logger.getLogger(ItsmMtpToNewTodo.class);
	

	/**
	 * @description:  每次生成待办或者业务提醒之后，就将维护计划的下次生成时间增加一个周期
	 * @author: 王中华
	 * @createDate: 2014-11-15
	 * @throws Exception:
	 */
	@Scheduled(cron = "0 0 1 * * ?")  //定时到每天凌晨1点扫描生成待办
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public void cycMtpToNewTodo() throws Exception{
		LOG.debug("周期性维护计划生成工单定时任务扫描开始时间"+ new Date());
		List<ItsmMaintainPlan> mtpList = maintainPlanDao.queryAllCycMTP();
		 
		for(int i=0; i<mtpList.size(); i++){
			ItsmMaintainPlan tempMTP = mtpList.get(i);
			Date newToDoTime = tempMTP.getNewToDoTime(); //下次生成待办的时间
			Date currToDoTime = tempMTP.getCurrStartTime();
			Date nowTime = new Date();
			//下次生成待办的时间早于当前时间   或者   当前周期开始时间早于当前时间且下次生成待办时间为空（第一次生成）
			boolean flag = (newToDoTime==null&&currToDoTime.before(nowTime))||(newToDoTime.before(nowTime)); 
			//boolean flag = true;
			
			
			String siteId = tempMTP.getSiteid();
			String principal = tempMTP.getPrincipal();
			String workTeams = tempMTP.getWorkTeam();
			
			UserInfo userInfo = itcMvcService.getUserInfo(siteId+"scheduler",siteId);
			ThreadLocalHandler.createNewVarableOweUserInfo(userInfo);
			
			if(flag){  //预警期已到
				 //查询所有负责人
				 List<String> principalList = new ArrayList<String>();
				 principalList = woUtilService.selectPrincipalList(principal,workTeams);
				 
				if(tempMTP.getIsAutoGenerWo()==1){  //设置了直接生成工单
					 for (int j = 0; j < principalList.size(); j++) {
						 //为每一个负责人生成一条工单
						 woUtilService.cycMtpStartFlow(tempMTP,userInfo,principalList.get(j));
					 }
				}else{  //设置了不直接生成工单
					if(tempMTP.getHasAlertTodo()==0){  //此周期内未生成提醒待办（防止重复生成提醒待办）
						//将本条周期性维护计划记录添加到待办，双击之后进入维护计划详情里面，然后可以手动点击按钮生成工单
						String todoCode = "CYC"+tempMTP.getMaintainPlanCode();
						String ProcessId = "CYC"+tempMTP.getMaintainPlanCode(); //待办的唯一标识
						String jumpPath = "itsm/maintainPlan/queryFullMTPPage.do?todoId="+ProcessId+"&maintainPlanId="+tempMTP.getId();
						
						// 构建Bean
				        HomepageWorkTask homeworkTask = new HomepageWorkTask();
				        homeworkTask.setFlow(todoCode);// 编号，如工单编号CYCMTP20140902001
				        homeworkTask.setName("周期工单生成"); // 名称
				        homeworkTask.setProcessInstId(ProcessId);
				        homeworkTask.setStatusName("新建工单"); // 状态
				        homeworkTask.setType(HomepageWorkTask.TaskType.Process); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft 草稿;xxxx.Process 流程实例
				        homeworkTask.setTypeName("周期工单"); // 类别
				        homeworkTask.setUrl(jumpPath);  // 扭转的URL
				        
				        //加入待办列表(业务提醒)
				        homepageService.createNotice(homeworkTask, principalList, userInfo);
				        
						maintainPlanDao.updateMTPhasAlertTodo(tempMTP.getId(),1);  //1代表已经生成提醒待办，0待办未生成提醒待办
					}
				}
				// 修改维护计划的下次生成    待办 或者 业务提醒  时间,和当前开始时间
				if(newToDoTime==null){
					newToDoTime = new Date();
				}
				long newToDoTimeLong = newToDoTime.getTime() + tempMTP.getMaintainPlanCycle()*24*60*60*1000 ;
				newToDoTime = new Date(newToDoTimeLong);
				LOG.debug("周期（天）:--------------"+tempMTP.getMaintainPlanCycle());
				LOG.debug("新生产待办时间:--------------"+newToDoTime);
				maintainPlanDao.updateMTPTodoTime(new Date(),newToDoTime,tempMTP.getId());
			}//end if
		}
	}

	
	
}
