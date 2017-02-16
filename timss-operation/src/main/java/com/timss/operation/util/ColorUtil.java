package com.timss.operation.util;

import org.springframework.beans.factory.annotation.Value;


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
    public static final String BAI = "#018ED6";
    
    /**
     * 中班
     */
    public static final String ZHONG= "#DCB86A";
    
    /**
     * 夜班
     */
    public static final String YE = "#352D2B";
    
    /**
     * 休息
     */
    public static final String XIU = "#82C247";
    
    /**
     * 早班关键字字符串
     */
    @Value("${color.baiban}")
    private static String baiban;
    
    /**
     * 中班关键字字符串
     */
    @Value("${color.zhongban}")
    private static String zhongban;
    
    /**
     * 夜班关键字字符串
     */
    @Value("${color.yeban}")
    private static String yeban;
    
    /**
     * 休息关键字字符串
     */
    @Value("${color.xiuxi}")
    private static String xiuxi;
    
    
    /**
     * 
     * @description:通过班次名字转换成相对应的颜色值
     * @author: fengzt
     * @createDate: 2014年6月20日
     * @param shiftName
     * @return:
     */
    public static String shiftNameChangToColor( String shiftName ){
    	if(shiftName==null){
    		return "";
    	}
        if( "早班,早,baiban,zaoban,morning,白班,白".contains( shiftName )  ){
            return BAI;
        }else if( "中班,中,zhongban,noon".contains( shiftName ) ){
            return ZHONG;
        }else if( "夜班,夜,yeban,wanban,night,晚班,晚,日,日班".contains( shiftName ) ){
            return YE;
        }else if( "休息,休,xiuxi".contains( shiftName ) ){
            return XIU;
        }
        
        return "";
    }
    
    public static void main(String[] args) {
        String value = ColorUtil.shiftNameChangToColor( "bai" );
        System.out.println( value );
    }
}
