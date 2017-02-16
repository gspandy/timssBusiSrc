package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.vo.BidResultVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * 省风电招标dao
 * @ClassName:     SFCBidResultDao
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-9-17 上午9:20:02
 */
public interface SFCBidResultDao {

	/**
	 * 根据招标信息模糊查询招标结果列表，对返回结果使用数据权限过滤
	 * @Title: queryBidResultListAndFilter
	 * @param page
	 * @return
	 */
	@RowFilter(flowIdColumn="flowId",exclusiveRule="PMS_EXCLUDE",isRouteFilter=true)
	List<BidResultVo> queryBidResultListAndFilter(Page<BidResultVo> page);
	
	/**
	 * 查询招标结果列表
	 * @Title: queryBidResultList
	 * @param page
	 * @return
	 */
	List<BidResultVo> queryBidResultList(Page<BidResultVo> page);
}
