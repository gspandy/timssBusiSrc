package com.timss.workorder.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WoSkillServiceTest  extends TestUnit {
	@Autowired
	WoSkillService woSkillService ;
	@Test
	public void testQueryWoSkillById() {
		
		TestUnitGolbalService.SetCurentUserById("890107", "ITC");
		woSkillService.queryWoSkillById(0);
	}

}
