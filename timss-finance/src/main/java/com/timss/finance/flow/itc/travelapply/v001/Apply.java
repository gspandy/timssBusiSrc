package com.timss.finance.flow.itc.travelapply.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.facade.util.InitUserAndSiteIdNewUtil;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.timss.finance.util.ChangeStatusUtil;
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

        financeManagementApplySpecialService.updateFinApplyCurrHandlerUser( id, userInfoScope, "normal" );

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
