package com.timss.pms.service.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
import com.timss.pms.bean.Pay;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.dao.PayplanTmpDao;
import com.timss.pms.service.PayService;
import com.timss.pms.service.PayplanTmpService;

@Transactional
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})
public class PayServiceImplTest extends TestUnit{
	
	@Autowired
	PayService payService;
	@Autowired
	PayplanTmpDao payplanTmpDao;
	@Test
	public void test() {
		Pay pay=new Pay();
		pay.setPayplanId(999);
		pay.setContractId(999);
		pay.setActualpay(403.0);
		pay.setType("income");
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		payService.insertPay(pay,null);
		
		payService.tmpUpdatePay(pay,null);
		
		payService.deletePay(String.valueOf(pay.getId()));
		
		PayplanTmp payplanTmp=new PayplanTmp();
		List<PayplanTmp> payplanTmps=new ArrayList<PayplanTmp>();
		payplanTmps.add(payplanTmp);
		
	
	}

}
