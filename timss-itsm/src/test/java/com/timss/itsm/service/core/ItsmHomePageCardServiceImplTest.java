package com.timss.itsm.service.core;

import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.service.ItsmHomePageCardService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class ItsmHomePageCardServiceImplTest  extends TestUnit {

	@Autowired
	ItsmHomePageCardService itsmHomePageCardService;
	
	//@Test
	public void testMonthCardStatistic() {
		fail("Not yet implemented");
	}

	@Test
	public void testYearCardStatistic() throws Exception {
		TestUnitGolbalService.SetCurentUserById("890152", "ITC");
//		Map<String, Object> woRateStatisticByCurrYear =itsmHomePageCardService.yearCardStatistic();
//		System.out.println(woRateStatisticByCurrYear);
	}

}
