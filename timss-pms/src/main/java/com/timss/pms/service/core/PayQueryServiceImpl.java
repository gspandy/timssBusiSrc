package com.timss.pms.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.dao.PayDao;
import com.timss.pms.service.PayQueryService;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.PayVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
@Service
public class PayQueryServiceImpl implements PayQueryService {
	@Autowired 
	ItcMvcService itcMvcService;
	@Autowired
	PayDao payDao;
	private static final Logger LOGGER =Logger.getLogger(PayQueryServiceImpl.class);
	@Override
	public Page<PayVo> queryPayListAndFilter(Page<PayVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询结算数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", userInfo.getSiteId());
		List<PayVo> projects = payDao.queryPayListAndFilter(page);
		InitVoEnumUtil.initPayVoList(projects, itcMvcService);

		page.setResults(projects);
		LOGGER.info("查询结算数据成功");
		
		return page;
	}

}
