package com.timss.pms.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;

import com.timss.pms.bean.Contract;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class ContractDaoTest extends TestUnit{
	
	@Autowired
	ContractDao contractDao;
	@Test
	public void insertContract() {
		Contract contract=new Contract();
		contract.setName("contract");
		contract.setProjectId(1022);
		contract.setProjectId(1);
		contract.setStatusApp("approving");
		contractDao.insertContract(contract);
	}
	@Test
	public void queryByContractId(){
		contractDao.queryContractById("1012");
	}

}
