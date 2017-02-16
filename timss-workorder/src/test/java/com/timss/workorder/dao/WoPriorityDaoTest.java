package com.timss.workorder.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.bean.WoPriority;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WoPriorityDaoTest extends TestUnit {

	@Autowired
	private WoPriorityDao woPriorityDao;
	@Test
	public void testInsertWoPriority() {
		int id = woPriorityDao.getNextParamsConfId();
		WoPriority woPriority= new WoPriority();
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
