package com.timss.inventory.workflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.service.core.InvMatAcceptService;
import com.timss.inventory.vo.InvMatAcceptDtlVo;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

/**
 * 
 * 执行验收选人逻辑
 * @description: {desc}
 * @company: gdyd
 * @className: SelectUserForXGBM.java
 * @author: 890145
 * @createDate: 2015-11-7
 * @updateUser: 890145
 * @version: 1.0
 */
@Service
public class SelectUserForZXYS implements SelectUserInterface{
	@Autowired
	WorkflowService workflowService;
	@Autowired
	IAuthorizationManager iAuthorizationManager; 
	@Autowired
	InvMatAcceptService iMatAcceptService;
	@Autowired
	ItcMvcService itcMvcService;
	private static final Logger LOGGER=Logger.getLogger(SelectUserForZXYS.class);
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		LOGGER.info("开始获取流程"+selectUserInfo.getProcessInstId()+"的执行验收审批节点审批人员");
		//获取流程实例id
		String processInstId=selectUserInfo.getProcessInstId();
		//获取流程id
		String inacId=(String)workflowService.getVariable(processInstId, "inacId");
		//根据详情的采购申请人获取id
		InvMatAcceptDtlVo invMatAcceptVO=iMatAcceptService.queryInvMatAcceptById(inacId);
	    InvMatAcceptDetail detail= invMatAcceptVO.getInvMatAcceptDetails().get(0);
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		
		SecureUser secureUse=iAuthorizationManager.retriveUserById(detail.getPurapplyUsercode(), userInfo.getSiteId());
		List<SecureUser> secureUsers=new ArrayList<SecureUser>();
		secureUsers.add(secureUse);
		return secureUsers;
	}

	

}
