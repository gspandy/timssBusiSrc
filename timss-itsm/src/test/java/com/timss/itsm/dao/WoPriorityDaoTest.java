package com.timss.itsm.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.bean.ItsmWoPriority;
import com.timss.itsm.dao.ItsmWoPriorityDao;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WoPriorityDaoTest extends TestUnit {

	@Autowired
	private ItsmWoPriorityDao woPriorityDao;
	@Test
	public void testInsertWoPriority() {
		int id = woPriorityDao.getNextParamsConfId();
		ItsmWoPriority woPriority= new ItsmWoPriority();
		woPriority.setId(id);
		woPriority.setName("A1");
		woPriority.setRemarks("最高级别的优先级");
		woPriorityDao.insertWoPriority(woPriority);
	}

	//@Test
	public void testUpdateWoPriority() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeleteWoPriority() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryWoPriorityById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testGetNextParamsConfId() {
		fail("Not yet implemented");
	}

}
