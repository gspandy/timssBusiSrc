package com.timss.itsm.flow.core.wo.v001;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.service.InvMatApplyService;
import com.timss.itsm.bean.ItsmJPItems;
import com.timss.itsm.bean.ItsmOperRecord;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.dao.ItsmOperRecordDao;
import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmWoStatisticUtilService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.timss.itsm.service.ItsmWorkTimeService;
import com.timss.itsm.vo.ItsmWorkTimeVo;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class WorkPlan extends TaskHandlerBase {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmWorkOrderDao workOrderDao;
    @Autowired
    private ItsmOperRecordDao operRecordDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService woUtilService;
    @Autowired
    private InvMatApplyService invMatApplyService;
    @Autowired
    ItsmWoStatisticUtilService itsmWoStatisticUtilService;
    @Autowired
    ItsmWorkTimeService workTimeService;
    @Autowired
    private ItsmWorkOrderService itsmWorkOrderService;
    
    private static final Logger LOG = Logger.getLogger( WorkPlan.class );

    public void init(TaskInfo taskInfo) {

        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "workPlan";
        woUtilService.updateBusinessStatus(itsmWorkOrderService, woId, woStatus );

    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘工程师处理’的onComplete(),开始处理业务逻辑-----------------" );

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String userId = userInfoScope.getUserId();

        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        ItsmWorkOrder workOrder = workOrderDao.queryWOById( woId );
        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );

        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject businessDataobj = JSONObject.fromObject( businessData );

        String woFormDataStr = businessDataobj.getString( "workOrderForm" );
        String woPlanDataStr = businessDataobj.getString( "woPlanData" );
        String attachmentIds = businessDataobj.getString( "attachmentIds" );
        String toolDataStr = businessDataobj.getString( "toolData" );
        JSONObject woPlanDataobj = JSONObject.fromObject( woPlanDataStr );

        // 操作附件
        try {
            woUtilService.insertAttachMatch( woId, attachmentIds, "WO", "workPlan" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        // 插入工单策划内容
        Map<String, String> addJPDataMap = new HashMap<String, String>();

        addJPDataMap.put( "woId", woId );
        addJPDataMap.put( "woFormData", woFormDataStr );
        addJPDataMap.put( "toolDataStr", toolDataStr );
        addJPDataMap.put( "jpSource", "plan" ); // 作业方案的来源：standard:标准作业方案；maintainPlan:维护计划；
                                                // plan：工单策划；actual:实际
        addJPDataMap.put( "commitStyle", "commit" ); // 提交方式（commit：审批，save:暂存）

        // 获取操作人属于哪个运维组
        String operUserTeam = woUtilService.getOperUserTeam( userInfoScope, workOrder );
        ItsmOperRecord operRecord = new ItsmOperRecord();
        operRecord.setWoId( woId );
        operRecord.setOperDate( new Date() );
        operRecord.setFlowId( workOrder.getWorkflowId() );
        operRecord.setOperUser( userId );
        operRecord.setSiteid( siteid );
        operRecord.setOperType( "workPlan" );
        operRecord.setOperUserTeam( operUserTeam );

        // 修改协作人员信息，延时信息，
        String planOperStyle = woPlanDataobj.get( "planOper" ).toString();

        if ( "nowHandlerWO".equals( planOperStyle ) ) { // 策划
            operRecord.setOperContent( "nowHandlerWO" );
            // 获取策划时 工具的内容列表
            JSONObject itemsJsonObj = JSONObject.fromObject( toolDataStr );
            int itemsDatagridNum = Integer.valueOf( itemsJsonObj.get( "total" ).toString() ); // 记录数
            JSONArray itemsJsonArray = itemsJsonObj.getJSONArray( "rows" ); // 记录数组

            HashMap<String, Object> dataToInventory = new HashMap<String, Object>();

            dataToInventory.put( "applyUser", userId );
            dataToInventory.put( "woName", workOrder.getDescription() );
            dataToInventory.put( "type", "out" );
            dataToInventory.put( "woId", woId );
            dataToInventory.put( "woCode", workOrder.getWorkOrderCode() );
            // 标识是哪种类型的领料
            dataToInventory.put( "applyType", "itsm_picking" );

            List<JSONObject> jpPlanItemsObjList = new ArrayList<JSONObject>();
            ; // 汇报时的工具列表(JSONObject)

            for ( int i = 0; i < itemsDatagridNum; i++ ) {
                String itemsRecord = itemsJsonArray.get( i ).toString(); // 某条记录的字符串表示
                ItsmJPItems jpItems;
                try {
                    jpItems = JsonHelper.fromJsonStringToBean( itemsRecord, ItsmJPItems.class );
                } catch (Exception e) {
                    LOG.error( e.getMessage() );
                    throw new RuntimeException( e );
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put( jpItems.getItemsId(), jpItems.getApplyCount() );

                jpPlanItemsObjList.add( jsonObject );
            }
            dataToInventory.put( "items", jpPlanItemsObjList );

            // 库存出库申请 提供数据是：dataToInventory ，
            if ( itemsDatagridNum > 0 ) {
                // 库存出库申请 提供数据是：dataToInventory ，格式是：{items=[{"38":3},
                // {"53":8}], applyUser=126060, woId=176, woCode=WO20140723003,
                // type=out}
                try {
                    invMatApplyService.workOrderTriggerProcesses( dataToInventory );
                } catch (Exception e) {
                    LOG.error( e.getMessage() );
                    throw new RuntimeException( e );
                }
            }

            LOG.info( "workOrder trigger Inventory processInst finished " );

            String partnerIds = woPlanDataobj.getString( "partnerIds" ).toString(); // 协助人员id
            String partnerNames = woPlanDataobj.getString( "partnerNames" ).toString(); // 协助人员名字

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put( "partnerIds", partnerIds );
            params.put( "partnerNames", partnerNames );
            params.put( "id", woId );
            params.put( "modifydate", new Date() );
            params.put( "modifyuser", userId );
            workOrderDao.updatePartnerInfo( params );

        } else if ( "delayWO".equals( planOperStyle ) ) { // 延时
            operRecord.setOperContent( "delayWO" );
            Long delayToTime = Long.valueOf( woPlanDataobj.getString( "delayToTime" ) ); // 延时到
            Date delayDate = new Date( delayToTime );
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put( "delayType", "workPlan" );
            params.put( "delayToTime", delayDate );
            params.put( "id", woId);
            params.put( "modifydate", new Date() );
            params.put( "modifyuser", userId );
            workOrderDao.updateDelayInfo( params );
        }
        // 修改工单的开始服务时间beginTime和响应时长respondLen(秒)
        Date beginTime = new Date();
        Date discoverTime = workOrder.getDiscoverTime();
        ItsmWorkTimeVo workTimeVo1 = itsmWoStatisticUtilService.setWorkTimeVo( discoverTime, beginTime );
        int respondLen = 0;
        try {
            respondLen = (int) (workTimeService.calDay( workTimeVo1 ) * workTimeVo1.getWorkTime() * 60 * 60);
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        workOrderDao.updateWoBeginTime( woId, beginTime, respondLen );
        woUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService,woId, userInfoScope, "normal" );
        operRecordDao.insertOperRecord( operRecord ); // 插入操作记录
    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String userId = userInfoScope.getUserId();

        if ( !userId.endsWith( "scheduler" ) ) {
            woUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService,woId, userInfoScope, "rollback" );
        }
    }

}
