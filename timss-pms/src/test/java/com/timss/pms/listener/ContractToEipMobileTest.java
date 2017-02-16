package com.timss.pms.listener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.pms.util.FlowEipUtil;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetTask;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
import com.yudean.workflow.service.HistoryInfoService;
import com.yudean.workflow.service.WorkflowService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})

public class ContractToEipMobileTest extends TestUnit{
	@Autowired
	ContractToEipMobile contractToEipMobile;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	HistoryInfoService historyInfoService;
	@Test
	public void test() {
		 TestUnitGolbalService.SetCurentUserById("890167", "ITC");
         ParamDetailBean empb = new ParamDetailBean();
         empb.setFlowNo("PR20141104001");
         empb.setProcessId("358901");
         RetContentBean eip = contractToEipMobile.retrieveWorkflowFormDetails(empb);
         String reVal = JsonHelper.fromBeanToJsonString(eip);
         System.out.println( "result ------" +  reVal );
	}
	
	public void testProcess() throws UnsupportedEncodingException{
		 TestUnitGolbalService.SetCurentUserById("890167", "ITC");
         List<String> nextUser = new ArrayList<String>();
         nextUser.add("890145");
         
         ParamProcessBean empb = new ParamProcessBean();
         empb.setFlowID("next");
         empb.setFlowNo("PR20141201005");
         empb.setNextUser(nextUser);
         empb.setProcessId("411228");
         empb.setTaskKey("jlshjsfa");
         String value = URLDecoder.decode("testzhongwen测试中文", "UTF-8");
         empb.setOpinion( value );
         
         FlowEipUtil flowEipUtil=new FlowEipUtil(null, "411228", workflowService, itcMvcService,historyInfoService);
         
         
         List<RetTask> list=flowEipUtil.getNextTasks();
         empb.setTaskKey("end");
         RetProcessBean emrb = contractToEipMobile.processWorkflow(empb);
         String reVal = JsonHelper.fromBeanToJsonString(emrb);
         System.out.println(JsonHelper.fromBeanToJsonString(list));
         System.out.println("----------------"+reVal);
         
//         empb.setTaskKey(list.get(0).getTask().getValue());
//         emrb = contractToEipMobile.processWorkflow(empb);
//         reVal = JsonHelper.fromBeanToJsonString(emrb);
//         System.out.println(JsonHelper.fromBeanToJsonString(list));
//         System.out.println("----------------"+reVal);
//         
//         empb.setTaskKey(list.get(0).getTask().getValue());
//         emrb = contractToEipMobile.processWorkflow(empb);
//         reVal = JsonHelper.fromBeanToJsonString(emrb);
//         System.out.println(JsonHelper.fromBeanToJsonString(list));
//         System.out.println("----------------"+reVal);
//         
//         empb.setTaskKey(list.get(0).getTask().getValue());
//         emrb = contractToEipMobile.processWorkflow(empb);
//         reVal = JsonHelper.fromBeanToJsonString(emrb);
//         System.out.println(JsonHelper.fromBeanToJsonString(list));
//         System.out.println("----------------"+reVal);
	}
	
	public void testProcessRollBack() throws UnsupportedEncodingException{
		TestUnitGolbalService.SetCurentUserById("890167", "ITC");
		
        List<String> nextUser = new ArrayList<String>();
        nextUser.add("890167");
        
        ParamProcessBean empb = new ParamProcessBean();
        empb.setFlowID("rollback");
        empb.setFlowNo("PR20141104001");
        empb.setNextUser(nextUser);
        empb.setProcessId("358901");
        empb.setTaskKey("xmfzrbxjsfa");
        String value = URLDecoder.decode("testzhongwen退回", "UTF-8");
        empb.setOpinion( value );
        RetProcessBean emrb = contractToEipMobile.processWorkflow(empb);
        String reVal = JsonHelper.fromBeanToJsonString(emrb);
        System.out.println("----------------"+reVal);
	}

}
