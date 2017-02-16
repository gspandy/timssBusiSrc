package com.timss.itsm.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class ItsmCommonUtil {
    private static Logger logger=Logger.getLogger(ItsmCommonUtil.class);
    /**
     * @description:如果长度大于length,则返回前length个字符，如果不大于则原值返回
     * @author: 王中华
     * @createDate: 2016-8-24
     * @param parentStr  被操作字符串
     * @param length  截取长度
     * @return:
     */
    public static String subString( String parentStr,int length) {
        String  result = parentStr.length()<=100?parentStr:parentStr.substring( 0, 100 );
        return result;
    }

    /**
     * @description:获取properties文件
     * @author: 890166
     * @createDate: 2014-8-16
     * @param key
     * @return:
     */
    public static String getProperties(String key){
            Properties p = null;
            InputStream in = null;
            String reStr = null;
            try{
                    in = Thread.currentThread().getContextClassLoader().getResourceAsStream("itsm.properties");  
                    p = new Properties();  
                    p.load(in); 
                    reStr = p.getProperty(key);
            }catch(Exception e){
                    logger.warn("获取文件itsm.properties时出错",e);
                    
            }finally {  
        try {  
            if (in != null) {  
                in.close();  
            }  
        } catch (IOException e) {  
            logger.warn("关闭文件itsm.properties的流时出错",e);  
        }  
    }
            return reStr;
    }
    
    
}
