package com.timss.pms.service;

import com.timss.pms.vo.PayVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

public interface PayQueryService {
	/**
	 * 查询结算信息并过滤
	 * @Title: queryProjectListAndFilter
	 * @param page
	 * @param userInfo
	 * @return
	 */
	Page<PayVo> queryPayListAndFilter(Page<PayVo> page,UserInfoScope userInfo);
}
