package com.timss.operation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.ScheduleTaskPlan;
import com.yudean.itc.dto.Page;

public interface ScheduleTaskPlanDao { 

    /**
     * @description:查询列表
     * @author: 王中华
     * @createDate: 2016-11-30
     * @param page
     * @return:
     */
    List<ScheduleTaskPlan> queryScheduleTaskPlanList(Page<ScheduleTaskPlan> page);

    /**
     * @description:查询详情
     * @author: 王中华
     * @createDate: 2016-11-30
     * @param id
     * @return:
     */
    ScheduleTaskPlan queryTaskPlanById(String id);

    /**
     * @description:插入
     * @author: 王中华
     * @createDate: 2016-12-1
     * @param scheduleTaskPlan:
     */
    void insertScheduleTaskPlan(ScheduleTaskPlan scheduleTaskPlan);

    /**
     * @description:更新
     * @author: 王中华
     * @createDate: 2016-12-1
     * @param scheduleTaskPlan
     * @return:
     */
    int updateScheduleTaskPlan(ScheduleTaskPlan scheduleTaskPlan);
    
    /**
     * @description:删除
     * @author: 王中华
     * @createDate: 2016-12-2
     * @param id
     * @return:
     */
    int deleteScheduleTaskPlan(@Param("id")String id);

    /**
     * @description:根据规则查计划
     * @author: 王中华
     * @createDate: 2016-12-5
     * @param taskRuleId
     * @return:
     */
    ScheduleTaskPlan queryTaskPlanByRuleId(@Param("taskRuleId")String taskRuleId);

}
