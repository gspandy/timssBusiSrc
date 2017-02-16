package com.timss.ptw.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.ptw.bean.PtwIsolationPoint;
import com.timss.ptw.vo.PtwIsolationPointVo;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PtwIsolationPointDaoTest extends TestUnit{
	@Autowired
	PtwIsolationPointDao ptwIsolationPointDao;
	
	
	//@Test
	public void testQueryBeanByIsolationPointId() {
		fail("Not yet implemented");
	}

	//@Test
	public void testInsertPtwIsolationPoint() {
		PtwIsolationPoint ptwIsolationPoint = new PtwIsolationPoint();
		ptwIsolationPoint.setMethodId(127);
		ptwIsolationPoint.setPointNo("AST-00010697");
		ptwIsolationPoint.setYxbz(1);
		ptwIsolationPointDao.insertPtwIsolationPoint(ptwIsolationPoint);
	}

	//@Test
	public void testDeletePtwIsolationPointByPointNo(){
		ptwIsolationPointDao.deletePtwIsolationPointByPointNo("AST-00010697", "SJC");
	}
	
	
	//@Test
	public void testQueryBeanByIsolationPointNo(){
		ArrayList<PtwIsolationPointVo> result = ptwIsolationPointDao.queryBeanByIsolationPointNo("AST-00010698", "SJC");
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i));
			
		}
	}
	
}
