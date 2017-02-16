package com.timss.itsm.flow.yudean.wo.v002;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.bean.ItsmJPItems;
import com.timss.itsm.bean.ItsmJobPlan;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.dao.ItsmJPItemsDao;
import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmJobPlanService;
import com.timss.itsm.service.ItsmWoStatisticUtilService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.timss.itsm.service.ItsmWorkTimeService;
import com.timss.itsm.util.WoNoUtil;
import com.timss.itsm.vo.ItsmWorkTimeVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.UserExtParam;
import com.yudean.workflow.bean.WorkFlowExtParam;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class EndWorkReport extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmWorkOrderDao workOrderDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmJobPlanService jobPlanService;
    @Autowired
    private ItsmJPItemsDao jpItemsDao;
    @Autowired
    private ItsmWoUtilService itsmWoUtilService;
    @Autowired
    ItsmWoStatisticUtilService itsmWoStatisticUtilService;
    @Autowired
    ItsmWorkTimeService workTimeService;
    @Autowired
    HomepageService homepageService;
    @Autowired
    private ItsmWorkOrderService itsmWorkOrderService;
    
    private static final Logger LOG = Logger.getLogger( EndWorkReport.class );

    public void init(TaskInfo taskInfo) {

        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "endWorkReport";
        itsmWoUtilService.updateBusinessStatus(itsmWorkOrderService, woId, woStatus );

    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘完工汇报’的onComplete(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        ItsmWorkOrder workorder = workOrderDao.queryItWOById(  woId );
        String woNameString = workorder.getWoName();
        String createuserId = workorder.getCreateuser();
        String createSiteId = workorder.getSiteid();

        WorkFlowExtParam workFlowExtParam = taskInfo.getWorkFlowExtParam();
        Map<String, UserExtParam> userExtParamMap = workFlowExtParam.getUserDataMap();
        UserExtParam userParam = new UserExtParam( createuserId, createSiteId );
        if ( userExtParamMap.containsKey( createuserId ) ) {
            userExtParamMap.remove( createuserId );
        }
        userExtParamMap.put( createuserId, userParam );

        // 先根据工单ID，去作业方案表中，删掉对应的完工汇报的内容（因为是暂存），然后查找出对应的策划时的内容，根据函数计算出策划和此时提交的内容差异，
        // 调用资产接口修改库存,然后入库完工汇报的内容以及修改遗留问题的信息（根据工单ID查找预留问题，然后修改，如果没有则插入，插入时需要加上woId）

        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject businessDataobj = JSONObject.fromObject( businessData );

        String woFormDataStr = businessDataobj.getString( "workOrderForm" );
        String woReportDataStr = businessDataobj.getString( "woReportData" );
        String toolDataStr = businessDataobj.getString( "toolData" );
        String attachmentIds = businessDataobj.getString( "attachmentIds" );

        JSONObject woReportDataobj = JSONObject.fromObject( woReportDataStr );

        ItsmWorkOrder workOrder;
        try {
            workOrder = JsonHelper.toObject( woFormDataStr, ItsmWorkOrder.class );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        // 操作附件
        try {
            itsmWoUtilService.insertAttachMatch( woId, attachmentIds, "WO", "endWorkReport" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        // 删除汇报内容（暂存的），可删掉下面一行代码（现在汇报环节已经没有暂存功能）
        jobPlanService.deleteWOReportByWOId(  woId );

        // 获取策划时 工具的内容列表
        ItsmJobPlan woPlanJobPlan = jobPlanService.queryPlanJPByWOId( woId );// 查询此工单策划时的内容
        List<ItsmJPItems> jpPlanItemsList = new ArrayList<ItsmJPItems>();
        if ( woPlanJobPlan != null ) {
            int woPlanJPId = woPlanJobPlan.getId();
            jpPlanItemsList = jpItemsDao.queryJPItemsByJPId( woPlanJPId ); // 策划时的工具列表
        }

        // 获取汇报时 工具的内容列表
        JSONObject itemsJsonObj = JSONObject.fromObject( toolDataStr );
        int itemsDatagridNum = Integer.valueOf( itemsJsonObj.get( "total" ).toString() ); // 记录数
        JSONArray itemsJsonArray = itemsJsonObj.getJSONArray( "rows" ); // 记录数组
        List<ItsmJPItems> jpReportItemsList = new ArrayList<ItsmJPItems>(); // 汇报时的工具列表
        for ( int i = 0; i < itemsDatagridNum; i++ ) {
            String itemsRecord = itemsJsonArray.get( i ).toString(); // 某条记录的字符串表示
            ItsmJPItems jpItems;
            try {
                jpItems = JsonHelper.fromJsonStringToBean( itemsRecord, ItsmJPItems.class );
            } catch (Exception e) {
                LOG.error( e.getMessage() );
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
            // TODO 调用库存的接口，对库存进行增加 没用完的物资入库 dataToInventory
            // 格式：{Items={"1408":3,"1406":2,"1407":1}, applyUser=126060,
            // woId=181, woCode=WO20140723004, type=in}
            // try {
            // invMatTranService.workOrderTriggerProcesses(dataToInventory);
            // } catch (Exception e) {
            // LOG.error(e.getMessage());
            // throw new RuntimeException(e);
            // }
        }

        // 插入工单汇报内容
        HashMap<String, String> addJPDataMap = new HashMap<String, String>();
        addJPDataMap.put( "woFormData", woFormDataStr );
        addJPDataMap.put( "toolDataStr", toolDataStr );
        addJPDataMap.put( "jpSource", "actual" ); // 作业方案的来源：standard:标准作业方案；maintainPlan:维护计划；
                                                  // plan：工单策划；actual:实际

        // 修改工单信息
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put( "id", woId );

        Date discoverTime = workOrder.getDiscoverTime();
        Date beginTime = null;
        Date endTime = null;
        int respondLen = 0;
        int solveLen = 0;
        try {
            if ( woReportDataobj.get( "beginTime" ) != null ) {
                long begin = (Long) woReportDataobj.get( "beginTime" );
                beginTime = new Date( begin );
                ItsmWorkTimeVo workTimeVo1 = itsmWoStatisticUtilService.setWorkTimeVo( discoverTime, beginTime );
                respondLen = (int) (workTimeService.calDay( workTimeVo1 ) * workTimeVo1.getWorkTime() * 60 * 60);
            }
            if ( woReportDataobj.get( "beginTime" ) != null ) {
                long end = (Long) woReportDataobj.get( "endTime" );
                endTime = new Date( end );
                ItsmWorkTimeVo workTimeVo1 = itsmWoStatisticUtilService.setWorkTimeVo( discoverTime, endTime );
                solveLen = (int) (workTimeService.calDay( workTimeVo1 ) * workTimeVo1.getWorkTime() * 60 * 60);
            }
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        params.put( "beginTime", beginTime ); // 添加作业方案的ID（最新的）
        params.put( "endTime", endTime );
        params.put( "respondLen", respondLen );
        params.put( "solveLen", solveLen );
        params.put( "isHasRemainFault", "no_remainFault" ); // IT工单中没有遗留问题，
        params.put( "remainFaultId", 0 );
        params.put( "endReport", woReportDataobj.get( "endReport" ) );
        params.put( "jobPlanId", null ); // 添加作业方案的ID（最新的）
        params.put( "modifyuser", userId );
        params.put( "modifydate", new Date() );
        // 修改服务目录、服务性质、故障描述
        params.put( "faultTypeId", workOrder.getFaultTypeId() );
        params.put( "serCharacterId", workOrder.getSerCharacterId() );
        params.put( "faultDescription", workOrder.getDescription() );

        workOrderDao.updateWOOnReport( params );// 修改业务表中的数据
        //修改待办列表中的“名称”
        String todoNameString = workOrder.getDescription();
        if(woNameString==null || woNameString.isEmpty()){
            todoNameString = todoNameString.length()<=100?todoNameString:todoNameString.substring( 0, 100 );
        }else{
            todoNameString = woNameString;
        } 
        homepageService.modify( null, workOrder.getWorkOrderCode(), null, todoNameString, null, null, null, null );
        
        // 修改汇报人信息
        HashMap<String, String> parmas1 = new HashMap<String, String>();
        parmas1.put( "woId", woId );
        parmas1.put( "endReportUser", userId );
        workOrderDao.updateOperUserById( parmas1 );
        // 修改当前处理人信息
        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService, woId, userInfoScope, "normal" );

    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        // 修改当前处理人
        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmWorkOrderService, woId, userInfoScope, "rollback" );

    }

}
