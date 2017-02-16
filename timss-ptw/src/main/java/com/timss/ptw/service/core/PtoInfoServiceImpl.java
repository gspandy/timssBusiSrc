package com.timss.ptw.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.service.AssetInfoService;
import com.timss.ptw.bean.PtoInfo;
import com.timss.ptw.bean.PtoOperItem;
import com.timss.ptw.dao.PtoInfoDao;
import com.timss.ptw.dao.PtoOperItemDao;
import com.timss.ptw.service.AttachMatchService;
import com.timss.ptw.service.PtoInfoService;
import com.timss.ptw.service.PtwUtilService;
import com.timss.ptw.util.CommonUtil;
import com.timss.ptw.vo.PtoInfoVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.facade.ITaskFacade;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.support.IEnumerationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Service
public class PtoInfoServiceImpl implements PtoInfoService {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private HomepageService homepageService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private AttachMatchService attachMatchService;
    @Autowired
    @Qualifier("assetInfoServiceImpl")
    private AssetInfoService assetInfoService;
    @Autowired
    private PtoInfoDao ptoInfoDao;
    @Autowired
    private  PtoOperItemDao ptoOperItemDao;
    @Autowired
    private  PtwUtilService ptwUtilService;
    @Autowired 
    private IEnumerationManager iEnumerationManager;
    
    private static final Logger LOG = Logger.getLogger( PtoInfoServiceImpl.class );

    

    @Override
    public Page<PtoInfo> queryPtoListInfo(Page<PtoInfo> page) throws Exception {
        Map<String, Object> paramsMap = page.getParams();
        Object selectTreeIdObj = paramsMap.get( "selectTreeId" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        if ( selectTreeIdObj != null ) {
            String selectTreeId = (String) selectTreeIdObj;
            AssetBean assetBean = assetInfoService.queryAssetTreeRootBySiteId( siteId );
            if ( StringUtils.equals( selectTreeId, assetBean.getAssetId())   ) { // 如果选中的是根节点，则不过滤
                page.setParameter( "selectTreeId", null );
            }
        }
        List<PtoInfo> list = ptoInfoDao.queryPtoListInfo( page );
        page.setResults( list );
        LOG.info( "查询操作票列表信息" );
        return page;
    }



    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, Object> saveOrUpdatePtoInfo(PtoInfoVo ptoInfoVo, boolean startWorkFlow) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String deptid = userInfoScope.getOrgId();
        String siteid = userInfoScope.getSiteId(); 
        //将冗余的相关人员的名字设置进bean中
        ptwUtilService.setRelatePersonName(ptoInfoVo,siteid);
        
        Map<String, Object> resultMap = new HashMap<String, Object>( 0 );
        String id = ptoInfoVo.getId();
        String workflowId = ptoInfoVo.getWorkflowId();// 流程id
        String uploadIds = ptoInfoVo.getAttach();
        String code = ptoInfoVo.getCode();
        String currStatus = ptoInfoVo.getCurrStatus();
        String taskId = "noFlow";
        //TODO 是否需要启动流程（例如：SWF只要新建、然后确定是执行了还是作废了的操作，并没有汇报操作，也就是做完之后回来点击下鼠标，单就结束了）
        boolean isNeedFlow = CommonUtil.hasEnumValue( iEnumerationManager, "PTO_NEED_FLOW", "Y", siteid );
        
        if ( StringUtils.isEmpty( workflowId ) ) { // 确定是没有启动过流程的数据
            if ( StringUtils.isEmpty( id ) ) { //第一次提交或者暂存
                ptoInfoVo.setId( null );
                ptoInfoVo.setCreateuser( userId );
                ptoInfoVo.setCreatedate( new Date() );
                ptoInfoVo.setModifyuser( userId );
                ptoInfoVo.setModifydate( new Date() );
                ptoInfoVo.setDeptid( deptid );
                ptoInfoVo.setSiteid( siteid );
                if(isNeedFlow){
                    ptoInfoVo.setCurrStatus( "draft" );//需要启动流程的，存为草稿
                }else{
                    ptoInfoVo.setCurrStatus( "new" );//不需要启动流程的为新建
                }
                try {
                    ptoInfoDao.insertPtoInfo( ptoInfoVo );
                } catch (Exception e) {
                    LOG.warn( "新增操作票主表异常", e );
                    throw e;
                }
                
                id = ptoInfoVo.getId();
                // 处理操作项
                Integer index = 0 ;
                for ( PtoOperItem ptoOperItem : ptoInfoVo.getPtoOperItemList() ) {
                    ptoOperItem.setShowOrder( index );
                    ptoOperItem.setId( null );
                    ptoOperItem.setPtoId( id );
                    ptoOperItemDao.insertPtoOperItem( ptoOperItem );
                    index++;
                }

                code = ptoInfoVo.getCode();
                currStatus = ptoInfoVo.getCurrStatus();
                // 插入附件的相关数据
                attachMatchService.insertAttachMatch( id, uploadIds, "PTO", "new" );
                // 提交-启动流程
                if ( startWorkFlow && isNeedFlow ) {
                    Map<String, Object> workMap = new HashMap<String, Object>( 0 );
                    workMap = startWorkFlow( ptoInfoVo, userInfoScope );
                    taskId = workMap.get( "taskId" ).toString();
                    workflowId = workMap.get( "processInstId" ).toString();
                    ptoInfoVo.setWorkflowId( workflowId );
                    ptoInfoVo.setModifydate( new Date() );
                    ptoInfoVo.setModifyuser( userId );
                    String[] params = new String[] { "workflowId" };
                    ptoInfoDao.updatePtoInfo( ptoInfoVo, params );
                    LOG.info( "-------------web层：操作票首次提交完成,操作票编号：" + code + "操作票流程：" + workflowId
                            + "-------------" );
                } else { // 暂存
                    String jumpPath = "ptw/ptoInfo/todolistToPtoPage.do?id=" + id;
                    if(isNeedFlow){ //需要走流程的站点
                        HomepageWorkTask homeworkTask = new HomepageWorkTask();
                        homeworkTask.setFlow( ptoInfoVo.getCode() );
                        homeworkTask.setName( ptoInfoVo.getTask().length() > 100 ? ptoInfoVo.getTask().substring( 0,
                                100 ) : ptoInfoVo.getTask()); // 名称
                        homeworkTask.setProcessInstId( null ); // 草稿时流程实例ID可以不用设置
                        homeworkTask.setStatusName( "草稿"); // 状态
                        homeworkTask.setTypeName( "操作票" ); // 类别
                        homeworkTask.setUrl( jumpPath );// 扭转的URL
                        homepageService.create( homeworkTask, userInfoScope ); // 调用接口创建草稿
                    }else{  //不需要走流程的站点，生物质站点不启动流程，新建后直接点击是否执行或者作废，就结束了
                        
                    }
                    LOG.info( "-------------web层：标准操作票首次暂存完成,操作票编号：" + code + "-------------" );
                }
            } else {// 编辑-暂存或提交(非第一次)
              //修改操作票的基本信息
                String[] params = new String[] { "task", "assetId","assetName", "type","preBeginOperTime","preEndOperTime",
                        "guardian", "guardianName", "commander", "commanderName","operItemRemarks"};
                updatePtoInfoNoWithWorkflow(ptoInfoVo, params, "new");
                
                if ( startWorkFlow ) { // 启动流程
                    Map<String, Object> workMap = new HashMap<String, Object>( 0 );
                    workMap = startWorkFlow( ptoInfoVo, userInfoScope );
                    workflowId = workMap.get( "processInstId" ).toString();
                    taskId = workMap.get( "taskId" ).toString();
                    ptoInfoVo.setWorkflowId( workflowId );
                    ptoInfoVo.setModifydate( new Date() );
                    ptoInfoVo.setModifyuser( userInfoScope.getUserId() );
                    String[]  params1 = new String[] { "workflowId" };
                    ptoInfoDao.updatePtoInfo( ptoInfoVo, params1 );
                    LOG.info( "-------------web层：之前暂存的标准操作票提交完成,标准操作票流程：" + workflowId + "-------------" );
                } else {
                    LOG.info( "-------------web层：之前暂存的标准操作票暂存完成,标准操作票流程：" + workflowId + "-------------" );
                }
            }
        } else {  //不启动流程，或者是修改与流程无关时
            String[] params = new String[] { "task", "assetId","assetName", "type","preBeginOperTime","preEndOperTime",
                    "guardian", "guardianName", "commander", "commanderName","operItemRemarks"};
            updatePtoInfoNoWithWorkflow(ptoInfoVo, params, ptoInfoVo.getCurrStatus());
        }
        resultMap.put( "taskId", taskId );
        resultMap.put( "workflowId", workflowId );
        resultMap.put( "id", id );
        resultMap.put( "currStatus", currStatus );
        resultMap.put( "code", code );
        resultMap.put( "isNeedFlow",isNeedFlow);
        return resultMap;
    }

    @Override
    public void updatePtoInfoNoWithWorkflow(PtoInfoVo ptoInfoVo, String[] params, String currstatus) throws Exception{
        String id = ptoInfoVo.getId();
        String uploadIds = ptoInfoVo.getAttach();
        
        ptoInfoDao.updatePtoInfo( ptoInfoVo, params );
        //修改操作票的操作项信息
        ptoOperItemDao.deletePtoOperItemsByPtoId( ptoInfoVo.getId() );
        
        for ( PtoOperItem ptoOperItem : ptoInfoVo.getPtoOperItemList() ) {
            ptoOperItem.setId( null );
            ptoOperItem.setPtoId( ptoInfoVo.getId() );
            ptoOperItemDao.insertPtoOperItem( ptoOperItem );
        }
        // 先删掉所有相关的附件数据
        attachMatchService.deleteAttachMatch( id, null, "PTO" );
        // 插入附件的相关数据
        attachMatchService.insertAttachMatch( id, uploadIds, "PTO", currstatus );
    }
    
    @Override
    public PtoInfoVo queryPtoInfoById(String ptoId) {
        return ptoInfoDao.queryPtoInfoById(ptoId);
    }

    @Override
    public PtoInfoVo queryPtoInfoVoById(String ptoId) {
        PtoInfoVo ptoInfoVo = ptoInfoDao.queryPtoInfoById(ptoId);
        List<PtoOperItem> ptoOperItems = ptoOperItemDao.queryPtoOperItemByPtoId( ptoId );
        ptoInfoVo.setPtoOperItemList( ptoOperItems );
        String workflowIdString = ptoInfoVo.getWorkflowId();
        
        if(!StringUtils.isEmpty(workflowIdString)){
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( ptoInfoVo.getWorkflowId() );
            if(activities.size()>0){
                Task task = activities.get( 0 );
                String taskId = task.getId();
                ptoInfoVo.setTaskId( taskId );
            }
            
        }
        
        return ptoInfoVo;
    }
    
    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public int obsoletePtoInfoById(String ptoId) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        int updateRowsNum = ptoInfoDao.updatePtoStatusById(ptoId,"obsolete",userId,new Date());
        return updateRowsNum;
    }



    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public int deletePtoInfoById(String ptoId) {
        int updateRowsNum = ptoInfoDao.deletePtoInfoById(ptoId);
        return updateRowsNum;
    }

    /**
     * @description: 启动流程
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param sptoInfoVo
     * @param userInfoScope
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException:
     */
    private Map<String, Object> startWorkFlow(PtoInfoVo ptoInfoVo, UserInfoScope userInfoScope)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String siteid = userInfoScope.getSiteId(); 
        
        String defkey = workflowService.queryForLatestProcessDefKey( "ptw_"+siteid.toLowerCase()+"_pto" );// 获取审批操作票的最新流程定义版本
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "businessId", ptoInfoVo.getId() );
        // 启动新流程
        LOG.info( "-------------操作票ID：" + ptoInfoVo.getId() + "启动----------------------" );
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                userInfoScope.getUserId(), map );
        LOG.info( "-------------操作票ID：" + ptoInfoVo.getId() + "结束----------------------" );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();
        // 加入待办列表
        String flowCode = ptoInfoVo.getCode();
        String id = ptoInfoVo.getId();
        String jumpPath = "ptw/ptoInfo/todolistToPtoPage.do?id=" + id;
        // 构建待办参数Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );
        homeworkTask.setName( ptoInfoVo.getTask().length() > 100 ? ptoInfoVo.getTask().substring( 0, 100 ) : ptoInfoVo
                .getTask() ); // 名称
        homeworkTask.setProcessInstId( processInstId ); // 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "新建操作票记录" ); // 状态
        homeworkTask.setTypeName( "操作票" ); // 类别
        homeworkTask.setUrl( jumpPath ); // 跳转的URL
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
    public void deletePtoInfo(String id) {
        LOG.info( "-------------删除操作票，操作票ID：" + id + "----------------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        PtoInfoVo ptoInfoVo = ptoInfoDao.queryPtoInfoById(id);
        String[] params = new String[] { "delFlag" };
        ptoInfoVo.setDelFlag( "Y" );
        ptoInfoDao.updatePtoInfo( ptoInfoVo, params );
        homepageService.Delete( ptoInfoVo.getCode(), userInfoScope );// 删除首页草稿
        
    }

    @Override
    public void obsoletePtoInfo(String id) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        PtoInfoVo ptoInfoVo = ptoInfoDao.queryPtoInfoById( id );
        String workflowId = ptoInfoVo.getWorkflowId();
        if(workflowId != null){
         // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( workflowId );
            Task task = activities.get( 0 );
            String taskId = task.getId();
            // 修改操作票状态
            LOG.info( "-------------作废操作票处理开始,操作票CODE：" + ptoInfoVo.getCode() );
            // 终止流程
            workflowService.stopProcess( taskId, userId, userId, "作废" );
            // 删掉对应的待办
            homepageService.complete( workflowId, userInfoScope, "已作废" );
        }
        
        ptoInfoVo.setCurrStatus( "obsolete" );
        String[] params = new String[] { "currStatus" };
        ptoInfoDao.updatePtoInfo( ptoInfoVo, params );
    }

    @Override
    public void hasDonePtoInfo(String id) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        PtoInfoVo ptoInfoVo = ptoInfoDao.queryPtoInfoById( id );
        ptoInfoVo.setCurrStatus( "end" );
        ptoInfoVo.setModifydate( new Date() );
        ptoInfoVo.setModifyuser( userId );
        String[] params = new String[] { "currStatus" };
        ptoInfoDao.updatePtoInfo( ptoInfoVo, params );        
    }

    @Override
    public void updatePtoInfoOnCheck(Map<String, Object> params) {
        ptoInfoDao.updatePtoInfoOnCheck(params);
    }



   


    



}
