package com.timss.itsm.flow.core.wo.v001;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class SendWorkorder extends TaskHandlerBase {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmWorkOrderDao workOrderDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService woUtilService;
    @Autowired
    HomepageService homepageService;
    @Autowired
    private ItsmWorkOrderService itsmWorkOrderService;
    
    private static final Logger LOG = Logger.getLogger( SendWorkorder.class );

    public void init(TaskInfo taskInfo) {

        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "sendWO";
        woUtilService.updateBusinessStatus(itsmWorkOrderService, woId, woStatus );
    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘派发工单’的onComplete(),开始处理业务逻辑-----------------" );

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );
            JSONObject businessDataobj = JSONObject.fromObject( businessData );
            String workOrderFormDate = businessDataobj.getString( "workOrderForm" );
            ItsmWorkOrder workOrder = JsonHelper.toObject( workOrderFormDate, ItsmWorkOrder.class );
            workOrderDao.updateWOByCsOnCommit( workOrder );
            //修改待办列表中的“名称”
            String todoNameString = workOrder.getDescription();
            if(workOrder.getWoName()==null || workOrder.getWoName().isEmpty()){
                todoNameString = todoNameString.length()<=100?todoNameString:todoNameString.substring( 0, 100 );
            }else{
                todoNameString = workOrder.getWoName();
            }
            homepageService.modify( null, workOrder.getWorkOrderCode(), null, todoNameString, null, null, null, null );
            
            String attachmentIds = businessDataobj.getString( "attachmentIds" );
            // 操作附件
            woUtilService.insertAttachMatch( woId, attachmentIds, "WO", "sendWorkorder" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        woUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService,woId, userInfoScope, "normal" );

        // 修改最新派单时间
        workOrderDao.updateSendWoTime( woId, new Date() );

    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        woUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService,woId, userInfoScope, "rollback" );

    }

}
