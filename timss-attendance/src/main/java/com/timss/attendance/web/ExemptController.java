package com.timss.attendance.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.timss.attendance.service.ExemptService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 免打卡名单controller
 * @description: {desc}
 * @company: gdyd
 * @className: ExemptController.java
 * @author: zhuw
 * @createDate: 2016年1月25日
 * @updateUser: 
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/exempt")
public class ExemptController {
    
    @Autowired
    private ExemptService exemptService;

    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * 
     * @description:免打卡名单
     * @author: zhuw
     * @createDate: 2016年1月25日
     * @return:String
     */
    @RequestMapping("/queryExemptAtdMenu")
    public ModelAndView queryExemptAtdMenu(){
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String siteId = userInfoScope.getSiteId();
    	//免打卡人员查询
    	Map<String, Object> dataMap = exemptService.queryRelatedToUsersOrOrgs( siteId, "atdExempt" );
    	return new ModelAndView( "checkin/Exempt-updateExempt.jsp", dataMap );
    }
    
    /**
     * 
     * @description:更新免打卡名单
     * @author: zhuw
     * @createDate: 2016年1月25日
     * @return:Map
     */
    @RequestMapping("/updateExempt")
    public Map<String, Object> updateExempt(String userDel, String userAdd, String orgDel, String orgAdd) {

       int count = exemptService.updateExempt(userDel, userAdd, orgDel, orgAdd, "atdExempt" );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }
    
}
