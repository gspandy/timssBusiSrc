package com.timss.workorder.dao;

import static org.junit.Assert.fail;

import org.springframework.beans.factory.annotation.Autowired;

import com.yudean.mvc.testunit.TestUnit;


public class JPPreHazardDaoTest  extends TestUnit{
	@Autowired
	JPPreHazardDao jpPreHazardDao ;
	//@Test
	public void testInsertJPPreHazard() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryJPPreHazardByJPId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeleteJPPreHazardByJPId() {
		jpPreHazardDao.deleteJPPreHazardByJPId(64);
	}

	//@Test
	public void testQueryJPPreHazardById() {
		fail("Not yet implemented");
	}

}
