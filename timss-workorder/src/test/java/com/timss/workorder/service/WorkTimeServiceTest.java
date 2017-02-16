package com.timss.workorder.service;

import static org.junit.Assert.*;

import java.util.Date;

import oracle.sql.DATE;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.vo.WorkTimeVo;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class WorkTimeServiceTest  extends TestUnit {

	@Autowired
	WorkTimeService workTimeService;
	
	@Test
	@Transactional
	public void testCalDay()  throws Exception{
		WorkTimeVo result = new WorkTimeVo();
		Date startdate = new Date();
		long start = startdate.getTime();
		long end = start+2*24*60*60*1000;
		
		result.setStart(new Date());
		result.setEnd(new Date(end));
		result.setSiteId("ITC");
		result.setMorning("0730");
		result.setForenoon("1200");
		result.setNoon("1400");
		result.setAfternoon("1730");
		result.setWorkTime(8);
		result.setFlag(false);
		double hours = workTimeService.calDay(result)*8;
		System.out.println("工作时间差（小时）为："+hours);
		
	}

}
