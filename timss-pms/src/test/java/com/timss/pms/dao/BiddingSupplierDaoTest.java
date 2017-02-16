package com.timss.pms.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.bean.BiddingSupplier;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

public class BiddingSupplierDaoTest extends TestUnit{
    @Autowired
    BiddingSupplierDao biddingSupplierDao;
	@Test
	public void test() {
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		BiddingSupplier biddingSupplier=new BiddingSupplier();
		biddingSupplier.setBId("sde");
		biddingSupplier.setId("332");
		biddingSupplierDao.insertBiddingSupplier(biddingSupplier);
		
		biddingSupplierDao.deleteBiddingSupplierByBiddingId("sde");
		biddingSupplierDao.queryBiddingSupplierListByBiddingId("sde");
	}

}
