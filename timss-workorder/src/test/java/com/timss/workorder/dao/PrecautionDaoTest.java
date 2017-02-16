package com.timss.workorder.dao;

import static org.junit.Assert.fail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PrecautionDaoTest extends TestUnit{
	@Autowired
	PrecautionDao precautionDao ;

	//@Test
	public void testInsertPrecaution() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdatePrecaution() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryPrecautionById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeletePrecautionById() {
		precautionDao.deletePrecautionById(119);
		
	}

	//@Test
	public void testGetNextPrecautionId() {
		fail("Not yet implemented");
	}

}
