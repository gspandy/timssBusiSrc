package com.timss.attendance.service;

import java.util.List;
import java.util.Map;

import com.timss.attendance.bean.MeetingBean;

/**
 * 
 * @title: 预定会议室service
 * @description: {desc}
 * @company: gdyd
 * @className: MeetingService.java
 * @author: fengzt
 * @createDate: 2015年3月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface MeetingService {

    /**
     * 
     * @description:插入或者更新 预定会议室
     * @author: fengzt
     * @createDate: 2015年3月4日
     * @param meeting
     * @return:Map<String, Object>
     */
    Map<String, Object> insertOrUpdateMeeting(MeetingBean meeting);
    
    /**
     * 
     * @description:校验是否同一个会议室在时间段是否有冲突
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param meetingBean
     * @return:Map<String, Object>
     */
    Map<String, Object> isValidateMeeting(MeetingBean meetingBean);
    
    
    /**
     * 
     * @description:通过ID查询
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param id
     * @return:
     */
    MeetingBean queryMeetingById(int id);

    /**
     * 
     * @description:日历时间渲染
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param start
     * @param end
     * @param meetingNo 
     * @return:Map<String, Object>
     */
    Map<String, Object> queryCalendarByDate(String start, String end, String meetingNo);

    /**
     * 
     * @description:删除
     * @author: fengzt
     * @createDate: 2015年3月6日
     * @param id
     * @param remark
     * @return:int
     */
    int deleteMeeting(int id, String remark);

    /**
     * 
     * @description:通过会议室编号来查询
     * @author: fengzt
     * @createDate: 2015年3月23日
     * @param meetingNo
     * @param amount 天数
     * @param rownum  条数
     * @return:List<MeetingBean>
     */
    List<MeetingBean> queryMeetingByNo(String meetingNo, int amount , int rownum );

}
