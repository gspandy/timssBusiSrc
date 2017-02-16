package com.timss.pms.service.core;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.activiti.engine.HistoryService;
import org.apache.commons.io.input.ReaderInputStream;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
import com.yudean.workflow.service.WorkflowService;
import com.timss.pms.bean.Project;
import com.timss.pms.dao.ProjectDao;
import com.timss.pms.service.ProjectService;
import com.timss.pms.vo.ProjectDtlVo;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})
@Transactional
public class ProjectServiceImplTest extends TestUnit{
	@Autowired
	ProjectService projectService;
	@Autowired
	ProjectDao projectDao;
	@Autowired
	HistoryService historyService;
	@Autowired
	WorkflowService workflowService;
	
	public void test() {
		Project project=new Project();
		project.setProjectName("测试项目 包含站点信息 genx");
		project.setPlanId(1012);
		double rDouble=projectDao.queryProjectPaySumById(468);
		assertEquals(54360.0, rDouble);
		
	}
	
	@Test
	public void testPattern(){
		
		
	}

}
