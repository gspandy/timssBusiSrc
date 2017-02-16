package com.timss.pms.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;
import com.timss.pms.bean.PayplanTmp;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PayplanTmpDaoTest extends TestUnit{

	@Autowired
	PayplanTmpDao payplanTmpDao;
	@Test
	public void test() {
		List<PayplanTmp> payplanTmps=new ArrayList<PayplanTmp>();
		PayplanTmp payplanTmp=new PayplanTmp();
		payplanTmp.setPaySum(1000.0);
		payplanTmp.setContractId("999");
		payplanTmps.add(payplanTmp);
		payplanTmpDao.insertPayplanTmpList(payplanTmps);
		
		payplanTmpDao.deletePayplanTmpByContractId(999);
		
	}

}
