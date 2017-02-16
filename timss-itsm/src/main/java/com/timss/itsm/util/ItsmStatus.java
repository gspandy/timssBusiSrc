package com.timss.itsm.util;

public enum ItsmStatus {
    DRAFT("draft", "草稿"),
    NEW_WO("newWO", "新建"), 
    APPLICANT_AUDIT("applicantAudit", "申请人流转"),
    INIT_COM_AUDIT("initComAudit", "申请单位审批"), 
    INFOCENTER_AUDIT("infoCenterAudit", "集团信息中心审批"),
    GROUP_DEPT_AUDIT("groupDeptAudit", "集团部门审批"),
    SEND_WO("sendWO", "科技公司派单"), 
    WHB_MANAGER_AUDIT("whbManagerAudit","经理审批"),
    CHIEF_AUDIT("chiefAudit", "主管审批"), 
    SEND_WO_B("sendWOB", "客服派单"),
    WORK_PALN("workPlan", "工程师处理"),
    WO_FEEDBACK("woFeedback", "回访"), 
    WO_FILING("woFiling", "已结束"),
    WO_OBSOLETE("woObsolete", "已作废"), 
    DELAY_AUDIT("delayAudit", "延时审批"),
    ENDWORK_REPORT("endWorkReport", "完工报告"),
    APPLICANT_CONFIRM("applicantConfirm", "申请人确认");
     
    private String enName;
    private String cnName;
    
    private ItsmStatus(String enName,String cnName) {
        this.enName = enName;
        this.cnName = cnName;
    }
    
    public String getEnName(){
        return enName;
    }
    
    public String getCnName(){
        return cnName;
    }
}
