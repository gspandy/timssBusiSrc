package com.timss.purchase.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

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

    private ReflectionUtil() {

    }

    /**
     * @description: 通过反射获取字段中存值
     * @author: 890166
     * @createDate: 2014-9-22
     * @param obj
     * @param fieldName
     * @return:
     */
    public static String getFieldValueByFieldName(Object obj, String fieldName) throws Exception {
        Class<?> objClass = obj.getClass();
        String value = null;

        Field field = objClass.getDeclaredField( fieldName );
        // 重新组织字段代码
        StringBuilder entityField = new StringBuilder( "" );
        entityField.append( fieldName.substring( 0, 1 ).toUpperCase() ).append(
                fieldName.substring( 1, fieldName.length() ) );
        // 获取get方法名称
        String getMethodName = "get" + entityField.toString();
        Object valueObj = objClass.getMethod( getMethodName ).invoke( obj );
        // 根据不同类型都一律转换为字符串
        if ( "java.lang.String".equals( field.getType().getName() ) ) {
            value = String.valueOf( valueObj == null ? "" : valueObj );
        } else if ( "java.util.Date".equals( field.getType().getName() ) ) {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
            value = sdf.format( valueObj == null ? new Date() : valueObj );
        } else if ( "java.math.BigDecimal".equals( field.getType().getName() ) ) {
            value = String.valueOf( valueObj == null ? "0" : valueObj );
        }
        return value;
    }
    /**
     * @description: 通过反射获取字段(包瓜父类)中存值
     * @author: 890162
     * @createDate: 2016-8-18
     * @param obj
     * @param fieldName
     * @return:
     */
    public static String getFieldValueByFieldNameSec(Object obj, String fieldName) throws Exception {
        Class<?> objClass = obj.getClass();
        String value = null;

        Field field = getRecursiveDeclaredField(obj, fieldName );
        // 重新组织字段代码
        StringBuilder entityField = new StringBuilder( "" );
        entityField.append( fieldName.substring( 0, 1 ).toUpperCase() ).append(
                fieldName.substring( 1, fieldName.length() ) );
        // 获取get方法名称
        String getMethodName = "get" + entityField.toString();
        Object valueObj = objClass.getMethod( getMethodName ).invoke( obj );
        // 根据不同类型都一律转换为字符串
        if ( "java.lang.String".equals( field.getType().getName() ) ) {
            value = String.valueOf( valueObj == null ? "" : valueObj );
        } else if ( "java.util.Date".equals( field.getType().getName() ) ) {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
            value = sdf.format( valueObj == null ? new Date() : valueObj );
        } else if ( "java.math.BigDecimal".equals( field.getType().getName() ) ) {
            value = String.valueOf( valueObj == null ? "0" : valueObj );
        } else if ( "java.lang.Double".equals( field.getType().getName() )){
            if( null == valueObj){
                valueObj = "0";
            }
            BigDecimal valbd = new BigDecimal(String.valueOf( valueObj ));  
            value = String.valueOf( valbd );
        } else if ( "java.lang.Integer".equals( field.getType().getName() )){
            value = String.valueOf( null == valueObj ? "0" : valueObj );
        }
        return value;
    }
    
    public static Field getRecursiveDeclaredField(Object obj,String fieldName){
        Field field = null ;
        for(Class<?> clazz = obj.getClass();clazz != ItcMvcBean.class ;clazz = clazz.getSuperclass()){
            try{
                field = clazz.getDeclaredField( fieldName );
                return field;
            }catch(Exception e){

            }
        }
        return null;
    }

    /**
     * @description:通过反射设置字段中存值
     * @author: 890166
     * @createDate: 2015-6-1
     * @param obj
     * @param fieldName
     * @return
     * @throws Exception :
     */
    public static Object setFieldValueByFieldName(Object obj, String fieldName, String fieldValue) throws Exception {
        Class<?> objClass = obj.getClass();

        // 获取字段信息
        Field field = objClass.getDeclaredField( fieldName );
        field.setAccessible( true );

        // 根据设入的实体类字段类型判断
        if ( "java.lang.String".equals( field.getType().getName() ) ) {// 转换成字符串
            String value = fieldValue;
            field.set( obj, value );
        } else if ( "java.util.Date".equals( field.getType().getName() ) ) {// 转换成日期
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            String dateStr = sdf.format( sdf.parse( fieldValue ) );

            DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            Date value = format.parse( dateStr );
            field.set( obj, value );
        } else if ( "java.math.BigDecimal".equals( field.getType().getName() ) ) {// 转换成大浮点
            String bigDecimalStr = fieldValue;
            BigDecimal value = new BigDecimal( bigDecimalStr );
            field.set( obj, value );
        } else if ( "java.lang.Double".equals( field.getType().getName() ) ) {// 转换成double
            String doubleStr = fieldValue;
            Double value = Double.valueOf( doubleStr );
            field.set( obj, value );
        } else if ( "java.lang.Integer".equals( field.getType().getName() ) ) {// 转换成double
            String integerStr = fieldValue;
            Integer value = Integer.valueOf( integerStr );
            field.set( obj, value );
        }
        return obj;
    }

    /**
     * @description: Bean 与 Bean之间的数据转换
     * @author: 890166
     * @createDate: 2014-10-17
     * @param formObj
     * @param toObj
     * @return
     * @throws Exception :
     */
    public static Object conventBean2Bean(Object fromObj, Object toObj) throws Exception {
        Class<?> fromObjClass = fromObj.getClass();
        Field[] fromFieldArray = fromObjClass.getDeclaredFields();
        String fromFieldName = null;

        Class<?> toObjClass = toObj.getClass();
        Field[] toFieldArray = toObjClass.getDeclaredFields();
        String toFieldName = null;

        for ( Field toField : toFieldArray ) {
            // 获取赋值实体字段名称
            toFieldName = toField.getName();
            toField.setAccessible( true );
            for ( Field fromField : fromFieldArray ) {
                // 获取取值实体字段名称
                fromFieldName = fromField.getName();
                fromField.setAccessible( true );

                if ( toFieldName.equals( fromFieldName ) ) {
                    StringBuilder entityField = new StringBuilder( "" );
                    entityField.append( toFieldName.substring( 0, 1 ).toUpperCase() ).append(
                            toFieldName.substring( 1, toFieldName.length() ) );
                    // 获取get方法名称
                    String getMethodName = "get" + entityField.toString();
                    Object valueObj = fromObjClass.getMethod( getMethodName ).invoke( fromObj );
                    // 根据设入的实体类字段类型判断
                    if ( "java.lang.String".equals( toField.getType().getName() ) ) {// 转换成字符串
                        String value = String.valueOf( valueObj == null ? "" : valueObj );
                        toField.set( toObj, value );
                        break;
                    } else if ( "java.util.Date".equals( toField.getType().getName() ) ) {// 转换成日期
                        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                        String dateStr = sdf.format( valueObj == null ? new Date() : valueObj );

                        DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                        Date value = format.parse( dateStr );
                        toField.set( toObj, value );
                        break;
                    } else if ( "java.math.BigDecimal".equals( toField.getType().getName() ) ) {// 转换成大浮点
                        String bigDecimalStr = String.valueOf( valueObj == null ? "0.00" : valueObj );
                        BigDecimal value = new BigDecimal( bigDecimalStr );
                        toField.set( toObj, value );
                        break;
                    } else if ( "java.lang.Double".equals( toField.getType().getName() ) ) {// 转换成double
                        String doubleStr = String.valueOf( valueObj == null ? "0.00" : valueObj );
                        Double value = Double.valueOf( doubleStr );
                        toField.set( toObj, value );
                        break;
                    } else if ( "java.lang.Integer".equals( toField.getType().getName() ) ) {// 转换成double
                        String integerStr = String.valueOf( valueObj == null ? "0" : valueObj );
                        Integer value = Integer.valueOf( integerStr );
                        toField.set( toObj, value );
                        break;
                    }
                }
            }
        }
        return toObj;
    }
}
