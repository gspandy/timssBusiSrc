package com.timss.atd.flow.itc.leave.v001;

import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.dao.LeaveDao;
import com.timss.attendance.service.StatService;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 
 * @title: 行政部登记备案
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveXzbBeianHandler.java
 * @author: fengzt
 * @createDate: 2014年9月1日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class LeaveXzbbeianHandler extends TaskHandlerBase {

    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private LeaveDao leaveDao;
   
    @Autowired
    private StatService statService;

    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    Logger logger = LoggerFactory.getLogger( LeaveXzbbeianHandler.class );
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        logger.info( " LeaveXzbBeianHandler beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        
        logger.info( "LeaveXzbBeianHandler init" );
        
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId").toString();
        
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.XZBDJBA );
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
        logger.info( "LeaveXzbBeianHandler onComplete" );
        
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId").toString();
        
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.CLOSED );
        parmas.put( "instanceId", instanceId);
        parmas.put( "id", id );
        parmas.put( "updateDate", new Date() );
        leaveDao.updateOperUserById(parmas);
        //更新统计信息
        //statService.updateCurrentYearStat();
        
        try {
        	wfUtil.checkWorkStatusAndStatByLeaveId(Integer.parseInt(id));
		} catch (Exception e) {
			logger.error("error checkWorkStatusAndStatByLeaveId("+id+")",e);
		}
        
        super.onComplete( taskInfo );
    }

    
}
