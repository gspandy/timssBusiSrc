package com.timss.finance.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.timss.finance.bean.FinanceManagementApply;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

public class FinanceManagementApplyDaoTest extends TestUnit{
    @Autowired
    FinanceManagementApplyDao financeManagementApplyDao;
	@Test
	@Transactional
	public void test() {
		TestUnitGolbalService.SetCurentUserById("890145", "ITC");
		String idString=null;
		FinanceManagementApply financeManagementApply=new FinanceManagementApply();
		financeManagementApply.setId("");
		financeManagementApplyDao.insertFinanceManagementApply(financeManagementApply);
		idString=financeManagementApply.getId();
		financeManagementApplyDao.updateFinanceManagementApply(financeManagementApply);
		financeManagementApplyDao.queryFinanceManagementApplyById(idString);
		financeManagementApplyDao.queryFinanceManagementList(null);
		financeManagementApplyDao.deleteFinanceManagementApplyById(idString);
	}

}
