package com.timss.ptw.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.service.AssetInfoService;
import com.timss.ptw.bean.PtwStandardBean;
import com.timss.ptw.bean.PtwStdSafeBean;
import com.timss.ptw.dao.PtwStandardDao;
import com.timss.ptw.service.PtwStandardService;
import com.timss.ptw.vo.VOUtil;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: 标准工作票
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStandardServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年8月28日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("ptwStandardService")
@Transactional(propagation = Propagation.SUPPORTS)
public class PtwStandardServiceImpl implements PtwStandardService {

    private Logger log = LoggerFactory.getLogger( PtwStandardServiceImpl.class );

    @Autowired
    private PtwStandardDao ptwStandardDao;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private HomepageService homepageService;

    @Autowired
    private AttachmentMapper attachmentMapper;
    
    @Autowired
    @Qualifier("assetInfoServiceImpl")
    private AssetInfoService assetInfoService;

    /**
     * @description:拿到登录用户信息
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    private UserInfoScope getUserInfoScope() {
        UserInfoScope userInfoScope = new UserInfoScopeImpl();
        try {
            userInfoScope = itcMvcService.getUserInfoScopeDatas();
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return userInfoScope;
    }


    /**
     * @description:通过站点拿到所有标准工作票信息
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @return:List<PtwStandardBean>
     */
    @Override
    public List<PtwStandardBean> queryPtwStandardBySiteId(Page<HashMap<?, ?>> pageVo) {
        String siteId = getUserInfoScope().getSiteId();
        pageVo.setParameter( "siteId", siteId );
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );

        if ( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ) {
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        } else {
            pageVo.setSortKey( "MODIFYDATE" );
            pageVo.setSortOrder( "DESC" );
        }

//        pageVo.setParameter( "isCancel", 1 );
        pageVo.setParameter( "inUse", 1 );
        pageVo.setParameter( "isExpire", 1 );
        pageVo.setParameter( "flowStatus", siteId.toLowerCase() + "_flow_std_status_0" );
        return ptwStandardDao.queryPtwStandardBySiteId( pageVo );
    }

    /**
     * @description:高级搜索（也分站点）
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param map
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    @Override
    public List<PtwStandardBean> queryPtwStandardBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );

        if ( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ) {
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        } else {
            pageVo.setSortKey( "wtNo" );
            pageVo.setSortOrder( "DESC" );
        }

        pageVo.setParams( map );

        String siteId = getUserInfoScope().getSiteId();
        pageVo.setParameter( "siteId", siteId );
        
//        pageVo.setParameter( "isCancel", 1 );
        pageVo.setParameter( "inUse", 1 );
        pageVo.setParameter( "isExpire", 1 );
        pageVo.setParameter( "flowStatus", siteId.toLowerCase() + "_flow_std_status_0" );

        return ptwStandardDao.queryPtwStandardBySearch( pageVo );
    }


    /**
     * 
     * @description:暂存 不包括流程
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param formData
     * @param safeItems
     * @return:Map
     * @throws JSONException 
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertOrUpdatePtwStandard(String formData, String safeItems) {
        PtwStandardBean bean = JsonHelper.fromJsonStringToBean( formData, PtwStandardBean.class );
       
        List<PtwStdSafeBean> items = VOUtil.fromJsonToListObject( safeItems, PtwStdSafeBean.class );
        
        //是否已经存在草稿代办 
        boolean daibanFlag = false;
        if( StringUtils.isNotBlank( bean.getId() ) ){
            daibanFlag = true;
        }
        
        int count = 0;
        if( StringUtils.isNotBlank( bean.getId() ) ){
            //update
            count = updatePtwStandard( bean, items );
        }else{
            count = insertPtwStandard( bean, items );
        }
        
        //重新插入或更新过的信息
        bean = ptwStandardDao.queryPtwStandardById( bean.getId() );
        items = ptwStandardDao.queryPtwStdSafeByWtId( bean.getId() );
        
        //已经暂存过 退回就不用删除草稿代办
        if( daibanFlag && StringUtils.isBlank( bean.getInstantId() ) ){
            //删除草稿 
            homepageService.Delete( bean.getSheetNo(), getUserInfoScope() );
        }
        
        //退回暂存 不用创建草稿待办
        if( StringUtils.isBlank( bean.getInstantId() )){
            //加入待办-草稿 列表
            String flowCode = bean.getSheetNo();
            
            String jumpPath = "ptw/ptwStandard/updatePtwStandardMenu.do?id=" + bean.getId();
            // 构建Bean
            HomepageWorkTask homeworkTask = new HomepageWorkTask();
            homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
            homeworkTask.setTypeName("标准工作票");// 名称
            homeworkTask.setProcessInstId(flowCode); // 草稿时流程实例ID可以不用设置
            homeworkTask.setStatusName( "草稿" ); // 状态
            homeworkTask.setType(HomepageWorkTask.TaskType.Draft); // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
            homeworkTask.setName( bean.getWorkContent() ); // 类别
            homeworkTask.setUrl(jumpPath);// 扭转的URL
            homepageService.create( homeworkTask, getUserInfoScope() ); // 调用接口创建草稿
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        if ( count > 0 ) {
            map.put( "result", "success" );
            map.put( "bean", bean );
            map.put( "items", items );
        } else {
            map.put( "result", "fail" );
        }
        
        return map;
    }

    /**
     * 
     * @description:插入标准工作票
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param bean
     * @param items
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertPtwStandard(PtwStandardBean bean, List<PtwStdSafeBean> items) {
        //设置标准工作票的基础信息
        bean = setPtwStdBaseInfo( bean );
        
        int baseCount = ptwStandardDao.insertPtwStdBaseInfo( bean );
        
        items = setStdSafeItems( bean, items );
        int insertItemCount = ptwStandardDao.insertBacthPtwStdSafe( items );
        log.info( "标准工作票插入：" + baseCount + " ----子项插入：" + insertItemCount );
        
        return baseCount;
    }


    /**
     * 
     * @description:设置标准工作票的基本信息
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param bean
     * @return:PtwStandardBean
     */
    private PtwStandardBean setPtwStdBaseInfo(PtwStandardBean bean) {
        //设置ID
        bean.setId( UUIDGenerator.getUUID() );
        bean.setCreateuser( getUserInfoScope().getUserId() );
        bean.setCreateUserName( getUserInfoScope().getUserName() );
        
        bean.setCreatedate( new Date() );
        bean.setDeptid( getUserInfoScope().getOrgId() );
        String siteId = getUserInfoScope().getSiteId();
        bean.setSiteid( siteId );
        
       
        bean.setFlowStatus( siteId.toLowerCase() + "_flow_std_status_0" );
        
        bean.setIsCheck( 0 );
        bean.setIsCancel( 1 );
        bean.setIsExpire( 1 );
        bean.setInUse( 1 );
        
        if( StringUtils.isBlank( bean.getEqNo() )){
            bean.setEqName( null );
        }
            
        return bean;
    }


    /**
     * 
     * @description:更新标准工作票
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param bean
     * @param items
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updatePtwStandard(PtwStandardBean bean, List<PtwStdSafeBean> items) {
        bean.setModifyuser( getUserInfoScope().getUserId() );
        bean.setModifyUserName( getUserInfoScope().getUserName() );
        bean.setModifydate( new Date() );
        if( StringUtils.isBlank( bean.getEqNo() )){
            bean.setEqName( null );
        }
        //更新标准工作票基本信息
        int updateCount = ptwStandardDao.updatePtwStandard( bean );
        
        //先删除后插入
        int delCount = ptwStandardDao.deletePtwStdSafeByWtId( bean.getId() );
        
        items = setStdSafeItems( bean, items );
        int insertItemCount = ptwStandardDao.insertBacthPtwStdSafe( items );
        
        log.info( "更新标准工作票基本信息条数：" + updateCount + " ---删除子项：" + delCount + " ----插入子项：" + insertItemCount );
        return insertItemCount;
    }


    /**
     * 
     * @description:设置安全措施
     * @author: fengzt
     * @createDate: 2015年7月20日
     * @param bean
     * @param items
     * @return:
     */
    private List<PtwStdSafeBean> setStdSafeItems(PtwStandardBean bean, List<PtwStdSafeBean> items) {
        String wtId = bean.getId();
        String siteId = getUserInfoScope().getSiteId();
        
        for( PtwStdSafeBean vo : items ){
            //设置id
            vo.setId( UUIDGenerator.getUUID() );
            vo.setWtId( wtId );
            if( StringUtils.isNotBlank( vo.getSafeType() ) && vo.getSafeType().indexOf( "_ptw_safe_type_" ) >= 0 ){
                log.info( vo.getSafeType() );
            }else{
                vo.setSafeType( siteId.toLowerCase() + "_ptw_safe_type_" + vo.getSafeType() );
            }
        }
            
        return items;
    }

    /**
     * 
     * @description:保存 有流程 
     * @author: fengzt
     * @createDate: 2015年7月21日
     * @param formData
     * @param safeItems
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> insertPtwStandardAll(String formData, String safeItems) {
        PtwStandardBean bean = JsonHelper.fromJsonStringToBean( formData, PtwStandardBean.class );
        List<PtwStdSafeBean> items = VOUtil.fromJsonToListObject( safeItems, PtwStdSafeBean.class );
        
        //是否已经存在草稿代办 
        boolean daibanFlag = false;
        if( StringUtils.isNotBlank( bean.getId() ) ){
            daibanFlag = true;
        }
        
        int count = 0;
        if( StringUtils.isNotBlank( bean.getId() ) ){
            //update
            count = updatePtwStandard( bean, items );
        }else{
            count = insertPtwStandard( bean, items );
        }
        
        log.info( "插入或更新条数 = " + count  );
        String siteId = getUserInfoScope().getSiteId();
        //启动流程
        String processKey = "ptw_" + siteId.toLowerCase() + "_std";
        log.info( "---------- flow " + processKey + " start --------" );
        //获取最新流程定义版本
        String defkey = workflowService.queryForLatestProcessDefKey( processKey );
        Map<String, Object> map = new HashMap<String, Object>();
       
        //启动流程
        ProcessInstance processInstance;
        try {
            map.put( "businessId", bean.getId() );
            
            //获取流程实例ID
            processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey, getUserInfoScope().getUserId(), map);
            String processInstId = processInstance.getProcessInstanceId();
            map.put( "processInstId", processInstId );
            bean.setInstantId(processInstId);
            
            if( StringUtils.isNotBlank( bean.getId() ) ){
                String flowCode = bean.getSheetNo();
                if(flowCode == null){
                    flowCode = ptwStandardDao.queryPtwStandardById( bean.getId() ).getSheetNo();
                }
                //提交之前已经暂存过 删除个人草稿代办
                if( daibanFlag ){
                    homepageService.Delete( flowCode, getUserInfoScope() );
                }
                //加入待办列表
                String jumpPath = "ptw/ptwStandard/updatePtwStandardMenu.do?id=" + bean.getId();
                homepageService.createProcess(flowCode, processInstId, "标准工作票", bean.getWorkContent(), 
                        "提出申请", jumpPath, getUserInfoScope(), null);
                
                //获取当前活动节点
                List<Task> activities = workflowService.getActiveTasks(processInstId);
                //刚启动流程，第一个活动节点肯定是属于当前登录人的
                Task task = activities.get(0);
                map.put( "taskId", task.getId() );
            }
        }catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return map;
    }

    /**
     * 
     * @description:通过ID查询标准工作票基本信息
     * @author: fengzt
     * @createDate: 2015年7月21日
     * @param id
     * @return:PtwStandardBean
     */
    @Override
    public PtwStandardBean queryPtwStandardById(String id) {
        if( StringUtils.isNotBlank( id ) ){
            return ptwStandardDao.queryPtwStandardById( id );
        }
        return null;
    }

    /**
     * 
     * @description:通过标准工作票的ID查询安全措施
     * @author: fengzt
     * @createDate: 2015年7月21日
     * @param id
     * @return:List<PtwStdSafeBean>
     */
    @Override
    public List<PtwStdSafeBean> queryPtwStdSafeByWtId(String id) {
        if( StringUtils.isNotBlank( id ) ){
            return ptwStandardDao.queryPtwStdSafeByWtId( id );
        }
        return null;
    }

    /**
     * 
     * @description:更新基础信息
     * @author: fengzt
     * @createDate: 2015年7月22日
     * @param bean
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updatePtwStandardStatus(PtwStandardBean bean) {
        bean.setModifydate( new Date() );
        bean.setModifyuser( getUserInfoScope().getUserId() );
        bean.setModifyUserName( getUserInfoScope().getUserName() );
        
        return ptwStandardDao.updatePtwStandardStatus( bean );
    }

    /**
     * 
     * @description:更新标准工作票的信息 删除、过期、作废
     * @author: fengzt
     * @createDate: 2015年7月22日
     * @param id
     * @param flag
     * @return: int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int deletePtwStandardBaseInfo(String id, String flag) {
        Map<String, Object> map = new HashMap<String, Object>();
        PtwStandardBean bean = queryPtwStandardById( id );
        
        if( "delete".equals( flag ) ){
            map.put( "inUse", 0 );
            //删除首页草稿
            homepageService.Delete( bean.getSheetNo(), getUserInfoScope() ); 
        }else if( "cancel".equals( flag ) ){
            map.put( "isCancel", 0 );
            map.put( "cancelDate", new Date() );
            map.put( "cancerUser", getUserInfoScope().getUserId() );
            map.put( "cancelUserName", getUserInfoScope().getUserName() );
            map.put( "flowStatus", getUserInfoScope().getSiteId().toLowerCase() + "_flow_std_status_5" );
            String userId = getUserInfoScope().getUserId();
            log.info( "流程instantId = " + bean.getInstantId() + "---流水号为： " + bean.getWtNo() );
            
            //获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( bean.getInstantId() );
            //刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get(0);
            String taskId = task.getId();
            
            workflowService.stopProcess( taskId, userId, userId,  "作废" + "。" );
            // 办毕
            homepageService.complete( bean.getInstantId(), getUserInfoScope(), "作废" );
        }else if( "expire".equals( flag ) ){
            map.put( "isExpire", 0 );
        }
        
        map.put( "modifyUser", getUserInfoScope().getUserId() );
        map.put( "modifyUserName", getUserInfoScope().getUserName() );
        map.put( "modifyDate", new Date() );
        map.put( "id", id );
        
        return ptwStandardDao.deletePtwStandardBaseInfo( map );
    }

    /**
     * 
     * @description:查询设备号查询树及其子节点
     * @author: fengzt
     * @createDate: 2015年7月24日
     * @param eqId
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    @Override
    public List<PtwStandardBean> queryPtwStandardByEqId(String eqId, Page<HashMap<?, ?>> pageVo) throws Exception {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );

        if ( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ) {
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        } else {
            pageVo.setSortKey( "wtNo" );
            pageVo.setSortOrder( "DESC" );
        }

        pageVo.setParameter( "eqId", eqId );
        // 如果选中的是根节点，则不过滤
        String siteId = getUserInfoScope().getSiteId();
        Map<String, Object> paramsMap = pageVo.getParams();
        Object selectTreeIdObj = paramsMap.get( "eqId" );
        if ( selectTreeIdObj != null ) {
            String selectTreeId = (String) selectTreeIdObj;
            AssetBean assetBean = assetInfoService.queryAssetTreeRootBySiteId( siteId );
            if ( StringUtils.equals( selectTreeId, assetBean.getAssetId())   ) {
                pageVo.setParameter( "eqId", null );
            }
        }
        pageVo.setParameter( "siteId", siteId );
        
//        pageVo.setParameter( "isCancel", 1 );
        pageVo.setParameter( "inUse", 1 );
        pageVo.setParameter( "isExpire", 1 );
        pageVo.setParameter( "flowStatus", siteId.toLowerCase() + "_flow_std_status_0" );
        

        return ptwStandardDao.queryPtwStandardByEqId( pageVo );
    }

    /**
     * 
     * @description:根据树点击事件查询树及其子节点的标准工作票（已审批通过的）
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param eqId
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    @Override
    public List<PtwStandardBean> queryFinishPtwStandardByEqId(String eqId, Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );

        if ( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ) {
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        } else {
            pageVo.setSortKey( "wtNo" );
            pageVo.setSortOrder( "DESC" );
        }

        pageVo.setParameter( "eqId", eqId );

        String siteId = getUserInfoScope().getSiteId();
        pageVo.setParameter( "siteId", siteId );
        
        pageVo.setParameter( "isCancel", 1 );
        pageVo.setParameter( "inUse", 1 );
        pageVo.setParameter( "isExpire", 1 );
        pageVo.setParameter( "flowStatus", siteId.toLowerCase() + "_flow_std_status_4" );
        

        return ptwStandardDao.queryFinishPtwStandardByEqId( pageVo );
    }

    /**
     * 
     * @description:标题查询（已审批通过的）
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param map
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    @Override
    public List<PtwStandardBean> queryFinishPtwStandardBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );

        if ( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ) {
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        } else {
            pageVo.setSortKey( "wtNo" );
            pageVo.setSortOrder( "DESC" );
        }

        pageVo.setParams( map );
        pageVo.setParameter( "nowTime", new Date() );
        String siteId = getUserInfoScope().getSiteId();
        pageVo.setParameter( "siteId", siteId );
        
        pageVo.setParameter( "isCancel", 1 );
        pageVo.setParameter( "inUse", 1 );
        pageVo.setParameter( "isExpire", 1 );
        pageVo.setParameter( "flowStatus", siteId.toLowerCase() + "_flow_std_status_4" );
       
        return ptwStandardDao.queryFinishPtwStandardBySearch( pageVo );
    }

    /**
     * 
     * @description:按站点查询（已审批通过的）
     * @author: fengzt
     * @createDate: 2015年8月12日
     * @param pageVo
     * @return:List<PtwStandardBean>
     */
    @Override
    public List<PtwStandardBean> queryFinishPtwStandardBySiteId(Page<HashMap<?, ?>> pageVo) {
        String siteId = getUserInfoScope().getSiteId();
        pageVo.setParameter( "siteId", siteId );
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );

        if ( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ) {
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        } else {
            pageVo.setSortKey( "wtNo" );
            pageVo.setSortOrder( "DESC" );
        }

        pageVo.setParameter( "isCancel", 1 );
        pageVo.setParameter( "inUse", 1 );
        pageVo.setParameter( "isExpire", 1 );
        pageVo.setParameter( "flowStatus", siteId.toLowerCase() + "_flow_std_status_4" );
        return ptwStandardDao.queryFinishPtwStandardBySiteId( pageVo );
    }


    @Override
    public Page<PtwStandardBean> querySptoInfoByMultiCode(Page<PtwStandardBean> page) {
        page.setParameter( "isCancel", 1 );
        page.setParameter( "inUse", 1 );
        page.setParameter( "isExpire", 1 );
        List<PtwStandardBean> sptwBeanList = ptwStandardDao.querySptoInfoByMultiCode(page);
        page.setResults( sptwBeanList );
        return page;
    }


    @Override
    public int getMaxVersionByCode(String wtNo, String siteid) {
        return ptwStandardDao.getMaxVersionByCode(wtNo,siteid);
    }


    @Override
    public Page<PtwStandardBean> querySameCodeSptwList(Page<PtwStandardBean> page) {
        List<PtwStandardBean> list = ptwStandardDao.querySameCodeSptwListByCode(page );
        page.setResults( list );
        return page;
    }


    @Override
    public boolean checkValidTimeData(String sptwId, String wtNo, Date beginTime, Date endTime) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        List<PtwStandardBean> list = ptwStandardDao.querySptwInfoListByCode(sptwId,siteid, wtNo );
        if(list.size() > 0){
           long newBeginTime = beginTime.getTime();
           long newEndTime = endTime.getTime();
           for ( PtwStandardBean oldSptw : list ) {
               long oldBeginTime = oldSptw.getBeginTime().getTime();
               long oldEndTime = oldSptw.getEndTime().getTime();
               if(sptwId==null || !sptwId.equals( oldSptw.getId() )){
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
    public int updateValidTime(String id, Date beginTime, Date endTime) {
        int result = ptwStandardDao.updateValidTime( id, beginTime, endTime );
        return result;
    }


    @Override
    public int invalidateOtherSameCodeSptw(String id, String wtNo, String userId, String siteId) {
        return ptwStandardDao.invalidateOtherSameCodeSptw(id,wtNo,userId,siteId,new Date());
    }


}
