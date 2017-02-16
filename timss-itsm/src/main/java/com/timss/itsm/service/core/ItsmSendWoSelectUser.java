package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.service.ItsmWoFaultTypeService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

@Component("ItsmSendWoSelectUser")
public class ItsmSendWoSelectUser implements SelectUserInterface {

	@Autowired
	//@Qualifier("WorkOrderServiceImpl")
	private ItsmWorkOrderService workOrderService;
	@Autowired
	private ItsmWoFaultTypeService woFaultTypeService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private ItsmWoUtilService woUtilService;
	@Autowired
    private IAuthorizationManager authManager;
	private static final Logger LOG = Logger.getLogger(ItsmSendWoSelectUser.class);
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		LOG.info("-------------进入派单的选人接口实现-----------------");
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		
		List<SecureUser> resultList = new ArrayList<SecureUser>();
		String woId = workflowService.getVariable(selectUserInfo.getProcessInstId(), "businessId").toString();
		ItsmWorkOrder workOrder = (com.timss.itsm.bean.ItsmWorkOrder) workOrderService.queryItWOById(woId).get("bean");
		String faultTypeId = workOrder.getFaultTypeId();
		//查询工单对应的一级服务目录
		ItsmWoFaultType ftRoot = woFaultTypeService.queryFTRootBySiteId(siteId);
		ItsmWoFaultType oneLevelFType = woUtilService.getOneLevelFTById(Integer.valueOf(faultTypeId), ftRoot.getId());
		String faultTypePrincipalGroup = oneLevelFType.getPrincipalGroup();
		
		resultList = authManager.retriveUsersWithSpecificGroup(faultTypePrincipalGroup, null, false, true);
		//TODO 根据工单的服务目录，选择用户组或者用户，然后计算出每个用户的手上工单数量
		List<SecureUser> nextResultList = authManager.retriveUsersWithSpecificRole(siteId+"_ITSM_WHGCS", null, false, true);
		nextResultList.removeAll(resultList);
		resultList.addAll(nextResultList);
		
		//TODO 获取每个工程师手上的工单数量
		resultList = setUserWoSum(resultList,siteId);
		
		return resultList;
	}
	//给工程师集合都设置工单数
	private List<SecureUser> setUserWoSum(List<SecureUser> resultList,String siteId) {
		// TODO Auto-generated method stub
		for (int i = 0; i < resultList.size(); i++) {
			SecureUser tempUser = resultList.get(i);
			int woSum = getWoSum(tempUser,siteId);
			String nameString = tempUser.getName();
			nameString +="( "+woSum+" )";
			tempUser.setName(nameString); //修改工程师名字，添加拥有的工单数量
		}
		return resultList;
	}
	//获取工程师的工单数量
	private int getWoSum(SecureUser tempUser,String siteId) {
		// TODO Auto-generated method stub
		String userId = tempUser.getId();
		int woSum = workOrderService.getUserWoSum(userId,siteId);
		return woSum;
	}

	

}
