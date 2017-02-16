package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Milestone;
import com.timss.pms.vo.MilestoneVo;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})
@Transactional
public class MilestoneDaoTest extends TestUnit{
	@Autowired
    MilestoneDao milestoneDao;
	@Test
	public void test() {
		Milestone milestone=new Milestone();
		milestone.setMilestoneName("里程碑");
		milestone.setProjectId(1628);
		milestoneDao.insertMilestone(milestone);
		
		List<MilestoneVo> milestoneVos=milestoneDao.queryMilestoneListByProjectId(1628);
		assertEquals(1, milestoneVos.size());
		
		milestoneDao.updateMilestone(milestone);
		
		milestoneDao.deleteMilestoneByProjectId(1628);
		
		milestoneVos=milestoneDao.queryMilestoneListByProjectId(1628);
		assertEquals(0, milestoneVos.size());
	}

}
