package com.timss.pms.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.bean.Bidding;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

public class BiddingApplyServiceTest extends TestUnit{
    @Autowired
    BiddingApplyService biddingApplyService;
	@Test
	public void test() {
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		Bidding bidding=new Bidding();
		bidding.setId("00001002");
		biddingApplyService.updateBiddingApplyAndStartWorkflow(bidding,null);
	}

}
