package com.timss.workorder.flow.sbs.wo.v001;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.vo.InvItemVO;
import com.timss.workorder.bean.JPItems;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.JobPlanService;
import com.timss.workorder.service.WoUtilService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class WoPlan extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkOrderDao workOrderDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    @Qualifier("JobPlanServiceImpl")
    private JobPlanService jobPlanService;
    @Autowired
    private InvMatApplyService invMatApplyService;
    @Autowired
    private WoUtilService woUtilService;

    private static Logger logger = Logger.getLogger( WoPlan.class );

    public void init(TaskInfo taskInfo) {
        logger.debug( "-------------进入‘工单策划’的init(),开始处理业务逻辑-----------------" );
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "woPlan";
        woUtilService.updateWoStatus( woId, woStatus );

    }

    public void onComplete(TaskInfo taskInfo) {

        logger.info( "-------------进入‘工单策划’的onComplete(),开始处理业务逻辑-----------------" );

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );
        } catch (Exception e) {
            logger.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject businessDataobj = JSONObject.fromObject( businessData );
        String isToPTW = businessDataobj.getString( "isToPTW" );
        String jobPlanId = businessDataobj.getString( "jobPlanId" );
        String woFormDataStr = businessDataobj.getString( "workOrderForm" );
        String preHazardDataStr = businessDataobj.getString( "preHazardData" );
        String toolDataStr = businessDataobj.getString( "toolData" );
        String taskDataStr = businessDataobj.getString( "taskData" );
        String workerDataStr = businessDataobj.getString( "workerData" );

        WorkOrder workOrder;
        try {
            workOrder = JsonHelper.toObject( woFormDataStr, WorkOrder.class );
        } catch (Exception e) {
            logger.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        // 插入工单策划内容
        HashMap<String, String> addJPDataMap = new HashMap<String, String>();

        addJPDataMap.put( "woFormData", woFormDataStr );
        addJPDataMap.put( "preHazardDataStr", preHazardDataStr );
        addJPDataMap.put( "toolDataStr", toolDataStr );
        addJPDataMap.put( "taskDataStr", taskDataStr );
        addJPDataMap.put( "workerDataStr", workerDataStr );
        addJPDataMap.put( "jpSource", "plan" ); // 作业方案的来源：standard:标准作业方案；maintainPlan:维护计划；
                                                // plan：工单策划；actual:实际
        addJPDataMap.put( "commitStyle", "commit" ); // 提交方式（commit：审批，save:暂存）

        int jpId = 0;
        if ( !"null".equals( jobPlanId ) && !"".equals( jobPlanId ) ) {
            jpId = Integer.valueOf( jobPlanId );
        }

        try {
            if ( jpId == 0 ) {
                jpId = jobPlanService.insertJobPlan( addJPDataMap );
            } else {
                jpId = jobPlanService.updateJobPlan( addJPDataMap );
            }
        } catch (Exception e) {
            logger.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        // TODO 库存出库申请
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
        dataToInventory.put( "applyType", "wo_picking" );

        List<JSONObject> jpPlanItemsObjList = new ArrayList<JSONObject>();
         // 汇报时的工具列表(JSONObject)
        for ( int i = 0; i < itemsDatagridNum; i++ ) {
            String itemsRecord = itemsJsonArray.get( i ).toString(); // 某条记录的字符串表示
            JPItems jpItems;
            InvItemVO invMatApplyDetail;
            try {
                jpItems = JsonHelper.fromJsonStringToBean( itemsRecord, JPItems.class );
                invMatApplyDetail = JsonHelper.fromJsonStringToBean( itemsRecord, InvItemVO.class );
            } catch (Exception e) {
                logger.error( e.getMessage() );
                throw new RuntimeException( e );
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put( jpItems.getItemsId()+ "_applyCount", jpItems.getApplyCount() );
            jsonObject.put( jpItems.getItemsId() + "_wareHouseId", invMatApplyDetail.getWarehouseid() );
            jsonObject.put( jpItems.getItemsId() + "_cateId", invMatApplyDetail.getCateId() );


            jpPlanItemsObjList.add( jsonObject );
        }
        dataToInventory.put( "items", jpPlanItemsObjList );

        if ( itemsDatagridNum > 0 ) {
            // 库存出库申请 提供数据是：dataToInventory ，格式是：
           // {woName=SBS_0706001, applyUser=962023, items=[{"III1259":1}], woId=2023, woCode=WO20150706002, applyType=wo_picking, type=out}
            try {
                invMatApplyService.workOrderTriggerProcesses( dataToInventory );
            } catch (Exception e) {
                logger.error( "工单调用库存的接口出错："+e.getMessage());
                throw new RuntimeException( e );
            }

        }

        // 修改工单信息
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "isToPTW", isToPTW ); // 是否走工作票
        parmas.put( "id", woId );
        parmas.put( "jobPlanId", ((jpId == 0) ? null : jpId) ); // 添加作业方案的ID（最新的）
        parmas.put( "modifyuser", userId );
        parmas.put( "modifydate", new Date() );
        // TODO 此处需要获得走工作票时的工作票ID
        parmas.put( "ptwId", 0 ); // 关联工作票的ID

        workOrderDao.updateWOOnPlan( parmas );// 修改业务表中的数据

        // 修改当前执行人
        woUtilService.updateWoCurrHandlerUser( woId, userInfoScope, "normal" );
    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        // 修改当前执行人
        woUtilService.updateWoCurrHandlerUser( woId, userInfoScope, "rollback" );
    }
}
