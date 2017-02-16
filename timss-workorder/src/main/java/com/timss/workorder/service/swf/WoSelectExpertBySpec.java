package com.timss.workorder.service.swf;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

@Component("WoSelectExpertBySpec")
public class WoSelectExpertBySpec implements SelectUserInterface {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private IAuthorizationManager authManager;
    private static final Logger LOG = Logger.getLogger(WoSelectExpertBySpec.class);

    public WoSelectExpertBySpec() {
    }

    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
        LOG.info("-------------进入湛江生物质根据工单专业选择专工接口-----------------");
        String woSpecCode = this.workflowService.getVariable(selectUserInfo.getProcessInstId(), "woSpecCode").toString();
        
        List<SecureUser> resultList = new ArrayList<SecureUser>();
        if(woSpecCode!=null){
        	if(woSpecCode.equals("BOILER") || woSpecCode.equals("STEAM") || woSpecCode.equals("FUEL") || woSpecCode.equals("FIRE")){
        		resultList = authManager.retriveUsersWithSpecificGroup("SWF_WO_EXPERT_MACHINE", null, false, true);
        	}
        	else if(woSpecCode.equals("ELECTRIC") || woSpecCode.equals("THERMAL") ){
        		resultList = authManager.retriveUsersWithSpecificGroup("SWF_WO_EXPERT_ELECTRIC", null, false, true);
        	}
        	else if(woSpecCode.equals("ARCHITECT")){
        		resultList = authManager.retriveUsersWithSpecificGroup("SWF_WO_EXPERT_PLAN", null, false, true);
        	}
        	else{
        		List<SecureUser> resultList1 = authManager.retriveUsersWithSpecificGroup("SWF_WO_EXPERT_MACHINE", null, false, true);
        		List<SecureUser> resultList2 = authManager.retriveUsersWithSpecificGroup("SWF_WO_EXPERT_ELECTRIC", null, false, true);
        		List<SecureUser> resultList3 = authManager.retriveUsersWithSpecificGroup("SWF_WO_EXPERT_PLAN", null, false, true);
        		for (SecureUser secureUser : resultList1) {
					if(!resultList.contains(secureUser)){
						resultList.add(secureUser);
					}
				}
        		for (SecureUser secureUser : resultList2) {
					if(!resultList.contains(secureUser)){
						resultList.add(secureUser);
					}
				}
        		for (SecureUser secureUser : resultList3) {
					if(!resultList.contains(secureUser)){
						resultList.add(secureUser);
					}
				}
        	}
        }
        return resultList;
    }
}
