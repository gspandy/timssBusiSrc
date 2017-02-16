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

public class DutyConfirmDefect  extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WoUtilService woUtilService;
	
	private static Logger logger = Logger.getLogger(DutyConfirmDefect.class);
	
    @Override
    public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "DutyConfirmDefect init --- instantId = " + instantId + "-- businessId = " + woId );
        
        //更新工单状态
        woUtilService.updateWoStatus(woId, WoProcessStatusUtil.DUTY_CONFIRM_DEFECT_STR);
        super.init( taskInfo );
	}
    
    @Override
    public void onComplete(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "DutyConfirmDefect onComplete --- instantId = " + instantId + "-- businessId = " + woId );
    	
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		JSONObject obj = JSONObject.fromObject(businessData);
		String  woFormDataStr = obj.getString("workOrderForm");
		WorkOrder workOrder;
		try {
			//修改工单信息  
			workOrder = JsonHelper.toObject(woFormDataStr, WorkOrder.class);
			workOrder.setAppointTime(new Date());//确认缺陷时间
			workOrder.setFaultConfrimUser(userInfoScope.getUserId());//确认缺陷人 
			//faultDegreeCode（是否缺陷）在woFormDataStr已包含
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
	}
    
    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "DutyConfirmDefect beforeRollback --- instantId = " + instantId + "-- businessId = " + woId );
    	
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		JSONObject obj = JSONObject.fromObject(businessData);
		String  woFormDataStr = obj.getString("workOrderForm");
		WorkOrder workOrder;
		try {
			//修改工单信息
			workOrder = JsonHelper.toObject(woFormDataStr, WorkOrder.class);
	        workOrder.setModifydate(new Date());
	        workOrder.setModifyuser(userInfoScope.getUserId());
			workOrderDao.updateWorkOrder(workOrder);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} 

		//修改处理人
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"rollback");
	    super.beforeRollback(taskInfo, destTaskKey);
	}
}
