package com.timss.attendance.web;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.support.Configuration;
import com.yudean.itc.manager.support.IConfigurationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 目录controller
 * @description: 行政管理目录
 * @company: gdyd
 * @className: MenuController.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/menuController")
public class AtdMenuController {
    private Logger log = Logger.getLogger( AtdMenuController.class );
    
    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private AtdUserPrivUtil privUtil;
    
    @Autowired
    private IConfigurationManager confManager;
    
    /**
     * 
     * @description:请假申请
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/queryLeaveListMenu")
    @ReturnEnumsBind("ATD_LEI_CATEGORY")
    public String queryLeaveListMenu(){
        return "checkin/Leave-queryAllLeaveList.jsp";
        //return "checkin/Fold-insertFold.jsp";
        //return "checkin/Fold-updateFold.jsp";
        //return "checkin/Leave-queryCalendar.jsp";
        //return "checkin/Leave-queryAngularjs.jsp";
        
    }
    
    /**
     * 
     * @description:请假申请异常列表
     * @author: fengzt
     * @createDate: 2015年4月7日
     * @return:String
     */
    @RequestMapping("/queryLeaveExceptionListMenu")
    @ReturnEnumsBind("ATD_LEI_CATEGORY")
    public String queryLeaveExceptionListMenu(){
        return "checkin/Leave-queryExceptionLeaveList.jsp";
    }
    
    /**
     * 
     * @description:加班申请
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/queryOverTimeListMenu")
    public String queryOverTimeListMenu(){
        //return "checkin/Fold-updateFold.jsp";
        return "checkin/Overtime-queryAllOvertimeList.jsp";
    }
    
    /**
     * 
     * @description:考勤异常
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/queryAbnormityListMenu")
    @ReturnEnumsBind("ATD_AB_CATEGORY")
    public ModelAndView queryAbnormityListMenu(){
        String siteId = privUtil.getUserInfoScope().getSiteId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", siteId );
        
        return new ModelAndView("checkin/Abnormity-queryAllAbnormityList.jsp", map);
    }
    
    /**
     * 
     * @description:考勤统计
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/queryStatListMenu")
    @ReturnEnumsBind("ATD_LEI_CATEGORY")
    public ModelAndView queryStatListMenu(){
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        //角色 
        String roleFlag = privUtil.getStatPrivLevel();
        String userId = userInfo.getUserId();
        String deptId = userInfo.getOrgId();
        String siteId = userInfo.getSiteId();
        //今年的年份
        int year = DateFormatUtil.fromDateToCalendar( new Date() ).get( Calendar.YEAR );
        Date yearFirst = DateFormatUtil.getYearFrist();
        Date yearLast = DateFormatUtil.getYearLast();
        
        String yearFirstStr = "";
        String yearLastStr = "";
        try {
            yearFirstStr = DateFormatUtil.dateToString( yearFirst, "yyyy-MM-dd" );
            yearLastStr = DateFormatUtil.dateToString( yearLast, "yyyy-MM-dd" );
        } catch (ParseException e) {
            log.error( e.getMessage(), e );
        }
        
        map.put( "roleFlag", roleFlag );
        map.put( "userId", userId );
        map.put( "deptId", deptId );
        map.put( "siteId", siteId );
        map.put( "year", year );
        map.put( "yearFirstStr", yearFirstStr );
        map.put( "yearLastStr", yearLastStr );
        
        Configuration configuration = confManager.query( "num_of_retain_atd_machine_data_years", "NaN", "NaN" );
        Date now=new Date();
    	Date startDate=DateFormatUtil.addDate(now, "y", -(Integer.parseInt(configuration.getVal())));//该日期后有报表数据
    	Calendar calendar=DateFormatUtil.fromDateToCalendar( startDate );
        map.put( "startYear", calendar.get(Calendar.YEAR) );
        map.put( "startMonth", calendar.get(Calendar.MONTH) );
        
        return new ModelAndView("checkin/Stat-queryAllStatList.jsp", map);
    }
    
    /**
     * 
     * @description:休假规则
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/queryDefinitionListMenu")
    public String queryDefinitionListMenu(){
        return "checkin/Definition-updateDefinition.jsp";
    }
    
    /**
     * 
     * @description:人员资料
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/queryPersonInfoListMenu")
    public String queryPersonInfoListMenu(){
        return "checkin/Dept-getAllDeptList.jsp";
    }
    
    /**
     * 
     * @description:会议室预定
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @return:
     */
    @RequestMapping("/queryMeetingListMenu")
    @ReturnEnumsBind("ATD_MT_NO")
    public String queryMeetingListMenu(){
        return "checkin/Meeting-queryAllMeetingByCalendar.jsp";
    }
    
    /**
     * 
     * @description:打卡记录
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @return:String
     */
    @RequestMapping("/queryCardDataListMenu")
    @ReturnEnumsBind("ATD_MACHINE_WORK_STATUS")
    public String queryCardDataListMenu(){
        return "checkin/CardData-queryCardDataList.jsp";
    }
    
    /**
     * 
     * @description:出勤情况
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @return:String
     */
    @RequestMapping("/queryCheckMachineListMenu")
    @ReturnEnumsBind("ATD_WORK_STATUS_TYPE,ATD_LEI_CATEGORY,ATD_AB_CATEGORY")
    public ModelAndView queryCheckMachineListMenu(){
        Map<String, Object> map = new HashMap<String, Object>();
        String siteId = privUtil.getUserInfoScope().getSiteId();
        map.put( "siteId", siteId );
        return new ModelAndView("checkin/WorkStatus-queryWorkStatusList.jsp", map);
    }
    
    
}
