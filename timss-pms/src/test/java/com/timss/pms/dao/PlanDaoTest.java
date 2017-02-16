package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
import com.timss.pms.bean.Plan;
import com.timss.pms.service.PlanService;
import com.timss.pms.vo.PlanDtlVo;
import com.timss.pms.vo.PlanVo;
import com.yudean.itc.dto.Page;


@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})

public class PlanDaoTest extends TestUnit{
    @Autowired
	PlanDao planDao;
    @Autowired
    PlanService planService;
	@Test
	public void test() {
		
//		PlanDtlVo planDtlVo=planDao.queryPlanById(666);
//		Plan tmp=new Plan();
//		tmp.setId(666);
//		tmp.setHistCost(1.0);
//		tmp.setHistIncome(11.0);
//		tmp.setHistProfit(10.0);
//		tmp.setHistPercent(50.0);
//		planDao.updateHistInfo(tmp);
//		planDao.increaseCarryOverTimes(666);
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		PlanDtlVo planDtlVo=planDao.queryPlanById(666);
		planService.changePlan(planDtlVo);
	}
	
	
	public void testAll() {
		//插入测试
		Plan plan=new Plan();
		plan.setPlanName("hello world");
		plan.setYear("2014");
		planDao.insertPlan(plan);
		
		//测试查询
		int id=plan.getId();
		Plan plan2=planDao.queryPlanById(id);
		assertEquals("2014", plan2.getYear());
		
		//测试查询列表
		Page page=new Page(1,10);
		page.setFuzzyParameter("id", id);
		List<PlanVo> planVos=planDao.queryPlanList(page);
		assertEquals(1, planVos.size());
		
		//测试update
		plan.setYear("2015");
		planDao.updatePlan(plan);
		plan2=planDao.queryPlanById(id);
		assertEquals("2015", plan2.getYear());
		
		//测试delete
		int count=planDao.deletePlan(id);
		assertEquals(1, count);
	}

	public void queryById(){
		PlanDtlVo plan=planDao.queryPlanById(999);
		assertEquals("name", plan.getPlanName());
		
	}
	
	
	public void updatePlan(){
		Plan plan=new Plan();
		plan.setPlanName("hello world");
		plan.setYear("2014");
		plan.setId(70);
		plan.setStartTime(null);
		plan.setEndTime(null);
		plan.setType("guding");
		plan.setPlanPercent(3.0);
		planDao.updatePlan(plan);
		assertEquals("hello world", plan.getPlanName());
	}
	
	
	public void deletePlan(){
		int count=planDao.deletePlan(64);
		
	}

}
