package com.timss.itsm.flow.core.question.v001;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.bean.ItsmQuestionRd;
import com.timss.itsm.dao.ItsmQuestionRdDao;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class QuestionExpertSolve  extends TaskHandlerBase{
	
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ItsmQuestionRdDao questionRdDao;
	
	private static final Logger LOG = Logger.getLogger(QuestionExpertSolve.class);
	
	public void init(TaskInfo taskInfo){
		String id = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	ItsmQuestionRd questionRd = new ItsmQuestionRd();
    	questionRd.setId(Integer.valueOf(id));
    	questionRd.setStatus("qesolving");
    	String[] params = new String[]{"status"};
    	questionRdDao.updateQuestionRd(questionRd, params);
	}
	
 	public void onComplete(TaskInfo taskInfo){
	    LOG.debug("-------------进入‘问题专家解决问题’的onComplete(),开始处理业务逻辑-----------------");
	    String id = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
	  	ItsmQuestionRd questionRd = new ItsmQuestionRd();
	  	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas(); 
		 String businessData = null;
		  try {
				businessData = userInfoScope.getParam("businessData");
			} catch (Exception e) {
				LOG.error(e.getMessage(),e);
				throw new RuntimeException(e);
			}
			JSONObject businessDataobj = JSONObject.fromObject(businessData);
			String reason = businessDataobj.getString("reason");
			String solve = businessDataobj.getString("solve");
			String ns = businessDataobj.getString("ns");
			String[] params = new String[]{"reason","solve","status","ns"};
	    	questionRd.setId(Integer.valueOf(id));
	    	questionRd.setReason(reason);
	    	questionRd.setSolve(solve);
	    	questionRd.setNs(ns);
	    	questionRd.setStatus("finished");
	    	questionRdDao.updateQuestionRd(questionRd, params);
	    	LOG.debug("-------------进入‘问题专家解决问题’的onComplete(),处理业务逻辑完毕-----------------");
	}	
}
