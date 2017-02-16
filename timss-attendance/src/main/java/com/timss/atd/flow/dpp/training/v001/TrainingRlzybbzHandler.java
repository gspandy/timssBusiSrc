package com.timss.atd.flow.dpp.training.v001;

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
 * 人力资源部部长审批
 */
public class TrainingRlzybbzHandler extends TaskHandlerBase {
	Logger logger = LoggerFactory.getLogger( TrainingPxfbzrHandler.class );
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    final String handlerName="DPP TrainingRlzybbzHandler";
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
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
			wfUtil.updateTrainingAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_TRAINING_RLZYBBZ, userInfo.getSecureUser());
			super.init( taskInfo );
		} catch (Exception e) {
			logger.error( e.getMessage(), e );
		}
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
