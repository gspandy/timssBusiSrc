package com.timss.pms.service.sjw;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

/**
 * 立项工程专责选人
 * @ClassName:     SelectUserForGCZZ
 * @company: gdyd
 * @author:    gchw
 * @date:   2016-8-29
 */
@Component("SelectUserForGCZZ")
public class SelectUserForGCZZ implements SelectUserInterface{
	@Autowired
	WorkflowService workflowService;
	@Autowired
	IAuthorizationManager iAuthorizationManager; 
	public SelectUserForGCZZ() {
            
        }
	private static final Logger LOGGER=Logger.getLogger(SelectUserForGCZZ.class);
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		LOGGER.info("开始获取流程"+selectUserInfo.getProcessInstId()+"的场长节点审批人员");
		//获取流程实例id
		String processInstId=selectUserInfo.getProcessInstId();
		//获取合同归属字段值
		String gczz=(String)workflowService.getVariable(processInstId, "businessLeader");
		//获取对应的用户组
		SecureUser user = iAuthorizationManager.retriveUserById( gczz, "SJW" );
		LOGGER.info("获取流程"+selectUserInfo.getProcessInstId()+"的工程专责的用户是"+user.getId()+"_"+user.getName());
		List<SecureUser> secureUsers = new ArrayList<SecureUser>(0);
		secureUsers.add( user );
		return secureUsers;
	}
}
