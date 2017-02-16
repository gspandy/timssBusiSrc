package com.timss.operation.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ScheduleTaskPlan extends ItcMvcBean{

    private static final long serialVersionUID = 7042250693905847806L;
    
    /**
     * 主键id
     */
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL) // 自动生成id
    private String id;
    
    /**
     * 对应的定时任务规则组件ID
     */
    private String componentId;
    /**
     * 编号
     */
    @AutoGen(value = "OPR_SCHED_TASK_PLAN", requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW)
    private String code;
    /**
     * 类型
     */
    private String type;
    /**
     * 关联设备id
     */
    private String assetId;
    /**
     * 关联设备名
     */
    private String assetName;
    /**
     * 是否启用
     */
    private String activityFlag;
    /**
     * 工作内容
     */
    private String content;
    /**
     * 周期类型
     */
    private String cycleType;
    /**
     * 周期长度
     */
    private int cycleLen;
    
    /**
     * 指定日期
     */
    private String specifiedDate;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 计划失效时间
     */
    private Date invalideTime;
    /**
     * 是否允许转下一班
     */
    private String nextShift;
    /**
     * 工种
     */
    private String deptId;
    /**
     * 班次
     */
    private String shiftIds; 
    
  
    private String delFlag;


    public String getSpecifiedDate() {
        return specifiedDate;
    }


    public void setSpecifiedDate(String specifiedDate) {
        this.specifiedDate = specifiedDate;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getComponentId() {
        return componentId;
    }


    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
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


    public String getActivityFlag() {
        return activityFlag;
    }


    public void setActivityFlag(String activityFlag) {
        this.activityFlag = activityFlag;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getCycleType() {
        return cycleType;
    }


    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }


    public int getCycleLen() {
        return cycleLen;
    }


    public void setCycleLen(int cycleLen) {
        this.cycleLen = cycleLen;
    }


    public Date getBeginTime() {
        return beginTime;
    }


    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }


    public Date getInvalideTime() {
        return invalideTime;
    }


    public void setInvalideTime(Date invalideTime) {
        this.invalideTime = invalideTime;
    }


    public String getNextShift() {
        return nextShift;
    }


    public void setNextShift(String nextShift) {
        this.nextShift = nextShift;
    }


    public String getDeptId() {
        return deptId;
    }


    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }


    public String getShiftIds() {
        return shiftIds;
    }


    public void setShiftIds(String shiftIds) {
        this.shiftIds = shiftIds;
    }


    public String getDelFlag() {
        return delFlag;
    }


    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
   
    
}
