package com.timss.finance.listener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

public class TestEipInterface extends TestUnit{
	 @Autowired
	 FinanceToEipMobile financeToEipMobile;
   
     public void test() throws UnsupportedEncodingException {
    	 TestUnitGolbalService.SetCurentUserById("890145", "ITC");
         List<String> nextUser = new ArrayList<String>();
         nextUser.add("890145");
         
         ParamProcessBean empb = new ParamProcessBean();
         empb.setFlowID("next");
         empb.setFlowNo("FIN20150325002");
         empb.setNextUser(nextUser);
         empb.setProcessId("509301");
         empb.setTaskKey("accounting_approve");
         String value = URLDecoder.decode("testzhongwen测试中文", "UTF-8");
         empb.setOpinion( value );
         RetProcessBean emrb = financeToEipMobile.processWorkflow(empb);
        
        
     }
     
    @Test
 	public void testFMP() {
 		 TestUnitGolbalService.SetCurentUserById("890167", "ITC");
          ParamDetailBean empb = new ParamDetailBean();
          empb.setFlowNo("FIN20150506006");
          empb.setProcessId("559301");
          RetContentBean eip = financeToEipMobile.retrieveWorkflowFormDetails(empb);
          String reVal = JsonHelper.fromBeanToJsonString(eip);
          System.out.println( "result ------" +  reVal );
 	}
}
