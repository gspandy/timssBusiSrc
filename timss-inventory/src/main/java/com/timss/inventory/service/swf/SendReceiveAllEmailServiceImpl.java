package com.timss.inventory.service.swf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.purchase.service.PurOrderService;
import com.timss.purchase.service.PurPayService;
import com.timss.purchase.vo.PurPayVO;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMsgService;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class SendReceiveAllEmailServiceImpl implements SendReceiveAllEmailService {

	private static final Logger LOGGER = Logger.getLogger(SendReceiveAllEmailServiceImpl.class);
	private static final String MAIL_TEMPLATE = "inv_wzrk";
	private static final String RECEIVE_ROLE = "SWF_CGY";
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    public IAuthorizationManager authManager;
    @Autowired
    private PurOrderService purOrderService;
    @Autowired
    ItcMsgService itcMsgService;
	
	@Override
	public void sendEmail(String purOrderNo,String purOrderName) throws RuntimeException, Exception {
		
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		Map<String, Object> bindMap = new HashMap<String, Object>();

		bindMap.put("contractNo", purOrderNo);
		bindMap.put("contractName", purOrderName);
		// bindMap.put("url", url);

		// 获取邮件接收人
		List<SecureUser> us = authManager.retriveUsersWithSpecificRoleAndSite(RECEIVE_ROLE, userInfo.getSiteId());

		if (us != null && us.size() != 0) {
			List<UserInfo> sendUserList = new ArrayList<UserInfo>();
			for (SecureUser user : us) {
				sendUserList.clear();
				UserInfo u = itcMvcService.getUserInfoById(user.getId());
				sendUserList.add(u);
				itcMsgService.SendMail(MAIL_TEMPLATE, bindMap, sendUserList, userInfo);
			}
		} else {
            LOGGER.info("按角色查询不到人员.角色为" + RECEIVE_ROLE);
		}
      

	}

}
