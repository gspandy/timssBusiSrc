package com.timss.pms.service.core;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.service.PrifixSequenceService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})

public class PrifixSequenceServiceImplTest extends TestUnit{
    @Autowired
    PrifixSequenceService prifixSequenceService;
	@Test
	public void test() {
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		String valString=prifixSequenceService.getNextSequenceVal("2005", "test");
		assertEquals("20051", valString);
		prifixSequenceService.increaseSequence("2005", "test");
		valString=prifixSequenceService.getNextSequenceVal("2005", "test");
		assertEquals("20052", valString);
		
		prifixSequenceService.increaseSequence("2007", "test");
		valString=prifixSequenceService.getNextSequenceVal("2007", "test");
		assertEquals("20072", valString);
	}

}
