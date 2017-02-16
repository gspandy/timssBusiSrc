package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

@Component("ItsmEndWorkReportSelectUser")
public class ItsmEndWorkReportSelectUser implements SelectUserInterface {

	@Autowired
	//@Qualifier("WorkOrderServiceImpl")
	private ItsmWorkOrderService workOrderService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private IAuthorizationManager authManager;
	private static final Logger LOG = Logger.getLogger(ItsmEndWorkReportSelectUser.class);
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		LOG.info("-------------进入完工汇报选人接口实现-----------------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		String userId = userInfoScope.getUserId();
		String userName = userInfoScope.getUserName();
		
		List<SecureUser> resultList = new ArrayList<SecureUser>();
		String woType = workflowService.getVariable(selectUserInfo.getProcessInstId(), "woType").toString();
		String woId = workflowService.getVariable(selectUserInfo.getProcessInstId(), "businessId").toString();
		ItsmWorkOrder workorder = (com.timss.itsm.bean.ItsmWorkOrder) workOrderService.queryItWOById(woId).get("bean");
		
		if("rwxWoType".equals(woType)||"hbWoType".equals(woType)){ //任务型和汇报型工单可选为  所有工程师
			resultList = authManager.retriveUsersWithSpecificRole(siteId+"_ITSM_WHGCS", null, false, true);
			
			if("hbWoType".equals(woType)){
				String createUser = workorder.getCreateuser();
				SecureUser 	tempSecureUser = authManager.retriveUserById(createUser, siteId); //创建者
				List<SecureUser> result0 = new ArrayList<SecureUser>();
				result0.add(tempSecureUser);
				resultList = result0;
			}
			 
		}else{
			//工单策划时的工程师（或者当前审批人，因为策划和汇报都是同一个人）
			SecureUser user = new SecureUser();
			user.setId(userId);
			user.setName(userName);
			resultList.add(user);
		}
		return resultList;
	}
}


