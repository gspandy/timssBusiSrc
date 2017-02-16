package com.timss.workorder.util;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}开工申请状态定义
 * @description: {desc}
 * @company: gdyd
 * @className: WoapplyStatusUtil.java
 * @author: 王中华
 * @createDate: 2015-12-20
 * @updateUser: 王中华
 * @version: 1.0
 */
public class WoapplyStatusUtil {
    /**
     * 0草稿
     */
    public static final String DRAFT = "draft";
    
    /**
     * 1填写开工申请
     */
    public static final String FILL_APPLY = "txkgsq";
    
    /**
     * 2项目负责人审批
     */
    public static final String PRINCIPAL_AUDIT = "xmfzrsp";
    
    /**
     * 3运检部部长审批
     */
    public static final String RUN_LEADER_AUDIT = "yjbbzsp";
    
    /**
     * 4生技安监部专责审核
     */
    public static final String SAFE_DEPARTMENT_AUDIT = "sjajbzzsh";
 
    /**
     * 5生技安监部部长审核
     */
    public static final String SAFE_DEPARTLEADER_AUDIT = "sjajbbzsh";
    /**
     * 6 完成
     */
    public static final String FINISH = "finish";
    
    
    /**
     * 7 已作废
     */
    public static final String INVALIDATE = "invalidate";
    
    
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
