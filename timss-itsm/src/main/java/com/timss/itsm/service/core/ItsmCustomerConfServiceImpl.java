package com.timss.itsm.service.core;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.itsm.bean.ItsmCustomerConf;
import com.timss.itsm.dao.ItsmCustomerConfDao;
import com.timss.itsm.service.ItsmCustomerConfService;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;


@Service
public class ItsmCustomerConfServiceImpl implements ItsmCustomerConfService {
	@Autowired
	private ItsmCustomerConfDao itsmCustomerConfDao;
	@Autowired
	private ItcMvcService itcMvcService;
	private static final Logger LOG = Logger.getLogger(ItsmCustomerConfServiceImpl.class);

	@Override
	public Page<ItsmCustomerConf> queryAllCustomerConf(
			Page<ItsmCustomerConf> page) {
		LOG.info("--------客服配置列表查询----------");
		List<ItsmCustomerConf> ret = itsmCustomerConfDao.queryCustomerConfList(page);
		page.setResults(ret);
		return page;
	}

	@Override
	public void insertCustomerConf(ItsmCustomerConf bean) {
		int id = itsmCustomerConfDao.getNextParamsConfId();
		bean.setId(id);
		itsmCustomerConfDao.insertCustomerConfInfo(bean);
		
	}

	@Override
	public void updateCustomerConf(ItsmCustomerConf bean) {
		itsmCustomerConfDao.updateCustomerConfInfo(bean);
		
	}

	@Override
	public ItsmCustomerConf queryCustomerConfById(int customerConfId) {
		return itsmCustomerConfDao.queryCustomerConfById(customerConfId);
	}

	@Override
	public void deleteCustomerConf(int customerConfId) {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId =userInfoScope.getUserId();
		itsmCustomerConfDao.deleteCustomerConf(customerConfId,new Date(),userId);
		
	}

	@Override
	public int judgeRepeatCustomerConf(String customerCode) {
		return itsmCustomerConfDao.judgeRepeatCustomerConf(customerCode);
	}

	@Override
	public ItsmCustomerConf getInitPriority(String customerCode,
			String faultTypeId) {
		return itsmCustomerConfDao.getInitPriority(customerCode,faultTypeId);
	}

	
}
