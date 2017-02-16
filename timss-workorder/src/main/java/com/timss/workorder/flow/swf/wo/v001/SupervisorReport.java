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

public class SupervisorReport extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WorkOrderDao workOrderDao;
    @Autowired
    private WoUtilService woUtilService;
    private static Logger logger = Logger.getLogger( SupervisorReport.class );

    @Override
    public void init(TaskInfo taskInfo) {
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        logger.info( "SupervisorReport init --- instantId = " + instantId + "-- businessId = " + woId );
        // 更新工单状态
        woUtilService.updateWoStatus( woId, WoProcessStatusUtil.SUPERVISOR_REPORT_STR );
       // super.init( taskInfo );
    }

    @Override
    public void onComplete(TaskInfo taskInfo) {
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        logger.info( "SupervisorReport onComplete --- instantId = " + instantId + "-- businessId = " + woId );
        Date nowTime = new Date();
        Date appointTime = workOrderDao.queryWOById( Integer.valueOf( woId ) ).getAppointTime();
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );
        } catch (Exception e) {
            logger.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject obj = JSONObject.fromObject( businessData );
        String reportFormDataStr = obj.getString( "reportForm" );

        try {
            // 修改工单信息
            WorkOrder tempObject = JsonHelper.toObject( reportFormDataStr, WorkOrder.class );
            WorkOrder workOrder = new WorkOrder();
            workOrder.setId( Integer.valueOf( woId ) );
            if ( tempObject.getWoIsDelay().equals( "Y" ) ) {
                workOrder.setWoIsDelay( "Y" );
                workOrder.setWoDelayType( tempObject.getWoDelayType() );
                workOrder.setWoDelayReason( tempObject.getWoDelayReason() );
                workOrder.setWoDelayLenEnum( tempObject.getWoDelayLenEnum() );
                workOrder.setWoDelayLen( tempObject.getWoDelayLen() );
            } else {
                // workOrder.setWoIsDelay("N"); //以防覆盖Y
                workOrder.setBeginTime( tempObject.getBeginTime() );
                workOrder.setEndTime( tempObject.getEndTime() );
                workOrder.setEndReport( tempObject.getEndReport() );
                workOrder.setFeedbackRemarks( tempObject.getFeedbackRemarks() );
                //如果是回填的话，就要更新解决时长信息
                long solveLen = (nowTime.getTime() - appointTime.getTime())/1000;
                workOrder.setSolveLen( solveLen );
            }
            workOrder.setFeedbackTime( nowTime );// 回填时间
            workOrder.setModifydate( nowTime );
            workOrder.setModifyuser( userInfoScope.getUserId() );
            workOrderDao.updateWorkOrderReport( workOrder );
        } catch (Exception e) {
            logger.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        // 修改处理人
        woUtilService.updateWoCurrHandlerUser( woId, userInfoScope, "normal" );
        super.onComplete( taskInfo );
    }

    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        logger.info( "SupervisorReport beforeRollback --- instantId = " + instantId + "-- businessId = " + woId );
        // 修改处理人
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        woUtilService.updateWoCurrHandlerUser( woId, userInfoScope, "rollback" );
        super.beforeRollback( taskInfo, destTaskKey );
    }
}
