package com.timss.pms.exception.util;

import com.timss.pms.exception.PmsBasicException;

/**
 * 为bean附加站点属性和用户名称过程出现的异常
 * @ClassName:     InitSiteAndUserException
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-10 上午10:49:46
 */
public class InitSiteAndUserException extends PmsBasicException{
	public InitSiteAndUserException(String message,Exception e){
		super(message,e);
	}
	public InitSiteAndUserException(Exception e){
		super(e);
	}
}
