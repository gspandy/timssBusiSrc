package com.timss.pms.schedule;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.pms.dao.PlanDao;
import com.timss.pms.service.PlanService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})
public class PlanChangePerYearScheduleTest extends TestUnit{
    @Autowired
    PlanChangePerYearSchedule perYearSchedule;
    @Autowired
    PlanService planService;
    @Autowired
    PlanDao planDao;
	@Test
	public void test() {
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		perYearSchedule.changePlanTodoList();
//		System.out.println("----------------"+planService.getActualCost("644"));
	}

}
