package com.timss.workorder.dao;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.bean.WoLabel;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WoLabelDaoTest extends TestUnit{

	@Autowired
	private WoLabelDao woLabelDao;
	@Test
	public void testInsertWoLabel() {
		int id = woLabelDao.getNextParamsConfId();
		WoLabel woLabel = new WoLabel();
		woLabel.setId(id);
		woLabel.setName("难搞");
		woLabel.setWeight(12);
		woLabel.setRemarks("难缠，不好打交道");
		
		woLabelDao.insertWoLabel(woLabel);
	}

	//@Test
	public void testUpdateWoLabel() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeleteWoLabel() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryWoLabelById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testGetNextParamsConfId() {
		fail("Not yet implemented");
	}

}
