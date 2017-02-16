package com.timss.attendance.util;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * 
 * @title: 集合排序
 * @description: {desc}
 * @company: gdyd
 * @className: SortUtil.java
 * @author: fengzt
 * @createDate: 2014年6月23日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class SortUtil {
    private static Logger log = Logger.getLogger( SortUtil.class );
    
    /**
     * 升序标志
     */
    public static final boolean ASC = true;
    /**
     * 降序标志
     */
    public static final boolean DESC = false;
    
    /**
     * @title: 集合排序
     * @description: 根据给定的集合和要进行排序的字段按照字符串的升序或降序进行排序
     * @author: fengzt
     * @createDate: 2014-06-23
     * @updateUser: fengzt
     * @param: list 要进行排序的集合
     * @param: sortKey 要进行排序的字段，像String,Integer这种类型只要把sortKey填null或者""即可
     * @param: isAsc 排序的类型，true为升序，false为降序
     * @version: 1.0
     */
    public static <T> void sortList(final List<T> list, final String sortKey, final boolean isAsc) {
        if ( list != null && list.size() > 0 ) {
            Collections.sort( list, new Comparator<T>() {
                public int compare(T o1, T o2) {
                    String methodName = null;
                    Method method = null;
                    String value1 = "";
                    String value2 = "";
                    try {
                        if ( !"".equals( sortKey ) && sortKey != null ) {
                            methodName = new StringBuffer().append( "get" ).append( StringUtils.capitalize( sortKey ) )
                                    .toString();
                            method = o1.getClass().getDeclaredMethod( methodName );
                            value1 = method.invoke( o1 ).toString();
                            value2 = method.invoke( o2 ).toString();
                        } else {
                            value1 = o1.toString();
                            value2 = o2.toString();
                        }
                    } catch (Exception e) {
                        log.error( e.getMessage(), e );
                    }
                    if ( isAsc ) {
                        return value1.compareTo( value2 );
                    } else {
                        return value2.compareTo( value1 );
                    }
                }
            } );
        }
    }
    
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add( "c" );
        list.add( "b" );
        list.add( "a" );
        list.add( "ab" );
        list.add( "ca" );
        list.add( "abd" );
        sortList( list, null, true );
        System.out.println("升序：" );
        for ( String string : list ) {
            System.out.print(string+ "\t");
        }
        sortList( list, null, false );
        System.out.println();
        System.out.println("降序：" );
        for ( String string : list ) {
            System.out.print(string + "\t");
        }
    }
}
