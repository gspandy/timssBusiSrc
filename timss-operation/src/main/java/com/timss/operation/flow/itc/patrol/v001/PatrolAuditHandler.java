package com.timss.operation.flow.itc.patrol.v001;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.operation.dao.PatrolDao;
import com.timss.operation.util.ProcessStatusUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 
 * @title: 审批点检日志申请
 * @description: {desc}
 * @company: gdyd
 * @className: PatrolAuditHandler.java
 * @author: fengtw
 * @createDate: 2015年11月3日
 * @updateUser: fengtw
 * @version: 1.0
 */
public class PatrolAuditHandler extends TaskHandlerBase {
    Logger logger = LoggerFactory.getLogger( PatrolAuditHandler.class );
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private PatrolDao patrolDao;
   
    @Autowired
    private WorkflowService workflowService;
    
    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        String instantId = taskInfo.getProcessInstanceId();
        String patrolId = workflowService.getVariable( instantId, "businessId").toString();
        logger.info( "PatrolApplyHandler init --- instantId = " + instantId + "-- patrolId = " + patrolId );
                      
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "instantId", instantId);
        parmas.put( "status", ProcessStatusUtil.AUDIT_STR );//专业主管审批
        parmas.put( "patrolId", patrolId );
        patrolDao.updatePatrolStatus(parmas);
        
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        String instantId = taskInfo.getProcessInstanceId();
        String patrolId = workflowService.getVariable( instantId, "businessId").toString();
        logger.info( "PatrolApplyHandler complete --- instantId = " + instantId + "-- patrolId = " + patrolId );
                     
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "instantId", instantId);
        parmas.put( "status", ProcessStatusUtil.DONE_STR );//已完成
        parmas.put( "patrolId", patrolId );
        patrolDao.updatePatrolStatus(parmas);
        super.onComplete( taskInfo );
    }
}
