package com.timss.atd.flow.zjw.abnormity.v001;

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
 * 分管领导
 */
public class AbnormityFgldshHandler extends TaskHandlerBase {

    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="ZJW AbnormityFgldshHandler";
    
    Logger logger = LoggerFactory.getLogger( AbnormityFgldshHandler.class );
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        logger.info( handlerName+" beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        logger.info( handlerName+" init" );
        wfUtil.updateAbnormityAuditStatusByTask(taskInfo, ProcessStatusUtil.FGLDSH);
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	logger.info(  handlerName+" onComplete" );
        super.onComplete( taskInfo );
    }

    
}
