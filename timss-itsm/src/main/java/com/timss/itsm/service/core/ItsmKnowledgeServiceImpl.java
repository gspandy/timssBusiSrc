package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmKnowledge;
import com.timss.itsm.dao.ItsmKnowledgeDao;
import com.timss.itsm.service.ItsmKnowledgeService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.util.ItsmConstant;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.bean.ProcessFucExtParam;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecProcRoute.VisibleType;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Service
public class ItsmKnowledgeServiceImpl implements ItsmKnowledgeService {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private HomepageService homepageService;
    @Autowired
    private ItsmKnowledgeDao itsmKnowledgeDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService woUtilService;

    private static final Logger LOG = Logger.getLogger( ItsmKnowledgeServiceImpl.class );

    @Override
    public Map<String, Object> queryItsmKnowledgeById(int id) {
        ItsmKnowledge itsmKnowledge = itsmKnowledgeDao.queryItsmKnowledgeById( id );
        String klStatus = itsmKnowledge.getCurrStatus();
        String taskId = "";
        List<String> candidateUsers = new ArrayList<String>();
        String auditEndStatus = ItsmConstant.KL_AUDIT_END;
        String auditObsolete = ItsmConstant.KL_OBSOLETE;
        boolean flag1 = auditEndStatus.equals( klStatus );
        boolean flag2 = auditObsolete.equals( klStatus );
        if ( klStatus != null && (!flag1) && (!flag2) ) {
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( itsmKnowledge.getWorkflowId() );
            Task task = activities.get( 0 );
            taskId = task.getId();
            // 获取节点的候选人
            candidateUsers = workflowService.getCandidateUsers( taskId );
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "bean", itsmKnowledge );
        map.put( "taskId", taskId );
        map.put( "candidateUsers", candidateUsers );
        return map;

    }

    @Override
    public void updateItsmKnowledge(ItsmKnowledge itsmKnowledge) {
        itsmKnowledgeDao.updateItsmKnowledge( itsmKnowledge );
    }

    @Override
    public void deleteItsmKnowledge(int klId) {
        itsmKnowledgeDao.deleteItsmKnowLedge( klId );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, Object> insertItsmKnowledge(Map<String, String> addkldata) throws Exception {
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String siteid = userInfoScope.getSiteId();
        String deptid = userInfoScope.getOrgId();

        String knowledgeString = addkldata.get( "knowledgeData" );
        String uploadIds = addkldata.get( "uploadIds" );
        ItsmKnowledge itsmKnowledge = JsonHelper.fromJsonStringToBean( knowledgeString, ItsmKnowledge.class );
        int klId = itsmKnowledgeDao.getNextKLId(); // 获取要插入记录的ID
        itsmKnowledge.setId( klId );
        itsmKnowledge.setYxbz( 1 );
        itsmKnowledge.setCreatedate( new Date() );
        itsmKnowledge.setCreateuser( userId );
        itsmKnowledge.setSiteid( siteid );
        itsmKnowledge.setDeptid( deptid );

        // 启动流程
        String defkey = workflowService.queryForLatestProcessDefKey( "itsm_yudean_kl" );// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "businessId", klId );
        LOG.info( "-------------ITC知识ID：" + klId + "启动流程----------------------" );
        // 启动流程
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                userInfoScope.getUserId(), map );

        LOG.info( "-------------ITC知识ID：" + klId + "流程启动结束----------------------" );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();

        // 修改业务表中的信息
        itsmKnowledge.setWorkflowId( processInstId );
        itsmKnowledgeDao.insertItsmKnowledge( itsmKnowledge );

        // 加入待办列表
        String flowCode = itsmKnowledgeDao.queryItsmKnowledgeById( klId ).getKnowledgeCode();
        String jumpPath = "itsm/knowledge/openKnowledgePage.do?klId=" + klId;
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
        String description = itsmKnowledge.getName();
        int size = (description.length() > 100) ? 100 : description.length();
        homeworkTask.setName( description.substring( 0, size ) ); // 名称

        homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "知识审批" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                   // 草稿;xxxx.Process
                                                                   // 流程实例
        homeworkTask.setTypeName( "IT知识单" ); // 类别
        homeworkTask.setUrl( jumpPath ); // 扭转的URL

        // //刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = getFistNodeTask( processInstId ); // 获取第一个活动节点
        // 获取可是配置类型
        VisibleType visibleType = workflowService.getVisibleTypeByTaskId( task.getId() );
        ProcessFucExtParam extParam = new ProcessFucExtParam();
        extParam.setVisibleType( visibleType );
        // 加入待办列表
        homepageService.create( homeworkTask, userInfoScope, extParam );

        // 先删掉所有相关的附件数据
        woUtilService.deleteAttachMatch( String.valueOf( klId ), null, "KL" );
        // 插入附件的相关数据
        woUtilService.insertAttachMatch( String.valueOf( klId ), uploadIds, "KL", "newKl" );

        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "taskId", task.getId() );
        resultHashMap.put( "klId", klId );
        resultHashMap.put( "knowledgeCode", flowCode );
        resultHashMap.put( "workflowId", processInstId );

        return resultHashMap;

    }

    @Override
    public Page<ItsmKnowledge> queryItsmKnowledgeList(Page<ItsmKnowledge> page) {
        List<ItsmKnowledge> ret3 = itsmKnowledgeDao.queryItsmKnowledgeList( page );
        page.setResults( ret3 );
        LOG.info( "查询知识列表信息" );
        return page;
    }

    private Task getFistNodeTask(String processInstId) {
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        return task;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void deleteKnowledge(int klId) {
        itsmKnowledgeDao.deleteItsmKnowLedge( klId );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void obsoleteWorkOrder(int klId) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        ItsmKnowledge itsmKnowledge = itsmKnowledgeDao.queryItsmKnowledgeById( klId );
        String workflowId = itsmKnowledge.getWorkflowId();
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( workflowId );
        Task task = activities.get( 0 );
        String taskId = task.getId();

        // 修改知识状态
        LOG.info( "-------------作废知识单处理开始,知识单ID：" + klId );
        // 终止流程
        workflowService.stopProcess( taskId, userId, userId, "作废" );

        itsmKnowledgeDao.updateItsmKnowledgeStatus( String.valueOf( klId ), ItsmConstant.KL_OBSOLETE );
        // 删掉对应的待办
        homepageService.complete( workflowId, userInfoScope, "已作废" );
    }
}
