package com.timss.operation.service.core;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.operation.bean.ScheduleTask;
import com.timss.operation.bean.ScheduleTaskPlan;
import com.timss.operation.dao.ScheduleDetailDao;
import com.timss.operation.dao.ShiftDao;
import com.timss.operation.service.ScheduleTaskPlanService;
import com.timss.operation.service.ScheduleTaskService;
import com.timss.operation.util.ScheduleTaskDoStatus;
import com.timss.operation.vo.CalendarPageVo;
import com.timss.operation.vo.CalendarVo;
import com.yudean.itc.bean.CreateScheduledTaskResult;
import com.yudean.itc.bean.ScheduledTaskRule;
import com.yudean.itc.manager.support.ICreateToDoTaskManager;
@Service("GenerateScheduleTaskServiceImpl")
public class GenerateScheduleTaskServiceImpl implements ICreateToDoTaskManager {

    @Autowired
    private ScheduleTaskPlanService taskPlanService;
    @Autowired
    private ScheduleTaskService taskService;
    @Autowired
    private ScheduleDetailDao scheduleDetailDao;
    @Autowired
    private ShiftDao shiftDao;
    
    @Override
    public CreateScheduledTaskResult createTaskByRule(ScheduledTaskRule scheduledTaskRule,Date thisCycleTodoTime) {
        CreateScheduledTaskResult result = new CreateScheduledTaskResult();
        String taskRuleId = scheduledTaskRule.getId();
        String cycleType = scheduledTaskRule.getCycleType();
        //查询此规则对应的定期任务计划
        ScheduleTaskPlan taskPlan = taskPlanService.queryTaskPlanByRuleId( taskRuleId );
        
        String shiftIds = taskPlan.getShiftIds() ;
        String[] shiftIdArr = shiftIds.split( "," ); 
        int createRecordsNum = 0 ;
        StringBuffer businessIds = new StringBuffer();
        //给每个班次生成待执行任务
        for ( String shiftId : shiftIdArr ) {
            ScheduleTask task = new ScheduleTask();
            task.setTaskPlanId( taskPlan.getId() );
            task.setContent( taskPlan.getContent() );
            task.setAssetId( taskPlan.getAssetId() );
            if( taskPlan.getAssetId() != null){
                task.setAssetName( taskPlan.getAssetName() );
            }
            task.setDoStatus( ScheduleTaskDoStatus.UNDO.getEnName() );  //未执行
            task.setSiteid( taskPlan.getSiteid() );
            task.setType( taskPlan.getType() );
            task.setDeptId( taskPlan.getDeptId() );
            task.setShiftId( Integer.valueOf( shiftId ) );
            String shiftName = shiftDao.queryShiftById( Integer.valueOf( shiftId ) ).getName();
            task.setShiftName( shiftName );
            task.setCreatedate( new Date() );
            if(!"SD_M".equals( cycleType )){  //周期类型 不是 每月指定日期
                // 根据日期，工种，班次，查询对应的值别
                CalendarVo caluendarVo = getDutyInfo(task,thisCycleTodoTime);
                if(caluendarVo != null){
                    task.setDutyId( String.valueOf( caluendarVo.getDutyId() ) );
                    task.setDutyName( caluendarVo.getDutyName() );
                    taskService.insertScheduleTask(task);
                    createRecordsNum = createRecordsNum + 1;
                    businessIds.append( task.getId() );
                    businessIds.append( "," );
                }
            }else{//周期类型 是 每月指定日期
               String[] specifiedDateStrings = taskPlan.getSpecifiedDate().split( "," ); 
               for ( String specifiedDate : specifiedDateStrings ) {  //遍历选定日期
                   int specifiedDateInt = Integer.valueOf( specifiedDate );
                   Calendar thisCycleCalendar = Calendar.getInstance();
                   thisCycleCalendar.setTime( thisCycleTodoTime );
                   // 本次月份最大一天， 考虑30日的情况，如果没有则不生成
                   int biggestDayOfThisCycle = thisCycleCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                   if(specifiedDateInt > biggestDayOfThisCycle){  //如果指定日期大于最大日期，则不生成，直接跳过
                       continue ;
                   }
                   thisCycleCalendar.set( Calendar.DAY_OF_MONTH, specifiedDateInt );
                   Date tempTodoTimeDate = thisCycleCalendar.getTime();//此条待执行任务的开始时间
                   if(tempTodoTimeDate.before( new Date() )){  //如果执行时间已过了
                       continue ;
                   }else{  //生产待执行任务
                       // 根据日期，工种，班次，查询对应的值别
                       CalendarVo caluendarVo = getDutyInfo(task,tempTodoTimeDate);
                       if(caluendarVo != null){
                           task.setDutyId( String.valueOf( caluendarVo.getDutyId() ) );
                           task.setDutyName( caluendarVo.getDutyName() );
                           taskService.insertScheduleTask(task);
                           businessIds.append( task.getId() );
                           businessIds.append( "," );
                           createRecordsNum = createRecordsNum + 1;
                           task.setId( null );  //在此成循环中，上一次插入的id，code会保留下来，所以要清空
                           task.setCode( null );
                           task.setDutyId( null ); 
                           task.setDutyName( null );
                           
                          
                       }
                   }
                   
               }
            }
           
        }
        if(businessIds.length()==0){
            businessIds.append( "" ) ;
        }else{
            businessIds.deleteCharAt( businessIds.length()-1 );
        }
        
        result.setBusinessId( businessIds.toString() );  //生成记录的id记录，逗号分隔
        result.setNoticeType( "NO" );//不发送消息
        result.setCreateRecordsNum( createRecordsNum ); //创建的待执行任务记录数量
        
        return result;
    }

    private CalendarVo getDutyInfo(ScheduleTask task,Date thisTaskTodoTime){
        CalendarVo resultCalendarVo = null;
        CalendarPageVo vo = new CalendarPageVo();
        vo.setShiftId( Integer.valueOf( task.getShiftId() ) );
        vo.setStationId( task.getDeptId() );
        
        Calendar cal = Calendar.getInstance();
        cal.setTime( thisTaskTodoTime );
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR,0);
        Date thatDay = cal.getTime();  //待执行任务，该执行的那天日期
        //设置班次日期
        task.setShiftDate( thatDay );        
        vo.setStartDate( thatDay );
        vo.setEndDate( thatDay );
        
        List<CalendarVo> resultList = scheduleDetailDao.getCalendarByDSD(vo);
        if(resultList.size()>0){
            resultCalendarVo = resultList.get( 0 );
        }
        return resultCalendarVo;
    }
}
