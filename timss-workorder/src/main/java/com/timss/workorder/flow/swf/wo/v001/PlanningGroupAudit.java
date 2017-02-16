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

public class PlanningGroupAudit  extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private WoUtilService woUtilService;
	private static Logger logger = Logger.getLogger(PlanningGroupAudit.class);
	
    @Override
	public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "PlanningGroupAudit init --- instantId = " + instantId + "-- businessId = " + woId );
        //更新工单状态
        woUtilService.updateWoStatus(woId, WoProcessStatusUtil.PLANNING_GROUP_AUDIT_STR);
        super.init( taskInfo );
	}
	
    @Override
    public void onComplete(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "PlanningGroupAudit onComplete --- instantId = " + instantId + "-- businessId = " + woId );
    	
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		JSONObject obj = JSONObject.fromObject(businessData);
		String woFormDataStr = obj.getString("workOrderForm");
		String isReAssign = obj.getString("isReAssign");
		WorkOrder workOrder;
		try {
			//修改工单信息
			workOrder = JsonHelper.toObject(woFormDataStr, WorkOrder.class);
			if(isReAssign!=null && isReAssign.equals("Y")){ 	//是否重新分配
				workOrder.setIsAdditionWO(Integer.valueOf(1));
			}
			else{
				workOrder.setIsAdditionWO(Integer.valueOf(0));
			}
			workOrder.setCycleEndTime(new Date());//重新分配时间
	        workOrder.setModifydate(new Date());
	        workOrder.setModifyuser(userInfoScope.getUserId());
			workOrderDao.updateWorkOrder(workOrder);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} 
		//修改处理人
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"normal");
	    super.onComplete( taskInfo );
        //更新工单状态
	    if(isReAssign!=null && isReAssign.equals("N")){
	        woUtilService.updateWoStatus(woId, WoProcessStatusUtil.DONE_STR);
	    }
        super.init( taskInfo );
    }
	    
    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "PlanningGroupAudit beforeRollback --- instantId = " + instantId + "-- businessId = " + woId );
		//修改处理人
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"rollback");
	    super.beforeRollback(taskInfo, destTaskKey);		
	}			
	
}
