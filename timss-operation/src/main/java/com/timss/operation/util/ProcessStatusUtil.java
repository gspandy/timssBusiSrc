package com.timss.operation.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
public class ProcessStatusUtil {
    /**
     * 草稿
     */
    public static final String DRAFT_STR = "DRAFT";
    
    /**
     * 提交点检日志
     */
    public static final String APPLY_STR = "APPLY";
    
    /**
     * 专业主管审批
     */
    public static final String AUDIT_STR = "AUDIT";
    
    /**
     * 完成
     */
    public static final String DONE_STR = "DONE";
    
    /**
     * 作废
     */
    public static final String INVALID_STR = "INVALID";
 
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
