package com.timss.itsm.dao;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils.Null;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.dao.ItsmMaintainPlanDao;
import com.timss.itsm.bean.ItsmMaintainPlan;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class MaintainPlanDaoTest  extends TestUnit{
	@Autowired
    private ItsmMaintainPlanDao maintainPlanDao;
	
	//@Test
	public void testInsertMaintainPlan() {
		fail("Not yet implemented");
	}

	//@Test
	public void testUpdateMaintainPlan() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryMTPById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryAllMTP() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryAllParentMTP() {
		System.out.println("测试例子");
		//maintainPlanDao.queryAllParentMTP(page);
	}

	//@Test
	public void testGetNextMTPId() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testqueryAllCycMTP() {
		List<ItsmMaintainPlan> list= maintainPlanDao.queryAllCycMTP();
		System.out.println(list.size());
		for(int i=0; i<list.size(); i++){
			ItsmMaintainPlan temPlan = list.get(i);
			System.out.println(temPlan.getId()+"   ;    "+temPlan.getAlertTime()+"    ;    "+temPlan.getNewToDoTime());
			if(temPlan.getNewToDoTime()!=null && temPlan.getNewToDoTime().before(new Date())){
				System.out.println("客官，请稍候哦~");
			}
			System.out.println();
		}
		
		System.out.println("XXXXX");
	}
	
	//@Test
	public void testupdateMTPTodoTime() {
		Date date1 = new Date();
		Date date2 = new Date(date1.getTime()+24*60*60*1000);
		maintainPlanDao.updateMTPTodoTime(date1,date2,146);
		System.out.println(date1.before(date2));
		System.out.println("XXXXX");
	}
	
	//@Test
	public void testupdateMTPhasAlertTodo() {
		
		maintainPlanDao.updateMTPhasAlertTodo(345,1);
		
	}
	
}
