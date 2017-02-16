package com.timss.atd.flow.dpp.overtime.v001;

import org.apache.commons.lang3.StringUtils;
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
 * 加班申请--分部意见
 */
public class OvertimeFbyjHandler extends TaskHandlerBase {
    Logger logger = LoggerFactory.getLogger( OvertimeFbyjHandler.class );
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    final String handlerName="DPP OvertimeFbyjHandler";
    @Autowired
    private AtdUserPrivUtil privUtil;
    
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
        wfUtil.updateOvertimeAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_OVERTIME_FBYJ);
        super.init( taskInfo );
    }
    
    @Override
    public void onShowAudit(String taskId){
    	logger.info( handlerName+" onShowAudit" );
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
        	String businessData = userInfo.getParam( "businessData" );
        	if ( StringUtils.isNotBlank( businessData ) ) {
            	if(wfUtil.updateOvertimeRealOverHours(businessData)){
            		logger.info("更新"+ProcessStatusUtil.DPP_OVERTIME_FBYJ+"成功");
                }else{
                	logger.error("更新"+ProcessStatusUtil.DPP_OVERTIME_FBYJ+"失败");
                }
            }
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
