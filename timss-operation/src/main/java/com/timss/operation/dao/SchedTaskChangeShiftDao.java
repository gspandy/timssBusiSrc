package com.timss.operation.dao;

import java.util.List;

import com.timss.operation.bean.ScheduleTaskChangeShift;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: SchedTaskChangeShiftDao.java
 * @author: 王中华
 * @createDate: 2016-12-7
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface SchedTaskChangeShiftDao {

    /**
     * @description:查询
     * @author: 王中华
     * @createDate: 2016-12-7
     * @param taskId
     * @return:
     */
    List<ScheduleTaskChangeShift> querySchedTaskChangeShiftList(String taskId);

    /**
     * @description:插入
     * @author: 王中华
     * @createDate: 2016-12-7
     * @param scheduleTaskChangeShift
     * @return:
     */
    int insertSchedTaskChangeShift(ScheduleTaskChangeShift scheduleTaskChangeShift);

    /**
     * @description:查询afterShift为空的记录
     * @author: 王中华
     * @createDate: 2017-2-7
     * @param taskId
     * @return:
     */
    ScheduleTaskChangeShift queryLastSchedTaskChangeShift(String taskId);

    /**
     * @description:修改afterShift为空的记录
     * @author: 王中华
     * @createDate: 2017-2-7
     * @param lastTaskChangeShift:
     */
    void updateSchedTaskChangeShift(ScheduleTaskChangeShift lastTaskChangeShift);
    
}
