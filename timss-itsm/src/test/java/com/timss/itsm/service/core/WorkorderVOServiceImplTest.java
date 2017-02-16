package com.timss.itsm.service.core;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.service.ItsmWorkOrderVOService;
import com.timss.itsm.vo.ItsmWorkOrderVO;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WorkorderVOServiceImplTest extends TestUnit{

	@Autowired
	ItsmWorkOrderVOService workOrderVOService ; 
	
	@Test
	public void testQueryWOVOByAssetId() {
		List<ItsmWorkOrderVO> list= workOrderVOService.queryWOVOByAssetId("AST-00004174", "ITC");
		System.out.println(list.size());
		for(int i=0; i<list.size(); i++){
			System.out.println(list.get(i).toString());
			System.out.println(list.get(i).getDescription());
		}
		
		System.out.println("XXXXX");
	}

}
