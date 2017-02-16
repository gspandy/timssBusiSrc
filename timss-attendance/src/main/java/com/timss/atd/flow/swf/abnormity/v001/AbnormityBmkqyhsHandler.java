package com.timss.atd.flow.swf.abnormity.v001;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 考勤异常--部门考勤员核实
 */
public class AbnormityBmkqyhsHandler extends TaskHandlerBase {
    Logger log = LoggerFactory.getLogger( AbnormityBmkqyhsHandler.class );
    
    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="SWF AbnormityBmkqyhsHandler";
    
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
        wfUtil.updateAbnormityAuditStatusByTask(taskInfo, ProcessStatusUtil.BMKQYHS);
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
