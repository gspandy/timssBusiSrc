package com.timss.ptw.dao;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.ptw.bean.PtwKeyBox;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwKeyBoxDaoTest  extends TestUnit {
	@Autowired
	PtwKeyBoxDao  ptwKeyBoxDao;

	//@Test
	public void testQueryPtwKeyBoxList() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryPtwKeyBoxById() {
		fail("Not yet implemented");
	}

	//@Test
	public void testInsertPtwKeyBox() {
		PtwKeyBox ptwKeyBoxInfo = new PtwKeyBox();
		ptwKeyBoxInfo.setKeyBoxNo("852er1364");
		ptwKeyBoxInfo.setUseType("xxxx");
		ptwKeyBoxInfo.setPurpose("隔离方法说明");
		ptwKeyBoxInfo.setSiteid("SJC");
		ptwKeyBoxInfo.setCreateuser("890107");
		ptwKeyBoxInfo.setModifyuser("890107");
		ptwKeyBoxInfo.setCreatedate(new Date());
		ptwKeyBoxInfo.setModifydate(new Date());
		ptwKeyBoxInfo.setYxbz(1);
		
		
		ptwKeyBoxDao.insertPtwKeyBox(ptwKeyBoxInfo);
	}

	//@Test
	public void testUpdatePtwKeyBox() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDeletePtwKeyBoxById() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryPtwKeyBoxByNo() {
		System.out.println(ptwKeyBoxDao.queryByIds("6,2").size());
		System.out.println(1);
	}

}
