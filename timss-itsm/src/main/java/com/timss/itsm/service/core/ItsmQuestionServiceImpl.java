package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmQuestionRd;
import com.timss.itsm.bean.ItsmWoAttachment;
import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.dao.ItsmQuestionRdDao;
import com.timss.itsm.dao.ItsmWoFaultTypeDao;
import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmQuestionRdService;
import com.timss.itsm.service.ItsmWoAttachmentService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.vo.ItsmQuestionRdVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Service
public class ItsmQuestionServiceImpl implements ItsmQuestionRdService {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmQuestionRdDao questionRdDao;
    @Autowired
    ItsmWoFaultTypeDao woFaultTypeDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private HomepageService homepageService;
//    @Autowired
//    private ProcessEngine processEngine;
//    @Autowired
//    private IAuthorizationManager iAuthorizationManager;
    @Autowired
    private ItsmWoUtilService woUtilService;
    @Autowired
    private ItsmWoAttachmentService woAttachmentService;
    @Autowired
    private ItsmWorkOrderDao workOrderDao;

    private static final Logger LOG = Logger.getLogger( ItsmQuestionServiceImpl.class );

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, Object> saveOrUpdateQuestionRd(ItsmQuestionRdVo questionRdVo, boolean startWorkFlow)
            throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> resultMap = new HashMap<String, Object>( 0 );
        int id = questionRdVo.getId();
        String workflowId = questionRdVo.getWorkflowId();// 流程id
        String uploadIds = questionRdVo.getAttach();
        String code = questionRdVo.getCode();
        String status = questionRdVo.getStatus();
        String taskId = "noFlow";
        if ( StringUtils.isEmpty( workflowId ) ) { // 确定不是因为回退的节点
            if ( 0 == id ) { // save
                String createuser = userInfoScope.getUserId();
                Date createdate = new Date();
                String deptid = userInfoScope.getOrgId();
                String siteid = userInfoScope.getSiteId();
                questionRdVo.setCreateuser( createuser );
                questionRdVo.setCreatedate( createdate );
                questionRdVo.setDeptid( deptid );
                questionRdVo.setSiteid( siteid );
                questionRdVo.setStatus( "draft" );
                questionRdVo.setStep( "draft" );
                questionRdDao.insertQuestionRd( questionRdVo );
                id = questionRdVo.getId();
                code = questionRdVo.getCode();
                status = questionRdVo.getStatus();
                // 插入附件的相关数据
                woUtilService.insertAttachMatch( String.valueOf( id ), uploadIds, "QUESTION", "new" );
                if ( startWorkFlow ) {
                    // 启动流程
                    Map<String, Object> workMap = new HashMap<String, Object>( 0 );
                    workMap = startWorkFlow( questionRdVo, userInfoScope );
                    taskId = workMap.get( "taskId" ).toString();
                    workflowId = workMap.get( "processInstId" ).toString();
                    questionRdVo.setWorkflowId( workflowId );
                    questionRdVo.setModifydate( new Date() );
                    questionRdVo.setModifyuser( userInfoScope.getUserId() );
                    String[] params = new String[] { "workflowId" };
                    questionRdDao.updateQuestionRd( questionRdVo, params );
                    LOG.info( "-------------web层：问题首次提交完成,问题编号：" + code + "问题流程：" + workflowId + "-------------" );
                } else {
                    // 新建草稿
                    String jumpPath = "itsm/questionrd/todolistTOQuestionPage.do?id=" + id;
                    HomepageWorkTask homeworkTask = new HomepageWorkTask();
                    homeworkTask.setFlow( questionRdVo.getCode() );
                    homeworkTask.setName( questionRdVo.getTitle() ); // 名称
                    homeworkTask.setProcessInstId( null ); // 草稿时流程实例ID可以不用设置
                    homeworkTask.setStatusName( "草稿" ); // 状态
                    homeworkTask.setTypeName( "IT问题" ); // 类别
                    homeworkTask.setUrl( jumpPath );// 扭转的URL
                    homeworkTask.setType( HomepageWorkTask.TaskType.Draft ); // 枚举类型定义是草稿还是流程,Draft
                                                                             // 草稿;Process
                                                                             // 流程实例
                    homepageService.create( homeworkTask, userInfoScope ); // 调用接口创建草稿
                    LOG.info( "-------------web层：问题首次暂存完成,问题编号：" + code + "-------------" );
                }
            } else {
                // update
                String[] params = new String[] { "title", "resourceid", "category", "priorityid", "desp",
                        "eventQuestionCode" };
                questionRdDao.updateQuestionRd( questionRdVo, params );
                // 先删掉所有相关的附件数据
                woUtilService.deleteAttachMatch( String.valueOf( id ), null, "QUESTION" );
                // 插入附件的相关数据
                woUtilService.insertAttachMatch( String.valueOf( id ), uploadIds, "QUESTION", "new" );
                if ( startWorkFlow ) { // 启动流程
                    Map<String, Object> workMap = new HashMap<String, Object>( 0 );
                    workMap = startWorkFlow( questionRdVo, userInfoScope );
                    workflowId = workMap.get( "processInstId" ).toString();
                    taskId = workMap.get( "taskId" ).toString();
                    questionRdVo.setWorkflowId( workflowId );
                    questionRdVo.setModifydate( new Date() );
                    questionRdVo.setModifyuser( userInfoScope.getUserId() );
                    params = new String[] { "workflowId" };
                    questionRdDao.updateQuestionRd( questionRdVo, params );
                    LOG.info( "-------------web层：之前暂存的问题提交完成,问题流程：" + workflowId + "-------------" );
                } else {
                    LOG.info( "-------------web层：之前暂存的问题暂存完成,问题流程：" + workflowId + "-------------" );
                }
            }
        } else {
            LOG.info( "-------------web层：审批的问题再次提交或暂存开始,问题编号：" + code + "-------------" );
            String[] params = new String[] { "title", "resourceid", "category", "priorityid", "desp",
                    "eventQuestionCode" };
            questionRdVo.setModifydate( new Date() );
            questionRdVo.setModifyuser( userInfoScope.getUserId() );
            questionRdDao.updateQuestionRd( questionRdVo, params );
            // 先删掉所有相关的附件数据
            woUtilService.deleteAttachMatch( String.valueOf( id ), null, "QUESTION" );
            
            // 插入附件的相关数据
            woUtilService.insertAttachMatch( String.valueOf( id ), uploadIds, "QUESTION", "new" );
            LOG.info( "-------------web层：回退的问题再次提交或暂存完成,问题编号：" + code + "-------------" );
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( workflowId );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get( 0 );
            taskId = task.getId();
        }
        resultMap.put( "taskId", taskId );
        resultMap.put( "workflowId", workflowId );
        resultMap.put( "id", id );
        resultMap.put( "status", status );
        resultMap.put( "code", code );
        return resultMap;
    }

    /**
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @Title:startWorkFlow
     * @Description:启动流程
     * @param @param questionRd
     * @return void
     * @throws
     */
    private Map<String, Object> startWorkFlow(ItsmQuestionRd questionRd, UserInfoScope userInfoScope)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String defkey = workflowService.queryForLatestProcessDefKey( "itsm_core_question" );// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "businessId", questionRd.getId() );
        // 启动新流程
        LOG.info( "-------------问题ID：" + questionRd.getId() + "启动----------------------" );
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                userInfoScope.getUserId(), map );
        LOG.info( "-------------问题ID：" + questionRd.getId() + "结束----------------------" );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();
        // 加入待办列表
        String flowCode = questionRd.getCode();
        int id = questionRd.getId();
        String jumpPath = "itsm/questionrd/todolistTOQuestionPage.do?id=" + id;
        // 构建待办参数Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );
        homeworkTask.setName( questionRd.getTitle() ); // 名称
        homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "新建问题记录" ); // 状态
        homeworkTask.setTypeName( "IT问题" ); // 类别
        homeworkTask.setUrl( jumpPath ); // 跳转的URL
        homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,Draft
                                                                   // 草稿;Process
                                                                   // 流程实例
        // 加入待办列表
        homepageService.create( homeworkTask, userInfoScope );
        // 获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        map.put( "taskId", task.getId() );
        map.put( "processInstId", processInstId );
        map.put( "id", id );
        return map;
    }

    @Override
    public Page<ItsmQuestionRd> queryQuestionRd(Page<ItsmQuestionRd> page) throws Exception {
        Map<String, Object> paramsMap = page.getParams();
        Object selectTreeIdObj = paramsMap.get( "selectTreeId" );
        if ( selectTreeIdObj != null ) {
            int selectTreeId = Integer.valueOf( (String) selectTreeIdObj );
            ItsmWoFaultType ftRoot = woFaultTypeDao.queryFTRootBySiteId( "ITC" );
            if ( selectTreeId == ftRoot.getId() ) { // 如果选中的是根节点，则不过滤
                page.setParameter( "selectTreeId", null );
            }
        }
        List<ItsmQuestionRd> list = questionRdDao.queryQuestionRd( page );
        page.setResults( list );
        LOG.info( "查询问题列表信息" );
        return page;
    }

    @Override
    public Map<String, Object> queryQuestionRdById(int id) {
        ItsmQuestionRd questionRd = questionRdDao.queryQuestionRdById( id );
        String status = questionRd.getStatus();
        String taskId = "";
        List<String> candidateUsers = new ArrayList<String>();
        if ( status != null && !"draft".equals( status ) && !"closed".equals( status ) && !"finished".equals( status ) ) {
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( questionRd.getWorkflowId() );
            Task task = activities.get( 0 );
            taskId = task.getId();
            // 获取节点的候选人
            candidateUsers = workflowService.getCandidateUsers( taskId );
        }
        List<ItsmWoAttachment> attachList = woAttachmentService.queryWoAttachmentById( String.valueOf( questionRd.getId() ), "QUESTION" );
        ArrayList<String> aList = new ArrayList<String>();
        for ( int i = 0; i < attachList.size(); i++ ) {
            aList.add( attachList.get( i ).getAttachId() );
        }
        List<Map<String, Object>> attachmentMap = FileUploadUtil.getJsonFileList( Constant.basePath, aList );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "bean", questionRd );
        map.put( "taskId", taskId );
        map.put( "candidateUsers", candidateUsers );
        map.put( "attachmentMap", attachmentMap );
        return map;
    }

    @Override
    public void deleteQuestionRd(int id) {
        LOG.info( "-------------删除问题，问题ID：" + id + "----------------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        ItsmQuestionRd questionRd = questionRdDao.queryQuestionRdById( id );
        String[] params = new String[] { "delFlag" };
        questionRd.setDelFlag( "1" );
        questionRdDao.updateQuestionRd( questionRd, params );
        homepageService.Delete( questionRd.getCode(), userInfoScope );// 删除首页草稿
    }

    @Override
    public void stopQuestionRd(Map<String, Object> parmas) {

    }

    @Override
    public void cancelCommitQuestionRd(int id) {

    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void obsoleteQuestionRd(int id) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        ItsmQuestionRd questionRd = questionRdDao.queryQuestionRdById( Integer.valueOf( id ) );
        String workflowId = questionRd.getWorkflowId();
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( workflowId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        String taskId = task.getId();
        // 修改问题状态
        LOG.info( "-------------作废问题处理开始,问题ID：" + id );
        // 终止流程
        workflowService.stopProcess( taskId, userId, userId, "作废" );
        questionRd.setStatus( "zuofei" );
        String[] params = new String[] { "status" };
        questionRdDao.updateQuestionRd( questionRd, params );
        // 删掉对应的待办
        homepageService.complete( workflowId, userInfoScope, "已作废" );
    }

    @Override
    public int knowledgeIdAddToPromble(int prombleId, int konwledgeId) {
        return questionRdDao.updateKlIdOfQuest( prombleId, konwledgeId );
    }

    @Override
    public List<Map<String, Object>> queryWOFuzzyByName(String name) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>( 0 );
        Page<ItsmWorkOrder> page = new Page<ItsmWorkOrder>( 1, 11 );
        Map<String, Object> fuzzyParams = new HashMap<String, Object>( 0 );
        fuzzyParams.put( "WO_CODE", name );
        page.setFuzzyParams( fuzzyParams );
        List<ItsmWorkOrder> itsmWOs = workOrderDao.queryAllWO( page );
        for ( int i = 0; i < itsmWOs.size(); i++ ) {
            ItsmWorkOrder wo = itsmWOs.get( i );
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "name", wo.getWorkOrderCode() );
            map.put( "id", null == wo.getDescription() ? "" : wo.getDescription() );
            result.add( map );
        }
        return result;
    }
}
