package com.timss.ptw.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtoInfo.java
 * @author: 王中华
 * @createDate: 2015-7-29
 * @updateUser: 王中华
 * @version: 1.0
 */
public class PtoInfo extends ItcMvcBean {

    private static final long serialVersionUID = -4773707906715690646L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String id;
    @AutoGen(requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW, value = "PTW_PTO_CODE")
    private String code;  //编号
    private String sptoId;  //标准操作票ID
    private String type;
    private String windStation;  //风电场
    private String task;  //操作任务
    private String assetId;  //设备ID
    private String assetName;  //设备名
    private String isProper;  //是否合格
    private String problem;  //存在的问题
    private Date preBeginOperTime;  //预计操作时间
    private Date preEndOperTime;  //预计结束时间
    private Date beginOperTime;  //操作时间
    private Date endOperTime;  //结束时间
    private String commander;  //发令人
    private String commanderName;
    private String operator;  //操作人
    private String operatorName;
    private String guardian;  //监护人
    private String guardianName;  
    private String ondutyMonitor;  //值长
    private String ondutyMonitorName;
    private String ondutyPrincipal;  //值班负责人
    private String ondutyPrincipalName;
    private String workflowId;  //流程ID
    private String currHandlerUser;  //当前处理人
    private String currHandlerUserName;  //当前处理人名
    private String currStatus;  //当前状态
    private String delFlag;  //删除标识
    private String operItemRemarks; //操作项备注
    
    public String getOperItemRemarks() {
        return operItemRemarks;
    }
    public void setOperItemRemarks(String operItemRemarks) {
        this.operItemRemarks = operItemRemarks;
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
    public String getSptoId() {
        return sptoId;
    }
    public void setSptoId(String sptoId) {
        this.sptoId = sptoId;
    }
    /**
     * @return the windStation
     */
    public String getWindStation() {
        return windStation;
    }
    /**
     * @param windStation the windStation to set
     */
    public void setWindStation(String windStation) {
        this.windStation = windStation;
    }
    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getIsProper() {
        return isProper;
    }
    public void setIsProper(String isProper) {
        this.isProper = isProper;
    }
    public String getProblem() {
        return problem;
    }
    public void setProblem(String problem) {
        this.problem = problem;
    }
    public Date getPreBeginOperTime() {
        return preBeginOperTime;
    }
    public void setPreBeginOperTime(Date preBbeginOperTime) {
        this.preBeginOperTime = preBbeginOperTime;
    }
    public Date getPreEndOperTime() {
        return preEndOperTime;
    }
    public void setPreEndOperTime(Date preEndOperTime) {
        this.preEndOperTime = preEndOperTime;
    }
    public Date getBeginOperTime() {
        return beginOperTime;
    }
    public void setBeginOperTime(Date beginOperTime) {
        this.beginOperTime = beginOperTime;
    }
    public Date getEndOperTime() {
        return endOperTime;
    }
    public void setEndOperTime(Date endOperTime) {
        this.endOperTime = endOperTime;
    }
    public String getCommander() {
        return commander;
    }
    public void setCommander(String commander) {
        this.commander = commander;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getGuardian() {
        return guardian;
    }
    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }
    public String getOndutyMonitor() {
        return ondutyMonitor;
    }
    public void setOndutyMonitor(String ondutyMonitor) {
        this.ondutyMonitor = ondutyMonitor;
    }
    public String getOndutyPrincipal() {
        return ondutyPrincipal;
    }
    public void setOndutyPrincipal(String ondutyPrincipal) {
        this.ondutyPrincipal = ondutyPrincipal;
    }
    public String getWorkflowId() {
        return workflowId;
    }
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }
    public String getCurrHandlerUser() {
        return currHandlerUser;
    }
    public void setCurrHandlerUser(String currHandlerUser) {
        this.currHandlerUser = currHandlerUser;
    }
    public String getCurrHandlerUserName() {
        return currHandlerUserName;
    }
    public void setCurrHandlerUserName(String currHandlerUserName) {
        this.currHandlerUserName = currHandlerUserName;
    }
    public String getCurrStatus() {
        return currStatus;
    }
    public void setCurrStatus(String currStatus) {
        this.currStatus = currStatus;
    }
    public String getDelFlag() {
        return delFlag;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    
    public String getCommanderName() {
        return commanderName;
    }
    public void setCommanderName(String commanderName) {
        this.commanderName = commanderName;
    }
    public String getOperatorName() {
        return operatorName;
    }
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    public String getGuardianName() {
        return guardianName;
    }
    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }
    public String getOndutyMonitorName() {
        return ondutyMonitorName;
    }
    public void setOndutyMonitorName(String ondutyMonitorName) {
        this.ondutyMonitorName = ondutyMonitorName;
    }
    public String getOndutyPrincipalName() {
        return ondutyPrincipalName;
    }
    public void setOndutyPrincipalName(String ondutyPrincipalName) {
        this.ondutyPrincipalName = ondutyPrincipalName;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PtoInfo [id=" + id + ", code=" + code + ", sptoId=" + sptoId + ", type=" + type + ", windStation="
                + windStation + ", task=" + task + ", assetId=" + assetId + ", assetName=" + assetName + ", isProper="
                + isProper + ", problem=" + problem + ", preBeginOperTime=" + preBeginOperTime + ", preEndOperTime="
                + preEndOperTime + ", beginOperTime=" + beginOperTime + ", endOperTime=" + endOperTime + ", commander="
                + commander + ", commanderName=" + commanderName + ", operator=" + operator + ", operatorName="
                + operatorName + ", guardian=" + guardian + ", guardianName=" + guardianName + ", ondutyMonitor="
                + ondutyMonitor + ", ondutyMonitorName=" + ondutyMonitorName + ", ondutyPrincipal=" + ondutyPrincipal
                + ", ondutyPrincipalName=" + ondutyPrincipalName + ", workflowId=" + workflowId + ", currHandlerUser="
                + currHandlerUser + ", currHandlerUserName=" + currHandlerUserName + ", currStatus=" + currStatus
                + ", delFlag=" + delFlag + "]";
    }
   
}
