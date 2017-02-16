package com.timss.attendance.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.attendance.bean.MeetingBean;
import com.timss.attendance.service.MeetingService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 预定会议室表controller
 * @description: {desc}
 * @company: gdyd
 * @className: MeetingController.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("attendance/meeting")
public class MeetingController {
    private Logger log = Logger.getLogger( MeetingController.class );

    @Autowired
    private MeetingService meetingService;
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    
    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * 
     * @description:MEETING 新建页面
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/insertMeetingMenu")
    @ReturnEnumsBind("ATD_MT_NO")
    public ModelAndView insertMeetingMenu( Long date, String meetingNo ){
      //登录用户
        UserInfoScope userInfoScope = new UserInfoScopeImpl();
        try {
            userInfoScope = privUtil.getUserInfoScope();
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        String deptId = userInfoScope.getOrgId();
        String deptName = userInfoScope.getOrgName();
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "userId", userId );
        dataMap.put( "userName", userName );
        dataMap.put( "deptId", deptId );
        dataMap.put( "deptName", deptName );
        dataMap.put( "roleFlag", getLoginUserRole() );
        dataMap.put( "date", date );
        dataMap.put( "meetingNo", meetingNo );
        return new ModelAndView( "checkin/Meeting-insertMeeting.jsp", dataMap );
    }
    
    /**
     * 
     * @description:MEETING 更新页面
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    @RequestMapping("/updateMeetingMenu")
    @ReturnEnumsBind("ATD_MT_NO")
    public ModelAndView updateMeetingMenu( int id ){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "id", id );
        dataMap.put( "userId", privUtil.getUserInfoScope().getUserId() );
        dataMap.put( "roleFlag", getLoginUserRole() );
        return new ModelAndView( "checkin/Meeting-updateMeeting.jsp", dataMap );
    }


    /**
     * @description:保存预定会议室表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertOrUpdateMeeting")
    public @ResponseBody Map<String, Object> insertOrUpdateMeeting(String formData) {
        log.info( "传入的数据：" + formData );

        MeetingBean meeting = JsonHelper.fromJsonStringToBean( formData, MeetingBean.class );
        
        Map<String, Object> map = meetingService.insertOrUpdateMeeting( meeting );

        return map;

    }
    
    /**
     * @description:校验是否有重叠
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/isValidateMeeting")
    public @ResponseBody Map<String, Object> isValidateMeeting(String formData) {
        log.info( "传入的数据：" + formData );
        
        MeetingBean meeting = JsonHelper.fromJsonStringToBean( formData, MeetingBean.class );
        Map<String, Object> map = new HashMap<String, Object>();
        
        if( StringUtils.isNotBlank( meeting.getMeetingNo() )
                && meeting.getStartDate() != null && meeting.getEndDate() != null ){
            map = meetingService.isValidateMeeting( meeting );
        }
        
        return map;
        
    }
    
    /**
     * @description:渲染日历
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryCalendarByDate")
    public @ResponseBody Map<String, Object> queryCalendarByDate( String start, String end, String meetingNo ) {
        log.info( "传入的数据：" + start + " --- " + end );
        
        Map<String, Object> map = meetingService.queryCalendarByDate( start, end, meetingNo );
        
        return map;
        
    }
    
    /**
     * @description:通过ID查询
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryMeetingById")
    public @ResponseBody Map<String, Object> queryMeetingById( int id ) {
        log.info( "传入的数据：" + id );
        
        MeetingBean meetingBean = meetingService.queryMeetingById( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "meetingBean", meetingBean );
        if( meetingBean.getId() > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
        
    }
    
    /**
     * @description:删除
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping(value="/deleteMeeting",method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> deleteMeeting( int id, String remark ) {
        log.info( "传入的数据：" + id + "---- remark :" + remark );
        
        int count = meetingService.deleteMeeting( id, remark );
        
        Map<String, Object> map = new HashMap<String, Object>();
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
        
    }
    
    /**
     * 
     * @description:拿到用户的角色
     * @author: fengzt
     * @createDate: 2014年9月18日
     * @return:
     */
    private String getLoginUserRole(){
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        //用户拥有的角色
        List<Role> roleList = userInfo.getRoles();
        String roleFlag = null;
        String siteId = userInfo.getSiteId();
        String roleString = siteId + "_ZJL," + siteId +  "_FGJSFZJL," + siteId + "_FGJYFZJL"
                + siteId + "_HYSZY";
        for( Role role : roleList ){
            String roleId = role.getId();
          if( roleString.indexOf( roleId ) > 0 ){
                //领导
                roleFlag = "lingdao";
            }else{
                //其他角色
                if( roleFlag == null ){
                  roleFlag = "normal";  
                }
            }
        }
        return roleFlag;
    }
    
    /**
     * @description:通过meetingNo查询
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryMeetingByNo")
    public @ResponseBody Map<String, Object> queryMeetingByNo( String meetingNo, String more ) {
        log.info( "传入的数据：" + meetingNo );
        
        int amount = 90;
        int rownum = 1000;
        if( StringUtils.isNotBlank( more ) ){
            rownum = 1000;
            amount = 200;
        }
        
        List<MeetingBean> meetingBeans = meetingService.queryMeetingByNo( meetingNo, amount, rownum );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "meetingBeans", meetingBeans );
        if( meetingBeans != null && meetingBeans.size() > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        
        return map;
        
    }
    
}
