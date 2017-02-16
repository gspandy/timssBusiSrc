package com.timss.itsm.dao;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.bean.ItsmWoFaultType;
import com.yudean.mvc.testunit.TestUnit;

public class ItsmFaultTypeDaoTest  extends TestUnit {

	@Autowired 
	private ItsmWoFaultTypeDao faultTypeDao;
	//@Test
	public void testInsertFaultType() {
//		Calendar calendar = Calendar.getInstance();  
//		int  year = calendar.get(Calendar.YEAR);
//	    calendar.clear();  
//	    calendar.set(Calendar.YEAR, year);  
//	    Date currYearFirst = calendar.getTime(); //当前年的第一天  
		Calendar calendar = Calendar.getInstance();
		int  year = calendar.get(Calendar.YEAR);
		int  month = calendar.get(Calendar.MONTH);
		calendar.clear(); 
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		Date currdayOfCurrMonth = calendar.getTime();
		
	    System.out.println(year);
	    System.out.println(currdayOfCurrMonth);
	    calendar.add(Calendar.MONTH, -3);
	    year = calendar.get(Calendar.YEAR);
	    currdayOfCurrMonth = calendar.getTime();
	    System.out.println(year);
	    System.out.println(currdayOfCurrMonth);
//		int id = faultTypeDao.getNextParamsConfId();
//		ItsmWoFaultType faultType = new ItsmWoFaultType();
//		faultType.setId(id);
//		faultType.setRemarks("故障类型说明");
//		faultType.setKeywords("关键字1,关键字2");
//		faultType.setName("网络");
//		faultType.setDefaultScore(20);
//		
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
		ArrayList<ItsmWoFaultType> faultTypeList = (ArrayList<ItsmWoFaultType>) faultTypeDao.queryOneLevelFTBySiteId("ITC");
		for (int i = 0; i < faultTypeList.size(); i++) {
			System.out.println(faultTypeList.get(i).getName());
			
		}
	}
	@Test
	public void testgetOneLevelFTById() {
		
		ItsmWoFaultType temp = faultTypeDao.queryFaultTypeById(270);
		if(temp.getParentId() != 108){
			temp = getOneLevelFTById(temp.getParentId(),108);
		}
		System.out.println(temp);
	}
	
	private ItsmWoFaultType getOneLevelFTById(int id ,int rootId) {
		
		ItsmWoFaultType temp = faultTypeDao.queryFaultTypeById(id);
		if(temp.getParentId() != rootId){
			temp = getOneLevelFTById(temp.getParentId(),rootId);
		}
		return temp;
	}
}
