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
import com.yudean.workflow.service.WorkflowService;

@Component("ItsmYDZAuditSelectUser")
public class ItsmYDZAuditSelectUser implements SelectUserInterface {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    private WorkflowService workflowService;

    private static final Logger LOG = Logger.getLogger( ItsmYDZAuditSelectUser.class );

    @Override
    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
        LOG.info( "-------------进入集团总部审批选人接口实现-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String queryDeptId = workflowService.getVariable( selectUserInfo.getProcessInstId(), "centerNextAuditId" )
                .toString();

        List<SecureUser> resultList = new ArrayList<SecureUser>();
        List<SecureUser> initResultList = new ArrayList<SecureUser>();
        SecureUser secureuser = userInfoScope.getSecureUser();
        if ( !"".equals( queryDeptId ) ) {
            // 根据站点和部门查人
            initResultList = authManager.retriveActiveUsersOfGivenOrg( queryDeptId, true, secureuser );
        }

        for ( int i = 0; i < initResultList.size(); i++ ) {
            String tempUserId = initResultList.get( i ).getId().trim();
            if ( tempUserId.length() == 6 && WoNoUtil.isNumeric( tempUserId ) ) {
                resultList.add( initResultList.get( i ) );
            }
        }

        return resultList;
    }

}
