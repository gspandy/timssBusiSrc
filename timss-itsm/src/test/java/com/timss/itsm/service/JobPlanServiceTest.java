package com.timss.itsm.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.service.ItsmJobPlanService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class JobPlanServiceTest  extends TestUnit {
	@Autowired
	ItsmJobPlanService jobPlanService ;
	@Test
	public void testQueryWoSkillById() {
		
		TestUnitGolbalService.SetCurentUserById("890107", "SBS");
		jobPlanService.queryJPById(0);
	}

}
