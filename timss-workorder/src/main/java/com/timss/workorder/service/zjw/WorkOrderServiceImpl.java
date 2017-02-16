package com.timss.workorder.service.zjw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.service.AssetInfoService;
import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.vo.InvMatApplyToWorkOrder;
import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.service.PtwInfoService;
import com.timss.workorder.bean.JPItems;
import com.timss.workorder.bean.WoPriority;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoPriorityService;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.service.WorkOrderService;
import com.timss.workorder.service.WorkOrderServiceDef;
import com.timss.workorder.util.InvItemVOtoJPItemsUtil;
import com.timss.workorder.util.WoProcessStatusUtilZJW;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.MvcJsonUtil;
import com.yudean.workflow.service.WorkflowService;

public class WorkOrderServiceImpl implements WorkOrderService {
    @Autowired
    private WorkOrderServiceDef workOrderServiceDef;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkOrderDao workOrderDao;
    @Autowired
    private WoUtilService woUtilService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    HomepageService homepageService;
    @Autowired
    @Qualifier("assetInfoServiceImpl")
    AssetInfoService assetInfoService;
    @Autowired
    @Qualifier("WoPriorityServiceImpl")
    private WoPriorityService woPriorityService;
    @Autowired
    @Qualifier("ptwInfoServiceImpl")
    private PtwInfoService ptwInfoService;
    @Autowired
    private InvMatApplyService invMatApplyService;
    private static Logger logger = Logger.getLogger( WorkOrderServiceImpl.class );

    @Override
    public Map<String, Object> saveWorkOrder(Map<String, String> addWODataMap) throws Exception {
        return workOrderServiceDef.saveWorkOrder( addWODataMap );
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> insertWorkOrder(Map<String, String> WODataMap) throws Exception {
        return workOrderServiceDef.insertWorkOrder( WODataMap );
    }

    @Override
    public Map<String, String> updateWorkOrder(Map<String, String> addWODataMap) throws Exception {
        return workOrderServiceDef.updateWorkOrder( addWODataMap );
    }
    
    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void rollbackCommitWo(Map<String, String> addWODataMap) throws Exception {
        String woFormData = addWODataMap.get( "woFormData" );
        WorkOrder workOrder = JsonHelper.toObject( woFormData, WorkOrder.class );
        workOrderDao.updateWorkOrder( workOrder );
        String workflowId = workOrder.getWorkflowId();
        // 设置变量
        if ( workOrder.getWoCommitHandleStyle().equals( "SHIELD_CODE" ) ) {
            workflowService.setVariable( workflowId, "commitHandleStyle", "SHIELD_CODE" );
        } else if ( workOrder.getWoCommitHandleStyle().equals( "MAINTAIN" ) ) {
            workflowService.setVariable( workflowId, "commitHandleStyle", "MAINTAIN" );
        } else if ( workOrder.getWoCommitHandleStyle().equals( "REMOTE_RESET" ) ) {
            workflowService.setVariable( workflowId, "commitHandleStyle", "REMOTE_RESET" );

            // 状态为已完成
            String woStatus = WoProcessStatusUtilZJW.DONE;
            woUtilService.updateWoStatus( String.valueOf( workOrder.getId() ), woStatus );

            // 流程结束
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            String userId = userInfoScope.getUserId();
            String processInstId = workOrder.getWorkflowId();
            List<Task> activities = workflowService.getActiveTasks( processInstId );
            Task task = activities.get( 0 );
            workflowService.complete( task.getId(), userId, userId, null, "远程复位", false );
        } else {
            workflowService.setVariable( workflowId, "commitHandleStyle", "REPAIR_NOW" );
        }
    }

    @Override
    public Map<String, Object> queryWOById(int id) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String currentUser = userInfoScope.getUserId();
        WorkOrder workOrder = workOrderDao.queryWOById( id );
        String taskId = "";
        List<String> candidateUsers = new ArrayList<String>();
        if ( StringUtils.isNotBlank( workOrder.getWorkflowId() ) ) {
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( workOrder.getWorkflowId() );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            if ( activities.size() != 0 ) {
                Task task = activities.get( 0 );
                taskId = task.getId();
                // 获取节点的候选人
                candidateUsers = workflowService.getCandidateUsers( taskId );
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "bean", workOrder );
        map.put( "taskId", taskId );
        map.put( "candidateUsers", candidateUsers );

        // 是否为高优先级
        Map<String, Object> priority = null;
        Integer priorityId = workOrder.getPriorityId();
        if ( workOrder.getPriorityId() != null ) {
            priority = woPriorityService.queryWoPriorityById( priorityId, userInfoScope.getSiteId() );
            WoPriority p = (WoPriority) priority.get( "baseData" );
            if ( p.getSolveLength() == 0 ) {
                map.put( "isHighLevel", "Y" );
            } else {
                map.put( "isHighLevel", "N" );
            }
        }

        try {
            // 查询已领物资
            List<JPItems> jpPlanItemsList = new ArrayList<JPItems>();
            Page<InvMatApplyToWorkOrder> invItemVOList;
            invItemVOList = invMatApplyService.queryMatApplyByWoId( String.valueOf( id ), "wo_picking" );
            List<InvMatApplyToWorkOrder> itemsList = invItemVOList.getResults();
            jpPlanItemsList = InvItemVOtoJPItemsUtil.invItemVOtoJPItem( itemsList );

            JSONArray jpItemsListStr = new JSONArray();
            if ( jpPlanItemsList.size() > 0 ) { // 有申请领取的物资
                jpItemsListStr = MvcJsonUtil.JSONArrayFromList( jpPlanItemsList );
                map.put( "toolData", jpItemsListStr.toString() );
            }

            // 关联领料单
            List<InvMatApply> matApplyList = invMatApplyService.queryMatApplyByOutterId( String.valueOf( id ),
                    "wo_picking" );
            String relMatApplyIds = "";
            if ( matApplyList != null && matApplyList.size() != 0 ) {
                for ( InvMatApply invMatApply : matApplyList ) {
                    relMatApplyIds += invMatApply.getSheetno() + "," + invMatApply.getImaid() + ";";
                }
                if ( relMatApplyIds.length() != 0 ) {
                    relMatApplyIds = relMatApplyIds.substring( 0, relMatApplyIds.length() - 1 );
                }
                map.put( "relMatApplyIds", relMatApplyIds );
            }

            // 关联工作票
            List<PtwInfo> ptwList = ptwInfoService.queryPtwInfoListByWoId( String.valueOf( id ),
                    userInfoScope.getSiteId() );
            String relPtwIds = "";
            boolean hasValidPtw = false;// 只要有未终结或未作废的工作票，新建工作票按钮就隐藏 700已终结 800已作废
            if ( ptwList != null && ptwList.size() != 0 ) {
                for ( PtwInfo ptwInfo : ptwList ) {
                    relPtwIds += ptwInfo.getWtNo() + "," + ptwInfo.getId() + ";";
                    if ( !hasValidPtw && ptwInfo.getWtStatus() != 700 && ptwInfo.getWtStatus() != 800 ) {
                        hasValidPtw = true;
                    }
                }
                if ( relPtwIds.length() != 0 ) {
                    relPtwIds = relPtwIds.substring( 0, relPtwIds.length() - 1 );
                }
                map.put( "relPtwIds", relPtwIds );
                map.put( "hasValidPtw", hasValidPtw );
            }
            // map.put( "relPtwIds", "ptw20150101,0001;ptw20150102,0002;" );

        } catch (Exception e) {
            logger.error( e.getMessage() );
        }

        // 判断是否具有审批状态
        String approveFlag = null;
        if ( candidateUsers.contains( currentUser ) ) {
            approveFlag = "approver";
        } else {
            approveFlag = "others";
        }
        map.put( "approveFlag", approveFlag );
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void inPtwToNextStep(int woId, String userId) {
        logger.info( "-------------CORE,工作票触发工单走下一步，工单ID：" + woId + "----------------------" );
        WorkOrder workOrder = workOrderDao.queryWOById( woId );
        if ( !workOrder.getCurrStatus().equals( WoProcessStatusUtilZJW.WORK_TICKET_PROCEDURE ) ) {
            return;
        }
        String processInstId = workOrder.getWorkflowId();
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        // 刚启动流程，第一个活动节点是属于当前登录人的
        Task task = activities.get( 0 );

        Map<String, List<String>> userIds = new HashMap<String, List<String>>();

        List<String> userList = new ArrayList<String>();
        userList.add( workOrder.getEndReportUser() ); // 节点的审批人

        List<String> nextTasks = workflowService.getNextTaskDefKeys( processInstId, task.getTaskDefinitionKey() );
        userIds.put( nextTasks.get( 0 ), userList );

        // 完成任务
        // 第一个参数为任务ID
        // 第二个参数为执行人，这里必须是一个存在的虚拟人，数据库中需要有这个额人员信息
        // 第三个参数任务的拥有人，一般情况下就是执行人
        // 第四个参数为下一步执行人列表，为key-value的形式
        // 第五个参数为审批信息
        // 第六个参数可以不用管，填false即可
        workflowService.complete( task.getId(), null, null, userIds, "工作票流程结束", false );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void cycMtpObjToWo(String mtpId, String todoId) throws Exception {
        workOrderServiceDef.cycMtpObjToWo(mtpId, todoId);
    }

    @Override
    public Map<String, String> cancelCommitWO(String woId) {
        return workOrderServiceDef.cancelCommitWO( woId );
    }

    @Override
    public void deleteWorkOrder(int woId) {
        workOrderServiceDef.deleteWorkOrder( woId );
    }

    @Override
    public void deleteWorkOrderByWoCode(String woCode, String siteid) {
        workOrderServiceDef.deleteWorkOrderByWoCode( woCode, siteid );
    }

    @Override
    public int getNextWOId() {
        return workOrderServiceDef.getNextWOId();
    }

    @Override
    public int getUserWoSum(String userId, String siteId) {
        return workOrderServiceDef.getUserWoSum( userId, siteId );
    }

    @Override
    public boolean loginUserIsCusSer() {
        return false;
    }

    @Override
    public void obsoleteWorkOrder(String woIdString) {
        workOrderServiceDef.obsoleteWorkOrder( woIdString );
    }


    @Override
    public Page<WorkOrder> queryAllRelateWoOfQx(Page<WorkOrder> page) {
        return workOrderServiceDef.queryAllRelateWoOfQx( page );
    }

    @Override
    public Page<WorkOrder> queryAllWO(Page<WorkOrder> page) throws Exception {
        return workOrderServiceDef.queryAllWO( page );
    }

    @Override
    public Map<String, Object> queryItWOById(int woId) {
        return workOrderServiceDef.queryItWOById( woId );
    }

    @Override
    public Map<String, Object> queryWOBaseInfoByWOCode(String woCode, String siteId) {
        return workOrderServiceDef.queryWOBaseInfoByWOCode( woCode, siteId );
    }

    @Override
    public Map<String, String> saveWOOnPlan() throws Exception {
        return workOrderServiceDef.saveWOOnPlan();
    }

    @Override
    public void stopWorkOrder(Map<String, Object> parmas) {
        workOrderServiceDef.stopWorkOrder( parmas );
    }


    @Override
    public void updateOperUserById(Map<String, String> parmas) {
        workOrderServiceDef.updateOperUserById( parmas );
    }

    @Override
    public void updateWOAddPTWId(int woId, int ptwId) {
        workOrderServiceDef.updateWOAddPTWId( woId, ptwId );
    }

    @Override
    public void updateWOHandlerStyle(Map<String, Object> handStyleMap) {
        workOrderServiceDef.updateWOHandlerStyle( handStyleMap );
    }

    @Override
    public void updateWOOnAcceptance(Map<String, Object> parmas) {
        workOrderServiceDef.updateWOOnAcceptance( parmas );
    }

    @Override
    public void updateWOOnPlan(Map<String, Object> parmas) {
        workOrderServiceDef.updateWOOnPlan( parmas );
    }

    @Override
    public void updateWOOnReport(Map<String, Object> parmas) {
        workOrderServiceDef.updateWOOnReport( parmas );
    }

    @Override
    public void updateWOStatus(Map<String, Object> parmas) {
        workOrderServiceDef.updateWOStatus( parmas );
    }

    @Override
    public Map<String, String> updateWoBaseInfo() throws Exception {
        return workOrderServiceDef.updateWoBaseInfo();
    }

    @Override
    public void updateWorkflowId(String workflowId, String woId) {
        workOrderServiceDef.updateWorkflowId( workflowId, woId );
    }

    @Override
    public Map<String, String> wobackToSomeStep(String woId, String woStepFlag) throws Exception {
        return workOrderServiceDef.wobackToSomeStep( woId, woStepFlag );
    }

    @Override
    public List<WorkOrder> queryAllDelayWoNoSiteId() {
        return workOrderServiceDef.queryAllDelayWoNoSiteId(null);
    }

    @Override
    public String getDelayRestartNextTaskKey() {
        return null;
    }

    @Override
    public void updateCurrHandUserById(Map<String, String> parmas) {
        workOrderDao.updateCurrHandUserById( parmas );
    }
}
