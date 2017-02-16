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

public class ExpertDeal  extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private WoUtilService woUtilService;
	private static Logger logger = Logger.getLogger(ExpertDeal.class);
	
    @Override
	public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "ExpertDeal init --- instantId = " + instantId + "-- businessId = " + woId );
        //更新工单状态
        woUtilService.updateWoStatus(woId, WoProcessStatusUtil.EXPERT_DEAL_STR);
        super.init( taskInfo );
	}
	
    
    @Override
    public void onComplete(TaskInfo taskInfo){
    	
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "ExpertDeal onComplete --- instantId = " + instantId + "-- businessId = " + woId );
    	
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		WorkOrder workOrder = new WorkOrder();
		try {
			//修改工单信息
			workOrder.setId(Integer.valueOf(woId));
	        workOrder.setPartnerIds(userInfoScope.getUserId());//专工ID
	        workOrder.setSendWoTime(new Date());//专工下发缺陷时间
	        workOrder.setModifydate(new Date());
	        workOrder.setModifyuser(userInfoScope.getUserId());
			workOrderDao.updateWorkOrderExpert(workOrder);
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
        logger.info( "ExpertDeal beforeRollback --- instantId = " + instantId + "-- businessId = " + woId );
		//修改处理人
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"rollback");
	    super.beforeRollback(taskInfo, destTaskKey);		
	}		
	
}
