package com.timss.purchase.service;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: ERPService.java
 * @author: 890124
 * @createDate: 2016-5-19
 * @updateUser: 890124
 * @version: 1.0
 */
public interface ERPService {
	
	String sendToERP(String payId,String invNum,String invdesc) throws Exception, Throwable;

}
