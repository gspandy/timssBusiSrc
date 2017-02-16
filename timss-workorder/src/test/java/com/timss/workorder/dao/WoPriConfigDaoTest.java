package com.timss.workorder.dao;

import static org.junit.Assert.*;

import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.bean.WoPriConfig;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class WoPriConfigDaoTest  extends TestUnit{
	@Autowired
    private WoPriConfigDao woPriConfigDao;
	
	//@Test
	public void testInsertWoPriConfig() {
		WoPriConfig woPriConfig = new WoPriConfig();
		woPriConfig.setPriId(190);
		woPriConfig.setInfluenceScope("YYYY");
		woPriConfig.setUrgentDegree("XXXX");
		woPriConfig.setSiteid("ITC");
		woPriConfigDao.insertWoPriConfig(woPriConfig);
	}

	//@Test
	public void testDeleteWoPriConfig() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryWoPriConfigListById() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testQueryWoPriConfigByOtherCode(){
		WoPriConfig woPriConfig = woPriConfigDao.queryWoPriConfigByOtherCode("critical_urgencyDegree", "low_influenceScope", "INC");
		System.out.println(woPriConfig==null);
	}
}
