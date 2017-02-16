package com.timss.finance.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.finance.bean.FinanceFlowMatch;
import com.timss.finance.dao.FinanceFlowMatchDao;
import com.timss.finance.service.FinanceFlowMatchService;

@Service
public class FinanceFlowMatchServiceImpl implements FinanceFlowMatchService {
	
	@Autowired
	FinanceFlowMatchDao financeFlowMatchDao;
	
	@Override
	public FinanceFlowMatch queryFinanceFlowMatchByFid(String fid)
			 {
		FinanceFlowMatch financeFlowMatch=financeFlowMatchDao.queryFinanceFlowMatchByFid(fid);
		return financeFlowMatch;
	}

	
	@Override
	public FinanceFlowMatch queryFinanceFlowMatchByPid(String pid)
			 {
		FinanceFlowMatch financeFlowMatch=financeFlowMatchDao.queryFinanceFlowMatchByPid(pid);
		return financeFlowMatch;
	}

	@Override
	public FinanceFlowMatch insertFinanceFlowMatch(
			FinanceFlowMatch financeFlowMatch)  {
		financeFlowMatchDao.insertFinanceFlowMatch(financeFlowMatch);
		FinanceFlowMatch ffm=financeFlowMatch;
		return ffm;
	}
}
