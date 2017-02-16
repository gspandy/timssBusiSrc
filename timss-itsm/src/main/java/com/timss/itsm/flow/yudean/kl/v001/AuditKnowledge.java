package com.timss.itsm.flow.yudean.kl.v001;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.bean.ItsmKnowledge;
import com.timss.itsm.dao.ItsmKnowledgeDao;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.util.ItsmConstant;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


public class AuditKnowledge  extends TaskHandlerBase{
	
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ItsmKnowledgeDao itsmKnowledgeDao;
	@Autowired
	private ItsmWoUtilService woUtilService;
	
	private static final Logger LOG = Logger.getLogger(AuditKnowledge.class);
	
	public void init(TaskInfo taskInfo){
    	String knowledgeId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String klStatus = ItsmConstant.KL_AUDIT;
    	itsmKnowledgeDao.updateItsmKnowledgeStatus(knowledgeId, klStatus);
	}
	
  public void onComplete(TaskInfo taskInfo){
	  LOG.debug("-------------进入‘知识单审批’的onComplete(),开始处理业务逻辑-----------------");
	  
	  UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
//	  String siteid = userInfoScope.getSiteId();
	  String userId  = userInfoScope.getUserId();
	  
	  String klId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
	 
	  String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		}
		JSONObject businessDataobj = JSONObject.fromObject(businessData);
		String  klFormDataStr = businessDataobj.getString("knowledgeForm");
		String  attachmentIds = businessDataobj.getString("attachmentIds");
		
		ItsmKnowledge itsmKnowledge;
		try {
			itsmKnowledge = JsonHelper.toObject(klFormDataStr, ItsmKnowledge.class);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		} 
		
		//操作附件
		try {
			woUtilService.insertAttachMatch(klId, attachmentIds, "KL", "klAudit");
		}catch (Exception e) {
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		}
		itsmKnowledge.setCurrStatus(ItsmConstant.KL_AUDIT_END);
		itsmKnowledge.setAuditUser(userId);
		itsmKnowledge.setAuditDate(new Date());
	    itsmKnowledgeDao.updateItsmKnowledge(itsmKnowledge);
	  	//进入已结束状态，需要清空当前处理人信息
	  	itsmKnowledgeDao.clearklCurrHandlerUser(Integer.valueOf(klId));
	  	
	}
	    
	public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String klId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
		woUtilService.updateKlCurrHandlerUser(klId, userInfoScope,"rollback");
	}    
	
}
