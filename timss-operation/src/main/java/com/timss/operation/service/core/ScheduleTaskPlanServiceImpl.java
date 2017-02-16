package com.timss.operation.service.core;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.ScheduleTaskPlan;
import com.timss.operation.dao.ScheduleTaskPlanDao;
import com.timss.operation.service.ScheduleTaskPlanService;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.util.ReflectionUtils;
import com.yudean.itc.bean.ScheduledTaskRule;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.support.IBusinessMessageManager;
import com.yudean.itc.manager.support.IHistoryTaskManager;
import com.yudean.itc.manager.support.IScheduledTaskRuleManager;
@Service
public class ScheduleTaskPlanServiceImpl implements ScheduleTaskPlanService {

    @Autowired
    ScheduleTaskPlanDao taskPlanDao;
    @Autowired
    IScheduledTaskRuleManager scheduleTaskRuleService;
    @Autowired
    IHistoryTaskManager historyTaskManager;
    @Autowired
    IBusinessMessageManager messageService;
    @Override
    public Page<ScheduleTaskPlan> queryScheduleTaskPlanList(Page<ScheduleTaskPlan> page) {
        List<ScheduleTaskPlan> taskPlanList = taskPlanDao.queryScheduleTaskPlanList(page);
        page.setResults( taskPlanList );
        return page;
    }
    
    @Override
    public ScheduleTaskPlan queryTaskPlanById(String id) {
        return taskPlanDao.queryTaskPlanById(id);
    }
    
    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public String insertScheduleTaskPlan(ScheduleTaskPlan scheduleTaskPlan) {
        ScheduledTaskRule taskRule = new ScheduledTaskRule();
        ReflectionUtils.setCommonFieldValue( scheduleTaskPlan, taskRule, "id,serialVersionUID" );
        taskRule.setName( scheduleTaskPlan.getContent() );
        taskRule.setBusinessInterface( "GenerateScheduleTaskServiceImpl" );
        taskRule.setDelFlag( "N" );
        //插入组件表
        scheduleTaskRuleService.insertScheduledTaskRule( taskRule );
        //插入业务表
        scheduleTaskPlan.setComponentId( taskRule.getId() );
        if(StringUtils.isBlank( scheduleTaskPlan.getId() )){
            scheduleTaskPlan.setId( null );
        }
        taskPlanDao.insertScheduleTaskPlan(scheduleTaskPlan);
        // 第一次生成待执行任务
//        if("Y".equals( taskRule.getActivityFlag() )){
//           // TODO 获取第一条待执行任务的开始干活时间
////            Date firstTodoTime = getNextTodoTime(taskRule);
////            scheduleTaskRuleService.generateTodoTask( taskRule );
//        }
        
        
        return scheduleTaskPlan.getId();
    }
    
   
    

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public int updateScheduleTaskPlan(ScheduleTaskPlan scheduleTaskPlan) {
        //查找组件id
        ScheduleTaskPlan taskPlan = taskPlanDao.queryTaskPlanById( scheduleTaskPlan.getId() );
        //修改组件
        String componentId = taskPlan.getComponentId();
        ScheduledTaskRule rule = new ScheduledTaskRule();
        rule.setId( componentId );
        rule.setName( scheduleTaskPlan.getContent() );
        rule.setActivityFlag( scheduleTaskPlan.getActivityFlag() );
        rule.setCycleType( scheduleTaskPlan.getCycleType() );
        rule.setCycleLen( scheduleTaskPlan.getCycleLen() );
        rule.setBeginTime( scheduleTaskPlan.getBeginTime() );
        rule.setInvalideTime( scheduleTaskPlan.getInvalideTime() );
        rule.setModifydate( new Date() );
        rule.setModifyuser( scheduleTaskPlan.getModifyuser() );
        scheduleTaskRuleService.updateScheduledTaskRule( rule );
        //修改定期任务计划
        int updatenum = taskPlanDao.updateScheduleTaskPlan(scheduleTaskPlan);
        
        return updatenum;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public int deleteScheduleTaskPlan(String id) {
        //查询
        ScheduleTaskPlan taskPlan = taskPlanDao.queryTaskPlanById( id );
        //删除组件表中的规则
        String componentId = taskPlan.getComponentId();
        scheduleTaskRuleService.deleteScheduledTaskRule( componentId );
        //删除定期任务计划
        int deleteNum = taskPlanDao.deleteScheduleTaskPlan( id );
        
        return deleteNum;
    }

    @Override
    public ScheduleTaskPlan queryTaskPlanByRuleId(String taskRuleId) {
        ScheduleTaskPlan resultPlan = taskPlanDao.queryTaskPlanByRuleId( taskRuleId );
        return resultPlan;
    }

}
