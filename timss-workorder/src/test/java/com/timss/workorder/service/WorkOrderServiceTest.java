package com.timss.workorder.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WorkOrderServiceTest  extends TestUnit  {
	@Autowired
	WorkOrderService workOrderService ;
	//@Test
	public void testInsertWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOAddPTWId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateOperUserById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryAllWO() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryWOById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryItWOById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryWOBaseInfoByWOCode() {
		fail("Not yet implemented");
	}

	//@Test
	public void testGetNextWOId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testSaveWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOStatus() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOHandlerStyle() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOOnPlan() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOOnAcceptance() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOOnReport() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeleteWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testInPtwToNextStep() {
		fail("Not yet implemented");
	}

	//@Test
	public void testStopWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWorkflowId() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoginUserIsCusSer() {
		TestUnitGolbalService.SetCurentUserById("890170", "ITC");
		boolean flag = workOrderService.loginUserIsCusSer();
		System.out.println("result:"  +  flag);
	}

}
