package com.timss.purchase.service.swf;

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

@Component("PurchaseSelectGKHandler")
public class PurchaseSelectGKHandler implements SelectUserInterface {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private IAuthorizationManager authManager;
    private static final Logger LOG = Logger.getLogger(PurchaseSelectGKHandler.class);

    public PurchaseSelectGKHandler() {
    }

    @SuppressWarnings("deprecation")
    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
        LOG.info("-------------进入湛江生物质合同付款审批流程，选择归口部门负责人节点-----------------");
        Object itemTypeObj = this.workflowService.getVariable(selectUserInfo.getProcessInstId(), "itemType");
        String itemType = null!=itemTypeObj?itemTypeObj.toString():"" ;
        List<SecureUser> resultList = new ArrayList<SecureUser>();
        if(itemType!=null){
        	if(itemType.equals("LABOUR")){	//劳保用品
        		resultList = authManager.retriveUsersWithSpecificGroup("SWF_FKGK_RLDQB", null, false, true);
        	}
        	else if(itemType.equals("FIRE") || itemType.equals("OFFICE") || itemType.equals("IT") || itemType.equals( "EASYCONSUMED" )){	//办公用品、消防用品、IT类、低值易耗品
        		resultList = authManager.retriveUsersWithSpecificGroup("SWF_FKGK_ZHB", null, false, true);
        	}
        }
        return resultList;
    }
}
