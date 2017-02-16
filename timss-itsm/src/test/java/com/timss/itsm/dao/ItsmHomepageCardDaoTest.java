package com.timss.itsm.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class ItsmHomepageCardDaoTest extends TestUnit {

	@Autowired 
	private ItsmHomepageCardDao itsmHomepageCardDao;
	
	@Test
	public void testQueryOneLevlFtWoSum() {
		
		//获取当前年的第一天
		Calendar calendar = Calendar.getInstance();  
		int  year = calendar.get(Calendar.YEAR);
	    calendar.clear();  
	    calendar.set(Calendar.YEAR, year);  
	    Date currYearFirst = calendar.getTime(); //当前年的第一天  
	    
		List<Map<String, BigDecimal>> resultHashMapList = itsmHomepageCardDao.queryOneLevlFtWoSum(currYearFirst, new Date());
		Map<Integer, Integer> resultSumHashMap = new HashMap<Integer, Integer>();
		int allWoSum = 0 ;
		for (int i = 0; i < resultHashMapList.size(); i++) {
			Map<String, BigDecimal> temp = resultHashMapList.get(i);
			int ftId = temp.get("FTID").intValue();
			int woSum = temp.get("WOSUM").intValue();
			resultSumHashMap.put(ftId, woSum);
			allWoSum += woSum;
		}
		
		System.out.println("------------------------------------");
		System.out.println(allWoSum);
	}

}
