package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.timss.itsm.util.ItsmConstant;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

@Component("ItsmCenterAuditSelectUser")
public class ItsmCenterAuditSelectUser implements SelectUserInterface {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    private ItsmWorkOrderService workOrderService;
    @Autowired
    private WorkflowService workflowService;
    private static final Logger LOG = Logger.getLogger( ItsmCenterAuditSelectUser.class );

    @Override
    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
        LOG.info( "-------------进入集团信息中心审批选人接口实现-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        SecureUser secureuser = userInfoScope.getSecureUser();
        String processInstId = selectUserInfo.getProcessInstId();
        String woId = workflowService.getVariable( processInstId, "businessId" ).toString();
        ItsmWorkOrder workOrder = (com.timss.itsm.bean.ItsmWorkOrder) workOrderService.queryItWOById( woId ).get( "bean" );

        String woStatus = workOrder.getCurrStatus();  //状态
        
        List<SecureUser> resultList = new ArrayList<SecureUser>();
        if("applicantAudit".equals( woStatus ) || "draft".equals( woStatus )){
          //根据站点和部门查人
          resultList = authManager.retriveActiveUsersOfGivenOrg(ItsmConstant.INFOCENTERDEPTID, true, secureuser);
        }else if("groupDeptAudit".equals( woStatus )){
            List<String> processInstIdList = new ArrayList<String>();
            processInstIdList.add( processInstId );
            Map<String, String> auditUserMap =  workflowService.getTaskAuditUser( processInstIdList, "infocenter_audit" );
            
            SecureUser secureUser = authManager.retriveUserById( auditUserMap.get( processInstId), null );
            resultList.add( secureUser );
        }
        for ( int i = 0; i < resultList.size(); i++ ) {
            SecureUser tempSecureuser = resultList.get( i );
            tempSecureuser.setCurrentSite( "YDZ" );
        }
        return resultList;
    }

}
