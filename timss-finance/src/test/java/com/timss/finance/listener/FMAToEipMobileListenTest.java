package com.timss.finance.listener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

public class FMAToEipMobileListenTest extends TestUnit{
    @Autowired
    FMAToEipMobileListen fmaToEipMobileListen;
	@Test
	public void test() throws UnsupportedEncodingException {
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
        List<String> nextUser = new ArrayList<String>();
        nextUser.add("890145");
        
        ParamDetailBean empb = new ParamDetailBean();
 
        empb.setFlowNo("MA201505060001");
        empb.setProcessId("560301");
        
        String value = URLDecoder.decode("testzhongwen测试中文", "UTF-8");
        
        RetContentBean emrb = fmaToEipMobileListen.retrieveWorkflowFormDetails(empb);
        System.out.println("---------------"+JsonHelper.fromBeanToJsonString(emrb));
	}

}
