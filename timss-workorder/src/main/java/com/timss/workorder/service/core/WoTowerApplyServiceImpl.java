package com.timss.workorder.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.TowerApply;
import com.timss.workorder.dao.WoTowerApplyDao;
import com.timss.workorder.service.WoTowerApplyService;
import com.timss.workorder.util.TowerApplyStatusUtil;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Service
public class WoTowerApplyServiceImpl implements WoTowerApplyService {
	@Autowired
	private WoTowerApplyDao woTowerApplyDao;
	@Autowired
    private ItcMvcService itcMvcService;
	@Autowired
    private WorkflowService workflowService;
	@Autowired
    HomepageService homepageService;
	@Autowired
	ISecurityMaintenanceManager iSecurityMaintenanceManager;
	
	private static Logger logger = Logger.getLogger(WoTowerApplyServiceImpl.class);

	@Override
	public Page<TowerApply> queryAllTowerApply(TowerApply towerApply)
			throws Exception {
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Page<TowerApply> page = userInfo.getPage();
        Map<String, String[]> params = userInfo.getParamMap();

        if ( params.containsKey( "search" ) ) {
            String fuzzySearchParams = userInfo.getParam( "search" );
            Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
            page.setFuzzyParams( fuzzyParams );
        }
        
        page.setParameter("siteId", userInfo.getSiteId());
        page.setParameter("userId", userInfo.getUserId());
        String sort = String.valueOf(userInfo.getParam("sort") == null ? "" : userInfo.getParam("sort"));
        String order = String.valueOf(userInfo.getParam("order") == null ? "" : userInfo.getParam("order"));
        if (!"".equals(sort) && !"".equals(order)) {
        	page.setSortKey(sort);
	        page.setSortOrder(order);
	    } else {
	        page.setSortKey("createDate");
	        page.setSortOrder("desc");
	    }
        List<TowerApply> ret = woTowerApplyDao.queryAllTowerApply( page );
        page.setResults(ret);
        return page;
	}

	@Override
	public Map<String, Object> queryTowerApplyById(String id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		TowerApply towerApply = woTowerApplyDao.queryTowerApplyById(id);
        
        String taskId = "";
        // 获取当前活动节点
        if(towerApply.getWorkflowId() != null){
	        List<Task> activities = workflowService.getActiveTasks(towerApply.getWorkflowId());
	        if(activities.size() != 0 && activities != null && activities.get(0) != null){
	            Task task = activities.get(0);
	            taskId = task.getId();
	        }
        }
        
        resultMap.put( "bean", towerApply );
        resultMap.put( "taskId", taskId );
        return resultMap;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> insertTowerApply(TowerApply towerApply)
			throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String deptId = userInfoScope.getOrgId();
        String userid = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        
        towerApply.setApplicantNo(userid);
        towerApply.setApplicant( userName );
        towerApply.setApplyTime( new Date() );
        towerApply.setCreateuser(userid);
        towerApply.setCreateuserName(userName);
        towerApply.setCreateDate(new Date());
        towerApply.setCurrHandler(userid);
        towerApply.setCurrHandlerName(userName);
        towerApply.setSiteid(siteId);
        towerApply.setDeptid(deptId);
        towerApply.setDelFlag( "N" );
        woTowerApplyDao.insertTowerApply( towerApply );
        
        String towerApplyId = towerApply.getId(); // 获取要插入记录的ID

        // 启动流程
        String defkey = workflowService
                        .queryForLatestProcessDefKey("workorder_"
                                        + siteId.toLowerCase() + "_towerapply");// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("businessId", towerApplyId);
        logger.info("-------------CORE 启动流程开始，登塔申请ID："+towerApplyId+"---------------");
        // 启动流程
        ProcessInstance processInstance = workflowService
                        .startLatestProcessInstanceByDefKey(defkey,
                                        userInfoScope.getUserId(), map);
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();
        logger.info("-------------CORE 启动流程结束，登塔申请ID："+towerApplyId+"  流程实例ID："+processInstId+"---------------");
        
        towerApply.setWorkflowId(processInstId);
        towerApply.setApplyStatus("apply");  //填写开工申请

        woTowerApplyDao.updateWorkflowId(towerApplyId,processInstId);
        // 加入待办列表
        String jumpPath = "workorder/towerApply/openTowerApplyInfoPage.do?id=" + towerApplyId;
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow(towerApply.getApplyCode());// 编号
        homeworkTask.setName(towerApply.getApplyCompany()); // 名称
        homeworkTask.setProcessInstId(processInstId);// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName("填写登塔申请"); // 状态
        homeworkTask.setType(HomepageWorkTask.TaskType.Process); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                                                                                // 草稿;xxxx.Process
                                                                                                                                // 流程实例
        homeworkTask.setTypeName("登塔申请"); // 类别
        homeworkTask.setUrl(jumpPath); // 扭转的URL
        // 加入待办列表
        homepageService.create(homeworkTask, userInfoScope);

        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks(processInstId);
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get(0);

        Map<String, String> parmas = getTowerApplyCurrHanderInfo( userInfoScope );
        if(parmas != null){
            parmas.put( "towerApplyId", towerApplyId );
            woTowerApplyDao.updateCurrHander( parmas );
        }
       
        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "workflowId", processInstId );
        resultHashMap.put( "taskId", task.getId() );
        resultHashMap.put( "towerApplyId", towerApplyId );
        return resultHashMap;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, String>  updateTowerApply(Map<String, String> addWADataMap) throws Exception {
		Map<String, String> returnResult = new HashMap<String, String>();
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();

        String towerApplyFormDate = addWADataMap.get("towerApplyFormData"); // 开工申请基本信息表单数据
        String updateStyle = addWADataMap.get("updateStyle");

        TowerApply towerApply = JsonHelper.toObject(towerApplyFormDate,TowerApply.class);
        TowerApply oldWorkapply = woTowerApplyDao.queryTowerApplyById(towerApply.getId());

        if ("commit".equals(updateStyle)) { // 提交草稿，启动流程
                // 启动流程
                String defkey = workflowService
                                .queryForLatestProcessDefKey("workorder_"
                                        + siteId.toLowerCase() + "_towerapply");// 获取最新流程定义版本
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("businessId", towerApply.getId());

                // 启动新流程
                logger.info("-------------CORE 启动流程开始，申请单编号："+towerApply.getApplyCode()+"---------------");
                ProcessInstance processInstance = workflowService
                                .startLatestProcessInstanceByDefKey(defkey,
                                                userInfoScope.getUserId(), map);
                // 获取流程实例ID
                String processInstId = processInstance.getProcessInstanceId();
                logger.info("-------------CORE 启动流程结束，申请单编号："+towerApply.getApplyCode()+"  流程实例ID："+processInstId+"---------------");
                
                towerApply.setWorkflowId(processInstId);
                String towerApplyId = towerApply.getId();
                //更新流程实例ID
                woTowerApplyDao.updateWorkflowId(towerApplyId,processInstId);
                

                // 第一次启动流程 添加“待办”(未测试)
                String flowCode = towerApply.getApplyCode();
                
                String jumpPath = "workorder/towerApply/openTowerApplyInfoPage.do?id=" + towerApplyId;
                // 构建Bean
                HomepageWorkTask homeworkTask = new HomepageWorkTask();
                homeworkTask.setFlow(flowCode);// 编号，如工单编号 WO20140902001
                homeworkTask.setName(towerApply.getApplyCompany()); // 名称
                homeworkTask.setProcessInstId(processInstId);// 草稿时流程实例ID可以不用设置
                homeworkTask.setStatusName("运检部安全主管审批"); // 状态
                homeworkTask.setType(HomepageWorkTask.TaskType.Process); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                homeworkTask.setTypeName("登塔申请"); // 类别
                homeworkTask.setUrl(jumpPath); // 扭转的URL
                // 加入待办列表
                homepageService.create(homeworkTask, userInfoScope);

                // 获取当前活动节点
                List<Task> activities = workflowService.getActiveTasks(processInstId);
                // 刚启动流程，第一个活动节点肯定是属于当前登录人的
                Task task = activities.get(0);
                
                returnResult.put("workflowId",processInstId );
                returnResult.put("taskId", task.getId());

        } else if ("save".equals(updateStyle)) { // 再次暂存草稿，不启动流程
        		towerApply.setApplyStatus(oldWorkapply.getApplyStatus()); // 设置为草稿状态
        		towerApply.setWorkflowId(oldWorkapply.getWorkflowId());
        		towerApply.setModifyDate(new Date());
        		towerApply.setModifyuser(userInfoScope.getUserId());
        		towerApply.setModifyuserName(userInfoScope.getUserName());
                woTowerApplyDao.updateTowerApply( towerApply );
                
                returnResult.put("workflowId", "noFlow");
                returnResult.put("taskId", "noTask");
        }
        return returnResult;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int deleteTowerApply(String id) {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put("towerApplyId", id);
        parmas.put("currHandler", "");
        parmas.put("currHandlerName", "");
        woTowerApplyDao.updateCurrHander( parmas );
        TowerApply towerApply = woTowerApplyDao.queryTowerApplyById( id );
        homepageService.Delete(towerApply.getApplyCode(), userInfoScope); //删除首页草稿
        return woTowerApplyDao.deleteTowerApply(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> saveTowerApply(TowerApply towerApply)
			throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String deptId = userInfoScope.getOrgId();
        String userid = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        
        towerApply.setApplicantNo(userid);
        towerApply.setApplicant( userName );
        towerApply.setApplyTime( new Date() );
        towerApply.setCreateuser(userid);
        towerApply.setCreateuserName(userName);
        towerApply.setCreateDate(new Date());
        towerApply.setSiteid(siteId);
        towerApply.setDeptid(deptId);
        towerApply.setDelFlag( "N" );
        towerApply.setApplyStatus("draft"); // 设置为草稿状态
        
        woTowerApplyDao.insertTowerApply( towerApply );
        
        String flowCode = towerApply.getApplyCode();
        String towerApplyId = towerApply.getId();
        // 添加“待办”
        String jumpPath = "workorder/towerApply/openNewTowerApplyPage.do?id=" + towerApplyId;
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
        String towerApplyCompany = towerApply.getApplyCompany();
        homeworkTask.setName(towerApplyCompany); // 名称
                
        homeworkTask.setProcessInstId(null); // 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName("登塔申请草稿"); // 状态
        homeworkTask.setType(HomepageWorkTask.TaskType.Draft); // 枚举类型定义是草稿还是流程,Draft
                                                                                                                        // 草稿;Process
                                                                                                                        // 流程实例
        homeworkTask.setTypeName("登塔申请"); // 类别
        homeworkTask.setUrl(jumpPath);// 扭转的URL
        homepageService.create(homeworkTask, userInfoScope); // 调用接口创建草稿

        // 将当前处理人设置为暂存提交人
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put("towerApplyId", towerApplyId);
        parmas.put("currHandler", userid);
        parmas.put("currHandlerName", userName);
        woTowerApplyDao.updateCurrHander( parmas );
       

        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put("towerApplyId", towerApplyId);
        resultHashMap.put("applyCode", flowCode);

        return resultHashMap;
	}

	@Override
	public Map<String, String> getTowerApplyCurrHanderInfo(
			UserInfoScope userInfoScope) throws Exception {
		Map<String, String> parmas = new HashMap<String, String>();
        
        String userIds = userInfoScope.getParam("userIds");
        if(userIds == null){
                return null;
        }
        HashMap<String, List<String>> userIdsMap = (HashMap<String, List<String>>) new ObjectMapper().readValue(userIds, Map.class);
        Iterator iterator = userIdsMap.keySet().iterator();
        while(iterator.hasNext()) {
                List<String> auditUserId = userIdsMap.get(iterator.next());
                String nextAuditUserIds = "";
                String nextAuditUserNames = "";
                for (int i = 0; i < auditUserId.size(); i++) {
                        nextAuditUserIds = auditUserId.get(i)+",";
                        String tempUserIds = auditUserId.get(i);
                        if(tempUserIds.indexOf(",")>0){
                                String[] auditUserNames = tempUserIds.split(",");
                                for (int j = 0; j < auditUserNames.length; j++) {
                                        nextAuditUserNames += iSecurityMaintenanceManager.retrieveUserById(auditUserNames[j]).getName()+",";
                                }
                        }else{
                                nextAuditUserNames = iSecurityMaintenanceManager.retrieveUserById(auditUserId.get(i)).getName()+",";
                        }
                }
                nextAuditUserIds = nextAuditUserIds.substring(0,nextAuditUserIds.length()-1);
                nextAuditUserNames = nextAuditUserNames.substring(0,nextAuditUserNames.length()-1);
                
                parmas.put("currHandler", nextAuditUserIds);
                parmas.put("currHandlerName", nextAuditUserNames);
        }

        return parmas;
	}


	@Override
    public void rollbackUpdateTowerApplyCurrHander(UserInfoScope userInfoScope, String towerApplyId) {
        String nextAuditUserId = "";
        String nextAuditUserName = "";
        try {
            nextAuditUserId = userInfoScope.getParam("userId");
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
        nextAuditUserName = iSecurityMaintenanceManager.retrieveUserById(nextAuditUserId).getName();
        
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put("towerApplyId", towerApplyId);
        parmas.put("currHandler", nextAuditUserId);
        parmas.put("currHandlerName", nextAuditUserName);
        woTowerApplyDao.updateCurrHander( parmas );
        
    }
	
	
	@Override
    @Transactional(rollbackFor = { Exception.class},propagation = Propagation.REQUIRED)
    public void obsoleteTowerApply(String towerApplyId) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        TowerApply towerApply = woTowerApplyDao.queryTowerApplyById( towerApplyId );
        String workflowId = towerApply.getWorkflowId();
        //获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks(workflowId);
        //刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get(0);
        String taskId = task.getId();

        logger.info("-------------作废登塔处理开始,登塔单号ID："+ towerApply.getId());
        // 终止流程
        workflowService.stopProcess(taskId, userId, userId, "作废");
        // 修改开工申请状态
        woTowerApplyDao.updateTowerApplyStatus(towerApplyId, TowerApplyStatusUtil.INVALIDATE);
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put("towerApplyId", towerApplyId);
        parmas.put("currHandler", "");
        parmas.put("currHandlerName", "");
        woTowerApplyDao.updateCurrHander( parmas );
        // 删掉对应的待办
        homepageService.complete(workflowId, userInfoScope, "已作废");
        
    }
	

	 

}
