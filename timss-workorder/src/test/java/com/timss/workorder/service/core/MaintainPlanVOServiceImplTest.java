package com.timss.workorder.service.core;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.service.MaintainPlanVOService;
import com.timss.workorder.vo.MaintainPlanVO;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class MaintainPlanVOServiceImplTest extends TestUnit{
	@Autowired
	MaintainPlanVOService maintainPlanVOService;
	
	@Test
	public void testQueryMTPVOByAssetId() {
		List<MaintainPlanVO> list = maintainPlanVOService.queryMTPVOByAssetId("AST-00010043", "SBS");
		System.out.println(list.size());
		for(int i=0; i<list.size(); i++){
			System.out.println(list.get(i).toString());
			System.out.println(list.get(i).getMaintainPlanCode());
		}
		
		System.out.println("XXXXX");
	}

}
