package com.timss.workorder.service.sbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.service.AssetInfoService;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.MaintainPlanDao;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.service.WorkOrderService;
import com.timss.workorder.service.WorkOrderServiceDef;
import com.yudean.homepage.bean.HomepageWorkTask;
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
    private MaintainPlanDao maintainPlanDao;
    @Autowired
    private WoUtilService woUtilService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    HomepageService homepageService;
    @Autowired
    @Qualifier("assetInfoServiceImpl")
    AssetInfoService assetInfoService;

    private static Logger logger = Logger.getLogger( WorkOrderServiceImpl.class );

    /**
     * @description:为工单设置通用的属性值（站点，部门，有效标志，修改时间，修改人）
     * @author: 王中华
     * @createDate: 2014-9-4
     * @param workOrder
     * @param userInfoScope
     * @return:
     */
    private WorkOrder setGeneralAttr(WorkOrder workOrder, UserInfoScope userInfoScope) {
        String siteId = userInfoScope.getSiteId();
        String deptId = userInfoScope.getOrgId();
        String userId = userInfoScope.getUserId();
        workOrder.setModifydate( new Date() );
        workOrder.setModifyuser( userId );
        workOrder.setSiteid( siteId );
        workOrder.setDeptid( deptId );
        workOrder.setYxbz( 1 );
        if("".equals( workOrder.getWorkOrderCode() )){
            workOrder.setWorkOrderCode( null );
        }
        return workOrder;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> saveWorkOrder(Map<String, String> WODataMap) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        String woFormData = WODataMap.get( "woFormData" );
        WorkOrder workOrder = JsonHelper.toObject( woFormData, WorkOrder.class );

        int woId = workOrderDao.getNextWOId(); // 获取要插入记录的ID
        workOrder.setId( woId );
        workOrder.setCreatedate( new Date() );
        workOrder.setCreateuser( userId );
        workOrder = setGeneralAttr( workOrder, userInfoScope ); // 通用属性的赋值(修改人，修改时间，站点，部门，有效标识)
        workOrder.setCurrStatus( "draft" ); // 设置为草稿状态
        workOrderDao.insertWorkOrder( workOrder );

        String flowCode = workOrderDao.queryWOById( woId ).getWorkOrderCode();
        // 添加“待办”
        String jumpPath = "workorder/workorder/todolistTOWOPage.do?woId=" + woId;
        // homepageService.createProcess(flowCode, processInstId, "工单", "工单草稿",
        // "草稿", jumpPath, userInfoScope);
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如采购申请 WO20140902001
        String description = workOrder.getDescription();
        int size = (description.length() > 100) ? 100 : description.length();
        homeworkTask.setName( description.substring( 0, size ) ); // 名称

        // homeworkTask.setName("工单草稿");// 名称
        homeworkTask.setProcessInstId( null ); // 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "草稿" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Draft ); // 枚举类型定义是草稿还是流程,Draft
                                                                 // 草稿;Process
                                                                 // 流程实例
        homeworkTask.setTypeName( "工单" ); // 类别
        homeworkTask.setUrl( jumpPath );// 扭转的URL
        homepageService.create( homeworkTask, userInfoScope ); // 调用接口创建草稿

        // 将当前处理人设置为暂存提交人
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put( "woId", String.valueOf( woId ) );
        parmas.put( "currHandlerUser", userId );
        parmas.put( "currHandUserName", itcMvcService.getUserInfoById( userId ).getUserName() );
        workOrderDao.updateCurrHandUserById( parmas );

        // 如果是从维护计划过来的，则需要删除维护计划里面的记录
        Integer mtpId = workOrder.getMaintainPlanId();
        if ( mtpId != null && mtpId != 0 ) {
            maintainPlanDao.updateMTPToUnvailable( workOrder.getMaintainPlanId() );
        }

        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "woId", woId );
        resultHashMap.put( "workOrderCode", flowCode );

        return resultHashMap;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> insertWorkOrder(Map<String, String> WODataMap) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        // String deptId = userInfoScope.getOrgId();
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();

        String woFormData = WODataMap.get( "woFormData" );
        WorkOrder workOrder = JsonHelper.toObject( woFormData, WorkOrder.class );

        int woId = workOrderDao.getNextWOId(); // 获取要插入记录的ID
        workOrder.setId( woId );
        workOrder.setCreatedate( new Date() );
        workOrder.setCreateuser( userId );
        workOrder = setGeneralAttr( workOrder, userInfoScope ); // 通用属性的赋值(修改人，修改时间，站点，部门，有效标识)

        // 启动流程
        String defkey = workflowService.queryForLatestProcessDefKey( "workorder_" + siteId.toLowerCase() + "_wo" );// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "woType", workOrder.getWorkOrderTypeCode() );
        map.put( "businessId", woId );
        logger.info( "-------------CORE 启动流程开始，工单ID：" + woId + "---------------" );
        // 启动流程
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                userInfoScope.getUserId(), map );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();
        logger.info( "-------------CORE 启动流程结束，工单ID：" + woId + "  流程实例ID：" + processInstId + "---------------" );

        workOrder.setWorkflowId( processInstId );
        workOrder.setCurrStatus( "newWo" );
        // workOrder.setCurrStatus("monitorAudit");

        workOrderDao.insertWorkOrder( workOrder );
        // 修改当期处理人信息
        HashMap<String, String> parmas1 = new HashMap<String, String>();
        parmas1.put( "woId", String.valueOf( woId ) );
        parmas1.put( "currHandlerUser", userId );
        parmas1.put( "currHandlerUserName", userName );
        workOrderDao.updateOperUserById( parmas1 );

        String flowCode = workOrderDao.queryWOById( woId ).getWorkOrderCode();
        // 加入待办列表
        String jumpPath = "workorder/workorder/todolistTOWOPage.do?woId=" + woId;
        // homepageService.createProcess(flowCode, processInstId, "工单", "工单审核",
        // "班长审核", jumpPath, userInfoScope);
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
        String description = workOrder.getDescription();
        int size = (description.length() > 100) ? 100 : description.length();
        homeworkTask.setName( description.substring( 0, size ) ); // 名称
        homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "新建" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                   // 草稿;xxxx.Process
                                                                   // 流程实例
        homeworkTask.setTypeName( "工单" ); // 类别
        homeworkTask.setUrl( jumpPath ); // 扭转的URL
        // 加入待办列表
        homepageService.create( homeworkTask, userInfoScope );

        Integer mtpId = workOrder.getMaintainPlanId();
        // 如果是从维护计划过来的，则需要删除维护计划里面的记录
        if ( mtpId != null && mtpId != 0 ) {
            maintainPlanDao.updateMTPToUnvailable( workOrder.getMaintainPlanId() );
        }
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );

        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "taskId", task.getId() );
        resultHashMap.put( "woId", woId );
        resultHashMap.put( "workOrderCode", flowCode );
        resultHashMap.put( "workflowId", processInstId );
        return resultHashMap;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, String> updateWorkOrder(Map<String, String> WODataMap) throws Exception {
        Map<String, String> returnResult = new HashMap<String, String>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String userId = userInfoScope.getUserId();

        String workOrderFormDate = WODataMap.get( "woFormData" ); // 工单基本信息表单数据
        String updateStyle = WODataMap.get( "updateStyle" );

        WorkOrder workOrder = JsonHelper.toObject( workOrderFormDate, WorkOrder.class );
        WorkOrder oldWorkOrder = workOrderDao.queryWOById( workOrder.getId() );

        if ( "commit".equals( updateStyle ) ) { // 提交草稿，启动流程
            // 启动流程
            String defkey = workflowService.queryForLatestProcessDefKey( "workorder_" + siteId.toLowerCase() + "_wo" );// 获取最新流程定义版本
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "woType", workOrder.getWorkOrderTypeCode() );
            map.put( "businessId", workOrder.getId() );

            // 启动新流程
            logger.info( "-------------CORE 启动流程开始，工单编号：" + workOrder.getWorkOrderTypeCode() + "---------------" );
            ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                    userInfoScope.getUserId(), map );
            // 获取流程实例ID
            String processInstId = processInstance.getProcessInstanceId();
            logger.info( "-------------CORE 启动流程结束，工单编号：" + workOrder.getWorkOrderTypeCode() + "  流程实例ID："
                    + processInstId + "---------------" );

            workOrder.setWorkflowId( processInstId );
            workOrder.setCurrStatus( "monitorAudit" );
            workOrder.setModifydate( new Date() );
            workOrder.setModifyuser( userInfoScope.getUserId() );
            workOrderDao.updateWorkOrder( workOrder );

            // 第一次启动流程 添加“待办”(未测试)
            String flowCode = workOrder.getWorkOrderCode();
            int woId = workOrder.getId();
            String jumpPath = "workorder/workorder/todolistTOWOPage.do?woId=" + woId;
            // 构建Bean
            HomepageWorkTask homeworkTask = new HomepageWorkTask();
            homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
            String description = workOrder.getDescription();
            int size = (description.length() > 100) ? 100 : description.length();
            homeworkTask.setName( description.substring( 0, size ) ); // 名称
            homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
            homeworkTask.setStatusName( "班长审核" ); // 状态
            homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                       // 草稿;xxxx.Process
                                                                       // 流程实例
            homeworkTask.setTypeName( "工单" ); // 类别
            homeworkTask.setUrl( jumpPath ); // 扭转的URL
            // 加入待办列表
            homepageService.create( homeworkTask, userInfoScope );

            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( processInstId );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get( 0 );

            returnResult.put( "workflowId", processInstId );
            returnResult.put( "taskId", task.getId() );

        } else if ( "save".equals( updateStyle ) ) { // 再次暂存草稿，不启动流程
            workOrder.setCurrStatus( "draft" ); // 设置为草稿状态
            workOrder.setWorkflowId( oldWorkOrder.getWorkflowId() );
            workOrder.setModifydate( new Date() );
            workOrder.setModifyuser( userInfoScope.getUserId() );
            workOrderDao.updateWorkOrder( workOrder );
            returnResult.put( "workflowId", "noFlow" );
            returnResult.put( "taskId", "noTask" );
        }
        return returnResult;
    }

    @Override
    public Page<WorkOrder> queryAllWO(Page<WorkOrder> page) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        logger.info( "---------------查询  " + siteid + " 工单列表信息----------------------" );
        // 如果有选择左边树节点，则插入查询过滤，如果选中的是根节点，则不过滤
        Map<String, Object> paramsMap = page.getParams();
        Object selectTreeIdObj = paramsMap.get( "selectTreeId" );
        Object rowFilterFlagObj = paramsMap.get( "rowFilterFlag" );
        if ( selectTreeIdObj != null ) {
            String selectTreeId = (String) selectTreeIdObj;
            AssetBean assertRoot = assetInfoService.queryAssetTreeRootBySiteId( siteid );
            if ( selectTreeId.equals( assertRoot.getAssetId() ) ) { // 如果选中的是根节点，则不过滤
                page.setParameter( "selectTreeId", null );
            }
        }

        List<WorkOrder> ret = null;
        if ( rowFilterFlagObj != null ) {
            if ( "1".equals( rowFilterFlagObj.toString() ) ) { // 进行行过滤的查询
                ret = workOrderDao.queryFilterAllWO( page );
            } else { // 没有进行行过滤的查询
                ret = workOrderDao.queryAllWO( page );
            }
        } else {
            ret = workOrderDao.queryAllWO( page );
        }

        page.setResults( ret );
        return page;
    }

    @Override
    public Map<String, Object> queryWOById(int id) {
        WorkOrder workOrder = workOrderDao.queryWOById( id );
        String woStatus = workOrder.getCurrStatus();

        String taskId = "";
        List<String> candidateUsers = new ArrayList<String>();
        if ( woStatus != null && !"draft".equals( woStatus ) && !"woFiling".equals( woStatus )
                && !"woObsolete".equals( woStatus ) ) {
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( workOrder.getWorkflowId() );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get( 0 );

            taskId = task.getId();
            // 获取节点的候选人
            candidateUsers = workflowService.getCandidateUsers( taskId );
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "bean", workOrder );
        map.put( "taskId", taskId );
        map.put( "candidateUsers", candidateUsers );
        return map;
    }

//    @Override
//    public int getNextWOId() {
//        return workOrderDao.getNextWOId();
//    }
//
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void updateWOStatus(Map<String, Object> parmas) {
//        workOrderDao.updateWOStatus( parmas );
//    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void updateWOHandlerStyle(Map<String, Object> handStyleMap) {
//        workOrderDao.updateWOHandlerStyle( handStyleMap );
//
//    }
//
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void updateWOOnPlan(Map<String, Object> parmas) {
//        workOrderDao.updateWOOnPlan( parmas );
//    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void updateWOOnAcceptance(Map<String, Object> parmas) {
//        workOrderDao.updateWOOnAcceptance( parmas );
//    }
//
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void updateWOOnReport(Map<String, Object> parmas) {
//        workOrderDao.updateWOOnReport( parmas );
//    }

//    @Override
//    public Map<String, Object> queryWOBaseInfoByWOCode(String woCode, String siteId) {
//        WorkOrder workOrder = workOrderDao.queryWOBaseInfoByWOCode( woCode, siteId );
//        Map<String, Object> result = new HashMap<String, Object>();
//        if ( workOrder == null ) {
//            result.put( "woExist", false );
//            result.put( "isInPTW", false );
//
//        } else {
//            result.put( "woExist", true );
//            if ( "inPTWing".equals( workOrder.getCurrStatus() ) ) { // 工单的当前状态是“工作票流程中”
//                result.put( "isInPTW", true );
//            } else {
//                result.put( "isInPTW", false );
//            }
//        }
//        result.put( "workOrder", workOrder );
//
//        return result;
//    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void updateWOAddPTWId(int woId, int ptwId) {
//        workOrderDao.updateWOAddPTWId( woId, ptwId );
//    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void inPtwToNextStep(int woId, String userId) {
//        logger.info( "-------------CORE,工作票触发工单走下一步，工单ID：" + woId + "----------------------" );
//        WorkOrder workOrder = workOrderDao.queryWOById( woId );
//        if ( !workOrder.getCurrStatus().equals( "inPTWing" ) ) {
//            return;
//        }
//        String processInstId = workOrder.getWorkflowId();
//        // 获取当前活动节点
//        List<Task> activities = workflowService.getActiveTasks( processInstId );
//        // 刚启动流程，第一个活动节点是属于当前登录人的
//        Task task = activities.get( 0 );
//
//        Map<String, List<String>> userIds = new HashMap<String, List<String>>();
//        List<String> userList = new ArrayList<String>();
//
//        userList.add( userId ); // 节点的审批人
//        // logger.debug("tasKname:--------------"+task.getTaskDefinitionKey());
//
//        List<String> nextTasks = workflowService.getNextTaskDefKeys( processInstId, task.getTaskDefinitionKey() );
//        userIds.put( nextTasks.get( 0 ), userList );
//
//        // 完成任务
//        // 这里第二个参数为执行人，这里必须是一个存在的虚拟人，数据库中需要有这个额人员信息
//        // 第三个参数任务的拥有人，一般情况下就是执行人
//        // 第四个参数为下一步执行人列表，为key-value的形式
//        // 第五个参数为审批信息
//        // 第六个参数可以不用管，填false即可
//        workflowService.complete( task.getId(), userId, userId, userIds, "确认", false );
//
//    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void deleteWorkOrder(int woId) {
//        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
//        WorkOrder workOrder = workOrderDao.queryWOById( woId );
//        if ( workOrder != null ) {
//            workOrderDao.deleteWorkOrder( woId );// 删除工单
//            homepageService.Delete( workOrder.getWorkOrderCode(), userInfoScope ); // 删除首页草稿
//        }
//    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void updateOperUserById(Map<String, String> parmas) {
//        workOrderDao.updateOperUserById( parmas );
//    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void stopWorkOrder(Map<String, Object> parmas) {
//
//        String taskId = (String) parmas.get( "taskId" );
//        String assignee = (String) parmas.get( "assignee" );
//        String owner = (String) parmas.get( "owner" );
//        String message = (String) parmas.get( "message" );
//
//        workOrderDao.updateStopWO( parmas );
//        // 终止流程后需要对流程进行处理
//        workflowService.stopProcess( taskId, assignee, owner, message );
//    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public Map<String, Object> queryItWOById(int woId) {
//        return null;
//    }

//    @Override
//    public void updateWorkflowId(String workflowId, String woId) {
//        workOrderDao.updateWorkflowId( workflowId, woId );
//
//    }

//    @Override
//    public boolean loginUserIsCusSer() {
//        // 电厂不存在客服角色
//        return false;
//    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void cycMtpObjToWo(String mtpId, String todoId) throws Exception {
//        logger.info( "-------------CORE,维护计划生成工单，维护计划ID：" + mtpId + "----------------------" );
//        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
//        String siteId = userInfoScope.getSiteId();
//        String userId = userInfoScope.getUserId();
//
//        WorkOrder workOrder = new WorkOrder();
//        MaintainPlan tempMTP = maintainPlanDao.queryMTPById( Integer.valueOf( mtpId ) );
//        Date newToDoTime = tempMTP.getNewToDoTime(); // 下次生成待办的时间
//
//        // 设置了直接生成工单
//        int woId = workOrderDao.getNextWOId(); // 获取要插入记录的ID
//        workOrder.setId( woId ); // 工单ID
//        workOrder.setMaintainPlanId( tempMTP.getId() );
//        workOrder.setDescription( tempMTP.getDescription() );
//        workOrder.setEquipId( tempMTP.getEquipId() );
//        workOrder.setEquipName( tempMTP.getEquipName() );
//        workOrder.setWoSpecCode( tempMTP.getSpecialtyId() );
//        workOrder.setFaultTypeId( tempMTP.getFaultTypeId() );
//        workOrder.setFaultTypeName( tempMTP.getFaultTypeName() );
//        workOrder.setRemarks( tempMTP.getRemarks() );
//        workOrder.setCreateuser( siteId + "scheduler" );
//        workOrder.setCreatedate( new Date() );
//        workOrder.setSiteid( siteId );
//        workOrder.setDeptid( tempMTP.getDeptid() );
//        workOrder.setYxbz( 1 );
//
//        String principal = tempMTP.getPrincipal(); // 维护计划负责人
//
//        // 启动流程
//        String defkey = workflowService.queryForLatestProcessDefKey( "workorder_" + siteId.toLowerCase() + "_wo" );// 获取最新流程定义版本
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put( "woType", "whWoType" ); // 周期性维护工单
//        map.put( "businessId", woId );
//        // 启动流程
//        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey, siteId
//                + "scheduler", map );
//        // 获取流程实例ID
//        String processInstId = processInstance.getProcessInstanceId();
//
//        workOrder.setWorkflowId( processInstId );
//        workOrder.setCurrStatus( "woPlan" ); // 周期性工单直接进入工作策划阶段
//        workOrderDao.insertWorkOrder( workOrder );
//
//        // 加入待办列表
//        String flowCode = workOrderDao.queryWOById( woId ).getWorkOrderCode();
//        String jumpPath = "workorder/workorder/openWOPlanInfoPage.do?woStatus=woPlan&woId=" + woId;
//
//        // 构建Bean
//        HomepageWorkTask homeworkTask = new HomepageWorkTask();
//        homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
//        String description = workOrder.getDescription();
//        int size = (description.length() > 100) ? 100 : description.length();
//        homeworkTask.setName( description.substring( 0, size ) ); // 名称
//        homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
//        homeworkTask.setStatusName( "工单策划" ); // 状态
//        homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
//                                                                   // 草稿;xxxx.Process
//                                                                   // 流程实例
//        homeworkTask.setTypeName( "工单" ); // 类别
//        homeworkTask.setUrl( jumpPath ); // 扭转的URL
//        // 加入待办列表
//        homepageService.create( homeworkTask, userInfoScope );
//
//        // 获取当前活动节点
//        List<Task> activities = workflowService.getActiveTasks( processInstId );
//        // 刚启动流程，第一个活动节点是属于当前登录人的
//        Task task = activities.get( 0 );
//
//        Map<String, List<String>> userIds = new HashMap<String, List<String>>();
//        List<String> userList = new ArrayList<String>();
//        userList.add( principal ); // 节点的审批人
//
//        List<String> nextTasks = workflowService.getNextTaskDefKeys( processInstId, task.getTaskDefinitionKey() );
//        userIds.put( nextTasks.get( 0 ), userList );
//
//        itcMvcService.setLocalAttribute( "isFromScheduler", "yes" ); // 此处虽然不是定时任务生成工单，但是也需要自动走完提交这一环节，
//                                                                     // 而且没有下一步的审批人可选，所有借用“isFromScheduler”这个参数
//        // 完成任务
//        // 这里第二个参数为执行人，这里必须是一个存在的虚拟人，数据库中需要有这个额人员信息
//        // 第三个参数任务的拥有人，一般情况下就是执行人
//        // 第四个参数为下一步执行人列表，为key-value的形式
//        // 第五个参数为审批信息
//        // 第六个参数可以不用管，填false即可
//        workflowService.complete( task.getId(), userId, userId, userIds, "同意", false );
//
//        // 修改当前执行人和执行人名
//        Map<String, String> parmas = new HashMap<String, String>();
//        parmas.put( "woId", String.valueOf( woId ) );
//        parmas.put( "currHandlerUser", principal );
//        String principalName = itcMvcService.getUserInfoById( principal ).getUserName();
//        parmas.put( "currHandUserName", principalName );
//        workOrderDao.updateCurrHandUserById( parmas );
//
//        // 修改维护计划的下次生成待办时间,和当前开始时间
//        if ( newToDoTime == null ) {
//            newToDoTime = new Date();
//        }
//        // logger.debug("本周期开始时间:--------------"+newToDoTime);
//        // logger.debug("当前时间long:--------------"+newToDoTime.getTime());
//        // logger.debug("周期（天）:--------------aaa"+tempMTP.getMaintainPlanCycle());
//        // logger.debug("周期（天）long:--------------aaa"+(long)tempMTP.getMaintainPlanCycle()*24*60*60*1000);
//
//        long newToDoTimeLong = newToDoTime.getTime() + (long) tempMTP.getMaintainPlanCycle() * 24 * 60 * 60 * 1000;
//        newToDoTime = new Date( newToDoTimeLong );
//
//        // logger.debug("下周期时间long:--------------"+newToDoTimeLong);
//        // logger.debug("新生产待办时间:--------------"+newToDoTime);
//
//        maintainPlanDao.updateMTPTodoTime( new Date(), newToDoTime, tempMTP.getId() );
//
//        maintainPlanDao.updateMTPhasAlertTodo( tempMTP.getId(), 0 ); // 1代表已经生成提醒待办，0待办未生成提醒待办
//
//        // 删掉提醒待办
//        if ( (!"null".equals( todoId )) && (todoId != null) ) {
//            homepageService.Delete( todoId, userInfoScope );
//        }
//    }

    // @Override
    // @Transactional(rollbackFor = { Exception.class},propagation =
    // Propagation.REQUIRED)
    // public Map<String,String> cancelCommitWO(String woId) {
    // logger.info("-------------SBS工单取消提交，工单ID："+woId+"----------------------");
    // // UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    // // WorkOrder workOrder =
    // workOrderDao.queryItWOById(Integer.valueOf(woId));
    //
    // // //删掉启动流程的待办
    // // homepageService.Delete(workOrder.getWorkflowId(),userInfoScope);
    // // //加入草稿待办
    // // String flowCode = workOrder.getWorkOrderCode();
    // // String jumpPath = "workorder/workorder/todolistTOWOPage.do?woId=" +
    // woId ;
    // // HomepageWorkTask homeworkTask = new HomepageWorkTask();
    // // homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
    // // homeworkTask.setName("工单草稿");// 名称
    // // homeworkTask.setProcessInstId(null); // 草稿时流程实例ID可以不用设置
    // // homeworkTask.setStatusName("草稿"); // 状态
    // // homeworkTask.setType(HomepageWorkTask.TaskType.Draft); //
    // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
    // // homeworkTask.setTypeName("工单"); // 类别
    // // homeworkTask.setUrl(jumpPath);// 扭转的URL
    // // homepageService.create(homeworkTask, userInfoScope); // 调用接口创建草稿
    // //
    // // //删掉流程
    // // workflowService.delete(workOrder.getWorkflowId(), "提交取消");
    // // //清空工单中流程ID的信息
    // // workOrderDao.updateWorkflowId("",woId);
    // //修改工单状态
    // // String woStatus = "draft";
    // String woStatus = "newWo";
    // woUtilService.updateWoStatus(woId, woStatus);
    //
    // Map<String,String> mav = new HashMap<String,String>();
    // mav.put("result", "success");
    // return mav;
    // }

    // @Override
    // @Transactional(rollbackFor = { Exception.class},propagation =
    // Propagation.REQUIRED)
    // public Map<String, String> saveWOOnPlan() throws Exception{
    // UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    // String userId = userInfoScope.getUserId();
    // String woFormDataStr =
    // userInfoScope.getParam("workOrderForm");//获取前台传过来的form表单数据
    // WorkOrder workOrder = JsonHelper.toObject(woFormDataStr,
    // WorkOrder.class);
    // String isToPTW = userInfoScope.getParam("isToPTW");
    // String jobPlanId = userInfoScope.getParam("jobPlanId");
    // String preHazardDataStr = userInfoScope.getParam("preHazardData");
    // String toolDataStr = userInfoScope.getParam("toolData");
    // String taskDataStr = userInfoScope.getParam("taskData");
    // String workerDataStr = userInfoScope.getParam("workerData");
    //
    // //汇报表单的提取
    // String woReportFormDataStr =
    // userInfoScope.getParam("woReportForm");//获取前台传过来的form表单数据
    // JSONObject woReportFormJsonObj=
    // JSONObject.fromObject(woReportFormDataStr);
    //
    // //插入工单策划内容
    // Map<String,String> addJPDataMap = new HashMap<String, String>();
    // addJPDataMap.put("woFormData", woFormDataStr);
    // addJPDataMap.put("preHazardDataStr", preHazardDataStr);
    // addJPDataMap.put("toolDataStr", toolDataStr);
    // addJPDataMap.put("taskDataStr", taskDataStr);
    // addJPDataMap.put("workerDataStr", workerDataStr);
    // addJPDataMap.put("commitStyle", "save");
    // addJPDataMap.put("jobPlanId", jobPlanId);
    // addJPDataMap.put("userId", userId);
    //
    // String woCurrStatus = workOrder.getCurrStatus();
    // if("woPlan".equals(woCurrStatus)){
    // addJPDataMap.put("jpSource", "plan");
    // //作业方案的来源：standard:标准作业方案；maintainPlan:维护计划；
    // //plan：工单策划；actual:实际
    // }else if("endWOReport".equals(woCurrStatus)){
    // addJPDataMap.put("jpSource", "actual");
    // }
    //
    // int jpId = Integer.valueOf(jobPlanId);
    // try {
    // if(jpId == 0){
    // jpId = jobPlanService.insertJobPlan(addJPDataMap);
    // }else{
    // jpId = jobPlanService.updateJobPlan(addJPDataMap);
    // }
    // } catch (Exception e) {
    // logger.error(e.getMessage());
    // }
    //
    // //修改工单信息
    // Map<String, Object> parmas = new HashMap<String, Object>();
    // parmas.put("id", workOrder.getId());
    // if(jpId != 0){
    // parmas.put("jobPlanId", jpId); //添加作业方案的ID（最新的）
    // }
    //
    // parmas.put("modifyuser", userId);
    // parmas.put("modifydate", new Date());
    //
    // if("woPlan".equals(woCurrStatus)){
    // parmas.put("isToPTW", isToPTW); //是否走工作票
    // //TODO 此处需要获得走工作票时的工作票ID
    // parmas.put("ptwId", 0); //关联工作票的ID
    //
    // }else if("endWOReport".equals(woCurrStatus)){
    //
    // Date beginTime = null;
    // Date endTime = null;
    // if(woReportFormJsonObj.get("beginTime")!=null){
    // long begin =(Long) woReportFormJsonObj.get("beginTime");
    // beginTime = new Date(begin);
    // }
    // if(woReportFormJsonObj.get("endTime")!=null){
    // long end =(Long) woReportFormJsonObj.get("endTime");
    // endTime = new Date(end);
    // }
    // parmas.put("beginTime",beginTime); //添加作业方案的ID（最新的）
    // parmas.put("endTime", endTime);
    //
    // parmas.put("isHasRemainFault", "no_remainFault"); //暂存时，不暂存遗留问题记录
    // parmas.put("endReport", woReportFormJsonObj.get("endReport"));
    // // parmas.put("remainFaultId", 0);
    // }
    //
    // //------------------汇报-----------------------
    // if("woPlan".equals(woCurrStatus)){
    // workOrderDao.updateWOOnPlan(parmas);//修改业务表中的数据
    // }else if("endWOReport".equals(woCurrStatus)){
    // workOrderDao.updateWOOnReport(parmas);//修改业务表中的数据
    // }
    //
    // Map<String,String> mav = new HashMap<String,String>();
    // mav.put("result", "success");
    // return mav;
    // }

    // @Override
    // public void deleteWorkOrderByWoCode(String woCode, String siteid) {
    // workOrderDao.deleteWorkOrderByWoCode(woCode,siteid);
    // }
    // ---------------作废工单-----------------------------
    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void obsoleteWorkOrder(String woId) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        WorkOrder workOrder = workOrderDao.queryItWOById( Integer.valueOf( woId ) );
        String workflowId = workOrder.getWorkflowId();
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( workflowId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        String taskId = task.getId();

        logger.info( "-------------作废工单处理开始,工单ID：" + woId );
        // 终止流程
        workflowService.stopProcess( taskId, userId, userId, "作废" );
        // 修改工单状态
        woUtilService.updateWoStatus( woId, "woObsolete" );
        // 删掉对应的待办
        homepageService.complete( workOrder.getWorkflowId(), userInfoScope, "已作废" );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void rollbackCommitWo(Map<String, String> addWODataMap) throws Exception {
        String woFormData = addWODataMap.get( "woFormData" );
        WorkOrder workOrder = JsonHelper.toObject( woFormData, WorkOrder.class );
        workOrderDao.updateWorkOrder( workOrder );
    }

    @Override
    public Map<String, String> cancelCommitWO(String woId) {
        return workOrderServiceDef.cancelCommitWO( woId );
    }

    @Override
    public void cycMtpObjToWo(String mtpId, String todoId) throws Exception {
        workOrderServiceDef.cycMtpObjToWo( mtpId, todoId );
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
    public void inPtwToNextStep(int woId, String userId) {
        workOrderServiceDef.inPtwToNextStep( woId, userId );
    }

    @Override
    public boolean loginUserIsCusSer() {
        return false;
    }

    @Override
    public Page<WorkOrder> queryAllRelateWoOfQx(Page<WorkOrder> page) {
        return workOrderServiceDef.queryAllRelateWoOfQx( page );
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
