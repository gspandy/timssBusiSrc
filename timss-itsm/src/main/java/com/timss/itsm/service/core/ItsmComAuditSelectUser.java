package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.itsm.util.WoNoUtil;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;

@Component("ItsmComAuditSelectUser")
public class ItsmComAuditSelectUser implements SelectUserInterface {

	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
        private IAuthorizationManager authManager;
	private static final Logger LOG = Logger.getLogger(ItsmComAuditSelectUser.class);
	
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		LOG.info("-------------进入申请人单位审批选人接口实现-----------------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		
		List<SecureUser> resultList = new ArrayList<SecureUser>();
		//根据角色选人
		List<SecureUser> initResultList = authManager.retriveUsersWithSpecificRole(siteId+"_YG", null, false, true);
		for ( int i = 0; i < initResultList.size(); i++ ) {
		    String tempUserId = initResultList.get( i ).getId().trim();
		    if(tempUserId.length()==6 && WoNoUtil.isNumeric(tempUserId)){
		        SecureUser tempbean =  initResultList.get( i );
		        //<font size="2" color="blue">This is some text!</font>
		        tempbean.setName( tempbean.getName()+"##("+tempbean.getId()+")##" );
		        resultList.add( tempbean );
		    }
                }
		return resultList;
	}

}
