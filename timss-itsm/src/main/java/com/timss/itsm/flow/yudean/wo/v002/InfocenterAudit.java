package com.timss.itsm.flow.yudean.wo.v002;

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

public class InfocenterAudit extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService itsmWoUtilService;
    @Autowired
    private ItsmWorkOrderService itsmWorkOrderService;
    
    private static final Logger LOG = Logger.getLogger( InfocenterAudit.class );

    public void init(TaskInfo taskInfo) {

        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "infoCenterAudit";
        itsmWoUtilService.updateBusinessStatus(itsmWorkOrderService, woId, woStatus );

    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘信息中心审批’的onComplete(),开始处理业务逻辑-----------------" );

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        try {
            itsmWoUtilService.updateWoAndAttach( woId, userInfoScope, "infoCenterAudit" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        
        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject businessDataobj = JSONObject.fromObject( businessData );
        String centerNextAudit = businessDataobj.getString( "centerNextAudit" );
        if("endWo".equals( centerNextAudit )){
            // 修改工单的状态
            itsmWoUtilService.updateBusinessStatus(itsmWorkOrderService, woId, "woFiling" );
            // 回访完之后，进入已结束状态，需要清空当前处理人信息
            itsmWoUtilService.clearBusinessCurrHandlerUser(itsmWorkOrderService, woId );
        }else{
            itsmWoUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService, woId, userInfoScope, "normal" );
        }

        LOG.debug( "-------------进入‘信息中心审批’的onComplete(),业务逻辑处理结束-----------------" );
    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService, woId, userInfoScope, "rollback" );

    }

}
