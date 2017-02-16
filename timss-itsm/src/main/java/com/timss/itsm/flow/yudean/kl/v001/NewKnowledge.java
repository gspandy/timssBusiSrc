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

public class NewKnowledge  extends TaskHandlerBase{
	
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	ItsmKnowledgeDao itsmKnowledgeDao;
	@Autowired
	ItsmWoUtilService itsmWoUtilService;
	@Autowired
	private ItsmWoUtilService woUtilService;
	private static final Logger LOG = Logger.getLogger(NewKnowledge.class);
	
	public void init(TaskInfo taskInfo){
		String knowledgeId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String klStatus = ItsmConstant.KL_NEW;
    	itsmKnowledgeDao.updateItsmKnowledgeStatus(knowledgeId, klStatus);
	}
	
	public void onComplete(TaskInfo taskInfo){
	  LOG.info("-------------进入‘新建it知识单’的onComplete(),开始处理业务逻辑-----------------");
	  UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	  String userId  = userInfoScope.getUserId();
	  if(userInfoScope!=null){
		  String knowledgeId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
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
				woUtilService.insertAttachMatch(knowledgeId, attachmentIds, "KL", "klAudit");
			}catch (Exception e) {
				LOG.error(e.getMessage());
				throw new RuntimeException(e);
			}
			itsmKnowledge.setCurrStatus(ItsmConstant.KL_AUDIT);
			itsmKnowledge.setAuditUser(userId);
			itsmKnowledge.setAuditDate(new Date());
		    itsmKnowledgeDao.updateItsmKnowledge(itsmKnowledge);
		  
		    itsmWoUtilService.updateKlCurrHandlerUser(knowledgeId, userInfoScope,"normal");
	  }
	}
	    
	public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
		
	}
}
