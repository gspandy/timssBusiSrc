package com.timss.inventory.flow.swf.invmatapply.v002;

import java.util.Collections;
import java.util.List;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.service.InvMatApplyService;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: Draft.java
 * @author: 890166
 * @createDate: 2014-9-17
 * @updateUser: 890166
 * @version: 1.0
 */
public class Draft extends TaskHandlerBase {

    private static Logger LOG = Logger.getLogger( Draft.class );

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    public IAuthorizationManager authManager;
    @Autowired
    InvMatApplyService invMatApplyService;

    @Override
    public void init(TaskInfo taskInfo) {
        String userId = null;

        UserInfo ui = itcMvcService.getUserInfoScopeDatas();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        try {
            List<String> uIdList = (List<String>) workflowService.getVariable( taskInfo.getProcessInstanceId(),
                    WorkFlowConstants.CURRENT_TASK_CANDIDATE_USER );
            if ( null == uIdList || uIdList.isEmpty() ) {
                uIdList = Collections.emptyList();
            } else {
                userId = uIdList.get( 0 );
            }

            if ( StringUtils.isNotEmpty( userInfoScope.getParam( "userId" ) ) ) {
                // 流程退回
                userId = userInfoScope.getParam( "userId" );
            }

            if ( null == userId ) {
                userId = ui.getUserId();
            }

            SecureUser user = authManager.retriveUserById( userId, ui.getSiteId() );

            // 初始化流程
            workflowService.setVariable( taskInfo.getProcessInstanceId(), "isMaintain", "N" );
            workflowService.setVariable( taskInfo.getProcessInstanceId(), "isEquipment", "N" );

            // 获取当前用户角色是否维护单位
            List<Role> rList = user.getRoles();
            if ( null != rList && !rList.isEmpty() ) {
                for ( Role role : rList ) {
                    if ( "SWF_WXDWRY".equals( role.getId() ) ) {
                        workflowService.setVariable( taskInfo.getProcessInstanceId(), "isMaintain", "Y" );
                        break;
                    }
                }
            }

            List<SecureUserGroup> sugList = user.getGroups();
            if ( null != sugList && !sugList.isEmpty() ) {
                for ( SecureUserGroup sug : sugList ) {
                    if ( "SWF_SBBDJY".equals( sug.getId() ) ) {
                        workflowService.setVariable( taskInfo.getProcessInstanceId(), "isEquipment", "Y" );
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOG.info( ">>>>>>>>>>>>>>>>>>> init 报错：" + e );
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
