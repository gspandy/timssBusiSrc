package com.timss.workorder.service.core;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.service.MaintainPlanService;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class MaintainPlanServiceImplTest extends TestUnit {

	@Autowired
	MaintainPlanService maintainPlanService;
	//@Test
	public void testInsertMaintainPlan() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryAllMTP() {
		System.out.println("xxxxxx");
	}

	//@Test
	public void testQueryAllParentMTP() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryMTPById() {
		fail("Not yet implemented");
	}

	//
	public void testGetNextMTPId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateMaintainPlan() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateMTPToUnvailable() {
		fail("Not yet implemented");
	}
	
	//@Test
	public void testqueryAllCycMTP() {
		
		fail("Not yet implemented");
	}
	
	
	

}
