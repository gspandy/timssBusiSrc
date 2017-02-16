package com.timss.finance.flow.hyg.travelapply.v001;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.facade.util.InitUserAndSiteIdNewUtil;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.timss.finance.util.ChangeStatusUtil;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class Apply extends TaskHandlerBase {

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    WorkflowService wfs; // by type
    
    @Autowired
    private HomepageService homepageService;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    @Qualifier("FinanceManagementApplySpecialServiceImpl")
    FinanceManagementApplySpecialService financeManagementApplySpecialService;

    private Logger logger = Logger.getLogger( Apply.class );

    public void init(TaskInfo taskInfo) {
        String id = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "businessId" );
        financeManagementApplySpecialService.updateFinanceManageApplyFlowStatus( id, "apply" );
    }

    public void onComplete(TaskInfo taskInfo) {
        UserInfoScope userInfoScope = (UserInfoScope) itcMvcService.getUserInfoScopeDatas();
        FinanceManagementApply fma = getProjectFromBrowser( "出差申请工作流节点" + taskInfo.getTaskDefKey() + " 获取的信息" );
        String id = null;
        if ( fma != null ) {
            id = fma.getId();
            ChangeStatusUtil.changeToApprovingStatus( fma );
            InitUserAndSiteIdNewUtil.initUpdate( fma, itcMvcService );
            financeManagementApplySpecialService.updateFinanceManagementApplyApproving( fma, null );
        } else {
            // 移动端处理
            String workflowId = taskInfo.getProcessInstanceId();
            String businessId = (String) wfs.getVariable( workflowId, "businessId" );
            id = businessId;
            financeManagementApplySpecialService.updateFinanceManagementApplyApproving( businessId );
        }
        //更新代办名称
        String flowCode = fma.getId();
        String name = fma.getName();
        if(name.length()>50){
        	name = name.substring(0,47)+"...";
        }
        homepageService.modify(null, flowCode, null, name, null, null, null, null);
        financeManagementApplySpecialService.updateFinApplyCurrHandlerUser( id, userInfoScope, "normal" );
        
        if(fma.getProInstId() !=null && fma.getProInstId() !=""){//对于退回到第一个节点修改了字段的情况 流程分支判断的参数重新设置
    	String type = fma.getType();//outProvince 省外   inProvince 省内
	    //计算出差天数
	    Date beginDate = fma.getStrDate();
        Date endDate = fma.getEndDate();
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(beginDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(endDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        int applyNum =(day2-day1)+1;
         
        wfs.setVariable(fma.getProInstId(), "type", type);
        wfs.setVariable(fma.getProInstId(), "applyNum", applyNum);
        }
        logger.info( "完成出差申请工作流节点" + taskInfo.getTaskDefKey() + "对信息的更新" );
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
