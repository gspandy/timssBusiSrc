package com.timss.itsm.flow.yudean.wo.v002;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class GroupdeptAudit extends TaskHandlerBase {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService itsmWoUtilService;
    @Autowired
    private ItsmWorkOrderService itsmWorkOrderService;

    private static final Logger LOG = Logger.getLogger( GroupdeptAudit.class );

    public void init(TaskInfo taskInfo) {

        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "groupDeptAudit";
        itsmWoUtilService.updateBusinessStatus(itsmWorkOrderService, woId, woStatus );

    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘集团部门审批’的onComplete(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        try {
            itsmWoUtilService.updateWoAndAttach( woId, userInfoScope, "groupDeptAudit" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
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
