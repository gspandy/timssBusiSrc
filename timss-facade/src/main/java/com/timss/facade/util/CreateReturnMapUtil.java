package com.timss.facade.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
/**
 * 
 * @ClassName:     createReturnMap
 * @company: gdyd
 * @Description: 后台放回给前台的信息，操作是成功或者是失败
 * @author:    黄晓岚
 * @date:   2014-6-19 上午8:52:22
 */
public class CreateReturnMapUtil {
	private static Logger logger=Logger.getLogger(CreateReturnMapUtil.class);
	public static String SUCCESS_FLAG="success";
	public static String FAIL_FLAG="error";
	
	public static String FRONT_SUCCESS_FLAG="ok";
	
	/**
	 * 
	 * @Title: createMap
	 * @Description: 创建返回给前端的标志信息
	 * @param flag 标志信息,使用类本身的静态变量
	 * @param msg 详细的描述信息
	 * @return 
	 */
	public static Map createMap(String flag,String msg){
		logger.info("构造flag:"+flag+" ,msg:"+msg+"返回前台");
		HashMap map=new HashMap();
		map.put("flag", flag);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 
	 * @Title: createMap
	 * @Description: 创建返回给前台的信息
	 * @param flag 标志信息
	 * @param msg 提示信息
	 * @param data 返回数据
	 * @return
	 */
	public static Map createMap(String flag,String msg,Object data){
		logger.info("构造flag:"+flag+" ,msg:"+msg+" data:"+data+",返回前台");
		HashMap map=new HashMap();
		map.put("flag", flag);
		map.put("msg", msg);
		map.put("data", data);
		return map;
	}
	
	/**
	 * 创建一个带权限数据的返回结果
	 * @Title: createMapWithPrivilege
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param flag 标志信息
	 * @param msg 描述信息
	 * @param data 返回的数据
	 * @param privileges 返回的权限数据
	 * @return
	 */
	public static Map createMapWithPrivilege(String flag,String msg,Object data,Object privileges){
		logger.info("构造flag:"+flag+" ,msg:"+msg+" data:"+data+" pri:"+privileges+",返回前台");
		HashMap map=new HashMap();
		map.put("flag", flag);
		map.put("msg", msg);
		map.put("data", data);
		map.put("pri", privileges);
		return map;
	}
	
	/**
	 * 
	 * @Title: createGridMap
	 * @Description: 创建返回给前台的grid格式的数据
	 * @param flag 标志信息
	 * @param total 列表项数量
	 * @param rows 列表数据
	 * @return
	 */
	public static Map createGridMap(String flag,int total,Object rows){
		HashMap map=new HashMap();
		map.put("flag", flag);
		if(total>-1){
			map.put("total", total);
		}
		map.put("rows", rows);
		return map;
	}
	
	public static Map createFrontPageSucessReturn(Object data){
		HashMap map=new HashMap<String,Object>();
		map.put("status", FRONT_SUCCESS_FLAG);
		map.put("data", data);
		return map;
	}
}
