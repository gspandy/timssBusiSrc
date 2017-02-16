package com.timss.attendance.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @title: 从资源文件中获取配置信息
 * @description: {desc}
 * @company: gdyd
 * @className: PropertyUtil.java
 * @author: fengzt
 * @createDate: 2014年10月15日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PropertyUtil {
    private Logger log = Logger.getLogger( PropertyUtil.class );

    /**
     * <p>
     * Description:存放名值对的表
     * </p>
     */
    private Map<String, String> propertiesMap = new HashMap<String, String>();

    /**
     * @param propertiesFileName 资源文件名
     * @throws IOException
     */
    public PropertyUtil(String propertiesFileName) {
        super();
        // 1. 从默认包的路径中读取配置文件
        URL propertyFileURL = Thread.currentThread().getContextClassLoader()
                .getResource( propertiesFileName );
        InputStream in = null;
        try {
            in = propertyFileURL.openStream();
            Properties properties = new Properties();
            properties.load( in );
            Enumeration<Object> enums = properties.keys();
            // 2. 将配置文件中的名值对放进表中
            while (enums.hasMoreElements()) {
                String key = (String) enums.nextElement();
                String value = properties.getProperty( key );
                this.propertiesMap.put( key, value );
            }// END WHILE
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        } finally {
            if ( null != in ) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error( e.getMessage(), e );
                }
            }// END IF
        }// END TRY-CATCH-FINALLY

    }

    /**
     * @param propertiesFileName 资源文件绝对路径
     * @throws IOException
     */
    public PropertyUtil(File propertiesFile) throws IOException {
        super();
        // 1. 从绝对路径中读取配置文件
        InputStream in = null;
        try {
            in = new FileInputStream( propertiesFile );
            Properties properties = new Properties();
            properties.load( in );
            Enumeration<Object> enums = properties.keys();
            // 2. 将配置文件中的名值对放进表中
            while (enums.hasMoreElements()) {
                String key = (String) enums.nextElement();
                String value = properties.getProperty( key );
                this.propertiesMap.put( key, value );
            }// END WHILE
        } catch (IOException e) {
            throw e;
        } finally {
            if ( null != in ) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw e;
                }
            }// END IF
        }// END TRY-CATCH-FINALLY

    }

    /**
     * 根据名取得值
     * 
     * @author ADMIN
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String value = this.propertiesMap.get( key );
        return value.trim();

    }

    /**
     * <p>
     * Description:获取所有属性
     * </p>
     * 
     * @author Administrator
     * @return
     */
    public Map<String, String> getPropertiesMap() {
        return propertiesMap;
    }
    
    public static void main(String[] args) {
        PropertyUtil propertyUtil = new PropertyUtil( "attendanceEip.properties" );
            System.out.println(  propertyUtil.getProperty( "eip2AbnormityFormGroupName" ) );
       
    }

}
