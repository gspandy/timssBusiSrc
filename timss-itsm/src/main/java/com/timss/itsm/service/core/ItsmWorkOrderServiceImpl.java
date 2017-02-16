package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmMaintainPlan;
import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.dao.ItsmMaintainPlanDao;
import com.timss.itsm.dao.ItsmWoFaultTypeDao;
import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmJobPlanService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.timss.itsm.util.ItsmConstant;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.bean.ProcessFucExtParam;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecProcRoute.VisibleType;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.process.ProcessDefInfo;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;

@Service
public class ItsmWorkOrderServiceImpl implements ItsmWorkOrderService {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmWorkOrderDao workOrderDao;
    @Autowired
    ItsmWoFaultTypeDao woFaultTypeDao;
    @Autowired
    private ItsmMaintainPlanDao maintainPlanDao;
    @Autowired
    // @Qualifier("JobPlanServiceImpl")
    private ItsmJobPlanService jobPlanService;
    @Autowired
    private ItsmWoUtilService woUtilService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private HomepageService homepageService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    SelectUserService selectUserService;
    @Autowired
    private IAuthorizationManager iAuthorizationManager;

    private static final Logger LOG = Logger.getLogger( ItsmWorkOrderServiceImpl.class );

    /**
     * @description:为工单设置通用的属性值（站点，部门，有效标志，修改时间，修改人）
     * @author: 王中华
     * @createDate: 2014-9-4
     * @param workOrder
     * @param userInfoScope
     * @return:
     */
    private ItsmWorkOrder setGeneralAttr(ItsmWorkOrder workOrder, UserInfoScope userInfoScope) {
        String siteId = userInfoScope.getSiteId();
        String deptId = userInfoScope.getOrgId();
        String userId = userInfoScope.getUserId();
        workOrder.setModifydate( new Date() );
        workOrder.setModifyuser( userId );
        workOrder.setSiteid( siteId );
        workOrder.setDeptid( deptId );
        workOrder.setYxbz( 1 );
        return workOrder;
    }

    @Override
    public Map<String, Object> saveInitWorkOrder(Map<String, String> wODataMap) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        String woFormData = wODataMap.get( "woFormData" );
        String uploadIds = wODataMap.get( "uploadIds" );
        ItsmWorkOrder workOrder = JsonHelper.fromJsonStringToBean( woFormData, ItsmWorkOrder.class );
        String desString = workOrder.getDescription();
        workOrder.setDescription( desString );

//        int woId = workOrderDao.getNextWOId(); // 获取要插入记录的ID
//        workOrder.setId( woId );
        workOrder.setCreatedate( new Date() );
        workOrder.setCreateuser( userId );
        // 设置报障时间，便于统计中计算响应时间
        workOrder.setDiscoverTime( new Date() );

        workOrder = setGeneralAttr( workOrder, userInfoScope ); // 通用属性的赋值
        workOrder.setCurrStatus( "draft" ); // 设置为草稿状态
        workOrder.setWorkOrderTypeCode( "qxWoType" );
        workOrderDao.insertWorkOrder( workOrder );
        String woId = workOrder.getId();
        
        String flowCode = workOrderDao.queryWOById( woId ).getWorkOrderCode();

        String jumpPath = "itsm/workorder/todolistTOWOPage.do?woId=" + woId;
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如采购申请 WO20140902001
        String woName=workOrder.getWoName();
        if(woName == null || woName.length() == 0){
            String description = workOrder.getDescription();
            if ( description == null || description.length() == 0 ) {
                description = workOrder.getWorkOrderCode() + "审批";
            }
            int size = (description.length() > 100) ? 100 : description.length();
            homeworkTask.setName( description.substring( 0, size ) ); // 名称
        }else{
            homeworkTask.setName( woName ); // 名称
        }
        

        // homeworkTask.setName("工单草稿");// 名称
        homeworkTask.setProcessInstId( null ); // 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "草稿" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Draft ); // 枚举类型定义是草稿还是流程,Draft
                                                                 // 草稿;Process
                                                                 // 流程实例
        homeworkTask.setTypeName( "IT工单" ); // 类别
        homeworkTask.setUrl( jumpPath );// 扭转的URL
        homepageService.create( homeworkTask, userInfoScope, null ); // 调用接口创建草稿

        // 将当前处理人设置为暂存提交人
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put( "woId", String.valueOf( woId ) );
        parmas.put( "currHandlerUser", userId );
        parmas.put( "currHandUserName", itcMvcService.getUserInfoById( userId ).getUserName() );
        workOrderDao.updateCurrHandUserById( parmas );

        // 先删掉所有相关的附件数据
        woUtilService.deleteAttachMatch( woId, null, "WO" );
        // 插入附件的相关数据
        woUtilService.insertAttachMatch( woId, uploadIds, "WO", "woDraft" );

        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "woId", woId );
        resultHashMap.put( "workOrderCode", flowCode );

        return resultHashMap;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, Object> saveWorkOrder(Map<String, String> wODataMap) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        String woFormData = wODataMap.get( "woFormData" );
        String uploadIds = wODataMap.get( "uploadIds" );
        ItsmWorkOrder workOrder = JsonHelper.fromJsonStringToBean( woFormData, ItsmWorkOrder.class );
        String desString = workOrder.getDescription();
        workOrder.setDescription( desString );
        String woType = workOrder.getWorkOrderTypeCode();

        workOrder.setCreatedate( new Date() );
        workOrder.setCreateuser( userId );
        // 设置报障时间，便于统计中计算响应时间
        if ( "qxWoType".equals( woType ) ) {
            workOrder.setDiscoverTime( workOrder.getCreatedate() );
        }

        workOrder = setGeneralAttr( workOrder, userInfoScope ); // 通用属性的赋值
        workOrder.setCurrStatus( "draft" ); // 设置为草稿状态
        workOrderDao.insertWorkOrder( workOrder );
        String flowCode = workOrder.getWorkOrderCode();
        String woId = workOrder.getId();
        String jumpPath = "itsm/workorder/todolistTOWOPage.do?woId=" + woId;
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如采购申请 WO20140902001
        String description = workOrder.getDescription();
        if ( description == null || description.length() == 0 ) {
            description = workOrder.getWorkOrderCode() + "审批";
        }
        int size = (description.length() > 100) ? 100 : description.length();
        homeworkTask.setName( description.substring( 0, size ) ); // 名称

        // homeworkTask.setName("工单草稿");// 名称
        homeworkTask.setProcessInstId( null ); // 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "草稿" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Draft ); // 枚举类型定义是草稿还是流程,Draft
                                                                 // 草稿;Process
                                                                 // 流程实例
        homeworkTask.setTypeName( "IT工单" ); // 类别
        homeworkTask.setUrl( jumpPath );// 扭转的URL
        homepageService.create( homeworkTask, userInfoScope, null ); // 调用接口创建草稿

        // 将当前处理人设置为暂存提交人
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put( "woId", String.valueOf( woId ) );
        parmas.put( "currHandlerUser", userId );
        parmas.put( "currHandUserName", itcMvcService.getUserInfoById( userId ).getUserName() );
        workOrderDao.updateCurrHandUserById( parmas );

        // 先删掉所有相关的附件数据
        woUtilService.deleteAttachMatch( woId, null, "WO" );
        // 插入附件的相关数据
        woUtilService.insertAttachMatch( woId, uploadIds, "WO", "woDraft" );

        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "woId", woId );
        resultHashMap.put( "workOrderCode", flowCode );

        return resultHashMap;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, Object> insertWorkOrder(Map<String, String> wODataMap) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        String woFormData = wODataMap.get( "woFormData" );
        
        String uploadIds = wODataMap.get( "uploadIds" );
        String itcWhbWo = wODataMap.get( "itcWhbWo" ); // 判断是否是专业报障用户（工程师）
        String needInfoCenterAudit = wODataMap.get( "needInfoCenterAudit" );  //是否需要信息中心审批
        boolean itcWhbWoFlag = Boolean.parseBoolean( itcWhbWo );
        boolean needInfoCenterAuditFlag = "yes".equals( needInfoCenterAudit )?true:false;
        
        String comNextAuditFlag = "";
        if(itcWhbWoFlag&&needInfoCenterAuditFlag){ //如果是维护部建单且需要信息中心审批，则必要要经过部门再审批,且不能立即派单
            comNextAuditFlag = "own";
        }
        ItsmWorkOrder workOrder = JsonHelper.toObject( woFormData, ItsmWorkOrder.class );
        if("".equals( workOrder.getId() )){
            workOrder.setId( null );
        }
        if("".equals( workOrder.getWorkOrderCode() )){
            workOrder.setWorkOrderCode( null );
        }
        // 更新客户的位置信息
        String customerLoc = workOrder.getCustomerLocation().trim();
        String customerCode = workOrder.getCustomerCode().trim();
        if ( customerLoc.length() > 0 && customerCode.length() > 0 ) {
            woUtilService.updateCustomerLocInfo( workOrder.getCustomerCode(), customerLoc );
        }

        String desString = workOrder.getDescription();
        workOrder.setDescription( desString );
        String woType = workOrder.getWorkOrderTypeCode();

        // 设置报障时间，便于统计中计算响应时间
        if ( "qxWoType".equals( woType ) || "rwxWoType".equals( woType ) ) {
            workOrder.setDiscoverTime( new Date() );
        }

        workOrder.setCreatedate( new Date() );
        workOrder.setCreateuser( userId );
        workOrder = setGeneralAttr( workOrder, userInfoScope ); // 通用属性的赋值(修改人，修改时间，站点，部门，有效标识)
        workOrderDao.insertWorkOrder( workOrder );
        String woId = workOrder.getId();
        // 启动流程
        String defkey = workflowService.queryForLatestProcessDefKey( ItsmConstant.WORKFLOWKEY );// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "woType", woType );
        map.put( "businessId", woId );
        map.put( "itcWhbWo", itcWhbWoFlag );
        map.put( "needInfoCenterAudit",needInfoCenterAuditFlag);
        map.put( "comNextAudit",comNextAuditFlag);
        
        LOG.info( "-------------ITC工单ID：" + woId + "启动流程----------------------" );
        // 启动流程
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                userInfoScope.getUserId(), map );

        LOG.info( "-------------ITC工单ID：" + woId + "流程启动结束----------------------" );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();

        // 修改业务表中的信息
        workOrder.setWorkflowId( processInstId );
        workOrderDao.updateWorkflowId( processInstId, woId );
        

        // 加入待办列表
        String flowCode = workOrder.getWorkOrderCode();
        String jumpPath = "itsm/workorder/todolistTOWOPage.do?woId=" + woId;
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
        String description = workOrder.getDescription();
        if ( description == null || description.length() == 0 ) {
            description = workOrder.getWorkOrderCode() + "审批";
        }
        int size = (description.length() > 100) ? 100 : description.length();
        homeworkTask.setName( description.substring( 0, size ) ); // 名称

        homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "客服派单" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                   // 草稿;xxxx.Process
                                                                   // 流程实例
        homeworkTask.setTypeName( "IT工单" ); // 类别
        homeworkTask.setUrl( jumpPath ); // 扭转的URL

        // //获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        //刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = getFistNodeTask( processInstId ); // 获取第一个活动节点
        // 获取可是配置类型
        VisibleType visibleType = workflowService.getVisibleTypeByTaskId( task.getId() );
        ProcessFucExtParam extParam = new ProcessFucExtParam();
        extParam.setVisibleType( visibleType );
        // 加入待办列表
        homepageService.create( homeworkTask, userInfoScope, extParam );

        // 获取下一步执行人
        Map<String, List<String>> userIds = new HashMap<String, List<String>>();
        ProcessDefInfo pdi = new ProcessDefInfo( processEngine, task.getProcessDefinitionId() );

        String handleUserIds = "";
        String handleUserNames = "";
        for ( String nextTaskDefKey : workflowService.getNextTaskDefKeys( task.getProcessInstanceId(),
                task.getTaskDefinitionKey() ) ) {
            if ( nextTaskDefKey == null ) {
                continue;
            }
            FlowElement el = pdi.getElement( nextTaskDefKey );

            List<String> userList = new ArrayList<String>();
            List<SecureUser> secureUsers = workflowService.selectUsersByTaskKey( task.getProcessInstanceId(),
                    el.getId(), task.getId() );
            if ( secureUsers != null && secureUsers.size() != 0 ) {
                for ( SecureUser user : secureUsers ) {
                    userList.add( user.getId() );
                    handleUserIds += user.getId() + ",";
                    handleUserNames += user.getName() + ",";
                }
            } else {
                LOG.debug( "no users can be found by the elementifo." );
            }
            userIds.put( nextTaskDefKey, userList );
        }

        if ( "qxWoType".equals( woType )) { 
            // 不弹审批
            workflowService.complete( task.getId(), userId, userId, userIds, "新建工单", false );
            // 获取当前活动节点
            activities = workflowService.getActiveTasks( processInstId );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            task = activities.get( 0 );
            String nextAuditUserIdsStr = handleUserIds.substring( 0, handleUserIds.length() - 1 );
            String nextAuditUserNamesStr = handleUserNames.substring( 0, handleUserNames.length() - 1 );
            Map<String, String> parmas = new HashMap<String, String>();
            parmas.put( "woId", String.valueOf( woId ) );
            parmas.put( "currHandlerUser", nextAuditUserIdsStr );
            parmas.put( "currHandUserName", nextAuditUserNamesStr );
            workOrderDao.updateCurrHandUserById( parmas );
        }

        // 先删掉所有相关的附件数据
        woUtilService.deleteAttachMatch( woId, null, "WO" );
        // 插入附件的相关数据
        woUtilService.insertAttachMatch( woId, uploadIds, "WO", "newWo" );

        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "taskId", task.getId() );
        resultHashMap.put( "woId", woId );
        resultHashMap.put( "workOrderCode", flowCode );
        resultHashMap.put( "workflowId", processInstId );

        return resultHashMap;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, Object> insertInitWorkOrder(Map<String, String> wODataMap) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        String woFormData = wODataMap.get( "woFormData" );
        String uploadIds = wODataMap.get( "uploadIds" );
        String itcWhbWo = wODataMap.get( "itcWhbWo" ); // 判断是否是专业报障用户（工程师）
        boolean itcWhbWoFlag = Boolean.parseBoolean( itcWhbWo );
        String infoCenterWo = wODataMap.get( "infoCenterWo" );
        boolean infoCenterWoFlag = Boolean.parseBoolean( infoCenterWo );
        String comNextAudit = wODataMap.get( "comNextAudit" );
        ItsmWorkOrder workOrder = JsonHelper.toObject( woFormData, ItsmWorkOrder.class );
        if("".equals( workOrder.getId() )){
            workOrder.setId( null );
            workOrder.setWorkOrderCode( null );
        }
        // 更新客户的位置信息
        String customerLoc = workOrder.getCustomerLocation().trim();
        if ( customerLoc.length() > 0 ) {
            woUtilService.updateCustomerLocInfo( workOrder.getCustomerCode(), customerLoc );
        }

        String desString = workOrder.getDescription();
        workOrder.setDescription( desString );
        workOrder.setWorkOrderTypeCode( "qxWoType" ); // 非专业用户的报障都是缺陷工单类型

        workOrder.setCreatedate( new Date() );
        workOrder.setDiscoverTime( new Date() );
        workOrder.setCreateuser( userId );
        workOrder = setGeneralAttr( workOrder, userInfoScope ); // 通用属性的赋值(修改人，修改时间，站点，部门，有效标识)
        workOrderDao.insertWorkOrder( workOrder );
        String woId = workOrder.getId(); // 获取记录的ID
        
        // 启动流程
        String defkey = workflowService.queryForLatestProcessDefKey( ItsmConstant.WORKFLOWKEY );// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "businessId", woId );
        map.put( "itcWhbWo", itcWhbWoFlag );
        map.put( "infoCenterWo", infoCenterWoFlag );
        map.put( "comNextAudit", comNextAudit );

        LOG.info( "-------------ITC工单ID：" + woId + "启动流程----------------------" );
        // 启动流程
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                userInfoScope.getUserId(), map );

        LOG.info( "-------------ITC工单ID：" + woId + "流程启动结束----------------------" );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();

        // 修改业务表中的流程实例id信息
        workOrder.setWorkflowId( processInstId );
        workOrderDao.updateWorkflowId( processInstId, woId );

        // 加入待办列表
        String flowCode = workOrder.getWorkOrderCode();
        String jumpPath = "itsm/workorder/todolistTOWOPage.do?woId=" + woId;
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
        String woName = workOrder.getWoName();
        if(woName == null || woName.length() == 0){
            String description = workOrder.getDescription();
            if ( description == null || description.length() == 0 ) {
                description = workOrder.getWorkOrderCode() + "审批";
            }
            int size = (description.length() > 100) ? 100 : description.length();
            homeworkTask.setName( description.substring( 0, size ) ); // 名称
        }else{
            homeworkTask.setName( woName ); // 名称
        }
       

        homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "客服派单" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                   // 草稿;xxxx.Process
                                                                   // 流程实例
        homeworkTask.setTypeName( "IT工单" ); // 类别
        homeworkTask.setUrl( jumpPath ); // 扭转的URL

        Task task = getFistNodeTask( processInstId ); // 获取第一个活动节点
        // 获取可是配置类型
        VisibleType visibleType = workflowService.getVisibleTypeByTaskId( task.getId() );
        ProcessFucExtParam extParam = new ProcessFucExtParam();
        extParam.setVisibleType( visibleType );

        // 加入待办列表
        homepageService.create( homeworkTask, userInfoScope, extParam );
        // 获取当前活动节点
        // 获取下一步执行人
        Map<String, List<String>> userIds = new HashMap<String, List<String>>();
        ProcessDefInfo pdi = new ProcessDefInfo( processEngine, task.getProcessDefinitionId() );

        String handleUserIds = "";
        String handleUserNames = "";
        for ( String nextTaskDefKey : workflowService.getNextTaskDefKeys( task.getProcessInstanceId(),
                task.getTaskDefinitionKey() ) ) {
            if ( nextTaskDefKey == null ) {
                continue;
            }
            FlowElement el = pdi.getElement( nextTaskDefKey );

            List<String> userList = new ArrayList<String>();

            if ( itcWhbWoFlag ) { // 维护部建单
                List<SecureUser> secureUsers = workflowService.selectUsersByTaskKey( task.getProcessInstanceId(),
                        el.getId(), task.getId() );
                if ( secureUsers != null && secureUsers.size() != 0 ) {
                    for ( SecureUser user : secureUsers ) {
                        userList.add( user.getId() );
                        handleUserIds += user.getId() + ",";
                        handleUserNames += user.getName() + ",";
                    }
                } else {
                    LOG.debug( "no users can be found by the elementifo." );
                }
            } else { // 非维护部建单
                userList.add( userId );
                handleUserIds = userId;
                handleUserNames = userInfoScope.getUserName();
            }
            userIds.put( nextTaskDefKey, userList );
        }
        //TODO 申请人流转时自动执行其中一步
        if ( (!infoCenterWoFlag) && (!itcWhbWoFlag) ) { // 非科技公司+非信息中心建单 （ 或
                                                        // 维护部建单+需要信息中心审批：此处不存在这种情况）。默认要执行一步
            // 不弹审批
            workflowService.complete( task.getId(), userId, userId, userIds, "新建工单", false );
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( processInstId );

            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            task = activities.get( 0 );
            String nextAuditUserIdsStr = userInfoScope.getUserId();
            String nextAuditUserNamesStr = userInfoScope.getUserName();
            Map<String, String> parmas = new HashMap<String, String>();
            parmas.put( "woId", woId );
            parmas.put( "currHandlerUser", nextAuditUserIdsStr );
            parmas.put( "currHandUserName", nextAuditUserNamesStr );
            workOrderDao.updateCurrHandUserById( parmas );
        }

        // 先删掉所有相关的附件数据
        woUtilService.deleteAttachMatch( woId, null, "WO" );
        // 插入附件的相关数据
        woUtilService.insertAttachMatch( woId, uploadIds, "WO", "newWo" );

        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "taskId", task.getId() );
        resultHashMap.put( "woId", woId );
        resultHashMap.put( "workOrderCode", flowCode );
        resultHashMap.put( "workflowId", processInstId );

        return resultHashMap;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, String> updateInitWorkOrder(Map<String, String> wODataMap) throws Exception {
        Map<String, String> returnResult = new HashMap<String, String>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        String workOrderFormDate = wODataMap.get( "woFormData" ); // 工单基本信息表单数据
        String updateStyle = wODataMap.get( "updateStyle" );
        String uploadIds = wODataMap.get( "uploadIds" );
        String specUser = wODataMap.get( "specUser" ); // 判断是否是专业报障用户（工程师）
        boolean isEngineerFlag = Boolean.parseBoolean( specUser );
        String gotoWhb = wODataMap.get( "gotoWhb" );
        boolean isGotoWhbFlag = Boolean.parseBoolean( gotoWhb );

        ItsmWorkOrder workOrder = JsonHelper.toObject( workOrderFormDate, ItsmWorkOrder.class );
        String desString = workOrder.getDescription();
        workOrder.setDescription( desString );
        
        ItsmWorkOrder oldWorkOrder = workOrderDao.queryWOById( workOrder.getId() );
        workOrder.setWorkOrderTypeCode( oldWorkOrder.getWorkOrderTypeCode() );
        
        if ( "commit".equals( updateStyle ) ) { // 提交草稿，启动流程
            // 启动流程
            String defkey = workflowService.queryForLatestProcessDefKey( ItsmConstant.WORKFLOWKEY );// 获取最新流程定义版本
            Map<String, Object> map = new HashMap<String, Object>();
            String woType = workOrder.getWorkOrderTypeCode();
            map.put( "woType", woType );
            map.put( "specUser", isEngineerFlag );
            map.put( "goToWhb", isGotoWhbFlag );
            map.put( "businessId", workOrder.getId() );

            // 启动新流程
            LOG.info( "-------------ITC工单ID：" + workOrder.getId() + "启动----------------------" );
            ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                    userInfoScope.getUserId(), map );
            LOG.info( "-------------ITC工单ID：" + workOrder.getId() + "结束----------------------" );
            // 获取流程实例ID
            String processInstId = processInstance.getProcessInstanceId();

            workOrder.setWorkflowId( processInstId );
            workOrder.setCurrStatus( "sendWO" );
            workOrder.setModifydate( new Date() );
            workOrder.setModifyuser( userInfoScope.getUserId() );
            workOrderDao.updateWorkOrder( workOrder );

            // 加入待办列表
            String flowCode = workOrder.getWorkOrderCode();
            String woId = workOrder.getId();
            String jumpPath = "itsm/workorder/todolistTOWOPage.do?woId=" + woId;
            // 构建待办参数Bean
            HomepageWorkTask homeworkTask = new HomepageWorkTask();
            homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
            String description = workOrder.getDescription();
            String woName = workOrder.getWoName();
            if(woName == null || woName.length() == 0 ){
                if ( description == null || description.length() == 0 ) {
                    description = workOrder.getWorkOrderCode() + "审批";
                }
                int size = (description.length() > 100) ? 100 : description.length();
                homeworkTask.setName( description.substring( 0, size ) ); // 名称
            }else{
                homeworkTask.setName( woName ); // 名称
            }
           
            
            homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
            String todoWoStatus = "报障审批";

            homeworkTask.setStatusName( todoWoStatus ); // 状态
            homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                       // 草稿;xxxx.Process
                                                                       // 流程实例
            homeworkTask.setTypeName( "IT工单" ); // 类别
            homeworkTask.setUrl( jumpPath ); // 扭转的URL
            Task task = getFistNodeTask( processInstId ); // 获取第一个活动节点
            // 获取可是配置类型
            VisibleType visibleType = workflowService.getVisibleTypeByTaskId( task.getId() );
            ProcessFucExtParam extParam = new ProcessFucExtParam();
            extParam.setVisibleType( visibleType );
            // 加入待办列表
            homepageService.create( homeworkTask, userInfoScope, extParam );

            // 先删掉所有相关的附件数据
            woUtilService.deleteAttachMatch( woId, null, "WO" );
            // 插入附件的相关数据
            woUtilService.insertAttachMatch( woId, uploadIds, "WO", "newWo" );

            returnResult.put( "workflowId", processInstId );
            returnResult.put( "taskId", task.getId() );

        } else if ( "save".equals( updateStyle ) ) { // 再次暂存草稿，不启动流程
            workOrder.setCurrStatus( "draft" ); // 设置为草稿状态
            workOrder.setWorkflowId( oldWorkOrder.getWorkflowId() );
            workOrder.setModifydate( new Date() );
            workOrder.setModifyuser( userInfoScope.getUserId() );
            workOrderDao.updateWorkOrder( workOrder );
            // 先删掉所有相关的附件数据
            woUtilService.deleteAttachMatch( workOrder.getId(), null, "WO" );
            // 插入附件的相关数据
            woUtilService.insertAttachMatch( workOrder.getId(), uploadIds, "WO", "woDraft" );
          //修改待办或草稿列表中的“名称”
            String todoNameString = workOrder.getDescription();
            if(workOrder.getWoName()==null || workOrder.getWoName().isEmpty()){
                todoNameString = todoNameString.length()<=100?todoNameString:todoNameString.substring( 0, 100 );
            }else{
                todoNameString = workOrder.getWoName();
            }
            homepageService.modify( null, workOrder.getWorkOrderCode(), null, todoNameString, null, null, null, null );
            
            returnResult.put( "workflowId", "noFlow" );
            returnResult.put( "taskId", "noTask" );
        }
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, String> updateWorkOrder(Map<String, String> wODataMap) throws Exception {
        Map<String, String> returnResult = new HashMap<String, String>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        String workOrderFormDate = wODataMap.get( "woFormData" ); // 工单基本信息表单数据
        String updateStyle = wODataMap.get( "updateStyle" );
        String uploadIds = wODataMap.get( "uploadIds" );
        String itcWhbWo = wODataMap.get( "itcWhbWo" ); // 判断是否是专业报障用户（工程师）
        String needInfoCenterAudit = wODataMap.get( "needInfoCenterAudit" );  //是否需要信息中心审批
        boolean itcWhbWoFlag = Boolean.parseBoolean( itcWhbWo );
        boolean needInfoCenterAuditFlag = "yes".equals( needInfoCenterAudit )?true:false;
        String comNextAuditFlag = "";
        if(itcWhbWoFlag&&needInfoCenterAuditFlag){ //如果是维护部建单且需要信息中心审批，则必要要经过部门再审批,且不能立即派单
            comNextAuditFlag = "own";
        }
        
        ItsmWorkOrder workOrder = JsonHelper.toObject( workOrderFormDate, ItsmWorkOrder.class );
        String desString = workOrder.getDescription();
        workOrder.setDescription( desString );
        workOrder.setCustomerName(null);  //防止在前台连接了字符串取代真正的用户名和公司
        workOrder.setCustomerCom( null );
        ItsmWorkOrder oldWorkOrder = workOrderDao.queryWOById( workOrder.getId() );
        workOrder.setWorkOrderTypeCode( oldWorkOrder.getWorkOrderTypeCode() );
        
        if ( "commit".equals( updateStyle ) ) { // 提交草稿，启动流程
            // 启动流程
            String defkey = workflowService.queryForLatestProcessDefKey( ItsmConstant.WORKFLOWKEY );// 获取最新流程定义版本
            Map<String, Object> map = new HashMap<String, Object>();
            String woType = workOrder.getWorkOrderTypeCode();
            map.put( "woType", woType );
            map.put( "businessId", workOrder.getId() );
            map.put( "itcWhbWo", itcWhbWoFlag );
            map.put( "needInfoCenterAudit",needInfoCenterAuditFlag);
            map.put( "comNextAudit",comNextAuditFlag);
            
            // 启动新流程
            LOG.info( "-------------ITC工单ID：" + workOrder.getId() + "启动----------------------" );
            ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                    userInfoScope.getUserId(), map );
            LOG.info( "-------------ITC工单ID：" + workOrder.getId() + "结束----------------------" );
            // 获取流程实例ID
            String processInstId = processInstance.getProcessInstanceId();

            workOrder.setWorkflowId( processInstId );
            workOrder.setCurrStatus( "sendWO" );
            workOrder.setModifydate( new Date() );
            workOrder.setModifyuser( userInfoScope.getUserId() );
            workOrderDao.updateWorkOrder( workOrder );

            // 加入待办列表
            String flowCode = workOrder.getWorkOrderCode();
            String woId = workOrder.getId();
            String jumpPath = "itsm/workorder/todolistTOWOPage.do?woId=" + woId;
            // 构建待办参数Bean
            HomepageWorkTask homeworkTask = new HomepageWorkTask();
            homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
            String description = workOrder.getDescription();
            if ( description == null || description.length() == 0 ) {
                description = workOrder.getWorkOrderCode() + "审批";
            }
            int size = (description.length() > 100) ? 100 : description.length();
            homeworkTask.setName( description.substring( 0, size ) ); // 名称
            homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
            homeworkTask.setStatusName( "客服派单" ); // 状态
            homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                       // 草稿;xxxx.Process
                                                                       // 流程实例
            homeworkTask.setTypeName( "IT工单" ); // 类别
            homeworkTask.setUrl( jumpPath ); // 扭转的URL

            Task task = getFistNodeTask( processInstId ); // 获取第一个活动节点
            // 获取可是配置类型
            VisibleType visibleType = workflowService.getVisibleTypeByTaskId( task.getId() );
            ProcessFucExtParam extParam = new ProcessFucExtParam();
            extParam.setVisibleType( visibleType );
            // 加入待办列表
            homepageService.create( homeworkTask, userInfoScope, extParam );

            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( processInstId );
            // //刚启动流程，第一个活动节点肯定是属于当前登录人的
            // 获取下一步执行人
            Map<String, List<String>> userIds = new HashMap<String, List<String>>();
            ProcessDefInfo pdi = new ProcessDefInfo( processEngine, task.getProcessDefinitionId() );

            String handleUserIds = "";
            String handleUserNames = "";
            for ( String nextTaskDefKey : workflowService.getNextTaskDefKeys( task.getProcessInstanceId(),
                    task.getTaskDefinitionKey() ) ) {
                if ( nextTaskDefKey == null ) {
                    continue;
                }
                FlowElement el = pdi.getElement( nextTaskDefKey );

                List<String> userList = new ArrayList<String>();
                List<SecureUser> secureUsers = workflowService.selectUsersByTaskKey( task.getProcessInstanceId(),
                        el.getId(), task.getId() );
                if ( secureUsers != null && secureUsers.size() != 0 ) {
                    for ( SecureUser user : secureUsers ) {
                        userList.add( user.getId() );
                        handleUserIds += user.getId() + ",";
                        handleUserNames += user.getName() + ",";
                    }
                } else {
                    LOG.debug( "no users can be found by the elementifo." );
                }
                userIds.put( nextTaskDefKey, userList );
            }

            if ( "qxWoType".equals( woType ) ) { // 当为缺陷型单时，需要不弹出审批框。默认要执行一步
                // 不弹审批
                workflowService.complete( task.getId(), userId, userId, userIds, "新建工单", false );
                // 获取当前活动节点
                activities = workflowService.getActiveTasks( processInstId );
                // 刚启动流程，第一个活动节点肯定是属于当前登录人的
                task = activities.get( 0 );
                String nextAuditUserIdsStr = handleUserIds.substring( 0, handleUserIds.length() - 1 );
                String nextAuditUserNamesStr = handleUserNames.substring( 0, handleUserNames.length() - 1 );
                Map<String, String> parmas = new HashMap<String, String>();
                parmas.put( "woId", woId );
                parmas.put( "currHandlerUser", nextAuditUserIdsStr );
                parmas.put( "currHandUserName", nextAuditUserNamesStr );
                workOrderDao.updateCurrHandUserById( parmas );
            }

            // 先删掉所有相关的附件数据
            woUtilService.deleteAttachMatch( woId, null, "WO" );
            // 插入附件的相关数据
            woUtilService.insertAttachMatch( woId, uploadIds, "WO", "newWo" );

            returnResult.put( "workflowId", processInstId );
            returnResult.put( "taskId", task.getId() );

        } else if ( "save".equals( updateStyle ) ) { // 再次暂存草稿，不启动流程
            workOrder.setCurrStatus( "draft" ); // 设置为草稿状态
            workOrder.setWorkflowId( oldWorkOrder.getWorkflowId() );
            workOrder.setModifydate( new Date() );
            workOrder.setModifyuser( userInfoScope.getUserId() );
            workOrderDao.updateWorkOrder( workOrder );
            // 先删掉所有相关的附件数据
            woUtilService.deleteAttachMatch( workOrder.getId(), null, "WO" );
            // 插入附件的相关数据
            woUtilService.insertAttachMatch( workOrder.getId(), uploadIds, "WO", "woDraft" );

            returnResult.put( "workflowId", "noFlow" );
            returnResult.put( "taskId", "noTask" );
        }
        return returnResult;
    }

    @Override
    public Page<ItsmWorkOrder> queryAllWO(Page<ItsmWorkOrder> page) {
        // 如果有选择左边树节点，则插入查询过滤，如果选中的是根节点，则不过滤
        Map<String, Object> paramsMap = page.getParams();
        Object selectTreeIdObj = paramsMap.get( "selectTreeId" );
        Object rowFilterFlagObj = paramsMap.get( "rowFilterFlag" );
        if ( selectTreeIdObj != null && !"".equals( (String) selectTreeIdObj ) ) {
            int selectTreeId = Integer.valueOf( (String) selectTreeIdObj );
            ItsmWoFaultType ftRoot = woFaultTypeDao.queryFTRootBySiteId( "ITC" );
            if ( selectTreeId == ftRoot.getId() ) { // 如果选中的是根节点，则不过滤
                page.setParameter( "selectTreeId", null );
            }
        }

        String sortKeyString = "column1 desc," + page.getSortKey();
        page.setSortKey( sortKeyString );
        List<ItsmWorkOrder> ret3 = null;
        if ( rowFilterFlagObj != null ) {
            if ( "1".equals( rowFilterFlagObj.toString() ) ) { // 进行行过滤的查询
                ret3 = workOrderDao.queryFilterAllSortedItWO( page );
            } else { // 没有进行行过滤的查询
                ret3 = workOrderDao.queryAllSortedItWO( page );
            }
        } else {
            ret3 = workOrderDao.queryAllSortedItWO( page );
        }
        
        page.setResults( ret3 );
        LOG.info( "查询工单列表信息" );
        return page;
    }

    @Override
    public Map<String, Object> queryWOById(String id) {
        return queryItWOById( id );
    }

    @Override
    public Map<String, Object> queryItWOById(String woId) {
        ItsmWorkOrder workOrder = workOrderDao.queryItWOById( woId );
        String woStatus = workOrder.getCurrStatus();

        String taskId = "";
        List<String> candidateUsers = new ArrayList<String>();
        if ( woStatus != null && !"draft".equals( woStatus ) && !"woFiling".equals( woStatus )
                && !"woObsolete".equals( woStatus ) ) {
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( workOrder.getWorkflowId() );
           if(activities.size() > 0){
               Task task = activities.get( 0 );
               taskId = task.getId();
               // 获取节点的候选人
               candidateUsers = workflowService.getCandidateUsers( taskId );
           }else{
               taskId = "";
               candidateUsers = null;
           }
            
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "bean", workOrder );
        map.put( "taskId", taskId );
        map.put( "candidateUsers", candidateUsers );
        return map;
    }

    @Override
    public int getNextWOId() {
        return workOrderDao.getNextWOId();
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateWOStatus(Map<String, Object> parmas) {
        workOrderDao.updateWOStatus( parmas );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateWOHandlerStyle(Map<String, Object> handStyleMap) {
        workOrderDao.updateWOHandlerStyle( handStyleMap );

    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateWOOnPlan(Map<String, Object> parmas) {
        workOrderDao.updateWOOnPlan( parmas );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateWOOnAcceptance(Map<String, Object> parmas) {
        workOrderDao.updateWOOnAcceptance( parmas );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateWOOnReport(Map<String, Object> parmas) {
        workOrderDao.updateWOOnReport( parmas );
    }

    @Override
    public Map<String, Object> queryWOBaseInfoByWOCode(String woCode, String siteId) {
        ItsmWorkOrder workOrder = workOrderDao.queryWOBaseInfoByWOCode( woCode, siteId );
        Map<String, Object> result = new HashMap<String, Object>();
        if ( workOrder == null ) {
            result.put( "woExist", false );
            result.put( "isInPTW", false );

        } else {
            result.put( "woExist", true );
            if ( "inPTWing".equals( workOrder.getCurrStatus() ) ) { // 工单的当前状态是“工作票流程中”
                result.put( "isInPTW", true );
            } else {
                result.put( "isInPTW", false );
            }
        }
        result.put( "workOrder", workOrder );

        return result;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void deleteWorkOrder(String woId) {
        LOG.info( "-------------ITC 删除工单，工单ID：" + woId + "----------------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        ItsmWorkOrder workOrder = workOrderDao.queryWOById( woId );
        workOrderDao.deleteWorkOrder( woId );// 删除工单
        homepageService.Delete( workOrder.getWorkOrderCode(), userInfoScope );// 删除首页草稿
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateOperUserById(Map<String, String> parmas) {
        workOrderDao.updateOperUserById( parmas );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void stopWorkOrder(Map<String, Object> parmas) {

        String woId = (String) parmas.get( "woId" );
        String taskId = (String) parmas.get( "taskId" );
        String assignee = (String) parmas.get( "assignee" );
        String owner = (String) parmas.get( "owner" );
        String message = (String) parmas.get( "message" );
        workOrderDao.updateStopWO( parmas );
        // 终止流程后需要对流程进行处理
        LOG.info( "-------------ITC取消工单提交开始，工单ID：" + woId + "----------------------" );
        workflowService.stopProcess( taskId, assignee, owner, message );
        LOG.info( "-------------ITC取消工单提交开始，工单ID：" + woId + "----------------------" );
    }

    @Override
    public void updateWorkflowId(String workflowId, String woId) {
        workOrderDao.updateWorkflowId( workflowId, woId );
    }

    @Override
    public boolean userInGroupOrRole(String userGroupOrRoleId, String type, String userId, String userSiteId) {
        boolean flag = false;
        SecureUser user = iAuthorizationManager.retriveUserById( userId, userSiteId );
        List<Role> userRoles = user.getRoles();
        List<SecureUserGroup> userGroups = user.getGroups();
        if ( "role".equals( type ) ) {
            for ( int i = 0; i < userRoles.size(); i++ ) {
                if ( userRoles.get( i ).getId().equals( userGroupOrRoleId ) ) {
                    flag = true;
                }
            }
        } else if ( "usergroup".equals( type ) ) {
            for ( int i = 0; i < userGroups.size(); i++ ) {
                if ( userGroups.get( i ).getId().equals( userGroupOrRoleId ) ) {
                    flag = true;
                }
            }
        }

        return flag;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void cycMtpObjToWo(String mtpId, String todoId) throws Exception {
        LOG.info( "-------------ITC 维护计划生成工单，维护计划ID：" + mtpId + "----------------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        ItsmMaintainPlan tempMTP = maintainPlanDao.queryITMTPById( Integer.valueOf( mtpId ) );
        String principal = tempMTP.getPrincipal(); // 维护计划负责人

        if ( principal.contains( userId ) ) { // 负责人之一，手动生成工单（只给自己生成）
            woUtilService.cycMtpStartFlow( tempMTP, userInfoScope, userId );
        } else { // 非负责人（给所有人生成工单）
        }

        // 删掉提醒待办
        if ( (!"null".equals( todoId )) && (todoId != null) ) {
            homepageService.Delete( todoId, userInfoScope );
        }

    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, String> cancelCommitWO(String woId) {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        ItsmWorkOrder workOrder = workOrderDao.queryItWOById( woId );

        // 删掉启动流程的待办
        homepageService.Delete( workOrder.getWorkflowId(), userInfoScope );
        // 加入草稿待办
        String flowCode = workOrder.getWorkOrderCode();
        String jumpPath = "itsm/workorder/todolistTOWOPage.do?woId=" + woId;
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如采购申请 WO20140902001
        homeworkTask.setName( "IT工单草稿" );// 名称
        homeworkTask.setProcessInstId( null ); // 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "草稿" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Draft ); // 枚举类型定义是草稿还是流程,Draft
                                                                 // 草稿;Process
                                                                 // 流程实例
        homeworkTask.setTypeName( "IT工单" ); // 类别
        homeworkTask.setUrl( jumpPath );// 扭转的URL
        homepageService.create( homeworkTask, userInfoScope, null ); // 调用接口创建草稿

        // 删掉流程
        LOG.info( "-------------ITC取消工单提交开始，工单编号：" + workOrder.getWorkOrderCode() + "----------------------" );
        workflowService.delete( workOrder.getWorkflowId(), "提交取消" );
        LOG.info( "-------------ITC取消工单提交结束，工单编号：" + workOrder.getWorkOrderCode() + "----------------------" );
        // 清空工单中流程ID的信息
        workOrderDao.updateWorkflowId( "", woId );
        // 修改工单状态
        String woStatus = "draft";
        Map<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "woStatus", woStatus );
        parmas.put( "id", woId );
        parmas.put( "modifydate", new Date() );
        workOrderDao.updateWOStatus( parmas );
        
        
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;

    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, String> saveWOOnPlan() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String woFormDataStr = userInfoScope.getParam( "workOrderForm" );// 获取前台传过来的form表单数据
        ItsmWorkOrder workOrder = JsonHelper.toObject( woFormDataStr, ItsmWorkOrder.class );
        String isToPTW = userInfoScope.getParam( "isToPTW" );
        String jobPlanId = userInfoScope.getParam( "jobPlanId" );
        String preHazardDataStr = userInfoScope.getParam( "preHazardData" );
        String toolDataStr = userInfoScope.getParam( "toolData" );
        String taskDataStr = userInfoScope.getParam( "taskData" );
        String workerDataStr = userInfoScope.getParam( "workerData" );

        // 汇报表单的提取
        String woReportFormDataStr = userInfoScope.getParam( "woReportForm" );// 获取前台传过来的form表单数据
        JSONObject woReportFormJsonObj = JSONObject.fromObject( woReportFormDataStr );

        // 插入工单策划内容
        Map<String, String> addJPDataMap = new HashMap<String, String>();
        addJPDataMap.put( "woFormData", woFormDataStr );
        addJPDataMap.put( "preHazardDataStr", preHazardDataStr );
        addJPDataMap.put( "toolDataStr", toolDataStr );
        addJPDataMap.put( "taskDataStr", taskDataStr );
        addJPDataMap.put( "workerDataStr", workerDataStr );
        addJPDataMap.put( "commitStyle", "save" );
        addJPDataMap.put( "jobPlanId", jobPlanId );
        addJPDataMap.put( "userId", userId );

        String woCurrStatus = workOrder.getCurrStatus();
        if ( "woPlan".equals( woCurrStatus ) ) {
            addJPDataMap.put( "jpSource", "plan" ); // 作业方案的来源：standard:标准作业方案；maintainPlan:维护计划；
                                                    // plan：工单策划；actual:实际
        } else if ( "endWOReport".equals( woCurrStatus ) ) {
            addJPDataMap.put( "jpSource", "actual" );
        }

        int jpId = Integer.valueOf( jobPlanId );
        try {
            if ( jpId == 0 ) {
                jpId = jobPlanService.insertJobPlan( addJPDataMap );
            } else {
                jpId = jobPlanService.updateJobPlan( addJPDataMap );
            }
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        // 修改工单信息
        Map<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "id", workOrder.getId() );
        parmas.put( "jobPlanId", jpId ); // 添加作业方案的ID（最新的）
        parmas.put( "modifyuser", userId );
        parmas.put( "modifydate", new Date() );

        if ( "woPlan".equals( woCurrStatus ) ) {
            parmas.put( "isToPTW", isToPTW ); // 是否走工作票
            // 此处需要获得走工作票时的工作票ID
            parmas.put( "ptwId", 0 ); // 关联工作票的ID

        } else if ( "endWOReport".equals( woCurrStatus ) ) {

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

            parmas.put( "isHasRemainFault", "no_remainFault" ); // 暂存时，不暂存遗留问题记录
            parmas.put( "endReport", woReportFormJsonObj.get( "endReport" ) );
            parmas.put( "remainFaultId", 0 );
        }

        // ------------------汇报-----------------------
        if ( "woPlan".equals( woCurrStatus ) ) {
            workOrderDao.updateWOOnPlan( parmas );// 修改业务表中的数据
        } else if ( "endWOReport".equals( woCurrStatus ) ) {
            workOrderDao.updateWOOnReport( parmas );// 修改业务表中的数据
        }

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void deleteWorkOrderByWoCode(String woCode, String siteid) {
        workOrderDao.deleteWorkOrderByWoCode( woCode, siteid );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void obsoleteWorkOrder(String woId) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        ItsmWorkOrder workOrder = workOrderDao.queryItWOById( woId );
        String workflowId = workOrder.getWorkflowId();
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( workflowId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        String taskId = task.getId();

        // 修改工单状态
        LOG.info( "-------------作废工单处理开始,工单ID：" + woId );
        // 终止流程
        workflowService.stopProcess( taskId, userId, userId, "作废" );
        Map<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "woStatus", "woObsolete" );
        parmas.put( "id", woId );
        parmas.put( "modifydate", new Date() );
        workOrderDao.updateWOStatus( parmas );
        
        // 删掉对应的待办
        homepageService.complete( workflowId, userInfoScope, "已作废" );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void rollbackCommitWo(Map<String, String> addWODataMap) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        String woFormData = addWODataMap.get( "woFormData" );
        String uploadIds = addWODataMap.get( "uploadIds" );

        ItsmWorkOrder workOrder = JsonHelper.toObject( woFormData, ItsmWorkOrder.class );

        workOrderDao.updateWorkOrder( workOrder );
        String woId = workOrder.getId();
        workOrder = workOrderDao.queryItWOById( woId );
        String processInstId = workOrder.getWorkflowId();
        String woType = workOrder.getWorkOrderTypeCode();

        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );

        // 获取下一步执行人
        Map<String, List<String>> userIds = new HashMap<String, List<String>>();
        ProcessDefInfo pdi = new ProcessDefInfo( processEngine, task.getProcessDefinitionId() );

        String handleUserIds = "";
        String handleUserNames = "";
        for ( String nextTaskDefKey : workflowService.getNextTaskDefKeys( task.getProcessInstanceId(),
                task.getTaskDefinitionKey() ) ) {
            if ( nextTaskDefKey == null ) {
                continue;
            }
            FlowElement el = pdi.getElement( nextTaskDefKey );

            List<String> userList = new ArrayList<String>();
            List<SecureUser> secureUsers = workflowService.selectUsersByTaskKey( task.getProcessInstanceId(),
                    el.getId(), task.getId() );
            if ( secureUsers != null && secureUsers.size() != 0 ) {
                for ( SecureUser user : secureUsers ) {
                    userList.add( user.getId() );
                    handleUserIds += user.getId() + ",";
                    handleUserNames += user.getName() + ",";
                }
            } else {
                LOG.debug( "no users can be found by the elementifo." );
            }
            userIds.put( nextTaskDefKey, userList );
        }

        if ( "qxWoType".equals( woType ) ) { // 当为缺陷型单时，需要不弹出审批框。默认要执行一步
            // 不弹审批
            workflowService.complete( task.getId(), userId, userId, userIds, "提交IT工单", false );
            // 获取当前活动节点
            activities = workflowService.getActiveTasks( processInstId );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            task = activities.get( 0 );
            String nextAuditUserIdsStr = handleUserIds.substring( 0, handleUserIds.length() - 1 );
            String nextAuditUserNamesStr = handleUserNames.substring( 0, handleUserNames.length() - 1 );
            Map<String, String> parmas = new HashMap<String, String>();
            parmas.put( "woId", String.valueOf( woId ) );
            parmas.put( "currHandlerUser", nextAuditUserIdsStr );
            parmas.put( "currHandUserName", nextAuditUserNamesStr );
            workOrderDao.updateCurrHandUserById( parmas );
        }
        // 先删掉所有相关的附件数据
        woUtilService.deleteAttachMatch( workOrder.getId(), null, "WO" );
        // 插入附件的相关数据
        woUtilService.insertAttachMatch( workOrder.getId(), uploadIds, "WO", "newWo" );

    }

    @Override
    public Map<String, String> updateWoBaseInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String workOrderFormDate = userInfoScope.getParam( "workOrderForm" );// 获取前台传过来的form表单数据
        ItsmWorkOrder workOrder = JsonHelper.toObject( workOrderFormDate, ItsmWorkOrder.class );
        workOrderDao.updateWorkOrder( workOrder );

        //修改待办列表中的“名称”
        String todoNameString = workOrder.getDescription();
        if(workOrder.getWoName()==null || workOrder.getWoName().isEmpty()){
            todoNameString = todoNameString.length()<=100?todoNameString:todoNameString.substring( 0, 100 );
        }else{
            todoNameString = workOrder.getWoName();
        }
        homepageService.modify( null, workOrder.getWorkOrderCode(), null, todoNameString, null, null, null, null );
        
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    @Override
    public int getUserWoSum(String userId, String siteId) {
        return workOrderDao.getUserWoSum( userId, siteId );
    }

    @Override
    public Map<String, String> wobackToSomeStep(String woId, String woStepFlag) throws Exception {
        return null;
    }

    @Override
    public int queryNewWoSize(Date beginTime, Date endTime, String siteid) {
        return workOrderDao.queryNewWoSize( beginTime, endTime, siteid );
    }

    @Override
    public List<ItsmWorkOrder> queryNewWoList(Date beginTime, Date endTime, String siteid, int n, int selectSize) {
        return workOrderDao.queryNewWoList( beginTime, endTime, siteid, n, selectSize );
    }

    @Override
    public void updateCurrHandUserById(Map<String, String> parmas) {
        workOrderDao.updateCurrHandUserById( parmas );
    }

    @Override
    public void insertWorkOrderBean(ItsmWorkOrder workOrder) {
        workOrderDao.insertWorkOrder( workOrder );
    }

    @Override
    public List<ItsmWorkOrder> queryAllWoOfSomePeriod(Date beginTime, Date endTime, int selectIndex, int selectSize) {
        return workOrderDao.queryAllWoOfSomePeriod( beginTime, endTime, selectIndex, selectSize );
    }

    @Override
    public int queryAllWoSumOfSomePeriod(Date beginTime, Date endTime) {
        return workOrderDao.queryAllWoSumOfSomePeriod( beginTime, endTime );
    }

    @Override
    public void updateWoOnSendWo(Map<String, String> params) {
        String woId = params.get( "woId" );
        String faultTypeId = params.get( "faultTypeId" );
        String serCharacterId = params.get( "serCharacterId" );
        String priorityId = params.get( "priorityId" );
        workOrderDao.updateWoOnSendWo( woId, faultTypeId, serCharacterId, priorityId );
    }

    private Task getFistNodeTask(String processInstId) {
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        return task;
    }

    @Override
    public void updateHandUserAndStatus(String processInstId) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        Task task = activities.get( 0 );
        
        selectUserService.setProcessInstId( processInstId );
        selectUserService .setTaskId( task.getId() );
        //获取历史节点的执行人
        List<SecureUser> secureUsers = selectUserService.byHistroicAssignee( "send_workorder_a" );
        //获取对应的工单
        ItsmWorkOrder itsmWorkOrder = workOrderDao.queryWOByProcessInstId( processInstId, siteid);
      //修改当前处理人和工单状态
        Map<String, String> parmas1 = new HashMap<String, String>();
        parmas1.put( "woId", itsmWorkOrder.getId() );
        parmas1.put( "currHandlerUser", secureUsers.get( 0 ).getId() );
        parmas1.put( "currHandUserName", secureUsers.get( 0 ).getName() );
        workOrderDao.updateCurrHandUserById( parmas1 );
        // 修改工单的状态
        Map<String, Object> parmas2 = new HashMap<String, Object>();
        parmas2.put( "woStatus", "sendWOB" );
        parmas2.put( "id", itsmWorkOrder.getId() );
        parmas2.put( "modifydate", new Date() );
        workOrderDao.updateWOStatus( parmas2 );
        
    }

    @Override
    public void updateBusinessCurrHandler(String businessId, String userIds, String userNames) {
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put( "woId", businessId );
        parmas.put( "currHandlerUser", userIds );
        parmas.put( "currHandUserName", userNames );
        workOrderDao.updateCurrHandUserById( parmas );
    }

    @Override
    public void updateBusinessCurrStatus(String businessId, String status) {
        Map<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "woStatus", status );
        parmas.put( "id", businessId );
        parmas.put( "modifydate", new Date() );
        workOrderDao.updateWOStatus( parmas );
    }

}
