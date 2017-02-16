package com.timss.itsm.service.core;

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
import com.timss.itsm.bean.ItsmComplainRd;
import com.timss.itsm.dao.ItsmComplainRdDao;
import com.timss.itsm.service.ItsmComplainRdService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.util.ItsmConstant;
import com.timss.itsm.vo.ItsmComplainRdVO;
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
public class ItsmComplainRdServiceImpl implements ItsmComplainRdService{

	@Autowired
	private ItsmComplainRdDao complainRdDao;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
    private HomepageService homepageService;
	@Autowired
	private ItsmWoUtilService woUtilService;
	
	private static final Logger LOG=Logger.getLogger(ItsmComplainRdServiceImpl.class);
	
	private Task getFistNodeTask(String processInstId) {
		 // 获取当前活动节点
       List<Task> activities = workflowService.getActiveTasks( processInstId );
       // 刚启动流程，第一个活动节点肯定是属于当前登录人的
       Task task = activities.get( 0 );
       return task;
	}
	private String subName(String name,int beginIndex,int endIndex){
		int size = (name.length() > endIndex) ? endIndex : name.length();
	    String flowName=name.substring(beginIndex, size);
		return flowName;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public ItsmComplainRdVO insertComplainRd(ItsmComplainRdVO complainRdVO) throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		ItsmComplainRd complainRd = complainRdVO.getComplainRds();
		complainRd.setcurrStatus("draft");// 当期状态草稿
		complainRd.setCreatedate(new Date());
		complainRd.setCreateuser(userInfoScope.getUserId());
		complainRd.setModifydate(new Date());
		complainRd.setModifyuser(userInfoScope.getUserId());
		complainRd.setActive("Y");
		complainRd.setSiteid(userInfoScope.getSiteId());
		complainRd.setDeptid(userInfoScope.getOrgId());// 部门
		String complainRdId = complainRd.getId();
		if ("".equals(complainRdId)) { // uuId为空才new
			complainRd.setId(null);
		}
		//插入
		complainRdDao.insertComplainRd(complainRd);  
		// 启动流程
		String processKey = "itsm_" + siteId.toLowerCase() + "_complain";
        String defkey = workflowService.queryForLatestProcessDefKey(processKey);// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(WorkFlowConstants.BUSINESS_ID, complainRd.getId());
        LOG.info( "-------------启动流程----------------------" );
        // 启动流程
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                userInfoScope.getUserId(), map ); 
        LOG.info( "-------------流程启动结束----------------------" );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();
        workflowService.setVariable(processInstId, "orgId", userInfoScope.getOrgId());
        complainRd.setWorkflowId(processInstId);
        complainRdDao.updateComplainRd(complainRd);
        // 加入待办列表
        String flowCode = complainRd.getCode();
        String flowName = subName(complainRd.getContent(), 0, 100);
        String jumpPath = "itsm/complainRecords/openComplainPage.do?complainRdId=" + complainRd.getId();
        String typeName = ItsmConstant.ITC_TYPENAME;//代办类别
        if(!"ITC".equals(siteId)){
        	typeName = ItsmConstant.DPP_TYPENAME;
        }
        // 构建Bean
        HomepageWorkTask homeworkTask = woUtilService.joinHomepageWorkTask
        		(flowCode, flowName, processInstId, "新建投诉",  typeName, jumpPath);
        // 加入待办列表
        homepageService.create( homeworkTask, userInfoScope, null );
        //获取第一个活动节点
        Task task = getFistNodeTask( processInstId ); 
        // 先删掉所有相关的附件数据
        woUtilService.deleteAttachment(complainRd.getId(), null, "Cp");
        // 插入附件的相关数据
        woUtilService.insertAttachment(complainRd.getId(), complainRdVO.getUploadIds(), "Cp", "newCp");
        
        ItsmComplainRdVO complainRdVO2 = new ItsmComplainRdVO();
        complainRdVO2.setTaskId(task.getId());
        complainRdVO2.setComplainId(complainRd.getId());
        complainRdVO2.setWorkflowId(processInstId);
        return complainRdVO2;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public ItsmComplainRdVO saveComplainRd(ItsmComplainRdVO complainRdVO) throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		String uploadIds = complainRdVO.getUploadIds();
		ItsmComplainRd complainRd = complainRdVO.getComplainRds();
		complainRd.setcurrStatus("draft");// 当期状态草稿
		complainRd.setCreatedate(new Date());
		complainRd.setCreateuser(userInfoScope.getUserId());
		complainRd.setModifydate(new Date());
		complainRd.setModifyuser(userInfoScope.getUserId());
		complainRd.setActive("Y");
		complainRd.setSiteid(userInfoScope.getSiteId());
		complainRd.setDeptid(userInfoScope.getOrgId());// 部门
		String complainRdId = complainRd.getId();
		if ("".equals(complainRdId)) { // uuId为空才new
			complainRd.setId(null);
		}
		complainRdDao.insertComplainRd(complainRd);  //插入
        String flowCode = complainRd.getCode();
        String flowName = subName(complainRd.getContent(), 0, 100);
        String jumpPath = "itsm/complainRecords/openComplainPage.do?complainRdId=" + complainRd.getId();
        String typeName = ItsmConstant.ITC_TYPENAME;//代办类别
        if(!"ITC".equals(siteId)){
        	typeName = ItsmConstant.DPP_TYPENAME;
        }
        // 构建Bean
        HomepageWorkTask homeworkTask = woUtilService.joinHomepageWorkTask
        		(flowCode, flowName, null, "草稿",  typeName, jumpPath);
        // 加入待办列表
        homepageService.create( homeworkTask, userInfoScope, null );
        // 先删掉所有相关的附件数据
        woUtilService.deleteAttachment(complainRd.getId(), null, "Cp");
        // 插入附件的相关数据
        woUtilService.insertAttachment(complainRd.getId(), uploadIds, "Cp", "draft");
        
        ItsmComplainRdVO complainRdVO2 = new ItsmComplainRdVO();
        complainRdVO2.setComplainId(complainRd.getId());
        return complainRdVO2;
	}
	//对没有启动流程的跟新投诉记录
	@Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public ItsmComplainRdVO updateComplainRd(ItsmComplainRdVO complainRdVO) throws Exception {
		ItsmComplainRdVO complainRdVO2 = new ItsmComplainRdVO();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String commitStyle = complainRdVO.getCommitStyle();
    	String siteId = userInfoScope.getSiteId();
    	String uploadIds = complainRdVO.getUploadIds();
    	ItsmComplainRd complainRd = complainRdVO.getComplainRds();
    	ItsmComplainRd oldcomplainRd = complainRdDao.queryCpRdById(complainRd.getId());
    	complainRd.setCode(oldcomplainRd.getCode());
        if ("commit".equals( commitStyle )) { // 提交草稿，启动流程
        	// 启动流程
    		String processKey = "itsm_" + siteId.toLowerCase() + "_complain";
            String defkey = workflowService.queryForLatestProcessDefKey(processKey);// 获取最新流程定义版本
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(WorkFlowConstants.BUSINESS_ID, complainRd.getId());
            // 启动流程
            ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                    userInfoScope.getUserId(), map ); 
            // 获取流程实例ID
            String processInstId = processInstance.getProcessInstanceId();
            workflowService.setVariable(processInstId, "orgId", userInfoScope.getOrgId());
            complainRd.setWorkflowId(processInstId);
            complainRdDao.updateComplainRd(complainRd);
            // 加入待办列表
            String flowCode = complainRd.getCode();
            String flowName = subName(complainRd.getContent(), 0, 100);
            String jumpPath = "itsm/complainRecords/openComplainPage.do?complainRdId=" + complainRd.getId();
            String typeName = ItsmConstant.ITC_TYPENAME;//代办类别
            if(!"ITC".equals(siteId)){
            	typeName = ItsmConstant.DPP_TYPENAME;
            }
            // 构建Bean
            HomepageWorkTask homeworkTask = woUtilService.joinHomepageWorkTask
            		(flowCode, flowName, processInstId, "审批",  typeName, jumpPath);

            Task task = getFistNodeTask( processInstId ); // 获取第一个活动节点
            // 加入待办列表
            homepageService.create( homeworkTask, userInfoScope, null );
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( processInstId );
            // 获取下一步执行人
            ProcessDefInfo pdi = new ProcessDefInfo( processEngine, task.getProcessDefinitionId() );
            String handleUserNames = "";
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
                        handleUserNames += user.getName() + ",";
                    }
                } else {
                    LOG.debug( "no users can be found by the elementifo." );
                }
            }
                // 刚启动流程，第一个活动节点肯定是属于当前登录人的
                task = activities.get( 0 );
                String nextAuditUserNames = handleUserNames.substring( 0, handleUserNames.length() - 1 );
                Map<String, String> parmas = new HashMap<String, String>();
                parmas.put( "complainRdId", complainRd.getId());
                parmas.put( "complainHandlerUser", nextAuditUserNames );
                complainRdDao.updateCurrHandUserById( parmas );
                // 先删掉所有相关的附件数据
                woUtilService.deleteAttachment(complainRd.getId(), null, "Cp");
                // 插入附件的相关数据
                woUtilService.insertAttachment(complainRd.getId(), uploadIds, "Cp", "newCp");
                
                complainRdVO2.setWorkflowId(processInstId);
                complainRdVO2.setTaskId(task.getId());
                complainRdVO2.setComplainId(complainRd.getId());
        }else if ("save".equals( commitStyle ) ) { // 再次暂存草稿，不启动流程
        		complainRd.setcurrStatus( "draft" ); // 设置为草稿状态
        		String workflowId = oldcomplainRd.getWorkflowId();
        		if(null == workflowId){ //对于草稿的第二次暂存来说是没有workflowId
        			complainRd.setWorkflowId("");
        		}else{
        			complainRd.setWorkflowId(workflowId);
        		}
        		complainRdDao.updateComplainRd(complainRd);
        		 // 先删掉所有相关的附件数据
                woUtilService.deleteAttachment(complainRd.getId(), null, "Cp");
                // 插入附件的相关数据
                woUtilService.insertAttachment(complainRd.getId(), uploadIds, "Cp", "draft");
        		// 跟新待办列表
                String flowCode = complainRd.getCode();
                String flowName = subName(complainRd.getContent(), 0, 100);
                String jumpPath = "itsm/complainRecords/openComplainPage.do?complainRdId=" + complainRd.getId();
                String typeName = ItsmConstant.ITC_TYPENAME;//代办类别
                if(!"ITC".equals(siteId)){
                	typeName = ItsmConstant.DPP_TYPENAME;
                }
                // 构建Bean
                HomepageWorkTask homeworkTask = woUtilService.joinHomepageWorkTask
                		(flowCode, flowName, null, "草稿",  typeName, jumpPath);
                // 加入待办列表
                homepageService.create( homeworkTask, userInfoScope, null );
                complainRdVO2.setWorkflowId("noFlow");
                complainRdVO2.setTaskId("noTask");
                complainRdVO2.setComplainId(complainRd.getId());
            }
            return complainRdVO2;
	}
	//退回再提交
	@Override
	public void rollbackCommit(ItsmComplainRdVO complainRdVO)throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String uploadIds = complainRdVO.getUploadIds();
        String siteId = userInfoScope.getSiteId();
        ItsmComplainRd complainRd = complainRdVO.getComplainRds();
    	complainRdDao.updateComplainRd(complainRd);
    	String complainRdId = complainRd.getId();
    	complainRd = complainRdDao.queryCpRdById(complainRdId);
    	String processInstId = complainRd.getWorkflowId();
    	// 待办列表
        String flowCode = complainRd.getCode();
        String flowName = subName(complainRd.getContent(), 0, 100);
        String jumpPath = "itsm/complainRecords/openComplainPage.do?complainRdId=" + complainRd.getId();
        String typeName = ItsmConstant.ITC_TYPENAME;//代办类别
        if(!"ITC".equals(siteId)){
        	typeName = ItsmConstant.DPP_TYPENAME;
        }
        // 构建Bean
        HomepageWorkTask homeworkTask = woUtilService.joinHomepageWorkTask
        		(flowCode, flowName, processInstId, "审批",  typeName, jumpPath);
        
        homepageService.create( homeworkTask, userInfoScope, null );
        Task task = getFistNodeTask( processInstId ); // 获取第一个活动节点
    	// 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        task = activities.get( 0 );
        // 获取下一步执行人
        ProcessDefInfo pdi = new ProcessDefInfo( processEngine, task.getProcessDefinitionId() );
        String handleUserNames = "";
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
                     handleUserNames += user.getName() + ",";
                 }
             } else {
                 LOG.debug( "no users can be found by the elementifo." );
             }
        }
        String nextAuditUserNames = handleUserNames.substring( 0, handleUserNames.length() - 1 );
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put( "complainRdId", complainRdId );
        parmas.put( "complainHandlerUser", nextAuditUserNames );
        complainRdDao.updateCurrHandUserById( parmas );
        // 先删掉所有相关的附件数据
        woUtilService.deleteAttachment(complainRd.getId(), null, "Cp");
        // 插入附件的相关数据
        woUtilService.insertAttachment(complainRd.getId(), uploadIds, "Cp", "newCp");
	}
	
	@Override
	public ItsmComplainRdVO queryCpRdById(String complainRdId) {
		ItsmComplainRd complainRd=complainRdDao.queryCpRdById(complainRdId);
		String currStatus=complainRd.getcurrStatus();
		String processInstId = complainRd.getWorkflowId();
		String taskId="";
		boolean flag = ( "draft".equals(currStatus) && null != processInstId ) ||
				!"obsolete".equals(currStatus) && !"draft".equals(currStatus) && !"end".equals(currStatus);
		// 获取当前活动节点
		if(flag){
			List<Task> activities = workflowService.getActiveTasks( complainRd.getWorkflowId() );
			Task task = activities.get( 0 );
			taskId = task.getId();
		}
		ItsmComplainRdVO complainRdVO = new ItsmComplainRdVO();
		complainRdVO.setComplainRds(complainRd);
		complainRdVO.setTaskId(taskId);
        return complainRdVO;
    }
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public Page<ItsmComplainRd> queryComplainRdList(Page<ItsmComplainRd> page) {
		List<ItsmComplainRd> ret=complainRdDao.queryComplainRdList(page);
		page.setResults(ret);
		return page;
	}
	
	//更新当期状态
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public void updateComplainRdStatus(String complainRdId, String currStatus) {
		ItsmComplainRd complainRd = new ItsmComplainRd();
		complainRd.setId(complainRdId);
		complainRd.setcurrStatus(currStatus);
		complainRdDao.updateComplainRdStatus(complainRd);
	}

	//更新处理人
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public void updateCurrHandlerUser(String complainRdId,UserInfoScope userInfoScope, String flag) 
			throws Exception {
		String userIds = "";
		if ( "normal".equals( flag ) ) {
			userIds = userInfoScope.getParam( "userIds" );
		Map<String, List<String>> userIdsMap = (Map<String, List<String>>) new ObjectMapper().readValue(userIds, Map.class);
		Iterator<String> iterator = userIdsMap.keySet().iterator();
		while (iterator.hasNext()) {
             List<String> auditUserId = userIdsMap.get( iterator.next() );
             String nextAuditUserNames = "";
             for ( int i = 0; i < auditUserId.size(); i++ ) {
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
                 nextAuditUserNames = nextAuditUserNames.substring( 0, nextAuditUserNames.length() - 1 );
                 Map<String, String> parmas = new HashMap<String, String>();
                 parmas.put( "complainRdId", complainRdId );
                 parmas.put( "complainHandlerUser", nextAuditUserNames );
                 complainRdDao.updateCurrHandUserById( parmas );
				}
				}else if ( "rollback".equals( flag ) ) { // 回退的时候是单个人
	                String nextAuditUserId = "";
	                String nextAuditUserName = "";
	                nextAuditUserId = userInfoScope.getParam( "userId" );
	                nextAuditUserName = itcMvcService.getUserInfoById( nextAuditUserId ).getUserName();
	                Map<String, String> parmas = new HashMap<String, String>();
	                parmas.put( "complainRdId", complainRdId );
	                parmas.put( "complainHandlerUser", nextAuditUserName );
	                complainRdDao.updateCurrHandUserById( parmas );
				}
		}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public void obsoleteWorkOrder(String complainRdId) {
		 UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	     String userId = userInfoScope.getUserId();
	     ItsmComplainRd complainRd=complainRdDao.queryCpRdById(complainRdId);
	     String workflowId=complainRd.getWorkflowId();
	     // 获取当前活动节点
	        List<Task> activities = workflowService.getActiveTasks( workflowId );
	        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
	        Task task = activities.get( 0 );
	        String taskId = task.getId();
	        // 修改状态
	        LOG.info( "-------------作废工单处理开始,ID：" + complainRdId );
	        // 终止流程
	        workflowService.stopProcess( taskId, userId, userId, "作废" );
	        String currStatus="obsolete";
	        complainRd.setId(complainRdId);
			complainRd.setcurrStatus(currStatus);
			complainRdDao.updateComplainRdStatus(complainRd);
	        // 删掉对应的待办
	        homepageService.complete( workflowId, userInfoScope, "已作废" );
	    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public void deleteWorkOrder(String complainRdId) {
		LOG.info( "-------------ITC 删除工单，工单ID：" + complainRdId + "----------------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        ItsmComplainRd complainRd = complainRdDao.queryCpRdById(complainRdId);
        complainRdDao.deleteComplainRd(complainRdId);// 删除
        homepageService.Delete( complainRd.getCode(), userInfoScope );// 删除首页草稿
	}
}