package com.timss.attendance.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.facade.sec.ISecurityFacade;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 考勤用户和权限的通用函数
 */
@Service("atdUserPrivUtil")
public class AtdUserPrivUtil {
	private Logger log = LoggerFactory.getLogger( AtdUserPrivUtil.class );
	
	@Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private ISecurityFacade securityFacade;
    
	/**
	 * 根据用户拥有的考勤统计的数据查看权限，判断用户的角色权限等级<br/>
	 * 站点最高/可查看站点数据、部门最高/可查看部门数据、普通/仅可查看本人数据
	 * @return
	 */
	public String getStatPrivLevel(){
        //用户拥有的权限
        List<String> privList = getUserInfoScope().getSecureUser().getPrivileges();
        String level = "normal";
        if(privList!=null){
        	if(privList.contains("atd_stat_dept")){
        		level = "deptMgr";//部门主管，拥有部门数据的最高权限
        	}
        	if(privList.contains("atd_stat_site")){
        		level = "hrMgr";//人事主管，拥有考勤数据的最高权限
        	}
        }
        return level;
    }
	
	public UserInfoScope getUserInfoScope(){
        UserInfoScope userInfoScope = new UserInfoScopeImpl();
        try {
            userInfoScope = itcMvcService.getUserInfoScopeDatas();
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return userInfoScope;
    }
	
	/**
	 * 查询用户组用户id字符串，逗号分隔，用于判断用户是否属于用户组
	 * @param groupCode
	 * @return
	 */
	public String getGroupUserIdsStr(String groupCode){
		List<SecureUser>deptMgrUser=securityFacade.retriveActiveUsersWithSpecificGroup(groupCode, null, null);
		String str="";
		if(deptMgrUser!=null&&deptMgrUser.size()>0){
			for (SecureUser secureUser : deptMgrUser) {
				str+=secureUser.getId()+",";
			}
		}
		return str;
	}
	
	/**
	 * 用户是否属于某个用户组
	 * @param userId
	 * @param groupCode
	 * @return
	 */
	public Boolean isBelongGroup(String userId,String groupCode){
    	String groupUsers=getGroupUserIdsStr(groupCode);
    	return groupUsers.contains(userId+",");
    }
}
