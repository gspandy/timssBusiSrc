package com.timss.pms.service;

import java.util.List;

import com.timss.pms.bean.BodyPartOfERPIB;
import com.timss.pms.bean.ERPInBrower;
import com.timss.pms.bean.ERPResponse;
import com.timss.pms.bean.HeaderPartOfERPIB;
import com.yudean.mvc.bean.userinfo.UserInfo;

/**
 * 项目管理与ERP接口的数据交互接口实现
 * @ClassName:     SendMessageToERP
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-8-24 上午8:47:51
 */
public interface PMSToERPService {
	/**
	 * 将收款信息发送给ERP系统
	 * @Title: SendReceiptMessage
	 * @param payId
	 * @param header TODO
	 * @param body TODO
	 * @param userInfo
	 * @return
	 */
	public ERPResponse sendReceiptMessage(String payId,HeaderPartOfERPIB header, List<BodyPartOfERPIB> body, UserInfo userInfo);
	
	public ERPInBrower getERPDataFromReceiptId(String payId,UserInfo userInfo);
}
