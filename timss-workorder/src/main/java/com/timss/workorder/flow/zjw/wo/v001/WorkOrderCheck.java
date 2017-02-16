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

public class WorkOrderCheck  extends TaskHandlerBase {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WoUtilService woUtilService;
	@Autowired
	private WorkOrderDao workOrderDao;
	private static Logger logger = Logger.getLogger(WorkOrderCheck.class);
	
    @Override
	public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "WorkOrderCheck init --- instantId = " + instantId + "-- businessId = " + woId );
        //更新工单状态
        woUtilService.updateWoStatus(woId, WoProcessStatusUtilZJW.WORK_ORDER_CHECK);
        super.init( taskInfo );
	}
	
    @Override
    public void onComplete(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        String checkHandleStyle = workflowService.getVariable(taskInfo.getProcessInstanceId(), "checkHandleStyle").toString();
        logger.info( "WorkOrderCheck onComplete --- instantId = " + instantId + "-- businessId = " + woId );
    	
        //如果是验收通过则更新汇报信息
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		JSONObject obj = JSONObject.fromObject(businessData);
		String  woFormDataStr = obj.getString("checkForm");
		WorkOrder workOrder;
		try {
	        //修改工单信息  
			WorkOrder tempObject = JsonHelper.toObject(woFormDataStr, WorkOrder.class);
			workOrder = new WorkOrder();
	        if(checkHandleStyle!=null && checkHandleStyle.equals("Y")){
    			workOrder.setId(Integer.valueOf(woId));
    			workOrder.setDefaultEndTime(new Date());//验收时间
    			workOrder.setLoseElectricPower(tempObject.getLoseElectricPower());//损失电量
    			workOrder.setApproveStopTime(tempObject.getApproveStopTime());//累计故障小时
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

        //更新工单状态
        woUtilService.updateWoStatus(woId, WoProcessStatusUtilZJW.DONE);
	    super.onComplete( taskInfo );
    }
	    
    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "WorkOrderCheck beforeRollback --- instantId = " + instantId + "-- businessId = " + woId );
		//修改处理人
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"rollback");
	    super.beforeRollback(taskInfo, destTaskKey);		
    }
}
