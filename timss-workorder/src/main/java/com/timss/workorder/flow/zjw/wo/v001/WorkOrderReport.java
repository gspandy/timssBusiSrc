package com.timss.workorder.flow.zjw.wo.v001;

import java.util.Date;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.util.WoProcessStatusUtilZJW;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class WorkOrderReport  extends TaskHandlerBase {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WoUtilService woUtilService;
	@Autowired
	private WorkOrderDao workOrderDao;
	private static Logger logger = Logger.getLogger(WorkOrderReport.class);
	
    @Override
	public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "WorkOrderReport init --- instantId = " + instantId + "-- businessId = " + woId );
        //更新工单状态
        woUtilService.updateWoStatus(woId, WoProcessStatusUtilZJW.WORK_ORDER_REPORT);
        super.init( taskInfo );
	}
	
    @Override
    public void onComplete(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        String reportHandleStyle = workflowService.getVariable(taskInfo.getProcessInstanceId(), "reportHandleStyle").toString();
        logger.info( "WorkOrderReport onComplete --- instantId = " + instantId + "-- businessId = " + woId );
    	
        //如果是处理完成更新完工汇报信息
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		JSONObject obj = JSONObject.fromObject(businessData);
		String  woFormDataStr = obj.getString("reportForm");
		WorkOrder workOrder;
		try {
			//修改工单信息  
			WorkOrder tempObject = JsonHelper.toObject(woFormDataStr, WorkOrder.class);
			workOrder = new WorkOrder();
	        if(reportHandleStyle!=null && reportHandleStyle.equals("DONE")){
				workOrder.setId(Integer.valueOf(woId));
				workOrder.setNewFaultRemarks(tempObject.getNewFaultRemarks());//故障现象
				workOrder.setBeginTime(tempObject.getBeginTime());//实际开工时间
				workOrder.setEndTime(tempObject.getEndTime());//实际完工时间
				workOrder.setEndReport(tempObject.getEndReport());//故障处理情况
				workOrder.setFeedbackTime(new Date());//回填时间
		        workOrder.setModifydate(new Date());
		        workOrder.setModifyuser(userInfoScope.getUserId());
				workOrderDao.updateWoAuditInfoZJW(workOrder);
	        }
	        /*else{
				workOrder.setId(Integer.valueOf(woId));
		        workOrder.setModifydate(new Date());
		        workOrder.setModifyuser(userInfoScope.getUserId());
				workOrderDao.cleanWoReportInfoZJW(workOrder);
	        }*/
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
        logger.info( "WorkOrderReport beforeRollback --- instantId = " + instantId + "-- businessId = " + woId );
		//修改处理人
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"rollback");
	    super.beforeRollback(taskInfo, destTaskKey);		
    }
}
