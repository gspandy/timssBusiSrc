package com.timss.workorder.service.swf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.service.AssetInfoService;
import com.timss.workorder.bean.WoPriority;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoPriorityService;
import com.timss.workorder.service.WorkOrderService;
import com.timss.workorder.service.WorkOrderServiceDef;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

public class WorkOrderServiceImpl implements WorkOrderService{
    @Autowired
    private WorkOrderServiceDef workOrderServiceDef;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkOrderDao workOrderDao;
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
    private static Logger logger = Logger.getLogger( WorkOrderServiceImpl.class );

    @Override
    public Map<String, Object> saveWorkOrder(Map<String, String> addWODataMap) throws Exception {
        return workOrderServiceDef.saveWorkOrder( addWODataMap );
    }

    @Override
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
        if ( workOrder.getWorkOrderTypeCode().equals( "DEFECT" ) ) {
            workflowService.setVariable( workflowId, "woType", "DEFECT" );
        } else {
            workflowService.setVariable( workflowId, "woType", "OTHER" );
            workflowService.setVariable( workflowId, "isDefect", "Y" );
            workflowService.setVariable( workflowId, "woSpecCode", workOrder.getWoSpecCode() );// WoSelectExpertBySpec用到
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
        Map<String, String> queryParams = new HashMap<String, String>();
        //查询条件WO_DELAY_LEN不为空，WO_IS_DELAY值为Y,CURR_STATUS的值为值长启动工单（DELAY_DUTY_RESTART）
        queryParams.put( "woIsDelay", "Y" );  //是延时工单
        queryParams.put( "currStatus", "DELAY_DUTY_RESTART" );  //状态正处于“值长启动工单”
        queryParams.put( "woDelayLen", "Y" ); //有延时长度值
        queryParams.put( "siteid", "SWF" );
        return workOrderServiceDef.queryAllDelayWoNoSiteId(queryParams);
    }

    @Override
    public String getDelayRestartNextTaskKey() {
        return "supervisor_report"; //下一节点的taskKey
    }

    @Override
    public void updateCurrHandUserById(Map<String, String> parmas) {
        workOrderDao.updateCurrHandUserById( parmas );
    }
}
