package com.timss.pms.util;

import java.math.BigDecimal;

/**
 * 数值处理帮助类
 * @ClassName:     NumberUtil
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-8-26 上午9:30:11
 */
public class NumberUtil {
	/**
	 * 将double类型值保留两位小数后转换为string
	 * @Title: to2PrecisionString
	 * @param value
	 * @return
	 */
	public static String to2PrecisionString(double value){
		BigDecimal bigDecimal=new BigDecimal(value);
		return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
		
	}
}
