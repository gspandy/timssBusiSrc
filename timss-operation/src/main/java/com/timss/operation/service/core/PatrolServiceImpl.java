package com.timss.operation.service.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.homepage.service.HomepageService;
import com.timss.operation.bean.Patrol;
import com.timss.operation.dao.PatrolDao;
import com.timss.operation.service.PatrolService;
import com.timss.operation.util.ProcessStatusUtil;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: 点检日志service Implements
 * @description: 
 * @company: gdyd
 * @className: PatrolServiceImpl.java
 * @author: fengtw
 * @createDate: 2015年10月29日
 * @updateUser: fengtw
 * @version: 1.0
 */
@Service("patrolService")
@Transactional(propagation=Propagation.SUPPORTS)
public class PatrolServiceImpl implements PatrolService {
    private Logger log = Logger.getLogger( PatrolServiceImpl.class );
    
    @Autowired
    private PatrolDao patrolDao;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private HomepageService homepageService;
    
    /**
     * 
     * @description:插入/更新点检日志
     * @author: fengtw
     * @createDate: 2015年11月2日
     * @param formData
     * @return:Map<String, Object> 
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertOrUpdatePatrol(String formData) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Patrol patrolBean = JsonHelper.fromJsonStringToBean( formData, Patrol.class );
        
        //插入或更新日志信息
        int count = 0;
        if(StringUtils.isBlank(patrolBean.getPatrolId())){  //只有在第一次新建时才可能insert 此时没有流程实例id
            patrolBean.setPatrolId(UUIDGenerator.getUUID());
        	patrolBean.setIsCancel("N");
        	patrolBean.setIsDelete("N");
        	patrolBean.setStatus( ProcessStatusUtil.DRAFT_STR );//业务数据状态
        	patrolBean.setDeptid( userInfoScope.getOrgId() );
        	patrolBean.setSiteid( userInfoScope.getSiteId() );
        	patrolBean.setCreateuser(userInfoScope.getUserId());
        	patrolBean.setCreateUserName(userInfoScope.getUserName());
        	patrolBean.setCreatedate( new Date() );
            count = patrolDao.insertPatrol( patrolBean );//bean中自动包含了seqNum
        }
        else{
        	patrolBean.setModifyuser(userInfoScope.getUserId());
        	patrolBean.setModifyUserName(userInfoScope.getUserName());
        	patrolBean.setModifydate( new Date() );
        	count = patrolDao.updatePatrol( patrolBean );
            patrolBean = patrolDao.queryPatrolById( patrolBean.getPatrolId() );//重新查询获取完整bean 拿到seqNum
        }

        //第一次暂存则需要创建草稿待办 后面退回再暂存不需要再创建草稿待办
        if(StringUtils.isBlank( patrolBean.getInstantId() )){   //有流程id说明是被退回，此时暂存就不用创建草稿待办了，否则需要创建或更新
            //加入待办-草稿 列表
            //生成待办标题
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        	String checkDate = format.format(patrolBean.getCheckDate());
        	String speciality = ProcessStatusUtil.getEnumName(itcMvcService, "OPR_SPECIALITY", patrolBean.getSpeciality()); 
        	String title = checkDate + " " + speciality + "专业点检日志";
        	
            HomepageWorkTask homeworkTask = new HomepageWorkTask();
            String flowCode = patrolBean.getSeqNum();
            String jumpPath = "operation/patrol/updatePatrolPage.do?patrolId=" + patrolBean.getPatrolId() + "&fromDraftBox=true";
            homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
            //homeworkTask.setProcessInstId(""); // 草稿时流程实例ID可以不用设置
            homeworkTask.setTypeName("点检日志");// 类别名称
            homeworkTask.setName(title); // 任务名称
            homeworkTask.setStatusName(ProcessStatusUtil.getEnumName(itcMvcService, "OPR_PATROL_STATUS", ProcessStatusUtil.DRAFT_STR) ); // 流程状态名称
            homeworkTask.setUrl(jumpPath);//跳转的URL
            homeworkTask.setType(HomepageWorkTask.TaskType.Draft); // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
            homepageService.create( homeworkTask, userInfoScope ); // 调用接口创建草稿 	
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "count", count );
        map.put( "patrolBean", patrolBean );
        return map;
    }
 
    /**
     * 
     * @description:提交点检日志
     * @author: fengtw
     * @createDate: 2015年11月3日
     * @param abnormityBean
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertPatrol( String formData ) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Patrol patrolBean = JsonHelper.fromJsonStringToBean( formData, Patrol.class );
        log.info(  "页面传入json ==== " + formData );
        
        //插入或更新日志信息
        int count = 0;
        if(StringUtils.isBlank(patrolBean.getPatrolId())){
            patrolBean.setPatrolId(UUIDGenerator.getUUID());
        	patrolBean.setIsCancel("N");
        	patrolBean.setIsDelete("N");
        	patrolBean.setStatus( ProcessStatusUtil.APPLY_STR );//业务数据状态，后面流程启动也会在init中设置为APPLY_STR一次
        	patrolBean.setDeptid( userInfoScope.getOrgId() );
        	patrolBean.setSiteid( userInfoScope.getSiteId() );
        	patrolBean.setCreateuser(userInfoScope.getUserId());
        	patrolBean.setCreateUserName(userInfoScope.getUserName());
        	patrolBean.setCreatedate( new Date() );
            count = patrolDao.insertPatrol( patrolBean );
        }
        else{
        	patrolBean.setModifyuser(userInfoScope.getUserId());
        	patrolBean.setModifyUserName(userInfoScope.getUserName());
        	patrolBean.setModifydate( new Date() );
        	count = patrolDao.updatePatrol( patrolBean );
            patrolBean = patrolDao.queryPatrolById( patrolBean.getPatrolId() );
        }
        
        //获取最新流程定义版本
        String processKey = "operation_" + userInfoScope.getSiteId().toLowerCase() + "_patrol";
        String defkey = workflowService.queryForLatestProcessDefKey( processKey );
        log.info( "processKey = " + processKey + "----- defkey = "  + defkey + " ----patrolBean.toString = " + patrolBean.toString() );
       
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "businessId", patrolBean.getPatrolId() );
        ProcessInstance processInstance;
        try {
            //启动流程  获取流程实例ID
            processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey, userInfoScope.getUserId(), map);
            String processInstId = processInstance.getProcessInstanceId();
            map.put( "processInstId", processInstId );
            log.info( "流程实例ID processInstId=" + processInstId + " --- businessId = " +  patrolBean.getPatrolId() );
            
            //生成待办标题
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        	String checkDate = format.format(patrolBean.getCheckDate());
        	String speciality = ProcessStatusUtil.getEnumName(itcMvcService, "OPR_SPECIALITY", patrolBean.getSpeciality()); 
        	String title = checkDate + " " + speciality + "专业点检日志";
        	
            //加入待办-草稿 列表
            HomepageWorkTask homeworkTask = new HomepageWorkTask();
            String flowCode = patrolBean.getSeqNum();
            String jumpPath = "operation/patrol/updatePatrolPage.do?patrolId=" + patrolBean.getPatrolId() + "&fromDraftBox=true";
            homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
            homeworkTask.setProcessInstId(processInstId); // 草稿时流程实例ID可以不用设置
            homeworkTask.setTypeName("点检日志");// 类别名称
            homeworkTask.setName(title); // 任务名称（点检情况）
            homeworkTask.setStatusName( ProcessStatusUtil.getEnumName(itcMvcService, "OPR_PATROL_STATUS", ProcessStatusUtil.APPLY_STR) ); // 流程状态名称
            homeworkTask.setUrl(jumpPath);//跳转的URL
            homeworkTask.setType(HomepageWorkTask.TaskType.Process); // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
            homepageService.create( homeworkTask, userInfoScope ); // 调用接口创建草稿 	
            
            //获取当前活动节点   刚启动流程，第一个活动节点肯定是属于当前登录人的
            List<Task> activities = workflowService.getActiveTasks(processInstId);
            Task task = activities.get(0);
            map.put( "taskId", task.getId() );
        }catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return map;
    }
    
    /**
     * @description:通过ID 删除点检日志
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param : patrolId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deletePatrolById(String patrolId) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        //删除草稿待办
   	 	Patrol patrolBean = patrolDao.queryPatrolById( patrolId );
        homepageService.Delete( patrolBean.getSeqNum(), userInfoScope ); 
        //删除日志信息
        int count = patrolDao.deletePatrolById( patrolId );
        return count;
    }

    /**
     * @description:更新点检日志表
     * @author: fengtw
     * @createDate: 2015年10月30日
     * @param : patrol
     * @return int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updatePatrol(Patrol patrol) {
        return patrolDao.updatePatrol( patrol );
    }

    /**
     * @description:根据Id拿到点检日志表
     * @author: fengtw
     * @createDate: 2015年11月4日
     * @param id
     * @return:Patrol
     */
    public Map<String, Object> queryPatrolById(String patrolId) {
         UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	 Patrol patrolBean = patrolDao.queryPatrolById( patrolId );
         
         //当前登录人
         String currentUser = userInfoScope.getUserId();
         List<Task> activities = new ArrayList<Task>();
         if( StringUtils.isNotBlank( patrolBean.getInstantId() ) ){
             activities = workflowService.getActiveTasks( patrolBean.getInstantId() );
         }
         Map<String, Object> map = new HashMap<String, Object>();
         map.put( "userId", currentUser );
         
         map.put( "rowData", patrolBean );
         if( !activities.isEmpty() ){
             Task task = activities.get(0);
             map.put( "taskId", task.getId() );
             
             //拿到审批人的列表
             List<String> userList = workflowService.getCandidateUsers( task.getId() );
             
             //判断是否具有审批状态
             String applyFlag = null;
             if( userList.contains( currentUser )){
                 applyFlag ="approver";
             }else{
                 applyFlag = "others";
             }
             
             map.put( "applyFlag", applyFlag );
         }
         
         return map;
    }

    /**
     * @description:查询某个站点下的所有点检日志
     * @author: fengtw
     * @createDate: 2015年11月2日
     * @param map HashMap
     * @param page HashMap
     * @return:List<Patrol>
     */
	@Override
	public List<Patrol> queryPatrolByPage(Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
        	pageVo.setSortKey( sort );
        	pageVo.setSortOrder( order );
        }else{
        	pageVo.setSortKey( "checkdate desc, createdate" );
        	pageVo.setSortOrder( "DESC" );
        }
        //用户登录的站点
        String siteId = null;
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        siteId = userInfoScope.getSiteId();
        pageVo.setParameter( "siteid", siteId );
        //按站点搜索
        List<Patrol> patrolList = patrolDao.queryPatrolByPage( pageVo );
        return patrolList;
	}
	
    /**
     * @description:点检日志列表 高级搜索
     * @author: fengtw
     * @createDate: 日期
     * @param map HashMap
     * @param page HashMap
     * @return:List<Patrol>
     */
    public List<Patrol> queryPatrolBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
        	pageVo.setSortKey( sort );
        	pageVo.setSortOrder( order );
        }else{
        	pageVo.setSortKey( "checkdate desc, createdate" );
        	pageVo.setSortOrder( "DESC" );
        }
        //用户登录的站点
        String siteId = null;
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        siteId =  userInfoScope.getSiteId();
        pageVo.setParameter( "siteid", siteId );
        
        if(map!=null){
        	pageVo.setParams( map );
        }
        //条件搜索
        List<Patrol> patrolList = patrolDao.queryPatrolBySearch( pageVo );
        return patrolList;
    }

    /**
     * 
     * @description:作废流程
     * @author: fengtw
     * @createDate: 2015年11月4日
     * @param id
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int invalidPatrol(String patrolId) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String statusName = ProcessStatusUtil.getEnumName(itcMvcService, "OPR_PATROL_STATUS", ProcessStatusUtil.INVALID_STR);
        
        //更新点检日志状态信息
        Patrol patrolBean = patrolDao.queryPatrolById( patrolId );
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "instantId", patrolBean.getInstantId());
        parmas.put( "status", ProcessStatusUtil.INVALID_STR );//业务数据状态更新为已作废
        parmas.put( "patrolId", patrolId );
        int count = patrolDao.updatePatrolStatus(parmas);
        
        //获取流程实例id  终止流程
        String userId = userInfoScope.getUserId();
        String instantId = patrolBean.getInstantId();
        log.info( "流程instantId = " + instantId + "---流水号为： " + patrolBean.getSeqNum() );
        List<Task> activities = workflowService.getActiveTasks( instantId );
        Task task = activities.get(0);
        String taskId = task.getId();
        workflowService.stopProcess( taskId, userId, userId, statusName );
        
        //删除待办
        homepageService.complete( instantId, userInfoScope, statusName );//流程状态为作废
        return count;
    } 

}