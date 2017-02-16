package com.timss.operation.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ScheduleTask extends ItcMvcBean{

    private static final long serialVersionUID = 7042250693905847806L;
    
    /**
     * 主键id
     */
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL) // 自动生成id
    private String id;
    
    /**
     * 编号
     */
    @AutoGen(value = "OPR_DQ_TASK", requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW)
    private String code;
    /**
     * 定期工作计划id
     */
    private String taskPlanId;
    
    /**
     * 类型
     */
    private String type;
    /**
     * 工作内容
     */
    private String content;
    /**
     * 关联设备id
     */
    private String assetId;
    /**
     * 关联设备名
     */
    private String assetName;
    /**
     * 工种
     */
    private String deptId;
    private String deptName;
    /**
     * 负责班次
     */
    private int shiftId;
    /**
     * 负责班次
     */
    private String shiftName;
    /**
     * 班次日期
     */
    private Date shiftDate;
    /**
     * 值别
     */
    private String dutyName;
    /**
     * 值别
     */
    private String dutyId;
    /**
     * 执行状态
     */
    private String doStatus;
    /**
     * 执行结果
     */
    private String doResult;
    /**
     * 执行时间
     */
    private Date doTime;
    /**
     * 执行人
     */
    private String doUserNames;
    /**
     * 执行人
     */
    private String doUserIds;
    /**
     * 记录人
     */
    private String recorder;
    /**
     * 记录人
     */
    private String recorderName;
    /**
     * 记录时间
     */
    private Date recordeTime;
    /**
     * 情况即备注
     */
    private String remarks;
    
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAssetId() {
        return assetId;
    }
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
    public String getAssetName() {
        return assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
    public int getShiftId() {
        return shiftId;
    }
    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }
    public String getDoStatus() {
        return doStatus;
    }
    public void setDoStatus(String doStatus) {
        this.doStatus = doStatus;
    }
    public String getDoResult() {
        return doResult;
    }
    public void setDoResult(String doResult) {
        this.doResult = doResult;
    }
    public Date getDoTime() {
        return doTime;
    }
    public void setDoTime(Date doTime) {
        this.doTime = doTime;
    }
    public String getDoUserNames() {
        return doUserNames;
    }
    public void setDoUserNames(String doUserNames) {
        this.doUserNames = doUserNames;
    }
    public String getDoUserIds() {
        return doUserIds;
    }
    public void setDoUserIds(String doUserIds) {
        this.doUserIds = doUserIds;
    }
    public String getRecorder() {
        return recorder;
    }
    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }
    public String getRecorderName() {
        return recorderName;
    }
    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }
    public Date getRecordeTime() {
        return recordeTime;
    }
    public void setRecordeTime(Date recordeTime) {
        this.recordeTime = recordeTime;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getTaskPlanId() {
        return taskPlanId;
    }
    public void setTaskPlanId(String taskPlanId) {
        this.taskPlanId = taskPlanId;
    }
    public String getDeptId() {
        return deptId;
    }
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    public String getDutyName() {
        return dutyName;
    }
    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }
    public String getDutyId() {
        return dutyId;
    }
    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }
    public String getShiftName() {
        return shiftName;
    }
    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }
    public Date getShiftDate() {
        return shiftDate;
    }
    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }
    
}
