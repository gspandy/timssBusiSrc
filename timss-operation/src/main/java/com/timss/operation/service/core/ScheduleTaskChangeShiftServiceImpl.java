package com.timss.operation.service.core;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.operation.bean.ScheduleTaskChangeShift;
import com.timss.operation.dao.SchedTaskChangeShiftDao;
import com.timss.operation.service.ScheduleTaskChangeShiftService;
@Service
public class ScheduleTaskChangeShiftServiceImpl implements ScheduleTaskChangeShiftService {

    @Autowired
    SchedTaskChangeShiftDao taskChangeShiftDao;
    @Override
    public List<ScheduleTaskChangeShift> querySchedTaskChangeShiftList(String taskId) {
        List<ScheduleTaskChangeShift> result = taskChangeShiftDao.querySchedTaskChangeShiftList( taskId );
        return result;
    }

    @Override
    public int insertSchedTaskChangeShift(ScheduleTaskChangeShift taskChangeShift) {
        String taskId = taskChangeShift.getTaskId();
        String afterShiftId = taskChangeShift.getAfterShiftId();
        Date afterShiftDate = taskChangeShift.getAfterShiftDate();
        ScheduleTaskChangeShift lastTaskChangeShift = taskChangeShiftDao.queryLastSchedTaskChangeShift( taskId );
        if(lastTaskChangeShift != null){
            lastTaskChangeShift.setCreatedate( taskChangeShift.getCreatedate() );
            lastTaskChangeShift.setCreateuser( taskChangeShift.getCreateuser() );
            lastTaskChangeShift.setBeforeShift( taskChangeShift.getAfterShift() );
            lastTaskChangeShift.setBeforeShiftId( afterShiftId );
            lastTaskChangeShift.setBeforeShiftDate( afterShiftDate );
            taskChangeShiftDao.updateSchedTaskChangeShift(lastTaskChangeShift);
        }
        
        return taskChangeShiftDao.insertSchedTaskChangeShift( taskChangeShift );
    }

}
