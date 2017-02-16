package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.PlanHistory;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})
@Transactional
public class PlanHistoryDaoTest extends TestUnit{
	 @Autowired
	    PlanHistoryDao planHistoryDao;
		@Test
		public void test() {
			PlanHistory planHistory=new PlanHistory();
			planHistory.setPlanId(673);
			
			planHistoryDao.insertPlanHistory(planHistory);
			
			List mList=planHistoryDao.queryPlanHistoryListByPlanId(673);
			assertEquals(1, mList.size());
			
			planHistoryDao.deletePlanHistoryByPlanId(673);
			mList=planHistoryDao.queryPlanHistoryListByPlanId(673);
			assertEquals(0, mList.size());
		}

}
