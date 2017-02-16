package com.timss.pms.service.sfc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.dao.SFCContractDao;
import com.timss.pms.service.ContractQueryService;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.ContractVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

public class ContractQueryServiceImpl implements ContractQueryService{
	
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	SFCContractDao sfcContractDao;
	
	private static final Logger LOGGER =Logger.getLogger(ContractQueryServiceImpl.class);
	@Override
	public Page<ContractVo> queryContractListAndFilter(Page<ContractVo> page,
			UserInfoScope userInfo) {
		LOGGER.debug("开始查询合同数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", userInfo.getSiteId());
		List<ContractVo> projects = sfcContractDao.queryContractListAndFilter(page);
		InitVoEnumUtil.initSFCContractVoList(projects, itcMvcService);

		page.setResults(projects);
		LOGGER.debug("查询合同成功");
		
		return page;
	}

}
