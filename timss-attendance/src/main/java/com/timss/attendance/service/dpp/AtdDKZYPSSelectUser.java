package com.timss.attendance.service.dpp;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.attendance.util.AtdUserPrivUtil;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;


@Component("AtdDKZYPSSelectUser")
public class AtdDKZYPSSelectUser implements SelectUserInterface{
	
	@Autowired
	private WorkflowService workflowService;
	 
	@Autowired
	private SelectUserService selectUser;
	
	@Autowired
    private AtdUserPrivUtil privUtil;
	
	private static final Logger log = Logger.getLogger( AtdDKZYPSSelectUser.class );
	
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		log.info( "-------------合理化建议第四环节对口专业评审小姐评审选人逻辑-----------------" );
		String rationType=workflowService.getVariable(selectUserInfo.getProcessInstId(), "rationType").toString();
		rationType=rationType.replace("RATION", "HLH");
		log.info("msg="+rationType);
		String rationTypeUserGroup = rationType.concat("ZZ");
		log.info("rationTypeUserGroup="+rationTypeUserGroup);
		List<SecureUser> userList =selectUser.byGroup(rationType);
		Iterator it = userList.iterator();  
		while(it.hasNext()) {  
			SecureUser u = (SecureUser) it.next();
			/*如果是组长，则删除组长返回*/
			log.info("isBelongGroup="+privUtil.isBelongGroup(u.getId(), rationTypeUserGroup));
            if (privUtil.isBelongGroup(u.getId(), rationTypeUserGroup)) {
				it.remove();
			}
		}  
		return userList;
	}

}
