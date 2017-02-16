package com.timss.inventory.flow.zjw.invmatapply.v002;

import java.util.List;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.service.InvMatApplyService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: 物资领料
 * @description: 物资领料
 * @company: gdyd
 * @className: Draft.java
 * @author: 890162
 * @createDate: 2016-4-22
 * @updateUser: 890162
 * @version: 1.0
 */
public class Draft extends TaskHandlerBase {
	private static Logger LOG = Logger.getLogger( Draft.class );
	
    @Autowired
    WorkflowService workflowService;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    IAuthorizationManager authManager;
    @Autowired
    InvMatApplyService invMatApplyService;
    @Override
    public void onShowAudit(String taskId){
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Task task = workflowService.getTaskByTaskId( taskId );
        String processInstId = task.getProcessInstanceId();
        String siteId = userInfoScope.getSiteId();
        String userId = userInfoScope.getUserId();
        boolean isBz = false;
        List<SecureUser> users = authManager.retriveUsersWithSpecificRoleAndSite( "ZJW_BZRY", siteId );
        for ( SecureUser secureUser : users ) {
            if ( secureUser.getId().equals(userId) ) {
                isBz = true;
            }
        }
        workflowService.setVariable( processInstId, "isBz", isBz?"Y":"N" );
    	InvMatApply ima = new InvMatApply();
    	ima.setStatus("draft");
    	Object imaidObj = workflowService.getVariable( processInstId, "imaid" );
        String imaid = imaidObj == null ? "" : String.valueOf( imaidObj );
        if ( !"".equals( imaid ) ) {
        	ima.setImaid(imaid);
        	try {
				invMatApplyService.updateMatApply( ima );
			} catch (Exception e) {
				 LOG.info( ">>>>>>>>>>>>>>>>>>> 领料onShowAudit update 报错：" + e );
			}
        }
    }
    
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	String processInstId = taskInfo.getProcessInstanceId();
    	Task task = workflowService.getTaskByTaskId(taskInfo.getTaskInstId());
    	List< String > taskNextList = workflowService.getNextTaskDefKeys( processInstId , task.getTaskDefinitionKey() );
	    if ( null != taskNextList && !taskNextList.isEmpty() ) {
	    	String status = taskNextList.get( 0 );
	    	InvMatApply ima = new InvMatApply();
	    	ima.setStatus(status);
	    	Object imaidObj = workflowService.getVariable( processInstId, "imaid" );
	    	Object imaList = workflowService.getVariable( processInstId, "imaList" );
	    	List< InvMatApplyDetail > listData = (List<InvMatApplyDetail>) imaList;
            String imaid = imaidObj == null ? "" : String.valueOf( imaidObj );
	        if ( !"".equals( imaid ) ) {
	        	ima.setImaid(imaid);
	        	try {
					invMatApplyService.updateInvMatApply( listData,ima );
				} catch (Exception e) {
					 LOG.info( ">>>>>>>>>>>>>>>>>>> 领料onComplete update 报错：" + e );
				}
	        }
	    }
    }
}
