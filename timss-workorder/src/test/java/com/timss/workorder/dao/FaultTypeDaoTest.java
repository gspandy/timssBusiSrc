package com.timss.workorder.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.bean.WoFaultType;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class FaultTypeDaoTest  extends TestUnit {

	@Autowired 
	private WoFaultTypeDao faultTypeDao;
	@Test
	public void testInsertFaultType() {
		int id = faultTypeDao.getNextParamsConfId();
		WoFaultType faultType = new WoFaultType();
		faultType.setId(id);
		faultType.setRemarks("故障类型说明");
		faultType.setKeywords("关键字1,关键字2");
		faultType.setName("网络");
		faultType.setDefaultScore(20);
		
//		faultTypeDao.insertFaultType(faultType);
	}

	//@Test
	public void testUpdateFaultType() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeleteFaultType() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryFaultTypeById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testGetNextParamsConfId() {
		int id = faultTypeDao.getNextParamsConfId();
		System.out.println(id);
	}

	//@Test
	public void testqueryOneLevelFTBySiteId() {
		ArrayList<WoFaultType> faultTypeList = (ArrayList<WoFaultType>) faultTypeDao.queryOneLevelFTBySiteId("ITC");
		for (int i = 0; i < faultTypeList.size(); i++) {
			System.out.println(faultTypeList.get(i).getName());
			
		}
	}
	//@Test
	public void testgetOneLevelFTById() {
		
		WoFaultType temp = faultTypeDao.queryFaultTypeById(270);
		if(temp.getParentId() != 108){
			temp = getOneLevelFTById(temp.getParentId(),108);
		}
		System.out.println(temp);
	}
	
	private WoFaultType getOneLevelFTById(int id ,int rootId) {
		
		WoFaultType temp = faultTypeDao.queryFaultTypeById(id);
		if(temp.getParentId() != rootId){
			temp = getOneLevelFTById(temp.getParentId(),rootId);
		}
		return temp;
	}
}
