package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;
import com.timss.pms.bean.Payplan;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class PayplanDaoTest extends TestUnit{
	@Autowired
	PayplanDao payplanDao;
	@Test
	public void test() {
//		List<Payplan> lists=new ArrayList<Payplan>();
//		Payplan payplan=new Payplan();
//		payplan.setPaySum(100.0);
//		lists.add(payplan);
//		payplanDao.insertPayplanList(lists);
		
	}

}
