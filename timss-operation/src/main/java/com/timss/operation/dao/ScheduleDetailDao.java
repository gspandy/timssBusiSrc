package com.timss.operation.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.PersonJobs;
import com.timss.operation.bean.RulesHistory;
import com.timss.operation.bean.ScheduleDetail;
import com.timss.operation.vo.CalendarPageVo;
import com.timss.operation.vo.CalendarVo;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 日历DAO
 * @description: {desc}
 * @company: gdyd
 * @className: ScheduleDetailMapper.java
 * @author: fengzt
 * @createDate: 2014年6月16日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface ScheduleDetailDao {

	/**
	 * 根据id查询排班/运行日历项详情
	 * @param scheduleId
	 * @return
	 */
	ScheduleDetail queryDetailByScheduleId(@Param("scheduleId")Integer scheduleId);
	
    /**
     * 
     * @description:通过uuid 拿到datagrid行列
     * @author: fengzt
     * @createDate: 2014年6月16日
     * @param uuid
     * @return:List<CalendarVo>
     */
    public List<CalendarVo> getRuleDatagridByUuid(String uuid);

    /**
     * 
     * @description:列数有多少个值别
     * @author: fengzt
     * @createDate: 2014年6月16日
     * @param uuid
     * @return:int
     */
    public int getDutyColumnsByUuid(String uuid);

    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2014年6月16日
     * @param map
     * @return:int
     */
    public int batchInsert(List<CalendarVo> list );

    /**
     * 
     * @description:保存日历
     * @author: fengzt
     * @createDate: 2014年6月16日
     * @param calendarVo
     * @return:int
     */
    public int insertScheduleDetail(CalendarVo calendarVo );

    /**
     * 
     * @description:通过岗位、值别、日期获取当天排班日历
     * @author: huanglw
     * @createDate: 2014年6月24日
     * @param CalendarPageVo
     * @return:
     */
    public List<CalendarVo> getCalendarByDSD(CalendarPageVo vo);
    
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
     * @description:通过CalendarVo的list，批量更新日历数据
     * @author: huanglw
     * @createDate: 2014年7月1日
     * @param voList
     * @return:更新的数量
     */
    public int updateStationCalendar(List<CalendarVo> voList);



    /**
     * 
     * @description: 是否用相同的datagrid生成整年
     * @author: fengzt
     * @createDate: 2014年7月1日
     * @param map
     * @return:List<CalendarVo> 如果只用一条规则，返回只有一条数据size = 1
     */
    public List<CalendarVo> queryYearCalendarRules(HashMap<String, Object> map);

    /**
     * 
     * @description:拿到年视图第一天数据
     * @author: fengzt
     * @createDate: 2014年7月1日
     * @param pageVo
     * @return:CalendarVo
     */
    public List<CalendarVo> queryFristCalendar(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:拿到年视图最后一天数据
     * @author: fengzt
     * @createDate: 2014年7月1日
     * @param pageVo1
     * @return:CalendarVo
     */
    public List<CalendarVo> queryLastCalendar(Page<HashMap<?, ?>> pageVo1);

    /**
     * 
     * @description:查找by  stationid dateTime
     * @author: fengzt
     * @createDate: 2014年7月2日
     * @param rulesHistory
     * @return:List<CalendarVo>
     */
    public List<CalendarVo> queryScheduleDetailByRulesHistory(RulesHistory rulesHistory);

    /**
     * 
     * @description:删除by  stationid dateTime（区间）
     * @author: fengzt
     * @createDate: 2014年7月2日
     * @param rulesHistory
     * @return:int
     */
    public int deleteScheduleDetail(RulesHistory rulesHistory);

    /**
     * @description:传入日历id获取同一天的日历
     * @author: huanglw
     * @createDate: 2014年7月22日
     * @param nextScheduleId
     * @return:List<CalendarVo>
     */
    public List<CalendarVo> getSameDayCalendarVoById(int nextScheduleId);
    
    /**
     * 查询当值值别的值班人员
     * @param scheduleId
     * @param isPresent 过滤是否出勤
     * @return
     */
    List<PersonJobs>querySchedulePersonByScheduleId(@Param("scheduleId")Integer scheduleId,@Param("isPresent")String isPresent);
    
    /**
     * 从排班表中获取符合条件的排班，从人员值别表中获取对应值别的人员，或从参数中获取人员，生成排班人员<br/>
     * 用于从排班规则生成排班日历时，生成排班人员信息
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
    Integer insertSchedulePersonFromScheduleDutyAndUserIds(@Param("scheduleIds")Integer[]scheduleIds,
    		@Param("startDate")Date startDate,@Param("endDate")Date endDate,
    		@Param("dutyIds")Integer[]dutyIds,@Param("stationId")String stationId,@Param("uuid")String uuid,@Param("siteId")String siteId,
    		@Param("userIds")String[]userIds);

    /**
     * 修改值班人员是否出勤
     * @param scheduleId
     * @param userIds 为空修改整个排班的人
     * @param isPresent Y/N是否出勤
     * @return
     * @throws Exception
     */
    Integer updateSchedulePersonIsPresent(@Param("scheduleId")Integer scheduleId,@Param("userIds")String[]userIds,@Param("isPresent")String isPresent)throws Exception;
    
    /**
     * 从排班表中获取符合条件的排班，从参数中获取人员，删除该排班人员<br/>
     * 用于对值别人员调整时，删除排班人员信息
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
    Integer deleteSchedulePersonFromScheduleDutyAndUserIds(@Param("scheduleIds")Integer[]scheduleIds,
    		@Param("startDate")Date startDate,@Param("endDate")Date endDate,
    		@Param("dutyIds")Integer[]dutyIds,@Param("stationId")String stationId,@Param("uuid")String uuid,@Param("siteId")String siteId,
    		@Param("userIds")String[]userIds);
    
    /**
     * 查询指定站点指定时间内人员的排班情况
     * @param siteId 为空则查询所有站点
     * @param userIds
     * @param startDate 为空则开始时间无限制
     * @param endDate 为空则结束时间无限制
     * @return 以日期_用户id为key的map
     */
    @MapKey("flag")
    Map<String, DutyPersonShiftVo>querySchedulePersonAndShiftBySiteAndTime(@Param("siteId")String siteId,@Param("userIds")String[] userIds,
    		@Param("startDate")String startDate,@Param("endDate")String endDate);
}
