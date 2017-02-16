package com.timss.workorder.flow.zjw.wo.v001;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
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

public class MonitorAudit extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
	@Autowired
	private WorkOrderDao workOrderDao;
    @Autowired
    private WoUtilService woUtilService;
    private static Logger logger = Logger.getLogger(MonitorAudit.class);

    @Override
    public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "MonitorAudit init --- instantId = " + instantId + "-- businessId = " + woId );
        
        //更新工单状态
        woUtilService.updateWoStatus(woId, WoProcessStatusUtilZJW.MONITOR_AUDIT);
        super.init( taskInfo );
    }

    @Override
	public void onComplete(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
		String monitorHandleStyle = workflowService.getVariable(taskInfo.getProcessInstanceId(), "monitorHandleStyle").toString();
        logger.info( "MonitorAudit onComplete --- instantId = " + instantId + "-- businessId = " + woId );
    	
        //如果是立即处理更新班组成员和工作负责人
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		if(monitorHandleStyle!=null && monitorHandleStyle.equals("REPAIR_NOW")){
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
				WorkOrder tempObject = JsonHelper.toObject(woFormDataStr, WorkOrder.class);
				workOrder = new WorkOrder();
				workOrder.setId(Integer.valueOf(woId));
				//工作策划节点处理人作为工作负责人
				String userIds = userInfoScope.getParam("userIds");
				String nextAuditUserIds = "";
				if(userIds != null){
					HashMap<String, List<String>> userIdsMap = (HashMap<String, List<String>>) new ObjectMapper().readValue(userIds, Map.class);
					Iterator iterator = userIdsMap.keySet().iterator();
					while(iterator.hasNext()) {
						List<String> auditUserId = userIdsMap.get(iterator.next());
						for (int i = 0; i < auditUserId.size(); i++) {
							nextAuditUserIds = auditUserId.get(i)+",";
						}
						nextAuditUserIds = nextAuditUserIds.substring(0,nextAuditUserIds.length()-1);
					}
				}
				workOrder.setEndReportUser(nextAuditUserIds);//工作负责人
				workOrder.setWoMaintainExecutorName(tempObject.getWoMaintainExecutorName());//班组成员
				workOrder.setSendWoTime(new Date());//下发时间
		        workOrder.setModifydate(new Date());
		        workOrder.setModifyuser(userInfoScope.getUserId());
				workOrderDao.updateWoAuditInfoZJW(workOrder);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			} 			
		}

		//修改处理人
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"normal");
	    super.onComplete( taskInfo );
	}
    
	@Override
	public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
	    String instantId = taskInfo.getProcessInstanceId();
	    String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
	    logger.info( "MonitorAudit beforeRollback --- instantId = " + instantId + "-- businessId = " + woId );
		
		//修改处理人
	    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"rollback");
	    super.beforeRollback(taskInfo, destTaskKey);		
	}	

}
