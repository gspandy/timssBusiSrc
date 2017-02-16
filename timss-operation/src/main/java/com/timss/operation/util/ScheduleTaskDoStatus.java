package com.timss.operation.util;

/**
 * @title: {title} 定期任务执行状态枚举
 * @description: {desc}
 * @company: gdyd
 * @className: ScheduleTaskDoStatus.java
 * @author: 王中华
 * @createDate: 2016-12-7
 * @updateUser: 王中华
 * @version: 1.0
 */
public enum ScheduleTaskDoStatus {
    DONE("done", "已执行"),
    UNDO("undo", "未执行"), 
    NOTDO("notdo", "不执行");
    
    private String enName;
    private String cnName;
    
    private ScheduleTaskDoStatus(String enName,String cnName) {
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
