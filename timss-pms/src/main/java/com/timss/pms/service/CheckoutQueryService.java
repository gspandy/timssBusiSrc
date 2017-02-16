package com.timss.pms.service;

import com.timss.pms.vo.CheckoutVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

public interface CheckoutQueryService {
	/**
	 * 查询验收信息并过滤
	 * @Title: queryProjectListAndFilter
	 * @param page
	 * @param userInfo
	 * @return
	 */
	Page<CheckoutVo> queryCheckoutListAndFilter(Page<CheckoutVo> page,UserInfoScope userInfo);
}
