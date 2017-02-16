package com.timss.atd.flow.dpp.overtime.v001;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 加班申请--公司主管领导意见--结束节点
 */
public class OvertimeZgldyjHandler extends TaskHandlerBase {
	Logger logger = LoggerFactory.getLogger( OvertimeFbyjHandler.class );
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    final String handlerName="DPP OvertimeBmyjHandler";
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private WorkflowService workflowService;
    
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
        wfUtil.updateOvertimeAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_OVERTIME_ZGLDYJ);
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	logger.info(  handlerName+" onComplete" );
        wfUtil.updateOvertimeAuditStatusByTask(taskInfo, ProcessStatusUtil.CLOSED);
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId").toString();
        //核定转补休时长
        //wfUtil.updateOvertimeTransferCompensate(Integer.parseInt(id),null);
        
        try {
			wfUtil.checkWorkStatusAndStatByOvertimeId(Integer.parseInt(id));//更新休假统计
		} catch (Exception e) {
			logger.error("error checkWorkStatusAndStatByOvertimeId("+id+")",e);
		}
        super.onComplete( taskInfo );
    }
}
