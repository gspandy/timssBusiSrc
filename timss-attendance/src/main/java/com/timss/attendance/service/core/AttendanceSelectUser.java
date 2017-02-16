package com.timss.attendance.service.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.attendance.util.AtdUserPrivUtil;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;

@Component("AttendanceSelectUser")
public class AttendanceSelectUser implements SelectUserInterface {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private SelectUserService selectUser;
    @Autowired
    private AtdUserPrivUtil privUtil;

    private static final Logger log = Logger.getLogger( AttendanceSelectUser.class );

    @Override
    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
    	log.info( "-------------进入集团总部审批选人接口实现-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        String userId =workflowService.getVariable(selectUserInfo.getProcessInstId(), "userId").toString();
        String orgId =workflowService.getVariable(selectUserInfo.getProcessInstId(), "orgId").toString();
        SecureUser user = authManager.retriveUserById(userId, userInfoScope.getSiteId());
        List<SecureUserGroup> groupList = user.getGroups();
        List<String> groups = getGroupStr(groupList);
        if (groups.contains("DPP_BMLD")||groups.contains("DPP_BMZFZ")) {
        	return selectUser.byGroupAndOrgCode("DPP_BMFZR", orgId, "U");
		}else{
			return selectUser.byGroupAndOrgCode("DPP_BMLD", orgId, "U");
		}
    }
    
    private List<String> getGroupStr(List<SecureUserGroup> groupList){
    	
    	List<String> groups = new ArrayList<String>();
    	if(groupList != null && groupList.size() != 0){
    		for (SecureUserGroup secureUserGroup : groupList) {
				groups.add(secureUserGroup.getId());
			}
    	}
    	return groups;
    }

}
