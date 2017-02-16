package com.timss.atd.flow.dpp.leave.v001;

import java.util.List;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.dao.LeaveDao;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 请假申请--部门审批
 */
public class LeaveBmspHandler extends TaskHandlerBase {
    Logger log = LoggerFactory.getLogger( LeaveBmspHandler.class );
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private LeaveDao leaveDao;
    final String handlerName="DPP LeaveBmspHandler";
    
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
        wfUtil.updateLeaveAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_LEAVE_BMSP);
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
        String finish = workflowService.getVariable( instanceId, "finish").toString();
        if("BMSP".equals(finish)){
        	wfUtil.updateLeaveAuditStatusByTask(taskInfo, ProcessStatusUtil.CLOSED);
        	try {
            	wfUtil.checkWorkStatusAndStatByLeaveId(Integer.parseInt(id));
    		} catch (Exception e) {
    			log.error("error checkWorkStatusAndStatByLeaveId("+id+")",e);
    		}
        }
        super.onComplete( taskInfo );
    }
    
    @Override
    public void onShowAudit(String taskId) {
    	Task taskInfo = workflowService.getTaskByTaskId(taskId);
    	String instanceId = taskInfo.getProcessInstanceId(); 
        Integer id=Integer.parseInt(workflowService.getVariable( instanceId, "businessId" ).toString());
        LeaveBean leaveBean = leaveDao.queryLeaveById(id);
        List<LeaveItemBean> leaveItemVos = leaveDao.queryLeaveItemList( String.valueOf(id) );
        
        for ( LeaveItemBean vo : leaveItemVos ){
        	String category=vo.getCategory();
        	Double days=vo.getLeaveDays();
        	if ("dpp_le_category_3".equals(category) && days>=1) {
        		workflowService.setVariable(instanceId, "finish", "RLZYBSP");
        	}
        }
    	super.onShowAudit(taskId);
    }
}
