package com.timss.attendance.util;



/**
 * 
 * @title: 日历颜色渲染常量
 * @description: {desc}
 * @company: gdyd
 * @className: ColorUtil.java
 * @author: fengzt
 * @createDate: 2014年6月17日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class ColorUtil {

    /**
     * 白班
     */
    public static final String BAI = "#8085E9";
    
    /**
     * 中班
     */
    public static final String ZHONG= "#90ED7D";
    
    /**
     * 夜班
     */
    public static final String YE = "#F7A35C";
    
    /**
     * 休息
     */
    public static final String XIU = "#7CB5EC";
    

    
    /**
     * 
     * @description:通过名字转换成相对应的颜色值
     * @author: fengzt
     * @createDate: 2014年6月20日
     * @param name
     * @return:
     */
    public static String nameChangToColor( String name ){
        if( "1608".contains( name )  ){
            return BAI;
        }else if( "1611".contains( name ) ){
            return ZHONG;
        }else if( "1806".contains( name ) ){
            return YE;
        }else {
            return XIU;
        }
        
    }
    
    public static void main(String[] args) {
        String value = ColorUtil.nameChangToColor( "1608" );
        System.out.println( value );
    }
}
