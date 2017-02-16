package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Outsourcing;
import com.timss.pms.vo.OutsourcingVo;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})
@Transactional
public class OutsourcingDaoTest extends TestUnit{
    @Autowired
    OutsourcingDao outsourcingDao;
	@Test
	public void insert() {
		Outsourcing outsourcing=new Outsourcing();
		outsourcing.setOutsourcingName("hello");
		outsourcing.setProjectId(1628);
		outsourcingDao.insertOutsourcing(outsourcing);
		
		List<OutsourcingVo> outsourcingVos=outsourcingDao.queryOutsourcingListByProjectId(1628);
		assertEquals(1, outsourcingVos.size());
		
		outsourcingDao.updateOutsourcing(outsourcing);
		
		outsourcingDao.deleteOutsourcing(outsourcingVos.get(0).getOutsourcingId());
		
		outsourcingVos=outsourcingDao.queryOutsourcingListByProjectId(1628);
		assertEquals(0, outsourcingVos.size());
		
	}

}
