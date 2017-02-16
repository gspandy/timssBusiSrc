package com.timss.itsm.flow.dpp.complain.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONObject;

import com.timss.itsm.bean.ItsmComplainRd;
import com.timss.itsm.dao.ItsmComplainRdDao;
import com.timss.itsm.service.ItsmComplainRdService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @description:
 * @author: 
 * @createDate: 
 * @param 
 * @return:
 */
public class BmfzrConfirm extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private WorkflowService workflowService;
	@Autowired
	private ItsmComplainRdService complainRdService;
	@Autowired
	private ItsmComplainRdDao complainRdDao;

	
	private static final Logger LOG = Logger.getLogger( BmfzrConfirm.class );
	
	public void init(TaskInfo taskInfo){
		String complainRdId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String currstatus="bmfzrConfirm";
    	complainRdService.updateComplainRdStatus(complainRdId, currstatus);
	}
	public void onComplete(TaskInfo taskInfo){
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		if(userInfoScope!=null){
			String businessData = null;
			try {
				businessData = userInfoScope.getParam( "businessData" );
				JSONObject businessDataobj = JSONObject.fromObject( businessData );
				String complainRdForm = businessDataobj.getString( "complainRdForm" );
				ItsmComplainRd complainRd=JsonHelper.toObject(complainRdForm, ItsmComplainRd.class);
				complainRdDao.updateComplainRd(complainRd);
				String complainRdId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
				complainRdService.updateCurrHandlerUser(complainRdId, userInfoScope, "normal");
			} catch (Exception e) {
				LOG.error( e.getMessage() );
			}
		  }
	 }
	public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		if(userInfoScope!=null){
			String complainRdId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
			try {
				complainRdService.updateCurrHandlerUser(complainRdId, userInfoScope, "rollback");
			} catch (Exception e) {
				 LOG.error( e.getMessage() );
			}
		  }
	}
}