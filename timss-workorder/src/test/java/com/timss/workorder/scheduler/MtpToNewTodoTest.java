package com.timss.workorder.scheduler;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.yudean.mvc.testunit.TestUnit;

public class MtpToNewTodoTest  extends TestUnit {
	@Autowired
	MtpToNewTodo mtpToNewTodo;
	
	//@Test
	//@Transactional
	public void testCycMtpToNewTodo() throws Exception {
		//TestUnitGolbalService.SetCurentUserById("SBSscheduler", "SBS"); //构建一个登录当前用户
		mtpToNewTodo.cycMtpToNewTodo() ;
		System.out.println("------------------测试函数执行完-----------------------");
	}
	
}
