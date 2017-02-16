package com.timss.atd.flow.dpp.leave.v001;

import java.util.List;

import net.sf.json.JSONObject;

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
import com.timss.attendance.util.VOUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 请假申请--部门审批
 */
public class LeaveRlzybspHandler extends TaskHandlerBase {
    Logger log = LoggerFactory.getLogger( LeaveRlzybspHandler.class );
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private LeaveDao leaveDao;
    final String handlerName="DPP LeaveRlzybspHandler";
    
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
        wfUtil.updateLeaveAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_LEAVE_RLZYBSP);
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
        if("RLZYBSP".equals(finish)){
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
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	Task taskInfo = workflowService.getTaskByTaskId(taskId);
    	String instanceId = taskInfo.getProcessInstanceId(); 
        String category = (String) workflowService.getVariable(instanceId, "category");
		Double day = Double.parseDouble(workflowService.getVariable(instanceId, "countDay").toString());
		if (category.equals("dpp_le_category_3")&&day>=10.0||category.equals("dpp_le_category_2")&&day>=3.0) {
			String finish="FGLDSP";
			workflowService.setVariable(instanceId, "finish", finish);
		}
		super.onShowAudit(taskId);
    }
}
