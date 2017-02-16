package test.com.timss.inventory.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.dao.InvMatMappingDao;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatMappingDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-19
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class InvMatMappingDaoTest extends TestUnit{

	private static final Logger LOG = Logger.getLogger(InvMatMappingDaoTest.class);
	
	@Autowired
	InvMatMappingDao invMatMappingDao;
	
	@Test
	public void testQueryInvMatMappingInfo(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("outterid", "IWII0004");
		map.put("type", "storagematerials");
		List<InvMatMapping> immList = invMatMappingDao.queryInvMatMappingInfo(map);
		for(InvMatMapping imm : immList){
			LOG.debug(imm.getImtdid());
		}
	}
	
	@Test
	public void testInsertInvMatMapping(){
		InvMatMapping imm = new InvMatMapping();
		imm.setImtdid("IMDI0003");
		imm.setOutterid("IWII0011");
		imm.setTranType("storagematerials");
		int count = invMatMappingDao.insertInvMatMapping(imm);
		LOG.debug("共添加了记录条数为=============>"+count);
	}
	
	@Test
	public void testDeleteMatMappingByImtdid(){
		int count = invMatMappingDao.deleteMatMappingByImtdid("IMDI0003");
		LOG.debug("共删除了关联记录条数为=============>"+count);
	}
}
