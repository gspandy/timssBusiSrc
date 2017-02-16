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
public class WoProcessStatusUtil {
    /**
     * 0草稿
     */
    public static final String DRAFT_STR = "DRAFT";
    
    /**
     * 1工单提交
     */
    public static final String WO_COMMIT_STR = "WO_COMMIT";
    
    /**
     * 2值长确认缺陷
     */
    public static final String DUTY_CONFIRM_DEFECT_STR = "DUTY_CONFIRM_DEFECT";
    
    /**
     * 3专工下发缺陷
     */
    public static final String EXPERT_DEAL_STR = "EXPERT_DEAL";
    
    /**
     * 4项目负责人策划
     */
    public static final String SUPERVISOR_PLAN_STR = "SUPERVISOR_PLAN";
 
    /**
     * 5项目负责人回填
     */
    public static final String SUPERVISOR_REPORT_STR = "SUPERVISOR_REPORT";
    
    /**
     * 6值长确认结束
     */
    public static final String DUTY_CONFIRM_OVER_STR = "DUTY_CONFIRM_OVER";
    
    /**
     * 7设备部审批
     */
    public static final String DEALY_EQUIP_AUDIT_STR = "DEALY_EQUIP_AUDIT";
    
    /**
     * 8生安部审批
     */
    public static final String DELAY_SAFE_AUDIT_STR = "DELAY_SAFE_AUDIT";
    
    /**
     * 9运行部审批
     */
    public static final String DELAY_OPERATION_AUDIT_STR = "DELAY_OPERATION_AUDIT";
    
    /**
     * 10分管生产领导审批
     */
    public static final String DELAY_LEADER_AUDIT_STR = "DELAY_LEADER_AUDIT";
    
    /**
     * 11值长确认延期
     */
    public static final String DELAY_DUTY_AUDIT_STR = "DELAY_DUTY_AUDIT";
    
    /**
     * 12值长启动工单
     */
    public static final String DELAY_DUTY_RESTART_STR = "DELAY_DUTY_RESTART";
    
    /**
     * 生技主管启动工单
     */
    public static final String DELAY_DIRECTOR_RESTART_STR = "DELAY_DIRECTOR_RESTART";
    
    /**
     * 13策划组重新分配
     */
    public static final String PLANNING_GROUP_AUDIT_STR = "PLANNING_GROUP_AUDIT";
    
    /**
     * 14生安分部审批
     */
    public static final String SAFE_CLEAR_AUDIT_STR = "SAFE_CLEAR_AUDIT";
    
    /**
     * 15值长清除缺陷
     */
    public static final String DUTY_CLEAR_AUDIT_STR = "DUTY_CLEAR_AUDIT";
 
    /**
     * 16已作废
     */
    public static final String OBSELETE_STR = "OBSELETE";
    
    /**
     * 17已完成
     */
    public static final String DONE_STR = "DONE";
    
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
