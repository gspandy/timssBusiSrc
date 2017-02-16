package com.timss.finance.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

@Component("FinanceSelectUserServiceImpl")
public class FinanceSelectUserServiceImpl implements SelectUserInterface {

	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	IAuthorizationManager im;
	
	@Autowired
	private ItcMvcService itcMvcService;
	
	@Autowired
	private OrganizationMapper om;
	
	@Override
	//自己/他人报销选择部门经理调用的选人逻辑
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
	    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	        String siteid = userInfoScope.getSiteId();
		//获取实际报销人
		String beneficiaryid = (String) workflowService.getVariable(selectUserInfo.getProcessInstId(), "beneficiaryid");

		List<Organization> organList = om.selectOrgUserBelongsTo(beneficiaryid);
		
		//获取部门编号
		String orgCode = organList.get(0).getCode();
		
		//获取实际报销人所属部门经理 TODO 要根据站点实现动态的配置
		List<SecureUser> suList = selectUserInfo.getAm().retriveUsersWithSpecificRole( siteid+"_BMJL", orgCode, true, true );
		
		return suList;
	}
}
