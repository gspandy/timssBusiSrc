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

@Component("ItsmApplicantSelectUser")
public class ItsmApplicantSelectUser implements SelectUserInterface {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    private ItsmWorkOrderService workOrderService;
    @Autowired
    private WorkflowService workflowService;
    private static final Logger LOG = Logger.getLogger( ItsmApplicantSelectUser.class );
    
    @Override
    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
        LOG.info( "-------------进入申请人审批选人接口实现-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
//        String siteId = userInfoScope.getSiteId();
//        String temp = selectUserInfo.getProcessInstId();
      //  LOG.info( "-------------进入站点管理员审批选人接口实现-----------------" );
        String woId = workflowService.getVariable( selectUserInfo.getProcessInstId(), "businessId" ).toString();
        ItsmWorkOrder workOrder = (com.timss.itsm.bean.ItsmWorkOrder) workOrderService.queryItWOById( woId ).get( "bean" );
        String createUserId = workOrder.getCreateuser();
        SecureUser secureUser = authManager.retriveUserById( createUserId, null );
        List<SecureUser> resultList = new ArrayList<SecureUser>();
        resultList.add( secureUser );
//        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
//        String siteId = userInfoScope.getSiteId();
//        List<SecureUser> resultList =  authManager.retriveUsersWithSpecificRole(siteId+"_ITSM_ADMIN", null, false, true);
        
        return resultList;
    }

}
