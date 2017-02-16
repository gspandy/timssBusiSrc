package com.timss.finance.service.core;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.finance.service.SubFlowService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Service
//@Scope("prototype")
public class SubFlowServiceImpl implements SubFlowService {
    
	@Autowired
	public RuntimeService runtimeService;
	
	@Autowired
	private ItcMvcService itcMvcService;
	
	@Autowired
	private WorkflowService wfs;
	
	@Autowired
	private IAuthorizationManager im;
	
	Logger logger = Logger.getLogger(SubFlowServiceImpl.class);
	
	//获取报销人数(多人报销用)
	@Override
	public List<String> getFinUserNum(Execution execution) {
	    logger.info("into getFinUserNum.");
	    String subProcessUserIds = (String)runtimeService.getVariable(execution.getProcessInstanceId(), "subProcessUserIds");
	    if(subProcessUserIds == null){
	    	return null;
	    }
	    String[] userIdsArray = subProcessUserIds.split(",");
	    List<String> userList=Arrays.asList(userIdsArray);          
	    return userList;   
	}
	
	//设置子流程用到的流程变量(多人报销用)
	@Override
	@Transactional
	public void setSubFlowVariables(String pid) {
	    logger.info("into setSubFlowVariables.pid: " + pid);
	    
		// 获取全局参数
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		List<SecureUser> suList = im.retriveUsersWithSpecificGroup(
		        siteid+"_INV_LEADER", null, true, true);// groupid,组织机构,是否包含下属机构，是否返回活动用户

		// 判断"领导分支"
		boolean checkIsLeader = false;
		for (SecureUser su : suList) {
			if (su.getId().contains(userInfoScope.getUserId())
					&& su.getName().contains(userInfoScope.getUserName())) {
				checkIsLeader = true;
			}
		}
		
		logger.info("checkIsLeader: " + checkIsLeader);
		
		//设置流程节点中的分支判断
		wfs.setVariable(pid, "isLeader", checkIsLeader);
	    
	    return;
	}
}
