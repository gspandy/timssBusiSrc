package com.timss.facade.service;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import com.yudean.homepage.service.HomepageService;
//import com.yudean.itc.code.NotificationType;
//import com.yudean.itc.dto.sec.SecureUser;
//import com.yudean.itc.manager.support.INotificationManager;
//import com.yudean.mvc.bean.userinfo.UserInfo;
//import com.yudean.mvc.service.ItcMvcService;
//import com.yudean.mvc.testunit.TestUnit;
//
//@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
//public class sendMainTest extends TestUnit {
//
//	// @Autowired
//	// INotificationManager notificationManager;
//	//
//	// @Autowired
//	// ItcMvcService itcMvcService;
//	//
//	// @Autowired
//	// HomepageService homepageService;
//	//
//	// @Test
//	// public void mainSendTest() {
//	//
//	// UserInfo user = itcMvcService.getUserInfoById("890128");
//	// List<SecureUser> recipients = new ArrayList<SecureUser>();
//	// recipients.add(user.getSecureUser());
//	// NotificationType[] type = { NotificationType.EMAIL };
//	//
//	// Map<String, Object> bindMap = new HashMap<String, Object>();
//	// bindMap.put("task_createusername", "测试创建用户");
//	// bindMap.put("task_type", "测试类型");
//	// bindMap.put("task_name", "测试环节名称");
//	// bindMap.put("task_createdeptname", "测试部门");
//	// bindMap.put("task_createusercode", "测试创建人");
//	//
//	// notificationManager.notify(recipients, type, "hop_TaskDoing", bindMap);
//	// }
//	//
//	// @Test
//	// public void testCycMtpToNewTodo() throws Exception {
//	// String flowCode = "123";
//	// String jumpPath = "workorder/maintainPlan/queryFullMTPPage.do?todoCode="
//	// + flowCode + "&maintainPlanId=" + 1235;
//	//
//	// // 构建Bean
//	// HomepageWorkTask homeworkTask = new HomepageWorkTask();
//	// homeworkTask.setFlow(flowCode);// 编号，如工单编号 WO20140902001
//	// homeworkTask.setName("周期工单生成"); // 名称
//	// homeworkTask.setProcessInstId("12345");// 草稿时流程实例ID可以不用设置
//	// homeworkTask.setStatusName("新建工单"); // 状态
//	// homeworkTask.setType(HomepageWorkTask.TaskType.Process); //
//	// 枚举类型定义是草稿还是流程,HomepageWorkTask.TaskType.Draft
//	// // 草稿;xxxx.Process
//	// // 流程实例
//	// homeworkTask.setTypeName("工单"); // 类别
//	// homeworkTask.setUrl(jumpPath); // 扭转的URL
//	//
//	// // 加入待办列表
//	// Integer ii = homepageService.create(homeworkTask,
//	// itcMvcService.getUserInfoScopeDatas());
//	//
//	// List<String> todoUserList = new ArrayList<String>();
//	// todoUserList.add(itcMvcService.getUserInfoById("898000").getUserId());
//	// // 四个参数分别是：唯一主键，状态，给谁（列表），谁操作
//	// homepageService.Process("12345", "新建工单", todoUserList,
//	// itcMvcService.getUserInfoScopeDatas());
//	// }
//}
