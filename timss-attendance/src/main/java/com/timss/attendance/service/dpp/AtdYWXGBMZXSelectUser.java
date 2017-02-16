package com.timss.attendance.service.dpp;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.attendance.util.AtdUserPrivUtil;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.facade.sec.ISecurityFacade;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;

@Component("AtdYWXGBMZXSelectUser")
public class AtdYWXGBMZXSelectUser implements SelectUserInterface {

	@Autowired
	private WorkflowService workflowService;
	 
	@Autowired
	private SelectUserService selectUser;
	
	@Autowired
    private AtdUserPrivUtil privUtil;
	
	private static final Logger log = Logger.getLogger( AtdYWXGBMZXSelectUser.class );
	
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		log.info( "-------------合理化建议第七环节业务相关部门执行选人逻辑-----------------" );
		String userId = privUtil.getUserInfoScope().getUserId();
		log.info("msg="+userId);
		String majorOrgId = workflowService.getVariable(selectUserInfo.getProcessInstId(), "majorOrgId").toString();
		List<SecureUser> userList =selectUser.byRoleAndOrg("DPP_YG",majorOrgId,"D");
		Iterator<SecureUser> it = userList.iterator();  
		while(it.hasNext()) {  
			SecureUser u = it.next();
			if (u.getId().equals(userId)) {
				it.remove();
			}
		}  
		return userList;
	}
}
