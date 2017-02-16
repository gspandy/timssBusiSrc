package com.timss.workorder.service;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class ItWoStatisServiceTest  extends TestUnit {

	@Autowired
	ItWoStatisService itWoStatisService ;
	
	@Test
	@Transactional
	public void testQueryItWoStatisticVO() throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date beginTime = sdf.parse("2014-11-08");
		itWoStatisService.queryItWoStatisticVO(beginTime, new Date(), "ITC", "requstStatistic");
	}

}
