package com.timss.itsm.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.dao.ItsmWorkOrderVODao;
import com.timss.itsm.vo.ItsmWorkOrderVO;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WorkOrderVODaoTest extends TestUnit{

	@Autowired
	ItsmWorkOrderVODao workOrderVODao;
	@Test
	public void testQueryWOVOByAssetId() {
		List<ItsmWorkOrderVO> list= workOrderVODao.queryWOVOByAssetId("AST-00009997", "SBS");
		System.out.println(list.size());
		for(int i=0; i<list.size(); i++){
			System.out.println(list.get(i).toString());
			System.out.println(list.get(i).getDescription());
		}
		
		System.out.println("XXXXX");
	}

}
