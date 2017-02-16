package com.timss.finance.flow.dpp.travelapply.v001;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.facade.util.InitUserAndSiteIdNewUtil;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.dao.FinanceManagementApplyDao;
import com.timss.finance.service.FinanceManagementApplyService;
import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.timss.finance.util.ChangeStatusUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class LeaderAudit extends TaskHandlerBase {
	@Autowired
	private WorkflowService wfs; //by type
	@Autowired
        private ItcMvcService itcMvcService;
	@Autowired
	@Qualifier("FinanceManagementApplySpecialServiceImpl")
	private FinanceManagementApplySpecialService financeManagementApplySpecialService;
	@Autowired
        private FinanceManagementApplyService financeManagementApplyService;
	@Autowired
        FinanceManagementApplyDao financeManagementApplyDao;
	
	private static Logger logger = Logger.getLogger(LeaderAudit.class);
	
	public void init(TaskInfo taskInfo) {
		String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
		financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus(id, "gsldsp");
	}
	
	 public void onComplete(TaskInfo taskInfo) {
	        String id = wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
	        FinanceManagementApply fma = getProjectFromBrowser( "出差申请工作流节点" + taskInfo.getTaskDefKey() + " 获取的信息" );
	        if(fma == null){
	            fma = financeManagementApplyService.queryFinanceManagementApplyById( id );
	        }
	        ChangeStatusUtil.changeToApprovingStatus( fma );
	        InitUserAndSiteIdNewUtil.initUpdate( fma, itcMvcService );
	        financeManagementApplySpecialService.updateFinanceManagementApplyApproving( fma, null );
	       
	        Map<String, String> parmas = new HashMap<String, String>();
	        parmas.put("id", id);
	        parmas.put("currHandlerUser", "");
	        parmas.put("currHandUserName", "");
	        financeManagementApplyDao.updateCurrHandUserById(parmas);
	        
	        financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus(id,"AE");
	        
	        financeManagementApplySpecialService.updateFinanceManagementApplyApproved(id);      
	        
	        logger.info( "公司领导审批工作流节点" + taskInfo.getTaskDefKey() + "对信息的更新" );
	    }

	    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
	        logger.debug( "-------------进入‘分管副总审批回退’的beforeRollBack(),开始处理业务逻辑-----------------" );
	        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	        String id = wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
	        financeManagementApplySpecialService.updateFinApplyCurrHandlerUser( id, userInfoScope, "rollback" );

	    }
	    
	    private FinanceManagementApply getProjectFromBrowser(String prefix) {
                UserInfoScope userInfoScope = (UserInfoScope) itcMvcService.getUserInfoScopeDatas();
                try {
                    String fmaString = userInfoScope.getParam( "businessData" );
                    logger.info( prefix + ":" + fmaString );
                    FinanceManagementApply project = null;
                    if ( fmaString != null && !fmaString.equals( "" ) ) {
                        project = JsonHelper.fromJsonStringToBean( fmaString, FinanceManagementApply.class );
                    } else {
                        return null;
                    }

                    return project;
                } catch (Exception e) {
                    throw new RuntimeException( "出差申请工作流获取信息失败", e );
                }
         }
	    
}
