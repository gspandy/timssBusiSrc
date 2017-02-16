package com.timss.finance.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: ReflectionUtil.java
 * @author: 890166
 * @createDate: 2014-10-17
 * @updateUser: 890166
 * @version: 1.0
 */
public class ReflectionUtil {
    private static Logger logger =Logger.getLogger(ReflectionUtil.class);
	/**
	 * @description: 通过反射获取字段中存值
	 * @author: 890166
	 * @createDate: 2014-9-22
	 * @param obj
	 * @param fieldName
	 * @return:
	 */
	public static String getFieldValueByFieldName(Object obj, String fieldName)
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				value = sdf.format(valueObj == null ? new Date() : valueObj);
			} else if ("java.math.BigDecimal".equals(field.getType().getName())) {
				value = String.valueOf(valueObj == null ? "0" : valueObj);
			} else if ("double".equals(field.getType().getName())) {
				value = String.valueOf(valueObj == null ? "0" : valueObj);
			} else if ("int".equals(field.getType().getName())) {
				value = String.valueOf(valueObj == null ? "0" : valueObj);
			}
		} catch (Exception e) {
			logger.warn("通过反射获取fieldName:"+fieldName+" 失败");
		}
		return value;
	}
	
	/**
	 * @description: Bean 与 Bean之间的数据转换
	 * @author: 890166
	 * @createDate: 2014-10-17
	 * @param formObj
	 * @param toObj
	 * @return
	 * @throws Exception:
	 */
	public static Object conventBean2Bean(Object fromObj, Object toObj) throws Exception {
		Class<?> fromObjClass = fromObj.getClass();
		Field[] fromFieldArray = fromObjClass.getDeclaredFields();
		String fromFieldName = null;
		
		Class<?> toObjClass = toObj.getClass();
		Field[] toFieldArray = toObjClass.getDeclaredFields();
		String toFieldName = null;
		
		for(Field toField : toFieldArray){
			//获取赋值实体字段名称
			toFieldName = toField.getName();
			toField.setAccessible(true);
			for(Field fromField : fromFieldArray){
				//获取取值实体字段名称
				fromFieldName = fromField.getName();
				fromField.setAccessible(true);
				
				if(toFieldName.equals(fromFieldName)){
					StringBuffer entityField = new StringBuffer("");
					entityField.append(toFieldName.substring(0, 1).toUpperCase()).append(toFieldName.substring(1, toFieldName.length()));
					// 获取get方法名称
					String getMethodName = "get" + entityField.toString();
					Object valueObj = fromObjClass.getMethod(getMethodName).invoke(fromObj);
					// 根据设入的实体类字段类型判断
					if ("java.lang.String".equals(toField.getType().getName())) {//转换成字符串
						String value = String.valueOf(valueObj == null ? "" : valueObj);
						toField.set(toObj, value);
						break;
					} else if ("java.util.Date".equals(toField.getType().getName())) {//转换成日期
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String dateStr = sdf.format(valueObj == null ? new Date() : valueObj);
						
						DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
						Date value = format.parse(dateStr);  
						toField.set(toObj, value);
						break;
					} else if ("java.math.BigDecimal".equals(toField.getType().getName())) {//转换成大浮点
						String bigDecimalStr = String.valueOf(valueObj == null ? "0.00" : valueObj);
						BigDecimal value = new BigDecimal(bigDecimalStr);
						toField.set(toObj, value);
						break;
					} else if ("java.lang.Double".equals(toField.getType().getName())) {//转换成double
						String doubleStr = String.valueOf(valueObj == null ? "0.00" : valueObj);
						Double value = Double.valueOf(doubleStr);
						toField.set(toObj, value);
						break;
					}else if ("java.lang.Integer".equals(toField.getType().getName())) {//转换成double
						String integerStr = String.valueOf(valueObj == null ? "0" : valueObj);
						Integer value = Integer.valueOf(integerStr);
						toField.set(toObj, value);
						break;
					}
				}
			}
		}
		return toObj;
	}
}
