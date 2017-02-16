package com.timss.attendance.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.timss.attendance.bean.CarApplyBean;
import com.timss.attendance.dao.CarApplyDao;
import com.timss.attendance.service.AtdAttachService;
import com.timss.attendance.service.CarApplyService;
import com.timss.attendance.vo.CarApplyVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.process.ProcessDefInfo;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.utils.WorkFlowConstants;


@Service
public class CarApplyServiceImpl implements CarApplyService{
	
	@Autowired
	private CarApplyDao carApplyDao;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private HomepageService homepageService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
    private ProcessEngine processEngine;
	@Autowired 
    private AtdAttachService attachService;
	
	private static final Logger LOG=Logger.getLogger(CarApplyServiceImpl.class);
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public CarApplyVo saveCarApply(CarApplyBean bean) throws Exception {
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfo.getSiteId();
		String userId = userInfo.getUserId();
		String deptId = userInfo.getOrgId();
		bean.setSiteId(siteId);
		bean.setDepId(deptId);
		bean.setCreateDate(new Date());
		bean.setCreateUser(userId);
		bean.setModifyDate(new Date());
		bean.setModifyUser(userId);
		bean.setDelInd("Y");
		bean.setStatus("draft");
		carApplyDao.insertCarApply(bean);
		
		String flowCode = bean.getCaNum();
		String jumpPath = "attendance/carApply/openCarApplyPage.do?caId="+bean.getCaId();
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，
        homeworkTask.setName( userInfo.getUserName()+"(工号:" +userInfo.getUserId() +")的用车申请" ); // 名称
        homeworkTask.setProcessInstId( null ); // 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName( "草稿" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Draft );
        homeworkTask.setTypeName( "用车申请" ); // 类别
        homeworkTask.setUrl( jumpPath );// 扭转的URL
        homepageService.create( homeworkTask, userInfo, null ); // 调用接口创建草稿
        
        //插入附件的相关数据
        String[] uploadIds = bean.getUploadIds();
        attachService.delete("carApply", bean.getCaId(), null);
//        for(int i=0;i<uploadIds.length;i++)
        if(null !=uploadIds &&uploadIds.length != 0){
        	attachService.insert("carApply", bean.getCaId(), uploadIds);
        }
		CarApplyVo carApplyVo = new CarApplyVo();
		carApplyVo.setCarApplyBean(bean);
		return carApplyVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CarApplyVo updateCarApply(CarApplyBean bean) throws Exception{
		//插入附件的相关数据
        String[] uploadIds = bean.getUploadIds();
        attachService.delete("carApply", bean.getCaId(), null);
        if(null !=uploadIds &&uploadIds.length != 0){
        	attachService.insert("carApply", bean.getCaId(), uploadIds);
        }
		carApplyDao.updateCarApply(bean);
		String caId = bean.getCaId();
		CarApplyVo carApplyVo = new CarApplyVo();
		carApplyVo.setCarApplyBean(carApplyDao.queryById(caId));
		return carApplyVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteById(String caId) throws Exception {
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		CarApplyBean bean = carApplyDao.queryById(caId);
		homepageService.Delete( bean.getCaNum(), userInfo );// 删除首页草稿 
		return carApplyDao.deleteCarApply(caId);
	}

	@Override
	public Page<CarApplyBean> queryList(Page<CarApplyBean> page) {
		List<CarApplyBean> list = carApplyDao.queryList(page);
		page.setResults(list);
		return page;
	}

	@Override
	public CarApplyVo queryById(String caId) {
		CarApplyBean bean = carApplyDao.queryById(caId);
		String status = bean.getStatus();
		String processInstId = bean.getWorkflowId();
		String taskId = "";
		boolean flag = ("draft".equals(status) && null!=processInstId ) || 
						!"obsolete".equals(status) && !"draft".equals(status) && !"end".equals(status);
		if(flag){
			 List<Task> activities = workflowService.getActiveTasks( bean.getWorkflowId() );
		        Task task = activities.get( 0 );
		        taskId = task.getId();
		}
		
		CarApplyVo carApplyVo = new CarApplyVo();
		carApplyVo.setCarApplyBean(bean);
		carApplyVo.setTaskId(taskId);
        return carApplyVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CarApplyVo insertCarApply(CarApplyBean bean) throws Exception{
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfo.getSiteId();
		String userId = userInfo.getUserId();
		String deptId = userInfo.getOrgId();
		bean.setSiteId(siteId);
		bean.setDepId(deptId);
		bean.setCreateDate(new Date());
		bean.setCreateUser(userId);
		bean.setModifyDate(new Date());
		bean.setModifyUser(userId);
		bean.setDelInd("Y");
		bean.setStatus("draft");
		carApplyDao.insertCarApply(bean);
		
		String caId = bean.getCaId();
		//启动流程
        String processKey = "atd_" + siteId.toLowerCase() + "_carapply";
        //获取最新流程定义版本
        String defkey = workflowService.queryForLatestProcessDefKey( processKey );
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(WorkFlowConstants.BUSINESS_ID, caId);
		LOG.info( "-------------启动流程开始----------------------" );
		ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,
				userId, map);
		LOG.info( "-------------启动流程结束----------------------" );
		// 获取流程实例ID
		String processInstId = processInstance.getProcessInstanceId();
		workflowService.setVariable(processInstId, "orgId", userInfo.getOrgId());
		//综合部1230802  财务部1230804
		String isSpecialDept;
		if("1230802".equals(deptId) || "1230804".equals(deptId)){
    		isSpecialDept ="Y";
    	}else{
    		isSpecialDept ="N";
    	}
		workflowService.setVariable( processInstId, "isSpecialDept", isSpecialDept);
		bean.setWorkflowId(processInstId);
		carApplyDao.updateCarApply(bean);
		
		//加入代办
		String flowCode = bean.getCaNum();
		String jumpPath = "attendance/carApply/openCarApplyPage.do?caId="+bean.getCaId();
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，
        homeworkTask.setName(userInfo.getUserName()+"(工号:" +userInfo.getUserId() +")的用车申请" ); // 名称
        homeworkTask.setProcessInstId( processInstId ); 
        homeworkTask.setStatusName( "新建" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Process );
        homeworkTask.setTypeName( "用车申请" ); // 类别
        homeworkTask.setUrl( jumpPath );// 扭转的URL
        homepageService.create( homeworkTask, userInfo, null ); 
        //获取第一个活动节点
        Task task = getFistNodeTask( processInstId );
        //插入附件的相关数据
        String[] uploadIds = bean.getUploadIds();
        attachService.delete("carApply", bean.getCaId(), null);
        if(null !=uploadIds &&uploadIds.length != 0){
        	attachService.insert("carApply", bean.getCaId(), uploadIds);
        }
    	CarApplyVo carApplyVo = new CarApplyVo();
		carApplyVo.setCarApplyBean(bean);
		carApplyVo.setTaskId(task.getId());
		return carApplyVo;
	}
	
	@Override
	public CarApplyVo update(CarApplyBean bean) throws Exception {
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfo.getSiteId();
		String deptId = userInfo.getOrgId();
		String caId = bean.getCaId();
		String userId = userInfo.getUserId();
		String workflowId = bean.getWorkflowId();
		bean.setStatus("draft");
		carApplyDao.updateCarApply(bean);
		if("".equals(workflowId)){//草稿提交启动流程
		//启动流程
        String processKey = "atd_" + siteId.toLowerCase() + "_carapply";
        //获取最新流程定义版本
        String defkey = workflowService.queryForLatestProcessDefKey( processKey );
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(WorkFlowConstants.BUSINESS_ID, caId);
		LOG.info( "-------------启动流程开始----------------------" );
		ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,
				userId, map);
		LOG.info( "-------------启动流程结束----------------------" );
		// 获取流程实例ID
		String processInstId = processInstance.getProcessInstanceId();
		workflowService.setVariable(processInstId, "orgId", userInfo.getOrgId());
		String isSpecialDept;
		if("1230802".equals(deptId) || "1230804".equals(deptId)){
    		isSpecialDept ="Y";
    	}else{
    		isSpecialDept ="N";
    	}
		workflowService.setVariable( processInstId, "isSpecialDept", isSpecialDept);
		bean.setWorkflowId(processInstId);
//		bean.setStatus("newApply");
		carApplyDao.updateCarApply(bean);
		}
		String processInstId = bean.getWorkflowId();
		//加入代办
		bean = carApplyDao.queryById(bean.getCaId());
		String flowCode = bean.getCaNum();
		String jumpPath = "attendance/carApply/openCarApplyPage.do?caId="+bean.getCaId();
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow( flowCode );// 编号，
        homeworkTask.setName( userInfo.getUserName()+"(工号:" +userInfo.getUserId() +")的用车申请" ); // 名称
        homeworkTask.setProcessInstId( processInstId ); 
        homeworkTask.setStatusName( "新建" ); // 状态
        homeworkTask.setType( HomepageWorkTask.TaskType.Process );
        homeworkTask.setTypeName( "用车申请" ); // 类别
        homeworkTask.setUrl( jumpPath );// 扭转的URL
        homepageService.create( homeworkTask, userInfo, null ); 
        //获取第一个活动节点
        Task task = getFistNodeTask( processInstId );
        // 获取下一步执行人
        Map<String, List<String>> userIds = new HashMap<String, List<String>>();
        ProcessDefInfo pdi = new ProcessDefInfo( processEngine, task.getProcessDefinitionId() );
        String handleUserIds = "";
        for ( String nextTaskDefKey : workflowService.getNextTaskDefKeys( task.getProcessInstanceId(),
                task.getTaskDefinitionKey() ) ) {
            if ( nextTaskDefKey == null ) {
                continue;
            }
            FlowElement el = pdi.getElement( nextTaskDefKey );
            List<String> userList = new ArrayList<String>();
            List<SecureUser> secureUsers = workflowService.selectUsersByTaskKey( task.getProcessInstanceId(),
                    el.getId(), task.getId() );
            if ( secureUsers != null && secureUsers.size() != 0 ) {
                for ( SecureUser user : secureUsers ) {
                    userList.add( user.getId() );
                    handleUserIds += user.getId() + ",";
                }
            } else {
                LOG.debug( "no users can be found by the elementifo." );
            }
            userIds.put( nextTaskDefKey, userList );
        }
        String nextAuditUserIdsStr = handleUserIds.substring( 0, handleUserIds.length() - 1 );
        bean.setCurrHandUser(nextAuditUserIdsStr);
        //插入附件的相关数据
        String[] uploadIds = bean.getUploadIds();
        attachService.delete("carApply", bean.getCaId(), null);
        if(null !=uploadIds &&uploadIds.length != 0){
        	attachService.insert("carApply", bean.getCaId(), uploadIds);
        }
    	CarApplyVo carApplyVo = new CarApplyVo();
		carApplyVo.setCarApplyBean(bean);
		carApplyVo.setTaskId(task.getId());
		return carApplyVo;
	}

	@Override
	public void updateCurrHandlerUser(String caId, UserInfoScope userInfo,String flag) throws Exception {
		String userIds = "";
		if ( "normal".equals( flag ) ) {
			userIds = userInfo.getParam( "userIds" );
		Map<String, List<String>> userIdsMap = (Map<String, List<String>>) new ObjectMapper().readValue(userIds, Map.class);
		Iterator<String> iterator = userIdsMap.keySet().iterator();
		while (iterator.hasNext()) {
             List<String> auditUserId = userIdsMap.get( iterator.next() );
             String nextAuditUserIds = "";
             String nextAuditUserNames = "";
             for ( int i = 0; i < auditUserId.size(); i++ ) {
            	 nextAuditUserIds = auditUserId.get( i ) + ",";
                 String tempUserIds = auditUserId.get( i );
                 if ( tempUserIds.indexOf( "," ) > 0 ) {
                	 String[] auditUserNames = tempUserIds.split( "," );
                	 for ( int j = 0; j < auditUserNames.length; j++ ) {
                		 nextAuditUserNames += itcMvcService.getUserInfoById( auditUserNames[j] ).getUserName() + ",";
                    }
                 } else {
                         nextAuditUserNames = itcMvcService.getUserInfoById( auditUserId.get( i ) ).getUserName() + ",";
                     }
                 }
                 nextAuditUserIds = nextAuditUserIds.substring( 0, nextAuditUserIds.length() - 1 );
                 nextAuditUserNames = nextAuditUserNames.substring( 0, nextAuditUserNames.length() - 1 );
                 CarApplyBean bean =new CarApplyBean();
                 bean.setCaId(caId);
                 bean.setCurrHandUser(nextAuditUserIds);
                 carApplyDao.updateCarApply(bean);
				}
				}else if ( "rollback".equals( flag ) ) { // 回退的时候是单个人
	                String nextAuditUserId = "";
	                nextAuditUserId = userInfo.getParam( "userId" );
	                CarApplyBean bean =new CarApplyBean();
	                bean.setCaId(caId);
	                bean.setCurrHandUser(nextAuditUserId);
	                carApplyDao.updateCarApply(bean);
				}
		}
	
	@Override
	public int obsoleteCarApply(String caId) throws Exception {
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		CarApplyBean bean = carApplyDao.queryById(caId);
		String userId = userInfo.getUserId();
		String workflowId = bean.getWorkflowId();
		// 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( workflowId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        String taskId = task.getId();
        //终止流程
        workflowService.stopProcess( taskId, userId, userId, "作废" );
        // 删掉对应的待办
        homepageService.complete( workflowId, userInfo, "已作废" );
        bean.setCaId(caId);
        bean.setStatus("obsolete");
        carApplyDao.updateCarApply(bean);
        String status = bean.getStatus();
        if("obsolete".equals(status)){
        	return 1;
        }else{
        	return -1;
        }
	}
	
	private Task getFistNodeTask(String processInstId) {
		 // 获取当前活动节点
       List<Task> activities = workflowService.getActiveTasks( processInstId );
       // 刚启动流程，第一个活动节点肯定是属于当前登录人的
       Task task = activities.get( 0 );
       return task;
	}


}
