package com.timss.pms.exception;



/**
 * 项目管理基础的异常类
 * @ClassName:     PmsBasicException
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-10 上午10:48:20
 */
public class PmsBasicException extends RuntimeException{
	public PmsBasicException() {
		super();
	}
	public PmsBasicException(String message,Throwable throwable){
		super(message,throwable);
	}
	public PmsBasicException(Throwable throwable){
		super(throwable);
	}
	public PmsBasicException(String message){
		super(message);
	}
}
