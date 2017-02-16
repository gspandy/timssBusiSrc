package com.timss.ptw.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.timss.ptw.service.PtwPtoSelectUserService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;

public class PtwAuditPrivUtil {
	private  static final Logger LOGGER  = Logger.getLogger(PtwAuditPrivUtil.class);
	
	/**
     * @description:待办用户列表
     * @author: 朱旺
     * @createDate: 2016-2-16
     * @param itcMvcService 
     * @param ptwPtoSelectUserService
     * @param ptwTypeCode 工作票类型
     * @param wtStatus 工作票状态码
     * @param siteId 站点
     * @return:
     */
	public static List<UserInfo> queryNextUserList(ItcMvcService itcMvcService, PtwPtoSelectUserService ptwPtoSelectUserService, String ptwTypeCode, int wtStatus, String siteId){
		List<SecureUser> secureUserList;
        List<UserInfo> nextUserList = new ArrayList<UserInfo>();
		try {
			secureUserList = ptwPtoSelectUserService.selectUsersWithoutWorkFLow("ptw", ptwTypeCode, Integer.toString(wtStatus));
			 for(SecureUser secureUser:secureUserList){
				 UserInfo userInfo = itcMvcService.getUserInfo(secureUser.getId(), siteId); 
				 nextUserList.add(userInfo);
		     }
		} catch (Exception e1) {
			LOGGER .error("查询两票审核人信息失败");
		}
		return nextUserList;
	}
}
