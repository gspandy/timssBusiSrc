package com.timss.pms.service;

import java.util.List;
import java.util.Map;

import com.timss.pms.vo.BidResultVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

public interface BidQueryService {
	/**
	 * 查询招标信息并过滤
	 * @Title: queryProjectListAndFilter
	 * @param page
	 * @param userInfo
	 * @return
	 */
	Page<BidResultVo> queryBidResultListAndFilter(Page<BidResultVo> page,UserInfoScope userInfo);
	
	/**
	 * 根据招标名称模糊哦查询招标信息。
	 * @Title: queryBidResultList
	 * @param page
	 * @param userInfo
	 * @return
	 */
	List<Map<String,String>> queryBidResultListByKeyWord(String kw);
}
