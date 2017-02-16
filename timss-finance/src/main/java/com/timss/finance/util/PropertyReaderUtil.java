package com.timss.finance.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertyReaderUtil {
	private static Map<String, Properties> maps=new HashMap<String,Properties>();
	public static Properties readProperties(String name){
		return readProperties(name,false);
	}
	
	public static Properties readProperties(String name,boolean reflesh){
		if(reflesh){
			return getPropertiesAndRefleshMap(name);
		}else{
			return getPropertiesByCatch(name);
		}
	}
	
	private static Properties getPropertiesByCatch(String name){
		Properties properties=maps.get("name");
		if(properties==null){
			return getPropertiesAndRefleshMap(name);
		}else{
			return properties;
		}
	}

	private static Properties getPropertiesAndRefleshMap(String name) {
		Resource resource=new ClassPathResource(name);
		Properties properties;
		try {
			properties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			throw new RuntimeException("没有找到配置文件，在路径"+name, e);
		}
		maps.put("name", properties);
		return properties;
	}
	

}
