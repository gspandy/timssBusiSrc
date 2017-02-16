package com.timss.workorder.scheduler;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class WoReleaseToCustSerTest   extends TestUnit{
	@Autowired
	WoReleaseToCustSer woReleaseToCustSer;
	@Test
	public void testReleaseWoByOvertime() throws Exception {
		woReleaseToCustSer.releaseWoByOvertime();
		System.out.println("------------------测试函数执行完-----------------------");
	}

}
