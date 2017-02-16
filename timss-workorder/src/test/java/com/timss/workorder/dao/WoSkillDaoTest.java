package com.timss.workorder.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.bean.WoSkill;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WoSkillDaoTest extends TestUnit {

	@Autowired 
	private WoSkillDao woSkillDao;
	//@Test
	public void testInsertWoSkill() {
		int id = woSkillDao.getNextParamsConfId();
		WoSkill woSkill = new WoSkill();
		woSkill.setId(id);
		woSkill.setName("技能1");
		woSkill.setRemarks("技能1的说明");
		woSkillDao.insertWoSkill(woSkill);
	}

	//@Test
	public void testUpdateWoSkill() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeleteWoSkill() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryWoSkillById() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNextParamsConfId() {
		System.out.println("xxxx");
	}

}
