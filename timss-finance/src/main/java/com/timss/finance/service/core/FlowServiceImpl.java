package com.timss.finance.service.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.finance.bean.FinanceFlowMatch;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinancePageConfig;
import com.timss.finance.dao.FinanceFlowMatchDao;
import com.timss.finance.dao.FinanceMainDao;
import com.timss.finance.dao.FinancePageConfigDao;
import com.timss.finance.exception.FinanceBaseException;
import com.timss.finance.service.FinanceFlowMatchService;
import com.timss.finance.service.FinanceMainService;
import com.timss.finance.service.FlowService;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.utils.WorkFlowConstants;

@Service
public class FlowServiceImpl implements FlowService {
	private Logger logger = Logger.getLogger(FlowServiceImpl.class);
	
	private final String viewBasePath = "finance/financeInfoController/viewFinanceInfo.do?";

	@Autowired
	WorkflowService wfs;
	
	@Autowired
	FinanceFlowMatchService financeFlowMatchService;
	
	@Autowired
	public RuntimeService runtimeService;
	
	@Autowired
	private ItcMvcService itcMvcService;
	
	@Autowired
	HomepageService homepageService;
	
	@Autowired
	WorkflowService workflowService;
	
	@Autowired
	FinanceMainService financeMainService;
	
	@Autowired
	FinanceFlowMatchDao financeFlowMatchDao;
	
	@Autowired
	FinanceMainDao financeMainDao;
	@Autowired
	FinancePageConfigDao financePageConfigDao;
	@Autowired
	IAuthorizationManager im;
	
	
	@Override
	public Map<String, Object> startWorkflow(FinanceMain getMain,
			List<FinanceMainDetail> mainDtlList, String finNameEn,
			String finTypeEn, UserInfoScope userInfoScope)  {
	    String siteid = userInfoScope.getSiteId();
	    // 此处需要从数据库中读取，需重构，考虑不同站点的情况
	    FinancePageConfig finPageConfig = financePageConfigDao.getFinPageConf( finNameEn, finTypeEn, siteid );
	    String flowNameEn = finPageConfig.getFlowKey();
//		String flowNameEn = FinanceUtil.genFlowNameEn(finNameEn, finTypeEn);
		
		
		String defkey = wfs.queryForLatestProcessDefKey(flowNameEn);// 流程名称，不需要带version

		logger.info("流程定义key:" + defkey);

		Map<String, Object> taskMap = new HashMap<String, Object>();

		if (finTypeEn.equals("only") || finTypeEn.equals("other")) {
			taskMap.put( "mainDtlList", mainDtlList );
			taskMap.put( "fid", getMain.getFid() );
			taskMap.put( "finType", finTypeEn );
		} else if (finTypeEn.equals("more")) { //这里应该判断多人报销开启流程且进入子流程时才走这个分支
			String userIds;
			String childBusinessIds;
			String url;
			StringBuffer userIdsBuf = new StringBuffer();
			StringBuffer childBusinessIdsBuf = new StringBuffer();
			StringBuffer urlBuf = new StringBuffer();
			
			for (int i = 0; i < mainDtlList.size(); i++) {
				if(i != mainDtlList.size()-1) {
					userIdsBuf.append(mainDtlList.get(i).getBeneficiaryid()).append(",");
					childBusinessIdsBuf.append(mainDtlList.get(i).getId()).append(",");
				} else {
					userIdsBuf.append(mainDtlList.get(i).getBeneficiaryid());
					childBusinessIdsBuf.append(mainDtlList.get(i).getId());
				}
			}
			
			urlBuf.append("/finance/financeInfoController/viewFinanceInfo.do").
				append("?finTypeEn=").append(finTypeEn);
			
			logger.info("userIdsBuf: " + userIdsBuf);
			logger.info("childBusinessIdsBuf: " + childBusinessIdsBuf);
			logger.info("urlBuf: " + urlBuf);
			
			userIds = new String(userIdsBuf);
			childBusinessIds = new String(childBusinessIdsBuf);
			url = new String(urlBuf);
			
			//父业务id
			taskMap.put(WorkFlowConstants.BUSINESS_ID, getMain.getFid());
			//父业务id
			taskMap.put(WorkFlowConstants.PARENT_BUSINESS_ID, getMain.getFid());
			//决定子流程个数以及候选人
			taskMap.put(WorkFlowConstants.SUB_PROCESS_USER_IDS, userIds);
			//业务id
			taskMap.put(WorkFlowConstants.SUB_PROCESS_BUSINESS_IDS, childBusinessIds);
			//子流程待办跳转路径
			taskMap.put(WorkFlowConstants.SUB_PROCESS_URL, url);
			//绑定子流程与业务id的类名
			taskMap.put(WorkFlowConstants.BIND_CLASS_NAME, "com.timss.finance.service.FlowService");
			//绑定子流程与业务id的方法名
			taskMap.put(WorkFlowConstants.BIND_CLASS_METHOD, "bindSubFlow");
			//子流程待办中的类别
			taskMap.put(WorkFlowConstants.TYPE_NAME, "财务报销");
			//子流程待办中的业务名称(报销单名称)
			taskMap.put(WorkFlowConstants.BUSINESS_NAME, getMain.getFname());
			
			//报销金额
			taskMap.put( "amount", getMain.getTotal_amount() );
			

		}

		logger.info("###before startLatestProcessInstanceByDefKey--------------");
        
		ProcessInstance pi=null;
		try{
		    pi= wfs.startLatestProcessInstanceByDefKey(defkey,
				userInfoScope.getUserId(), taskMap);
		}catch (Exception e) {
		    throw new FinanceBaseException("流程启动失败，将要启动的流程key为"+defkey, e);
		}
		// 获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
		List<Task> activities = wfs.getActiveTasks(pi.getId());
		// 刚启动流程，第一个活动节点肯定是属于当前登录人的
		Task task = activities.get(0);

		logger.info("task.getId(): " + task.getId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskId", task.getId());
		map.put("pid", pi.getId());
		return map;
	}

	@Override
	public String getCandidateUsers(String taskId, String loginId){
		List<String> list = wfs.getCandidateUsers(taskId);
		String result = "others";
		
		for (int i = 0; i < list.size(); i++) {
			if (loginId.equals((String)list.get(i))) {
				result = "approver";
				break;
			}
		}
		
		return result;
	}
	/**
	 * 绑定业务id和流程ID
	 * 
	 * */
	@Override
	public void bindSubFlow(String businessId, String processInstId)  {
		logger.info("--------------businessId : " + businessId + ". processInstId : "+ processInstId);
		FinanceFlowMatch financeFlowMatch = new FinanceFlowMatch();
		financeFlowMatch.setPid(processInstId); //流程实例id
		financeFlowMatch.setFid(businessId); //业务id
		financeFlowMatchService.insertFinanceFlowMatch(financeFlowMatch);
	}

//	@Override
//	public List<String> getFinUserNum1(Execution execution) {
//	    logger.info("into getFinUserNum.");
//	    String userIds = (String)runtimeService.getVariable(execution.getProcessInstanceId(), "userIds");
//	    
//	    logger.info("userIds: " + userIds);
//	    
//	    if(userIds == null){
//	    	return null;
//	    }
//	    String[] userIdsArray = userIds.split(",");
//	    List<String> userList=Arrays.asList(userIdsArray);          
//	    return userList;   
//	}

	/**
	 * 终止工作流
	 * 
	 * **/
	@Override
	@Transactional(rollbackFor = { Exception.class},propagation = Propagation.REQUIRED)
	public void stopWorkFlow(String taskId, String assignee, String owner,
			String message, String businessId) {
		wfs.stopProcess(taskId, assignee, owner, message);
		return;
	}

	/** 
	 * @description: 回滚工作流操作
	 * @author: 890170
	 * @createDate: 2015-1-5
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class},propagation = Propagation.REQUIRED)
	//1.删除业务和pid关联;2.调用接口删除工作流信息;3.置为草稿状态;4.删除待办信息;1 5.增加一条草稿信息;1
	public String rollbackWorkFlowOpr(String fid)  {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		
		FinanceFlowMatch financeFlowMatch = financeFlowMatchDao.queryFinanceFlowMatchByFid(fid);
		FinanceMain financeMain = financeMainDao.queryFinanceMainByFid(fid);
		
		//1.删除业务和pid关联
		financeFlowMatchDao.deleteFinanceFlowMatch(financeFlowMatch);
		
		//2.修改成"草稿"状态
		financeMainService.updateFinanceMainStatusByFid( "finance_draft", fid );
		
		//3.调用接口删除工作流信息
		workflowService.delete(financeFlowMatch.getPid(), "取消提交报销单");
		
		//4.删掉启动流程的待办
		homepageService.Delete(financeFlowMatch.getPid(), userInfoScope);
		 
		//5.加入草稿待办
        HomepageWorkTask hwt = new HomepageWorkTask();
        hwt.setFlow(fid);
        hwt.setProcessInstId(fid); // 草稿时流程实例ID设置成fid
        hwt.setTypeName("财务报销"); // 类别
        hwt.setName(financeMain.getFname()); //名称
        hwt.setStatusName("草稿"); // 状态
        hwt.setUrl(viewBasePath + "businessId=" + fid); //跳转地址
        homepageService.create(hwt, userInfoScope); // 调用接口创建草稿
		
		return "success";
	}
	
	/** 
	 * @description: 作废报销单
	 * @author: 890170
	 * @createDate: 2015-1-7
	 */
	@Override
	@Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
	public Map<String, Object> abolishFinanceInfo(String fid)  {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		FinanceFlowMatch financeFlowMatch = financeFlowMatchDao.queryFinanceFlowMatchByFid(fid);
		String pid = financeFlowMatch.getPid();
		//获取当前活动节点
		List<Task> activities = workflowService.getActiveTasks(pid);
		//刚启动流程，第一个活动节点肯定是属于当前登录人的
		Task task = activities.get(0);
		String taskId = task.getId();
	
		logger.info("-------------作废报销单处理开始,报销单ID："+ fid);
		//终止流程
		workflowService.stopProcess(taskId, userId, userId, "作废报销单");
		//修改报销单状态
		financeMainService.updateFinanceMainStatusByFid( "finance_abolish", fid );
		//删掉对应的待办
		homepageService.complete(pid, userInfoScope, "已作废");
		
		resultMap.put("result", "success");
		
		return resultMap;
	}
}
