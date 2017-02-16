package com.timss.pms.service.sjw;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.service.ContractQueryService;
import com.timss.pms.service.ContractService;
import com.timss.pms.vo.ContractVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

public class ContractQueryServiceImpl implements ContractQueryService{
	
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	
	private static final Logger LOGGER =Logger.getLogger(ContractQueryServiceImpl.class);
	@Override
	public Page<ContractVo> queryContractListAndFilter(Page<ContractVo> page,
			UserInfoScope userInfo) {
	        page = contractService.queryContractList(page, userInfo);
	        LOGGER.info( "查询合同成功" );
	        return page;
	}

}
