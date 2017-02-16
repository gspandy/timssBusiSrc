package com.timss.pms.service.core;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

import com.timss.pms.bean.Plan;
import com.timss.pms.dao.PlanDao;
import com.timss.pms.service.PlanService;
import com.yudean.itc.dto.Page;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class PlanServiceImplTest extends TestUnit{

	@Autowired
	PlanService planService;
	@Test
	public void test() {
		Plan plan=new Plan();
		plan.setPlanName("测试用户和站点信息的插入");
		plan.setId(1012);
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");

		planService.tmpUpdatePlan(plan);

	}

}
