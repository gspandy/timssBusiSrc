package com.timss.finance.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.finance.bean.FinancePageConfig;
import com.timss.finance.dao.FinancePageConfigDao;
import com.timss.finance.service.FinancePageConfigService;
@Service 
public class FinancePageConfigServiceImpl implements FinancePageConfigService{
    @Autowired
    FinancePageConfigDao financePageConfigDao;
    @Override
    public List<FinancePageConfig> getFinPageConfByFlowType(String flowType, String siteid) {
        List<FinancePageConfig> result = financePageConfigDao.getFinPageConfByFlowType( flowType, siteid );
        return result;
    }
   
	

}
