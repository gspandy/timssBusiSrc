package com.timss.itsm.service.core;

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

import com.timss.itsm.bean.ItsmInfoWo;
import com.timss.itsm.bean.ItsmWoAttachment;
import com.timss.itsm.dao.ItsmInfoWoDao;
import com.timss.itsm.service.ItsmInfoWoService;
import com.timss.itsm.service.ItsmWoAttachmentService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.util.ItsmInfoWoStatus;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.bean.ProcessFucExtParam;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecProcRoute.VisibleType;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.workflow.service.WorkflowService;

@Service
public class ItsmInfoWoServiceImpl implements ItsmInfoWoService {
    @Autowired
    private HomepageService homepageService;
    @Autowired
    private ItsmInfoWoDao itsmInfoWoDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService itsmWoUtilService;
    @Autowired
    private ItsmWoAttachmentService woAttachmentService;
    
    private static final Logger LOG = Logger.getLogger( ItsmInfoWoServiceImpl.class );

    @Override
    public ItsmInfoWo queryItsmInfoWoById(String infoWoId) {
        ItsmInfoWo itsmInfoWo = itsmInfoWoDao.queryItsmInfoWoById( infoWoId );
        //taskId
        String workflowId = itsmInfoWo.getWorkflowId();
        Task task = getActivitiesTask( workflowId );
        if(task!=null){
            itsmInfoWo.setTaskId( task.getId() );
        }
        
        // 附件数据
        List<ItsmWoAttachment> attachList = woAttachmentService.queryWoAttachmentById( infoWoId, "INFOWO" );
        String attachmentIds = "";
        for ( int i = 0; i < attachList.size(); i++ ) {
            attachmentIds += attachList.get( i ).getAttachId()+",";
        }
        if(attachmentIds.length()>0){
            attachmentIds = attachmentIds.substring( 0, attachmentIds.length()-1 );
        }
        itsmInfoWo.setUploadIds( attachmentIds );
        
        return itsmInfoWo;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateItsmInfoWo(ItsmInfoWo itsmInfoWo) throws Exception {
        itsmInfoWoDao.updateItsmInfoWo( itsmInfoWo );
        String uploadIds  = itsmInfoWo.getUploadIds();
        //添加附件
        itsmWoUtilService.insertAttachMatch( itsmInfoWo.getId(), uploadIds,"INFOWO", "newApply" );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void deleteItsmInfoWo(String infoWoId) {
        itsmInfoWoDao.deleteItsmInfoWo( infoWoId );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public int insertItsmInfoWo(ItsmInfoWo itsmInfoWo) throws Exception {
        String uploadIds  = itsmInfoWo.getUploadIds();
        int result = itsmInfoWoDao.insertItsmInfoWo( itsmInfoWo );
        //添加附件
        itsmWoUtilService.insertAttachMatch( itsmInfoWo.getId(), uploadIds,"INFOWO", "newApply" );
        return result;
    }

    @Override
    public Page<ItsmInfoWo> queryItsmInfoWoList(Page<ItsmInfoWo> page) {
        List<ItsmInfoWo> infoWoList = itsmInfoWoDao.queryItsmInfoWoList( page );
        page.setResults( infoWoList );
        return page;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public int invalidWorkOrder(String infoWoId) {
        return itsmInfoWoDao.updateItsmInfoWoStatus( infoWoId, "" );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void insertItsmInfoWoWithFlow(ItsmInfoWo itsmInfoWo,UserInfoScope userInfoScope ) throws Exception {
        insertItsmInfoWo(itsmInfoWo);
        // 启动流程 并绑定到业务单上
        String defkey = workflowService.queryForLatestProcessDefKey( "itsm_"+itsmInfoWo.getSiteid().toLowerCase()+"_infowo" );// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "businessId", itsmInfoWo.getId() );
        map.put( "serType", itsmInfoWo.getSerType() );
        map.put( "businessType", itsmInfoWo.getBusinessType() );
        map.put( "rollbackFlag", "N");
        LOG.info( "-------------信息工单code：" + itsmInfoWo.getInfoWoCode() + "启动流程----------------------" );
        // 启动流程
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                itsmInfoWo.getCreateuser(), map );

        LOG.info( "-------------信息工单code：" + itsmInfoWo.getInfoWoCode() + "流程启动结束----------------------" );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();
        workflowService.setVariable(processInstId, "orgId", userInfoScope.getOrgId()); //为部门负责人的选人逻辑准备参数
        
        // 加入待办列表
        String flowCode = itsmInfoWo.getInfoWoCode();
        String jumpPath = "itsm/infoWo/openInfoWoPage.do?id=" + itsmInfoWo.getId();
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
        homeworkTask.setName( itsmInfoWo.getName() ); // 名称
        homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "草稿" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                   // 草稿;xxxx.Process
                                                                   // 流程实例
        homeworkTask.setTypeName( "信息工单" ); // 类别
        homeworkTask.setUrl( jumpPath ); // 扭转的URL

        // //刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = getActivitiesTask( processInstId ); // 获取第一个活动节点
        // 获取可视配置类型
        VisibleType visibleType = workflowService.getVisibleTypeByTaskId( task.getId() );
        ProcessFucExtParam extParam = new ProcessFucExtParam();
        extParam.setVisibleType( visibleType );
        // 加入待办列表
        homepageService.create( homeworkTask, userInfoScope, extParam );
        
        // 修改业务表中的信息
        itsmInfoWo.setWorkflowId( processInstId );
        itsmInfoWo.setTaskId( task.getId() );
        updateItsmInfoWo( itsmInfoWo );
        
        
    }

    private Task getActivitiesTask(String processInstId) {
        Task task = null;
        if(processInstId!=null && !"".equals( processInstId )){
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( processInstId );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            if(!activities.isEmpty()){
                task = activities.get( 0 );
            }
        }
        return task;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateBusinessCurrHandler(String businessId, String userIds, String userNames) {
        itsmInfoWoDao.updateInfoWoCurrHandlerUser(businessId,userIds,userNames);
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateBusinessCurrStatus(String businessId, String status) {
        itsmInfoWoDao.updateItsmInfoWoStatus( businessId, status );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateItsmInfoWoAndStartFlow(ItsmInfoWo itsmInfoWo,UserInfoScope userInfoScope) throws Exception {
        // 启动流程 并绑定到业务单上
        String defkey = workflowService.queryForLatestProcessDefKey( "itsm_"+itsmInfoWo.getSiteid().toLowerCase()+"_infowo" );// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "businessId", itsmInfoWo.getId() );
        map.put( "serType", itsmInfoWo.getSerType() );
        map.put( "businessType", itsmInfoWo.getBusinessType() );
        map.put( "rollbackFlag", "N");
        LOG.info( "-------------信息工单code：" + itsmInfoWo.getInfoWoCode() + "启动流程----------------------" );
        // 启动流程
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                itsmInfoWo.getCreateuser(), map );

        LOG.info( "-------------信息工单code：" + itsmInfoWo.getInfoWoCode() + "流程启动结束----------------------" );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();
        workflowService.setVariable(processInstId, "orgId", userInfoScope.getOrgId()); //为部门负责人的选人逻辑准备参数
        
        // 加入待办列表
        String flowCode = itsmInfoWo.getInfoWoCode();
        String jumpPath = "itsm/infoWo/openInfoWoPage.do?id=" + itsmInfoWo.getId();
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如工单编号 WO20140902001
        homeworkTask.setName( itsmInfoWo.getName() ); // 名称
        homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "草稿" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Process ); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                   // 草稿;xxxx.Process
                                                                   // 流程实例
        homeworkTask.setTypeName( "信息工单" ); // 类别
        homeworkTask.setUrl( jumpPath ); // 扭转的URL

        // //刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = getActivitiesTask( processInstId ); // 获取第一个活动节点
        // 获取可视配置类型
        VisibleType visibleType = workflowService.getVisibleTypeByTaskId( task.getId() );
        ProcessFucExtParam extParam = new ProcessFucExtParam();
        extParam.setVisibleType( visibleType );
        // 加入待办列表
        homepageService.create( homeworkTask, userInfoScope, extParam );
        
        itsmInfoWo.setWorkflowId( processInstId );
        itsmInfoWo.setTaskId( task.getId() );
        itsmInfoWoDao.updateItsmInfoWo( itsmInfoWo );
        
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void saveItsmInfoWo(ItsmInfoWo itsmInfoWo,UserInfoScope userInfoScope) throws Exception {
        insertItsmInfoWo( itsmInfoWo );
        //添加到草稿
        String flowCode = itsmInfoWo.getInfoWoCode();
        String infoWoId = itsmInfoWo.getId();
        String jumpPath =  "itsm/infoWo/openInfoWoPage.do?id=" + infoWoId;
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，如采购申请 WO20140902001
        String name = itsmInfoWo.getName();
        homeworkTask.setName( name ); // 名称
        homeworkTask.setProcessInstId( null ); // 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "草稿" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Draft ); // 枚举类型定义是草稿还是流程,Draft
                                                                 // 草稿;Process
                                                                 // 流程实例
        homeworkTask.setTypeName( "信息工单" ); // 类别
        homeworkTask.setUrl( jumpPath );// 扭转的URL
        homepageService.create( homeworkTask, userInfoScope, null ); // 调用接口创建草稿

    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void obsoleteItsmInfoWo(String infoWoId, UserInfoScope userInfoScope) {
        String userId = userInfoScope.getUserId();
        itsmInfoWoDao.clearInfoWoCurrHandlerUser( infoWoId );
        itsmInfoWoDao.updateItsmInfoWoStatus( infoWoId, ItsmInfoWoStatus.INVALID.getEnName() );
        ItsmInfoWo itsmInfoWo = itsmInfoWoDao.queryItsmInfoWoById( infoWoId );
        String workflowId = itsmInfoWo.getWorkflowId();
                
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( workflowId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        String taskId = task.getId();
        // 终止流程
        workflowService.stopProcess( taskId, userId, userId, ItsmInfoWoStatus.INVALID.getCnName() );
        // 删掉对应的待办
        homepageService.complete( workflowId, userInfoScope, ItsmInfoWoStatus.INVALID.getCnName() );
        
        
    }
}
