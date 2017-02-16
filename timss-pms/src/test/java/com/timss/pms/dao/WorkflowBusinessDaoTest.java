package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;

import com.timss.pms.bean.WorkflowBusiness;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class WorkflowBusinessDaoTest extends TestUnit{
    
	@Autowired
	WorkflowBusinessDao wfBusinessDao;
	
	@Test
	public void testAll(){
		String businessId=new Date().toString();
		String instanceId=new Date().toString()+"1";
		WorkflowBusiness wfbBusiness=new WorkflowBusiness();
		wfbBusiness.setBusinessId(businessId);
		wfbBusiness.setInstanceId(instanceId);
		wfBusinessDao.insertWorkflowBusiness(wfbBusiness);
		
		String tmp=wfBusinessDao.queryBusinessIdByWorkflowId(instanceId);
		assertEquals(businessId, tmp);
		
		tmp=wfBusinessDao.queryWorkflowIdByBusinessIdAndInstancePreffix(businessId, "");
		assertEquals(instanceId, tmp);
		
		String wStrin=wfBusinessDao.queryWorkflowIdByBusinessId("project_1509");
	};
	
	public void insert() {
		WorkflowBusiness wfbBusiness=new WorkflowBusiness();
		wfbBusiness.setBusinessId("businessId");
		wfbBusiness.setInstanceId("instanceId");
		wfBusinessDao.insertWorkflowBusiness(wfbBusiness);
	}
	
	
	public void queryByWorkflowId(){
		String bidString=wfBusinessDao.queryBusinessIdByWorkflowId("instanceId");
		assertEquals("businessId", bidString);
	}
	
	
	public void queryByBusinessId(){
		String wfidString=wfBusinessDao.queryWorkflowIdByBusinessIdAndInstancePreffix("businessId", "instanceI");
		assertEquals("instanceId", wfidString);
		
	}

}
