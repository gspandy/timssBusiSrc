package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Workload;
import com.timss.pms.vo.WorkloadVo;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})
@Transactional
public class WorkloadDaoTest extends TestUnit{
	@Autowired
    WorkloadDao workloadDao;
	@Test
	public void test() {
		Workload workload=new Workload();
		workload.setUserLevel("hello");
		workload.setProjectId(1628);
		workloadDao.insertWorkload(workload);
		
		List<WorkloadVo> workloadVos=workloadDao.queryWorkloadListByProjectId(1628);
		assertEquals(1, workloadVos.size());
		
		workloadDao.updateWorkload(workload);
		
		workloadDao.deleteWorkload(workloadVos.get(0).getWorkloadId());
		
		workloadVos=workloadDao.queryWorkloadListByProjectId(1628);
		assertEquals(0, workloadVos.size());
	}

}
