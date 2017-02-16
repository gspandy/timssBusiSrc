package com.timss.attendance.service.dpp;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.facade.sec.ISecurityFacade;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;

@Component("AtdJYYSPSelectUser")
public class AtdJYYSPSelectUser implements SelectUserInterface {

	public final static Logger LOGGER = Logger.getLogger(AtdJYYSPSelectUser.class);
	@Autowired
    private ItcMvcService itcMvcService;
	@Autowired
	private IAuthorizationManager iAuthorizationManager;
	@Autowired
	private ISecurityFacade securityFacade;
	
	private static final Logger log = Logger.getLogger( AtdJYYSPSelectUser.class );
	
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		log.info( "-------------合理化建议第二环节申请部门建议员选人逻辑-----------------" );
		//获取申请人的所属部门
		String org_code = itcMvcService.getUserInfoScopeDatas().getOrgId();
		String first_org_code = org_code;
		//往上递归获取上级部门
		List<Organization> organizations = iAuthorizationManager.retriveOrgsByRelation(org_code, true, true);
		LOGGER.debug("###" + organizations.size());
		//获取一级部门
		if (organizations != null || organizations.size() != 0) {
			first_org_code = organizations.size() > 1 ? organizations.get(organizations.size()-2).getCode():org_code;
		}
		LOGGER.debug("### first_org_code = " + first_org_code);
		LOGGER.debug("### org_code = " + org_code);
		//获取一级部门下所有的分部建议员
		List<SecureUser> userlists = securityFacade.retriveActiveUsersWithSpecificGroup("DPP_HLH_JYY", first_org_code, "D");
		return userlists;
	}
}
