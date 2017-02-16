package com.timss.workorder.service.core;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.service.WOStatisService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WOStatisServiceImplTest extends TestUnit{

	@Autowired
	WOStatisService woStatisService;
	
	
//	@Test
	public void testqueryYearOfFirstWO() {
		TestUnitGolbalService.SetCurentUserById("126060", "SBS");
		int year = woStatisService.queryYearOfFirstWO();
		System.out.println(year);
	}
	
	@Test
	public void testqueryAllWorkTeam() {
		TestUnitGolbalService.SetCurentUserById("126060", "SBS");
		woStatisService.queryAllWorkTeam();
	}

}
