package com.timss.finance.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.service.ItcMvcService;
//import com.yudean.mvc.testunit.TimssTestUnit;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class FinanceMainServiceTest extends TestUnit {

	@Autowired
	ItcMvcService itcMvcService;
	
	@Autowired
	FinanceMainService financeMainService;
	
	@Test
	public void testQueryFinanceMainByFid() throws Exception{
		String fid="FIN201407200001";
		System.out.println(
				financeMainService.queryFinanceMainByFid(fid)
				);
	
		
		
	}
}
