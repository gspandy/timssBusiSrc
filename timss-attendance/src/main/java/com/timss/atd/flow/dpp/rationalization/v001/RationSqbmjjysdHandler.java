package com.timss.atd.flow.dpp.rationalization.v001;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.timss.attendance.bean.RationalizationBean;
import com.timss.attendance.service.RationalzationService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ListUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

public class RationSqbmjjysdHandler extends TaskHandlerBase {

	Logger logger = LoggerFactory.getLogger( RationSqbmjjysdHandler.class );
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    final String handlerName="DPP RationSqbmjjysdHandler";
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private RationalzationService rationalzationService;
    @Autowired
    private ItcMvcService itcMvcService;
   
    
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
    	logger.info( handlerName+" beforeRollback" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
 		 if(userInfoScope!=null){
 			String rationId = workflowService.getVariable(sourceTaskInfo.getProcessInstanceId(), "businessId").toString();
 			try {
 				rationalzationService.updateCurrHandlerUser(rationId, userInfoScope, "rollback");
 			} catch (Exception e) {
 				logger.error( e.getMessage() );
 			}
 		  }
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
    	logger.info( handlerName+" init" );
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String userId = null;
        List<String> uIdList = (List<String>) workflowService.getVariable(
            taskInfo.getProcessInstanceId(), WorkFlowConstants.CURRENT_TASK_CANDIDATE_USER);
        if (null == uIdList || uIdList.isEmpty()) {
          uIdList = Collections.emptyList();
        } else {
        	//userId = uIdList.get(0);
        	userId = ListUtil.toString(uIdList);
        }
        // 流程退回获取人员id 从请求参数中获取
        try {
        	if (StringUtils.isNotEmpty(userInfo.getParam("userId"))) {
        		userId = userInfo.getParam("userId");
        	}
			wfUtil.updateRationAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_RATION_BMJYYSD, userInfo.getSecureUser(),userId);
			if (userId!=null||userId.length()>0) {
				RationalizationBean bean = new RationalizationBean();
				String rationalId = String.valueOf(workflowService.getVariable(taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID));
				bean.setUserId(userId);
				bean.setRationalId(rationalId);
				wfUtil.updateCommonRationApply(bean);
			}
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
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
        	//这里更新modifyuser 和 modifyDate;
        	wfUtil.completeCommonRationalizationApply(taskInfo, userInfo.getSecureUser());
			super.init( taskInfo );
		} catch (Exception e) {
			logger.error( e.getMessage(), e );
		}
        super.onComplete( taskInfo );
    }
    
    @Override
    public void onShowAudit(String taskId){
    	logger.info( handlerName+" onShowAudit" );
    	logger.info("taskId="+taskId);
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
        	//拿出传入的参数
        	Map<String, Object>params=VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
        	Task taskInfo =  workflowService.getTaskByTaskId( taskId );
        	String proinstId = taskInfo.getProcessInstanceId();

        	if("Y".equals(params.get("notSave"))){
        	}else{
        		//保存
        		String isAgreeByJJy = (String) workflowService.getVariable(proinstId, "isFile");
        		workflowService.setVariable(proinstId, "type", isAgreeByJJy.equals("Y")?"TG":"NTG");
        	}
        } catch (Exception e) {
        	logger.error( e.getMessage(), e );
        }
    }
	
}
