package com.timss.pms.service.sfc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.dao.SFCCheckoutDao;
import com.timss.pms.service.CheckoutQueryService;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.CheckoutVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;


public class CheckoutQueryServiceImpl implements CheckoutQueryService {
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	SFCCheckoutDao sfcCheckoutDao;
    private static final Logger LOGGER =Logger.getLogger(CheckoutQueryServiceImpl.class);
	@Override
	public Page<CheckoutVo> queryCheckoutListAndFilter(Page<CheckoutVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询验收数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", userInfo.getSiteId());
		List<CheckoutVo> projects = sfcCheckoutDao.queryCheckoutListAndFilter(page);
		InitVoEnumUtil.initCheckoutVoList(projects, itcMvcService);

		page.setResults(projects);
		LOGGER.info("查询验收数据成功");
		
		return page;
	}

}
