package com.timss.pms.service.core;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Outsourcing;
import com.timss.pms.dao.OutsourcingDao;
import com.timss.pms.service.OutsourcingService;
import com.timss.pms.vo.OutsourcingVo;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})

public class OutsourcingServiceImplTest extends TestUnit{
    
	@Autowired
    OutsourcingService outsourcingService;
	@Test
	public void insert() {
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		Outsourcing outsourcing=new Outsourcing();
		outsourcing.setOutsourcingName("hello");
		outsourcing.setProjectId(1654);
		outsourcingService.insertOutsourcing(outsourcing);
		
		
	}

}
