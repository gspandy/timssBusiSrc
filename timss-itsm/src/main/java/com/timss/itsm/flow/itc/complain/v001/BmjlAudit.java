package com.timss.itsm.flow.itc.complain.v001;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.service.ItsmComplainRdService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @description:部门经理审批
 * @author: 
 * @createDate: 
 * @param 
 * @return:
 */
public class BmjlAudit extends TaskHandlerBase {
	
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private WorkflowService workflowService;
	@Autowired
	private ItsmComplainRdService complainRdService;
	@Autowired
	private ItsmWoUtilService woUtilService;
	
	private static final Logger LOG = Logger.getLogger( ComplainAccept.class );
	
	public void init(TaskInfo taskInfo){
		String complainRdId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String currstatus="bmjlAudit";
    	complainRdService.updateComplainRdStatus(complainRdId, currstatus);
	}
	public void onComplete(TaskInfo taskInfo) {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		if(userInfoScope!=null){
			String complainRdId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
			
			String businessData = null;
			try {
				businessData = userInfoScope.getParam( "businessData" );
				JSONObject businessDataobj = JSONObject.fromObject( businessData );
				
				String attachmentIds = businessDataobj.getString( "attachmentIds" );
				// 操作附件
				woUtilService.deleteAttachment(complainRdId, null, "Cp");
                woUtilService.insertAttachment(complainRdId, attachmentIds, "Cp", "bmjlAudit");
			
				complainRdService.updateCurrHandlerUser(complainRdId, userInfoScope, "normal");
				complainRdService.updateComplainRdStatus(complainRdId, "end");//审批完后结束
			} catch (Exception e) {
				LOG.error( e.getMessage());
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
