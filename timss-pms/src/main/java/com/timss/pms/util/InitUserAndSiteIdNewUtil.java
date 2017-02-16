package com.timss.pms.util;


import java.lang.reflect.Method;
import java.util.Date;

import org.apache.log4j.Logger;

import com.timss.pms.exception.PmsBasicException;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 初始化用户和站点信息帮助类,用于新表中
 * 
 * @ClassName: InitUserAndSiteIdNewUtil
 * @company: gdyd
 * @Description:TODO
 * @author: 黄晓岚
 * @date: 2014-7-8 上午8:33:49
 */
public class InitUserAndSiteIdNewUtil {
	private static String updateUserMethod = "setModifyuser";
	private static String createUserMethod = "setCreateuser";
	private static String siteidMethod = "setSiteid";
	private static String updateTimeMethod="setModifydate";
	private static String createTimeMethod="setCreatedate";

	private static final Logger LOGGER = Logger
			.getLogger(InitUserAndSiteIdNewUtil.class);

	/**
	 * 初始化object对象的用户属性值
	 * 
	 * @Title: initUser
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param object
	 *            bean 包含创建人和修改人信息
	 * @param userMethod
	 *            需要进行反射的set方法字符串
	 * @param itcMvcService
	 *            获取用户id
	 * @throws Exception
	 */
	private static void initUser(Object object, String userMethod,
			ItcMvcService itcMvcService) {
		if (object == null) {
			LOGGER.warn("增加user时，发现bean为null");
			return;
		}
		LOGGER.info("设置用户为当前用户，object:"+object+",userMethod:"+userMethod);
		UserInfoScope userInfoScope;

		userInfoScope = itcMvcService.getUserInfoScopeDatas();

		String userId = userInfoScope.getUserId();
		executeMethod(object, userMethod,userId);

		
	}

	/**
	 * 更新object对象的更新人员的当前人员，更新updateUser字段
	 * 
	 * @Title: initUpdateUser
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param object
	 * @param itcMvcService
	 * @throws Exception
	 */
	public static void initUpdateUser(Object object, ItcMvcService itcMvcService) {
		initUser(object, updateUserMethod, itcMvcService);
	}

	/**
	 * 更新object对象的创建人员的当前用户，更新createUser字段
	 * 
	 * @Title: initCreateUser
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param object
	 * @param itcMvcService
	 * @throws Exception
	 */
	public static void initCreateUser(Object object, ItcMvcService itcMvcService) {
		initUser(object, createUserMethod, itcMvcService);
	}

	/**
	 * 更新对象object的站点属性，更新siteid字段
	 * 
	 * @Title: initSiteid
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param object
	 * @param itcMvcService
	 * @throws Exception
	 */
	public static void initSiteid(Object object, ItcMvcService itcMvcService) {
		if (object == null) {
			LOGGER.warn("object 不能为空");
			return;
		}
		LOGGER.info("设置站点为当前站点，object:"+object+",siteidMethod:"+siteidMethod);
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String site = userInfoScope.getSiteId();
		executeMethod(object, siteidMethod,site);
		
	}
	
	private static Object executeMethod(Object origin,
			String methodName,Object args) {
		if(origin==null){
			return null;
		}
		Class<?> c=origin.getClass();
		Object result=null;
		
		
		Method method;
		try {
			method = c.getMethod(methodName, args.getClass());
			result=method.invoke(origin, args);
			
			
		}  catch (NoSuchMethodException e) {
			LOGGER.warn("类型"+c+"中没有找到"+methodName+"方法");
		} catch (Exception e) {
			
			throw new PmsBasicException("反射失败", e);
		}
		return result;
	}
	
	
	private static void initTime(Object object ,String field){
		if (object == null) {
			LOGGER.warn("object 不能为空");
			return;
		}
		executeMethod(object, field,new Date()) ;
		
	}
	
	/**
	 * 插入创建时间，默认更新createTime字段
	 * @Title: initCreateTime
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param object 
	 */
	public static void initCreateTime(Object object){
		initTime(object, createTimeMethod);
	}
	
	/**
	 * 初始化更新时间，默认更新updateTime字段
	 * @Title: initUpdateTime
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param object
	 */
	public static void initUpdateTime(Object object){
		initTime(object, updateTimeMethod);
	}
	
	/**
	 * 更新数据时，附加更新人信息，更新时间
	 * @Title: initUpdate
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param object
	 * @param itcMvcService
	 */
	public static void initUpdate(Object object,ItcMvcService itcMvcService){
		initUpdateTime(object);
		initUpdateUser(object, itcMvcService);
		initSiteid(object, itcMvcService);
	}
	
	/**
	 * 插入数据时附加站点信息，创建时间，创建人
	 * @Title: initCreate
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param object
	 * @param itcMvcService
	 */
	public static void initCreate(Object object,ItcMvcService itcMvcService){
		initCreateUser(object, itcMvcService);
		initCreateTime(object);
		initSiteid(object, itcMvcService);
	}
}
