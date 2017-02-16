package com.timss.ptw.service.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.ptw.bean.PtoInfo;
import com.timss.ptw.service.PtoInfoService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

@Component("PtwPtoReportSelectUser")
public class PtwPtoReportSelectUser implements SelectUserInterface {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    private PtoInfoService ptoInfoService;
    @Autowired
    private WorkflowService workflowService;
    
    private static final Logger LOG = Logger.getLogger( PtwPtoReportSelectUser.class );

    @Override
    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
        LOG.info( "-------------进入操作票操作汇报的选人接口实现-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        List<SecureUser> resultList = new ArrayList<SecureUser>();
        String ptoId = workflowService.getVariable(selectUserInfo.getProcessInstId(), "businessId").toString();
        PtoInfo ptoInfo = ptoInfoService.queryPtoInfoById(ptoId);
        //以被选监护人为汇报人
        String guardians = ptoInfo.getGuardian();
        if(StringUtils.isNotEmpty(guardians)){
        	String[] guardianList = guardians.split(",");
        	for(String guardian : guardianList){
        		SecureUser user = authManager.retriveUserById( guardian, siteid );
                resultList.add(user);
        	}
        }
        /*SecureUser user = authManager.retriveUserById( ptoInfo.getGuardian(), siteid );
        resultList.add(user);*/

        return resultList;
    }
}

