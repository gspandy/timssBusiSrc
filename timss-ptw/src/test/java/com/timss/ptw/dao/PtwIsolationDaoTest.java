package com.timss.ptw.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-webserviceClient-config.xml",
"classpath:config/context/applicationContext-workflow.xml"})
public class PtwIsolationDaoTest extends TestUnit{
	@Autowired
	PtwIsolationDao ptwIsolationDao;
	
	@Test
	public void testDelete() {
	    ptwIsolationDao.deletePtwIsolationItem(10,1981);
	}	
}
