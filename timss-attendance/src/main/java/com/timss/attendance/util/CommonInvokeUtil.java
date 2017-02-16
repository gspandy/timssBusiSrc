package com.timss.attendance.util;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CommonInvokeUtil {
    private static Logger log = Logger.getLogger( CommonInvokeUtil.class );

    /**
     * @description: 通过反射获取字段中存值
     * @author: 890166
     * @createDate: 2014-9-22
     * @param obj
     * @param fieldName
     * @return:
     */
    public static String getFieldValueByFieldName(Object obj, String fieldName, String formatString )
                    throws Exception {
            Class<?> objClass = obj.getClass();
            String value = null;

            Field field = objClass.getDeclaredField(fieldName);
            try {
                    // 重新组织字段代码
                    StringBuffer entityField = new StringBuffer("");
                    entityField.append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1, fieldName.length()));
                    // 获取get方法名称
                    String getMethodName = "get" + entityField.toString();
                    Object valueObj = objClass.getMethod(getMethodName).invoke(obj);
                    // 根据不同类型都一律转换为字符串
                    if ("java.lang.String".equals(field.getType().getName())) {
                            value = String.valueOf(valueObj == null ? "" : valueObj);
                    } else if ("java.util.Date".equals(field.getType().getName())) {
                        if( valueObj != null && StringUtils.isNotBlank( formatString )){
                            value = DateFormatUtil.dateToString( (Date)valueObj, formatString );
                        }else if( valueObj == null && StringUtils.isNotBlank( formatString )){
                            value = DateFormatUtil.dateToString( new Date(), formatString );
                        }else{
                            value = DateFormatUtil.dateToString( new Date(), "yyyy-MM-dd HH:mm" );
                        }
                    } else if ("java.math.BigDecimal".equals(field.getType().getName())) {
                            value = String.valueOf(valueObj == null ? "0" : valueObj);
                    }else if ("java.lang.Double".equals(field.getType().getName())) {
                        value = String.valueOf(valueObj == null ? "0" : valueObj);
                    }else if ("java.lang.Integer".equals(field.getType().getName())) {
                        value = String.valueOf(valueObj == null ? "0" : valueObj);
                    }else if ("java.lang.Float".equals(field.getType().getName())) {
                        value = String.valueOf(valueObj == null ? "0" : valueObj);
                    }else if ("double".equals(field.getType().getName())) {
                        value = String.valueOf(valueObj == null ? "0" : valueObj);
                    }else if ("int".equals(field.getType().getName())) {
                        value = String.valueOf(valueObj == null ? "0" : valueObj);
                    }else if ("float".equals(field.getType().getName())) {
                        value = String.valueOf(valueObj == null ? "0" : valueObj);
                    }
            } catch (Exception e) {
                    log.error( e.getMessage(), e );
            }
            return value;
    }
}
