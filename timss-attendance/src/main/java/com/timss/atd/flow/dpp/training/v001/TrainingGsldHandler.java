package com.timss.atd.flow.dpp.training.v001;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 公司领导审批
 */
public class TrainingGsldHandler extends TaskHandlerBase {
    Logger log = LoggerFactory.getLogger( TrainingGsldHandler.class );
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    @Autowired
    private AtdUserPrivUtil privUtil;
    final String handlerName="DPP TrainingGsldHandler";
    
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
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
			wfUtil.updateTrainingAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_TRAINING_GSLD, userInfo.getSecureUser());
			super.init( taskInfo );
		} catch (Exception e) {
			log.error( e.getMessage(), e );
		}
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        log.info( handlerName+" onComplete" );
    /*    UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
			wfUtil.updateTrainingAuditStatusByTask(taskInfo, ProcessStatusUtil.CLOSED, userInfo.getSecureUser());
			super.init( taskInfo );
		} catch (Exception e) {
			log.error( e.getMessage(), e );
		}*/
        super.onComplete( taskInfo );
    }
}
