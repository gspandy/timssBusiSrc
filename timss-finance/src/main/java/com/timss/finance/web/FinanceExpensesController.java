package com.timss.finance.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.finance.service.FinanceExpensesService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Controller
@RequestMapping(value = "finance/finaExpenses")
public class FinanceExpensesController {
	
	 @Autowired
	 FinanceExpensesService financeExpensesService;
	 @Autowired
	 ItcMvcService itcMvcService;

    @RequestMapping(value = "/finaExpensesReport", method = RequestMethod.GET)
    public ModelAndView findFinanceInfoList() throws Exception {
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
    	String beginYear = financeExpensesService.queryBeginYearBySiteId(siteId);
    	String jumpUrl = "/financeExpenses/finaExpensesReport.jsp";
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put( "beginYear",  beginYear);
    	ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView ;
    }
    
}
