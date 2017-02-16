package com.timss.atd.flow.itc.abnormity.v001;

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
 * 考勤异常--行政部审批
 */
public class AbnormityExecutHandler extends TaskHandlerBase {
    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private ItcMvcService ItcMvcService;
    
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="ITC AbnormityExecutHandler";
    
    Logger logger = LoggerFactory.getLogger( AbnormityExecutHandler.class );
    
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
    	logger.info(  handlerName+" beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
    	logger.info(  handlerName+" init" );
        wfUtil.updateAbnormityAuditStatusByTask(taskInfo, ProcessStatusUtil.XZBBEIAN);
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	logger.info(  handlerName+" onComplete" );
    	wfUtil.updateAbnormityAuditStatusByTask(taskInfo, ProcessStatusUtil.CLOSED);
    	
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId").toString();
        try {
			wfUtil.checkWorkStatusByAbnormityId(Integer.parseInt(id));
		} catch (Exception e) {
			logger.error("error checkWorkStatusByAbnormityId("+id+")",e);
		}
        super.onComplete( taskInfo );
    }
}
