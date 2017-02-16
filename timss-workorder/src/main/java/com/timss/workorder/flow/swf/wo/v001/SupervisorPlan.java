package com.timss.workorder.flow.swf.wo.v001;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.util.WoProcessStatusUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class SupervisorPlan  extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private WoUtilService woUtilService;
	private static Logger logger = Logger.getLogger(SupervisorPlan.class);
	
    @Override
	public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "SupervisorPlan init --- instantId = " + instantId + "-- businessId = " + woId );
        //更新工单状态
        woUtilService.updateWoStatus(woId, WoProcessStatusUtil.SUPERVISOR_PLAN_STR);
        super.init( taskInfo );
	}
	
    @Override
    public void onComplete(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "SupervisorPlan onComplete --- instantId = " + instantId + "-- businessId = " + woId );
    	
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		JSONObject obj = JSONObject.fromObject(businessData);
		String  planFormDataStr = obj.getString("planForm");
		WorkOrder workOrder;
		try {
			//修改工单信息
			workOrder = JsonHelper.toObject(planFormDataStr, WorkOrder.class);
			workOrder.setId(Integer.valueOf(woId));
			workOrder.setEndReportUser(userInfoScope.getUserId());//写入项目负责人
			workOrder.setCycleBeginTime(new Date());//策划提交时间
	        workOrder.setModifydate(new Date());
	        workOrder.setModifyuser(userInfoScope.getUserId());
			workOrderDao.updateWorkOrderPlan(workOrder);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} 
		//修改处理人
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"normal");
	    super.onComplete( taskInfo );
    }
	    
    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "SupervisorPlan beforeRollback --- instantId = " + instantId + "-- businessId = " + woId );
		//修改处理人
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"rollback");
	    super.beforeRollback(taskInfo, destTaskKey);		
    }
}
