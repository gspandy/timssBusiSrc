package com.timss.itsm.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmMaintainPlan extends ItcMvcBean {

    private static final long serialVersionUID = 4335684793757684448L;
    /** id */
    private int id;
    @AutoGen(value = "ITSM_MTP_CODE", requireType = GenerationType.REQUIRED_NEW)
    private String maintainPlanCode; // 编号
    private String description; // 描述（名字）
    private String specialtyId; // 专业
    private String faultTypeId; // 故障类型ID
    private String faultTypeName; // 故障类型ID
    private String equipNameCode;
    private String equipName; // 设备名
    private String equipId; // 设备ID
    private String principal;// 负责人
    private String principalName;// 负责人
    private String workTeam; // 工作班组
    private String workTeamName; // 工作班组名
    private int maintainPlanCycle; // 维护计划周期
    private Date currStartTime; // 当前周期开始时间
    private int alertTime; // 预警时间长度
    private String maintainPlanFrom; // 维护计划来源(1:维护计划;2:不立即处理;3:遗留问题)
    private String remarks; // 备注
    private Integer jobPlanId; // 作业方案ID
    private Integer parentMTPId; // 父维修计划ID
    private String parentMTPCode; // 父维护计划编号
    private int isHandler; // 是否已经处理
    private Integer preWO; // 前续工单（遗留问题对应的父工单）
    private Date newToDoTime;// 下次生成代办的时间
    private int isAutoGenerWo; // 是否自动生成工单（1：是，0：否）
    private int hasAlertTodo; // 是否有生成提醒待办（当isAutoGenerWo==0时此字段才有意义）
    int yxbz; // 有效标识

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaintainPlanCode() {
        return maintainPlanCode;
    }

    public void setMaintainPlanCode(String maintainPlanCode) {
        this.maintainPlanCode = maintainPlanCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(String specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getFaultTypeId() {
        return faultTypeId;
    }

    public void setFaultTypeId(String faultTypeId) {
        this.faultTypeId = faultTypeId;
    }

    public String getFaultTypeName() {
        return faultTypeName;
    }

    public void setFaultTypeName(String faultTypeName) {
        this.faultTypeName = faultTypeName;
    }

    public String getEquipNameCode() {
        return equipNameCode;
    }

    public void setEquipNameCode(String equipNameCode) {
        this.equipNameCode = equipNameCode;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getWorkTeam() {
        return workTeam;
    }

    public void setWorkTeam(String workTeam) {
        this.workTeam = workTeam;
    }

    public String getWorkTeamName() {
        return workTeamName;
    }

    public void setWorkTeamName(String workTeamName) {
        this.workTeamName = workTeamName;
    }

    public int getMaintainPlanCycle() {
        return maintainPlanCycle;
    }

    public void setMaintainPlanCycle(int maintainPlanCycle) {
        this.maintainPlanCycle = maintainPlanCycle;
    }

    public Date getCurrStartTime() {
        return currStartTime;
    }

    public void setCurrStartTime(Date currStartTime) {
        this.currStartTime = currStartTime;
    }

    public int getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(int alertTime) {
        this.alertTime = alertTime;
    }

    public String getMaintainPlanFrom() {
        return maintainPlanFrom;
    }

    public void setMaintainPlanFrom(String maintainPlanFrom) {
        this.maintainPlanFrom = maintainPlanFrom;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getJobPlanId() {
        return jobPlanId;
    }

    public void setJobPlanId(Integer jobPlanId) {
        this.jobPlanId = jobPlanId;
    }

    public Integer getParentMTPId() {
        return parentMTPId;
    }

    public void setParentMTPId(Integer parentMTPId) {
        this.parentMTPId = parentMTPId;
    }

    public String getParentMTPCode() {
        return parentMTPCode;
    }

    public void setParentMTPCode(String parentMTPCode) {
        this.parentMTPCode = parentMTPCode;
    }

    public int getIsHandler() {
        return isHandler;
    }

    public void setIsHandler(int isHandler) {
        this.isHandler = isHandler;
    }

    public Integer getPreWO() {
        return preWO;
    }

    public void setPreWO(Integer preWO) {
        this.preWO = preWO;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Date getNewToDoTime() {
        return newToDoTime;
    }

    public void setNewToDoTime(Date newToDoTime) {
        this.newToDoTime = newToDoTime;
    }

    public int getYxbz() {
        return yxbz;
    }

    public void setYxbz(int yxbz) {
        this.yxbz = yxbz;
    }

    public int getIsAutoGenerWo() {
        return isAutoGenerWo;
    }

    public void setIsAutoGenerWo(int isAutoGenerWo) {
        this.isAutoGenerWo = isAutoGenerWo;
    }

    public int getHasAlertTodo() {
        return hasAlertTodo;
    }

    public void setHasAlertTodo(int hasAlertTodo) {
        this.hasAlertTodo = hasAlertTodo;
    }

    @Override
    public String toString() {
        return "MaintainPlan [id=" + id + ", maintainPlanCode=" + maintainPlanCode + ", description=" + description
                + ", specialtyId=" + specialtyId + ", faultTypeId=" + faultTypeId + ", faultTypeName=" + faultTypeName
                + ", equipNameCode=" + equipNameCode + ", equipName=" + equipName + ", equipId=" + equipId
                + ", principal=" + principal + ", principalName=" + principalName + ", workTeam=" + workTeam
                + ", workTeamName=" + workTeamName + ", maintainPlanCycle=" + maintainPlanCycle + ", currStartTime="
                + currStartTime + ", alertTime=" + alertTime + ", maintainPlanFrom=" + maintainPlanFrom + ", remarks="
                + remarks + ", jobPlanId=" + jobPlanId + ", parentMTPId=" + parentMTPId + ", parentMTPCode="
                + parentMTPCode + ", isHandler=" + isHandler + ", preWO=" + preWO + ", newToDoTime=" + newToDoTime
                + ", isAutoGenerWo=" + isAutoGenerWo + ", hasAlertTodo=" + hasAlertTodo + ", yxbz=" + yxbz + "]";
    }

}
