package com.timss.operation.bean;

import java.util.Date;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ScheduleTaskChangeShift extends ItcMvcBean{

    private static final long serialVersionUID = 5651939994627149718L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL) // 自动生成id
    private String id;
    /**
     * 流转前班次Id
     */
    private String beforeShiftId;
    /**
     * 流转前班次日期
     */
    private Date beforeShiftDate;
    /**
     * 流转前班次例如“2017-02-07 夜班(丙值)”
     */
    private String beforeShift;
    
    /**
     * 流转后班次Id
     */
    private String afterShiftId;
    
    /**
     * 流转后班次日期
     */
    private Date afterShiftDate;
    /**
     * 流转后班次
     */
    private String afterShift;
    /**
     * 原因及备注
     */
    private String remarks ;
    /**
     * 定期工作id
     */
    private String taskId ;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getBeforeShiftDate() {
        return beforeShiftDate;
    }
    public void setBeforeShiftDate(Date beforeShiftDate) {
        this.beforeShiftDate = beforeShiftDate;
    }
    public String getBeforeShiftId() {
        return beforeShiftId;
    }
    public void setBeforeShiftId(String beforeShiftId) {
        this.beforeShiftId = beforeShiftId;
    }
    public Date getAfterShiftDate() {
        return afterShiftDate;
    }
    public void setAfterShiftDate(Date afterShiftDate) {
        this.afterShiftDate = afterShiftDate;
    }
    public String getAfterShiftId() {
        return afterShiftId;
    }
    public void setAfterShiftId(String afterShiftId) {
        this.afterShiftId = afterShiftId;
    }
    public String getBeforeShift() {
        return beforeShift;
    }
    public void setBeforeShift(String beforeShift) {
        this.beforeShift = beforeShift;
    }
    public String getAfterShift() {
        return afterShift;
    }
    public void setAfterShift(String afterShift) {
        this.afterShift = afterShift;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    
}
