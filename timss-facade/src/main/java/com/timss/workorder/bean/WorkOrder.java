package com.timss.workorder.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class WorkOrder extends ItcMvcBean {

    private static final long serialVersionUID = 6597495110798227159L;
    private int id; // ID

    @AutoGen(value = "WO_WO_CODE", requireType = GenerationType.REQUIRED_NULL)
    private String workOrderCode; // 工单编号

    private Integer maintainPlanId;// 维护计划ID
    private Integer jobPlanId;// 作业方案ID
    private Integer parentWOId;// 父工单ID
    private String parentWOCode;// 父工单编号
    private String woDefectId;// 缺陷单ID
    private String woDefectCode;// 缺陷单编号

    private String description;// 故障描述
    private String workOrderTypeCode;// 工单类型编号

    private String customerCode;// 客户（报障人）工号
    private String customerName;// 客户（报障人）姓名
    private String customerPhone;// 客户（报障人）电话
    private String customerLocation;// 客户（报障人）地址
    private String customerCom; // 客户（报障人）公司名
    private String customerDept; // 客户（报障人）部门名

    private Integer priorityId;// 优先级ID
    private String priorityName;// 优先级名字（服务级别名字）
    private String faultTypeId;// 故障类型ID
    private String faultTypeName;// 故障类型名
    private String serCharacterId;// 服务性质ID
    private String serCharacterName;// 服务性质名
    private String workOrderLabel;// 工单标识
    private String assignEngineerCode;// 意向工程师工号
    private String faultAddress;// 故障地址
    private String newFaultRemarks;// 报障备注
    private Integer weight;// 权重
    private Date appointTime;// 预约上门时间
    private Date defaultBeginTime;// 预计开始时间
    private Date defaultEndTime;// 预计完工时间
    private Date defaultFeedbackTime;// 预计回访时间
    private Date beginTime;// 开始时间
    private Date endTime;// 完工时间
    private Date feedbackTime;// 回访时间
    private Date sendWoTime; // 最新派单时间
    private Integer isAdditionWO;// 是否是补单
    private int isCycleWO;// 是否是周期工单
    private Integer cycleWOId;// 周期工单主体表
    private Date cycleBeginTime;// 周期开始时间
    private Date cycleEndTime;// 周期结束时间
    private String fbResultType;// 回访结果类型
    private String isComplaint;// 是否投诉
    private int isNextEngineer;// 下次是否选此工程师
    private String feedbackRemarks;// 回访备注

    private String workflowId; // 工单对应的流程ID
    private String currStatus; // 工单的当前状态
    private String currHandlerUser; // 工单的当前执行人工号列表
    private String currHandUserName; // 当前处理人名字列表
    private String equipSiteCode;// 设备位置编号
    private String equipNameCode;// 设备编号
    private String equipSite;
    private String equipName;
    private String equipId; // 设备ID
    private int equipSiteId; // 设备位置ID
    private String faultDegreeCode;// 故障程度分类编号
    private String woSpecCode;// 工单专业编号

    private Date discoverTime;// 发现日期
    private String urgentDegreeCode;// 紧急程度
    private String influenceScope;// 影响范围
    private Float currWindSpeed;// 当前风速
    private Float loseElectricPower;// 损失电量
    private int isStepOver;// 是否跨班组
    private String isNowHandlerMonitor;// 是否立即处理
    private String isNowHandlerAssistant;//
    private String isNowHandlerPlantLeader;//
    private String isToWorkTicket;// 是否走工作票

    private int ptwId;// 关联工作票
    private String maintainPlanFrom;// 维护计划来源
    private Float approveStopTime;// 批准停机时间
    private String endReport; // 完工汇报情况
    private String isHasRemainFault; // 是否有遗留问题
    private int nextWoMtp;// 遗留问题对应的维护计划ID
    private String remarks;//

    private String faultConfrimUser;// 故障确认人
    private String endReportUser;// 完工汇报人
    private String endReportUserName;// 完工汇报人名字
    private String acceptanceUser; // 验收人

    private int yxbz;
    private int hasRelease; // 是否有自动释放过
    private int isStop; // 是否审批终止了的工单（非正常结束工单）
    private String sendWoNow; // 用来标识客服新建时是否立即派单

    private String partnerIds;
    private String partnerNames;

    private String principalEngeer; // 仅用于个人运维统计
    private String principalEngeerTeam; // 仅用于团队运维统计

    private String woMaintainCom; // 维修公司或部门
    private String woMaintainTeam; // 维修班组
    private String woMaintainExecutor; // 维修负责人
    private String woMaintainExecutorName; // 维修负责人
    private String woDelayType; // 延期类型
    private String woDelayReason; // 延期原因
    private String woDelayLenEnum; // 延期时长（枚举值）
    private Float woDelayLen; // 延期时长（数字小时）
    private String woIsDelay; // 是否延期

    private String woWindStation; // 风电场
    private String woCommitHandleStyle; // 提交时的处理方式
    private String createUserName;// 创建人名字
    private long solveLen;   //解决时间长度
    
    private String isOverWo; // 是否是超时工单（Y/N）

    public String getIsOverWo() {
        return isOverWo;
    }

    public void setIsOverWo(String isOverWo) {
        this.isOverWo = isOverWo;
    }

    public long getSolveLen() {
        return solveLen;
    }

    public void setSolveLen(long solveLen) {
        this.solveLen = solveLen;
    }

    public String getWoDelayLenEnum() {
        return woDelayLenEnum;
    }

    public void setWoDelayLenEnum(String woDelayLenEnum) {
        this.woDelayLenEnum = woDelayLenEnum;
    }

    public Float getWoDelayLen() {
        return woDelayLen;
    }

    public void setWoDelayLen(Float woDelayLen) {
        this.woDelayLen = woDelayLen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkOrderCode() {
        return workOrderCode;
    }

    public void setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
    }

    public Integer getMaintainPlanId() {
        return maintainPlanId;
    }

    public void setMaintainPlanId(Integer maintainPlanId) {
        this.maintainPlanId = maintainPlanId;
    }

    public Integer getJobPlanId() {
        return jobPlanId;
    }

    public void setJobPlanId(Integer jobPlanId) {
        this.jobPlanId = jobPlanId;
    }

    public Integer getParentWOId() {
        return parentWOId;
    }

    public void setParentWOId(Integer parentWOId) {
        this.parentWOId = parentWOId;
    }

    public String getParentWOCode() {
        return parentWOCode;
    }

    public String getWoDefectId() {
        return woDefectId;
    }

    public void setWoDefectId(String woDefectId) {
        this.woDefectId = woDefectId;
    }

    public String getWoDefectCode() {
        return woDefectCode;
    }

    public void setWoDefectCode(String woDefectCode) {
        this.woDefectCode = woDefectCode;
    }

    public void setParentWOCode(String parentWOCode) {
        this.parentWOCode = parentWOCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkOrderTypeCode() {
        return workOrderTypeCode;
    }

    public void setWorkOrderTypeCode(String workOrderTypeCode) {
        this.workOrderTypeCode = workOrderTypeCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getCustomerCom() {
        return customerCom;
    }

    public void setCustomerCom(String customerCom) {
        this.customerCom = customerCom;
    }

    public String getCustomerDept() {
        return customerDept;
    }

    public void setCustomerDept(String customerDept) {
        this.customerDept = customerDept;
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
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

    public String getSerCharacterId() {
        return serCharacterId;
    }

    public void setSerCharacterId(String serCharacterId) {
        this.serCharacterId = serCharacterId;
    }

    public String getSerCharacterName() {
        return serCharacterName;
    }

    public void setSerCharacterName(String serCharacterName) {
        this.serCharacterName = serCharacterName;
    }

    public String getWorkOrderLabel() {
        return workOrderLabel;
    }

    public void setWorkOrderLabel(String workOrderLabel) {
        this.workOrderLabel = workOrderLabel;
    }

    public String getAssignEngineerCode() {
        return assignEngineerCode;
    }

    public void setAssignEngineerCode(String assignEngineerCode) {
        this.assignEngineerCode = assignEngineerCode;
    }

    public String getFaultAddress() {
        return faultAddress;
    }

    public void setFaultAddress(String faultAddress) {
        this.faultAddress = faultAddress;
    }

    public String getNewFaultRemarks() {
        return newFaultRemarks;
    }

    public void setNewFaultRemarks(String newFaultRemarks) {
        this.newFaultRemarks = newFaultRemarks;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Date getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(Date appointTime) {
        this.appointTime = appointTime;
    }

    public Date getDefaultBeginTime() {
        return defaultBeginTime;
    }

    public void setDefaultBeginTime(Date defaultBeginTime) {
        this.defaultBeginTime = defaultBeginTime;
    }

    public Date getDefaultEndTime() {
        return defaultEndTime;
    }

    public void setDefaultEndTime(Date defaultEndTime) {
        this.defaultEndTime = defaultEndTime;
    }

    public Date getDefaultFeedbackTime() {
        return defaultFeedbackTime;
    }

    public void setDefaultFeedbackTime(Date defaultFeedbackTime) {
        this.defaultFeedbackTime = defaultFeedbackTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public Date getSendWoTime() {
        return sendWoTime;
    }

    public void setSendWoTime(Date sendWoTime) {
        this.sendWoTime = sendWoTime;
    }

    public Integer getIsAdditionWO() {
        return isAdditionWO;
    }

    public void setIsAdditionWO(Integer isAdditionWO) {
        this.isAdditionWO = isAdditionWO;
    }

    public int getIsCycleWO() {
        return isCycleWO;
    }

    public void setIsCycleWO(int isCycleWO) {
        this.isCycleWO = isCycleWO;
    }

    public Integer getCycleWOId() {
        return cycleWOId;
    }

    public void setCycleWOId(Integer cycleWOId) {
        this.cycleWOId = cycleWOId;
    }

    public Date getCycleBeginTime() {
        return cycleBeginTime;
    }

    public void setCycleBeginTime(Date cycleBeginTime) {
        this.cycleBeginTime = cycleBeginTime;
    }

    public Date getCycleEndTime() {
        return cycleEndTime;
    }

    public void setCycleEndTime(Date cycleEndTime) {
        this.cycleEndTime = cycleEndTime;
    }

    public String getFbResultType() {
        return fbResultType;
    }

    public void setFbResultType(String fbResultType) {
        this.fbResultType = fbResultType;
    }

    public String getIsComplaint() {
        return isComplaint;
    }

    public void setIsComplaint(String isComplaint) {
        this.isComplaint = isComplaint;
    }

    public int getIsNextEngineer() {
        return isNextEngineer;
    }

    public void setIsNextEngineer(int isNextEngineer) {
        this.isNextEngineer = isNextEngineer;
    }

    public String getFeedbackRemarks() {
        return feedbackRemarks;
    }

    public void setFeedbackRemarks(String feedbackRemarks) {
        this.feedbackRemarks = feedbackRemarks;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getCurrStatus() {
        return currStatus;
    }

    public void setCurrStatus(String currStatus) {
        this.currStatus = currStatus;
    }

    public String getCurrHandlerUser() {
        return currHandlerUser;
    }

    public void setCurrHandlerUser(String currHandlerUser) {
        this.currHandlerUser = currHandlerUser;
    }

    public String getCurrHandUserName() {
        return currHandUserName;
    }

    public void setCurrHandUserName(String currHandUserName) {
        this.currHandUserName = currHandUserName;
    }

    public String getEquipSiteCode() {
        return equipSiteCode;
    }

    public void setEquipSiteCode(String equipSiteCode) {
        this.equipSiteCode = equipSiteCode;
    }

    public String getEquipNameCode() {
        return equipNameCode;
    }

    public void setEquipNameCode(String equipNameCode) {
        this.equipNameCode = equipNameCode;
    }

    public String getEquipSite() {
        return equipSite;
    }

    public void setEquipSite(String equipSite) {
        this.equipSite = equipSite;
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

    public int getEquipSiteId() {
        return equipSiteId;
    }

    public void setEquipSiteId(int equipSiteId) {
        this.equipSiteId = equipSiteId;
    }

    public String getFaultDegreeCode() {
        return faultDegreeCode;
    }

    public void setFaultDegreeCode(String faultDegreeCode) {
        this.faultDegreeCode = faultDegreeCode;
    }

    public String getWoSpecCode() {
        return woSpecCode;
    }

    public void setWoSpecCode(String woSpecCode) {
        this.woSpecCode = woSpecCode;
    }

    public Date getDiscoverTime() {
        return discoverTime;
    }

    public void setDiscoverTime(Date discoverTime) {
        this.discoverTime = discoverTime;
    }

    public String getUrgentDegreeCode() {
        return urgentDegreeCode;
    }

    public void setUrgentDegreeCode(String urgentDegreeCode) {
        this.urgentDegreeCode = urgentDegreeCode;
    }

    public String getInfluenceScope() {
        return influenceScope;
    }

    public void setInfluenceScope(String influenceScope) {
        this.influenceScope = influenceScope;
    }

    public Float getCurrWindSpeed() {
        return currWindSpeed;
    }

    public void setCurrWindSpeed(Float currWindSpeed) {
        this.currWindSpeed = currWindSpeed;
    }

    public Float getLoseElectricPower() {
        return loseElectricPower;
    }

    public void setLoseElectricPower(Float loseElectricPower) {
        this.loseElectricPower = loseElectricPower;
    }

    public int getIsStepOver() {
        return isStepOver;
    }

    public void setIsStepOver(int isStepOver) {
        this.isStepOver = isStepOver;
    }

    public String getIsNowHandlerMonitor() {
        return isNowHandlerMonitor;
    }

    public void setIsNowHandlerMonitor(String isNowHandlerMonitor) {
        this.isNowHandlerMonitor = isNowHandlerMonitor;
    }

    public String getIsNowHandlerAssistant() {
        return isNowHandlerAssistant;
    }

    public void setIsNowHandlerAssistant(String isNowHandlerAssistant) {
        this.isNowHandlerAssistant = isNowHandlerAssistant;
    }

    public String getIsNowHandlerPlantLeader() {
        return isNowHandlerPlantLeader;
    }

    public void setIsNowHandlerPlantLeader(String isNowHandlerPlantLeader) {
        this.isNowHandlerPlantLeader = isNowHandlerPlantLeader;
    }

    public String getIsToWorkTicket() {
        return isToWorkTicket;
    }

    public void setIsToWorkTicket(String isToWorkTicket) {
        this.isToWorkTicket = isToWorkTicket;
    }

    public int getPtwId() {
        return ptwId;
    }

    public void setPtwId(int ptwId) {
        this.ptwId = ptwId;
    }

    public String getMaintainPlanFrom() {
        return maintainPlanFrom;
    }

    public void setMaintainPlanFrom(String maintainPlanFrom) {
        this.maintainPlanFrom = maintainPlanFrom;
    }

    public Float getApproveStopTime() {
        return approveStopTime;
    }

    public void setApproveStopTime(Float approveStopTime) {
        this.approveStopTime = approveStopTime;
    }

    public String getEndReport() {
        return endReport;
    }

    public void setEndReport(String endReport) {
        this.endReport = endReport;
    }

    public String getIsHasRemainFault() {
        return isHasRemainFault;
    }

    public void setIsHasRemainFault(String isHasRemainFault) {
        this.isHasRemainFault = isHasRemainFault;
    }

    public int getNextWoMtp() {
        return nextWoMtp;
    }

    public void setNextWoMtp(int nextWoMtp) {
        this.nextWoMtp = nextWoMtp;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFaultConfrimUser() {
        return faultConfrimUser;
    }

    public void setFaultConfrimUser(String faultConfrimUser) {
        this.faultConfrimUser = faultConfrimUser;
    }

    public String getEndReportUser() {
        return endReportUser;
    }

    public void setEndReportUser(String endReportUser) {
        this.endReportUser = endReportUser;
    }

    public String getEndReportUserName() {
        return endReportUserName;
    }

    public void setEndReportUserName(String endReportUserName) {
        this.endReportUserName = endReportUserName;
    }

    public String getAcceptanceUser() {
        return acceptanceUser;
    }

    public void setAcceptanceUser(String acceptanceUser) {
        this.acceptanceUser = acceptanceUser;
    }

    public int getYxbz() {
        return yxbz;
    }

    public void setYxbz(int yxbz) {
        this.yxbz = yxbz;
    }

    public int getHasRelease() {
        return hasRelease;
    }

    public void setHasRelease(int hasRelease) {
        this.hasRelease = hasRelease;
    }

    public int getIsStop() {
        return isStop;
    }

    public void setIsStop(int isStop) {
        this.isStop = isStop;
    }

    public String getPartnerIds() {
        return partnerIds;
    }

    public void setPartnerIds(String partnerIds) {
        this.partnerIds = partnerIds;
    }

    public String getPartnerNames() {
        return partnerNames;
    }

    public void setPartnerNames(String partnerNames) {
        this.partnerNames = partnerNames;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getSendWoNow() {
        return sendWoNow;
    }

    public void setSendWoNow(String sendWoNow) {
        this.sendWoNow = sendWoNow;
    }

    public String getPrincipalEngeer() {
        return principalEngeer;
    }

    public void setPrincipalEngeer(String principalEngeer) {
        this.principalEngeer = principalEngeer;
    }

    public String getPrincipalEngeerTeam() {
        return principalEngeerTeam;
    }

    public void setPrincipalEngeerTeam(String principalEngeerTeam) {
        this.principalEngeerTeam = principalEngeerTeam;
    }

    public String getWoMaintainCom() {
        return woMaintainCom;
    }

    public void setWoMaintainCom(String woMaintainCom) {
        this.woMaintainCom = woMaintainCom;
    }

    public String getWoMaintainTeam() {
        return woMaintainTeam;
    }

    public void setWoMaintainTeam(String woMaintainTeam) {
        this.woMaintainTeam = woMaintainTeam;
    }

    public String getWoMaintainExecutor() {
        return woMaintainExecutor;
    }

    public void setWoMaintainExecutor(String woMaintainExecutor) {
        this.woMaintainExecutor = woMaintainExecutor;
    }

    public String getWoDelayType() {
        return woDelayType;
    }

    public void setWoDelayType(String woDelayType) {
        this.woDelayType = woDelayType;
    }

    public String getWoDelayReason() {
        return woDelayReason;
    }

    public void setWoDelayReason(String woDelayReason) {
        this.woDelayReason = woDelayReason;
    }

    public String getWoIsDelay() {
        return woIsDelay;
    }

    public void setWoIsDelay(String woIsDelay) {
        this.woIsDelay = woIsDelay;
    }

    public String getWoMaintainExecutorName() {
        return woMaintainExecutorName;
    }

    public void setWoMaintainExecutorName(String woMaintainExecutorName) {
        this.woMaintainExecutorName = woMaintainExecutorName;
    }

    public String getWoWindStation() {
        return woWindStation;
    }

    public void setWoWindStation(String woWindStation) {
        this.woWindStation = woWindStation;
    }

    public String getWoCommitHandleStyle() {
        return woCommitHandleStyle;
    }

    public void setWoCommitHandleStyle(String woCommitHandleStyle) {
        this.woCommitHandleStyle = woCommitHandleStyle;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    @Override
    public String toString() {
        return "WorkOrder [id=" + id + ", workOrderCode=" + workOrderCode + ", maintainPlanId=" + maintainPlanId
                + ", jobPlanId=" + jobPlanId + ", parentWOId=" + parentWOId + ", parentWOCode=" + parentWOCode
                + ", woDefectId=" + woDefectId + ", woDefectCode=" + woDefectCode + ", description=" + description
                + ", workOrderTypeCode=" + workOrderTypeCode + ", customerCode=" + customerCode + ", customerName="
                + customerName + ", customerPhone=" + customerPhone + ", customerLocation=" + customerLocation
                + ", customerCom=" + customerCom + ", customerDept=" + customerDept + ", priorityId=" + priorityId
                + ", priorityName=" + priorityName + ", faultTypeId=" + faultTypeId + ", faultTypeName="
                + faultTypeName + ", serCharacterId=" + serCharacterId + ", serCharacterName=" + serCharacterName
                + ", workOrderLabel=" + workOrderLabel + ", assignEngineerCode=" + assignEngineerCode
                + ", faultAddress=" + faultAddress + ", newFaultRemarks=" + newFaultRemarks + ", weight=" + weight
                + ", appointTime=" + appointTime + ", defaultBeginTime=" + defaultBeginTime + ", defaultEndTime="
                + defaultEndTime + ", defaultFeedbackTime=" + defaultFeedbackTime + ", beginTime=" + beginTime
                + ", endTime=" + endTime + ", feedbackTime=" + feedbackTime + ", sendWoTime=" + sendWoTime
                + ", isAdditionWO=" + isAdditionWO + ", isCycleWO=" + isCycleWO + ", cycleWOId=" + cycleWOId
                + ", cycleBeginTime=" + cycleBeginTime + ", cycleEndTime=" + cycleEndTime + ", fbResultType="
                + fbResultType + ", isComplaint=" + isComplaint + ", isNextEngineer=" + isNextEngineer
                + ", feedbackRemarks=" + feedbackRemarks + ", workflowId=" + workflowId + ", currStatus=" + currStatus
                + ", currHandlerUser=" + currHandlerUser + ", currHandUserName=" + currHandUserName
                + ", equipSiteCode=" + equipSiteCode + ", equipNameCode=" + equipNameCode + ", equipSite=" + equipSite
                + ", equipName=" + equipName + ", equipId=" + equipId + ", equipSiteId=" + equipSiteId
                + ", faultDegreeCode=" + faultDegreeCode + ", woSpecCode=" + woSpecCode + ", discoverTime="
                + discoverTime + ", urgentDegreeCode=" + urgentDegreeCode + ", influenceScope=" + influenceScope
                + ", currWindSpeed=" + currWindSpeed + ", loseElectricPower=" + loseElectricPower + ", isStepOver="
                + isStepOver + ", isNowHandlerMonitor=" + isNowHandlerMonitor + ", isNowHandlerAssistant="
                + isNowHandlerAssistant + ", isNowHandlerPlantLeader=" + isNowHandlerPlantLeader + ", isToWorkTicket="
                + isToWorkTicket + ", ptwId=" + ptwId + ", maintainPlanFrom=" + maintainPlanFrom + ", approveStopTime="
                + approveStopTime + ", endReport=" + endReport + ", isHasRemainFault=" + isHasRemainFault
                + ", nextWoMtp=" + nextWoMtp + ", remarks=" + remarks + ", faultConfrimUser=" + faultConfrimUser
                + ", endReportUser=" + endReportUser + ", endReportUserName=" + endReportUserName + ", acceptanceUser="
                + acceptanceUser + ", yxbz=" + yxbz + ", hasRelease=" + hasRelease + ", isStop=" + isStop
                + ", sendWoNow=" + sendWoNow + ", partnerIds=" + partnerIds + ", partnerNames=" + partnerNames
                + ", principalEngeer=" + principalEngeer + ", principalEngeerTeam=" + principalEngeerTeam
                + ", woMaintainCom=" + woMaintainCom + ", woMaintainTeam=" + woMaintainTeam + ", woMaintainExecutor="
                + woMaintainExecutor + ", woMaintainExecutorName=" + woMaintainExecutorName + ", woDelayType="
                + woDelayType + ", woDelayReason=" + woDelayReason + ", woDelayLenEnum=" + woDelayLenEnum
                + ", woDelayLen=" + woDelayLen + ", woIsDelay=" + woIsDelay + ", woWindStation=" + woWindStation
                + ", woCommitHandleStyle=" + woCommitHandleStyle + ", createUserName=" + createUserName + "]";
    }

  

}
