package com.timss.workorder.util;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}登塔申请状态定义
 * @description: {desc}
 * @company: gdyd
 * @className: TowerApplyStatusUtil.java
 * @author: 朱旺
 * @createDate: 2015-12-22
 * @updateUser:
 * @version: 1.0
 */
public class TowerApplyStatusUtil {
    /**
     * 0草稿
     */
    public static final String DRAFT = "draft";
    
    /**
     * 1填写登塔申请
     */
    public static final String FILL_APPLY = "apply";
    
    /**
     * 2运检部安全主管审批
     */
    public static final String DEPARTMENT_AUDIT = "departmentAudit";
    
    /**
     * 3运检部部长审批
     */
    public static final String TRANSPORT_AUDIT = "transportAudit";
    
    /**
     * 4生技安监部专责审核
     */
    public static final String RESPON_AUDIT = "responAudit";
 
    /**
     * 5生技安监部部长审核
     */
    public static final String MINISTER_AUDIT = "ministerAudit";
    
    /**
     * 6分管副总审核
     */
    public static final String DEPUTY_AUDIT = "deputyAudit";
    
    /**
     * 6 已完成
     */
    public static final String FINISH = "finish";
    
    
    /**
     * 7 作废
     */
    public static final String INVALIDATE = "invalidate";
    
    
	/**
     * 
     * @description:枚举转名称
     * @author: 朱旺
     * @createDate: 2015年12月22日
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
