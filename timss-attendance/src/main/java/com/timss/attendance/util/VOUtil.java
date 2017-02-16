package com.timss.attendance.util;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 
 * @title: VOUtil
 * @description: json转vo , vo 转JSON
 * @company: gdyd
 * @className: VOUtil.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: yyn 20160216 换成fastjson
 * @version: 1.0
 */
public class VOUtil {

    private static Logger log = Logger.getLogger( VOUtil.class );
    
    /**
     * 
     * @description:vo转json
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param vo
     * @return:json
     */
    public static String fromVoToJsonUtil(Object vo) {
    	return JSON.toJSONString(vo);
    }
    
    /**
     * 
     * @description:json转vo
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param dataJson
     * @param clss
     * @return:
     */
    public static <T> T fromJsonToVoUtil(String dataJson , Class<T> clss ) {
        return JSON.parseObject(dataJson, clss);
    }
   
    /**
     * 
     * @description:json 转 HashMap
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param dataJson
     * @return:
     */
    public static HashMap<String, Object> fromJsonToHashMap( String dataJson ){
    	return JSON.parseObject(dataJson, new TypeReference<HashMap<String, Object>>(){});
    }
    
    /**
     * 
     * @description:json转List<Object>
     * @author: fengzt
     * @param <T>
     * @createDate: 2014年6月4日
     * @param dataJson
     * @param clss
     * @return:List<T>
     */
    public static  <T> List<T> fromJsonToListObject(String dataJson , Class<T> clazz ) {
    	return JSON.parseArray(dataJson, clazz);
    }
}
