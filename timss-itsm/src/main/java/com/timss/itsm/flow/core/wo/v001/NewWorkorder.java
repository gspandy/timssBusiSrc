package com.timss.itsm.flow.core.wo.v001;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.bean.ItsmOperRecord;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.dao.ItsmOperRecordDao;
import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class NewWorkorder extends TaskHandlerBase {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService woUtilService;
    @Autowired
    ItsmWorkOrderDao workOrderDao;
    @Autowired
    private ItsmOperRecordDao operRecordDao;
    @Autowired
    private ItsmWorkOrderService itsmWorkOrderService;
    
    private static final Logger LOG = Logger.getLogger( NewWorkorder.class );

    public void init(TaskInfo taskInfo) {
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "newWO";
        woUtilService.updateBusinessStatus(itsmWorkOrderService, woId, woStatus );
    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘新建工单’的onComplete(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        if ( userInfoScope != null ) {
            String userId = userInfoScope.getUserId();
            String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

            String businessData = null;
            try {
                businessData = userInfoScope.getParam( "businessData" );
                JSONObject businessDataobj = JSONObject.fromObject( businessData );
                if ( businessData != null ) {
                    String attachmentIds = businessDataobj.getString( "attachmentIds" );
                    // 操作附件
                    // 先删掉所有相关的附件数据
                    woUtilService.deleteAttachMatch(  woId , null, "WO" );
                    // 插入附件的相关数据
                    woUtilService.insertAttachMatch(  woId , attachmentIds, "WO", "newWo" );
                }

            } catch (Exception e) {
                LOG.error( e.getMessage() );
                throw new RuntimeException( e );
            }

            woUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService,woId, userInfoScope, "normal" );

            // 如果是汇报性工单，则要在操作历史里面加入工程师处理环节（虚拟的）记录
            ItsmWorkOrder workOrder = workOrderDao.queryItWOById( woId );
            String woType = workOrder.getWorkOrderTypeCode();
            String siteid = userInfoScope.getSiteId();
            if ( "hbWoType".equals( woType ) ) {
                String operUserTeam = woUtilService.getOperUserTeam( userInfoScope, workOrder );

                ItsmOperRecord operRecord = new ItsmOperRecord();
                operRecord.setWoId( woId );
                operRecord.setOperDate( new Date() );
                operRecord.setFlowId( workOrder.getWorkflowId() );
                operRecord.setOperUser( userId );
                operRecord.setSiteid( siteid );
                operRecord.setOperType( "workPlan" );
                operRecord.setOperUserTeam( operUserTeam );
                operRecord.setOperContent( "hbWoType" );
                operRecordDao.insertOperRecord( operRecord ); // 插入操作记录
            }
        }
    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {

    }

}
