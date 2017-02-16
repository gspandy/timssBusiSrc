package com.timss.workorder.util;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 流程状态定义
 * @description: {desc}
 * @company: gdyd
 * @className: ProcessStatusUtil.java
 * @author: fengtw
 * @createDate: 日期
 * @updateUser: fengtw
 * @version: 1.0
 */
public class WoProcessStatusUtilZJW {
    /**
     * 0草稿
     */
    public static final String DRAFT = "DRAFT";
    
    /**
     * 1工单提交
     */
    public static final String WORK_ORDER_COMMIT = "WORK_ORDER_COMMIT";
    
    /**
     * 2班长审核
     */
    public static final String MONITOR_AUDIT = "MONITOR_AUDIT";
    
    /**
     * 3场长(助理)审核
     */
    public static final String CHAIRMAN_AUDIT = "CHAIRMAN_AUDIT";
    
    /**
     * 4班长分发
     */
    public static final String MONITOR_DISTRIBUTE = "MONITOR_DISTRIBUTE";
 
    /**
     * 5工作策划
     */
    public static final String WORK_ORDER_PLAN = "WORK_ORDER_PLAN";
    
    /**
     * 6工作票流程
     */
    public static final String WORK_TICKET_PROCEDURE = "WORK_TICKET_PROCEDURE";
    
    /**
     * 7填写故障缺陷处理单
     */
    public static final String WORK_ORDER_REPORT = "WORK_ORDER_REPORT";
    
    /**
     * 8验收工作
     */
    public static final String WORK_ORDER_CHECK = "WORK_ORDER_CHECK";
    
    /**
     * 9运检主管审批
     */
    public static final String SHIELD_OPR_DIRECTOR_AUDIT = "SHIELD_OPR_DIRECTOR_AUDIT";
    
    /**
     * 10运检部长审批
     */
    public static final String SHIELD_OPR_MINISTER_AUDIT = "SHIELD_OPR_MINISTER_AUDIT";
    
    /**
     * 11生安专责审批
     */
    public static final String SHIELD_SAFE_CLERK_AUDIT = "SHIELD_SAFE_CLERK_AUDIT";
    
    /**
     * 12生安部部长审批
     */
    public static final String SHIELD_SAFE_MINISTER_AUDIT = "SHIELD_SAFE_MINISTER_AUDIT";
    
    /**
     * 13值长复位
     */
    public static final String SHIELD_DUTY_RESET = "SHIELD_DUTY_RESET";
    
    /**
     * 14挂起
     */
    public static final String SUSPEND = "SUSPEND";
    
    /**
     * 15已作废
     */
    public static final String OBSELETE = "OBSELETE";
    
    /**
     * 16已完成
     */
    public static final String DONE = "DONE";
    
	/**
     * 
     * @description:枚举转名称
     * @author: fengtw
     * @createDate: 2015年11月3日
     * @param type 枚举类型   key 枚举值
     * @return:
     */
    public static String getEnumName(ItcMvcService itcMvcService, String type, String key) {
        String value = "";
        if( StringUtils.isNotBlank( key ) ){
            List<AppEnum> emList = itcMvcService.getEnum( type );
            if( emList != null && emList.size() > 0 ){
                for( AppEnum appVo : emList ){
                    if( key.equalsIgnoreCase( appVo.getCode() ) ){
                    	value =  appVo.getLabel();
                        break;
                    }
                }
            }
        }
        return value;
    }
  
}
