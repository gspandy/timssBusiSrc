package com.timss.pms.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Bidding;
import com.timss.pms.vo.BiddingDtlVo;
import com.timss.pms.vo.BiddingVo;
import com.yudean.mvc.testunit.TestUnit;

public class BiddingDaoTest extends TestUnit{
    @Autowired
    BiddingDao biddingDao;
	@Test
	@Transactional
	public void test() {
		Bidding bid=new Bidding();
		
		bid.setId("4");
		bid.setBName("test");
		biddingDao.insertBidding(bid);
		
		String id=bid.getId();
		String bidName="test2";
		bid.setBName(bidName);
		biddingDao.updateBidding(bid);
		
		BiddingDtlVo biddingDtlVo=biddingDao.queryBiddingByBiddingId(id);
		assertEquals("test2", biddingDtlVo.getBName());
		
		biddingDao.deleteBidding(id);
	}

}
