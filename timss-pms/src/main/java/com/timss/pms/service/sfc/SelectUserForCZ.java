package com.timss.pms.service.sfc;

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
 * 项目公司相关部门节点选人逻辑
 * @ClassName:     SelectUserForXGBM
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-9-24 下午4:13:56
 */
@Service
public class SelectUserForCZ implements SelectUserInterface{
	@Autowired
	WorkflowService workflowService;
	@Autowired
	IAuthorizationManager iAuthorizationManager; 
	
	private static final Logger LOGGER=Logger.getLogger(SelectUserForCZ.class);
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		LOGGER.info("开始获取流程"+selectUserInfo.getProcessInstId()+"的场长节点审批人员");
		//获取流程实例id
		String processInstId=selectUserInfo.getProcessInstId();
		//获取合同归属字段值
		String belongTo=(String)workflowService.getVariable(processInstId, "belongTo");
		//获取对应的用户组
		String groupId=getGroupName(belongTo);
		LOGGER.info("获取流程"+selectUserInfo.getProcessInstId()+"的场长节点审批人员所属的用户组是"+groupId);
		//获取用户组对应的所有用户
		List<SecureUser> secureUsers=iAuthorizationManager.retriveUsersWithSpecificGroup(groupId, null, false, true);
		return secureUsers;
	}

	private String getGroupName(String belongTo) {
		return belongTo+"_PMS_CZ";
	}

}
