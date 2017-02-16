package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.service.ItcMvcService;

import com.yudean.mvc.testunit.TestUnit;

import com.yudean.mvc.testunit.TestUnitGolbalService;
import com.timss.pms.bean.Bid;
import com.timss.pms.bean.BidMethod;
import com.timss.pms.bean.BidResult;
import com.timss.pms.vo.BidDtlVo;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class BidDaoTest extends TestUnit{
    
	@Autowired
	BidDao bidDao;
	@Autowired
	ItcMvcService itcMvcService;
	@Test
	public void testAll(){
		Bid bid=new Bid();
		bid.setBudget(100.0);
		bid.setName("junit test bid");
		bid.setProjectId(999);
		bidDao.insertBid(bid);
		int id=bid.getBidId();
		
		BidDtlVo bidDtlVo=bidDao.queryBidByBidId(id);
		assertEquals("junit test bid", bidDtlVo.getName());
		
		BidMethod bidMethod=new BidMethod();
		bidMethod.setBidId(id);
		bidMethod.setCommand("commad");
		bidDao.insertBidMethod(bidMethod);
		bidDtlVo=bidDao.queryBidByBidId(bid.getBidId());
		assertEquals("commad", bidDtlVo.getBidMethod().get(0).getCommand());
		
		BidResult bidResult=new BidResult();
		bidResult.setBidId(id);
		bidResult.setCommand("commadcommad");
		bidDao.insertBidResult(bidResult);
		bidDtlVo=bidDao.queryBidByBidId(bid.getBidId());
		assertEquals("commadcommad", bidDtlVo.getBidResult().get(0).getCommand());
	}
	
	public void insert() {
		Bid bid=new Bid();
		bid.setBudget(100.0);
		bid.setName("junit test bid");
		bid.setProjectId(1016);
		bidDao.insertBid(bid);
	}
	
	
	public void queryProjectId() {
		List list=itcMvcService.getEnum("PMS_CONTRACT_TYPE");
		
		/*List list=bidDao.queryBidListByProjectId(1016);*/
		assertEquals(false, list==null);
	}
	
	
	public void queryBidId() {
		
		BidDtlVo bidDtlVo=bidDao.queryBidByBidId(1003);
		assertEquals("1003", bidDtlVo.getBidId().toString());
	
	}

	
	public void insertBidMethod() {
		BidMethod bidMethod=new BidMethod();
		bidMethod.setBidId(1003);
		bidMethod.setCommand("commad");
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		bidDao.insertBidMethod(bidMethod);
	}
	
	
	public void insertBidResult() {
		BidResult bidResult=new BidResult();
		bidResult.setBidId(1003);
		bidResult.setCommand("commad");
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		bidDao.insertBidResult(bidResult);
	}
}
