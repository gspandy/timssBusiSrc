package com.timss.pms.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.timss.finance.vo.ErpIdsControl;
import com.timss.finance.vo.ErpIdsGLInt;
import com.timss.pms.bean.ERPResponse;

/**
 * XML解析类
 * @ClassName:     XMLPaerserUtil
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-8-24 上午9:53:49
 */
public class XMLPaerserUtil {
	/**
	 * 将java对象转换为XML字符串
	 * @Title: parseObjectToXMLString
	 * @param source
	 * @return
	 */
	public static String parseObjectToXMLString(Object source){
		XStream xStream=new XStream(new DomDriver());
		xStream.alias("ErpIdsControl", ErpIdsControl.class);
		xStream.alias("ErpIdsGLInt", ErpIdsGLInt.class);
		String result=xStream.toXML(source);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T parseXMLStringToObject(String xml){
		XStream xStream=new XStream(new DomDriver());
		xStream.alias("service", ERPResponse.class);
		T t=(T)xStream.fromXML(xml);
		return t;
	}
}
