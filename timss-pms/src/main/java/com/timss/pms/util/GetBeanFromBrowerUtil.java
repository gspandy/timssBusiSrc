package com.timss.pms.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.timss.pms.exception.PmsBasicException;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 将前台传过来的参数转换为bean
 * @ClassName:     GetBeanFromBrowerUtil
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-23 上午11:07:48
 */
public class GetBeanFromBrowerUtil {
	static Logger LOGGER=Logger.getLogger(GetBeanFromBrowerUtil.class);
	
	/**
	 * 
	 * @Title: getBeanFromBrower
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param prefix
	 * @param param
	 * @param c
	 * @param itcMvcService
	 * @return
	 * @throws Exception
	 */
	public static <T> T getBeanFromBrower(String prefix,String param,Class<T> c,ItcMvcService itcMvcService){
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String projectString;
		try {
			projectString = userInfoScope.getParam(param);
		} catch (Exception e) {
			throw new PmsBasicException("pms获取前端参数："+param+"出错",e);
		}
		LOGGER.info(prefix+":"+projectString);
		// jsonString To JavaBean的方法
		
		T t=null;
		if(StringUtils.isNotBlank(projectString)){
			t = JsonHelper.fromJsonStringToBean(projectString,c);
		}
		
	    return  t;
	}
	
	
	public static <T> List<T> getBeanListFromBrower(String prefix,String param,Class<T> c,ItcMvcService itcMvcService) throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String projectString = userInfoScope.getParam(param);
		LOGGER.info(prefix+":"+projectString);
		if(projectString==null || projectString.equals("") || projectString.equals("null")){
			return null;
		}
		// jsonString To JavaBean的方法
		List<T> t = JsonUtil.fromJsonStringToList(projectString, c);
		return t;
	}
	
	public static Map getMapFromBrower(String prefix,String param,ItcMvcService itcMvcService){
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String projectString;
		try {
			projectString = userInfoScope.getParam(param);
		} catch (Exception e) {
			throw new PmsBasicException("从userInfoScope获取map类型的参数时出错", e);
		}
		LOGGER.info(prefix+":"+projectString);
		// jsonString To JavaBean的方法
		Map map=null;
		try {
			if(StringUtils.isNotBlank(projectString)){
				map = MapHelper.jsonToHashMap(projectString);
			}
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new PmsBasicException(e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new PmsBasicException(e);
		}
		return map;
	}
}
