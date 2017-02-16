package com.timss.pms.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.timss.pms.exception.PmsBasicException;
/**
 * map类型与bean类型转换类
 * @ClassName:     BeanMapConvertUtil
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-8-12 下午2:37:37
 */
public class BeanMapConvertUtil {
	public static <T> List<T>  ListMapToListBean(List<Map> source,Class<T> c){
		if(source==null){
			return null;
		}
		List<T> lists=new ArrayList<T>();
		for(int i=0;i<source.size();i++){
			Map map=source.get(i);
			T t=null;
			try {
				t=c.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				throw new PmsBasicException("类型转换出错,要转成的类型为"+c.getName(), e);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				throw new PmsBasicException("类型转换出错,要转成的类型为"+c.getName(), e);
			}
			
			BeanWrapper beanWrapper = new BeanWrapperImpl(t); 
			beanWrapper.convertIfNecessary("", c);
			beanWrapper.setPropertyValues(map); 
			lists.add(t);
		}
		return lists;
	}
	
	
}
