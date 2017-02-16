package com.timss.itsm.service.core;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.service.ItsmMaintainPlanVOService;
import com.timss.itsm.vo.ItsmMaintainPlanVO;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class MaintainPlanVOServiceImplTest extends TestUnit{
	@Autowired
	ItsmMaintainPlanVOService maintainPlanVOService;
	
	@Test
	public void testQueryMTPVOByAssetId() {
		List<ItsmMaintainPlanVO> list = maintainPlanVOService.queryMTPVOByAssetId("AST-00010043", "SBS");
		System.out.println(list.size());
		for(int i=0; i<list.size(); i++){
			System.out.println(list.get(i).toString());
			System.out.println(list.get(i).getMaintainPlanCode());
		}
		
		System.out.println("XXXXX");
	}

}
