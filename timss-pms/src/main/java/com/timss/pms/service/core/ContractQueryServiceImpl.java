package com.timss.pms.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.dao.ContractDao;
import com.timss.pms.service.ContractQueryService;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.ContractVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class ContractQueryServiceImpl implements ContractQueryService{
	
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	ContractDao contractDao;
	
	private static final Logger LOGGER =Logger.getLogger(ContractQueryServiceImpl.class);
	@Override
	public Page<ContractVo> queryContractListAndFilter(Page<ContractVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询合同数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", userInfo.getSiteId());
		List<ContractVo> contracts = contractDao.queryContractListAndFilter(page);
		InitVoEnumUtil.initContractVoList(contracts, itcMvcService);

		page.setResults(contracts);
		LOGGER.info("查询招标合同成功");
		
		return page;
	}

}
