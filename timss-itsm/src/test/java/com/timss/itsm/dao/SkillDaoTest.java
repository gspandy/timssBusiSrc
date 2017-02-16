package com.timss.itsm.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.bean.ItsmWoSkill;
import com.timss.itsm.dao.ItsmWoSkillDao;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class SkillDaoTest extends TestUnit {

	@Autowired
	private ItsmWoSkillDao skillDao; 
	//@Test
	public void testInsertSkill() {
		int id = skillDao.getNextParamsConfId();
		ItsmWoSkill skill = new ItsmWoSkill();
		skill.setId(id);
		skill.setName("测试1");
		skill.setRemarks("对技能的说明");
		skillDao.insertWoSkill(skill);
	}

	//@Test
	public void testUpdateSkill() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeleteSkill() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testQuerySkillById() {
		ItsmWoSkill woSkill = skillDao.queryWoSkillById(27);
		System.out.println(woSkill);
	}
	//@Test
	public void testGetNextParamsConfId(){
		int id = skillDao.getNextParamsConfId();
		System.out.println(id);
	}
}
