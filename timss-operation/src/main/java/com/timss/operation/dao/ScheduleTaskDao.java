package com.timss.operation.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.ScheduleTask;
import com.yudean.itc.dto.Page;

public interface ScheduleTaskDao {

    List<ScheduleTask> queryScheduleTaskList(Page<ScheduleTask> page);
    
    List<ScheduleTask> querytodoScheduleTaskList(Page<ScheduleTask> page);

    ScheduleTask queryTaskById(@Param("id")String id);

    int updateScheduleTask(ScheduleTask scheduleTask);

    void insertScheduleTask(ScheduleTask scheduleTask);

    List<ScheduleTask> queryTaskListByTaskPlanId(@Param("taskPlanId")String taskPlanId);

    List<ScheduleTask> queryUndoScheduleTaskListByShift(@Param("shiftId")String shiftId ,@Param("shiftDate")Date shiftDate);

   
    
}
