package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.MilestoneHistory;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})
@Transactional
public class MilestoneHistoryDaoTest extends TestUnit{
    @Autowired
    MilestoneHistoryDao milestoneHistoryDao;
	@Test
	public void test() {
		MilestoneHistory milestoneHistory=new MilestoneHistory();
		milestoneHistory.setContent("content");
		milestoneHistory.setProjectId(1677);
		milestoneHistory.setMilestoneHistoryUser("890145");
		milestoneHistory.setTime(new Date());
		milestoneHistory.setOperator("add");
		milestoneHistoryDao.insertMilestoneHistory(milestoneHistory);
		
		List mList=milestoneHistoryDao.queryMilestoneHistoryListByProjectId(1677);
		assertEquals(1, mList.size());
		
		milestoneHistoryDao.deleteMilestoneHistoryByProjectId(1677);
		mList=milestoneHistoryDao.queryMilestoneHistoryListByProjectId(1677);
		assertEquals(0, mList.size());
	}

}
