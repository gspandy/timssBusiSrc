package com.timss.finance.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.yudean.itc.util.json.JsonHelper;

/**
 * json字符串与java对象转换类
 * @ClassName:     JsonUtil
 * @company: gdyd
 * @Description: 
 * @author:    黄晓岚
 * @date:   2014-7-14 下午4:48:18
 */
public class JsonUtil {
	static Logger logger=Logger.getLogger(JsonUtil.class);
	/**
	 * 将json字符串转换为List<Bean>的java对象
	 * @Title: fromJsonStringToList
	 * @Description: 
	 * @param json json字符串
	 * @param c  解析后， Bean的实际类型
	 * @return 返回List<Bean>的java对象
	 */
	public static <T> List<T> fromJsonStringToList(String json,Class<T> c){
		//字符串为空时，返回空对象
		if(json==null || json.equals("")){
			return null;
		}
		//将json字符串解析为jsonArray
		JSONArray jsonArray=JSONArray.fromObject(json);
		List<T> list=new ArrayList<T>();
		for(int i=0;i<jsonArray.size();i++){
			//jsonArray的每一项转换为一个bean对象
			T t = (T)JsonHelper.fromJsonStringToBean(jsonArray.getString(i), c);
			list.add(t);
		}
		
		return list;
	}
}
