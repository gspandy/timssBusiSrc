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
import com.timss.inventory.vo.InvMatApplyToWorkOrder;
import com.timss.workorder.bean.JPItems;
import com.timss.workorder.bean.MaintainPlan;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.JobPlanService;
import com.timss.workorder.service.MaintainPlanService;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.util.InvItemVOtoJPItemsUtil;
import com.timss.workorder.util.WoNoUtil;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class EndWoReport extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkOrderDao workOrderDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    @Qualifier("MaintainPlanServiceImpl")
    private MaintainPlanService maintainPlanService;
    @Autowired
    @Qualifier("JobPlanServiceImpl")
    private JobPlanService jobPlanService;

    @Autowired
    private InvMatApplyService invMatApplyService;
    @Autowired
    private WoUtilService woUtilService;

    private static Logger logger = Logger.getLogger( EndWoReport.class );

    public void init(TaskInfo taskInfo) {

        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "endWOReport";
        woUtilService.updateWoStatus( woId, woStatus );
    }

    public void onComplete(TaskInfo taskInfo) {

        logger.debug( "-------------进入onComplete(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        // 先根据工单ID，去作业方案表中，删掉对应的完工汇报的内容（因为是暂存），然后查找出对应的策划时的内容，根据函数计算出策划和此时提交的内容差异，
        // 调用库存接口修改库存,然后入库完工汇报的内容以及修改遗留问题的信息（根据工单ID查找预留问题，然后修改，如果没有则插入，插入时需要加上woId）

        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );
        } catch (Exception e) {
            logger.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject businessDataobj = JSONObject.fromObject( businessData );
        String woFormDataStr = businessDataobj.getString( "workOrderForm" );
        String woReportFormDataStr = businessDataobj.getString( "woReportForm" );
        JSONObject woReportFormDataObj = JSONObject.fromObject( woReportFormDataStr );
        String jobPlanId = businessDataobj.getString( "jobPlanId" );

        String isHasRemainFault = woReportFormDataObj.getString( "isHasRemainFault" );
        String RemainFaultFormStr = null;
        if ( "has_remainFault".equals( isHasRemainFault ) ) { // 枚举变量has_remainFault,no_remainFault
            RemainFaultFormStr = businessDataobj.getString( "remainFaultFormData" );
        }

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
        // 删除汇报内容
        jobPlanService.deleteWOReportByWOId( Integer.valueOf( woId ) );
        // 获取策划时 工具的内容列表
        // JobPlan woPlanJobPlan =
        // jobPlanService.queryPlanJPByWOId(Integer.valueOf(woId));//查询此工单策划时的内容
        // List<JPItems> jpPlanItemsList = new ArrayList<JPItems>();
        // if(woPlanJobPlan != null){
        // int woPlanJPId = woPlanJobPlan.getId();
        // jpPlanItemsList = jpItemsDao.queryJPItemsByJPId(woPlanJPId);
        // //策划时的工具列表
        // }

        // TODO 通过接口获取物料内容列表
        Page<InvMatApplyToWorkOrder> invItemVOList;
        try {
            invItemVOList = invMatApplyService.queryMatApplyByWoId( woId, "wo_picking" );
        } catch (Exception e) {
            logger.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        List<InvMatApplyToWorkOrder> itemsList = invItemVOList.getResults();
        List<JPItems> jpPlanItemsList = InvItemVOtoJPItemsUtil.invItemVOtoJPItem( itemsList );

        // 获取汇报时 工具的内容列表
        JSONObject itemsJsonObj = JSONObject.fromObject( toolDataStr );
        int itemsDatagridNum = Integer.valueOf( itemsJsonObj.get( "total" ).toString() ); // 记录数
        JSONArray itemsJsonArray = itemsJsonObj.getJSONArray( "rows" ); // 记录数组
        List<JPItems> jpReportItemsList = new ArrayList<JPItems>(); // 汇报时的工具列表
        for ( int i = 0; i < itemsDatagridNum; i++ ) {
            String itemsRecord = itemsJsonArray.get( i ).toString(); // 某条记录的字符串表示
            JPItems jpItems;
            try {
                jpItems = JsonHelper.fromJsonStringToBean( itemsRecord, JPItems.class );
            } catch (Exception e) {
                logger.error( e.getMessage() );
                throw new RuntimeException( e );
            }
            jpReportItemsList.add( jpItems );
        }
        // 获取策划和汇报时 工具的数量差异
        JSONObject itemsNumSUB = new JSONObject();
        if ( jpPlanItemsList.size() > 0 ) {
            itemsNumSUB = WoNoUtil.getItemsNumSubtraction( jpPlanItemsList, jpReportItemsList );
            HashMap<String, Object> dataToInventory = new HashMap<String, Object>();
            dataToInventory.put( "applyUser", userId );
            dataToInventory.put( "type", "in" );
            dataToInventory.put( "woId", woId );
            dataToInventory.put( "woCode", workOrder.getWorkOrderCode() );
            dataToInventory.put( "Items", itemsNumSUB );
            // System.out.println(dataToInventory);
            // //TODO 调用库存的接口，对库存进行增加 没用完的物资入库 dataToInventory
            // 格式：{Items={"1408":3,"1406":2,"1407":1}, applyUser=126060,
            // woId=181, woCode=WO20140723004, type=in}
            // try {
            // invMatTranService.workOrderTriggerProcesses(dataToInventory);
            // } catch (Exception e) {
            // logger.error(e.getMessage());
            // throw new RuntimeException(e);
            // }
        }

        // 插入工单汇报内容
        HashMap<String, String> addJPDataMap = new HashMap<String, String>();
        addJPDataMap.put( "woFormData", woFormDataStr );
        addJPDataMap.put( "preHazardDataStr", preHazardDataStr );
        addJPDataMap.put( "toolDataStr", toolDataStr );
        addJPDataMap.put( "taskDataStr", taskDataStr );
        addJPDataMap.put( "workerDataStr", workerDataStr );
        addJPDataMap.put( "jpSource", "actual" ); // 作业方案的来源：standard:标准作业方案；maintainPlan:维护计划；
                                                  // plan：工单策划；actual:实际

        int jpId = 0;
        if ( !"null".equals( jobPlanId ) && !"".equals( jobPlanId )) {
            jpId = Integer.valueOf( jobPlanId );
        }

        try {
            if ( jpId != 0 ) {
                jpId = jobPlanService.insertJobPlan( addJPDataMap );
            } else {
                jpId = jobPlanService.updateJobPlan( addJPDataMap );
            }
        } catch (Exception e) {
            logger.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        // 遗留问题添加入库（插入到维护计划列表中）
        int remainFaultId = 0;
        if ( RemainFaultFormStr != null ) {
            HashMap<String, String> maintainPlanData = new HashMap<String, String>();
            maintainPlanData.put( "mtpFormData", RemainFaultFormStr );
            maintainPlanData.put( "mtpSource", "remainFault_maintainPlan" );
            MaintainPlan maintainPlan = new MaintainPlan();
            try {
                maintainPlan = maintainPlanService.insertMaintainPlan( maintainPlanData );
            } catch (Exception e) {
                logger.error( e.getMessage() );
                throw new RuntimeException( e );
            }
            remainFaultId = maintainPlan.getId(); // 遗留问题ID

        }
        // 修改工单信息
        JSONObject woReportFormJsonObj = JSONObject.fromObject( woReportFormDataStr );

        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "id", Integer.valueOf( woId ) );

        Date beginTime = null;
        Date endTime = null;
        if ( woReportFormJsonObj.get( "beginTime" ) != null ) {
            long begin = (Long) woReportFormJsonObj.get( "beginTime" );
            beginTime = new Date( begin );
        }
        if ( woReportFormJsonObj.get( "beginTime" ) != null ) {
            long end = (Long) woReportFormJsonObj.get( "endTime" );
            endTime = new Date( end );
        }
        parmas.put( "beginTime", beginTime ); // 添加作业方案的ID（最新的）
        parmas.put( "endTime", endTime );

        parmas.put( "isHasRemainFault", woReportFormJsonObj.get( "isHasRemainFault" ) );
        parmas.put( "endReport", woReportFormJsonObj.get( "endReport" ) );
        parmas.put( "remainFaultId", remainFaultId ); // remainFaultId为遗留问题插入到维护计划表中的ID
        parmas.put( "jobPlanId", ((jpId == 0) ? null : jpId) ); // 添加作业方案的ID（最新的）
        parmas.put( "modifyuser", userId );
        parmas.put( "modifydate", new Date() );

        workOrderDao.updateWOOnReport( parmas );// 修改业务表中的数据

        // 修改汇报人信息
        HashMap<String, String> parmas1 = new HashMap<String, String>();
        parmas1.put( "woId", woId );
        parmas1.put( "endReportUser", userId );
        workOrderDao.updateOperUserById( parmas1 );

        // 修改当前执行人
        woUtilService.updateWoCurrHandlerUser( woId, userInfoScope, "normal" );

    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        // 修改当前执行人
        try {
            String userId = userInfoScope.getParam( "userId" );
            HashMap<String, String> parmas = new HashMap<String, String>();
            parmas.put( "woId", woId );
            parmas.put( "currHandlerUser", userId );
            workOrderDao.updateOperUserById( parmas );
        } catch (Exception e) {
            logger.error( e.getMessage() );
            throw new RuntimeException( e );
        }
    }
}
