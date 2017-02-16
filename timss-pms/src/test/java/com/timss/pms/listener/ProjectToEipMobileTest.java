package com.timss.pms.listener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.gdyd.esb.services.employee.EmployeeDAO;
import com.timss.pms.service.ProjectService;
import com.timss.pms.util.FlowEipUtil;
import com.yudean.interfaces.service.impl.EipInterfaceService;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetTask;
import com.yudean.itc.util.MD5;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
import com.yudean.workflow.service.HistoryInfoService;
import com.yudean.workflow.service.WorkflowService;
import static org.junit.Assert.*;

public class ProjectToEipMobileTest extends TestUnit{
	@Autowired
	PmsITCEipMobile projectToEipMobile;
	@Autowired
	EipInterfaceService eipInterfaceService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	HistoryInfoService historyInfoService;
	@Autowired
	ProjectService projectService;
	
	@Test
	public void test() {
		 TestUnitGolbalService.SetCurentUserById("890167", "ITC");
         ParamDetailBean empb = new ParamDetailBean();
        
         empb.setFlowNo("PR20150414001");
         empb.setProcessId("315501");
         //RetContentBean eip = projectToEipMobile.retrieveWorkflowFormDetails(empb);
         Object eip= eipInterfaceService.getTaskDetailMobile("890145", MD5.GetMD5Code("1"),"PR20150414001" , "ITC");
         String reVal = JsonHelper.fromBeanToJsonString(eip);
         System.out.println( "result ------" +  reVal );
         
	}
	@Test
	public void testProcess() throws UnsupportedEncodingException{
		 TestUnitGolbalService.SetCurentUserById("890167", "ITC");
         List<String> nextUser = new ArrayList<String>();
         nextUser.add("890145");
         String processInstId="536808";
         String taskKey="";
         ParamProcessBean empb = new ParamProcessBean();
         empb.setFlowID("next");
         empb.setFlowNo("PR20150414001");
         empb.setNextUser(nextUser);
         empb.setProcessId("536808");
         
         String value = URLDecoder.decode("testzhongwen测试中文", "UTF-8");
         empb.setOpinion( value );
         
         FlowEipUtil flowEipUtil=new FlowEipUtil(null, "536808", workflowService, itcMvcService,historyInfoService);
         
         
         List<RetTask> list=flowEipUtil.getNextTasks();
         taskKey="dszsp";
         empb.setTaskKey("dszsp");
         empb.setOpinion("提交董事长申请");
         RetProcessBean emrb = projectToEipMobile.processWorkflow(empb);
         List<org.activiti.engine.task.Task> task=workflowService.getActiveTasks(processInstId);
         assertEquals(taskKey, task.get(0).getTaskDefinitionKey());
         String reVal = JsonHelper.fromBeanToJsonString(emrb);
         System.out.println(JsonHelper.fromBeanToJsonString(list));
         System.out.println("----------------"+reVal);
         
         list=flowEipUtil.getNextTasks();
         taskKey="zjlsh";
         empb.setTaskKey(taskKey);
         empb.setFlowID("rollback");
         empb.setOpinion("董事长回退给总经理");
         emrb = projectToEipMobile.processWorkflow(empb);
         task=workflowService.getActiveTasks(processInstId);
         assertEquals(taskKey, task.get(0).getTaskDefinitionKey());
         reVal = JsonHelper.fromBeanToJsonString(emrb);
         System.out.println(JsonHelper.fromBeanToJsonString(list));
         System.out.println("----------------"+reVal);
         
         list=flowEipUtil.getNextTasks();
         taskKey="xmbhsc";
         empb.setTaskKey(taskKey);
         empb.setOpinion("直接提交到项目编号生成");
         empb.setFlowID("next");
         emrb = projectToEipMobile.processWorkflow(empb);
         task=workflowService.getActiveTasks(processInstId);
         assertEquals(taskKey, task.get(0).getTaskDefinitionKey());
         reVal = JsonHelper.fromBeanToJsonString(emrb);
         System.out.println("----------------"+reVal);
         
         list=flowEipUtil.getNextTasks();
         taskKey="zjlsh";
         empb.setTaskKey(taskKey);
         empb.setFlowID("rollback");
         empb.setOpinion("编号回退给总经理");
         emrb = projectToEipMobile.processWorkflow(empb);
         task=workflowService.getActiveTasks(processInstId);
         assertEquals(taskKey, task.get(0).getTaskDefinitionKey());
         reVal = JsonHelper.fromBeanToJsonString(emrb);
         System.out.println(JsonHelper.fromBeanToJsonString(list));
         System.out.println("----------------"+reVal);
//         
//         list=flowEipUtil.getNextTasks();
//         empb.setTaskKey(list.get(0).getTask().getValue());
//         emrb = projectToEipMobile.processWorkflow(empb);
//         reVal = JsonHelper.fromBeanToJsonString(emrb);
//         System.out.println("----------------"+reVal);
//         
//         list=flowEipUtil.getNextTasks();
//         empb.setTaskKey(list.get(0).getTask().getValue());
//         emrb = projectToEipMobile.processWorkflow(empb);
//         reVal = JsonHelper.fromBeanToJsonString(emrb);
//         System.out.println("----------------"+reVal);
//         
//         list=flowEipUtil.getNextTasks();
//         empb.setTaskKey(list.get(0).getTask().getValue());
//         emrb = projectToEipMobile.processWorkflow(empb);
//         reVal = JsonHelper.fromBeanToJsonString(emrb);
//         System.out.println("----------------"+reVal);
//         
//         list=flowEipUtil.getNextTasks();
//         empb.setTaskKey(list.get(0).getTask().getValue());
//         emrb = projectToEipMobile.processWorkflow(empb);
//         reVal = JsonHelper.fromBeanToJsonString(emrb);
//         System.out.println("----------------"+reVal);
//         
//         list=flowEipUtil.getNextTasks();
//         empb.setTaskKey(list.get(0).getTask().getValue());
//         emrb = projectToEipMobile.processWorkflow(empb);
//         reVal = JsonHelper.fromBeanToJsonString(emrb);
//         System.out.println("----------------"+reVal);
//         
//         list=flowEipUtil.getNextTasks();
//         empb.setTaskKey(list.get(0).getTask().getValue());
//         emrb = projectToEipMobile.processWorkflow(empb);
//         reVal = JsonHelper.fromBeanToJsonString(emrb);
//         System.out.println("----------------"+reVal);
//         
//         list=flowEipUtil.getNextTasks();
//         empb.setTaskKey(list.get(0).getTask().getValue());
//         emrb = projectToEipMobile.processWorkflow(empb);
//         reVal = JsonHelper.fromBeanToJsonString(emrb);
//         System.out.println(JsonHelper.fromBeanToJsonString(list));
//         System.out.println("----------------"+reVal);
	}
    
	public void testProcessRollBack() throws UnsupportedEncodingException{
		TestUnitGolbalService.SetCurentUserById("890167", "ITC");
        List<String> nextUser = new ArrayList<String>();
        nextUser.add("890167");
        
//        ParamProcessBean empb = new ParamProcessBean();
//        empb.setFlowID("rollback");
//        empb.setFlowNo("PR20141022002");
//        empb.setNextUser(nextUser);
//        empb.setProcessId("315501");
//        empb.setTaskKey("xmfzrbxjsfa");
//        String value = URLDecoder.decode("testzhongwen退回", "UTF-8");
//        empb.setOpinion( value );
//        RetProcessBean emrb = projectToEipMobile.processWorkflow(empb);
//        String reVal = JsonHelper.fromBeanToJsonString(emrb);
//        System.out.println("----------------"+reVal);
        eipInterfaceService.processTaskDetailMobile("890139", MD5.GetMD5Code("1"), "next", "", "ITC", "PR20141117003", "", "890139", "");
	}

}
