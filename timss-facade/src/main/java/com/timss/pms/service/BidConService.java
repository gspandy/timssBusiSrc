package com.timss.pms.service;

import java.util.List;

import com.timss.pms.bean.BidCon;
import com.timss.pms.vo.BidConVo;

/**
 * 招标与招标关系service接口
 * @ClassName:     BidConService
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-23 上午10:34:08
 */
public interface BidConService {
	/**
	 * 插入招标与招标单位信息列表
	 * @Title: insertBidConList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bidCons
	 */
	void insertBidConList(List<BidCon> bidCons,int bidId);
	
	/**
	 * 根据招标id，查询招标单位信息
	 * @Title: queryBidConListByBidId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bidId
	 * @return
	 */
	List<BidConVo> queryBidConListByBidId(String bidId);
	
	/**
	 * 更新招标列表信息
	 * @Title: updateBidConList
	 * @param bidCons
	 */
	void updateBidConList(List<BidCon> bidCons);
}
