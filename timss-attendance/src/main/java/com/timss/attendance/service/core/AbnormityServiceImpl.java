package com.timss.attendance.service.core;

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

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.bean.AbnormityItemBean;
import com.timss.attendance.dao.AbnormityDao;
import com.timss.attendance.service.AbnormityService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.timss.attendance.util.VOUtil;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * 考勤异常
 */
@Service("abnormityService")
@Transactional(propagation=Propagation.SUPPORTS)
public class AbnormityServiceImpl implements AbnormityService {
    
    private Logger log = Logger.getLogger( AbnormityServiceImpl.class );
    
    @Autowired
    private AbnormityDao abnormityDao; 
    
    @Autowired
    private AttachmentMapper attachmentMapper;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private HomepageService homepageService;
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Integer updateAbnormityCreateDay(AbnormityBean bean){
    	Date currentDate = new Date();
		bean.setUpdateBy( privUtil.getUserInfoScope().getUserId() );
    	bean.setUpdateDate(currentDate);
    	if(bean.getCreateDay()==null){
    		bean.setCreateDay(currentDate);
    	}
    	
    	//更新主表
    	return abnormityDao.updateAbnormityCreateDay( bean );
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> saveAbnormity(AbnormityBean bean){
    	Boolean isExist=bean.getId()>0;//之前已暂存过了
    	Boolean result=insertOrUpdateAbnormity(bean)>0;
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	if(result){//更新成功
    		UserInfoScope userInfo = privUtil.getUserInfoScope();
    		map=queryAbnormityById(bean.getId());
    		bean=(AbnormityBean)map.get("rowData");
    		
    		if(StringUtils.isBlank(bean.getInstantId())){//没有启动流程的时候，需要更新草稿内容，否则是退回情况，不用更新草稿
    			if(isExist){
    				homepageService.Delete( bean.getNum(), userInfo );//先删除草稿
    			}
    			
    			String flowCode = bean.getNum();
    			String jumpPath = "attendance/abnormity/detail.do?id=" + bean.getId() + "&mode=edit";
                String reason = bean.getReason();
                if( StringUtils.isNotBlank( reason ) && reason.length() > 50 ){
                	reason = reason.substring( 0, 47 ) + "...";
                }
                // 构建Bean
                HomepageWorkTask homeworkTask = new HomepageWorkTask();
                homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
                homeworkTask.setTypeName("考勤异常");// 名称
                homeworkTask.setProcessInstId(flowCode); // 草稿时流程实例ID可以不用设置
                homeworkTask.setStatusName( ProcessStatusUtil.CAOGAO ); // 状态
                homeworkTask.setType(HomepageWorkTask.TaskType.Draft); // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
                homeworkTask.setName( reason ); // 类别
                homeworkTask.setUrl(jumpPath);// 扭转的URL
                homepageService.create( homeworkTask, userInfo ); // 调用接口创建草稿
    		}
    		map.put( "result", "success" );
    	}else{
    		map.put( "result", "fail" );
    	}
        
        return map;
    }
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> submitAbnormity(AbnormityBean bean){
    	Boolean isExist=bean.getId()>0;//之前已暂存过了
    	Boolean result=insertOrUpdateAbnormity(bean)>0;
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	if(result){//更新成功
    		UserInfoScope userInfo = privUtil.getUserInfoScope();
    		map=queryAbnormityById(bean.getId());
    		bean=(AbnormityBean)map.get("rowData");
    		
    		//启动流程
            String processKey = "atd_" + userInfo.getSiteId().toLowerCase() + "_abnormity";
            //获取最新流程定义版本
            String defkey = workflowService.queryForLatestProcessDefKey( processKey );
            map.put( "businessId", bean.getId() );
            ProcessInstance processInstance = null;
            try {
                processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey, userInfo.getUserId(), map );
            } catch (Exception e) {
                log.error( e.getMessage(), e );
            }
            //获取流程实例ID
            String processInstId = processInstance.getProcessInstanceId();
            map.put( "processInstId", processInstId );
            
            //获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks(processInstId);
            //刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get(0);
            map.put( "taskId", task.getId() );
            
            //加入或更新待办
            String flowCode = bean.getNum();
            if(isExist){//之前已存在要删除先
            	homepageService.Delete( flowCode, userInfo );
            }
            // 加入待办列表
            String reason = bean.getReason();
            if( StringUtils.isNotBlank( reason ) && reason.length() > 50 ){
            	reason = reason.substring( 0, 47 ) + "...";
            }
            String jumpPath = "attendance/abnormity/detail.do?id=" + bean.getId() + "&mode=edit";
            homepageService.createProcess( flowCode, processInstId, "考勤异常", reason,
            		task.getName(), jumpPath, userInfo, null );

            map.put("result", "success");
    	}else{
    		map.put("result", "fail");
    	}
    	
        return map;
    }
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Integer insertOrUpdateAbnormity(AbnormityBean bean){
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	Date currentDate = new Date();
    	int count=0;

    	int fileCount = 0;//插入附件
    	String msg="";//结果信息
        if(bean.getId()>0){//新建过了，更新
        	bean.setUpdateBy( userInfo.getUserId() );
        	bean.setUpdateDate(currentDate);
        	//更新主表
        	count=abnormityDao.updateAbnormity( bean );
        	
            //更新详情项
    		int addItemCount = (bean.getAddItemList()!=null&&bean.getAddItemList().size()>0)?
    				abnormityDao.insertBatchAbnormityItem( bean.getId(),bean.getAddItemList() ):0;
			int updateItemCount = (bean.getUpdateItemList()!=null&&bean.getUpdateItemList().size()>0)?
					abnormityDao.updateBatchAbnormityItem( bean.getUpdateItemList() ):0;    		
			int delItemCount = (bean.getDelItemList()!=null&&bean.getDelItemList().size()>0)?
					abnormityDao.deleteBatchAbnormityItem( bean.getDelItemList() ):0;
        	
        	//删除并重新插入附件
            int fileDel = abnormityDao.deleteFileByAbnormityId( bean.getId() );
            
            msg+="更新考勤异常"+bean.getId()+"="+count+"条;新建详情="+addItemCount+"条;更新详情="+updateItemCount+"条;删除详情="+delItemCount+"条;"
            		+ "删除附件="+fileDel+"条;";
        }else{//新建
        	bean.setCreateBy( userInfo.getUserId() );
        	bean.setUserName(userInfo.getUserName());
        	bean.setDeptId(userInfo.getOrgId());
        	bean.setDeptName(userInfo.getOrgName());
        	bean.setCreateDate( currentDate );
        	bean.setSiteId( userInfo.getSiteId() );
        	bean.setStatus( ProcessStatusUtil.CAOGAO );
        	// 插入主表
            count = abnormityDao.insertAbnormity( bean );
            // 插入明细
            int itemCount = abnormityDao.insertBatchAbnormityItem( bean.getId(),bean.getAddItemList() );
            
            msg+="插入考勤异常"+bean.getId()+"=" + count + "条; 插入明细 = " + itemCount + " 条; ";
        }
        
        if(bean.getFileIds()!=null&&bean.getFileIds().length>0){
        	fileCount = abnormityDao.insertBatchAbnormityFile( bean.getId(),bean.getFileIds() );
            // 设置状态表示不删除附件
            attachmentMapper.setAttachmentsBinded( bean.getFileIds(), 1 );
        }
        msg+="插入附件="+fileCount+"条";
        log.info(msg);
        return count;
    }
    
    @Override
    public List<Map<String, Object>> queryFileByAbnormityId(Integer abnormityId) {
        List<String> ids = abnormityDao.queryFileByAbnormityId( abnormityId );
        if ( ids != null && !ids.isEmpty() ) {
            // 文件附件MAP
            List<Map<String, Object>> fileMap = FileUploadUtil.getJsonFileList( Constant.basePath, ids );
            return fileMap;
        }
        return null;
    }
    
    @Override
    public AbnormityBean queryAbnormityBeanById(int id) {
    	return abnormityDao.queryAbnormityByIdWithItems( id );
    }
    
    /**
     * 
     * @description:通过Id查询考勤异常信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param id
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> queryAbnormityById(int id) {
        AbnormityBean abnormityBean = queryAbnormityBeanById(id);
        
        //当前登录人
        String currentUser = privUtil.getUserInfoScope().getUserId();
        List<Task> activities = new ArrayList<Task>();
        if( StringUtils.isNotBlank( abnormityBean.getInstantId() ) ){
            activities = workflowService.getActiveTasks( abnormityBean.getInstantId() );
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "userId", currentUser );
        
        map.put( "rowData", abnormityBean );
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
     * 
     * @description:更新考勤异常信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    @Override
    public int updateAbnormity(AbnormityBean vo) {
        return abnormityDao.updateAbnormity( vo );
    }


    /**
     * 
     * @description:通过站点拿到所有考勤异常信息
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @return:List<AbnormityBean>
     */
    @Override
    public List<AbnormityBean> queryAbnormityBySiteId( Page<HashMap<?, ?>> pageVo ) {
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        String siteId = userInfo.getSiteId();
        pageVo.setParameter( "siteId", siteId );
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }else{
            pageVo.setSortKey( "num" );
            pageVo.setSortOrder( "DESC" );
        }
        
        String userId = userInfo.getUserId();
        String deptId = userInfo.getOrgId();
        pageVo.setParameter( "deptId", deptId );
        pageVo.setParameter( "userId", userId );
        pageVo.setParameter( "status", ProcessStatusUtil.CAOGAO );
        
        List<AbnormityBean> result = new ArrayList<AbnormityBean>();
        
        result = abnormityDao.queryAbnormityBySiteId( pageVo);
        return  result;
    }

    
    /**
     * 
     * @description:高级搜索（也分站点）
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param map
     * @param pageVo
     * @return:List<AbnormityBean>
     */
    @Override
    public List<AbnormityBean> queryAbnormityBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }else{
            pageVo.setSortKey( "num" );
            pageVo.setSortOrder( "DESC" );
        }
        if(!"null".equals(map.get("_category"))){
        	map.put("category", map.get("_category"));
        }
        pageVo.setParams( map );
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String userId = userInfo.getUserId();
        String deptId = userInfo.getOrgId();
        String siteId = userInfo.getSiteId();
        pageVo.setParameter( "deptId", deptId );
        pageVo.setParameter( "userId", userId );
        pageVo.setParameter( "siteId", siteId );
        pageVo.setParameter( "statusTemp", ProcessStatusUtil.CAOGAO );
        
        List<AbnormityBean> result = new ArrayList<AbnormityBean>();
        
        result = abnormityDao.queryAbnormityBySearch( pageVo);
        return  result;
    }


    /**
     * 
     * @description:终止流程
     * @author: fengzt
     * @createDate: 2014年9月3日
     * @param businessId 
     * @param message 
     * @param owner 
     * @param assignee 
     * @param taskId 
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int deleteFlowAbnormity(String taskId, String assignee, String owner, String message, String businessId) {
        workflowService.stopProcess( taskId, assignee, owner, message );
        
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.END );
        parmas.put( "id", businessId );
        int count = abnormityDao.updateOperUserById(parmas);
        return count;
    }

    /**
     * 
     * @description:通过flowNo 
     * @author: fengzt
     * @createDate: 2014年10月15日
     * @param flowNo
     * @param siteId
     * @return:int
     */
    @Override
    public int queryIdByFlowNo(String flowNo, String siteId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "num", flowNo );
        map.put( "siteId", siteId );
        AbnormityBean vo = abnormityDao.queryAbnormityByNum( map );
        if( vo.getId() > 0 ){
            return vo.getId();
        }
        return 0;
    }


    /**
     * 
     * @description:删除 & 删除代办
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int deleteAbnormity(int id) {
        AbnormityBean bean = abnormityDao.queryAbnormityById( id );
        //删除首页草稿
        homepageService.Delete( bean.getNum(), privUtil.getUserInfoScope() ); 
        int count = abnormityDao.deleteAbnormity( id );
        return count;
    }

    /**
     * 
     * @description:作废 & 删除代办
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int invalidAbnormity(int id) {
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.INVALID );
        parmas.put( "id", id );
        int count = abnormityDao.updateOperUserById(parmas);
        
        AbnormityBean bean = abnormityDao.queryAbnormityById( id );
        String userId =userInfo.getUserId();
        log.info( "流程instantId = " + bean.getInstantId() + "---流水号为： " + bean.getNum() );
        
        //获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( bean.getInstantId() );
        //刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get(0);
        String taskId = task.getId();
        
        workflowService.stopProcess( taskId, userId, userId,  ProcessStatusUtil.INVALID + "。" );
        // 办毕
        homepageService.complete( bean.getInstantId(), userInfo, ProcessStatusUtil.INVALID );
        return count;
    }

    /**
     * 
     * @description:查询开始时间、结束时间内的已归档考勤信息
     * @author: fengzt
     * @createDate: 2015年6月15日
     * @param siteId
     * @param startTimeStr
     * @param endTimeStr
     * @return:List<LeaveContainItemVo>
     * @update:yyn 20160119
     */
    @Override
    public List<LeaveContainItemVo> queryAbnormityByDiffDay(String siteId,String userId, String startTimeStr, String endTimeStr) {
        Map<String, Object> params = new HashMap<String, Object>();
        Date startTime = DateFormatUtil.parseDate( startTimeStr, "yyyy-MM-dd HH:mm" );
        Date endTime = DateFormatUtil.parseDate( endTimeStr, "yyyy-MM-dd HH:mm" );
        
        params.put( "startDate", startTime );
        params.put( "endDate", endTime );
        params.put( "siteId", siteId );
        params.put( "userId", userId );
        params.put( "status", "('" + ProcessStatusUtil.CLOSED + "')" );
        
        return abnormityDao.queryAbnormityByDiffDay( params );
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Boolean updateAuditStatus(Integer abnormityId, String instanceId,
			String status) {
		HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", status);
        parmas.put( "instanceId", instanceId);
        parmas.put( "id", abnormityId );
        parmas.put( "updateDate", new Date() );
		return abnormityDao.updateOperUserById(parmas)>0;
	}
	
	@Override
    public AbnormityBean convertBean( String formData, String fileIds, String addRows, String delRows, String updateRows ){
		AbnormityBean bean=VOUtil.fromJsonToVoUtil( formData, AbnormityBean.class );
    	bean.setAddItemList(VOUtil.fromJsonToListObject(addRows, AbnormityItemBean.class));
    	bean.setDelItemList(VOUtil.fromJsonToListObject(delRows, AbnormityItemBean.class));
    	bean.setUpdateItemList(VOUtil.fromJsonToListObject(updateRows, AbnormityItemBean.class));
    	if ( !StringUtils.isBlank( fileIds ) ) {
    		String[] fileArr = fileIds.split( "," );
            bean.setFileIds(fileArr);
    	}
    	return bean;
    }
}
