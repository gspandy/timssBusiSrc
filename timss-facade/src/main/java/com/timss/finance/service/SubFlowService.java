package com.timss.finance.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.Execution;

import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * 
 * @title: Service
 * @description: 
 * @company: gdyd
 * @className: SubFlowService.java
 * @author: 梁兆麟
 * @createDate: 2014年10月10日
 * @updateUser: 梁兆麟
 * @version: 1.1
 */
public interface SubFlowService {
	/**
	* @description: 获取报销人数
	* @author: 梁兆麟
	* @createDate: 2014年10月9日
	* @param 
	* @return: List<String>
	* @ 
	*/
	public List<String> getFinUserNum(Execution execution);

	/**
	* @description: 获取报销人数
	* @author: 梁兆麟
	* @createDate: 2014年10月11日
	* @param 
	* @return: List<String>
	* @ 
	*/
	public void setSubFlowVariables(String pid);
}
