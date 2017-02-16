package com.timss.pms.service;


import java.util.List;

import com.timss.pms.bean.Bid;
import com.timss.pms.bean.BidCon;
import com.timss.pms.bean.BidMethod;
import com.timss.pms.bean.BidResult;
import com.timss.pms.vo.BidDtlVo;
import com.timss.pms.vo.BidVo;

/**
 * 
 * @ClassName:     BidService
 * @company: gdyd
 * @Description: 招标service
 * @author:    黄晓岚
 * @date:   2014-7-2 下午3:35:45
 */
public interface BidService {
	/**
	 * 
	 * @Title: insertBid
	 * @Description: 插入招标
	 * @param bid
	 */
	public void insertBid(Bid bid,List<BidCon> bidCons);
	
	/**
	 * 插入招标方法
	 * @Title: insertBidMethod
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bidMethod
	 */
	public void insertBidMethod(BidMethod bidMethod);
	
	/**
	 * 插入招标结果
	 * @Title: insertBidResult
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bidResult
	 */
	public void insertBidResult(BidResult bidResult,List<BidCon> bidCons);
	
	/**
	 * 
	 * @Title: queryBidListByProjectId
	 * @Description: 根据项目id查询招标信息
	 * @param projectId
	 * @return
	 */
	public List<BidVo> queryBidListByProjectId(String projectId);
	
	/**
	 * 根据bidId查询招标信息
	 * @Title: queryBidByBidId
	 * @Description: 
	 * @param bidId
	 * @return
	 */
	public BidDtlVo queryBidByBidId(int bidId);
}
