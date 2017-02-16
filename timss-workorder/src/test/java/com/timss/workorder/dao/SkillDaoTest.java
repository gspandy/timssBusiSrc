package com.timss.workorder.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.bean.WoSkill;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class SkillDaoTest extends TestUnit {

	@Autowired
	private WoSkillDao skillDao; 
	//@Test
	public void testInsertSkill() {
		int id = skillDao.getNextParamsConfId();
		WoSkill skill = new WoSkill();
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
		WoSkill woSkill = skillDao.queryWoSkillById(27);
		System.out.println(woSkill);
	}
	//@Test
	public void testGetNextParamsConfId(){
		int id = skillDao.getNextParamsConfId();
		System.out.println(id);
	}
}
