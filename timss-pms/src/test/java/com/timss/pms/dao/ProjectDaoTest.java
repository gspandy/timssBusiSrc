package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.List;


import org.apache.taglibs.standard.lang.jstl.NullLiteral;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yudean.mvc.testunit.TestUnit;

import com.timss.pms.bean.Plan;
import com.timss.pms.bean.Project;
import com.timss.pms.vo.ProjectDtlVo;
import com.timss.pms.vo.ProjectVo;
import com.yudean.itc.dto.Page;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class ProjectDaoTest extends TestUnit{
    @Autowired
    private ProjectDao projectDao;
    
    @Test
    public void testAll(){
    	
		
		projectDao.queryProjectDetailByYear("2014","ITC");
    }
	
	public void test() {
		Project project =new Project();
		project.setProjectName("projectName");
		project.setPyear("2014");
		projectDao.insertProject(project);
	}
	
	public void update(){
		Project project =new Project();
		project.setId(1016);
		project.setApplyBudget(1000.03);
		project.setPlanId(78);

		project.setPyear("2015");
		project.setProjectName("projectName");
		projectDao.updateProject(project);
	}
	

	public void queryById(){
		ProjectDtlVo project=projectDao.queryProjectById(999);
		assertEquals("projectName", project.getProjectName());
	}
	
	
	public void queryList(){
		
		ProjectVo project =new ProjectVo();
		
		project.setProjectName("projectName");
		Page<ProjectVo> page=new Page<ProjectVo>(1,10);
		page.setParameter("projectName", "Name");
		List<ProjectVo> projects=projectDao.queryProjectList(page);
		
	}
	
	
	
	public void deletePlan(){
		int count=projectDao.deleteProject(82);
	
	}
	
    public void onlyForTest(){
    	BeanWrapper bw=new BeanWrapperImpl(new Project());
    	PropertyEditorRegistrySupport ps=null;
    	bw.setPropertyValue("isRs", "o");
    	Thread.currentThread().getContextClassLoader();
    	ContextLoaderListener listener=null;
    	
    
    }
}
