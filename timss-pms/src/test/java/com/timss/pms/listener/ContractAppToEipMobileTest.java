package com.timss.pms.listener;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

public class ContractAppToEipMobileTest extends TestUnit{
	
    @Autowired
    ContractAppToEipMobile contractAppToEipMobile;
	@Test
	public void test() {
		 TestUnitGolbalService.SetCurentUserById("890167", "ITC");
         ParamDetailBean empb = new ParamDetailBean();
         empb.setFlowNo("PR20150511001");
         empb.setProcessId("565529");
         RetContentBean eip = contractAppToEipMobile.retrieveWorkflowFormDetails(empb);
         String reVal = JsonHelper.fromBeanToJsonString(eip);
         System.out.println( "result ------" +  reVal );
	}

}
