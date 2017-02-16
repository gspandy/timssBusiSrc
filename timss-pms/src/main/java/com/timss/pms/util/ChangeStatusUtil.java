package com.timss.pms.util;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.util.ReflectionUtils;

/**
 * 
 * @ClassName: ChangeStatus
 * @company: gdyd
 * @Description: 状态修改的工具类
 * @author: 黄晓岚
 * @date: 2014-6-24 上午8:40:56
 */
public class ChangeStatusUtil {

	/**
	 * 草稿状态对应的编码
	 */
	public static String draftCode = "draft";
	/**
	 * 通过状态编码
	 */
	public static String approvalCode = "approved";

	public static String approvingCode = "approving";

	public static String voidedCode = "voided";

	public static String biddingApply = "bia";

	public static String biddingResult = "bir";
	/**
	 * bean中获取设置状态的方法名
	 */
	private static String setStatusMethod = "setStatus";

	private static String setFlowStatusMethod = "setFlowStatus";

	private static final Logger LOGGER = Logger.getLogger(ChangeStatusUtil.class);

	/**
	 * 
	 * @Title: changeSToDraft
	 * @Description: 修改操作业务状态为value状态，object对象是一个bean,
	 * @param object
	 *            一个bean类型
	 * @param value
	 *            修改后的状态
	 */
	public static void changeSToValue(Object object, Object value) {
		try {
			LOGGER.info("设置对象" + object + "状态为" + value);
			// 获取object中设置状态的方法
			Method method = ReflectionUtils.findMethod(object.getClass(),
					setStatusMethod, String.class);
			// 设置object的状态位为草稿状态
			method.invoke(object, value);
		} catch (Exception e) {
			LOGGER.warn("状态转为草稿状态时出错，对象为" + object + ",状态为" + value, e);
		}
	}

	/**
	 * 
	 * @Title: changeSToDraft
	 * @Description: 修改操作业务状态为value状态，object对象是一个bean,
	 * @param object
	 *            一个bean类型
	 * @param value
	 *            修改后的状态
	 */
	public static void changeFlowStatusToValue(Object object, Object value) {
		try {
			LOGGER.info("设置对象" + object + "状态为" + value);
			// 获取object中设置状态的方法
			Method method = ReflectionUtils.findMethod(object.getClass(),
					setFlowStatusMethod, String.class);
			// 设置object的状态位为草稿状态
			method.invoke(object, value);
		} catch (Exception e) {
			LOGGER.warn("状态转为草稿状态时出错，对象为" + object + ",状态为" + value, e);
		}
	}

	public static void changeToVoidedStatus(Object object) {
		changeSToValue(object, voidedCode);
	}

	public static void changeSToValue(Object object, Object value, String suffix) {
		try {
			LOGGER.info("设置对象" + object + "状态为" + value);
			// 获取object中设置状态的方法
			Method method = ReflectionUtils.findMethod(object.getClass(),
					"setStatus" + suffix, String.class);
			// 设置object的状态位为草稿状态
			method.invoke(object, value);
		} catch (Exception e) {
			LOGGER.warn("状态转为草稿状态时出错，对象为" + object + ",状态为" + value, e);
		}
	}

	public static void changeToVoidedStatus(Object object, String suffix) {
		changeSToValue(object, voidedCode, suffix);
	}

	public static void changeToDraftStatusForBidding(Object object) {
		changeSToValue(object, draftCode);
		changeFlowStatusToValue(object, biddingApply);
	}

	public static void changeToApprovingForBidding(Object object) {
		changeSToValue(object, approvingCode);
		changeFlowStatusToValue(object, biddingApply);
	}

	public static void changeToApprovedForBidding(Object object) {
		changeSToValue(object, approvalCode);
		changeFlowStatusToValue(object, biddingApply);
	}

	public static void changeToApprovedForBiddingResult(Object object) {
		changeSToValue(object, approvalCode);
		changeFlowStatusToValue(object, biddingResult);
	}
	
	public static void changeToVoidedForBiddingApply(Object object) {
		changeSToValue(object, voidedCode);
		changeFlowStatusToValue(object, biddingApply);
	}
	
}
