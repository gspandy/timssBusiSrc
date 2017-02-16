package com.timss.purchase.service.swf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class SendPayEmailServiceImpl implements SendPayEmailService {

	private static final String MAIL_TEMPLATE = "inv_pay";
	private static final String RECEIVE_ROLE = "SWF_CGY";
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    public IAuthorizationManager authManager;
    @Autowired
    private PurPayService purPayService;
    @Autowired
    ItcMsgService itcMsgService;
	
	@Override
	public void sendPayEmail(String payId) throws RuntimeException, Exception {
		
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		PurPayVO purPayVo= purPayService.queryPurPayVoByPayId(itcMvcService.getUserInfoScopeDatas(), payId);
		// 获取付款类型
		String payType = purPayVo.getPayType();
		// 获取付款单号
		String payNO = purPayVo.getPayNo();
		// 获取合同编号、合同名称、url
		String contractNo = purPayVo.getSheetNo();
		String contractName = purPayVo.getSheetName();
		String url = "";
		String payTypeName = "";

		Map<String, Object> bindMap = new HashMap<String, Object>();
		
		List<AppEnum> appEnums = itcMvcService.getEnum("PUR_PAYTYPE");
		if (appEnums != null && appEnums.size() != 0) {
			for(AppEnum appEnum:appEnums){
				if(appEnum.getCode().equals(payType)){
					payTypeName = appEnum.getLabel();
					break;
				}
			}
		}
		
		bindMap.put("payType", payTypeName);
		bindMap.put("payNo", payNO);
		bindMap.put("contractNo", contractNo);
		bindMap.put("contractName", contractName);
		// bindMap.put("url", url);

		// 获取邮件接收人
		List<SecureUser> us = authManager.retriveUsersWithSpecificRoleAndSite(RECEIVE_ROLE, userInfo.getSiteId());

		if (us != null && us.size() != 0) {
			List<UserInfo> sendUserList = new ArrayList<UserInfo>();
			for (SecureUser user : us) {
				sendUserList.clear();
				UserInfo u = itcMvcService.getUserInfoById(user.getId());
				sendUserList.add(u);
				bindMap.put("url", url);
				itcMsgService.SendMail(MAIL_TEMPLATE, bindMap, sendUserList, userInfo);
			}
		} else {

		}
      

	}

}
