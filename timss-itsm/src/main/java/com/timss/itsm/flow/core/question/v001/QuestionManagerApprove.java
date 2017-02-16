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

public class QuestionManagerApprove  extends TaskHandlerBase{
	
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ItsmQuestionRdDao questionRdDao;
	
	private static final Logger LOG = Logger.getLogger(QuestionManagerApprove.class);
	
	public void init(TaskInfo taskInfo){
		String id = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	ItsmQuestionRd questionRd = new ItsmQuestionRd();
    	questionRd.setId(Integer.valueOf(id));
    	questionRd.setStatus("qmapproving");
    	String[] params = new String[]{"status"};
    	questionRdDao.updateQuestionRd(questionRd, params);
		
		
	}
	
 	public void onComplete(TaskInfo taskInfo){
	  LOG.debug("-------------进入‘问题经理审批’的onComplete(),开始处理业务逻辑-----------------");
	  UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	  String businessData = null;
	  try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			throw new RuntimeException(e);
		}
		JSONObject businessDataobj = JSONObject.fromObject(businessData);
		String id  = businessDataobj.getString("id");
		String opinion = businessDataobj.getString("opinion");
		String na = businessDataobj.getString("na");
		String[] params = new String[]{"opinion","na"};
		ItsmQuestionRd questionRd = new ItsmQuestionRd();
    	questionRd.setId(Integer.valueOf(id));
    	questionRd.setOpinion(opinion);
    	questionRd.setNa(na);
    	questionRdDao.updateQuestionRd(questionRd, params);
    	LOG.debug("-------------进入‘问题经理审批’的onComplete(),开始处理业务逻辑-----------------");
	}	
}
