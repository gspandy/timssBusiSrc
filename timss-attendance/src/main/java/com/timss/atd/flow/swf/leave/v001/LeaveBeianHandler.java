package com.timss.atd.flow.swf.leave.v001;

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
 * 备案
 */
public class LeaveBeianHandler extends TaskHandlerBase {

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
    
    Logger logger = LoggerFactory.getLogger( LeaveBeianHandler.class );
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        logger.info( " 备案 beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        
        logger.info( "备案 init" );
        
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId").toString();
        
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.SWF_BEIAN );
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
