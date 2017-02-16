package com.timss.pms.service.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
import com.timss.pms.bean.BidCon;
import com.timss.pms.service.BidConService;
import com.timss.pms.vo.BidConVo;
import com.timss.purchase.bean.PurVendor;
import com.timss.purchase.service.PurVendorService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class BidConServiceImplTest extends TestUnit{
	
	@Autowired
	BidConService bidConService;
	@Autowired
	PurVendorService purVendorService;
	@Test
	public void test() {
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		BidCon bidCon=new BidCon();
		bidCon.setBidId(999);
		bidCon.setCommand("hello world");
		List<BidCon> bidCons=new ArrayList<BidCon>();
		bidCons.add(bidCon);
		bidConService.insertBidConList(bidCons,999);
		
		List<BidConVo> bidConVos=bidConService.queryBidConListByBidId("999");
		assertEquals(1, bidConVos.size());
		assertEquals("hello world", bidConVos.get(0).getCommand());
		
		
	}


}
