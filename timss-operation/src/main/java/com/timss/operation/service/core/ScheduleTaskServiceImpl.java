package com.timss.operation.service.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.ScheduleTask;
import com.timss.operation.bean.ScheduleTaskChangeShift;
import com.timss.operation.dao.HandoverDao;
import com.timss.operation.dao.ScheduleDetailDao;
import com.timss.operation.dao.ScheduleTaskDao;
import com.timss.operation.service.ScheduleTaskChangeShiftService;
import com.timss.operation.service.ScheduleTaskService;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.vo.CalendarPageVo;
import com.timss.operation.vo.CalendarVo;
import com.timss.operation.vo.HandoverVo;

import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    ScheduleTaskDao taskDao;
    @Autowired
    private HandoverDao handoverDao;
    @Autowired
    private ScheduleDetailDao scheduleDetailDao;
    @Autowired
    private ScheduleTaskChangeShiftService taskChangeShiftService;
    
    @Override
    public Page<ScheduleTask> queryScheduleTaskList(Page<ScheduleTask> page) {
        List<ScheduleTask> list = taskDao.queryScheduleTaskList(page);
        page.setResults( list );
        return page;
    }

    @Override
    public Page<ScheduleTask> querytodoScheduleTaskList(Page<ScheduleTask> page) {
        List<ScheduleTask> list = taskDao.querytodoScheduleTaskList(page);
        page.setResults( list );
        return page;
    }
    
    @Override
    public ScheduleTask queryTaskById(String id) {
        return taskDao.queryTaskById( id );
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public int updateScheduleTask(ScheduleTask scheduleTask) {
        return taskDao.updateScheduleTask(scheduleTask) ;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public String insertScheduleTask(ScheduleTask scheduleTask) {
        //插入定期工作
        taskDao.insertScheduleTask(scheduleTask) ;
        
        // 插入定期工作的流转记录，一条没有AFTER_SHIFT值的流转记录
        ScheduleTaskChangeShift taskChangeShift = new ScheduleTaskChangeShift();
        StringBuffer beforeShift = new StringBuffer();
        String dateformatStr = DateFormatUtil.formatDate( scheduleTask.getShiftDate(), "yyyy-MM-dd" );
        beforeShift.append( dateformatStr );  //日期
        beforeShift.append( scheduleTask.getShiftName() );  //班次
        beforeShift.append( "(" );
        beforeShift.append( scheduleTask.getDutyName() );  //值别
        beforeShift.append( ")" );
        taskChangeShift.setBeforeShift( beforeShift.toString() );
        // 第一次生成待执行任务，插入流转记录时，流转前班次/流转后班次都设置为当前班次
        taskChangeShift.setBeforeShiftId( String.valueOf( scheduleTask.getShiftId() ) ); //流转前班次ID
        taskChangeShift.setAfterShiftId( String.valueOf( scheduleTask.getShiftId() ) );
        taskChangeShift.setTaskId( scheduleTask.getId() );
        taskChangeShift.setCreatedate( scheduleTask.getCreatedate() );
        taskChangeShift.setCreateuser( scheduleTask.getCreateuser() );
        taskChangeShift.setBeforeShiftDate( scheduleTask.getShiftDate() );
        taskChangeShift.setAfterShiftDate( scheduleTask.getShiftDate() );
        
        taskChangeShiftService.insertSchedTaskChangeShift( taskChangeShift );
       
        return scheduleTask.getId();
    }

    @Override
    public List<CalendarVo> queryShiftByStationIdAndDate(String stationId, Date shiftDate) {
        //获取此工种的所有班次信息
        CalendarPageVo vo = new CalendarPageVo();
        vo.setStationId( stationId );
        vo.setStartDate( shiftDate );
        vo.setEndDate( shiftDate );
        List<CalendarVo> CalendarVoList = scheduleDetailDao.getCalendarByDSD(vo);
        //此日期里面的交接班记录
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "stationId", stationId );
        map.put( "dateYMD", shiftDate );
        map.put( "isOver", 'Y' );
//        map.put( "shiftId", 0 ); //因为SQL里面是用0来过滤的
        List<HandoverVo> handoverVoList = handoverDao.queryHandoverBySDS(map);
        //过滤掉此日期里面已经交接班了的班次
        List<CalendarVo> resultList = new ArrayList<CalendarVo>();
        resultList.addAll( CalendarVoList );
        
        for ( CalendarVo calendarVo : CalendarVoList ) {
            for ( HandoverVo handoverVo : handoverVoList ) {
                if(calendarVo.getShiftId() == handoverVo.getCurrentShiftId()){
                    resultList.remove( calendarVo );
                }
            }
        }
        
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public ScheduleTask updateSchedTaskChangeShift(String id, String shiftInfo) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
       
        JSONObject jsonObject=JSONObject.fromObject(shiftInfo);
        String dutyName = (String) jsonObject.get("dutyName");  //值别
        String dutyId = (String) jsonObject.get("dutyId");  //值别
        Date afterShiftDate = new Date((Long) jsonObject.get("shiftDate"));  //日期
        String afterShiftId = (String) jsonObject.get("shiftId");  //班次
        String shiftName = (String) jsonObject.get("shiftName");   //班次
        String remarks = (String) jsonObject.get("remarks");   //备注及原因
        
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        ScheduleTask task = queryTaskById( id );
        Date beforeShiftDate = task.getShiftDate();
        String beforeShift =task.getShiftName();
        String afterShift = sdf.format(afterShiftDate)+shiftName+"("+dutyName+")";
        //修改定期任务主表里的班次信息
        task.setShiftDate( afterShiftDate );
        task.setShiftId( Integer.valueOf( afterShiftId ) );
        task.setShiftName( afterShift );
        task.setDutyId( dutyId );
        task.setDutyName( dutyName );
        updateScheduleTask(task);
        //插入流转记录
        ScheduleTaskChangeShift taskChangeShift = new ScheduleTaskChangeShift();
        taskChangeShift.setBeforeShiftId( String.valueOf( task.getShiftId()  )); // 流转前的班次id
        taskChangeShift.setAfterShiftId( afterShiftId );  // 流转后的班次id
        taskChangeShift.setBeforeShiftDate( beforeShiftDate ); //流转前班次日期
        taskChangeShift.setAfterShiftDate( afterShiftDate );  //流转后班次日期
        taskChangeShift.setBeforeShift( beforeShift );
        taskChangeShift.setAfterShift( afterShift );
        taskChangeShift.setRemarks( remarks );
        taskChangeShift.setTaskId( id );
        taskChangeShift.setCreatedate( new Date() );
        taskChangeShift.setCreateuser( userInfoScope.getUserId() );
        taskChangeShiftService.insertSchedTaskChangeShift( taskChangeShift );
        
        return task;
    }

    @Override
    public List<ScheduleTask> queryScheduleTaskListByTaskPlanId(String taskPlanId) {
        return taskDao.queryTaskListByTaskPlanId(taskPlanId);
    }

    @Override
    public List<ScheduleTask> queryUndoScheduleTaskListByShift(String shiftId, Date nowShiftDate) {
        return taskDao.queryUndoScheduleTaskListByShift(shiftId , nowShiftDate);
    }

   
   
}
