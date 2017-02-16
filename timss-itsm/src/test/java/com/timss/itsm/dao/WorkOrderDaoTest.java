package com.timss.itsm.dao;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class WorkOrderDaoTest extends TestUnit {

	@Autowired
    private ItsmWorkOrderDao workOrderDao;
	
	//@Test
	public void testInsertWorkOrder() {
		ItsmWorkOrder workOrder = new ItsmWorkOrder();
//		int id = workOrderDao.getNextWOId();
		
		workOrder.setId(1146);
		workOrder.setWorkOrderCode("");
		workOrder.setDescription("001");
		workOrder.setWorkOrderTypeCode("qxWoType");
		workOrder.setIsCycleWO(0);
		workOrder.setWorkflowId("365631");
		workOrder.setCurrStatus("currStatus");
		workOrder.setEquipId("AST-00012198");
		//workOrder.setWorkOrderCode("test");
		workOrderDao.insertWorkOrder(workOrder);
	}

	//@Test
	public void testUpdateWorkOrder() {
		ItsmWorkOrder workOrder = new ItsmWorkOrder();
		workOrder.setId(296);
		workOrder.setEquipId("AST-00009204");
		workOrder.setEquipName("月山站变电设备");
		workOrder.setEquipNameCode("A");
		workOrder.setCurrStatus("draft");
		workOrderDao.updateWorkOrder(workOrder);
	}
	
	//@Test
	public void testupdateOperUserById() {
		HashMap<String, String> parmas = new HashMap<String, String>();
		parmas.put("woId", "367");
		parmas.put("faultConfrimUser", "");
		workOrderDao.updateOperUserById(parmas);
	}

	//@Test
	public void testQueryAllWO() {
		workOrderDao.queryWOById(1);
	}

	//@Test
	public void testQueryWOById() {
		fail("Not yet implemented");
	}
	//@Test
	public void testQueryItcWOById() {
		ItsmWorkOrder workOrder = workOrderDao.queryItWOById(594);
		System.out.println(workOrder.getId()+":"+workOrder.getCustomerCode());
		System.out.println(workOrder.getCustomerName()+":"+workOrder.getCustomerPhone());
	}
	
	
	//@Test
	public void testqueryWOBaseInfoByWOCode() {
		ItsmWorkOrder workOrder = workOrderDao.queryWOBaseInfoByWOCode("WO20141110005","SBS");
		System.out.println(workOrder);
	}
		
	//@Test
	public void testGetNextWOId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOHandlerStyle() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOStatus() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateWOOnPlan() {
		HashMap<String,Object> parmas = new HashMap<String,Object>();
		parmas.put("isToPTW", "noPTW");   //是否走工作票
		parmas.put("id", 129);  
		parmas.put("jobPlanId", 141);     //添加作业方案的ID（最新的）
		parmas.put("modifyUser", "126060");
		parmas.put("modifyDate", new Date());
		//TODO 此处需要获得走工作票时的工作票ID
		parmas.put("ptwId", 0);  //关联工作票的ID
		workOrderDao.updateWOOnPlan(parmas);
		
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
	public void testupdateWOAddPTWId() {
		workOrderDao.updateWOAddPTWId(172, 50);
	}
	
	//@Test
	public void testdeleteWorkOrder() {
		workOrderDao.deleteWorkOrder(244);
	}
	//@Test
	public void testupdatePartnerInfo() {
		HashMap<String, Object> parmas = new HashMap<String, Object>();
		parmas.put("partnerIds", "890152、890128");
		parmas.put("partnerNames", "张三、李四");
		parmas.put("id", 602);
		parmas.put("modifyDate", new Date());
		parmas.put("modifyUser","890152" );
		workOrderDao.updatePartnerInfo(parmas);
	}
	//@Test
	public void testupdateDelayInfo() {
		HashMap<String, Object> parmas = new HashMap<String, Object>();
		parmas.put("delayType", "inWorking");
		parmas.put("delayToTime",  new Date());
		parmas.put("id", "602");
		parmas.put("modifyDate", new Date());
		parmas.put("modifyUser","890152" );
		workOrderDao.updateDelayInfo(parmas);
	}
}
