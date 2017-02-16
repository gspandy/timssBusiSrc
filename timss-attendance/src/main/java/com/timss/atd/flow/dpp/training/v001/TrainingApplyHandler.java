package com.timss.atd.flow.dpp.training.v001;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.TrainingBean;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 提交培训申请
 */
public class TrainingApplyHandler extends TaskHandlerBase {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="DPP TrainingApplyHandler";
    Logger log = LoggerFactory.getLogger( TrainingApplyHandler.class );
    
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
			wfUtil.initCommonTrainingApply(taskInfo,userInfo.getSecureUser());
			super.init( taskInfo );
		} catch (Exception e) {
			log.error( e.getMessage(), e );
		}
    }

    @Override
    public void onShowAudit(String taskId){
    	log.info( handlerName+" onShowAudit" );
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
        	//拿出传入的参数
        	Map<String, Object>params=VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
        	if("Y".equals(params.get("notSave"))){
        		workflowService.setVariable( params.get("instanceId").toString(), "orgId", userInfo.getOrgId() );
        	}else{
        		//保存
            	TrainingBean bean=wfUtil.updateTraining(userInfo.getParam( "businessData" ),userInfo.getSecureUser());
            	workflowService.setVariable( bean.getInstanceId(), "orgId", userInfo.getOrgId() );
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
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
			wfUtil.completeCommonTrainingApply(taskInfo,userInfo.getSecureUser());
			super.onComplete( taskInfo );
		} catch (Exception e) {
			log.error( e.getMessage(), e );
		}
    }
}
