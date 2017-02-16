package com.timss.workorder.dao;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yudean.mvc.testunit.TestUnit;


public class JPJobtaskDaoTest extends TestUnit {
	@Autowired
	JPJobtaskDao jpJobtaskDao ;
	
	//@Test
	public void testInsertJPJobtask() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateJPJobtask() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryJPJobtaskByJPId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeleteJPJobtaskByJPId() {
		jpJobtaskDao.deleteJPJobtaskByJPId(64);
	}

	//@Test
	public void testQueryJPJobtaskById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testGetNextJPJobtaskId() {
		fail("Not yet implemented");
	}

}
