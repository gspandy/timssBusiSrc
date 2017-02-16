package com.timss.workorder.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;
import com.timss.workorder.vo.MaintainPlanVO;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class MaintainPlanVODaoTest extends TestUnit{
	@Autowired
	MaintainPlanVODao maintainPlanVODao;
	@Test
	public void testQueryMTPByAssetId() {
		List<MaintainPlanVO> list = maintainPlanVODao.queryMTPVOByAssetId("AST-00010043", "SBS");
		System.out.println(list.size());
		for(int i=0; i<list.size(); i++){
			System.out.println(list.get(i).toString());
			System.out.println(list.get(i).getMaintainPlanCode());
		}
		
		System.out.println("XXXXX");
	}

}
