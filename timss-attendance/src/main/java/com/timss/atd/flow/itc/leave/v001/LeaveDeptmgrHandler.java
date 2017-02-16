package com.timss.atd.flow.itc.leave.v001;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.dao.LeaveDao;
import com.timss.attendance.util.ProcessStatusUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 
 * @title: 部门经理审批
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveApplyHandler.java
 * @author: fengzt
 * @createDate: 2014年9月1日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class LeaveDeptmgrHandler extends TaskHandlerBase {

    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private LeaveDao leaveDao;
   
    
    Logger logger = LoggerFactory.getLogger( LeaveDeptmgrHandler.class );
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        logger.info( " LeaveDeptMgrHandler beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        
        logger.info( "LeaveDeptMgrHandler init" );
        
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId").toString();
                        
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.DEPTMGR );
        parmas.put( "instanceId", instanceId);
        parmas.put( "id", id );
        leaveDao.updateOperUserById(parmas);
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        
        super.onComplete( taskInfo );
    }

    
}
