package com.timss.atd.flow.dpp.rationalization.v001;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.RationalizationBean;
import com.timss.attendance.dao.RationalizationDao;
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

public class RationGdHandler extends TaskHandlerBase{
    final String handlerName="DPP RationGdHandler";
    Logger logger = LoggerFactory.getLogger( RationGdHandler.class );
    
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private RationalizationDao rationalizationDao;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private RationalzationService rationalzationService;
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
        	Map<String, Object>params=VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
        	wfUtil.updateRationAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_RATION_GD, userInfo.getSecureUser(),userId);
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

    @Override
    public void onShowAudit(String taskId){
    	logger.info( handlerName+" init" );
    	logger.info("msg进入此方法 ");
    }
    
    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	try {
			wfUtil.completeCommonRationalizationApply(taskInfo, userInfo.getSecureUser());
			String rationalId = String.valueOf(workflowService.getVariable(taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID));
			rationalizationDao.setRecomNameNull(rationalId);
			wfUtil.updateRationAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_RATION_END, userInfo.getSecureUser(),null);
		} catch (Exception e) {
			e.printStackTrace();
		}	
    }
	
}
