package com.timss.finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.service.ItcMvcService;
//import com.yudean.mvc.testunit.TimssTestUnit;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class FinanceSummaryServiceTest extends TestUnit {
	@Autowired
	ItcMvcService itcMvcService;
}
