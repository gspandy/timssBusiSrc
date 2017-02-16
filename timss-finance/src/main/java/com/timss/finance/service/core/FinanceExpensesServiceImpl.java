package com.timss.finance.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.finance.dao.FinanceExpensesDao;
import com.timss.finance.service.FinanceExpensesService;

@Service
public class FinanceExpensesServiceImpl implements FinanceExpensesService {
		
	@Autowired
	FinanceExpensesDao financeExpensesDao;
	
	@Override
	public String queryBeginYearBySiteId(String siteId) {
		return financeExpensesDao.queryBeginYearBySiteId(siteId);
	}


}
