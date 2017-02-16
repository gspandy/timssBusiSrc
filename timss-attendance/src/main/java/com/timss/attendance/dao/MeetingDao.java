package com.timss.attendance.dao;

import java.util.List;
import java.util.Map;

import com.timss.attendance.bean.MeetingBean;

/**
 * 
 * @title: 预定会议室DAO
 * @description: {desc}
 * @company: gdyd
 * @className: meetingDao.java
 * @author: fengzt
 * @createDate: 2015年3月5日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface MeetingDao {

    /**
     * 
     * @description:插入
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param meetingBean
     * @return:int
     */
    int insertMeeting(MeetingBean meetingBean);

    /**
     * 
     * @description:通过ID查询
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param id
     * @return:MeetingBean
     */
    MeetingBean queryMeetingById(int id);

    /**
     * 
     * @description:更新
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param meetingBean
     * @return:int
     */
    int updateMeeting(MeetingBean meetingBean);

    /**
     * 
     * @description:检查同一个会议室时间段的bean
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param meetingBean
     * @return:List<MeetingBean>
     */
    List<MeetingBean> queryMeetingByDiffDate(MeetingBean meetingBean);

    /**
     * 
     * @description:会议室号-时间段-查询
     * @author: fengzt
     * @createDate: 2015年3月6日
     * @param params
     * @return:List<MeetingBean>
     */
    List<MeetingBean> queryCalendarByDate(Map<String, Object> params);

    /**
     * 
     * @description:当删除时修改状态
     * @author: fengzt
     * @createDate: 2015年3月6日
     * @param meetingBean
     * @return:int
     */
    int updateMeetingForStatus(MeetingBean meetingBean);

    /**
     * 
     * @description:会议室号-时间段-查询(rownum)
     * @author: fengzt
     * @createDate: 2015年3月6日
     * @param params
     * @return:List<MeetingBean>
     */
    List<MeetingBean> queryCalendarByDateMore(Map<String, Object> params);

}
