package com.timss.purchase.flow.zjw.purapply.v005;

import java.util.List;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: Draft工作流控制类
 * @description:
 * @company: gdyd
 * @className: Draft.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class Draft extends DefApplyProcess {
    @Autowired
    WorkflowService workflowService;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    IAuthorizationManager authManager;
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
        if(!isBz){
            users = authManager.retriveUsersWithSpecificRoleAndSite( "ZJW_BZSH", siteId );
            for ( SecureUser secureUser : users ) {
                if ( secureUser.getId().equals(userId) ) {
                    isBz = true;
                }
            }
        }
        workflowService.setVariable( processInstId, "isBz", isBz?"Y":"N" );
    }
}
