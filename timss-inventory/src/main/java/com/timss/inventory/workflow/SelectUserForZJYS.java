package com.timss.inventory.workflow;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

/**
 * 
 * 专家验收节点选人逻辑
 * @description: {desc}
 * @company: gdyd
 * @className: SelectUserForXGBM.java
 * @author: 890145
 * @createDate: 2015-11-7
 * @updateUser: 890145
 * @version: 1.0
 */
@Service
public class SelectUserForZJYS implements SelectUserInterface{
	@Autowired
	WorkflowService workflowService;
	@Autowired
	IAuthorizationManager iAuthorizationManager; 
	private static final Logger LOGGER=Logger.getLogger(SelectUserForZJYS.class);
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		LOGGER.info("开始获取流程"+selectUserInfo.getProcessInstId()+"的专家验收审批节点审批人员");
		//获取流程实例id
		String processInstId=selectUserInfo.getProcessInstId();
		//获取合同归属字段值
		String sp_mate=(String)workflowService.getVariable(processInstId, "SP_MATERIAL");
		//获取对应的用户组
		String groupId=getGroupName(sp_mate);
		
		LOGGER.info("获取流程"+selectUserInfo.getProcessInstId()+"的专家验收审批节点人员所属的用户组是"+groupId);
		//获取用户组对应的所有用户
		List<SecureUser> secureUsers=iAuthorizationManager.retriveUsersWithSpecificGroup(groupId, null, false, true);
		return secureUsers;
	}

	private String getGroupName(String groupName) {
		return "SWF_ZJYS_"+groupName;
	}

}
