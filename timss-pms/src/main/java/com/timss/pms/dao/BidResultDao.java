package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.BidResult;
import com.timss.pms.vo.BidResultDtlVo;
import com.timss.pms.vo.BidResultVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

public interface BidResultDao {
	int insertBidResult(BidResult bidResult);
	
	List<BidResultVo> queryBidResultListByProjectId(int projectId);
	
	BidResultDtlVo queryBidResultById(int id);
	
	int deleteBidResult(int id);
	
	int updateBidResult(BidResult bidResult);
	
	int updateByPrimaryKeySelective(BidResult bidResult);
	
	/**
	 * 
	 * @Title: queryBidResultList
	 * @Description: 查询招标结果信息
	 * @param page 查询的条件
	 * @return: List<Project>  查询结果
	 * @throws
	 */
	List<BidResultVo> queryBidResultList(Page<BidResultVo> page);
	
	/**
	 * 
	 * @Title: queryBidResultListAndFilter
	 * @Description: 查询招标结果信息并过滤
	 * @param page 查询的条件
	 * @return: List<Project>  查询结果
	 * @throws
	 */
	@RowFilter(flowIdColumn="flowId",exclusiveRule="PMS_EXCLUDE",exclusiveDeptRule="PMS_DEPT_EXCLUDE",isRouteFilter=true)
	List<BidResultVo> queryBidResultListAndFilter(Page<BidResultVo> page);
}
