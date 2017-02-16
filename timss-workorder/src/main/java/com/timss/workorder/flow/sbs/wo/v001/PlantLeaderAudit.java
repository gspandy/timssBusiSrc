package com.timss.workorder.flow.sbs.wo.v001;

import java.util.Date;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.bean.MaintainPlan;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.MaintainPlanDao;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoUtilService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class PlantLeaderAudit extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private MaintainPlanDao maintainPlanDao;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WoUtilService woUtilService;
	
	private static Logger logger = Logger.getLogger(AssistantToTeamLeader.class);
	
    public void init(TaskInfo taskInfo){
    	
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String woStatus = "plantLeaderAudit";
    	woUtilService.updateWoStatus(woId, woStatus);
    	
	}
    
    public void onComplete(TaskInfo taskInfo){
    	
    	logger.debug("-------------进入‘场长审核’的onComplete(),开始处理业务逻辑-----------------");
    	
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		String siteId = userInfoScope.getSiteId();
		String deptId = userInfoScope.getOrgId();
		
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
//    	String processInstId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "processInstId").toString();
    	


    	String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		JSONObject obj = JSONObject.fromObject(businessData);
		String  handlerStyleFormDataStr= obj.getString("handlerStyleFormData");
		String  woFormDataStr = obj.getString("workOrderForm");
		
		JSONObject handlerStyleFormDataJson = JSONObject.fromObject(handlerStyleFormDataStr);
		String handlerStyle = handlerStyleFormDataJson.getString("isNowHandlerPlantLeader");
		
		if("laterHandlerPlantLeader".equals(handlerStyle)){ //如果是不立即处理，则入库维护计划列表
			WorkOrder workOrder;
			try {
				workOrder = JsonHelper.toObject(woFormDataStr, WorkOrder.class);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			} 
			MaintainPlan maintainPlan = new MaintainPlan();
			
			int mtpId = maintainPlanDao.getNextMTPId(); //获取要插入维护计划的ID
			maintainPlan.setId(mtpId);
			
			maintainPlan.setDescription(workOrder.getDescription());
			maintainPlan.setEquipId(workOrder.getEquipId());
			maintainPlan.setEquipName(workOrder.getEquipName());
			maintainPlan.setMaintainPlanFrom("noHandler_maintainPlan");
			maintainPlan.setSpecialtyId(workOrder.getWoSpecCode());
			maintainPlan.setPreWO(Integer.valueOf(woId));
			maintainPlan.setRemarks(workOrder.getRemarks());
			maintainPlan.setCreatedate(new Date());
			maintainPlan.setCreateuser(userId);
			maintainPlan.setSiteid(siteId);
			maintainPlan.setDeptid(deptId);
			maintainPlan.setYxbz(1);
			maintainPlanDao.insertMaintainPlan(maintainPlan);
			
			//修改工单的状态
			HashMap<String, Object> parmas = new HashMap<String, Object>();
    		parmas.put("woStatus", "woFiling");  //不立即处理的单，直接到已归档状态
    		parmas.put("id", Integer.valueOf(woId));
    		parmas.put("modifydate", new Date());
    		workOrderDao.updateWOStatus(parmas);
    		//将当前处理人清空
    		HashMap<String, String> parmas1 = new HashMap<String, String>();
			parmas1.put("woId", woId);
			parmas1.put("currHandlerUser", "");
			workOrderDao.updateOperUserById(parmas1);
		}else if("nowHandlerPlantLeader".equals(handlerStyle)||"toAssistantHandlerPlantLeader".equals(handlerStyle)){
			HashMap<String, String> parmas = new HashMap<String, String>();
			parmas.put("woId", woId);
			parmas.put("faultConfrimUser", userId);
			workOrderDao.updateOperUserById(parmas);
		}
		
		//修改工单中的场长处理方式
		HashMap<String, Object> handStyleMap = new HashMap<String, Object>();
		handStyleMap.put("attribute", "IS_NOWHANDLER_PLANTLEADER");//数据库表中对应的字段
		handStyleMap.put("value", handlerStyle);
		handStyleMap.put("id", woId);
		handStyleMap.put("modifyuser", userId);
		handStyleMap.put("modifydate", new Date());
		workOrderDao.updateWOHandlerStyle(handStyleMap);
		
		
		//修改当前执行人
		 woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"normal");
		
	}
    
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	//修改当前执行人
		try {
				String userId = userInfoScope.getParam("userId");
				HashMap<String, String> parmas = new HashMap<String, String>();
				parmas.put("woId", woId);
				parmas.put("currHandlerUser", userId);
				workOrderDao.updateOperUserById(parmas);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
