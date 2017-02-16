package com.timss.workorder.service.core;

import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.service.WorkOrderService;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WorkOrderServiceImplTest extends TestUnit{
	@Autowired
	WorkOrderService workOrderService ; 
	//@Test
	public void testSaveWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testInsertWorkOrder() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWorkOrder() {
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
	public void testGetNextWOId() {
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

	@Test
	public void testQueryWOBaseInfoByWOCode() {
		Map<String, Object> resultHashMap = workOrderService.queryWOBaseInfoByWOCode("WO20140814004","SBS");
		WorkOrder workOrder = (WorkOrder)resultHashMap.get("workOrder");
		System.out.println(workOrder);
		System.out.println(resultHashMap);
		
	}
	//@Test
	public void testupdateWOAddPTWId() throws Exception {
		workOrderService.updateWOAddPTWId(172,60);
	}

}
