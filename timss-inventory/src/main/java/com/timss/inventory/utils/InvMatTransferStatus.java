package com.timss.inventory.utils;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 流程状态定义
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTransferStatus.java
 * @author: 890151
 * @createDate: 2016-1-8
 * @updateUser: 890151
 * @version: 1.0
 */
public class InvMatTransferStatus {
    /**
     * 0草稿
     */
    public static final String DRAFT = "DRAFT";
    
    /**
     * 1提交移库申请
     */
    public static final String TRANSFER_APPLY_COMMIT = "TRANSFER_APPLY_COMMIT";
    
    /**
     * 2接收方仓管员
     */
    public static final String STOREMAN_AUDIT = "STOREMAN_AUDIT";
    
    /**
     * 3已作废
     */
    public static final String OBSOLETE = "OBSOLETE";
    
    /**
     * 4已完成
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
