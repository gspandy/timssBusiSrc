package com.timss.pms.service;

import com.timss.pms.vo.ProjectVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

public interface ProjectQueryService {
	/**
	 * 查询项目信息并过滤
	 * @Title: queryProjectListAndFilter
	 * @param page
	 * @param userInfo
	 * @return
	 */
	Page<ProjectVo> queryProjectListAndFilter(Page<ProjectVo> page,UserInfoScope userInfo);
}
