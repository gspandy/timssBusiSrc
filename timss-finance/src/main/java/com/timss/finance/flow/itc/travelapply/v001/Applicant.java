package com.timss.finance.flow.itc.travelapply.v001;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.facade.util.InitUserAndSiteIdNewUtil;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.dao.FinanceManagementApplyDao;
import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.timss.finance.util.ChangeStatusUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class Applicant extends TaskHandlerBase {
	
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	FinanceManagementApplyDao financeManagementApplyDao;
	@Autowired
	WorkflowService wfs; //by type
	
	@Autowired
	FinanceManagementApplySpecialService financeManagementApplySpecialService;
	
	private Logger logger = Logger.getLogger(Applicant.class);
	
	public void init(TaskInfo taskInfo) {
		String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
		financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus(id, "sqrbl");
		
	}
	
	 public void onComplete(TaskInfo taskInfo) {
             String id = wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
             Map<String, String> parmas = new HashMap<String, String>();
             parmas.put("id", id);
             parmas.put("currHandlerUser", "");
             parmas.put("currHandUserName", "");
             financeManagementApplyDao.updateCurrHandUserById(parmas);
             
             FinanceManagementApply fma = getProjectFromBrowser( "出差申请工作流节点" + taskInfo.getTaskDefKey() + " 获取的信息" );
             ChangeStatusUtil.changeToApprovingStatus( fma );
             InitUserAndSiteIdNewUtil.initUpdate( fma, itcMvcService );
             if(fma != null){
                 financeManagementApplySpecialService.updateFinanceManagementApplyApproving( fma, null );
             }
             
             financeManagementApplySpecialService.updateFinanceManagementApplyApproved(id);
             
             logger.info( "完成申请人办理工作流节点" + taskInfo.getTaskDefKey() + "对信息的更新" );
         }
	   
	 public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
	     logger.debug( "-------------进入‘申请人办理审批回退’的beforeRollBack(),开始处理业务逻辑-----------------" );
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
