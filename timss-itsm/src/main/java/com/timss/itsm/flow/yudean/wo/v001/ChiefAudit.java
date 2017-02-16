package com.timss.itsm.flow.yudean.wo.v001;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class ChiefAudit extends TaskHandlerBase {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService itsmWoUtilService;
    @Autowired
    private ItsmWorkOrderService itsmWorkOrderService;

    private static final Logger LOG = Logger.getLogger( ChiefAudit.class );

    public void init(TaskInfo taskInfo) {
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "chiefAudit";
        itsmWoUtilService.updateBusinessStatus( itsmWorkOrderService, woId, woStatus );
    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘退单时主管审批’的onComplete(),开始处理业务逻辑-----------------" );

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject businessDataobj = JSONObject.fromObject( businessData );
        String attachmentIds = businessDataobj.getString( "attachmentIds" );
        // 操作附件
        try {
            itsmWoUtilService.insertAttachMatch( woId, attachmentIds, "WO", "chiefAudit" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        itsmWoUtilService.updateBusinessCurrHandlerUser( itsmWorkOrderService, woId, userInfoScope, "normal" );
    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        itsmWoUtilService.updateBusinessCurrHandlerUser( itsmWorkOrderService, woId, userInfoScope, "rollback" );
    }

}
