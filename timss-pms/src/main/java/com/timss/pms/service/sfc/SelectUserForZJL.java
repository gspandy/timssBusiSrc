package com.timss.pms.service.sfc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

/**
 * 项目公司总经理节点选择逻辑自定义实现
 * @ClassName:     SelectUserForZJL
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-9-24 下午4:17:50
 */
@Service
public class SelectUserForZJL implements SelectUserInterface {
	@Autowired
	WorkflowService workflowService;
	@Autowired
	IAuthorizationManager iAuthorizationManager; 
	private static Map<String, String> map=new HashMap<String, String>();
	
	private static final Logger LOGGER=Logger.getLogger(SelectUserForZJL.class);
    static{
		map.put("SBS", "12177");
		map.put("SHL", "1233510");
		map.put("SYJ", "1233597*");
		map.put("SGX", "1233596*");
		map.put("SHP", "1233598*");
		map.put("SPY", "1233599*");
		map.put("DBW", "1233701");
		map.put("YMC", "12336");
	}
	@Override
	public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
		//获取流程实例id
		String processInstId=selectUserInfo.getProcessInstId();
		//获取合同归属字段值
		String belongTo=(String)workflowService.getVariable(processInstId, "belongTo");
		//获取对应的用户组
		String groupId="SFC_PMS_XMGSZJL";
		//获取用户组对应的所有用户
		List<SecureUser> secureUsers=iAuthorizationManager.retriveUsersWithSpecificGroup(groupId, getDeptCode(belongTo), true, true);
		LOGGER.info("流程的"+selectUserInfo.getProcessInstId()+"相关部门总经理总经理的审批节点人员是"+secureUsers);
		return secureUsers;
	}
	
	
	
	private String getDeptCode(String belongTo){
		String deptCode=map.get(belongTo);
		if(StringUtils.isBlank(deptCode)){
			deptCode="12335";
		}
		return deptCode;
	}

}
