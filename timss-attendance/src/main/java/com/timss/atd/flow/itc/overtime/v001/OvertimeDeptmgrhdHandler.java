package com.timss.atd.flow.itc.overtime.v001;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 部门经理核定
 */
public class OvertimeDeptmgrhdHandler extends TaskHandlerBase {
    Logger log = LoggerFactory.getLogger( OvertimeDeptmgrhdHandler.class );
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="ITC OvertimeDeptmgrhdHandler";
    
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
        wfUtil.updateOvertimeAuditStatusByTask(taskInfo, ProcessStatusUtil.DEPTMGRHD);
        super.init( taskInfo );
    }
    
    @Override
    public void onShowAudit(String taskId){
    	log.info( handlerName+" onShowAudit" );
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
        	String businessData = userInfo.getParam( "businessData" );
        	if ( StringUtils.isNotBlank( businessData ) ) {
            	if(wfUtil.updateOvertimeRealOverHours(businessData)){
                	log.info("更新"+ProcessStatusUtil.DEPTMGRHD+"成功");
                }else{
                	log.error("更新"+ProcessStatusUtil.DEPTMGRHD+"失败");
                }
            }
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
        super.onComplete( taskInfo );
    }
}
