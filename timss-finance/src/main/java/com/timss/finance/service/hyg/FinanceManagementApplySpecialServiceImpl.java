package com.timss.finance.service.hyg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.timss.facade.util.FlowVoidUtil;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.dao.FinanceManagementApplyDao;
import com.timss.finance.service.FMDetailListService;
import com.timss.finance.service.FinanceManagementApplyService;
import com.timss.finance.service.FinanceManagementApplySpecialService;
import com.timss.finance.util.ChangeStatusUtil;
import com.timss.finance.util.CommonUtil;
import com.timss.finance.vo.FinanceManagementApplyDtlVo;
import com.timss.pms.bean.FlowVoidParamBean;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.manager.support.IEnumerationManager;
import com.yudean.itc.util.ReflectionUtils;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * 管理费用申请的特殊类，处理一些特别的请求
 * 
 * @ClassName: FinanceManagementApplySpecialServiceImpl
 * @company: gdyd
 * @author: 黄晓岚
 * @date: 2015-4-13 上午10:32:44
 */

public class FinanceManagementApplySpecialServiceImpl implements FinanceManagementApplySpecialService {
    @Autowired
    FinanceManagementApplyService financeManagementApplyService;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    HomepageService homepageService;
    @Autowired
    WorkflowService workflowService;
    @Autowired
    FMDetailListService fmDetailListService;
    @Autowired
    FinanceManagementApplyDao financeManagementApplyDao;
    @Autowired
    private IEnumerationManager iEnumerationManager;
    @Autowired
    private IAuthorizationManager authManager;

    Logger logger = Logger.getLogger( FinanceManagementApplySpecialServiceImpl.class );

    @Override
    @Transactional
    public String tmpInsertFinanceManagementApply(FinanceManagementApply financeManagementApply,
            List<FinanceMainDetail> details) {
        // 初始化为草稿状态
        ChangeStatusUtil.changeToDraftStatus( financeManagementApply );
        financeManagementApplyService.insertFinanceManagementApply( financeManagementApply );

        // 创建首页草稿记录
        createOrUpdateHomepageDraft( financeManagementApply, "" );
        String id = financeManagementApply.getId();
        // 更新行政报销明细
        if ( details != null ) {
            fmDetailListService.insertFMDetailList( id, details );
        }

        return id;
    }

    @Override
    @Transactional
    public Map<String, Object> insertFinanceManagementApplyAndStartWorkflow(
            FinanceManagementApply financeManagementApply, List<FinanceMainDetail> details) {
        // 初始化为草稿状态
        ChangeStatusUtil.changeToDraftStatus( financeManagementApply );
        financeManagementApplyService.insertFinanceManagementApply( financeManagementApply );
        // 创建首页草稿记录
        createOrUpdateHomepageDraft( financeManagementApply, "" );
        // 更新行政报销明细
        if ( details != null ) {
            fmDetailListService.insertFMDetailList( financeManagementApply.getId(), details );
        }
        // 启动工作流
        Map<String, Object> resultMap = startWorkflow( financeManagementApply );
        return resultMap;

    }

    // 启动流程，如果流程有草稿
    @Transactional
    private Map<String, Object> startWorkflow(FinanceManagementApply financeManagementApply) {
        UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
        String workflowKey = "finance_" + infoScope.getSiteId().toLowerCase() + "_"
                + financeManagementApply.getApplyType();
        String defkey = workflowService.queryForLatestProcessDefKey( workflowKey );
        Map<String, Object> map = new HashMap<String, Object>();
        ProcessInstance processInstance = null;
        map.put( "businessId", financeManagementApply.getId() );
        //是否是部门经理
        isDeptManager(map);
        //是否是综合部或者财务部
        isInZhbOrCwb( map);
        //出差天数
        Date beginDate = financeManagementApply.getStrDate();
        Date endDate = financeManagementApply.getEndDate();
        int applyNum = daysOfTwo(beginDate,endDate)+1;
        map.put( "applyNum", applyNum );
        
        //省内还是省外出差
        map.put( "type", financeManagementApply.getType() );
        try {
            processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey, infoScope.getUserId(), map );
        } catch (Exception e) {
            throw new RuntimeException( "出差申请流程启动失败", e );
        }
        String processInstId = processInstance.getProcessInstanceId();
        workflowService.setVariable( processInstId, "orgId", infoScope.getOrgId() );
        
        updateHomePageDraft( financeManagementApply, infoScope, processInstId );
        // 获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );

        map.put( "taskId", task.getId() );
        map.put( "id", financeManagementApply.getId() );
        return map;
    }

    public static int daysOfTwo(Date fDate, Date oDate) {

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(fDate);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(oDate);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;

     }
    
    private void isDeptManager(Map<String, Object> map) {
        UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
        String commitUserId = infoScope.getUserId();
        List<SecureUser> bmjlList = authManager.retriveUsersWithSpecificRole( "HYG_BMJL", null, true, true );
        String result = "N";
        for ( SecureUser secureUser : bmjlList ) {
            if(commitUserId.equals( secureUser.getId() )){
                result = "Y";
            }
        }
        map.put( "isDeptManager", result );
    }

    /**
     * @description:是否综合部或者财务部
     * @author: 王中华
     * @createDate: 2016-11-14
     * @param map
     * @param financeManagementApply:
     */
    private void isInZhbOrCwb(Map<String, Object> map) {
        UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
      //申请部门的id
        String applyDeptId = infoScope.getOrgId();
        
      //综合部与财务部的id
        String zhbAndCwuIds = CommonUtil.getProperties( "fin-specialConfig", "hyg_zhAndCwAndCyIds" );
        String[] zhbAndCwuIdList = zhbAndCwuIds.split( "," );
        String result = "N";
        for ( String zhbOrCwbId : zhbAndCwuIdList ) {
            if ( zhbOrCwbId.indexOf( applyDeptId )>=0 ) {
                result = "Y";
            }
        }
        
        map.put( "isSpecialDept", result );
       
    }

    // 更新首页待办，并且更新业务对应流程id
    private void updateHomePageDraft(FinanceManagementApply financeManagementApply, UserInfoScope infoScope,
            String processInstId) {
        // 保存流程id到业务表
        financeManagementApply.setProInstId( processInstId );
        financeManagementApplyService.updateFinanceManagementApply( financeManagementApply );

        createOrUpdateHomepageDraft( financeManagementApply, processInstId );

    }

    // 创建获取更新待办草稿信息
    private void createOrUpdateHomepageDraft(FinanceManagementApply financeManagementApply, String processInstId) {
        UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = infoScope.getSiteId();
        String applyType = financeManagementApply.getApplyType();
        // 更新待办草稿信息
        String url = "finance/fma/editFMAJsp.do?id=" + financeManagementApply.getId() + "&applyType=" + applyType;

        String todoTypeName = "";
        // 创建待办
        List<AppEnum> enumNewTodoList = iEnumerationManager.retriveEnumerationsByCat( "FIN_APPLY_TYPE" );
        for ( AppEnum appEnum : enumNewTodoList ) {
            if ( appEnum.getCode().equals( applyType ) && siteid.equals( appEnum.getSiteId() ) ) {
                todoTypeName = appEnum.getLabel();
            }
        }
        String status = financeManagementApply.getStatus();
        if(!"D".equals(status)){//对退回到第一个节点更新
        	String flowCode = financeManagementApply.getId();
        	String flowName = financeManagementApply.getName();
        	if(flowName.length()>50){
           	 flowName = flowName.substring(0, 47)+"...";
            }
        	homepageService.modify(null, flowCode, null, flowName, null, null, null, null);
        }else{
        	UserInfo userinfo = itcMvcService.getUserInfoScopeDatas();
        	HomepageWorkTask homepageworktask = new HomepageWorkTask();
        	homepageworktask.setFlow( financeManagementApply.getId() );
        	homepageworktask.setProcessInstId( processInstId );
        	homepageworktask.setUrl( url );
        	homepageworktask.setTypeName( todoTypeName );
        	homepageworktask.setName( financeManagementApply.getName() );
        	homepageworktask.setStatusName( "草稿" );
        	homepageService.create( homepageworktask, userinfo );
        }
    }

    @Override
    @Transactional
    public String tmpUpdateFinanceManagementApply(FinanceManagementApply financeManagementApply,
            List<FinanceMainDetail> details) {
    	String processInstId = getWorkflowId( financeManagementApply.getId() );
    	if(processInstId ==null && "".equals(processInstId)){
    		// 初始化为草稿状态
    		ChangeStatusUtil.changeToDraftStatus( financeManagementApply );
    	}
        // 更新管理费用申请信息
        financeManagementApplyService.updateFinanceManagementApply( financeManagementApply );
        // 更新行政报销明细
        if(details != null){
            fmDetailListService.updateFMDetailList( financeManagementApply.getId(), details );
        }
        
        // 更新首页草稿信息
        updateHomePageDraft( financeManagementApply, itcMvcService.getUserInfoScopeDatas(), processInstId );
        String id = financeManagementApply.getId();
        return id;

    }

    @Override
    @Transactional
    public Map<String, Object> updateFinanceManagementApplyAndStartWorkflow(
            FinanceManagementApply financeManagementApply, List<FinanceMainDetail> details) {
        // 获取流程id
        String flowid = getWorkflowId( financeManagementApply.getId() );
        // 初始化为草稿状态
        ChangeStatusUtil.changeToDraftStatus( financeManagementApply );
        // 更新管理费用申请信息
        financeManagementApplyService.updateFinanceManagementApply( financeManagementApply );

        // 更新行政报销明细
        fmDetailListService.updateFMDetailList( financeManagementApply.getId(), details );

        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 如果流程为空则启动流程
        if ( StringUtils.isBlank( flowid ) ) {
            // 启动工作流
            resultMap = startWorkflow( financeManagementApply );
        } else {
            List<Task> activities = workflowService.getActiveTasks( flowid );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get( 0 );

            resultMap.put( "taskId", task.getId() );
            resultMap.put( "id", financeManagementApply.getId() );
        }
        return resultMap;
    }

    private String getWorkflowId(String id) {
        FinanceManagementApplyDtlVo financeManagementApplyDtlVo = financeManagementApplyService
                .queryFinanceManagementApplyById( id );

        return financeManagementApplyDtlVo.getProInstId();
    }

    @Override
    @Transactional
    public boolean updateFinanceManagementApplyApproving(FinanceManagementApply financeManagementApply,
            List<FinanceMainDetail> details) {
        ChangeStatusUtil.changeToApprovingStatus( financeManagementApply );
        financeManagementApplyService.updateFinanceManagementApply( financeManagementApply );
        if(details != null){
            fmDetailListService.updateFMDetailList( financeManagementApply.getId(), details );
        }
        
        return true;
    }

    @Override
    @Transactional
    public boolean updateFinanceManagementApplyApproved(FinanceManagementApply financeManagementApply,
            List<FinanceMainDetail> details) {
        ChangeStatusUtil.changeToApprovedStatus( financeManagementApply );
        financeManagementApplyService.updateFinanceManagementApply( financeManagementApply );
        fmDetailListService.updateFMDetailList( financeManagementApply.getId(), details );
        return false;
    }

    @Override
    @Transactional
    public boolean updateFinanceManagementApplyApproved(String id) {
        FinanceManagementApplyDtlVo financeManagementApplyDtlVo = financeManagementApplyService
                .queryFinanceManagementApplyById( id );
        ChangeStatusUtil.changeToApprovedStatus( financeManagementApplyDtlVo );
        financeManagementApplyService.updateFinanceManagementApply( financeManagementApplyDtlVo );
        return true;

    }

    @Override
    @Transactional
    public boolean deleteFinanceManagementApply(String id) {
        // 删除业务数据
        financeManagementApplyService.deleteFinanceManagementApply( id );
        // 删除行政报销明细
        fmDetailListService.deleteFMDetailList( id );

        String processInstId = getWorkflowId( id );

        // 如果流程存在，则删除流程和首页草稿记录，否则删除首页草稿记录
        if ( processInstId != null && !"".equals( processInstId ) ) {
            workflowService.delete( processInstId, "" );
        } else {
            homepageService.Delete( id, itcMvcService.getUserInfoScopeDatas() );
        }

        return true;
    }

    @Override
    @Transactional
    public boolean stopWorkflow(String financeManagementApplyId, String taskId) {
        String processInstId = getWorkflowId( financeManagementApplyId );
        if ( processInstId != null && !"".equals( processInstId ) ) {
            List<Task> activities = workflowService.getActiveTasks( processInstId );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get( 0 );
            taskId = task.getId();

            FlowVoidUtil flowVoidUtil = new FlowVoidUtil( workflowService, homepageService );
            FlowVoidParamBean params = new FlowVoidParamBean();
            params.setTaskId( taskId );
            params.setProcessInstId( processInstId );
            String userId = itcMvcService.getUserInfoScopeDatas().getUserId();
            params.setAssignee( userId );
            params.setOwner( userId );
            params.setUserInfo( itcMvcService.getUserInfoScopeDatas() );
            flowVoidUtil.initFlowVoid( params );
        }

        FinanceManagementApplyDtlVo financeManagementApplyDtlVo = financeManagementApplyService
                .queryFinanceManagementApplyById( financeManagementApplyId );
        ChangeStatusUtil.changeToVoidedStatus( financeManagementApplyDtlVo );
        financeManagementApplyService.updateFinanceManagementApply( financeManagementApplyDtlVo );
        return true;
    }

    @Override
    @Transactional
    public boolean updateFinanceManageApplyFlowStatus(String id, String flowStatus) {
        String siteid = itcMvcService.getUserInfoScopeDatas().getSiteId();
        String flowStatusName = null;
        List<AppEnum> enumflowStatusList = iEnumerationManager.retriveEnumerationsByCat( "FIN_COSTAPPLY_STATUS" );
        for ( AppEnum appEnum : enumflowStatusList ) {
            if(appEnum.getCode().equals( flowStatus ) && siteid.equals( appEnum.getSiteId() )){
                flowStatusName = appEnum.getLabel();
            }
        }
        financeManagementApplyDao.updateFinanceManagementApplyFlowStatus( id, flowStatus, flowStatusName );
        return true;
    }

    @Override
    @Transactional
    public boolean updateFinanceManagementApplyApproving(String id) {
        FinanceManagementApplyDtlVo financeManagementApplyDtlVo = financeManagementApplyService
                .queryFinanceManagementApplyById( id );
        ChangeStatusUtil.changeToApprovingStatus( financeManagementApplyDtlVo );
        financeManagementApplyService.updateFinanceManagementApply( financeManagementApplyDtlVo );
        return true;
    }

    @Override
    @Transactional
    public boolean setWFVariable(String taskId, String processInstId, FinanceManagementApply fma) {
        Map<String, String> elementMap = workflowService.getElementInfo( taskId );
        if ( elementMap != null ) {
            String template = (String) elementMap.get( "template" );
            if ( template != null && !template.equals( "" ) ) {
                String[] tems = template.split( "," );
                for ( int i = 0; i < tems.length; i++ ) {
                    String fieldName = tems[i];
                    Object value = ReflectionUtils.obtainFieldValue( fma, fieldName );
                    workflowService.setVariable( processInstId, fieldName, value );
                }
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean voidFlow(FlowVoidParamBean params) {
        // 作废前先设置流程变量
        String processInstId = params.getProcessInstId();
        workflowService.setVariable( processInstId, "needHQ", "N" );

        FlowVoidUtil flowVoidUtil = new FlowVoidUtil( workflowService, homepageService );
        flowVoidUtil.initFlowVoid( params );
        processInstId = flowVoidUtil.getProcessInstId();
        // 更新状态为作废
        updateProjectToVoided( params.getBusinessId(), processInstId );
        return true;
    }

    private boolean updateProjectToVoided(String businessId, String processInstId) {
        logger.info( "更新管理费用为作废状态，id为" + businessId );

        FinanceManagementApply financeManagementApply = financeManagementApplyService
                .queryFinanceManagementApplyById( businessId );
        ChangeStatusUtil.changeToVoidedStatus( financeManagementApply );
        financeManagementApplyDao.updateFinanceManagementApply( financeManagementApply );
        updateFinanceManageApplyFlowStatus( businessId, "V" );
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateFinApplyCurrHandlerUser(String applyId, UserInfoScope userInfoScope, String flag) {
        try {
            String userIds = "";
            if ( "normal".equals( flag ) ) {
                userIds = userInfoScope.getParam( "userIds" );
                if(userIds==null){  //从移动EIP审批过来，组转数据
                    List<String> userIdsList = java.util.Arrays.asList(userInfoScope.getParam( "userkey" ));;
                    Map<String, List<String>> tempUserIds = new HashMap<String, List<String>>();
                    tempUserIds.put( "xxx", userIdsList ); 
                    userIds = JSONObject.toJSONString( tempUserIds );
                }
                if ( userIds == null ) {
                    return;
                }
                HashMap<String, List<String>> userIdsMap = (HashMap<String, List<String>>) new ObjectMapper()
                        .readValue( userIds, Map.class );
                Iterator iterator = userIdsMap.keySet().iterator();
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
                                nextAuditUserNames += itcMvcService.getUserInfoById( auditUserNames[j] ).getUserName()
                                        + ",";
                            }
                        } else {
                            nextAuditUserNames = itcMvcService.getUserInfoById( auditUserId.get( i ) ).getUserName()
                                    + ",";
                        }
                    }
                    nextAuditUserIds = nextAuditUserIds.substring( 0, nextAuditUserIds.length() - 1 );
                    nextAuditUserNames = nextAuditUserNames.substring( 0, nextAuditUserNames.length() - 1 );

                    Map<String, String> parmas = new HashMap<String, String>();
                    parmas.put( "id", applyId );
                    parmas.put( "currHandlerUser", nextAuditUserIds );
                    parmas.put( "currHandUserName", nextAuditUserNames );
                    financeManagementApplyDao.updateCurrHandUserById( parmas );
                }
            } else if ( "rollback".equals( flag ) ) { // 回退的时候是单个人
                String nextAuditUserId = "";
                String nextAuditUserName = "";
                nextAuditUserId = userInfoScope.getParam( "userId" );
                if(nextAuditUserId==null){
                    nextAuditUserId = userInfoScope.getParam( "userkey" );
                }
                
                nextAuditUserName = itcMvcService.getUserInfoById( nextAuditUserId ).getUserName();

                Map<String, String> parmas = new HashMap<String, String>();
                parmas.put( "id", applyId );
                parmas.put( "currHandlerUser", nextAuditUserId );
                parmas.put( "currHandUserName", nextAuditUserName );
                financeManagementApplyDao.updateCurrHandUserById( parmas );
            }
        } catch (Exception e) {
            logger.error( e.getMessage() );
        }

    }

	@Override
	public boolean endFlow(FlowVoidParamBean params) {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public List<FinanceManagementApply> queryApplyList(String type, Date beginDate, Date endDATE, String siteid) {
        // TODO Auto-generated method stub
        return null;
    }

  
}
