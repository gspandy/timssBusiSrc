package com.timss.pms.service.core;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;
import com.timss.pms.bean.Contract;
import com.timss.pms.service.ContractService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class ContractServiceImplTest extends TestUnit{

	@Autowired
	ContractService contractService;
	@Test
	public void test() {
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		Contract contract=new Contract();
		contract.setName("he");
		contract.setProjectId(999);
		contractService.insertContract(contract, null);
		Contract rContract=contractService.queryContractById(String.valueOf(contract.getId()));
		boolean b=contractService.isContractCodeExisted("2432", null);
		assertEquals(b, true);
		
	}

}
