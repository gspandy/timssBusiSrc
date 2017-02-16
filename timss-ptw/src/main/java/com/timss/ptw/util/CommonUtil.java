package com.timss.ptw.util;

import java.util.List;

import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.support.IEnumerationManager;


public class CommonUtil {
    
	/**
	 * @description:
	 * @author: 王中华
	 * @createDate: 2016-5-18
	 * @param iEnumerationManager 枚举类管理器
	 * @param enumCate 枚举类型
	 * @param enumValue 要比较的枚举值
	 * @param siteid 站点
	 * @return:
	 */
	public static boolean hasEnumValue(IEnumerationManager iEnumerationManager,String enumCate,
	        String enumValue,String siteid){
	    List<AppEnum> enumNewTodoList = iEnumerationManager.retriveEnumerationsByCat( enumCate );
	        for ( AppEnum appEnum : enumNewTodoList ) {
	            if(appEnum.getCode().equalsIgnoreCase( enumValue ) && 
	                    siteid.equals( appEnum.getSiteId() )){
	               return true;
	            }
	        }
	    return false;
	}
}
