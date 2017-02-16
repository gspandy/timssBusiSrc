package com.timss.pms.dao;


import java.util.List;

import com.timss.pms.bean.Bid;
import com.timss.pms.bean.BidMethod;
import com.timss.pms.bean.BidResult;
import com.timss.pms.vo.BidDtlVo;
import com.timss.pms.vo.BidVo;

/**
 * 
 * @ClassName:     BidDao
 * @company: gdyd
 * @Description: 招标dao接口
 * @author:    黄晓岚
 * @date:   2014-7-2 下午3:09:51
 */
public interface BidDao {

	/**
	 * 
	 * @Title: insertBid
	 * @Description: 插入招标信息
	 * @param bid
	 * @return
	 */
	int insertBid(Bid bid);
	
	/**
	 * 插入评标信息
	 * @Title: insertBidMethod
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bidMethod
	 * @return
	 */
	int insertBidMethod(BidMethod bidMethod);
	
	/**
	 * 插入招标结果
	 * @Title: insertBidResult
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bidResult
	 * @return
	 */
	int insertBidResult(BidResult bidResult);
	
	/**
	 * 
	 * @Title: deleteBid
	 * @Description: 根据id删除招标信息，物理删除
	 * @param bidId
	 * @return
	 */
	int deleteBid(int bidId);
	
	/**
	 * 
	 * @Title: queryBidListByProjectId
	 * @Description: 根据项目id，查询招标信息
	 * @param projectId
	 * @return
	 */
	List<BidVo> queryBidListByProjectId(int projectId);
	
	/**
	 * 
	 * @Title: queryBidByBidId
	 * @Description: 根据id，查找招标信息
	 * @param bidId
	 * @return
	 */
	BidDtlVo queryBidByBidId(int bidId);
}
