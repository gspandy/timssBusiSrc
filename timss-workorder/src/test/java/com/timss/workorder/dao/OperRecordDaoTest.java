package com.timss.workorder.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.workorder.bean.OperRecord;
import com.yudean.mvc.testunit.TestUnit;

@ContextConfiguration(locations={
		"classpath:config/context/applicationContext-webserviceClient-config.xml",
		"classpath:config/context/applicationContext-workflow.xml"})
public class OperRecordDaoTest extends TestUnit  {

	@Autowired
    private OperRecordDao operRecordDao;
	
	//@Test
	public void testInsertOperRecord() {
		OperRecord operRecord = new OperRecord();
		operRecord.setFlowId("1234556");
		operRecord.setOperContent("操作内容");
		operRecord.setOperDate(new Date());
		operRecord.setOperType("workPlan");
		operRecord.setOperUser("890152");
		operRecord.setOperUserTeam("team_xxx");
		operRecord.setSiteid("ITC");
		operRecord.setWoId(123456);
		operRecordDao.insertOperRecord(operRecord);
		
	}

}
