package com.timss.pms.web;

import java.text.NumberFormat;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.pms.service.FrontPageService;
import com.timss.pms.util.CreateReturnMapUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;

@Controller
@RequestMapping("pms/frontPage")
public class FrontPageController {
	@Autowired
	FrontPageService frontPageService;
	@Autowired
	ItcMvcService itcMvcService;
	
	@RequestMapping("getTotalContractSumThisYear")
	public ModelAndViewAjax getTotalContractSumThisYear(){
		UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
		//获取合同总额
		double totalSum = frontPageService.getTotalContractSumThisYear(infoScope.getSiteId());
		//合同总额的单元由元变为万元，并且保留四位小数
		NumberFormat numberFormat=NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(4);
		Map map=CreateReturnMapUtil.createFrontPageSucessReturn(numberFormat.format(totalSum/10000));
		return ViewUtil.Json(map);
	}
}
