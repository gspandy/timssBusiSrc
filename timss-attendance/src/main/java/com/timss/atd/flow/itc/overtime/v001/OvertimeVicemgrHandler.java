package com.timss.atd.flow.itc.overtime.v001;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 加班申请--分管副总经理审批
 */
public class OvertimeVicemgrHandler extends TaskHandlerBase {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="ITC OvertimeVicemgrHandler";
    Logger log = LoggerFactory.getLogger( OvertimeVicemgrHandler.class );
    
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
    	wfUtil.updateOvertimeAuditStatusByTask(taskInfo, ProcessStatusUtil.FGFZJLPZ);
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	log.info( handlerName+" onComplete" );
        super.onComplete( taskInfo );
    }
}
