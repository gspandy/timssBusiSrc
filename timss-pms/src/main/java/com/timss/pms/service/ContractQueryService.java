package com.timss.pms.service;

import com.timss.pms.vo.ContractVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

public interface ContractQueryService {
	/**
	 * 查询验收信息并过滤
	 * @Title: queryProjectListAndFilter
	 * @param page
	 * @param userInfo
	 * @return
	 */
	Page<ContractVo> queryContractListAndFilter(Page<ContractVo> page,UserInfoScope userInfo);
}
