package com.timss.finance.flow.hyg.travelapply.v001;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.dao.FinanceManagementApplyDao;
import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class ChargeLeader extends TaskHandlerBase {
    @Autowired
    private WorkflowService wfs; // by type
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
	FinanceManagementApplyDao financeManagementApplyDao;
    @Autowired
    @Qualifier("FinanceManagementApplySpecialServiceImpl")
    private FinanceManagementApplySpecialService financeManagementApplySpecialService;
    

    private static Logger logger = Logger.getLogger( ChargeLeader.class );

    public void init(TaskInfo taskInfo) {

        String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
        financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus( id, "chargeLeader" );

    }

    public void onComplete(TaskInfo taskInfo) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String fmaString;
		try {
			fmaString = userInfoScope.getParam( "businessData" );
			if ( fmaString != null && !fmaString.equals( "" ) ) {
				FinanceManagementApply fma = JsonHelper.fromJsonStringToBean( fmaString, FinanceManagementApply.class );
				String type = fma.getType();//outProvince 省外   inProvince 省内
				//判断是否是部门经理
				String commitUserId = fma.getApplyUser();
			    List<SecureUser> bmjlList = authManager.retriveUsersWithSpecificRole( "HYG_BMJL", null, true, true );
			    String result = "N";
			    for ( SecureUser secureUser : bmjlList ) {
			    	if(commitUserId.equals( secureUser.getId() )){
			    		result = "Y";
			        }
			    }
			    //计算出差天数
			    Date beginDate = fma.getStrDate();
		        Date endDate = fma.getEndDate();
		        Calendar aCalendar = Calendar.getInstance();
		        aCalendar.setTime(beginDate);
		        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		        aCalendar.setTime(endDate);
		        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		        int applyNum =(day2-day1)+1;
			    if("N".equals(result) && applyNum<=3 && "inProvince".equals(type)){//不是部门经理并且出差天数小于等于3天并且是省内  直接结束
			    	String id = wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
			         Map<String, String> parmas = new HashMap<String, String>();
			         parmas.put("id", id);
			         parmas.put("currHandlerUser", "");
			         parmas.put("currHandUserName", "");
			         financeManagementApplyDao.updateCurrHandUserById(parmas);
			         
			         financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus(id,"AE");
			         
			         financeManagementApplySpecialService.updateFinanceManagementApplyApproved(id); 
			    }else{
			    	String id = wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
			        financeManagementApplySpecialService.updateFinApplyCurrHandlerUser( id, userInfoScope, "normal" );
			    }
			}
		} catch (Exception e) {
			throw new RuntimeException( "出差申请工作流获取信息失败", e );
		}
    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        logger.debug( "-------------进入‘分管副总审批回退’的beforeRollBack(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        financeManagementApplySpecialService.updateFinApplyCurrHandlerUser( id, userInfoScope, "rollback" );

    }

}
