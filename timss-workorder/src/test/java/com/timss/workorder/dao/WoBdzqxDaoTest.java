package com.timss.workorder.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WoQx;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class WoBdzqxDaoTest  extends TestUnit{

	@Autowired
	WoQxDao woBdzqxDao;
	
	@Test
	@Transactional
	public void testInsertWoBdzqx() {
		WoQx  wobdzqx = new WoQx();
		wobdzqx.setDefectCode("xx");
		wobdzqx.setMonthCode("ss");
		wobdzqx.setYxbz(1);
		woBdzqxDao.insertWoQx(wobdzqx);
	}

//	@Test
	public void testUpdateWoBdzqx() {
		fail("Not yet implemented");
	}

//	@Test
	public void testQueryWoBdzqxById() {
		fail("Not yet implemented");
	}

//	@Test
	public void testDeleteWoBdzqxById() {
		fail("Not yet implemented");
	}

//	@Test
	public void testQueryAllWoBdzqx() {
		fail("Not yet implemented");
	}

}
