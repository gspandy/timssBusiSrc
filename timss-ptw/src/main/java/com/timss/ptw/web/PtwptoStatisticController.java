package com.timss.ptw.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;


@Controller
@RequestMapping(value = "ptw/ptwptoStatistic")
public class PtwptoStatisticController {
    @Autowired
    private ItcMvcService itcMvcService;
    
    @RequestMapping(value = "/ptwptoStatisticPage")
    @ReturnEnumsBind("WO_WIND_STATION")
    public ModelAndView ptwptoStatisticPage() throws Exception {
        UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = infoScope.getSiteId();
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        map.put( "siteId", siteid);
        String jumpUrl =  "ptwptoStatistic.jsp";
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView;
    }
    
    
}
