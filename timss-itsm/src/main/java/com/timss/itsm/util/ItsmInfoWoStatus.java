package com.timss.itsm.util;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: ItsmInfoWoStatus.java
 * @author: 王中华
 * @createDate: 2016-11-2
 * @updateUser: 王中华
 * @version: 1.0
 * 
 * draft,newApply,itCenterVerify,applyDeptPrincipalAudit,principalLeaderAudit,itCenterSend,
 * itCenterLend,applicantReturn,itCenterConfirm,itEngineerHandler,applicantValidation,end,
 * invalid
 */
public enum ItsmInfoWoStatus {
    DRAFT("draft", "草稿"),
    APPLY("newApply", "新建申请"), 
    ITCENTER_VERIFY("itCenterVerify", "信息中心核实"),
    APPLYDEPT_PRINCIPAL("applyDeptPrincipalAudit", "部门负责人审批"), 
    PRINCIPAL_LEADER("principalLeaderAudit", "主管领导审批"),
    ITCENTER_SEND("itCenterSend", "信息中心分派"),
    
    ITCENTER_LEND("itCenterLend", "信息中心借出"),
    APPLICANT_RETURN("applicantReturn", "申请人归还"),
    ITCENTER_CONFIRM("itCenterConfirm", "信息中心确认"),
    ENGINEER_HANDLER("itEngineerHandler", "专工处理"),
    APPLICANT_VALIDATION("applicantValidation", "申请人验证"),
    END("end", "结束"),
    INVALID("invalid", "作废");
    
    
    private String enName;
    private String cnName;
    
    private ItsmInfoWoStatus(String enName,String cnName) {
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
