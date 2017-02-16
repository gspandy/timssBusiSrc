package com.timss.pms.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.dao.FrontPageDao;

@Service
public class FrontPageService implements com.timss.pms.service.FrontPageService {
	@Autowired
	FrontPageDao frontPageDao;
	@Override
	public double getTotalContractSumThisYear(String siteid) {
		double result=frontPageDao.getTotalContractSumThisYear(siteid);
		return result;
	}

}
