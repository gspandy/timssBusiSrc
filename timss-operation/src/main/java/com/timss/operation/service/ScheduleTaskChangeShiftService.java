package com.timss.operation.service;

import java.util.List;

import com.timss.operation.bean.ScheduleTaskChangeShift;

public interface ScheduleTaskChangeShiftService {

    /**
     * @description:查询列表
     * @author: 王中华
     * @createDate: 2016-11-30
     * @param page
     * @return:
     */
    List<ScheduleTaskChangeShift> querySchedTaskChangeShiftList(String taskId);

   
    /**
     * @description:插入
     * @author: 王中华
     * @createDate: 2016-12-5
     * @param task:
     */
    int insertSchedTaskChangeShift(ScheduleTaskChangeShift taskChangeShift);

    
}
