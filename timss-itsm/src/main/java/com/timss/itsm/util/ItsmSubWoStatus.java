package com.timss.itsm.util;

public enum ItsmSubWoStatus {
    TO_SEND("toSend", "待分派"),
    HANDLE_SUBWO("handleSubWo", "工程师处理"), 
    DELAY_AUDIT("delayAudit", "延时审批"),
    CHIEF_AUDIT("chiefAudit", "主管审批"), 
    SEND_SUBWO("sendSubWo", "客服派单"),
    REPORT_REPORT("reportSubWo", "完工报告"),
    END_SUBWO("endSubWo", "已结束");
    
    private String enName;
    private String cnName;
    
    private ItsmSubWoStatus(String enName,String cnName) {
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
