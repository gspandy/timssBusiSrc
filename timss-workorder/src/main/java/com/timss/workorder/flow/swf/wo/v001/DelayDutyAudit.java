package com.timss.workorder.flow.swf.wo.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.service.WorkOrderService;
import com.timss.workorder.util.WoProcessStatusUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class DelayDutyAudit extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WoUtilService woUtilService;

    private static Logger logger = Logger.getLogger( DelayDutyAudit.class );

    @Override
    public void init(TaskInfo taskInfo) {
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        logger.info( "DelayDutyAudit init --- instantId = " + instantId + "-- businessId = " + woId );
        // 更新工单状态
        woUtilService.updateWoStatus( woId, WoProcessStatusUtil.DELAY_DUTY_AUDIT_STR );
        super.init( taskInfo );
    }

    @Override
    public void onComplete(TaskInfo taskInfo) {
        String instantId = taskInfo.getProcessInstanceId();
        Object woIdObject = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
        if(woIdObject != null){
            String woId = woIdObject.toString();
            logger.info( "DelayDutyAudit onComplete --- instantId = " + instantId + "-- businessId = " + woId );
            // 修改处理人
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            woUtilService.updateWoCurrHandlerUser( woId, userInfoScope, "normal" );
        }
        super.onComplete( taskInfo );
    }

    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        logger.info( "DelayDutyAudit beforeRollback --- instantId = " + instantId + "-- businessId = " + woId );
        // 修改处理人
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        woUtilService.updateWoCurrHandlerUser( woId, userInfoScope, "rollback" );
        super.beforeRollback( taskInfo, destTaskKey );
    }
}
