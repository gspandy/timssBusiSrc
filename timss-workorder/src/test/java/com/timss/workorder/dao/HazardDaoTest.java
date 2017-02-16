package com.timss.workorder.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yudean.mvc.testunit.TestUnit;


public class HazardDaoTest extends TestUnit{
	
	@Autowired
	HazardDao hazardDao;
	
	//@Test
	public void testInsertHazard() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateHazard() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryHazardById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeleteHazardById() {
		hazardDao.deleteHazardById(118);
	}

	//@Test
	public void testGetNextHazardId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testInsertPreHazard() {
		fail("Not yet implemented");
	}

}
