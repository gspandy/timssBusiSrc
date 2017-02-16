package com.timss.itsm.service;

import java.util.HashMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.bean.ItsmWoSkill;
import com.timss.itsm.service.ItsmWoSkillService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WoSkillServiceTest  extends TestUnit {
	@Autowired
	ItsmWoSkillService woSkillService ;
	@Test
	public void testQueryWoSkillById() {
		
		TestUnitGolbalService.SetCurentUserById("890107", "ITC");
		woSkillService.queryWoSkillById(0);
	}

	//@Test
	public void testInsertWoSkill(){
		HashMap<String, String> addWoSkillDataMap = new HashMap<String, String>();
		String formdataString  = "[]";
		addWoSkillDataMap.put("skillForm",formdataString);
		
		woSkillService.insertWoSkill(addWoSkillDataMap);
		
	}
}
