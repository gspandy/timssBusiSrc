package com.timss.workorder.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WoPriority;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoPriorityService;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.handler.ThreadLocalHandler;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title} 工单自动释放
 * @description: {desc}系统自动定时扫描  未上门开工，并且紧急度为危急的工单，如果当前时间超过了对应优先级的释放时间，
 * 则系统自动的将此工单退回给客服，并加上特殊标记（对于优先级高、紧急工单、自动释放工单，在工单列表中用红色标识），等待客服重新派单。
 * 是否释放工单的判断标准：1.	当前时间 > 派单时间 + 释放时间     2.	当前时间 > 报障时间 + 响应时间
 * @company: gdyd
 * @className: WoReleaseToCustSer.java
 * @author: 王中华
 * @createDate: 2014-10-31
 * @updateUser: 王中华
 * @version: 1.0
 */
@Component
@Lazy(false)
public class WoReleaseToCustSer {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	HomepageService homepageService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	WoPriorityService woPriorityService;
	@Autowired
    private IAuthorizationManager authManager;
	
	private static Logger logger = Logger.getLogger(WoReleaseToCustSer.class);
	

	@Scheduled(cron = "0 0/5 * * * ?")  //定时到每5分钟扫描一次
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void releaseWoByOvertime() throws Exception{
		logger.debug("工单自动释放定时任务扫描开始时间"+ new Date());
		//有优先级，工单处于“workPlan”状态
		List<WorkOrder> woList = workOrderDao.queryAllWoNoSiteId();
		
		for(int i=0; i<woList.size(); i++){
			WorkOrder tempWo = woList.get(i);
			int woId = tempWo.getId();  //工单Id
			int priorityId = tempWo.getPriorityId(); //优先级Id
			String siteid = tempWo.getSiteid();
			Date sendWoTime = tempWo.getSendWoTime(); //派单时间
			Date nowTime = new Date();
			WoPriority woPriority = (WoPriority) woPriorityService.queryWoPriorityById(priorityId,siteid).get("baseData");
			double releaseLength = woPriority.getReleaseLength(); //释放时间长度（小时）:这个时间长度必须工程师响应（对工程师而言）
			//自动释放判断逻辑
			long judgeTimeByRelease = sendWoTime.getTime() + (long)releaseLength*60*60*1000;   //派单时间 + 释放时间 
			boolean flag = false;
			
			if(nowTime.getTime() > judgeTimeByRelease){
				flag = true;
			}
			if(flag){  //满足了自动释放条件

				logger.debug("工单自动释放定时任务满足条件开始自动释放"+tempWo.toString());
				String siteId = tempWo.getSiteid();
				UserInfo userInfo = itcMvcService.getUserInfo(siteId+"scheduler",siteId);  //定时任务用户
				ThreadLocalHandler.createNewVarableOweUserInfo(userInfo);
				String schedulerUserId = userInfo.getUserId();   //定时任务默认用户
				
				List<SecureUser> tempList = authManager.retriveUsersWithSpecificRole(siteId+"_WO_KF", null, false, true);
				List<String> candidateList = new ArrayList<String>();  //派单环节的候选人列表
				String nextAuditUserIds = "";
				String nextAuditUserNames = "";
				for (int j = 0; j < tempList.size(); j++) {
					candidateList.add(tempList.get(j).getId());
					nextAuditUserIds += tempList.get(j).getId() + "," ;
					nextAuditUserNames += tempList.get(j).getName() + ",";
				}
				//TODO 查询工单流程当前状态,流程自动释放并且回到“派单”环节
				String processInstId = tempWo.getWorkflowId();
				
				/**参数1：流程实例ID
				 * 参数2：回退节点的key
				 * 参数3：审批意见
				 * 参数4：执行人
				 * 参数5：任务拥有人
				 * 参数6：历史执行人列表(所有客服)
				 * */
				workflowService.rollback(processInstId, "send_workorder", "自动释放，待重新派单", schedulerUserId, schedulerUserId, candidateList);
				
				//修改工单修改时间，当前处理人，派单新时间
				HashMap<String, String> parmas = new HashMap<String, String>();
				parmas.put("woId", String.valueOf(woId));
				parmas.put("currHandlerUser", nextAuditUserIds.substring(0, nextAuditUserIds.length()-1));
				parmas.put("currHandUserName", nextAuditUserNames.substring(0, nextAuditUserNames.length()-1));
				workOrderDao.updateCurrHandUserById(parmas);
				
				//增加工单释放记录
				workOrderDao.updateReleaseWo(String.valueOf(woId));
				
			}
		}
	}

}
