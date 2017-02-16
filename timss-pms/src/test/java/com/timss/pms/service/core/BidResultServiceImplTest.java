package com.timss.pms.service.core;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

import com.timss.pms.bean.BidResult;
import com.timss.pms.service.BidResultService;
import com.timss.pms.vo.BidResultDtlVo;
@Transactional
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class BidResultServiceImplTest extends TestUnit{

	@Autowired
	BidResultService bidResultService;
	@Test
	public void test() {
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		BidResult bidResult=new BidResult();
		bidResult.setName("bidresult");
		bidResult.setProjectId(999);
		bidResultService.tmpInsertBidResult(bidResult);
		
		BidResultDtlVo bidResult1=bidResultService.queryBidResultById(bidResult.getBidResultId());
		
		bidResultService.tmpUpdateBidResult(bidResult);
		
		bidResultService.deleteBidResult(bidResult.getBidResultId());
		
		
	}

}
