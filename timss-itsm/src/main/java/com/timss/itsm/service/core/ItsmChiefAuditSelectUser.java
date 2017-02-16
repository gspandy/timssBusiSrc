package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;

@Component("ItsmChiefAuditSelectUser")
public class ItsmChiefAuditSelectUser implements SelectUserInterface {

	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private IAuthorizationManager authManager;
	private static final Logger LOG = Logger.getLogger(ItsmChiefAuditSelectUser.class);
	
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		LOG.info("-------------进入主管审批选人接口实现-----------------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		
		List<SecureUser> resultList = new ArrayList<SecureUser>();
		// 根据用户组选人
		resultList = authManager.retriveUsersWithSpecificGroup(siteId+"_ITSM_WHGCS", null, false, true);
		
		return resultList;
	}

}
