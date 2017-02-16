package com.timss.operation.service;

import java.util.Date;
import java.util.List;

import com.timss.operation.bean.ScheduleTask;
import com.timss.operation.vo.CalendarVo;
import com.yudean.itc.dto.Page;

public interface ScheduleTaskService {

    /**
     * @description:查询列表
     * @author: 王中华
     * @createDate: 2016-11-30
     * @param page
     * @return:
     */
    Page<ScheduleTask> queryScheduleTaskList(Page<ScheduleTask> page);
    
    /**
     * @description:待执行工作列表
     * @author: 王中华
     * @createDate: 2017-2-8
     * @param page
     * @return:
     */
    Page<ScheduleTask> querytodoScheduleTaskList(Page<ScheduleTask> page);
    /**
     * @description:定期任务计划ID，查询曾经生成的任务
     * @author: 王中华
     * @createDate: 2016-12-11
     * @param id
     * @return:
     */
    List<ScheduleTask> queryScheduleTaskListByTaskPlanId(String taskPlanId);
    
   
    /**
     * @description:根据班次查询是否有未完成的任务
     * @author: 王中华
     * @createDate: 2016-12-12
     * @param shiftId
     * @param shiftDate
     * @return:
     */
    List<ScheduleTask> queryUndoScheduleTaskListByShift(String shiftId, Date shiftDate);
    
    /**
     * @description:查询详情
     * @author: 王中华
     * @createDate: 2016-11-30
     * @param id
     * @return:
     */
    ScheduleTask queryTaskById(String id);

   
    
    /**
     * @description:修改
     * @author: 王中华
     * @createDate: 2016-12-1 
     * @param scheduleTaskPlan
     * @return:
     */
    int updateScheduleTask(ScheduleTask scheduleTask);

    /**
     * @description:插入
     * @author: 王中华
     * @createDate: 2016-12-5
     * @param task:
     */
    String insertScheduleTask(ScheduleTask task);

    /**
     * @description:根据工种，时间查询排班日历
     * @author: 王中华
     * @createDate: 2016-12-6
     * @param stationId
     * @param shiftDate
     * @return:
     */
    List<CalendarVo> queryShiftByStationIdAndDate(String stationId, Date shiftDate);

    /**
     * @description:定期任务流转班次 
     * @author: 王中华
     * @createDate: 2016-12-7
     * @param id
     * @param shiftInfo
     * @return:
     */
    ScheduleTask updateSchedTaskChangeShift(String id, String shiftInfo);
    

   

    
    
}
