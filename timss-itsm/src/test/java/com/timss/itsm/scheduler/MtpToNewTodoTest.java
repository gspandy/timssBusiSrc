package com.timss.itsm.scheduler;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.itsm.scheduler.ItsmMtpToNewTodo;
import com.yudean.mvc.testunit.TestUnit;

public class MtpToNewTodoTest  extends TestUnit {
	@Autowired
	ItsmMtpToNewTodo mtpToNewTodo;
	
	//@Test
	//@Transactional
	public void testCycMtpToNewTodo() throws Exception {
		//TestUnitGolbalService.SetCurentUserById("SBSscheduler", "SBS"); //构建一个登录当前用户
		mtpToNewTodo.cycMtpToNewTodo() ;
		System.out.println("------------------测试函数执行完-----------------------");
	}
	
}
