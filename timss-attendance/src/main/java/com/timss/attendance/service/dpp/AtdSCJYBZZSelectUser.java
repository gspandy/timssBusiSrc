package com.timss.attendance.service.dpp;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.attendance.util.AtdUserPrivUtil;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.facade.sec.ISecurityFacade;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;

@Component("AtdSCJYBZZSelectUser")
public class AtdSCJYBZZSelectUser implements SelectUserInterface {

	@Autowired
	private WorkflowService workflowService;
	 
	@Autowired
	private SelectUserService selectUser;
	
	@Autowired
    private AtdUserPrivUtil privUtil;
	
	private static final Logger log = Logger.getLogger( AtdSCJYBZZSelectUser.class );
	
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		log.info( "-------------合理化建议第八环节生产经营部专责初评选人逻辑-----------------" );
		String rationType=workflowService.getVariable(selectUserInfo.getProcessInstId(), "rationType").toString();
		rationType=rationType.replace("RATION", "HLH");
		log.info("rationTypeUserGroup="+rationType);
		String rationTypeUserGroup = rationType.concat("ZZ");
		log.info("rationTypeUserGroup="+rationTypeUserGroup);
		List<SecureUser> userList =selectUser.byGroup(rationType);
		Iterator it = userList.iterator();  
		while(it.hasNext()) {  
			SecureUser u = (SecureUser) it.next();
			log.info("scjybisBelongGroup="+privUtil.isBelongGroup(u.getId(), rationTypeUserGroup));
			/*如果是组长，则删除组长返回*/
            if (!privUtil.isBelongGroup(u.getId(), rationTypeUserGroup)) {
				it.remove();
			}
		}  
		return userList;
	}
}
