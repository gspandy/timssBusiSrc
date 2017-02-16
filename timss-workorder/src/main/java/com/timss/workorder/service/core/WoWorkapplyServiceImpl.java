package com.timss.workorder.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WoapplyRisk;
import com.timss.workorder.bean.WoapplySafeInform;
import com.timss.workorder.bean.WoapplyWorker;
import com.timss.workorder.bean.Workapply;
import com.timss.workorder.dao.WoWorkapplyDao;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.service.WoWorkapplyService;
import com.timss.workorder.service.WoapplyRiskService;
import com.timss.workorder.service.WoapplySafeInformService;
import com.timss.workorder.service.WoapplyWorkerService;
import com.timss.workorder.util.WoapplyStatusUtil;
import com.timss.workorder.util.WoapplyUtil;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Service
public class WoWorkapplyServiceImpl implements WoWorkapplyService {
	@Autowired
	private WoWorkapplyDao woWorkapplyDao;
	@Autowired
        private WoapplyWorkerService woapplyWorkerService;
        @Autowired
        private WoapplyRiskService woapplyRiskService;
        @Autowired
        private WoapplySafeInformService woapplySafeInformService;
	@Autowired
        private ItcMvcService itcMvcService;
	@Autowired
        private WorkflowService workflowService;
	@Autowired
        private WoUtilService woUtilService;
	@Autowired
        HomepageService homepageService;
	
	private static Logger logger = Logger.getLogger(WoWorkapplyServiceImpl.class);

    @Override
    public Page<Workapply> queryAllWorkapply(Page<Workapply> page) throws Exception {
        logger.info( "查询所有开工申请" );
        List<Workapply> ret = woWorkapplyDao.queryAllWorkapply(page);
        page.setResults(ret);
        return page;
    }

    @Override
    public Map<String, Object> queryWorkapplyById(String id) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Workapply workapply = woWorkapplyDao.queryWorkapplyById(id);
        
        String taskId = "";
        // 获取当前活动节点
        if(workapply.getWorkflowId() != null){
            List<Task> activities = workflowService.getActiveTasks(workapply.getWorkflowId());
            if(activities.size() != 0 && activities != null && activities.get(0) != null){
                Task task = activities.get(0);
                taskId = task.getId();
            }
        }
        
        resultMap.put( "bean", workapply );
        resultMap.put( "taskId", taskId );
        return resultMap;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateWorkapply(Workapply workapply) {
        return  woWorkapplyDao.updateWorkapply( workapply);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteWorkapply(String id) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put("woapplyId", id);
        parmas.put("currHandlerUser", "");
        parmas.put("currHandUserName", "");
        woWorkapplyDao.updateCurrHander( parmas );
        Workapply workapply = woWorkapplyDao.queryWorkapplyById( id );
        homepageService.Delete(workapply.getWorkapplyCode(), userInfoScope); //删除首页草稿
        return woWorkapplyDao.deleteWorkapply(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> insertWorkapply(Workapply workapply) throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String deptId = userInfoScope.getOrgId();
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        workapply.setApplyTime( new Date() );
        workapply.setApplicant( userId );
        workapply.setCreatedate(new Date());
        workapply.setCreateuser(userId);
        workapply.setModifydate(new Date());
        workapply.setModifyuser(userId);
        workapply.setCurrHandler(userId);
        workapply.setCurrHandlerName(userName);
        workapply.setSiteid(siteId);
        workapply.setDeptid(deptId);
        workapply.setDelFlag( "N" );
        woWorkapplyDao.insertWorkapply( workapply );
        
        String woapplyId = workapply.getId(); // 获取要插入记录的ID

        // 启动流程
        String defkey = workflowService
                        .queryForLatestProcessDefKey("workorder_"
                                        + siteId.toLowerCase() + "_woapply");// 获取最新流程定义版本
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("businessId", woapplyId);
        logger.info("-------------CORE 启动流程开始，开工申请ID："+woapplyId+"---------------");
        // 启动流程
        ProcessInstance processInstance = workflowService
                        .startLatestProcessInstanceByDefKey(defkey,
                                        userInfoScope.getUserId(), map);
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();
        //部门ID设置到流程变量里面
        workflowService.setVariable( processInstId, "orgId", deptId );
        logger.info("-------------CORE 启动流程结束，开工申请ID："+woapplyId+"  流程实例ID："+processInstId+"---------------");
        
        workapply.setWorkflowId(processInstId);
        workapply.setApplyStatus("txkgsq");  //填写开工申请

        woWorkapplyDao.updateWorkflowId(woapplyId,processInstId);
        // 加入待办列表
        String jumpPath = "workorder/workapply/openNewWoapplyPage.do?woapplyId=" + woapplyId;
        // homepageService.createProcess(flowCode, processInstId, "开工申请", "开工申请审核",
        // "班长审核", jumpPath, userInfoScope);
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow(workapply.getWorkapplyCode());// 编号
        homeworkTask.setName(workapply.getName()); // 名称
        homeworkTask.setProcessInstId(processInstId);// 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName("填写开工申请"); // 状态
        homeworkTask.setType(HomepageWorkTask.TaskType.Process); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                                                                                                                                // 草稿;xxxx.Process
                                                                                                                                // 流程实例
        homeworkTask.setTypeName("开工申请"); // 类别
        homeworkTask.setUrl(jumpPath); // 扭转的URL
        // 加入待办列表
        homepageService.create(homeworkTask, userInfoScope);

        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks(processInstId);
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get(0);

        Map<String, String> parmas = woUtilService.getWoapplyCurrHanderInfo( userInfoScope );
        if(parmas != null){
            parmas.put( "woapplyId", woapplyId );
            woWorkapplyDao.updateCurrHander( parmas );
        }
       
        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "workflowId", processInstId );
        resultHashMap.put( "taskId", task.getId() );
        resultHashMap.put( "woapplyId", woapplyId );
        return resultHashMap;
    }

    @Override
    public Map<String, Object> saveWorkapply(Workapply workapply) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String siteId = userInfoScope.getSiteId();
        String deptId = userInfoScope.getOrgId();
        workapply.setApplyTime( new Date() );
        workapply.setApplicant( userId );
        workapply.setCreatedate(new Date());
        workapply.setCreateuser(userId);
        workapply.setModifydate(new Date());
        workapply.setModifyuser(userId);
        workapply.setSiteid(siteId);
        workapply.setDeptid(deptId);
        workapply.setDelFlag( "N" );
        workapply.setApplyStatus("draft"); // 设置为草稿状态
        
        woWorkapplyDao.insertWorkapply( workapply );

        String flowCode = workapply.getWorkapplyCode();
        String woapplyId = workapply.getId();
        // 添加“待办”
        String jumpPath = "workorder/workapply/openNewWoapplyPage.do?woapplyId=" + woapplyId;
        // homepageService.createProcess(flowCode, processInstId, "工单", "工单草稿",
        // "草稿", jumpPath, userInfoScope);
        // 构建Bean
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        homeworkTask.setFlow(flowCode);// 编号，如采购申请 WO20140902001
        String woapplyName = workapply.getName();
        homeworkTask.setName(woapplyName); // 名称
                
        homeworkTask.setProcessInstId(null); // 草稿时流程实例ID可以不用设置
        homeworkTask.setStatusName("开工申请草稿"); // 状态
        homeworkTask.setType(HomepageWorkTask.TaskType.Draft); // 枚举类型定义是草稿还是流程,Draft
                                                                                                                        // 草稿;Process
                                                                                                                        // 流程实例
        homeworkTask.setTypeName("开工申请"); // 类别
        homeworkTask.setUrl(jumpPath);// 扭转的URL
        homepageService.create(homeworkTask, userInfoScope); // 调用接口创建草稿

        // 将当前处理人设置为暂存提交人
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put("woapplyId", woapplyId);
        parmas.put("currHandlerUser", userId);
        parmas.put("currHandUserName", itcMvcService.getUserInfoById(userId)
                        .getUserName());
        woWorkapplyDao.updateCurrHander( parmas );
       

        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put("woapplyId", woapplyId);
        resultHashMap.put("workapplyCode", flowCode);

        return resultHashMap;
    }

    @Override
    public Map<String, String> updateWorkapply(Map<String, Object> addWADataMap) throws Exception{
        
        Map<String, String> returnResult = new HashMap<String, String>();
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String deptId = userInfoScope.getOrgId();
        String workapplyFormDate = (String) addWADataMap.get("woapplyFormData"); // 开工申请基本信息表单数据
        String updateStyle = (String) addWADataMap.get("updateStyle");
        String safeInformUser = (String) addWADataMap.get("safeInformUser");
        Workapply workapply = JsonHelper.toObject(workapplyFormDate,Workapply.class);
        workapply.setSafeInformUser( safeInformUser );
        Workapply oldWorkapply = woWorkapplyDao.queryWorkapplyById(workapply.getId());

        //TODO 删除安全交底、交底内容、外来施工人员、风险评估内容
        woapplySafeInformService.deleteSafeInformByWoapplyId( workapply.getId() );
        woapplyWorkerService.deleteWoapplyWorkerByWoapplyId( workapply.getId() );
        woapplyRiskService.deleteRiskListByWoapplyId( workapply.getId() );
        
        if ("commit".equals(updateStyle)) { // 提交草稿，启动流程
                // 启动流程
                String defkey = workflowService
                                .queryForLatestProcessDefKey("workorder_"
                                        + siteId.toLowerCase() + "_woapply");// 获取最新流程定义版本
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("businessId", workapply.getId());

                // 启动新流程
                logger.info("-------------CORE 启动流程开始，工单编号："+workapply.getWorkapplyCode()+"---------------");
                ProcessInstance processInstance = workflowService
                                .startLatestProcessInstanceByDefKey(defkey,
                                                userInfoScope.getUserId(), map);
                // 获取流程实例ID
                String processInstId = processInstance.getProcessInstanceId();
                //部门ID设置到流程变量里面
                workflowService.setVariable( processInstId, "orgId", deptId );
                logger.info("-------------CORE 启动流程结束，工单编号："+workapply.getWorkapplyCode()+"  流程实例ID："+processInstId+"---------------");
                
                workapply.setWorkflowId(processInstId);
                String woapplyId = workapply.getId();
                //更新流程实例ID
                woWorkapplyDao.updateWorkflowId(woapplyId,processInstId);
                

                // 第一次启动流程 添加“待办”(未测试)
                String flowCode = oldWorkapply.getWorkapplyCode();
                
                String jumpPath = "workorder/workapply/openNewWoapplyPage.do?woapplyId=" + woapplyId;
                // 构建Bean
                HomepageWorkTask homeworkTask = new HomepageWorkTask();
                homeworkTask.setFlow(flowCode);// 编号，如工单编号 WO20140902001
                homeworkTask.setName(workapply.getName()); // 名称
                homeworkTask.setProcessInstId(processInstId);// 草稿时流程实例ID可以不用设置
                homeworkTask.setStatusName("项目负责人审批"); // 状态
                homeworkTask.setType(HomepageWorkTask.TaskType.Process); // 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
                homeworkTask.setTypeName("开工申请"); // 类别
                homeworkTask.setUrl(jumpPath); // 扭转的URL
                // 加入待办列表
                homepageService.create(homeworkTask, userInfoScope);
                //更新开工申请单内容
                woWorkapplyDao.updateWorkapply( workapply );
                // 获取当前活动节点
                List<Task> activities = workflowService.getActiveTasks(processInstId);
                // 刚启动流程，第一个活动节点肯定是属于当前登录人的
                Task task = activities.get(0);
                
                returnResult.put("workflowId",processInstId );
                returnResult.put("taskId", task.getId());

        } else if ("save".equals(updateStyle)) { // 再次暂存草稿，不启动流程
                workapply.setApplyStatus(oldWorkapply.getApplyStatus()); // 设置为草稿状态
                workapply.setWorkflowId(oldWorkapply.getWorkflowId());
                workapply.setModifydate(new Date());
                workapply.setModifyuser(userInfoScope.getUserId());
                woWorkapplyDao.updateWorkapply( workapply );
              //更新待办
                String flowCode = workapply.getWorkapplyCode();
                HomepageWorkTask homeworkTask = new HomepageWorkTask();
                homeworkTask.setFlow(flowCode);// 编号，如工单编号 WO20140902001
                homeworkTask.setName(workapply.getName()); // 名称
                homepageService.create(homeworkTask, userInfoScope);
                
                returnResult.put("workflowId", "noFlow");
                returnResult.put("taskId", "noTask");
        }
      //TODO 插入安全交底、交底内容、外来施工人员、风险评估内容
        insertWoapplyListData(addWADataMap,workapply.getId());
        
        return returnResult;
    }

    private void insertWoapplyListData(Map<String, Object> addWADataMap,String woapplyId) {
        
        @SuppressWarnings("unchecked")
        List<WoapplySafeInform> woapplySafeInformList = (List<WoapplySafeInform>) addWADataMap.get( "safeinformList");  //安全交底内容
        @SuppressWarnings("unchecked")
        ArrayList<WoapplyWorker> woapplyWorkerList = (ArrayList<WoapplyWorker>) addWADataMap.get( "workerList");  //外来队伍施工人员
        @SuppressWarnings("unchecked")
        ArrayList<WoapplyRisk> riskAssessmentList = (ArrayList<WoapplyRisk>) addWADataMap.get( "riskAssessmentList");  //风险评估
        
        WoapplyUtil.setWoapplyIdInList( woapplySafeInformList, woapplyWorkerList, riskAssessmentList, woapplyId );
    
      //添加安全交底内容
        woapplySafeInformService.insertWoapplySafeInformList( woapplySafeInformList );
        //添加施工人员信息
        woapplyWorkerService.insertWoapplyWorkerList( woapplyWorkerList );
        //添加风险评估信息
        woapplyRiskService.insertWorkapplyList( riskAssessmentList );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class},propagation = Propagation.REQUIRED)
    public void obsoleteWorkapply(String woapplyId) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        Workapply workapply = woWorkapplyDao.queryWorkapplyById( woapplyId );
        String workflowId = workapply.getWorkflowId();
        //获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks(workflowId);
        //刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get(0);
        String taskId = task.getId();

        logger.info("-------------作废工单处理开始,工单ID："+ workapply.getId());
        // 终止流程
        workflowService.stopProcess(taskId, userId, userId, "作废");
        // 修改开工申请状态
        woUtilService.updateWoaplyStatus(woapplyId, WoapplyStatusUtil.INVALIDATE);
        Map<String, String> parmas = new HashMap<String, String>();
        parmas.put("woapplyId", woapplyId);
        parmas.put("currHandlerUser", "");
        parmas.put("currHandUserName", "");
        woWorkapplyDao.updateCurrHander( parmas );
        // 删掉对应的待办
        homepageService.complete(workflowId, userInfoScope, "已作废");
        
    }
	 
	

	 

}
