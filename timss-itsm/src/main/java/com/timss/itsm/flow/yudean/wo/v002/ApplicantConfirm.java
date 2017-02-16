package com.timss.itsm.flow.yudean.wo.v002;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.UserExtParam;
import com.yudean.workflow.bean.WorkFlowExtParam;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class ApplicantConfirm extends TaskHandlerBase {

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService itsmWoUtilService;
    @Autowired
    private ItsmWorkOrderService itsmWorkOrderService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private IAuthorizationManager authManager;

    private static final Logger LOG = Logger.getLogger( ApplicantConfirm.class );

    public void init(TaskInfo taskInfo) {
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "applicantConfirm";
        itsmWoUtilService.updateBusinessStatus(itsmWorkOrderService, woId, woStatus );
    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘申请人审批’的onComplete(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        // 构造跨站点待办的参数
        WorkFlowExtParam workFlowExtParam = taskInfo.getWorkFlowExtParam();
        Map<String, UserExtParam> userExtParamMap = workFlowExtParam.getUserDataMap();
        List<SecureUser> resultList = authManager.retriveUsersWithSpecificRole( "ITC_ITSM_KF", null, false, true );

        for ( int i = 0; i < resultList.size(); i++ ) {
            String selectUserId = resultList.get( i ).getId();
            UserExtParam userParam = new UserExtParam( selectUserId, "ITC" );
            if ( userExtParamMap.containsKey( selectUserId ) ) {
                userExtParamMap.remove( selectUserId );
            }
            userExtParamMap.put( selectUserId, userParam );
        }

        // 修改当前处理人信息
        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService, woId, userInfoScope, "normal" );

    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService, woId, userInfoScope, "rollback" );
    }

}
