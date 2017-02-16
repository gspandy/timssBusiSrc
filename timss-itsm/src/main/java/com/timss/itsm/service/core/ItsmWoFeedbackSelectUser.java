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
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

@Component("ItsmWoFeedbackSelectUser")
public class ItsmWoFeedbackSelectUser implements SelectUserInterface {

    @Autowired
    // @Qualifier("WorkOrderServiceImpl")
    private ItsmWorkOrderService workOrderService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private IAuthorizationManager authManager;
    private static final Logger LOG = Logger.getLogger( ItsmWoFeedbackSelectUser.class );

    @Override
    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
        LOG.info( "-------------进入完工回返的选人接口实现-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        List<SecureUser> resultList = new ArrayList<SecureUser>();
        String woType = workflowService.getVariable( selectUserInfo.getProcessInstId(), "woType" ).toString();
        String woId = workflowService.getVariable( selectUserInfo.getProcessInstId(), "businessId" ).toString();

        if ( "rwxWoType".equals( woType ) ) { // 任务型工单可选为 所有工程师
            ItsmWorkOrder workOrder = (com.timss.itsm.bean.ItsmWorkOrder) workOrderService.queryItWOById(
                     woId ).get( "bean" );
            String createUserId = workOrder.getCreateuser();
            UserInfo userInfo = itcMvcService.getUserInfoById( createUserId );
            SecureUser user = new SecureUser();
            user.setId( createUserId );
            user.setName( userInfo.getUserName() );
            resultList.add( user );

        } else {
            resultList = authManager.retriveUsersWithSpecificRole( "ITC_ITSM_KF", null, false, true );
        }
        return resultList;
    }

}
