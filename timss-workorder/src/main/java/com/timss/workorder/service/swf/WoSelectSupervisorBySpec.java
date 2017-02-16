package com.timss.workorder.service.swf;

import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("WoSelectSupervisorBySpec")
public class WoSelectSupervisorBySpec implements SelectUserInterface {
	@Autowired
	private ItcMvcService itcMvcService;
    @Autowired
    private IAuthorizationManager authManager;
    private static final Logger LOG = Logger.getLogger(WoSelectSupervisorBySpec.class);

    public WoSelectSupervisorBySpec() {
    }

    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
        LOG.info("-------------进入湛江生物质根据工单专业选择项目负责人接口-----------------");
        
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String currentUserId = userInfoScope.getUserId();//当前用户ID
		List<SecureUser> resultList1 = authManager.retriveUsersWithSpecificGroup("SWF_WO_EXPERT_MACHINE", null, false, true);
		List<SecureUser> resultList2 = authManager.retriveUsersWithSpecificGroup("SWF_WO_EXPERT_ELECTRIC", null, false, true);
		List<SecureUser> resultList3 = authManager.retriveUsersWithSpecificGroup("SWF_WO_EXPERT_PLAN", null, false, true);
		boolean machineFlag = false;
		boolean electricFlag = false;
		boolean planFlag = false;
		for (SecureUser secureUser : resultList1) {
			if(secureUser.getId().equals(currentUserId)){
				machineFlag = true;
				break;
			}
		}
		for (SecureUser secureUser : resultList2) {
			if(secureUser.getId().equals(currentUserId)){
				electricFlag = true;
				break;
			}
		}
		for (SecureUser secureUser : resultList3) {
			if(secureUser.getId().equals(currentUserId)){
				planFlag = true;
				break;
			}
		}
		
        List<SecureUser> resultList = new ArrayList<SecureUser>();
        resultList1.clear();
        resultList2.clear();
        resultList3.clear();
    	if(machineFlag){
    		resultList1 = authManager.retriveUsersWithSpecificGroup("SWF_WO_SUPERVISOR_MACHINE", null, false, true);
    	}
    	if(electricFlag){
    		resultList2 = authManager.retriveUsersWithSpecificGroup("SWF_WO_SUPERVISOR_ELECTRIC", null, false, true);
    	}
    	if(planFlag){
    		resultList3 = authManager.retriveUsersWithSpecificGroup("SWF_WO_SUPERVISOR_PLAN", null, false, true);
    	}
    	
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
        return resultList;
    }  
}
