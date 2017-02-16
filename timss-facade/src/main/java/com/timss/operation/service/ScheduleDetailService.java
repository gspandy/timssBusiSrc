package com.timss.operation.service;

import java.util.HashMap;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.timss.operation.bean.PersonJobs;
import com.timss.operation.bean.ScheduleDetail;
import com.timss.operation.vo.CalendarVo;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.timss.operation.vo.EventsVo;
import com.timss.operation.vo.RulesHistoryVo;

/**
 * 
 * @title: 日历
 * @description: 排班日历
 * @company: gdyd
 * @className: ScheduleDetailService.java
 * @author: fengzt
 * @createDate: 2014年6月13日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface ScheduleDetailService {

	/**
	 * 根据id查询排班/运行日历项详情
	 * @param scheduleId
	 * @return
	 */
	ScheduleDetail queryDetailByScheduleId(Integer scheduleId)throws Exception;
	
    /**
     * 
     * @description:生成日历
     * @author: fengzt
     * @createDate: 2014年6月13日
     * @param rulesHistoryVo
     * @return:int
     * @throws Exception 
     */
    public int insertScheduleDetail(RulesHistoryVo rulesHistoryVo) throws Exception;

    /**
     * 
     * @description:值别日历
     * @author: fengzt
     * @createDate: 2014年6月17日
     * @param dutyId
     * @param stationId
     * @param start
     * @param end:
     * @return  List<EventsVo>
     */
    public List<EventsVo> getDutyCalendarByDutyId(int dutyId, String stationId, String start, String end);
    
    /**
     * 
     * @description:岗位日历
     * @author: fengzt
     * @createDate: 2014年6月17日
     * @param stationId
     * @param start
     * @param end:
     * @return  List<EventsVo>
     */
    public List<EventsVo> getDutyCalendarByStationId(String stationId, String start, String end);

    /**
     * 
     * @description:通过岗位、值别、日期获取当天排班日历
     * @author: huanglw
     * @createDate: 2014年6月24日
     * @param date(String 'yyyy-MM-dd'),stationId,dutyId
     * @return:
     */
    public List<CalendarVo> getCalendarByDSD(String date,String stationId,int dutyId);

    /**
     * 从班次列表中过滤出给定排班开始时间之后的班次
     * 用于交接班获得可用的下一班班次
     * @param cVos
     * @param startTime
     * @return
     */
    public List<CalendarVo>getAvailableCalendarByDS(List<CalendarVo>cVos,String startTimeStr);
    
    /**
     * 
     * @description:更新单条日历数据
     * @author: huanglw
     * @createDate: 2014年6月25日
     * @param CalendarVo 表示一条日历数据（由岗位、值别、日期唯一确定）
     * @return:成功更新的数量
     */
    public int updateCalendar(CalendarVo cVo);

    /**
     * 
     * @description:通过岗位、日期获取当天各值别的排班
     * @author: huanglw
     * @createDate: 2014年6月24日
     * @param 当天日期date 岗位IDstationId
     * @return:
     */
    public List<CalendarVo> getCalendarByDS(String date, String stationId);

    /**
     * 
     * @description:年视图
     * @author: fengzt
     * @createDate: 2014年7月1日
     * @param year
     * @param stationId
     * @param dutyId
     * @return:Map<String, Object>
     * @throws ParseException 
     */
    public Map<String, Object> queryYearCalendar(int year, String stationId) throws ParseException;

    /**
     * 
     * @description:检查是否有重复数据
     * @author: fengzt
     * @createDate: 2014年7月2日
     * @param rulesHistoryVo
     * @return:boolean
     */
    public boolean isExistScheduleDetail(RulesHistoryVo rulesHistoryVo);

    /**
     * 
     * @description:删除数据
     * @author: fengzt
     * @createDate: 2014年7月2日
     * @param rulesHistoryVo
     * @return:int
     */
    public int deleteScheduleDetail(RulesHistoryVo rulesHistoryVo);



    /**
     * 
     * @description:通过一个动态的HashMap更新岗位视图日历
     * @author: huanglw
     * @createDate: 2014年7月1日
     * @param dataMap，其中包含stationId,dataTime,岗位下各值别的班次（数量动态）
     * @return:更新数量
     */
    public int updateStationCalendar(HashMap<String, Object> dataMap);

    /**
     * @description:传入日历id获取下一班的日历
     * @author: huanglw
     * @createDate: 2014年7月22日
     * @param nextScheduleId
     * @return:CalendarVo
     */
    public CalendarVo getNextCalendarVoById(int scheduleId);
    
    /**
     * 查询当值值别的值班人员
     * @param scheduleId
     * @param isPresent 过滤是否出勤
     * @return
     */
    List<PersonJobs>querySchedulePersonByScheduleId(Integer scheduleId,String isPresent)throws Exception;
    
    /**
     * 从值别人员刷新排班的值班人员
     * @param siteId
     * @param startDate
     * @param endDate
     * @param isRebuild 是否重建，重建会先清数据，否则追加数据
     * @return
     * @throws Exception
     */
    Integer refreshDutyPersonSchedule(String siteId,Date startDate,Date endDate,Boolean isRebuild)throws Exception;
    
    /**
     * 更新当值值别实际值班人员列表<br/>
     * 比较值班人员列表，找出新加和删除的人；如果是考勤调班，需要找到被标记成缺勤的人<br/>
     * 删除该排班要删除的人；删除与该排班同一天的要增加的人的其他排班，给该排班添加要增加的人；更新该天增加的人和删除的人的打卡统计信息；如果是考勤调班，将缺勤的人置为出勤
     * @param updateType 指定更新的方式（schedule：排班调整，直接物理删除/attendance：考勤，设置是否出勤为否）
     * @param scheduleId
     * @param userIdList 新的值班人员列表
     * @return
     */
    Integer updateSchedulePerson(String updateType,Integer scheduleId,List<String>userIdList)throws Exception;
    
    /**
     * 修改值班人员是否出勤
     * @param scheduleId
     * @param userIds 为空修改整个排班的人
     * @param isPresent Y/N是否出勤
     * @return
     * @throws Exception
     */
    Integer updateSchedulePersonIsPresent(Integer scheduleId,String[]userIds,String isPresent)throws Exception;
    
    /**
     * 从排班表中获取符合条件的排班，从人员值别表中获取对应值别的人员，或从参数中获取人员，生成排班人员<br/>
     * @param scheduleIds 指定的排班
     * @param startDate 指定排班的开始时间
     * @param endDate 指定排班的结束时间
     * @param dutyIds 指定值别
     * @param stationId 指定工种
     * @param uuid 指定生成日历id
     * @param siteId 指定检索日历的站点id
     * @param userIds 指定的人员id，如果该参数为空，则从人员值别表中获取对应值别的人员
     * @return
     */
    Integer insertSchedulePersonFromScheduleDutyAndUserIds(Integer[]scheduleIds,
    		Date startDate,Date endDate,
    		Integer[]dutyIds,String stationId,String uuid,String siteId,
    		String[]userIds)throws Exception;
    
    /**
     * 从排班表中获取符合条件的排班，从参数中获取人员，删除该排班人员<br/>
     * 所有参数为空时，可删表，慎重！
     * @param scheduleIds 指定的排班
     * @param startDate 指定排班的开始时间
     * @param endDate 指定排班的结束时间
     * @param dutyIds 指定值别
     * @param stationId 指定工种
     * @param uuid 指定生成日历id
     * @param siteId 指定检索日历的站点id
     * @param userIds 指定的人员id
     * @return
     */
    Integer deleteSchedulePersonFromScheduleDutyAndUserIds(Integer[]scheduleIds,
    		Date startDate,Date endDate,
    		Integer[]dutyIds,String stationId,String uuid,String siteId,
    		String[]userIds)throws Exception;
    
    /**
     * 查询指定日期指定站点人员的排班情况
     * @param siteId 为空则查询所有站点
     * @param userIds 
     * @param startDateStr yyyy-MM-dd的日期或以此开头的时间（会截取日期部分）为空则开始时间无限制
     * @param endDateStr yyyy-MM-dd的日期或以此开头的时间（会截取日期部分）为空则结束时间无限制
     * @return 以日期_用户id为key的map
     * @throws Exception
     */
    Map<String, DutyPersonShiftVo>querySchedulePersonAndShiftBySiteAndTime(String siteId,String[] userIds,String startDateStr,String endDateStr)throws Exception;
    
}
