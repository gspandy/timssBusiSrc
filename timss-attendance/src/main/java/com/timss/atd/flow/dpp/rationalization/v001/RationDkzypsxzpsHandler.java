package com.timss.atd.flow.dpp.rationalization.v001;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
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

public class RationDkzypsxzpsHandler extends TaskHandlerBase{

	
    
    final String handlerName="DPP RationDkzypsxzpsHandler";
    Logger log = LoggerFactory.getLogger( RationDkzypsxzpsHandler.class );
    
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private RationalzationService rationalzationService;
    @Autowired
    private RationalizationDao rationalzationDao;
    
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {  
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
			wfUtil.updateRationAuditStatusByTask(taskInfo, ProcessStatusUtil.DPP_RATION_PSXZSP, userInfo.getSecureUser(),userId);
			String rationalId = String.valueOf(workflowService.getVariable(taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID));
			if (userId!=null||userId.length()>0) {
				RationalizationBean bean = new RationalizationBean();
				bean.setUserId(userId);
				bean.setRationalId(rationalId);
				wfUtil.updateCommonRationApply(bean);
			}
			/*吏新多个处理人*/
			if (ListUtil.toString(uIdList).split(",").length > 1) {
				Map<String,String> parmas = new HashMap<String, String>();
				String name = rationalzationDao.selectNameByUserList(uIdList);
				parmas.put("rationId", rationalId);					
				parmas.put("HandlerName", name);					
				rationalzationDao.updateCurrHandUserById(parmas);
			}
            } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void onShowAudit(String taskId){
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	try {
			Map<String, Object>params=VOUtil.fromJsonToHashMap(userInfo.getParam("businessData"));
			Task taskInfo =  workflowService.getTaskByTaskId( taskId );
        	String proinstId = taskInfo.getProcessInstanceId();
        	String isAgreeByXizoZu = (String) workflowService.getVariable(proinstId, "isFile2");
			workflowService.setVariable(proinstId, "type", isAgreeByXizoZu.equals("Y")?"SHTG":"NSHTG");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	 super.onShowAudit(taskId);
    }
    
    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	log.info("进入对口专业评审小组评审,这里还要更新状态审核是否通过");
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
	 		Map<String, Object> params = VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
	 		RationalizationBean bean = VOUtil.fromJsonToVoUtil(params.get("formData").toString(), RationalizationBean.class);
	 		String fileIds = params.get("fileIds").toString();
	 		wfUtil.completeCommonRationalizationApply(taskInfo, userInfo.getSecureUser());
	 		if (!StringUtils.isBlank( fileIds)) {
	 			String[] fileArr = fileIds.split( "," );
	            bean.setFileIds(fileArr);
			}
	 		/*更新对口部门*/
	 		rationalzationService.update(bean, userInfo.getSecureUser());
	 		super.onComplete(taskInfo);
	 		/*此处要更新另一个是否的状态*/
	    } catch (Exception e) {
			log.error( e.getMessage(), e );
		}
    }
}
