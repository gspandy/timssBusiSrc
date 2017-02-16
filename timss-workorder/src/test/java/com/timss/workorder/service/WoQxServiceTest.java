package com.timss.workorder.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.bean.WoQx;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class WoQxServiceTest  extends TestUnit {

	@Autowired
	WoQxService woQxService ;
	
	@Test
	public void test() {
		TestUnitGolbalService.SetCurentUserById("890128", "SBS");
		List<WoQx> result = woQxService.queryQxByAssetId("AST-00012570", "SBS");
		System.out.println(result.get(0).toString());
	}

}
