package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmAttachment;
import com.timss.itsm.bean.ItsmCustomerLoc;
import com.timss.itsm.bean.ItsmMaintainPlan;
import com.timss.itsm.bean.ItsmWoAttachment;
import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.dao.ItsmKnowledgeDao;
import com.timss.itsm.dao.ItsmWoUtilDao;
import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmAttachmentService;
import com.timss.itsm.service.ItsmBusinessPubService;
import com.timss.itsm.service.ItsmWoAttachmentService;
import com.timss.itsm.service.ItsmWoFaultTypeService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.timss.itsm.util.ItsmConstant;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.bean.ProcessFucExtParam;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecProcRoute.VisibleType;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.UserExtParam;
import com.yudean.workflow.bean.WorkFlowExtParam;
import com.yudean.workflow.service.WorkflowService;

@Service
public class ItsmWoUtilServiceImpl implements ItsmWoUtilService {
    @Autowired
    ItsmWoFaultTypeService woFaultTypeService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    HomepageService homepageService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWorkOrderService workOrderService;
    @Autowired
    ItsmWoAttachmentService woAttachmentService;
    @Autowired
    ItsmAttachmentService attachmentService;
    @Autowired
    AttachmentMapper attachmentMapper;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    private ISecurityMaintenanceManager iSecManager;
    @Autowired
    private ItsmWoUtilDao itsmWoUtilDao;
    @Autowired
    ItsmKnowledgeDao itsmKnowledgeDao;
    @Autowired
    private ItsmWorkOrderDao workOrderDao;

    private static final Logger LOG = Logger.getLogger( ItsmWoUtilServiceImpl.class );

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void updateBusinessStatus(ItsmBusinessPubService itsmBusinessPubService,String woId, String woStatus) {
        itsmBusinessPubService.updateBusinessCurrStatus( woId, woStatus );
    }
    @Override  //ItsmWorkOrderSubService itsmWorkOrderSubService, String subWoId,
    public Map<String, String> queryAuditSelectUser(UserInfoScope userInfoScope) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>();
        String userIds = "";
        userIds = userInfoScope.getParam( "userIds" );
        if ( userIds == null ) {
            return null;
        }
        Map<String, List<String>> userIdsMap = (Map<String, List<String>>) new ObjectMapper().readValue( userIds, Map.class );
        Iterator iterator = userIdsMap.keySet().iterator();
        while (iterator.hasNext()) {
            List<String> auditUserId = userIdsMap.get( iterator.next() );
            String nextAuditUserIds = "";
            String nextAuditUserNames = "";
            for ( int i = 0; i < auditUserId.size(); i++ ) {
                nextAuditUserIds = auditUserId.get( i ) + ",";
                nextAuditUserNames = itcMvcService.getUserInfoById( auditUserId.get( i ) ).getUserName()
                            + ",";
               
            }
            nextAuditUserIds = nextAuditUserIds.substring( 0, nextAuditUserIds.length() - 1 );
            nextAuditUserNames = nextAuditUserNames.substring( 0, nextAuditUserNames.length() - 1 );
            resultMap.put( "ids", nextAuditUserIds );
            resultMap.put( "names", nextAuditUserNames );
        }
        return resultMap;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void updateBusinessCurrHandlerUser(ItsmBusinessPubService itsmBusinessPubService,String bussinessId, UserInfoScope userInfoScope, String flag) {
        try {
            String userIds = "";
            if ( "normal".equals( flag ) ) {
                userIds = userInfoScope.getParam( "userIds" );
                if ( userIds == null ) {
                    return;
                }
                Map<String, List<String>> userIdsMap = (Map<String, List<String>>) new ObjectMapper()
                        .readValue( userIds, Map.class );
                Iterator iterator = userIdsMap.keySet().iterator();
                while (iterator.hasNext()) {
                    List<String> auditUserId = userIdsMap.get( iterator.next() );
                    String nextAuditUserIds = "";
                    String nextAuditUserNames = "";
                    for ( int i = 0; i < auditUserId.size(); i++ ) {
                        nextAuditUserIds = auditUserId.get( i ) + ",";
                        String tempUserIds = auditUserId.get( i );
                        if ( tempUserIds.indexOf( "," ) > 0 ) {
                            String[] auditUserNames = tempUserIds.split( "," );
                            for ( int j = 0; j < auditUserNames.length; j++ ) {
                                nextAuditUserNames += itcMvcService.getUserInfoById( auditUserNames[j] ).getUserName()
                                        + ",";
                            }
                        } else {
                            nextAuditUserNames = itcMvcService.getUserInfoById( auditUserId.get( i ) ).getUserName()
                                    + ",";
                        }
                    }
                    nextAuditUserIds = nextAuditUserIds.substring( 0, nextAuditUserIds.length() - 1 );
                    nextAuditUserNames = nextAuditUserNames.substring( 0, nextAuditUserNames.length() - 1 );
                    
                    itsmBusinessPubService.updateBusinessCurrHandler( bussinessId, nextAuditUserIds, nextAuditUserNames );
                }
            } else if ( "rollback".equals( flag ) ) { // 回退的时候是单个人
                String nextAuditUserId = "";
                String nextAuditUserName = "";
                nextAuditUserId = userInfoScope.getParam( "userId" );
                nextAuditUserName = itcMvcService.getUserInfoById( nextAuditUserId ).getUserName();

                itsmBusinessPubService.updateBusinessCurrHandler( bussinessId, nextAuditUserId, nextAuditUserName );
            }
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void clearBusinessCurrHandlerUser(ItsmBusinessPubService itsmBusinessPubService,String businessId) {
        itsmBusinessPubService.updateBusinessCurrHandler( businessId, "", "" );
    }

    /**
     * @description:
     * @author: 王中华
     * @createDate: 2014-11-13
     * @param tempMTP 维护计划对象
     * @param userInfo 用户信息（定时任务用户）
     * @param principal 维护计划的单一负责人（生成的工单直接给此人策划）
     * @throws Exception:
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void cycMtpStartFlow(ItsmMaintainPlan tempMTP, UserInfo userInfo, String principal) throws Exception {
        LOG.info( "---------------周期性维护计划启动流程：维护计划编号：" + tempMTP.getMaintainPlanCode() + "--------------" );
        ItsmWorkOrder workOrder = new ItsmWorkOrder();

        String siteId = userInfo.getSiteId();

        // 设置了直接生成工单
//        int woId = workOrderService.getNextWOId(); // 获取要插入记录的ID
//        workOrder.setId( woId ); // 工单ID
        workOrder.setMaintainPlanId( tempMTP.getId() );
        workOrder.setWorkOrderTypeCode( "whWoType" ); // 设置工单类型
        workOrder.setDescription( tempMTP.getDescription() );
        workOrder.setEquipId( tempMTP.getEquipId() );
        workOrder.setEquipName( tempMTP.getEquipName() );
        workOrder.setWoSpecCode( tempMTP.getSpecialtyId() );
        workOrder.setFaultTypeId( tempMTP.getFaultTypeId() );
        workOrder.setFaultTypeName( tempMTP.getFaultTypeName() );
        workOrder.setRemarks( tempMTP.getRemarks() );
        workOrder.setCreateuser( userInfo.getUserId() );
        workOrder.setCreatedate( new Date() );
        workOrder.setSiteid( siteId );
        workOrder.setDeptid( tempMTP.getDeptid() );
        workOrder.setYxbz( 1 );
        workOrder.setCurrStatus( "woPlan" ); // 周期性工单直接进入工作策划阶段
        workOrderService.insertWorkOrderBean( workOrder );
        String woId = workOrder.getId();
        
        //  启动流程
        String defkey = workflowService.queryForLatestProcessDefKey( ItsmConstant.WORKFLOWKEY );// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "woType", "whWoType" ); // 周期性维护工单
        map.put( "businessId", woId );
        map.put( "specUser", true );
        // 启动流程
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey, siteId
                + "scheduler", map );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();

        workOrder.setWorkflowId( processInstId );
        workOrderService.updateWorkflowId( processInstId, woId );

        // 加入待办列表
        String flowCode = ((ItsmWorkOrder) workOrderService.queryWOById( woId ).get( "bean" )).getWorkOrderCode();
        String jumpPath = "itsm/workorder/openWOPlanInfoPage.do?woStatus=woPlan&woId=" + woId;

        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
        String description = tempMTP.getDescription();
        if ( description == null || description.length() == 0 ) {
            description = workOrder.getWorkOrderCode() + "审批";
        }
        int size = (description.length() > 100) ? 100 : description.length();
        homeworkTask.setName( description.substring( 0, size ) ); // 名称
        homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "工单策划" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                   // 草稿;xxxx.Process
                                                                   // 流程实例
        homeworkTask.setTypeName( "IT工单" ); // 类别
        homeworkTask.setUrl( jumpPath ); // 扭转的URL

        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        //  获取可是配置类型
        VisibleType visibleType = workflowService.getVisibleTypeByTaskId( task.getId() );
        ProcessFucExtParam extParam = new ProcessFucExtParam();
        extParam.setVisibleType( visibleType );

        // 加入待办列表
        homepageService.create( homeworkTask, userInfo, extParam );

        Map<String, List<String>> userIds = new HashMap<String, List<String>>();
        List<String> userList = new ArrayList<String>();
        userList.add( principal ); // 节点的审批人

        List<String> nextTasks = workflowService.getNextTaskDefKeys( processInstId, task.getTaskDefinitionKey() );
        userIds.put( nextTasks.get( 0 ), userList );

        if ( userInfo.getUserId().endsWith( "scheduler" ) ) {
            itcMvcService.setLocalAttribute( "isFromScheduler", "yes" );
        }

        // 完成任务
        // 这里第二个参数为执行人，这里必须是一个存在的虚拟人，数据库中需要有这个额人员信息
        // 第三个参数任务的拥有人，一般情况下就是执行人
        // 第四个参数为下一步执行人列表，为key-value的形式
        // 第五个参数为审批信息
        // 第六个参数可以不用管，填false即可
        workflowService.complete( task.getId(), userInfo.getUserId(), userInfo.getUserId(), userIds, "autocommit",
                false );

        // 修改当前执行人和执行人名
        HashMap<String, String> parmas = new HashMap<String, String>();
        parmas.put( "woId", String.valueOf( woId ) );
        parmas.put( "currHandlerUser", principal );
        String principalName = itcMvcService.getUserInfoById( principal ).getUserName();
        parmas.put( "currHandUserName", principalName );
        workOrderService.updateCurrHandUserById( parmas );

    }

    /**
     * @description:获取负责人（一个维护计划生成工单时所用的负责人）
     * @author: 王中华
     * @createDate: 2014-11-13
     * @param principal
     * @param workTeams
     * @return:
     */
    @Override
    public List<String> selectPrincipalList(String principal, String workTeams) {
        List<String> result = new ArrayList<String>();
        if ( principal != null ) { // 如果有直接负责人（电厂工单）
            result.add( principal );
        }
        if ( principal == null && workTeams != null ) { // 如果是负责组（IT工单）
            Set<String> set = new HashSet<String>(); // 所选用户组的所有用户
            String[] workTeam = workTeams.split( "," );
            // 查询出所有用户组的所有用户（去掉重复的）
            for ( int j = 0; j < workTeam.length; j++ ) {
                List<SecureUser> resultList = new ArrayList<SecureUser>();
                resultList = authManager.retriveUsersWithSpecificGroup( workTeam[j], null, false, true );
                for ( int k = 0; k < resultList.size(); k++ ) {
                    set.add( resultList.get( k ).getId() );
                }
            }
            // 为每一个用户生成一个工单
            Iterator<String> ite = set.iterator();
            while (ite.hasNext()) {
                result.add( ite.next() );
            }
        }
        return result;
    }

    @Override
    public String getOperUserTeam(UserInfoScope userInfoScope, ItsmWorkOrder workOrder) {
        String result = null;
        String siteid = userInfoScope.getSiteId();
        SecureUser engineerSecureUser = userInfoScope.getSecureUser();
        String woStatus = workOrder.getCurrStatus();
        // //获取要统计的用户组， 此处需要模糊查询，以"xxx"开头的用户组
        Page<SecureUserGroup> page = new Page<SecureUserGroup>();
        page = new Page<SecureUserGroup>();
        page.setPageSize( 100 );
        Map<String, Object> searchBy = new HashMap<String, Object>();
        searchBy.put( "searchBy", siteid.toLowerCase() + "_itsm_wt" );// 以xxx开头的用户组标识(此处需要将用户组中的ID变成小写)
        page.setParams( searchBy );

        SecureUser operator = userInfoScope.getSecureUser();
        Page<SecureUserGroup> qResult = iSecManager.retrieveGroups( page, operator );
        List<SecureUserGroup> userGroupList = qResult.getResults();

        if ( engineerSecureUser == null ) {
            if ( "workPlan".equals( woStatus ) ) { // 如果当前状态是“工程师处理”，此时是活动节点，则当前处理人为此单的工程师
                String handlerUserId = workOrder.getCurrHandlerUser();
                SecureUser tempSecureUser = authManager.retriveUserById( handlerUserId, siteid );
                result = getMaintainTeamId( tempSecureUser, userGroupList );
            }
        } else {
            result = getMaintainTeamId( engineerSecureUser, userGroupList );
        }

        return result;
    }

    @Override
    public String getMaintainTeamId(SecureUser engineerSecureUser, List<SecureUserGroup> statisticUserGroupList) {
        List<SecureUserGroup> userGroupsList = engineerSecureUser.getGroups(); // 当前处理人所在的用户组
        List<Role> userRoleList = engineerSecureUser.getRoles(); // 当前处理人拥有的角色
        for ( int i = 0; i < userRoleList.size(); i++ ) { // 如果拥有客服角色，则统计到“服务台”类型里去
            if ( "ITC_ITSM_KF".equals( userRoleList.get( i ).getId() ) ) {
                return "ITC_ITSM_WTFWT";
            }
        }
        for ( int i = 0; i < statisticUserGroupList.size(); i++ ) { // 如果拥有某个维护组，则统计到对应的维护组类型中去
            String statisticUGroupID = statisticUserGroupList.get( i ).getId();
            for ( int j = 0; j < userGroupsList.size(); j++ ) {
                String userGroupId = userGroupsList.get( j ).getId();
                if ( userGroupId.equals( statisticUGroupID ) ) {
                    return userGroupId;
                }
            }
        }
        return null;
    }

    /**
     * @description:查看服务目录对应的一级服务目录
     * @author: 王中华
     * @createDate: 2015-1-15
     * @param id 要查找的服务目录ID
     * @param rootId 树的根Id
     * @return:
     */
    @Override
    public ItsmWoFaultType getOneLevelFTById(int id, int rootId) {

        ItsmWoFaultType temp = woFaultTypeService.queryWoFaultTypeById( id );
        if ( temp.getParentId() != rootId ) {
            temp = getOneLevelFTById( temp.getParentId(), rootId );
        }
        return temp;
    }

    @Override
    public void insertAttachMatch(String businessId, String fileIds, String type, String loadPhase) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String userId = userInfoScope.getUserId();
        
        if ( fileIds != null  ) {
            String[] ids = fileIds.split( "," );
            ArrayList<String> newIds = new ArrayList<String>(); // 新的附件ids
            for ( int i = 0; i < ids.length; i++ ) {
                newIds.add( ids[i] );
            }

            List<ItsmWoAttachment> oldAttachmentList = woAttachmentService.queryWoAttachmentById( businessId, type );
            ArrayList<String> oldIds = new ArrayList<String>(); // 库中的附件ids
            for ( int i = 0; i < oldAttachmentList.size(); i++ ) {
                oldIds.add( oldAttachmentList.get( i ).getAttachId() );
            }
            HashMap<String, ArrayList<String>> operAttachmentList = getAttachmentList( newIds, oldIds );

            ArrayList<String> addList = operAttachmentList.get( "add" ); // 需要添加的附件
            ArrayList<String> removeList = operAttachmentList.get( "remove" ); // 需要删除的附件

            for ( int i = 0; i < addList.size(); i++ ) {
                String insertAttachId = addList.get( i );
                if(insertAttachId.length()>0){
                    ItsmWoAttachment woAttachment = new ItsmWoAttachment();
                    woAttachment.setId( businessId );
                    woAttachment.setType( type ); // "WO":工单;"MTP"：维护计划;"JP"：标准作业方案,"KL"知识单"INFOWO"信息工单
                    woAttachment.setAttachId( insertAttachId );
                    woAttachment.setLoadPhase( loadPhase );
                    woAttachment.setLoadTime( new Date() );
                    woAttachment.setLoadUser( userId );
                    woAttachment.setSiteid( siteId );
                    woAttachment.setYxbz( 1 );
                    woAttachmentService.insertWoAttachment( woAttachment );
                }
            }
            operAttachmentToServer( addList.toString().split( "," ), 1 ); // 绑定

            // 删掉附件
            for ( int i = 0; i < removeList.size(); i++ ) {
                woAttachmentService.deleteWoAttachment( businessId, removeList.get( i ), type, userId );
            }
            operAttachmentToServer( removeList.toString().split( "," ), 0 ); // 接触绑定
        }

    }

    /**
     * @description: 通过比较新的附件ID集合和数据库中的ID集合，计算出哪些附件是新增，哪些附件需要删除
     * @author: 王中华
     * @createDate: 2015-1-19
     * @param newIds
     * @param oldIds
     * @return:
     */
    private HashMap<String, ArrayList<String>> getAttachmentList(ArrayList<String> newIds, ArrayList<String> oldIds) {
        HashMap<String, ArrayList<String>> resultHashMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> addList = (ArrayList<String>) newIds.clone();
        ArrayList<String> removeList = (ArrayList<String>) oldIds.clone();

        for ( int i = addList.size() - 1; i >= 0; i-- ) {
            String str1 = addList.get( i );
            for ( int j = removeList.size() - 1; j >= 0; j-- ) {
                String str2 = removeList.get( j );
                if ( str1.equals( str2 ) ) {
                    addList.remove( i );
                    removeList.remove( j );
                }
            }
        }
        resultHashMap.put( "add", addList );
        resultHashMap.put( "remove", removeList );
        return resultHashMap;
    }

    private void operAttachmentToServer(String[] ids, int operType) {
        attachmentMapper.setAttachmentsBinded( ids, operType ); // 1表示绑定，0表示解除绑定
    }

    @Override
    public void deleteAttachMatch(String businessId, String attachId, String type) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        woAttachmentService.deleteWoAttachment( businessId, attachId, type, userId );
    }

    @Override
    public void updateCustomerLocInfo(String userId, String location) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String operUserId = userInfoScope.getUserId();
        // 查看是否有userId的记录
        ItsmCustomerLoc selectBean = itsmWoUtilDao.queryCustomerLocByUserId( userId );
        // 如果有，判读是否有做修改
        if ( selectBean == null ) {
            // 如果没有，做插入
            ItsmCustomerLoc tempBean = new ItsmCustomerLoc();
            tempBean.setCustomerCode( userId );
            tempBean.setCustomerLoc( location );
            tempBean.setCreatedate( new Date() );
            tempBean.setCreateuser( operUserId );
            tempBean.setModifydate( new Date() );
            tempBean.setModifyuser( operUserId );
            tempBean.setYxbz( 1 );
            itsmWoUtilDao.insertCustomerLocInfo( tempBean );
        } else {
            // 修改
            String selectLoc = selectBean.getCustomerLoc();
            if ( !selectLoc.equals( location ) ) {
                Map<String, Object> parma = new HashMap<String, Object>();
                parma.put( "customerCode", selectBean.getCustomerCode() );
                parma.put( "customerLoc", location );
                parma.put( "modifydate", new Date() );
                parma.put( "modifyuser", operUserId );
                itsmWoUtilDao.updateCustomerLocInfo( parma );
            }
        }
    }

    @Override
    public ItsmCustomerLoc queryCustomerLocInfo(String userId) {
        return itsmWoUtilDao.queryCustomerLocByUserId( userId );
    }

    @Override
    public Set<String> getNextStepSiteIdSet(ItcMvcService itcMvcService) {
        // 获取站点
        Object obj = itcMvcService.getLocalAttribute( "workflowExtParam" );
        Set<String> sitesSet = new HashSet<String>();
        if ( null != obj ) {
            WorkFlowExtParam extParam = (WorkFlowExtParam) obj;// 获取扩展阐述
            Map<String, UserExtParam> userworkFlowMap = extParam.getUserDataMap();
            Set<Entry<String, UserExtParam>> setList = userworkFlowMap.entrySet();
            for ( Entry<String, UserExtParam> entry : setList ) {
                String siteId = entry.getValue().getSiteId();// 获取站点信息
                sitesSet.add( siteId );
            }
        } else {
            sitesSet.add( itcMvcService.getUserInfoScopeDatas().getSiteId() );
        }
        return sitesSet;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void updateKlCurrHandlerUser(String klId, UserInfoScope userInfoScope, String flag) {
        try {
            String userIds = "";
            if ( "normal".equals( flag ) ) {
                userIds = userInfoScope.getParam( "userIds" );
                if ( userIds == null ) {
                    return;
                }
                HashMap<String, List<String>> userIdsMap = (HashMap<String, List<String>>) new ObjectMapper()
                        .readValue( userIds, Map.class );
                Iterator iterator = userIdsMap.keySet().iterator();
                while (iterator.hasNext()) {
                    List<String> auditUserId = userIdsMap.get( iterator.next() );
                    String nextAuditUserIds = "";
                    String nextAuditUserNames = "";
                    for ( int i = 0; i < auditUserId.size(); i++ ) {
                        nextAuditUserIds = auditUserId.get( i ) + ",";
                        String tempUserIds = auditUserId.get( i );
                        if ( tempUserIds.indexOf( "," ) > 0 ) {
                            String[] auditUserNames = tempUserIds.split( "," );
                            for ( int j = 0; j < auditUserNames.length; j++ ) {
                                nextAuditUserNames += itcMvcService.getUserInfoById( auditUserNames[j] ).getUserName()
                                        + ",";
                            }
                        } else {
                            nextAuditUserNames = itcMvcService.getUserInfoById( auditUserId.get( i ) ).getUserName()
                                    + ",";
                        }
                    }
                    nextAuditUserIds = nextAuditUserIds.substring( 0, nextAuditUserIds.length() - 1 );
                    nextAuditUserNames = nextAuditUserNames.substring( 0, nextAuditUserNames.length() - 1 );

                    itsmKnowledgeDao.updateKlCurrHandlerUser( klId, nextAuditUserIds, nextAuditUserNames );
                }
            } else if ( "rollback".equals( flag ) ) { // 回退的时候是单个人
                String nextAuditUserId = "";
                String nextAuditUserName = "";
                nextAuditUserId = userInfoScope.getParam( "userId" );
                nextAuditUserName = itcMvcService.getUserInfoById( nextAuditUserId ).getUserName();

                itsmKnowledgeDao.updateKlCurrHandlerUser( klId, nextAuditUserId, nextAuditUserName );
            }
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

    }

    @Override
    public void updateWoAndAttach(String woId, UserInfoScope userInfoScope, String woStatus) throws Exception {
        String userId = userInfoScope.getUserId();
        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject businessDataobj = JSONObject.fromObject( businessData );
        boolean flag = businessDataobj.containsKey( "initWorkOrderForm" );
        String woFormDataStr = "";
        if ( flag ) {
            woFormDataStr = businessDataobj.getString( "initWorkOrderForm" );
        } else {
            woFormDataStr = businessDataobj.getString( "workOrderForm" );
        }
        String attachmentIds = businessDataobj.getString( "attachmentIds" );

        ItsmWorkOrder workOrder;

        workOrder = JsonHelper.toObject( woFormDataStr, ItsmWorkOrder.class );

        workOrder.setModifydate( new Date() );
        workOrder.setModifyuser( userId );
        String[] params = new String[] { "description", "maintType", "faultTypeId", "customerPhone" };
        workOrderDao.updateInitWorkOrder( workOrder, params );
        //修改待办列表中的“名称”
        String todoNameString = workOrder.getDescription();
        if(workOrder.getWoName()==null || workOrder.getWoName().isEmpty()){
            todoNameString = todoNameString.length()<=100?todoNameString:todoNameString.substring( 0, 100 );
        }else{
            todoNameString =workOrder.getWoName();
        }
        
        homepageService.modify( null, workOrder.getWorkOrderCode(), null, todoNameString, null, null, null, null );
        // 操作附件
        insertAttachMatch( woId , attachmentIds, "WO", woStatus );

    }

    @Override
    public void insertAttachment(String businessId, String fileIds, String type, String loadPhase) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String userId = userInfoScope.getUserId();
        if ( "".equals( fileIds ) ) {
            fileIds = null;
        }
        if ( fileIds != null && fileIds != "" ) {
            String[] ids = fileIds.split( "," );
            ArrayList<String> newIds = new ArrayList<String>(); // 新的附件ids
            for ( int i = 0; i < ids.length; i++ ) {
                newIds.add( ids[i] );
            }

            List<ItsmAttachment> oldAttachmentList = attachmentService.queryAttachmentById( businessId, type );
            ArrayList<String> oldIds = new ArrayList<String>(); // 库中的附件ids
            for ( int i = 0; i < oldAttachmentList.size(); i++ ) {
                oldIds.add( oldAttachmentList.get( i ).getAttachId() );
            }
            HashMap<String, ArrayList<String>> operAttachmentList = getAttachmentList( newIds, oldIds );

            ArrayList<String> addList = operAttachmentList.get( "add" ); // 需要添加的附件
            ArrayList<String> removeList = operAttachmentList.get( "remove" ); // 需要删除的附件

            for ( int i = 0; i < addList.size(); i++ ) {

                ItsmAttachment attachment = new ItsmAttachment();
                attachment.setId( businessId );
                attachment.setType( type ); // "WO":工单;"MTP"：维护计划;"JP"：标准作业方案,"KL"知识单"Cp"投诉单"INFOWO"信息工单
                attachment.setAttachId( addList.get( i ) );
                attachment.setLoadPhase( loadPhase );
                attachment.setLoadTime( new Date() );
                attachment.setLoadUser( userId );
                attachment.setSiteid( siteId );
                attachment.setYxbz( 1 );
                attachmentService.insertAttachment( attachment );
            }
            operAttachmentToServer( addList.toString().split( "," ), 1 ); // 绑定

            // 删掉附件
            for ( int i = 0; i < removeList.size(); i++ ) {
                attachmentService.deleteAttachment( businessId, removeList.get( i ), type, userId );
            }
            operAttachmentToServer( removeList.toString().split( "," ), 0 ); // 解除绑定
        }        
    }

    @Override
    public void deleteAttachment(String businessId, String attachId, String type) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        attachmentService.deleteAttachment( businessId, attachId, type, userId );        
    }

    


	@Override
	public HomepageWorkTask joinHomepageWorkTask(String flowCode, String flowName,
			String processInstId, String statusName, 
			String typeName, String url) {
		  HomepageWorkTask homeworkTask = new HomepageWorkTask();
		  homeworkTask.setFlow(flowCode);
		  homeworkTask.setName(flowName);
		  if("".equals(processInstId) || null == processInstId){
			  homeworkTask.setProcessInstId(null);
			  homeworkTask.setType(HomepageWorkTask.TaskType.Draft);
		  }else{
			  homeworkTask.setProcessInstId(processInstId);
			  homeworkTask.setType(HomepageWorkTask.TaskType.Process);
		  }
		  homeworkTask.setStatusName(statusName);
		  homeworkTask.setTypeName(typeName);
		  homeworkTask.setUrl(url);
		  return homeworkTask;
	}
}
