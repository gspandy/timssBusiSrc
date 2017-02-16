package com.timss.atd.flow.dpp.leave.v001;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 请假申请--部门审批
 */
public class LeaveLdspHandler extends TaskHandlerBase {
    Logger log = LoggerFactory.getLogger( LeaveLdspHandler.class );
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private WorkflowService workflowService;
    final String handlerName="DPP LeaveLdspHandler";
    
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        log.info( handlerName+" beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        log.info( handlerName+" init" );
        wfUtil.updateLeaveAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_LEAVE_LDSP);
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        log.info( handlerName+" onComplete" );
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId" ).toString();
        wfUtil.updateLeaveAuditStatusByTask(taskInfo, ProcessStatusUtil.CLOSED);
        try {
            	wfUtil.checkWorkStatusAndStatByLeaveId(Integer.parseInt(id));
    	} catch (Exception e) {
    		log.error("error checkWorkStatusAndStatByLeaveId("+id+")",e);
    	}
        super.onComplete( taskInfo );
    }
}
