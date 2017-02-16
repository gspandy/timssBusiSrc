package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.BidCon;
import com.timss.pms.vo.BidConVo;

/**
 * 招标与招标单位关系表dao接口
 * @ClassName:     BidConDao
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-23 上午10:13:19
 */
public interface BidConDao {
	
	/**
	 * 插入招标单位列表
	 * @Title: insertBidConList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bidCons
	 * @return
	 */
	int insertBidConList(List<BidCon> bidCons);
	
	/**
	 * 根据招标id，查询所属的招标单位信息
	 * @Title: queryBidConListByBidId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	List<BidConVo> queryBidConListByBidId(int id);
	
	int updateBidCon(BidCon bidCon);
}
