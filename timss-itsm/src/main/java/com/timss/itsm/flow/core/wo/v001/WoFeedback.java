package com.timss.itsm.flow.core.wo.v001;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class WoFeedback extends TaskHandlerBase {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmWorkOrderDao workOrderDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService woUtilService;
    @Autowired
    private ItsmWorkOrderService itsmWorkOrderService;
    
    private static final Logger LOG = Logger.getLogger( WoFeedback.class );

    public void init(TaskInfo taskInfo) {

        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "woFeedback";
        woUtilService.updateBusinessStatus(itsmWorkOrderService, woId, woStatus );

    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘IT工单汇报’的onComplete(),开始处理业务逻辑-----------------" );

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );

        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject businessDataobj = JSONObject.fromObject( businessData );

        String woAcceptanceFormStr = businessDataobj.getString( "woAcceptanceForm" );
        String attachmentIds = businessDataobj.getString( "attachmentIds" );
        JSONObject woAcceptanceFormobj = JSONObject.fromObject( woAcceptanceFormStr );

        // 操作附件
        try {
            woUtilService.insertAttachMatch( woId, attachmentIds, "WO", "woFeedback" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "evaluateService", woAcceptanceFormobj.get( "evaluateService" ) );
        params.put( "feedbackRemarks", woAcceptanceFormobj.get( "feedbackRemarks" ) );
        params.put( "id", woId );
        params.put( "modifyuser", userId );
        params.put( "modifydate", new Date() );
        // 添加回访信息
        workOrderDao.updateWOOnFeedback( params );

        // 修改验收人信息
        Map<String, String> params1 = new HashMap<String, String>();
        params1.put( "woId", woId );
        params1.put( "acceptanceUser", userId );
        workOrderDao.updateOperUserById( params1 );

        // 修改工单的状态
        woUtilService.updateBusinessStatus(itsmWorkOrderService, woId, "woFiling" );
        // 回访完之后，进入已结束状态，需要清空当前处理人信息
        woUtilService.clearBusinessCurrHandlerUser(itsmWorkOrderService, woId );
    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        woUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService, woId, userInfoScope, "rollback" );
    }

}
