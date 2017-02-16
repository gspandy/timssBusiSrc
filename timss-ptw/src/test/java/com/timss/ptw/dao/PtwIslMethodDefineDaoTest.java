package com.timss.ptw.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.ptw.bean.PtwIslMethodDefine;
import com.timss.ptw.dao.PtwIslMethodDefineDao;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwIslMethodDefineDaoTest  extends TestUnit {
	@Autowired
	 PtwIslMethodDefineDao  ptwIslMethodDefineDao;
	
	//@Test
	public void testQueryPtwIsLMethDefList() {
		fail("Not yet implemented");
	}

	//@Test
	public void testQueryPtwIsLMethDefByPtwId() {
		PtwIslMethodDefine data  = ptwIslMethodDefineDao.queryPtwIsLMethDefById(82);
		System.out.println(data);
	}

	//@Test
	public void testInsertPtwIsLMethDef() {
		PtwIslMethodDefine ptwIsMethDefInfo = new PtwIslMethodDefine();
		ptwIsMethDefInfo.setNo("xxxx");
		ptwIsMethDefInfo.setMethod("隔离方法说明");
		ptwIsMethDefInfo.setSiteid("SJC");
		ptwIsMethDefInfo.setCreateuser("890107");
		ptwIsMethDefInfo.setModifyuser("890107");
		ptwIsMethDefInfo.setCreatedate(new Date());
		ptwIsMethDefInfo.setModifydate(new Date());
		ptwIsMethDefInfo.setYxbz(1);
		
		
		ptwIslMethodDefineDao.insertPtwIsLMethDef(ptwIsMethDefInfo);
	}

	//@Test
	public void testUpdatePtwIsLMethDef() {
		PtwIslMethodDefine data  = ptwIslMethodDefineDao.queryPtwIsLMethDefById(82);
		data.setMethod("hahahhahah");
		ptwIslMethodDefineDao.updatePtwIsLMethDef(data);
		System.out.println(data);
	}

	//@Test
	public void testDeletePtwIsLMethDefById() {
		ptwIslMethodDefineDao.deletePtwIsLMethDefById(82);
		PtwIslMethodDefine data  = ptwIslMethodDefineDao.queryPtwIsLMethDefById(82);
		System.out.println(data);
	}

	@Test
	public void testQueryPtwIsLMethDefByNo() {
		int count  = ptwIslMethodDefineDao.queryPtwIsLMethDefByNo("xxxx","SJC");
		System.out.println(count);
	}
}
