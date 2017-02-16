package com.timss.itsm.flow.dpp.complain.v001;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.timss.itsm.service.ItsmComplainRdService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppComment;
import com.yudean.itc.manager.support.ICommentManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @description:部门负责人处理
 * @author: yangk
 * @createDate: 2016年10月9日
 * @param 
 * @return:
 */
public class BmfzrHandle extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private WorkflowService workflowService;
	@Autowired
	private ItsmComplainRdService complainRdService;
	@Autowired
	private ICommentManager iCommentManager;
	@Autowired
	private ItsmWoUtilService woUtilService;
	
	private static final Logger LOG = Logger.getLogger( BmfzrHandle.class );
	
	public void init(TaskInfo taskInfo){
		LOG.info("*********进入部门负责人处理环节的init方法********");
		String complainRdId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
    	String currstatus="bmfzrHandle";
    	complainRdService.updateComplainRdStatus(complainRdId, currstatus);
	}
	public void onComplete(TaskInfo taskInfo)  {
		LOG.info("*********进入部门负责人处理环节的onComplete方法********");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
			String complainRdId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
			String businessData = null;
	            try {
					businessData = userInfoScope.getParam( "businessData" );
				} catch (Exception e) {
					 LOG.error( e.getMessage() );
			         throw new RuntimeException( e );
				}
	            JSONObject businessDataobj = JSONObject.fromObject( businessData );
	            String commentInfo = businessDataobj.getString( "commentInfo" );
			/**
			 * 将分析与处理记录保存
			 */
			AppComment appComment=new AppComment();
			appComment.setFlwId(complainRdId);
			appComment.setCommentInfo(commentInfo);
			SecureUser secureuser=userInfoScope.getSecureUser();
			iCommentManager.createComment(appComment,secureuser);
			
			String attachmentIds = businessDataobj.getString( "attachmentIds" );
			try {
				// 操作附件
				woUtilService.deleteAttachment(complainRdId, null, "Cp");
				woUtilService.insertAttachment(complainRdId, attachmentIds, "Cp", "cpHandle");
				
				complainRdService.updateCurrHandlerUser(complainRdId, userInfoScope, "normal");
			} catch (Exception e) {
				 LOG.error( e.getMessage());
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
