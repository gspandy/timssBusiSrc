package com.timss.attendance.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.bean.OvertimeFileBean;
import com.timss.attendance.bean.OvertimeItemBean;
import com.timss.attendance.dao.OvertimeDao;
import com.timss.attendance.service.OvertimeService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.timss.attendance.util.VOUtil;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.timss.attendance.vo.OvertimePageVo;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.ScheduleDetailService;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.support.IDateManager;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: 加班申请
 * @description: {desc}
 * @company: gdyd
 * @className: OvertimeServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年8月28日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("overtimeService")
@Transactional(propagation = Propagation.SUPPORTS)
public class OvertimeServiceImpl implements OvertimeService {

    private Logger log = LoggerFactory.getLogger( OvertimeServiceImpl.class );

    @Autowired
    private OvertimeDao overtimeDao;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private HomepageService homepageService;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private AtdUserPrivUtil privUtil;
    
    private Map<String, DutyPersonShiftVo>dutyPersonShiftMap;//人员的排班情况
    @Autowired
    private DutyService dutyService;
    @Autowired
    private ScheduleDetailService scheduleDetailService;
    @Autowired
    private IDateManager iDateManager;
    /**
     * @description:插入加班申请附件
     * @author: fengzt
     * @createDate: 2014年9月10日
     * @param id
     * @param fileIds
     * @return:
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private int insertOvertimeFile(int id, String fileIds) {
        if ( StringUtils.isBlank( fileIds ) ) {
            return 0;
        }

        String[] fileArr = fileIds.split( "," );/*
        List<OvertimeFileBean> fileBeans = new ArrayList<OvertimeFileBean>();
        for ( String fileId : fileArr ) {
            OvertimeFileBean vo = new OvertimeFileBean();
            vo.setFileId( fileId );
            vo.setOvertimeId( id );
            fileBeans.add( vo );
        }*/
        int count = overtimeDao.insertBatchOvertimeFile( id,fileArr );
        // 设置状态表示不删除附件
        attachmentMapper.setAttachmentsBinded( fileArr, 1 );
        return count;
    }

    /**
     * @description:设置明细表bean
     * @author: fengzt
     * @createDate: 2014年9月10日
     * @param vo
     * @return:OvertimeItemBean
     */
    private OvertimeItemBean setOvertimeItemBean(OvertimePageVo vo) {
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();

        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        String deptId = userInfoScope.getOrgId();
        String deptName = userInfoScope.getOrgName();
        OvertimeItemBean overtimeItemBean = new OvertimeItemBean();

        overtimeItemBean.setDeptId( deptId );
        overtimeItemBean.setDeptName( deptName );
        overtimeItemBean.setUserId( userId );
        overtimeItemBean.setUserName( userName );

        overtimeItemBean.setPlanOverHours( vo.getPlanOverHours() );
        //overtimeItemBean.setRealOverHours( vo.getPlanOverHours() );
        String startDateStr = vo.getStartDate();
        String endDateStr = vo.getEndDate();

        Date startDate = DateFormatUtil.parseDate( startDateStr, "yyyy-MM-dd HH:mm" );
        Date endDate = DateFormatUtil.parseDate( endDateStr, "yyyy-MM-dd HH:mm" );
        overtimeItemBean.setStartDate( startDate );
        overtimeItemBean.setEndDate( endDate );

        return overtimeItemBean;
    }
    
    @Override
    public OvertimeBean queryOvertimeBeanById(Integer id){
    	OvertimeBean overtimeBean = overtimeDao.queryOvertimeByIdWithItems(id);
    	return overtimeBean;
    }
    
    /**
     * @description:通过Id查询加班申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param id
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> queryOvertimeById(int id) {
        OvertimeBean overtimeBean = queryOvertimeBeanById(id);
        
        // 当前登录人
        String currentUser = privUtil.getUserInfoScope().getUserId();
        List<Task> activities = new ArrayList<Task>();
        if( StringUtils.isNotBlank( overtimeBean.getInstantId() ) ){
            activities = workflowService.getActiveTasks( overtimeBean.getInstantId() );
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "userId", currentUser );
        
        map.put( "rowData", overtimeBean );
        if ( !activities.isEmpty() ) {
            Task task = activities.get( 0 );
            map.put( "taskId", task.getId() );

            // 拿到审批人的列表
            List<String> userList = workflowService.getCandidateUsers( task.getId() );
           

            // 判断是否具有审批状态
            String applyFlag = null;
            if ( userList.contains( currentUser ) ) {
                applyFlag = "approver";
            } else {
                applyFlag = "others";
            }

            map.put( "applyFlag", applyFlag );
        }
      
        return map;
    }

    /**
     * @description:更新加班申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateOvertime(OvertimeBean vo) {
        return overtimeDao.updateOvertime( vo );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
	public Boolean updateAuditStatus(Integer overtimeId, String instanceId,
			String status) {
		HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", status);
        parmas.put( "instanceId", instanceId);
        parmas.put( "id", overtimeId );
        parmas.put( "updateDate", new Date() );
		return overtimeDao.updateOperUserById(parmas)>0;
	}
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Integer updateOvertimeCreateDay(OvertimeBean bean){
    	Date currentDate = new Date();
		bean.setUpdateBy( privUtil.getUserInfoScope().getUserId() );
    	bean.setUpdateDate(currentDate);
    	if(bean.getCreateDay()==null){
    		bean.setCreateDay(currentDate);
    	}
    	
    	//更新主表
    	return overtimeDao.updateOvertimeCreateDay( bean );
    }
    
    /**
     * @description:通过站点拿到所有加班申请信息
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @return:List<OvertimeBean>
     */
    @Override
    public List<OvertimeBean> queryOvertimeBySiteId(Page<HashMap<?, ?>> pageVo) {
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        String siteId = userInfo.getSiteId();
        pageVo.setParameter( "siteId", siteId );
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );

        if ( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ) {
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        } else {
            pageVo.setSortKey( "num" );
            pageVo.setSortOrder( "DESC" );
        }

        String userId = userInfo.getUserId();
        String deptId = userInfo.getOrgId();
        pageVo.setParameter( "deptId", deptId );
        pageVo.setParameter( "userId", userId );
        pageVo.setParameter( "status", ProcessStatusUtil.CAOGAO );

        List<OvertimeBean> result = new ArrayList<OvertimeBean>();

        result = overtimeDao.queryOvertimeBySiteId( pageVo );
        return result;
    }

    /**
     * @description:高级搜索（也分站点）
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param map
     * @param pageVo
     * @return:List<OvertimeBean>
     */
    @Override
    public List<OvertimeBean> queryOvertimeBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );

        if ( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ) {
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        } else {
            pageVo.setSortKey( "num" );
            pageVo.setSortOrder( "DESC" );
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

        List<OvertimeBean> result = new ArrayList<OvertimeBean>();

        result = overtimeDao.queryOvertimeBySearch( pageVo );
        return result;
    }
    

    /**
     * @description:终止流程
     * @author: fengzt
     * @createDate: 2014年9月3日
     * @param processInstId
     * @param reason
     * @param businessId
     * @return:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteFlowOvertime(String taskId, String assignee, String owner, String message, String businessId) {
        workflowService.stopProcess( taskId, assignee, owner, message );

        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.END );
        parmas.put( "id", businessId );
        int count = overtimeDao.updateOperUserById( parmas );
        return count;
    }

    /**
     * @description:查找附件
     * @author: fengzt
     * @createDate: 2014年9月11日
     * @param leaveId
     * @return:ArrayList<HashMap<String,Object>>
     */
    @Override
    public List<Map<String, Object>> queryFileByOvertimeId(String overtimeId) {
        List<OvertimeFileBean> ovList = overtimeDao.queryFileByOvertimeId( overtimeId );
        if ( !ovList.isEmpty() ) {
            ArrayList<String> ids = new ArrayList<String>();
            for ( OvertimeFileBean vo : ovList ) {
                ids.add( vo.getFileId() );
            }
            // 文件附件MAP
            List<Map<String,Object>> fileMap = FileUploadUtil.getJsonFileList( Constant.basePath, ids );
            return fileMap;
        }
        return null;
    }

    /**
     * @description:更新加班申请
     * @author: fengzt
     * @createDate: 2014年9月11日
     * @param parmas
     * @return:int
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateOperUserById(HashMap<String, Object> parmas) {

        return overtimeDao.updateOperUserById( parmas );
    }

    /**
     * 
     * @description:草稿模式编辑更新
     * @author: fengzt
     * @createDate: 2014年9月11日
     * @param vo
     * @param fileIds
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateOvertimeByCg(OvertimePageVo vo, String fileIds) {
        //更新主表
        OvertimeBean overtimeBean = new OvertimeBean();
        overtimeBean.setUpdateBy( privUtil.getUserInfoScope().getUserId() );
        overtimeBean.setUpdateDate( new Date() );
        overtimeBean.setOverTimeReason( vo.getOverTimeReason() );
        overtimeBean.setId( vo.getId() );
        overtimeDao.updateOvertime( overtimeBean );
        
        int delCount = overtimeDao.deleteOvertimeItem( vo.getId() );
        int file = overtimeDao.deleteOvertimeFile( vo.getId() );
        
        int addFileCount = insertOvertimeFile( vo.getId(), fileIds );
        // 设置加班申请明细
        OvertimeItemBean overtimeItemBean = setOvertimeItemBean( vo );
        // 插入明细
        overtimeItemBean.setOvertimeId( vo.getId() );
        int itemCount = overtimeDao.insertOvertimeItem( overtimeItemBean );
        
        log.info( "删除item = " + delCount + " ;删除file=" +  file + "; 增加附件=" + addFileCount + "; 增加明细=" + itemCount); 
        return 1;
    }

    /**
     * 
     * @description:通过num拿到ID
     * @author: fengzt
     * @createDate: 2014年10月16日
     * @param flowNo
     * @param siteId
     * @return:int
     */
    @Override
    public int queryIdByFlowNo(String flowNo, String siteId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "num", flowNo );
        map.put( "siteId", siteId );
        OvertimeBean vo = overtimeDao.queryOvertimeByNum( map );
        return vo.getId();
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
    public int deleteOvertime(int id) {
        OvertimeBean bean = overtimeDao.queryOvertimeById( id );
        //删除首页草稿
        homepageService.Delete( bean.getNum(), privUtil.getUserInfoScope() ); 
        
        int fileDelCount = overtimeDao.deleteOvertimeFile( id );
        int delItemCount = overtimeDao.deleteOvertimeItem( id );
        int count = overtimeDao.deleteOvertimeById( id );
        log.info( "流水号：" + bean.getNum() + "--Id = " + id + "--删除子项：" 
                + delItemCount + "条--删除基本信息：" + count + "条--删除文件：" + fileDelCount );
        return count;
    }

    /**
     * 
     * @description:作废 
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int invalidOvertime(int id) {
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.INVALID );
        parmas.put( "id", id );
        int count = overtimeDao.updateOperUserById(parmas);
        
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        OvertimeBean bean = overtimeDao.queryOvertimeById( id );
        String userId = userInfo.getUserId();
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

	@Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> saveOvertime(OvertimeBean bean){
    	Boolean isExist=bean.getId()>0;//之前已暂存过了
    	Boolean result=insertOrUpdateOvertime(bean)>0;
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	if(result){//更新成功
    		UserInfoScope userInfo = privUtil.getUserInfoScope();
    		map=queryOvertimeById(bean.getId());
    		bean=(OvertimeBean)map.get("rowData");
    		
    		if(StringUtils.isBlank(bean.getInstantId())){//没有启动流程的时候，需要更新草稿内容，否则是退回情况，不用更新草稿
    			if(isExist){
    				homepageService.Delete( bean.getNum(), userInfo );//先删除草稿
    			}
    			
    			String flowCode = bean.getNum();
            //    String jumpPath = "page/attendance/core/checkin/Overtime-updateOvertime.jsp?id=" + bean.getId();
                String jumpPath = "attendance/overtime/updateOvertimePage.do?id="+ bean.getId();
                String reason = bean.getOverTimeReason();
                if( StringUtils.isNotBlank( reason ) && reason.length() > 50 ){
                    reason = reason.substring( 0, 47 ) + "...";
                }
                // 构建Bean
                HomepageWorkTask homeworkTask = new HomepageWorkTask();
                homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
                homeworkTask.setTypeName("加班申请");// 名称
                homeworkTask.setProcessInstId(flowCode); // 草稿时流程实例ID可以不用设置
                homeworkTask.setStatusName( ProcessStatusUtil.CAOGAO ); // 状态
                homeworkTask.setType(HomepageWorkTask.TaskType.Draft); // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
                homeworkTask.setName( reason ); // 类别
                homeworkTask.setUrl(jumpPath);// 扭转的URL
                homepageService.create( homeworkTask, userInfo ); // 调用接口创建草稿
    		}else{
    			homepageService.modify(null, bean.getNum(), null, bean.getOverTimeReason().length()>50?bean.getOverTimeReason().substring(0, 47):bean.getOverTimeReason(), null, null, null, null);
    		}
    		map.put( "result", "success" );
    	}else{
    		map.put( "result", "fail" );
    	}
        
        return map;
    }
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> submitOvertime(OvertimeBean bean){
    	Boolean isExist=bean.getId()>0;//之前已暂存过了
    	Boolean result=insertOrUpdateOvertime(bean)>0;
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	if(result){//更新成功
    		UserInfoScope userInfo = privUtil.getUserInfoScope();
    		map=queryOvertimeById(bean.getId());
    		bean=(OvertimeBean)map.get("rowData");
    		
    		// 启动流程
            String processKey = "atd_" + userInfo.getSiteId().toLowerCase() + "_overtime";
            // 获取最新流程定义版本
            String defkey = workflowService.queryForLatestProcessDefKey( processKey );
            map.put( "businessId", bean.getId() );
            ProcessInstance processInstance = null;
            try {
                processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey, userInfo.getUserId(), map );
            } catch (Exception e) {
                log.error( e.getMessage(), e );
            }
            // 获取流程实例ID
            String processInstId = processInstance.getProcessInstanceId();
            map.put( "processInstId", processInstId );
            
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( processInstId );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get( 0 );
            map.put( "taskId", task.getId() );
            
            //加入或更新待办
            String flowCode = bean.getNum();
            if(isExist){//之前已存在要删除先
            	homepageService.Delete( flowCode, userInfo );
            }
            // 加入待办列表
            String reason = bean.getOverTimeReason();
            if( StringUtils.isNotBlank( reason ) && reason.length() > 50 ){
                reason = reason.substring( 0, 47 ) + "...";
            }
           // String jumpPath = "page/attendance/core/checkin/Overtime-updateOvertime.jsp?id=" + bean.getId();
            String jumpPath = "attendance/overtime/updateOvertimePage.do?id="+ bean.getId();
            homepageService.createProcess( flowCode, processInstId, "加班申请", reason,
            		task.getName(), jumpPath, userInfo, null );

            map.put("result", "success");
    	}else{
    		map.put("result", "fail");
    	}
    	
        return map;
    }
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Integer insertOrUpdateOvertime(OvertimeBean bean){
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	Date currentDate = new Date();
    	int count=0;
    	int fileCount = 0;//插入附件
    	String msg="";//结果信息
    	
        if(bean.getId()>0){//新建过了，更新
        	bean.setUpdateBy( userInfo.getUserId() );
        	bean.setUpdateDate(currentDate);
        	//更新主表
        	count=overtimeDao.updateOvertime( bean );
        	
            //更新详情项
    		int addItemCount = (bean.getAddItemList()!=null&&bean.getAddItemList().size()>0)?
    				overtimeDao.insertBatchOvertimeItem( bean.getId(),bean.getAddItemList() ):0;
			int updateItemCount = (bean.getUpdateItemList()!=null&&bean.getUpdateItemList().size()>0)?
					overtimeDao.updateBatchOvertimeItem( bean.getUpdateItemList() ):0;    		
			int delItemCount = (bean.getDelItemList()!=null&&bean.getDelItemList().size()>0)?
					overtimeDao.deleteBatchOvertimeItem( bean.getDelItemList() ):0;
        	
        	//删除并重新插入附件
            int fileDel = overtimeDao.deleteOvertimeFile( bean.getId() );
            
            msg+="更新加班"+bean.getId()+"="+count+"条;新建详情="+addItemCount+"条;更新详情="+updateItemCount+"条;删除详情="+delItemCount+"条;"
            		+ "删除附件="+fileDel+"条;";
        }else{//新建
        	bean.setCreateBy( userInfo.getUserId() );
        	bean.setUserName(userInfo.getUserName());
        	bean.setDeptId(userInfo.getOrgId());
        	bean.setDeptName(userInfo.getOrgName());
        	bean.setCreateDate( currentDate );
        	//bean.setCreateDay( currentDate );//提交的时候才插入
        	bean.setSiteId( userInfo.getSiteId() );
        	bean.setStatus( ProcessStatusUtil.CAOGAO );
        	// 插入主表
            count = overtimeDao.insertOvertime( bean );
            // 插入明细
            int itemCount = overtimeDao.insertBatchOvertimeItem( bean.getId(),bean.getAddItemList() );
            
            msg+="插入加班申请"+bean.getId()+"=" + count + "条; 插入明细 = " + itemCount + " 条; ";
        }
        
        if(bean.getFileIds()!=null&&bean.getFileIds().length>0){
        	fileCount = overtimeDao.insertBatchOvertimeFile( bean.getId(),bean.getFileIds() );
            // 设置状态表示不删除附件
            attachmentMapper.setAttachmentsBinded( bean.getFileIds(), 1 );
        }
        msg+="插入附件="+fileCount+"条";
        log.info(msg);
        return count;
    }
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Integer updateOvertimeRealOverHours(OvertimeBean bean){
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	Date currentDate = new Date();
    	int count=0;
    	bean.setUpdateBy( userInfo.getUserId() );
    	bean.setUpdateDate(currentDate);
    	
    	int updateItemCount = (bean.getUpdateItemList()!=null&&bean.getUpdateItemList().size()>0)?
				overtimeDao.updateBatchOvertimeItemRealOverHours( bean.getUpdateItemList() ):0; 
				
		//更新主表
		count=overtimeDao.updateOvertime( bean );		
		log.info( "核定时长，更新加班"+bean.getId()+"="+count+"条;更新详情="+updateItemCount+"条;");		
    	
        return count;
    }
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Integer updateOvertimeTransferCompensate(OvertimeBean bean,Boolean isFromRealOverHours){
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	Date currentDate = new Date();
    	int count=0;
    	bean.setUpdateBy( userInfo.getUserId() );
    	bean.setUpdateDate(currentDate);
    	
    	int updateItemCount = isFromRealOverHours?
    			overtimeDao.updateOvertimeItemTransferCompensateByRealOverHours(bean.getId()):(
    					(bean.getUpdateItemList()!=null&&bean.getUpdateItemList().size()>0)?
    							overtimeDao.updateBatchOvertimeItemTransferCompensate( bean.getUpdateItemList() ):0
				); 
				
		//更新主表
		count=overtimeDao.updateOvertime( bean );		
		log.info( "核定转补休时长，更新加班"+bean.getId()+"="+count+"条;更新详情="+updateItemCount+"条;isFromRealOverHours:"+isFromRealOverHours);		
    	
        return count;
    }
    
    @Override
    public OvertimeBean convertBean( String formData, String fileIds, String addRows, String delRows, String updateRows ){
    	OvertimeBean bean=VOUtil.fromJsonToVoUtil( formData, OvertimeBean.class );
    	bean.setAddItemList(VOUtil.fromJsonToListObject(addRows, OvertimeItemBean.class));
    	bean.setDelItemList(VOUtil.fromJsonToListObject(delRows, OvertimeItemBean.class));
    	bean.setUpdateItemList(VOUtil.fromJsonToListObject(updateRows, OvertimeItemBean.class));
    	if ( !StringUtils.isBlank( fileIds ) ) {
    		String[] fileArr = fileIds.split( "," );
            bean.setFileIds(fileArr);
    	}
    	return bean;
    }

	@Override
	public Double queryOvertimeTotalHoursByMonth(String userId, String siteId,
			String monthStr) {
		String[]statusList=new String[]{"已归档"};
		double num=overtimeDao.queryOvertimeTotalHoursByMonth(userId,siteId,monthStr,statusList);
		return num;
	}

	@Override
	public List<LeaveContainItemVo> queryOvertimeByDiffDay(String siteId,
			String[] userIds, String startTimeStr, String endTimeStr) {
		Map<String, Object> params = new HashMap<String, Object>();
        Date startTime = DateFormatUtil.parseDate( startTimeStr, "yyyy-MM-dd HH:mm" );
        Date endTime = DateFormatUtil.parseDate( endTimeStr, "yyyy-MM-dd HH:mm" );
        
        params.put( "startDate", startTime );
        params.put( "endDate", endTime );
        params.put( "siteId", siteId );
        params.put( "userIds", userIds );
        params.put( "status", "('" + ProcessStatusUtil.CLOSED + "')" );
        
        return overtimeDao.queryOvertimeByDiffDay( params );
	}

	@Override
	public Double countOvertimeHours(DefinitionBean definitionBean,
			String userId, Boolean isOpr, Date startDate, Date endDate) throws Exception {
		Double hours=0.0;//计算结果
		if(isOpr){
			//开始时间前一天
			Date prevDate=DateFormatUtil.addDate(startDate, "d", -1);
			String prevDateStr=DateFormatUtil.dateToString(prevDate, "yyyy-MM-dd");
			//结束时间后一天
			Date nextDate=DateFormatUtil.addDate(endDate, "d", 1);
			String nextDateStr=DateFormatUtil.dateToString(nextDate, "yyyy-MM-dd");
			//dutyPersonShiftMap=dutyService.queryDutyPersonAndShiftBySiteAndTime(definitionBean.getSiteId(),userId, prevDateStr, nextDateStr);
			dutyPersonShiftMap=scheduleDetailService.querySchedulePersonAndShiftBySiteAndTime(definitionBean.getSiteId(),new String[]{userId}, prevDateStr, nextDateStr);
		}
		
		//分天计算每天
		Date startTemp=DateFormatUtil.getFormatDate(startDate,"yyyy-MM-dd");
        Date endTemp=DateFormatUtil.getFormatDate(endDate,"yyyy-MM-dd");
        int diffDays=(int)DateFormatUtil.dateDiff("dd",startTemp,endTemp);
        for(int i=0;i<=diffDays;i++){
        	Date temp = DateFormatUtil.addDate( startTemp, "d", i );
        	//对当天的影响
        	hours+=countOvertimeHoursInOneDay(definitionBean, temp, (i==0?startDate:null),(i==diffDays?endDate:null), userId, isOpr);
        }
        
        if(isOpr){
			dutyPersonShiftMap=null;
		}
		return hours;
	}
	/**
	 * 计算一天内的实际加班时长（去除应上班时间）
	 * 行政人员周六日和节假日算1天加班，工作日取系统定义上下班时间做为工作时间
	 * 运行人员取排班的上下班时间做为工作时间
	 * @param definitionBean
	 * @param checkDay
	 * @param startTime
	 * @param endTime
	 * @param userId
	 * @param isOpr
	 * @return
	 * @throws Exception
	 */
	private Double countOvertimeHoursInOneDay(DefinitionBean definitionBean,Date checkDay,Date startTime,Date endTime,
			String userId,Boolean isOpr) throws Exception{
		Double hours=0.0;//计算结果
		String checkDayStr=DateFormatUtil.dateToString( checkDay, "yyyy-MM-dd");
		String prevDayStr=DateFormatUtil.dateToString( DateFormatUtil.addDate(checkDay, "d", -1), "yyyy-MM-dd");
		String nextDayStr=DateFormatUtil.dateToString( DateFormatUtil.addDate(checkDay, "d", 1), "yyyy-MM-dd");
		//当天的加班开始和结束时间
        Date start=startTime==null?DateFormatUtil.parseDate(checkDayStr+" 0000","yyyy-MM-dd HHmm"):startTime;
        Date end=endTime==null?DateFormatUtil.parseDate(nextDayStr+" 0000","yyyy-MM-dd HHmm"):endTime;
        if(DateFormatUtil.compareDateNoEqual(end, start)){//加班开始时间<加班结束时间
        	hours=DateFormatUtil.dateDiff("hh", start, end);//加班时间段时长
        	if(isOpr){//运行人员需要去拿排班信息和班次时间
            	hours-=getWorkHoursInOvertimeForOpr(userId,checkDayStr,start,end);//减掉加班时间内今天班次的工作时长
            	hours-=getWorkHoursInOvertimeForOpr(userId,prevDayStr,start,end);//班次可能跨天，减掉加班时间内昨天天班次的工作时长
    		}else{//非运行人员拿系统定义的上下班时间
    			//如果是周六日和节假日，计算一天，如果是工作日，减掉上班时间
    			if(iDateManager.checkIsWorkDate(checkDay,definitionBean.getSiteId())){
    				hours-=getWorkHoursInOvertime(definitionBean,checkDayStr,start,end);//减掉加班时间内今天上班的工作时长
    			}
    		}
        }
		return hours;
	}
	/**
	 * 找到运行加班时间内的工作时间，加班时长应减去这部分时间
	 * @param shiftVo
	 * @param checkDayStr
	 * @param start 加班时间
	 * @param end
	 */
	private Double getWorkHoursInOvertimeForOpr(String userId,String checkDayStr,Date start,Date end){
		Double hours=0.0;
		String flag=checkDayStr+"_"+userId;
		DutyPersonShiftVo shiftVo=dutyPersonShiftMap.get(flag);
		if(shiftVo!=null&&!"rest".equals(shiftVo.getShiftType())){//有排班
			Date shiftStartDate = DateFormatUtil.parseDate( checkDayStr + " " + shiftVo.getStartTime(), "yyyy-MM-dd HHmm" );
			Date shiftEndDate=DateFormatUtil.addDate(shiftStartDate, "H", shiftVo.getLongTime());
			hours+=countWorkHoursInOvertime(shiftStartDate,shiftEndDate,start,end);
		}
		return hours;
	}
	/**
	 * 找到加班时间内的工作时间，加班时长应减去这部分时间
	 * @param definitionBean
	 * @param checkDayStr
	 * @param start 加班时间
	 * @param end
	 * @return
	 */
	private Double getWorkHoursInOvertime(DefinitionBean definitionBean,String checkDayStr,Date start,Date end){
		Double hours=0.0;
		//获得工作时间
		String morning = definitionBean.getForeStartDate();
        String forenoon = definitionBean.getForeEndDate();
        String noon = definitionBean.getAfterStartDate();
        String afternoon = definitionBean.getAfterEndDate();
        Date morningDate = DateFormatUtil.parseDate( checkDayStr + " " + morning, "yyyy-MM-dd HHmm" ); 
        Date forenoonDate = DateFormatUtil.parseDate( checkDayStr + " " + forenoon, "yyyy-MM-dd HHmm" ); 
        Date noonDate = DateFormatUtil.parseDate( checkDayStr + " " + noon, "yyyy-MM-dd HHmm" ); 
        Date afternoonDate = DateFormatUtil.parseDate( checkDayStr + " " + afternoon, "yyyy-MM-dd HHmm" );
        hours+=countWorkHoursInOvertime(morningDate,forenoonDate,start,end);//上午上班
        hours+=countWorkHoursInOvertime(noonDate,afternoonDate,start,end);//下午上班
        return hours;
	}
	/**
	 * 计算加班时间内的原工作时长，即俩时间段的交集的长度
	 * @param workStart
	 * @param workEnd
	 * @param overtimeStart
	 * @param overtimeEnd
	 * @return
	 */
	private Double countWorkHoursInOvertime(Date workStart,Date workEnd,Date overtimeStart,Date overtimeEnd){
		Double hours=0.0;
		//没有交集
    	if(DateFormatUtil.compareDateNoEqual(workStart, overtimeEnd)||DateFormatUtil.compareDateNoEqual(overtimeStart, workEnd)){
    		
    	}else{
    		//找到小的时间范围就是交集
    		Date maxStartDate=DateFormatUtil.compareDateNoEqual(overtimeStart,workStart)?overtimeStart:workStart;
    		Date minEndDate=DateFormatUtil.compareDateNoEqual(workEnd, overtimeEnd)?overtimeEnd:workEnd;
    		hours=DateFormatUtil.dateDiff("hh", maxStartDate, minEndDate);
    	}
    	return hours;
	}
}
