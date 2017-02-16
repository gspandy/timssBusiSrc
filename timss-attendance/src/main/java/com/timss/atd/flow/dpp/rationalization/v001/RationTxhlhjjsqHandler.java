package com.timss.atd.flow.dpp.rationalization.v001;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.bean.RationalizationBean;
import com.timss.attendance.bean.TrainingBean;
import com.timss.attendance.service.RationalzationService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ListUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

/**
 * 提交合理化建议申请
 */
public class RationTxhlhjjsqHandler extends TaskHandlerBase {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    @Autowired
    private RationalzationService rationalzationService;
    @Autowired
    private HomepageService homepageService;
    
    final String handlerName="DPP RationSqbmjjysdHandler";
    Logger log = LoggerFactory.getLogger( RationTxhlhjjsqHandler.class );
    
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
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	log.info( handlerName+" init" ); 
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
            	wfUtil.initCommonRationApply(taskInfo,userInfo.getSecureUser(),userId);
          super.init( taskInfo );
		} catch (Exception e) {
			log.error( e.getMessage(), e );
		}
    }

    @Override
    public void onShowAudit(String taskId){
    	log.info( handlerName+" onShowAudit" );
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	Task taskInfo =  workflowService.getTaskByTaskId( taskId );
    	String proinstId = taskInfo.getProcessInstanceId();
    	try {
        	//拿出传入的参数
        	Map<String, Object>params=VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
        	if("Y".equals(params.get("notSave"))){
        	}else{
        		log.info("msg="+params);
        		RationalizationBean bean = wfUtil.updateRationalization(userInfo.getParam( "businessData" ), userInfo.getSecureUser());
        		/*这里如果对口专业做了修改的话，需要更改变量表*/
        		workflowService.setVariable(proinstId, "rationType", bean.getRationalType());
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
        	String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId").toString();
        	RationalizationBean  bean = rationalzationService.queryDetail(id);
        	String categoryName = null;
        	if (bean.getRationalType().contains("DPP_RATION_")) {
        		StringBuffer categoryListName = new StringBuffer();
                List<AppEnum> emList = itcMvcService.getEnum( "ATD_RATION_TYPE" );
                categoryName = rationalzationService.getCategoryName(emList, bean.getRationalType()); 
            }
        	String flowCode = bean.getRationalNo();
            homepageService.modify(null,flowCode, null, categoryName +"合理化建议", null, null, null,null);
			wfUtil.completeCommonRationalizationApply(taskInfo,userInfo.getSecureUser());
			super.onComplete( taskInfo );
		} catch (Exception e) {
			log.error( e.getMessage(), e );
		}
    }
}
