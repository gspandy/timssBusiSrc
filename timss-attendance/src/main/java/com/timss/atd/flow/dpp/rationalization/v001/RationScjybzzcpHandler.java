package com.timss.atd.flow.dpp.rationalization.v001;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

public class RationScjybzzcpHandler extends TaskHandlerBase{

	
    
    final String handlerName="DPP RationScjybzzcpHandler";
    Logger log = LoggerFactory.getLogger( RationScjybzzcpHandler.class );
    
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private RationalzationService rationalzationService;
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {   
    	log.info( handlerName+" beforeRollback" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
 		 if(userInfoScope!=null){
 			String rationId = workflowService.getVariable(sourceTaskInfo.getProcessInstanceId(), "businessId").toString();
 			try {
 				rationalzationService.updateCurrHandlerUser(rationId, userInfoScope, "rollback");
 			} catch (Exception e) {
 				log.error( e.getMessage() );
 			}
 		  }
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
    	log.info( handlerName+" onShowAudit" );
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
			wfUtil.updateRationAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_RATION_SCJYZZCP, userInfo.getSecureUser(),userId);
			if (userId!=null||userId.length()>0) {
				RationalizationBean bean = new RationalizationBean();
				String rationalId = String.valueOf(workflowService.getVariable(taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID));
				bean.setUserId(userId);
				bean.setRationalId(rationalId);
				wfUtil.updateCommonRationApply(bean);
			}
            } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void onShowAudit(String taskId){
    	log.info("进入生产技术部专责初评");
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	Map<String, Object> params;
		try {
			params = VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
			RationalizationBean b = VOUtil.fromJsonToVoUtil(params.get( "formData" ).toString(), RationalizationBean.class);
			RationalizationBean bean = wfUtil.updateRationalization(userInfo.getParam( "businessData" ), userInfo.getSecureUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
       log.info("进入生产技术部专责初评");
       UserInfoScope userInfo = privUtil.getUserInfoScope();
       try {
		Map<String, Object> params = VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
		wfUtil.completeCommonRationalizationApply(taskInfo, userInfo.getSecureUser());
	} catch (Exception e) {
		e.printStackTrace();
	}
    }

}
