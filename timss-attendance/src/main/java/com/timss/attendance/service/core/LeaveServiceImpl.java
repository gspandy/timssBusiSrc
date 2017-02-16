package com.timss.attendance.service.core;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveFileBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.bean.StatBean;
import com.timss.attendance.dao.LeaveDao;
import com.timss.attendance.service.DefinitionService;
import com.timss.attendance.service.LeaveService;
import com.timss.attendance.service.StatService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.timss.attendance.util.VOUtil;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.timss.attendance.vo.LeaveItemVo;
import com.timss.attendance.vo.ShiftOprVo;
import com.timss.attendance.vo.StatVo;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.ScheduleDetailService;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.support.IDateManager;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;

/**
 * 
 * @title: 请假申请
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年9月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("leaveService")
@Transactional(propagation=Propagation.SUPPORTS)
public class LeaveServiceImpl implements LeaveService {
    
    private Logger log = LoggerFactory.getLogger( LeaveServiceImpl.class );

    @Autowired
    private LeaveDao leaveDao; 
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private HomepageService homepageService;
    
    @Autowired
    private AttachmentMapper attachmentMapper;
    
    @Autowired
    private IDateManager iDateManager;
    
    @Autowired
    private DefinitionService definitionService;
    
    @Autowired
    private StatService statService;

    @Autowired
    private AtdUserPrivUtil privUtil;
    
    @Autowired
    private DutyService dutyService;
    @Autowired
    private ScheduleDetailService scheduleDetailService;
    @Autowired
    private SelectUserService selectUserService;
    
    /**
     * 
     * @description:是否是电厂运行班组人员 
     * @remark : 如果是有行政班 在加多一个行政班角色， 用&&判断， 缺点管理员要定期维护行政班人员
     * @author: fengzt
     * @createDate: 2015年3月31日
     * @param userId
     * @return:boolean
     */
    @Override
    public boolean isRoleBanzu( String userId, String siteId ){
        log.info( "userId = " + userId + " --siteId = " + siteId + "-----isRoleBanzu" );
        
        UserInfo userInfo = null;
        try {
            userInfo = itcMvcService.getUserInfo( userId, siteId );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        boolean flag = false;
        if( userInfo != null ){
            List<Role> roles = userInfo.getRoles();
            for( Role role : roles ){
                String roleId = role.getId();
                //班组角色
                if( roleId.equalsIgnoreCase( siteId + "_BZRY" ) ){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
    
    /**
     * 
     * @description:设置leaveBean for insert
     * @author: fengzt
     * @createDate: 2014年9月5日
     * @param leaveBean:
     */
    private void setLeaveBean( LeaveBean leaveBean ){
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        String deptId = userInfoScope.getOrgId();
        String siteId = userInfoScope.getSiteId();
        
        String deptName = userInfoScope.getOrgName();
        Date currentDate = new Date();
        
        leaveBean.setCreateBy( userId );
        leaveBean.setUserName( userName );
        leaveBean.setCreateDate( currentDate );
        leaveBean.setDeptId( deptId );
        
        leaveBean.setDeptName( deptName );
        leaveBean.setUpdateBy( userId );
        leaveBean.setUpdateDate( currentDate );
        leaveBean.setSiteId( siteId );
        leaveBean.setStatus( ProcessStatusUtil.CAOGAO );
    }
    
    /**
     * 
     * @description:请假申请
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param formData
     * @param ids 
     * @param rowData 
     * @return:Map<String, Object>
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public  Map<String, Object> insertLeave(String formData, String rowData, String fileIds ) {
        UserInfoScope userInfoScope = privUtil.getUserInfoScope();
        String siteId = userInfoScope.getSiteId();
        String userId = userInfoScope.getUserId();
        
        //json to bean
        LeaveBean leaveBean = JsonHelper.fromJsonStringToBean( formData, LeaveBean.class );
        //是否已经暂存过
        boolean daibanFlag = false;
        
        if( leaveBean.getId() > 0 ){
            daibanFlag = true;
            updateLeaveForSave( leaveBean, rowData, fileIds ); 
        }else{
            insertLeaveForSave( leaveBean, rowData, fileIds );
        }
        //重新查出leaveBean
        leaveBean = leaveDao.queryLeaveById( leaveBean.getId() );
        
        
        //启动流程
        String processKey = "atd_" + siteId.toLowerCase() + "_leave";
        //获取最新流程定义版本
        String defkey = workflowService.queryForLatestProcessDefKey( processKey );
        Map<String, Object> map = new HashMap<String, Object>();
       
        //启动流程
        ProcessInstance processInstance;
        try {
            map.put( "businessId", leaveBean.getId() );
            
            //获取流程实例ID
            processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,userId,map);
            String processInstId = processInstance.getProcessInstanceId();
            map.put( "processInstId", processInstId );
            leaveBean.setInstantId(processInstId);
            workflowService.setVariable(processInstId, "userId", leaveBean.getCreateBy());
            workflowService.setVariable(processInstId, "orgId", userInfoScope.getOrgId());
            
            if (leaveBean.getSiteId().toLowerCase().equals("hyg")) {
            	//1、判断是否部门经理
            	String applicant=leaveBean.getCreateBy();
            	String orgId=leaveBean.getDeptId();
            	boolean isBmjl = false;
            	
        		//设置是否部门经理，是否综合财务部，请假天数到变亘中去
        		List<SecureUser> users = selectUserService.byRole("HYG_BMJL");
        		if (!users.isEmpty()&&users.size()>0) {
        			for (int i = 0; i < users.size(); i++) {
        				if (applicant.equals(users.get(i).getId())) {
        					isBmjl = true;
        				}
        			}
        		}
            	
            	//判断是否综合财务部 "orgId","deptName"
            	boolean isZhCwb = getOrgCodeAndIdFromGivenName("海运",userInfoScope.getOrgId(),leaveBean.getDeptName().trim(),null);
            	
            	 //取得天数
            	 List<LeaveItemBean> leaveItemBeanList = VOUtil.fromJsonToListObject(rowData, LeaveItemBean.class);
            	 double leaveDays = 0.0;
            	 for (int i = 0; i < leaveItemBeanList.size(); i++) {
            		 leaveDays+=leaveItemBeanList.get(i).getLeaveDays();
				 }
            	 workflowService.setVariable(processInstId, "type", isBmjl?"Y":"N"); 
            	 workflowService.setVariable(processInstId, "zhbtype", isZhCwb?"Y":"N"); 
            	 workflowService.setVariable(processInstId, "leaveDays", leaveDays); 
            	 workflowService.setVariable(processInstId, "regId", orgId);
            }
            
            if( leaveBean.getId() > 0 ){
                String flowCode = leaveBean.getNum();
                
                //提交之前已经暂存过 删除个人草稿代办
                if( daibanFlag ){
                    homepageService.Delete( flowCode, privUtil.getUserInfoScope() );
                }
                //加入待办列表
                String jumpPath = "attendance/leave/updateLeaveToPage.do?id=" + leaveBean.getId();
                homepageService.createProcess(flowCode, processInstId, "请假申请", "请假申请", 
                        ProcessStatusUtil.CAOGAO, jumpPath, userInfoScope, null);
                
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
     * @description:请假附件
     * @author: fengzt
     * @createDate: 2014年9月5日
     * @param leaveBean
     * @param fileIds
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertLeaveFile(LeaveBean leaveBean, String fileIds) {
        if( StringUtils.isBlank( fileIds ) ){
            return 0;
        }
        String[] fileArr = fileIds.split( "," );
        List<LeaveFileBean> leaveFileBeans = new ArrayList<LeaveFileBean>();
        for( String fileId : fileArr ){
            LeaveFileBean vo = new LeaveFileBean();
            vo.setFileId( fileId );
            vo.setLeaveId( leaveBean.getId() );
            leaveFileBeans.add( vo );
        }
        int count = 0;
        if( !leaveFileBeans.isEmpty()  ){
            count = leaveDao.insertBatchLeaveFile( leaveFileBeans );
            //设置状态表示不删除附件
            attachmentMapper.setAttachmentsBinded( fileArr, 1 );
        }
        return count;
    }

    /**
     * 
     * @description:
     * @author: fengzt
     * @createDate: 2014年9月5日
     * @param leaveBean
     * @param leaveItemBeans
     * @return:
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int insertBatchLeaveItem(LeaveBean leaveBean, List<LeaveItemBean> leaveItemBeans) {
        int id = leaveBean.getId();
        for( LeaveItemBean vo : leaveItemBeans ){
            vo.setLeaveId( id );
        }
        int count = leaveDao.insertBatchLeaveItem( leaveItemBeans );
        return count;
    }

    @Override
    public LeaveBean queryLeaveBeanById(int id) {
    	LeaveBean bean=leaveDao.queryLeaveById( id );
    	List<LeaveItemBean>itemList=queryLeaveItemList(id+"");
    	bean.setItemList(itemList);
    	return bean;
    }
    
    /**
     * 
     * @description:通过Id查询请假申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param id
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> queryLeaveById(int id) {
        LeaveBean leaveBean = leaveDao.queryLeaveById( id );
        //当前登录人
        String currentUser = privUtil.getUserInfoScope().getUserId();
        
        List<Task> activities = new ArrayList<Task>();
        if( StringUtils.isNotBlank( leaveBean.getInstantId()  )){
            activities = workflowService.getActiveTasks( leaveBean.getInstantId() );
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "userId", currentUser );
        map.put( "rowData", leaveBean );
        
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
     * @description:更新请假申请信息
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    @Override
    public int updateLeave(LeaveBean vo) {
        return leaveDao.updateLeave( vo );
    }


    /**
     * 
     * @description:通过站点拿到所有请假申请信息
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @return:List<LeaveBean>
     */
    @Override
    public List<LeaveBean> queryLeaveBySiteId( Page<HashMap<?, ?>> pageVo ) {
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
        
        List<LeaveBean> result = new ArrayList<LeaveBean>();
        
        result = leaveDao.queryLeaveBySiteId( pageVo);
        return  result;
    }

    /**
     * 
     * @description:高级搜索（也分站点）
     * @author: fengzt
     * @createDate: 2014年9月2日
     * @param map 
     *          --页面高级搜索字段
     * @param pageVo
     *          --分页参数 pageSize pageNo sort order
     * @return:List<LeaveBean>
     */
    @Override
    public List<LeaveBean> queryLeaveBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }else{
            pageVo.setSortKey( "num" );
            pageVo.setSortOrder( "DESC" );
        }
        
        pageVo.setParams( map );
        
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String userId = userInfo.getUserId();
        String deptId = userInfo.getOrgId();
        String siteId = userInfo.getSiteId();
        pageVo.setParameter( "siteId", siteId );
        pageVo.setParameter( "deptId", deptId );
        pageVo.setParameter( "userId", userId );
        pageVo.setParameter( "statusTemp", ProcessStatusUtil.CAOGAO );
        
        List<LeaveBean> result = new ArrayList<LeaveBean>();
        
        result = leaveDao.queryLeaveBySearch( pageVo);
        return  result;
    }


    /**
     * 
     * @description:终止流程
     * @author: fengzt
     * @createDate: 2014年9月3日
     * @return:
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int deleteFlowLeave(String taskId, String assignee, String owner, String message, String businessId) {
        workflowService.stopProcess( taskId, assignee, owner, message );
        
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.END );
        parmas.put( "id", businessId );
        int count = leaveDao.updateOperUserById(parmas);
        return count;
    }

    /**
     * 
     * @description:通过请假申请查询明细
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @param leaveId
     * @return:List<LeaveItemBean>
     */
    @Override
    public List<LeaveItemBean> queryLeaveItemList(String leaveId) {
        return leaveDao.queryLeaveItemList( leaveId );
    }

    /**
     * 
     * @description:通过请假申请查询附件明细
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @param leaveId
     * @return:List<LeaveItemBean>
     */
    @Override
    public List<Map<String, Object>> queryFileByLeaveId(String leaveId) {
        List<LeaveFileBean> leaveFileBeans = leaveDao.queryFileByLeaveId( leaveId );
        if ( leaveFileBeans != null && !leaveFileBeans.isEmpty() ) {
            ArrayList<String> ids = new ArrayList<String>();
            for ( LeaveFileBean vo : leaveFileBeans ) {
                ids.add( vo.getFileId() );
            }
            // 文件附件MAP
            List<Map<String, Object>> fileMap = FileUploadUtil.getJsonFileList( Constant.basePath, ids );
            return fileMap;
        }
        return null;
    }

    /**
     * 
     * @description:通过请假主表删除附件
     * @author: fengzt
     * @createDate: 2014年9月12日
     * @param leaveId
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int deleteFileByLeaveId(int leaveId) {
        return leaveDao.deleteFileByLeaveId( leaveId );
    }

    /**
     * 
     * @description:剔除假期、周末 计算请假天数
     * @author: fengzt
     * @createDate: 2014年10月9日
     * @param start
     * @param end
     * @param siteId
     * @return:double
     */
    @Override
    public double getLeavesDays(Date start, Date end, String siteId) {
        double countDays = 0D;
        try {
            countDays = calDay( start, end, siteId,false );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return countDays;
    }

    /**
     * 
     * @description:包含节假日 、 周末 （for 婚假、产假）
     * @author: fengzt
     * @createDate: 2014年10月9日
     * @param start
     * @param end
     * @param siteId
     * @return:double
     */
    @Override
    public double getLeavesDaysContainFes(Date start, Date end, String siteId) {
        double countDays = 0D;
        try {
            countDays = calDay( start, end, siteId,true );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return countDays;
    }

    //计算每一天
    private double countLeaveDays( Date start, Date end, String siteId  ) throws Exception{
        //定义规则
        DefinitionBean definitionBean = definitionService.queryDefinitionBySite();
        /*String morning = "0800";
        String forenoon = "1200";
        String noon = "1400";
        String afternoon = "1700";*/
        String morning = definitionBean.getForeStartDate();
        String forenoon = definitionBean.getForeEndDate();
        String noon = definitionBean.getAfterStartDate();
        String afternoon = definitionBean.getAfterEndDate();
        double workTime = 8;
        
        //start 00:00
        Date startTemp = DateFormatUtil.getFormatDate( start, "yyyy-MM-dd" );
        Date endTemp = DateFormatUtil.getFormatDate( end, "yyyy-MM-dd" );
        double sumDay = 0D;
        
        //开始时间跟结束时间都在同一天
        if( startTemp.equals( endTemp ) ){
            Date morningDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( startTemp, "yyyy-MM-dd " ) + morning, "yyyy-MM-dd HHmm" ); 
            Date forenoonDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( startTemp, "yyyy-MM-dd " ) + forenoon, "yyyy-MM-dd HHmm" ); 
            Date noonDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( startTemp, "yyyy-MM-dd " ) + noon, "yyyy-MM-dd HHmm" ); 
            Date afternoonDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( startTemp, "yyyy-MM-dd " ) + afternoon, "yyyy-MM-dd HHmm" ); 
            
            if( DateFormatUtil.compareDate( morningDate, start ) &&  DateFormatUtil.compareDate( end, afternoonDate ) ){
                // start <= 0800 end >= 1700
                sumDay = 1D;
            }else if( DateFormatUtil.compareDate( morningDate, start ) &&  DateFormatUtil.compareDate( forenoonDate, end ) ){
                // start <= 0800 end <= 1200
                //两个时间点相差小时
                sumDay = DateFormatUtil.dateDiff( "hh", morningDate, end ) / workTime;
            }else if( DateFormatUtil.compareDate( morningDate, start ) &&  DateFormatUtil.compareDate( noonDate, end ) ){
                // start <= 0800 end <= 1400
                //两个时间点相差小时
                sumDay = DateFormatUtil.dateDiff( "hh", morningDate, forenoonDate ) / workTime;
            }else if( DateFormatUtil.compareDate( morningDate, start ) &&  DateFormatUtil.compareDate( afternoonDate, end ) ){
                // start <= 0800 end <= 1700
                //上午
                sumDay += DateFormatUtil.dateDiff( "hh", morningDate, forenoonDate ) / workTime;
                //下午
                // = 1700时
                if( afternoonDate.equals( end) ){
                    sumDay += 0.5;
                }else{
                    sumDay += DateFormatUtil.dateDiff( "hh", noonDate, end ) / workTime;
                }
            }else if( DateFormatUtil.compareDateNoEqual( start, morningDate ) &&  DateFormatUtil.compareDate( noonDate, end ) ){
                //start >0800 end <= 1400
                // end <= 1200
                if( DateFormatUtil.compareDate( forenoonDate, end ) ){
                    sumDay = DateFormatUtil.dateDiff( "hh", start, end ) / workTime;
                }else if(  DateFormatUtil.compareDate( forenoonDate, start )  ){
                    // start <= 1200 end > 1200
                    sumDay = DateFormatUtil.dateDiff( "hh", start, forenoonDate ) / workTime;
                }
            }else if( DateFormatUtil.compareDateNoEqual( start, morningDate ) &&  DateFormatUtil.compareDateNoEqual( noonDate, start )
                    && DateFormatUtil.compareDate( afternoonDate, end ) ){
                //start >0800 start < 1400 end <= 1700
                // start <= 1200
                if( DateFormatUtil.compareDate( forenoonDate, start ) ){
                    //上午
                    sumDay = DateFormatUtil.dateDiff( "hh", start, forenoonDate ) / workTime;
                }
                //下午
                // = 1700时
                if( afternoonDate.equals( end) ){
                    sumDay += 0.5;
                }else{
                    sumDay += DateFormatUtil.dateDiff( "hh", noonDate, end ) / workTime;
                }
            }else if( DateFormatUtil.compareDateNoEqual( start, morningDate ) &&  DateFormatUtil.compareDateNoEqual( noonDate, start )
                    && DateFormatUtil.compareDateNoEqual( end, afternoonDate ) ){
                //start >0800 start < 1400 end > 1700
                // start <= 1200
                if( DateFormatUtil.compareDate( forenoonDate, start ) ){
                    //上午
                    sumDay = DateFormatUtil.dateDiff( "hh", start, forenoonDate ) / workTime;
                }
                //下午
                sumDay += 0.5;
            }else if( DateFormatUtil.compareDate( start, forenoonDate ) ){
                //start >= 1200 
                //start <= 1400 
                if( DateFormatUtil.compareDate( noonDate, start ) ){
                    //end < 1700
                    if( DateFormatUtil.compareDateNoEqual( afternoonDate, end ) ){
                        sumDay = DateFormatUtil.dateDiff( "hh", noonDate, end ) / workTime;
                    }else if( DateFormatUtil.compareDate( end, afternoonDate )  ){
                        //end >= 1700
                        sumDay = 0.5;
                    }
                }else if( DateFormatUtil.compareDateNoEqual( start, noonDate ) ){
                    //start > 1400 
                    //start <= 1700 end <= 1700
                    if( DateFormatUtil.compareDate( afternoonDate, start ) && DateFormatUtil.compareDate( afternoonDate, end )  ){
                        sumDay = DateFormatUtil.dateDiff( "hh", start, end ) / workTime;
                    }else if ( DateFormatUtil.compareDate( afternoonDate, start ) && DateFormatUtil.compareDate( end, afternoonDate ) ){
                        //start <= 1700 end >= 1700
                        sumDay = DateFormatUtil.dateDiff( "hh", start, afternoonDate ) / workTime;
                    }
                }
            }
        }
        return sumDay;
    }
    
    //计算时间
    private  double calDay(Date start, Date end, String siteId, boolean isMarray ) throws Exception {
        //定义规则
        DefinitionBean definitionBean = definitionService.queryDefinitionBySite();
        //start 00:00
        Date startTemp = DateFormatUtil.getFormatDate( start, "yyyy-MM-dd" );
        Date endTemp = DateFormatUtil.getFormatDate( end, "yyyy-MM-dd" );
        //下午下班时间
        //String afternoon = "1700";
        String afternoon = definitionBean.getAfterEndDate();
        double sumDay = 0D;
        //是否上班
        boolean workFlag = false;
        
        //开始时间和结束时间
        int diffDays =  (int)DateFormatUtil.dateDiff( "dd", startTemp, endTemp ) ;
        
        
        if( diffDays == 0 ){
            if( isMarray ){
                workFlag = true;
            }else{
                Date dTemp =  DateFormatUtil.addDate( startTemp, "s", 1 );
                log.info( "校验时间为===" + dTemp );
                workFlag = iDateManager.checkIsWorkDate( dTemp, siteId );
            }
            if( workFlag ){
                sumDay = countLeaveDays( start, end, siteId );
            }
        }else{
            for( int i = 0; i <= diffDays; i++ ){
                Date temp = DateFormatUtil.addDate( startTemp, "d", i );
                Date afternoonDate = DateFormatUtil.parseDate( DateFormatUtil.dateToString( temp, "yyyy-MM-dd " ) + afternoon, "yyyy-MM-dd HHmm" ); 
                if( isMarray ){
                    workFlag = true;
                }else{
                    workFlag = iDateManager.checkIsWorkDate(  temp, siteId );
                }
                if( i == 0 ){
                    if( workFlag ){
                        sumDay = countLeaveDays( start, afternoonDate, siteId );
                    }
                }else if( i == diffDays ){
                    if( workFlag ){
                        sumDay += countLeaveDays( temp, end, siteId );
                    }
                }else{
                    if( workFlag ){
                        sumDay += 1D;
                    }
                }
            }
        }
        
        return sumDay;
    }

    /**
     * 
     * @description:插入or更新请假信息
     * @author: fengzt
     * @createDate: 2014年10月14日
     * @param formData
     * @param rowData
     * @param fileIds
     * @return:Map<String, Object> 
     */
    @SuppressWarnings("deprecation")
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertOrUpdateLeave(String formData, String rowData, String fileIds) {
        //json to bean
        LeaveBean leaveBean = JsonHelper.fromJsonStringToBean( formData, LeaveBean.class );
        //是否已经暂存过
        boolean daibanFlag = false;
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        if( leaveBean.getId() > 0 ){
            daibanFlag = true;
            updateLeaveForSave( leaveBean, rowData, fileIds ); 
        }else{
            insertLeaveForSave( leaveBean, rowData, fileIds );
        }
        //重新查出leaveBean
        leaveBean = leaveDao.queryLeaveById( leaveBean.getId() );
        
        //已经暂存过 退回就不用删除草稿代办
        if( daibanFlag && StringUtils.isBlank( leaveBean.getInstantId() ) ){
            //删除草稿 因为草稿中 categoryName 会改变
            homepageService.Delete( leaveBean.getNum(), userInfo );
        }
        
        //退回暂存 不用创建草稿待办
        if( StringUtils.isBlank( leaveBean.getInstantId() )){
            //加入待办-草稿 列表
            String flowCode = leaveBean.getNum();
            String jumpPath = "attendance/leave/updateLeaveToPage.do?id=" + leaveBean.getId();
            
            // 构建Bean
            HomepageWorkTask homeworkTask = new HomepageWorkTask();
            homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
            homeworkTask.setTypeName("请假申请");// 名称
            homeworkTask.setProcessInstId(flowCode); // 草稿时流程实例ID可以不用设置
            homeworkTask.setStatusName( ProcessStatusUtil.CAOGAO ); // 状态
            homeworkTask.setType(HomepageWorkTask.TaskType.Draft); // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
            homeworkTask.setName("请假申请" ); // 类别
            homeworkTask.setUrl(jumpPath);// 扭转的URL
            homepageService.create( homeworkTask, userInfo ); // 调用接口创建草稿
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "rowData", leaveBean );
        if( StringUtils.isNotBlank( leaveBean.getNum() ) ){
            map.put( "count", 1 );
        }
        
        return map;
    }
    /**
     * 
     * @description:插入请假信息(For 暂存)
     * @author: fengzt
     * @createDate: 2014年10月14日
     * @param leaveBean
     * @param rowData
     * @param fileIds:
     */
    private void insertLeaveForSave(LeaveBean leaveBean, String rowData, String fileIds) {
        //datagrid data to list object
        List<LeaveItemVo> leaveItemVos = VOUtil.fromJsonToListObject( rowData, LeaveItemVo.class );
        List<LeaveItemBean> leaveItemBeans = new ArrayList<LeaveItemBean>();
        
        //vo to bean
        StringBuffer categoryListName = new StringBuffer();
        List<AppEnum> emList = itcMvcService.getEnum( "ATD_LEI_CATEGORY" );
        for( LeaveItemVo vo : leaveItemVos ){
            LeaveItemBean leaveItemBean = new LeaveItemBean();
            String startDateStr = vo.getStartDate();
            String endDateStr = vo.getEndDate();
            Date startDate = new Date(Long.parseLong(startDateStr));//DateFormatUtil..parseDate( startDateStr, "yyyy-MM-dd HH:mm" );
            Date endDate = new Date(Long.parseLong(endDateStr));//DateFormatUtil.parseDate( endDateStr, "yyyy-MM-dd HH:mm" );
            
            leaveItemBean.setStartDate( startDate );
            leaveItemBean.setEndDate( endDate );
            leaveItemBean.setLeaveDays( vo.getLeaveDays() );
            leaveItemBean.setCategory( vo.getCategory() );
            leaveItemBeans.add( leaveItemBean );
            String categoryName = getCategoryName( emList, vo.getCategory() ) ;
            categoryListName.append("," + categoryName );
            
        }
        //设置插入
        setLeaveBean( leaveBean );
        
        if( !leaveItemBeans.isEmpty() ){
            leaveBean.setCategory( categoryListName.toString().substring( 1 ) );
            Date startDate = leaveItemBeans.get( 0 ).getStartDate();
            leaveBean.setCreateDay( startDate );
        }
        //请假单插入
        int count = leaveDao.insertLeave( leaveBean );
        //插入明细
        int itemCount = insertBatchLeaveItem( leaveBean, leaveItemBeans );
        //附件
        int fileCount = insertLeaveFile( leaveBean,fileIds );
        
        log.info( "请假单插入" + count +  "请假明细插入： " + itemCount + " 条; 附件插入: "+ fileCount + " 条！" );
    }

    /**
     * 
     * @description:更新请假申请 for暂存
     * @author: fengzt
     * @param fileIds 
     * @param rowData 
     * @createDate: 2014年10月14日
     * @param leaveBean:
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private void updateLeaveForSave(LeaveBean leaveBean, String rowData, String fileIds) {
        // 转化为list<Object>
        List<LeaveItemBean> leaveItemVos = setleaveItemBeans( rowData , leaveBean.getId() );
        leaveBean.setUpdateBy( privUtil.getUserInfoScope().getUserId() );
        leaveBean.setUpdateDate( new Date() );
        
        StringBuffer categoryListName = new StringBuffer();
        List<AppEnum> emList = itcMvcService.getEnum( "ATD_LEI_CATEGORY" );
        if ( !leaveItemVos.isEmpty() ) {
            for( LeaveItemBean vo : leaveItemVos ){
                String categoryName = getCategoryName( emList, vo.getCategory() ) ;
                categoryListName.append("," + categoryName );
            }
            Date startDate = leaveItemVos.get( 0 ).getStartDate();
            leaveBean.setCategory( categoryListName.toString().substring( 1 ) );
            leaveBean.setCreateDay( startDate );
        }

        // update
        int count = leaveDao.updateLeave( leaveBean );
        int fileDelCount = deleteFileByLeaveId( leaveBean.getId() );
        int addFileCount = insertLeaveFile( leaveBean, fileIds );
        
        int delItemCount = leaveDao.deleteLeaveItemByLeaveId( leaveBean.getId() );
        int addItemCount = leaveDao.insertBatchLeaveItem( leaveItemVos );
        log.info( "update leave count= " + count + " delete item count = " + delItemCount + " add item count = "
                + addItemCount + ";fileDelCount= "+ fileDelCount + ";addFileCount=" + addFileCount );
    }
    
    /**
     * 
     * @description:考勤异常枚举code to name
     * @author: fengzt
     * @createDate: 2014年10月11日
     * @param flowCode
     * @return:
     */
    private String getCategoryName(List<AppEnum> emList,String categoryId) {
        String categoryName = categoryId;
        if( StringUtils.isNotBlank( categoryId ) ){
            if( emList != null && emList.size() > 0 ){
                for( AppEnum appVo : emList ){
                    if( categoryName.equalsIgnoreCase( appVo.getCode() ) ){
                        categoryName =  appVo.getLabel();
                        break;
                    }
                }
            }
        }
        return categoryName;
    }

    /**
     * @description:转化为List bean
     * @author: fengzt
     * @createDate: 2014年9月9日
     * @param rowDatas
     * @param leaveId  
     * @return:List<LeaveItemBean>
     */
    private List<LeaveItemBean> setleaveItemBeans( String rowDatas, int leaveId ) {
        List<LeaveItemBean> leaveItemBeans = new ArrayList<LeaveItemBean>();
        try {
            leaveItemBeans = VOUtil.fromJsonToListObject( rowDatas, LeaveItemBean.class );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        } finally {
            if ( leaveItemBeans.isEmpty() ) {
                List<LeaveItemVo> leaveItemVos = VOUtil.fromJsonToListObject( rowDatas, LeaveItemVo.class );

                // vo to bean
                for ( LeaveItemVo vo : leaveItemVos ) {
                    LeaveItemBean leaveItemBean = new LeaveItemBean();
                    String startDateStr = vo.getStartDate();
                    String endDateStr = vo.getEndDate();
                    Date startDate = DateFormatUtil.parseDate( startDateStr, "yyyy-MM-dd hh:mm" );
                    Date endDate = DateFormatUtil.parseDate( endDateStr, "yyyy-MM-dd hh:mm" );

                    leaveItemBean.setStartDate( startDate );
                    leaveItemBean.setEndDate( endDate );
                    leaveItemBean.setLeaveDays( vo.getLeaveDays() );
                    leaveItemBean.setCategory( vo.getCategory() );
                    leaveItemBeans.add( leaveItemBean );
                }
            }
        }
        
        //新增没有父级ID
        if( !leaveItemBeans.isEmpty() ){
            for( LeaveItemBean item : leaveItemBeans ){
                item.setLeaveId( leaveId );
            }
        }

        return leaveItemBeans;
    }

    /**
     * 
     * @description:通过NUM查找Id
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
        LeaveBean leaveBean = leaveDao.queryLeaveByNum( map );
        return leaveBean==null?0:leaveBean.getId();
    }

    private Boolean checkIsOpr(String userId,String oprPersons){
		return oprPersons!=null&&oprPersons.contains(userId+",");
	}
    
    /**
     * 
     * @description:计算请假时间
     * @author: fengzt
     * @createDate: 2014年11月18日
     * @param startDate
     * @param endDate
     * @param category
     * @return:map
     * @throws Exception 
     * @updated:yyn
     */
    @Override
    public Map<String, Object> queryDiffLeaveDay(String startDate, String endDate, String category) throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
        Date start = DateFormatUtil.parseDate( startDate, "yyyy-MM-dd HH:mm" );
        Date end = DateFormatUtil.parseDate( endDate, "yyyy-MM-dd HH:mm" );
        if(StringUtils.isBlank(category)){//请假类型不能为空
        	return map;
        }
        if(DateFormatUtil.compareDateNoEqual(start,end)){
        	map.put( "result", "fail" );
            map.put( "reason", "开始时间必须小于结束时间");
            map.put( "diffDay", "" );
            return map;
        }
        
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String siteId = userInfo.getSiteId();
        String userId=userInfo.getUserId();
        String[] userIds=new String[]{userId};
        Date now=new Date();
        DefinitionBean definitionBean = definitionService.queryDefinitionBySiteId(siteId);
        
        if(DateFormatUtil.compareDateNoEqual(now, end)){//补假的情况
        	//计算是否超过有效补假期限
            //double diff = getLeavesDays( start, now, siteId );
            Double diff= countLeaveDays(null, definitionBean, userId, false, end, now);
            if( diff > definitionBean.getEffectiveDays() ){
                //超出规定日期内补单
                map.put( "result", "fail" );
                map.put( "reason", "超出补假有效期限天数：" + definitionBean.getEffectiveDays() + "天!");
                map.put( "diffDay", "" );
                return map;
            }
        }
        
        String oprPersons=dutyService.queryOprPersonsBySite(siteId);
        Double diffDay=countLeaveDays(category, definitionBean, userId, checkIsOpr(userId,oprPersons), start, end);
        
        int startYear = DateFormatUtil.fromDateToCalendar( start ).get( Calendar.YEAR );
        int endYear = DateFormatUtil.fromDateToCalendar( end ).get( Calendar.YEAR );
        int currentYear = DateFormatUtil.fromDateToCalendar( now ).get( Calendar.YEAR );
       
        //校验规则
        String[]type=category.split("_");
        Integer checkYear=currentYear;
		if(endYear<currentYear){
			checkYear=endYear;
		}else if(currentYear<startYear){
			checkYear=startYear;
		}
		//成功的返回
		map.put( "result", "success" );
        map.put( "diffDay", diffDay );
		Map<String, StatBean>statMap=statService.queryStatMap(siteId, userIds, checkYear, checkYear);
        switch (Integer.parseInt(type[3])) {
			case 1://年假
				if(statMap!=null&&statMap.get(checkYear+"_"+userId)!=null){
					StatBean stat=statMap.get(checkYear+"_"+userId);
					if("ITC".equals(siteId)&&(stat.getEnventLeave()+stat.getSickLeave())>=15){
						map.put( "result", "fail" );
						map.put( "reason", "事假和病假累计15天以上，不能再请年假!");
		                map.put( "diffDay", diffDay );
					}
					if(stat.getSurplusAnnual()<diffDay){
						map.put( "result", "fail" );
						map.put( "reason", "请年假天数超出剩余年休假天数!");
		                map.put( "diffDay", diffDay );
					}
				}else{
					map.put( "result", "fail" );
					map.put( "reason", "没有可用年休假!");
	                map.put( "diffDay", diffDay );
				}
				break;
			case 4://婚假
				if(diffDay>3){
					map.put( "result", "fail" );
	                map.put( "reason", "婚假天数超出3天!");
	                map.put( "diffDay", diffDay );
				}
				break;
			case 6://补休假
				if(!(statMap!=null&&statMap.get(checkYear+"_"+userId)!=null&&statMap.get(checkYear+"_"+userId).getSurplusCompensate()>=diffDay)){
					map.put( "result", "fail" );
	                map.put( "reason", "补休天数超出剩余补休天数!");
	                map.put( "diffDay", diffDay );
				}
				break;
			default:
				break;
		}
        
        /*StatVo vo = statService.queryStatByLogin();
        //补休、年假 跨年请假 
        if(  ( category.equalsIgnoreCase( siteId.toLowerCase() + "_le_category_6" ) 
                || category.equalsIgnoreCase( siteId.toLowerCase() + "_le_category_1" ) ) 
                && ( currentYear != startYear || currentYear != endYear ) ){
            //跨年请年假--不允许开始时间和结束时间年份不相等
            if( category.equalsIgnoreCase( siteId.toLowerCase() + "_le_category_1" ) && startYear == endYear ){
                double yearLeaveDays = vo.getAnnual();
                if( diffDay <= yearLeaveDays ){
                    map.put( "result", "success" );
                    map.put( "diffDay", diffDay );
                }else{
                    //年休假
                    map.put( "result", "fail" );
                    map.put( "reason", "请年假天数超出下一年年休假天数!");
                    map.put( "diffDay", diffDay );
                }
            } else if( category.equalsIgnoreCase( siteId.toLowerCase() + "_le_category_1" ) && startYear != endYear ){
                //年休假
                map.put( "result", "fail" );
                map.put( "reason", "请年假不允许跨年!");
                map.put( "diffDay", diffDay );
            }else if( category.equalsIgnoreCase( siteId.toLowerCase() + "_le_category_6" ) ){
                //补休假
                map.put( "result", "fail" );
                map.put( "reason", "请补休假不允许跨年!");
                map.put( "diffDay", diffDay );
            }
        }else{
            //剩余年假 = 上年结转年假 + 今年可享受年假 - 核减年假 - 已休年假
            double remainAnnual = vo.getAnnualRemain() + vo.getAnnual() - vo.getAnnualLevel() - vo.getSubAnualLeave();
            //剩余补休 = 上年结转补休假 + 加班天数 - 已补休
            double remainOvertime = vo.getCompensateRemain() + vo.getOverTime() - vo.getCompensateLeave();
            
            if( category.equalsIgnoreCase( siteId.toLowerCase() + "_le_category_1" ) && diffDay > remainAnnual ){
                //年休假
                map.put( "result", "fail" );
                map.put( "reason", "请年假天数超出剩余年休假天数!");
                map.put( "diffDay", diffDay );
            }else if( category.equalsIgnoreCase( siteId.toLowerCase() + "_le_category_6" ) &&  diffDay > remainOvertime ){
                //调休补休假
                map.put( "result", "fail" );
                map.put( "reason", "补休天数超出剩余补休天数!");
                map.put( "diffDay", diffDay );
            }else if( category.equalsIgnoreCase( siteId.toLowerCase() + "_le_category_4" ) && diffDay > 3){
            	//婚假
            	map.put( "result", "fail" );
                map.put( "reason", "婚假天数超出3天!");
                map.put( "diffDay", diffDay );
            }else{
                map.put( "result", "success" );
                map.put( "diffDay", diffDay );
            }
        }*/
        
        return map;
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
    public int deleteLeave(int id) {
        LeaveBean bean = leaveDao.queryLeaveById( id );
        //删除首页草稿
        homepageService.Delete( bean.getNum(), privUtil.getUserInfoScope() ); 
        
        //删除子项
        int delItemCount = leaveDao.deleteLeaveItemByLeaveId( id );
        int fileDelCount = deleteFileByLeaveId( id );
        int count = leaveDao.deleteLeaveById( id );
        log.info( "流水号：" + bean.getNum() + "--Id = " + id + "--删除子项：" 
                    + delItemCount + "条--删除基本信息：" + count + "条--删除文件：" + fileDelCount );
        return count;
    }

    /***
     * 
     * @description:作废 & 删除代办
     * @author: fengzt
     * @createDate: 2015年1月13日
     * @param id
     * @return:int
     */
    @Override
    public int invalidLeave(int id) {
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", ProcessStatusUtil.INVALID );
        parmas.put( "id", id );
        int count = leaveDao.updateOperUserById( parmas );
        
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        LeaveBean bean = leaveDao.queryLeaveById( id );
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

    /**
     * 
     * @description:通过月份查询 --- 首页卡片
     * @author: fengzt
     * @createDate: 2015年2月15日
     * @param year
     * @param month
     * @return:List<StatVo>
     */
    @Override
    public List<StatVo> queryCalendarByMonth(int year, int month) {
        Date monthStart = DateFormatUtil.getMonthFirst( year, month );
        Date monthEnd = DateFormatUtil.getMonthEnd( year, month );
        
        monthStart = DateFormatUtil.addDate( monthStart, "M", -3 );
        monthEnd = DateFormatUtil.addDate( monthEnd, "M", 3 );
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        params.put( "startDate", monthStart );
        params.put( "endDate", monthEnd );
        params.put( "siteId", privUtil.getUserInfoScope().getSiteId() );
        params.put( "status", "('行政部登记备案','已归档')" );
        
        List<StatVo> leaveList = leaveDao.queryLeaveByMonth( params );
        List<StatVo> leaveResult = new ArrayList<StatVo>();
        
        for( StatVo vo : leaveList ){
            List<StatVo> tempData = removeVacation( vo );
            leaveResult.addAll( tempData );
        }
        
        return leaveResult;
    }

    /**
     * 
     * @description:去掉假期
     * @author: fengzt
     * @createDate: 2015年3月2日
     * @param vo
     * @return:List<StatVo> 
     */
    private List<StatVo> removeVacation(StatVo vo) {
        Date startDate = DateFormatUtil.getFormatDate(vo.getStartDate(), "yyyy-MM-dd");//DateFormatUtil.parseDate( DateFormatUtil.formatDate( vo.getStartDate(), "yyyy-MM-dd" ) + " 0800", "yyyy-MM-dd hhmm");
        Date endDate =  DateFormatUtil.getFormatDate(vo.getEndDate(), "yyyy-MM-dd");//DateFormatUtil.parseDate( DateFormatUtil.formatDate( vo.getEndDate(), "yyyy-MM-dd" ) + " 0800", "yyyy-MM-dd hhmm");;
        log.info( vo.getUserName() + "----" + "startDate = " + startDate + "-----endDate = " + endDate );
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        
        //开始时间和结束时间
        int diffDays =  (int)DateFormatUtil.dateDiff( "dd", startDate, endDate ) ;
        List<StatVo> leaveResult = new ArrayList<StatVo>();
        
        if( diffDays == 0 ){
            boolean workFlag = iDateManager.checkIsWorkDate( startDate, userInfo.getSiteId() );
            if( workFlag ){
                leaveResult.add( vo );
            }
        }else{
            for( int i = 0; i <= diffDays; i++ ){
                Date startTemp = DateFormatUtil.addDate( startDate, "d", i );
                boolean workFlag = iDateManager.checkIsWorkDate( startTemp, userInfo.getSiteId() );
                if( workFlag ){
                    StatVo tempVo = new StatVo();
                    tempVo.setUserId( vo.getUserId() );
                    tempVo.setUserName( vo.getUserName() );
                    tempVo.setRemark1( vo.getRemark1() );
                    //把这一天设置到工作时间内以便在页面显示
                    tempVo.setStartDate( DateFormatUtil.parseDate( DateFormatUtil.formatDate( startTemp, "yyyy-MM-dd" ) + " 0800", "yyyy-MM-dd hhmm") );
                    tempVo.setEndDate( DateFormatUtil.parseDate( DateFormatUtil.formatDate( startTemp, "yyyy-MM-dd" ) + " 1700", "yyyy-MM-dd hhmm") );
                    leaveResult.add( tempVo );
                }
            }
        }
        
        return leaveResult;
    }

    /**
     * 
     * @description:通过天查询 --- 首页卡片
     * @author: fengzt
     * @createDate: 2014年10月26日
     * @param ctime
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> queryCalendarByDay(String ctime) {
        //转换日期
        Date cDate = DateFormatUtil.parseDate( ctime, "yyyy-MM-dd" );
        Calendar dTemp = DateFormatUtil.fromDateToCalendar( cDate );
        int year = dTemp.get( Calendar.YEAR );
        int month = dTemp.get( Calendar.MONTH ) + 1;
        
        //获取月份的数据
        List<StatVo> statVos = queryCalendarByMonth( year, month );
        
        List<StatVo> resultData = new ArrayList<StatVo>();
        
        //过滤在当天的
        for( StatVo vo : statVos ){
            if( isMidDate( cDate, vo ) ){
                resultData.add( vo );
            }
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "statVos", resultData );
        if( resultData.size() > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
    }

    /**
     * 
     * @description: 判断一个日期是否在中间
     * @author: fengzt
     * @createDate: 2014年10月26日
     * @param cDate
     * @param vo
     * @return:boolean
     */
    private boolean isMidDate(Date cDate, StatVo vo) {
        Date startDate = DateFormatUtil.getFormatDate( vo.getStartDate(), "yyyy-MM-dd" );
        Date endDate = DateFormatUtil.getFormatDate( vo.getEndDate(), "yyyy-MM-dd" );
        log.info( "startDate = " + startDate + "-----endDate = " + endDate );
        
        if( DateFormatUtil.compareDate( cDate, startDate )
                && DateFormatUtil.compareDate( endDate, cDate ) ){
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @description:查询班组人员值班的班次
     * @author: fengzt
     * @createDate: 2015年3月30日
     * @param siteId
     * @param userId --用户名
     * @param dateStr --某日 ( YYYY-MM-DD )
     * @return:List<ShiftOprVo>
     */
    @Override
    public List<ShiftOprVo> queryShiftOprByUser(String siteId, String userId, String dateStr) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "siteId", siteId );
        params.put( "userId", userId );
        params.put( "dateStr", dateStr );
        params.put( "type", "normal" );
        
        return leaveDao.queryShiftOprByUser( params );
    }

    /**
     * 
     * @description:计算班组请假天数
     * @author: fengzt
     * @createDate: 2015年3月30日
     * @param start
     * @param end
     * @return:double
     */
    @Override
    public double queryLeaveDaysByBanzu(Date start, Date end, String userId) {
        String siteId = privUtil.getUserInfoScope().getSiteId();
        
        //start 00:00
        Date startTemp = DateFormatUtil.getFormatDate( start, "yyyy-MM-dd" );
        Date endTemp = DateFormatUtil.getFormatDate( end, "yyyy-MM-dd" );
        //开始时间和结束时间
        int diffDays =  (int)DateFormatUtil.dateDiff( "dd", startTemp, endTemp ) ;
        double sumDay = 0D;
        
        //检查上班每一天是否在请假范围内
        for( int i = 0; i <= diffDays; i++ ){
            Date temp = DateFormatUtil.addDate( startTemp, "d", i );
            String dateStr = DateFormatUtil.formatDate( temp, "yyyy-MM-dd" );
            //判断是否上班
            List<ShiftOprVo> shiftVos = queryShiftOprByUser( siteId, userId, dateStr );
            if( shiftVos != null && shiftVos.size() > 0 ){
                ShiftOprVo vo = shiftVos.get( 0 );
                String startTime = vo.getStartTime();
                int longTime = vo.getLongTime();
                
                Date startDate = DateFormatUtil.parseDate( dateStr + " " + startTime , "yyyy-MM-dd HHmm" );
                Date endDate = DateFormatUtil.addDate( startDate, "H", longTime );
                
                if( DateFormatUtil.compareDate( startDate, start ) ){
                    //小于边界
                    if( DateFormatUtil.compareDate( end, endDate ) ){
                        sumDay += 1D;
                    }else{
                        sumDay += ( DateFormatUtil.dateDiff( "hh", startDate, end ) > 0 ? 
                                    DateFormatUtil.dateDiff( "hh", startDate, end ) : 0 ) / longTime;
                    }
                }else if( DateFormatUtil.compareDate( start, startDate ) && DateFormatUtil.compareDate( endDate, start ) ){
                    //中间
                    if( DateFormatUtil.compareDate( end, endDate ) ){
                        sumDay += DateFormatUtil.dateDiff( "hh", start, endDate ) / longTime;
                    }else{
                        sumDay += DateFormatUtil.dateDiff( "hh", start, end ) / longTime;
                    }
                }
            }
        }
        
        return sumDay;
    }

    /**
     * 
     * @description:请假申请审批跨月列表
     * @author: fengzt
     * @createDate: 2015年4月7日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    @Override
    public List<LeaveBean> queryExceptionLeaveList(Page<HashMap<?, ?>> pageVo) {
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
        pageVo.setParameter( "status", ProcessStatusUtil.CLOSED );
        pageVo.setParameter( "monthDay", "05" );
        
        List<LeaveBean> result = new ArrayList<LeaveBean>();
        result = leaveDao.queryExceptionLeaveList( pageVo);
        return  result;
    }

    /**
     * 
     * @description:请假申请审批跨月列表-查询
     * @author: fengzt
     * @createDate: 2015年4月7日
     * @param pageVo
     * @return:List<LeaveBean>
     */
    @Override
    public List<LeaveBean> queryExceptionLeaveListBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }else{
            pageVo.setSortKey( "num" );
            pageVo.setSortOrder( "DESC" );
        }
        
        pageVo.setParams( map );
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String userId = userInfo.getUserId();
        String deptId = userInfo.getOrgId();
        String siteId = userInfo.getSiteId();
        pageVo.setParameter( "siteId", siteId );
        pageVo.setParameter( "deptId", deptId );
        pageVo.setParameter( "userId", userId );
        pageVo.setParameter( "statusTemp", ProcessStatusUtil.CLOSED );
        pageVo.setParameter( "monthDay", "05" );
        
        List<LeaveBean> result = new ArrayList<LeaveBean>();
        
        result = leaveDao.queryExceptionLeaveListBySearch( pageVo);
        return  result;
    }
    
    /**
     * 
     * @description:检查用户这天是否上班 
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @param userId
     * @param siteId
     * @param workDate （需要检查的天 格式：yyyy-MM-dd ）
     * @return:
     */
    @Override
    public boolean isWorkDate( String userId, String siteId, String workDate ){
        boolean flag = false;
        //运行人员
        if( isRoleBanzu( userId, siteId) ){
          //判断是否上班
            List<ShiftOprVo> shiftVos = queryShiftOprByUser( siteId, userId, workDate );
            if( shiftVos != null && shiftVos.size() > 0 ){
                flag = true;
            }
        }else{
            Date temp = DateFormatUtil.parseDate( workDate, "yyyy-MM-dd" );
            flag = iDateManager.checkIsWorkDate(  DateFormatUtil.addDate( temp, "s", 1 ), siteId );
        }
        return flag;
    }


    /**
     * 
     * @description:查询时间区域内请假信息(已归档)
     * @author: fengzt
     * @createDate: 2015年6月12日
     * @param siteId
     * @param startTimeStr
     * @param endTimeStr
     * @param category 可指定请假类别
     * @return:List<LeaveContainItemVo>
     * @update:yyn 20160119
     */
    @Override
    public List<LeaveContainItemVo> queryLeaveByDiffDay(String siteId,String[] userIds, String startTimeStr, String endTimeStr,String category) {
        Map<String, Object> params = new HashMap<String, Object>();
        Date startTime = DateFormatUtil.parseDate( startTimeStr, "yyyy-MM-dd HH:mm" );
        Date endTime = DateFormatUtil.parseDate( endTimeStr, "yyyy-MM-dd HH:mm" );
        
        params.put( "startDate", startTime );
        params.put( "endDate", endTime );
        params.put( "siteId", siteId );
        params.put( "userIds", userIds );
        params.put( "category", category );
        params.put( "status", "('" + ProcessStatusUtil.CLOSED + "')" );
        
        return leaveDao.queryLeaveByDiffDay( params );
    }

	@Override
	public Double countLeaveDays(String category,DefinitionBean definitionBean, String userId,
			Boolean isOpr, Date startDate, Date endDate) throws Exception{
		Double days=0.0;//计算结果
		Map<String, DutyPersonShiftVo>dutyPersonShiftMap=null;
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
		
		Boolean isCountRestDay=false;//是否计算非工作日
		if(category!=null){
			//根据类别枚举的设置决定是否计算非工作日
			List<AppEnum> emList = itcMvcService.getEnum( "ATD_LEI_CATEGORY" );
			for (AppEnum appEnum : emList) {
				if(category.equals(appEnum.getCode())){
					isCountRestDay="1".equals(appEnum.getAttribute2());//计算非工作日
				}
			}
		}
		
		//分天计算每天
		Date startTemp=DateFormatUtil.getFormatDate(startDate,"yyyy-MM-dd");
        Date endTemp=DateFormatUtil.getFormatDate(endDate,"yyyy-MM-dd");
        int diffDays=(int)DateFormatUtil.dateDiff("dd",startTemp,endTemp);
        for(int i=0;i<=diffDays;i++){
        	Date temp = DateFormatUtil.addDate( startTemp, "d", i );
        	//对当天的影响
        	days+=countLeaveDaysInOneDay(definitionBean, temp, (i==0?startDate:null),(i==diffDays?endDate:null), userId, isOpr,startDate,endDate,isCountRestDay,dutyPersonShiftMap);
        }
        
        if(isOpr){
        	//如果是运行人员，有可能跨天对startDate的前一天有影响
        	Date temp = DateFormatUtil.addDate( startTemp, "d", -1 );
        	days+=countLeaveDaysInOneDay(definitionBean, temp, startDate,endDate, userId, isOpr,startDate,endDate,isCountRestDay,dutyPersonShiftMap);
		}
		return days;
	}

	/**
	 * 计算一天内的实际请假时长（去除非上班时间）
	 * 取请假时间和工作时间的交集占一天工作时间的比例做为请假天数
	 * 行政人员根据请假类型判断周六日和节假日是否计入，工作日取系统定义上下班时间做为工作时间
	 * 运行人员取排班的上下班时间做为工作时间
	 * @param definitionBean
	 * @param checkDay
	 * @param startTime
	 * @param endTime
	 * @param userId
	 * @param isOpr
	 * @param leaveStartTime
	 * @param leaveEndTime
	 * @param isCountRestDay 是否计算非工作日（周六日和节假日）
	 * @return
	 * @throws Exception
	 */
	private Double countLeaveDaysInOneDay(DefinitionBean definitionBean,Date checkDay,Date startTime,Date endTime,
			String userId,Boolean isOpr,Date leaveStartTime,Date leaveEndTime,Boolean isCountRestDay,Map<String, DutyPersonShiftVo>dutyPersonShiftMap) throws Exception{
		Double days=0.0;//计算结果
		String checkDayStr=DateFormatUtil.dateToString( checkDay, "yyyy-MM-dd");
		String prevDayStr=DateFormatUtil.dateToString( DateFormatUtil.addDate(checkDay, "d", -1), "yyyy-MM-dd");
		String nextDayStr=DateFormatUtil.dateToString( DateFormatUtil.addDate(checkDay, "d", 1), "yyyy-MM-dd");
		
		if(isOpr){//运行人员需要去拿排班信息和班次时间
			String flag=checkDayStr+"_"+userId;
			DutyPersonShiftVo shiftVo=dutyPersonShiftMap.get(flag);
			if(isCountRestDay||(shiftVo!=null&&!"rest".equals(shiftVo.getShiftType()))){//需要计算的情况
				Date shiftStartDate,shiftEndDate;
				if(shiftVo!=null&&!"rest".equals(shiftVo.getShiftType())){//有排班
					shiftStartDate = DateFormatUtil.parseDate( checkDayStr + " " + shiftVo.getStartTime(), "yyyy-MM-dd HHmm" );
					shiftEndDate=DateFormatUtil.addDate(shiftStartDate, "H", shiftVo.getLongTime());
				}else{//计算非工作日的情况
					//找出上一班的结束时间做为休息的开始时间
					DutyPersonShiftVo prevShiftVo=dutyPersonShiftMap.get(prevDayStr+"_"+userId);
					if(prevShiftVo!=null&&!"rest".equals(prevShiftVo.getShiftType())){
						Date prevShiftStartDate = DateFormatUtil.parseDate( prevDayStr + " " + prevShiftVo.getStartTime(), "yyyy-MM-dd HHmm" );
						Date prevShiftEndDate=DateFormatUtil.addDate(prevShiftStartDate, "H", prevShiftVo.getLongTime());
						shiftStartDate=prevShiftEndDate;
					}else{
						shiftStartDate=DateFormatUtil.parseDate( checkDayStr + " 0000", "yyyy-MM-dd HHmm" );
					}
					//找出下一班的开始时间做为休闲的结束时间
					DutyPersonShiftVo nextShiftVo=dutyPersonShiftMap.get(nextDayStr+"_"+userId);
					if(nextShiftVo!=null&&!"rest".equals(nextShiftVo.getShiftType())){
						Date nextShiftStartDate = DateFormatUtil.parseDate( nextDayStr + " " + nextShiftVo.getStartTime(), "yyyy-MM-dd HHmm" );
						shiftEndDate=nextShiftStartDate;
					}else{
						shiftEndDate=DateFormatUtil.parseDate( nextDayStr + " 0000", "yyyy-MM-dd HHmm" );
					}
				}
				//当天的请假开始和结束时间
		        Date start=startTime==null?shiftStartDate:startTime;
		        Date end=endTime==null?(
	            		//请假结束时间有可能是跨天但不跨班的，即属于下一天，但是要统计到当天的班次中的，应排除这种情况
	            		//即：请假结束时间<=当班下班时间，直接取请假结束时间
	            		DateFormatUtil.compareDate(shiftEndDate, leaveEndTime)?leaveEndTime:shiftEndDate
	            	):endTime;
	            if(DateFormatUtil.compareDateNoEqual(end, start)){//请假开始时间<请假结束时间		
	            	days+=countLeaveDaysInWorktime(shiftStartDate,shiftEndDate,start,end);
	            }
			}
		}else{//非运行人员拿系统定义的上下班时间
			//如果要 计算非工作日 或者 当天为工作日，则计算
			if(isCountRestDay||iDateManager.checkIsWorkDate(checkDay,definitionBean.getSiteId())){
				//当天的请假开始和结束时间
		        Date start=startTime==null?DateFormatUtil.parseDate(checkDayStr+" 0000","yyyy-MM-dd HHmm"):startTime;
		        Date end=endTime==null?DateFormatUtil.parseDate(nextDayStr+" 0000","yyyy-MM-dd HHmm"):endTime;
		        if(DateFormatUtil.compareDateNoEqual(end, start)){//请假开始时间<请假结束时间
		        	//获得工作时间
		    		String morning = definitionBean.getForeStartDate();
		            String forenoon = definitionBean.getForeEndDate();
		            String noon = definitionBean.getAfterStartDate();
		            String afternoon = definitionBean.getAfterEndDate();
		            Date morningDate = DateFormatUtil.parseDate( checkDayStr + " " + morning, "yyyy-MM-dd HHmm" ); 
		            Date forenoonDate = DateFormatUtil.parseDate( checkDayStr + " " + forenoon, "yyyy-MM-dd HHmm" ); 
		            Date noonDate = DateFormatUtil.parseDate( checkDayStr + " " + noon, "yyyy-MM-dd HHmm" ); 
		            Date afternoonDate = DateFormatUtil.parseDate( checkDayStr + " " + afternoon, "yyyy-MM-dd HHmm" );
		            //上午和下午各占半天
		            days+=0.5*countLeaveDaysInWorktime(morningDate,forenoonDate,start,end);//上午
		            days+=0.5*countLeaveDaysInWorktime(noonDate,afternoonDate,start,end);//下午
		        }
			}
        }
		return days;
	}
	/**
	 * 计算工作时间内的请假时长，即俩时间段的交集的长度
	 * @param workStart
	 * @param workEnd
	 * @param leaveStart
	 * @param leaveEnd
	 * @return
	 */
	private Double countLeaveDaysInWorktime(Date workStart,Date workEnd,Date leaveStart,Date leaveEnd){
		Double days=0.0;
		//没有交集
    	if(DateFormatUtil.compareDateNoEqual(workStart, leaveEnd)||DateFormatUtil.compareDateNoEqual(leaveStart, workEnd)){
    		
    	}else{
    		//找到小的时间范围就是交集
    		Date maxStartDate=DateFormatUtil.compareDateNoEqual(leaveStart,workStart)?leaveStart:workStart;
    		Date minEndDate=DateFormatUtil.compareDateNoEqual(workEnd, leaveEnd)?leaveEnd:workEnd;
    		days=DateFormatUtil.dateDiff("ss", maxStartDate, minEndDate)/DateFormatUtil.dateDiff("ss", workStart, workEnd);
    	}
    	return days;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Boolean updateAuditStatus(Integer leaveId, String instanceId,
			String status) {
		HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "status", status);
        parmas.put( "instanceId", instanceId);
        parmas.put( "id", leaveId );
        parmas.put( "updateDate", new Date() );
		return leaveDao.updateOperUserById(parmas)>0;
	}
	@Override
	public Boolean getOrgCodeAndIdFromGivenName(String name, String orgId, String deptName,String siteId) {
		String str = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		List<Map<String,String>> list = leaveDao.queryAllDeparts(map);
		Map<String,String> orgMap=new HashMap<String, String>();
		orgMap.put("ORG_CODE", orgId);
		orgMap.put("NAME", deptName);
		boolean isZhCwbType=false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get("NAME").equals("财务部")||list.get(i).get("NAME").equals("综合部")) {
				if (list.get(i).get("ORG_CODE").equals(orgId)) {
					isZhCwbType = true;
				}
			}
		}
		return isZhCwbType;
	}
	
}
