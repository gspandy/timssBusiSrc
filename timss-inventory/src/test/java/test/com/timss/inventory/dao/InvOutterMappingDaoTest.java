package test.com.timss.inventory.dao;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvOutterMapping;
import com.timss.inventory.dao.InvOutterMappingDao;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvOutterMappingDaoTest.java
 * @author: 890166
 * @createDate: 2014-8-12
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class InvOutterMappingDaoTest extends TestUnit {
	
	private static final Logger LOG = Logger.getLogger(InvOutterMappingDaoTest.class);
	
	@Autowired
	InvOutterMappingDao invOutterMappingDao;
	
	@Test
	public void testInsertInvOutterMapping(){
		InvOutterMapping iom = new InvOutterMapping();
		iom.setInvId("1");
		iom.setOutterId("2");
		iom.setSiteid("ITC");
		int count = invOutterMappingDao.insertInvOutterMapping(iom);
		LOG.debug("插入数据条数为=============>"+count);
	}
}
