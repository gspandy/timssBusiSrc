package com.timss.ptw.service.core;

import java.util.ArrayList;
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
import com.timss.ptw.bean.PtoAttachment;
import com.timss.ptw.bean.PtoOperItem;
import com.timss.ptw.bean.SptoInfo;
import com.timss.ptw.dao.SptoInfoDao;
import com.timss.ptw.service.AttachMatchService;
import com.timss.ptw.service.PtwUtilService;
import com.timss.ptw.service.SptoInfoService;
import com.timss.ptw.vo.SptoInfoVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Service
public class SptoInfoServiceImpl implements SptoInfoService {
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
    private SptoInfoDao sptoInfoDao;
    @Autowired
    private PtwUtilService ptwUtilService;
    private static final Logger LOG = Logger.getLogger( SptoInfoServiceImpl.class );

    /**
     * @Title:saveOrUpdateSptoInfo
     * @Description:保存或更新标准操作票
     * @param SptoInfoVo
     * @return Map<String, Object>
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, Object> saveOrUpdateSptoInfo(SptoInfoVo sptoInfoVo, boolean startWorkFlow) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> resultMap = new HashMap<String, Object>( 0 );
        String id = sptoInfoVo.getId();
        String procinstId = sptoInfoVo.getProcinstId();// 流程id
        String uploadIds = sptoInfoVo.getAttach();
        String code = sptoInfoVo.getCode();
        String status = sptoInfoVo.getStatus();
        String taskId = "noFlow";
        if ( StringUtils.isEmpty( procinstId ) ) { // 确定是没有启动过流程的数据
            if ( StringUtils.isEmpty( id ) ) { 
                //新建-暂存或提交
                String createuser = userInfoScope.getUserId();
                Date createdate = new Date();
                String deptid = userInfoScope.getOrgId();
                String siteid = userInfoScope.getSiteId();
                sptoInfoVo.setCreateuser( createuser );
                sptoInfoVo.setCreatedate( createdate );
                sptoInfoVo.setDeptid( deptid );
                sptoInfoVo.setSiteid( siteid );
                sptoInfoVo.setStatus( "draft" );
                sptoInfoVo.setDelFlag( "N" );
                sptoInfoVo.setIsDraft( "Y" );
                sptoInfoVo.setId( null );
                
                try {
                    sptoInfoDao.insertSptoInfo( sptoInfoVo );
                } catch (Exception e) {
                    LOG.warn( "新增标准操作票主表异常", e );
                    throw e;
                }
                
                // 处理操作项
                Integer index = 0 ;
                for ( PtoOperItem sptoItem : sptoInfoVo.getPtoOperItemList() ) {
                    sptoItem.setShowOrder( index );
                    sptoItem.setId( null );
                    sptoItem.setPtoId( sptoInfoVo.getId() );
                    sptoInfoDao.insertSptoItem( sptoItem );
                    index++;
                }

                id = sptoInfoVo.getId();
                code = sptoInfoVo.getCode();
                status = sptoInfoVo.getStatus();
                // 插入附件的相关数据
                attachMatchService.insertAttachMatch( id, uploadIds, "SPTO", "new" );
                // 提交-启动流程
                if ( startWorkFlow ) {
                    Map<String, Object> workMap = new HashMap<String, Object>( 0 );
                    workMap = startWorkFlow( sptoInfoVo, userInfoScope );
                    taskId = workMap.get( "taskId" ).toString();
                    procinstId = workMap.get( "processInstId" ).toString();
                    sptoInfoVo.setProcinstId( procinstId );
                    sptoInfoVo.setModifydate( new Date() );
                    sptoInfoVo.setModifyuser( userInfoScope.getUserId() );
                    String[] params = new String[] { "procinstId" };
                    sptoInfoDao.updateSptoInfo( sptoInfoVo, params );
                    LOG.info( "-------------web层：标准操作票首次提交完成,标准操作票编号：" + code + "标准操作票流程：" + procinstId
                            + "-------------" );
                } else {
                    // 暂存
                    String jumpPath = "ptw/sptoInfo/todolistTOSptoPage.do?id=" + id;
                    HomepageWorkTask homeworkTask = new HomepageWorkTask();
                    homeworkTask.setFlow( sptoInfoVo.getSheetNo() );
                    homeworkTask.setName( sptoInfoVo.getMission().length() > 30 ? sptoInfoVo.getMission().substring( 0,
                            30 ) : sptoInfoVo.getMission() ); // 名称
                    homeworkTask.setProcessInstId( null ); // 草稿时流程实例ID可以不用设置
                    homeworkTask.setStatusName( "草稿" ); // 状态
                    homeworkTask.setTypeName( "标准操作票" ); // 类别
                    homeworkTask.setUrl( jumpPath );// 扭转的URL
                    homepageService.create( homeworkTask, userInfoScope ); // 调用接口创建草稿
                    LOG.info( "-------------web层：标准操作票首次暂存完成,标准操作票编号：" + code + "-------------" );
                }
            } else {
                // 编辑-暂存或提交
                String[] params = new String[] { "mission", "equipment", "type","operItemRemarks" };
                sptoInfoDao.updateSptoInfo( sptoInfoVo, params );
                // 处理操作项
                sptoInfoDao.deleteSptoItem( sptoInfoVo.getId() );
                int index = 1;
                for ( PtoOperItem sptoItem : sptoInfoVo.getPtoOperItemList() ) {
                    sptoItem.setId( null );
                    sptoItem.setShowOrder( index );
                    sptoItem.setPtoId( sptoInfoVo.getId() );
                    sptoInfoDao.insertSptoItem( sptoItem );
                    index++;
                }
                // 先删掉所有相关的附件数据
                attachMatchService.deleteAttachMatch( id, null, "SPTO" );
                // 插入附件的相关数据
                attachMatchService.insertAttachMatch( id, uploadIds, "SPTO", "new" );
                if ( startWorkFlow ) { // 启动流程
                    Map<String, Object> workMap = new HashMap<String, Object>( 0 );
                    workMap = startWorkFlow( sptoInfoVo, userInfoScope );
                    procinstId = workMap.get( "processInstId" ).toString();
                    taskId = workMap.get( "taskId" ).toString();
                    sptoInfoVo.setProcinstId( procinstId );
                    sptoInfoVo.setModifydate( new Date() );
                    sptoInfoVo.setModifyuser( userInfoScope.getUserId() );
                    params = new String[] { "procinstId" };
                    sptoInfoDao.updateSptoInfo( sptoInfoVo, params );
                    LOG.info( "-------------web层：之前暂存的标准操作票提交完成,标准操作票流程：" + procinstId + "-------------" );
                } else {
                    LOG.info( "-------------web层：之前暂存的标准操作票暂存完成,标准操作票流程：" + procinstId + "-------------" );
//                    homepageService.modify( id, flow, typeName, name, statusName, operUser, url, userInfo );
                    String todoNameString = sptoInfoVo.getMission().length() > 100 ? sptoInfoVo.getMission().substring(
                            0, 100 ) : sptoInfoVo.getMission();
                    ptwUtilService.modifyHomepageTodoName( sptoInfoVo.getSheetNo(), todoNameString );
                }
            }
        } else {
            //退回-审批
            LOG.info( "-------------web层：回退的标准操作票再次审批开始,标准操作票编号：" + code + "-------------" );
            String[] params = new String[] { "mission", "equipment", "type","operItemRemarks" };
            sptoInfoVo.setModifydate( new Date() );
            sptoInfoVo.setModifyuser( userInfoScope.getUserId() );
            sptoInfoDao.updateSptoInfo( sptoInfoVo, params );
            //修改待办里的名称
            String todoNameString = sptoInfoVo.getMission().length() > 100 ? sptoInfoVo.getMission().substring(
                    0, 100 ) :  sptoInfoVo.getMission();
            ptwUtilService.modifyHomepageTodoName( sptoInfoVo.getSheetNo(), todoNameString );
            
            // 处理操作项
            sptoInfoDao.deleteSptoItem( sptoInfoVo.getId() );
            int index = 1;
            for ( PtoOperItem sptoItem : sptoInfoVo.getPtoOperItemList() ) {
                sptoItem.setId( null );
                sptoItem.setShowOrder( index );
                sptoItem.setPtoId( sptoInfoVo.getId() );
                sptoInfoDao.insertSptoItem( sptoItem );
                index++;
            }
            // 先删掉所有相关的附件数据
            attachMatchService.deleteAttachMatch( id, null, "SPTO" );
            // 插入附件的相关数据
            attachMatchService.insertAttachMatch( id, uploadIds, "SPTO", "new" );
            LOG.info( "-------------web层：回退的标准操作票再次审批完成,标准操作票编号：" + code + "-------------" );
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( procinstId );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get( 0 );
            taskId = task.getId();
        }
        resultMap.put( "taskId", taskId );
        resultMap.put( "procinstId", procinstId );
        resultMap.put( "id", id );
        resultMap.put( "sheetNo",  sptoInfoVo.getSheetNo());
        resultMap.put( "status", status );
        resultMap.put( "code", code );
        return resultMap;
    }

    /**
     * @Title:startWorkFlow
     * @Description:启动流程
     * @param sptoInfo
     * @return void
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private Map<String, Object> startWorkFlow(SptoInfoVo sptoInfoVo, UserInfoScope userInfoScope)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String defkey = workflowService.queryForLatestProcessDefKey( "ptw_core_spto" );// 获取审批标准操作票的最新流程定义版本
        if("modifyCommit".equals( sptoInfoVo.getCommitStyle() )){
            defkey = workflowService.queryForLatestProcessDefKey( "ptw_core_chgspto" );// 获取修改标准操作票的最新流程定义版本
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "businessId", sptoInfoVo.getId() );
        // 启动新流程
        LOG.info( "-------------标准操作票ID：" + sptoInfoVo.getId() + "启动----------------------" );
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                userInfoScope.getUserId(), map );
        LOG.info( "-------------标准操作票ID：" + sptoInfoVo.getId() + "结束----------------------" );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();
        // 加入待办列表
       // String flowCode = sptoInfoVo.getCode();
        String flowCode = sptoInfoVo.getSheetNo();
        String id = sptoInfoVo.getId();
        String jumpPath = "ptw/sptoInfo/todolistTOSptoPage.do?id=" + id;
        // 构建待办参数Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );
        homeworkTask.setName( sptoInfoVo.getMission().length() > 30 ? sptoInfoVo.getMission().substring( 0, 30 ) : sptoInfoVo
                .getMission() ); // 名称
        homeworkTask.setProcessInstId( processInstId );// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "新建标准操作票记录" ); // 状态
        homeworkTask.setTypeName( "标准操作票" ); // 类别
        if("modifyCommit".equals( sptoInfoVo.getCommitStyle() )){
            homeworkTask.setStatusName( "新建修改标准操作票记录" ); // 状态
            homeworkTask.setTypeName( "修改标准操作票" ); // 类别
        }
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

    /**
     * @Title:querySptoInfo
     * @Description:查询标准操作票列表
     * @param Page
     * @return Page<SptoInfo>
     * @throws Exception
     */
    @Override
    public Page<SptoInfo> querySptoInfo(Page<SptoInfo> page) throws Exception {
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
        page.setParameter( "nowTime", new Date());
        page.setParameter( "writeStatus", "write" );
        page.setParameter( "auditStatus", "audit" );
        page.setParameter( "permitStatus", "permit" );
        page.setParameter( "preauditStatus", "preaudit" );
        
        page.setParameter( "statusCon1", "draft" );
        page.setParameter( "statusCon2", "zuofei" );
        page.setParameter( "isDraft", "N" );
        page.setParameter( "delFlag", "N" );
        
        List<SptoInfo> list = sptoInfoDao.querySptoInfo( page );
        page.setResults( list );
        LOG.info( "查询标准操作票列表信息" );
        return page;
    }

    /**
     * @Title:querySptoInfoById
     * @Description:按ID查询标准操作票
     * @param id
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> querySptoInfoById(String id) {
        List<PtoOperItem> sptoItems = new ArrayList<PtoOperItem>( 0 );
        sptoItems = sptoInfoDao.querySptoItemsBySptoId( id );
        Map<String, String> statusCon = new HashMap<String, String>(0);
        statusCon.put( "writeStatus", "write" );
        statusCon.put( "auditStatus", "audit" );
        statusCon.put( "permitStatus", "permit" );
        statusCon.put( "preauditStatus", "preaudit" );
        SptoInfoVo sptoInfo = sptoInfoDao.querySptoInfoById( id, statusCon);
        String status = sptoInfo.getStatus();
        String taskId = "";
        List<String> candidateUsers = new ArrayList<String>();
        if ( status != null && !"draft".equals( status ) && !"zuofei".equals( status ) && !"closed".equals( status ) && !"passed".equals( status )
                && StringUtils.isNotEmpty( sptoInfo.getProcinstId() ) ) {
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( sptoInfo.getProcinstId() );
            Task task = activities.get( 0 );
            taskId = task.getId();
            // 获取节点的候选人
            candidateUsers = workflowService.getCandidateUsers( taskId );
        }
        List<PtoAttachment> attachList = attachMatchService.queryPtoAttachmentById( sptoInfo.getId(), "SPTO" );
        ArrayList<String> aList = new ArrayList<String>();
        for ( int i = 0; i < attachList.size(); i++ ) {
            aList.add( attachList.get( i ).getAttachId() );
        }
        List<Map<String, Object>> attachmentMap = FileUploadUtil.getJsonFileList( Constant.basePath, aList );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "bean", sptoInfo );
        map.put( "list", sptoItems );
        map.put( "taskId", taskId );
        map.put( "candidateUsers", candidateUsers );
        map.put( "attachmentMap", attachmentMap );
        return map;
    }

    /**
     * @Title:deleteSptoInfo
     * @Description:按id删除标准操作票
     * @param id
     * @return 
     * @throws 
     */
    @Override
    public void deleteSptoInfo(String id) {
        LOG.info( "-------------删除标准操作票，标准操作票ID：" + id + "----------------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, String> statusCon = new HashMap<String, String>(0);
        statusCon.put( "writeStatus", "write" );
        statusCon.put( "auditStatus", "audit" );
        statusCon.put( "permitStatus", "permit" );
        statusCon.put( "preauditStatus", "preaudit" );
        SptoInfoVo sptoInfoVo = sptoInfoDao.querySptoInfoById( id,statusCon );
        String[] params = new String[] { "delFlag" };
        sptoInfoVo.setDelFlag( "Y" );
        sptoInfoDao.updateSptoInfo( sptoInfoVo, params );
        homepageService.Delete( sptoInfoVo.getSheetNo(), userInfoScope );// 删除首页草稿
    }

    /**
     * @Title:obsoleteSptoInfo
     * @Description:按id作废标准操作票
     * @param id
     * @return 
     * @throws 
     */
    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void obsoleteSptoInfo(String id) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        Map<String, String> statusCon = new HashMap<String, String>(0);
        statusCon.put( "writeStatus", "write" );
        statusCon.put( "auditStatus", "audit" );
        statusCon.put( "permitStatus", "permit" );
        statusCon.put( "preauditStatus", "preaudit" );
        SptoInfoVo sptoInfoVo = sptoInfoDao.querySptoInfoById( id,statusCon );
        String workflowId = sptoInfoVo.getProcinstId();
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( workflowId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        String taskId = task.getId();
        // 修改标准操作票状态
        LOG.info( "-------------作废标准操作票处理开始,标准操作票ID：" + id );
        // 终止流程
        workflowService.stopProcess( taskId, userId, userId, "作废" );
        sptoInfoVo.setStatus( "zuofei" );
        String[] params = new String[] { "status" };
        sptoInfoDao.updateSptoInfo( sptoInfoVo, params );
        // 删掉对应的待办
        homepageService.complete( workflowId, userInfoScope, "已作废" );
    }
    

    @Override
    public Page<SptoInfoVo> querySptoInfoByCode(Page<SptoInfoVo> page) {
        List<SptoInfoVo> list = sptoInfoDao.querySptoInfoByCode( page );
        page.setResults( list );
        LOG.info( "根据标准操作票编号查询标准操作票列表信息" );
        return page;
    }

    @Override
    public int updateValidTime(String id, Date beginTime, Date endTime) {
        int result = sptoInfoDao.updateValidTime( id, beginTime, endTime );
        return result;
    }

    @Override
    public boolean checkValidTimeData(String sptoId,String sptoCode, Date beginTime, Date endTime) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        List<SptoInfoVo> list = sptoInfoDao.querySptoInfoListByCode(sptoId,siteid, sptoCode );
        if(list.size() > 0){
           long newBeginTime = beginTime.getTime();
           long newEndTime = endTime.getTime();
           for ( SptoInfoVo oldSpto : list ) {
               long oldBeginTime = oldSpto.getBeginTime().getTime();
               long oldEndTime = oldSpto.getEndTime().getTime();
               if(sptoId==null || !sptoId.equals( oldSpto.getId() )){
                   boolean flag1 = (oldBeginTime <= newBeginTime && newBeginTime <= oldEndTime)
                           || (oldBeginTime <= newEndTime && newEndTime <= oldEndTime);
                   boolean flag2 = (newBeginTime <= oldBeginTime && oldBeginTime <= newEndTime)
                           ||(newBeginTime <= oldEndTime && oldEndTime <= newEndTime);
                   if(flag1 || flag2){
                       return false;
                   } 
               }
              
           }
            
        }
        return true;
    }

    @Override
    public Page<SptoInfoVo> querySameCodeSptoList(Page<SptoInfoVo> page) {
        List<SptoInfoVo> list = sptoInfoDao.querySameCodeSptoListByCode(page );
        page.setResults( list );
        return page;
    }
}
