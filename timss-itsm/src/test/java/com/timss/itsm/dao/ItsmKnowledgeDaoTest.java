package com.timss.itsm.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmKnowledge;
import com.timss.itsm.service.ItsmKnowledgeService;
import com.yudean.mvc.testunit.TestUnit;

public class ItsmKnowledgeDaoTest extends TestUnit  {
	
	@Autowired
	ItsmKnowledgeDao itsmKnowledgeDao ;
	

	//@Test
	//@Transactional
	public void testQueryNormKLBaseList() {
		fail("Not yet implemented");
	}

//	@Test
//	@Transactional
	public void testQueryItsmKnowledgeById() {
		fail("Not yet implemented");
	}

//	@Test
//	@Transactional
	public void testInsertItsmKnowledge() {
		fail("Not yet implemented");
	}

//	@Test
//	@Transactional
	public void testUpdateItsmKnowledge() {
		fail("Not yet implemented");
	}

//	@Test
//	@Transactional
	public void testDeleteItsmKnowLedge() {
		fail("Not yet implemented");
	}

	@Test
	@Transactional
	public void testKnowledgeDao(){
		ItsmKnowledge itsmKnowledge = new ItsmKnowledge();
		itsmKnowledge.setId(123);
		itsmKnowledge.setCreatedate(new Date());
		itsmKnowledge.setCreateuser("890152");
		itsmKnowledge.setKeywords("keywords1,keywords2");
		itsmKnowledge.setName("测试1");
		itsmKnowledge.setSiteid("ITC");
		itsmKnowledge.setSolutionDescription("解决方案1");
		itsmKnowledge.setTroubleDescription("问题描述1");
		itsmKnowledge.setTypeId("203");
		
		itsmKnowledgeDao.insertItsmKnowledge(itsmKnowledge);
		
		ItsmKnowledge queryKl = itsmKnowledgeDao.queryItsmKnowledgeById(123);
		System.out.println(queryKl);
		assert queryKl.getId()== 123;
	}
}
