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
 * @title: 免考勤名单controller
 * @description: {desc}
 * @company: gdyd
 * @className: FreeAtdController.java
 * @author: zhuw
 * @createDate: 2016年5月5日
 * @updateUser: 
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/freeAtd")
public class FreeAtdController {
    
    @Autowired
    private ExemptService exemptService;

    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * 
     * @description:免考勤名单
     * @author: zhuw
     * @createDate: 2016年5月5日
     * @return:String
     */
    @RequestMapping("/queryFreeAtdMenu")
    public ModelAndView queryExemptAtdMenu(){
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String siteId = userInfoScope.getSiteId();
    	//免考勤人员查询
    	Map<String, Object> dataMap = exemptService.queryRelatedToUsersOrOrgs( siteId, "atdFreeAtd" );
    	return new ModelAndView( "checkin/FreeAtd-updateFreeAtd.jsp", dataMap );
    }
    
    /**
     * 
     * @description:更新免考勤名单
     * @author: zhuw
     * @createDate: 2016年5月5日
     * @return:Map
     */
    @RequestMapping("/updateFreeAtd")
    public Map<String, Object> updateExempt(String userDel, String userAdd, String orgDel, String orgAdd) {

       int count = exemptService.updateExempt(userDel, userAdd, orgDel, orgAdd, "atdFreeAtd" );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }
    
}
