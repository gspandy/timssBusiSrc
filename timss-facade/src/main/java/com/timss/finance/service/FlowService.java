package com.timss.finance.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.Execution;

import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * 
 * @title: 值别Service
 * @description: 
 * @company: gdyd
 * @className: FlowService.java
 * @author: 吴圣
 * @createDate: 2014年7月03日
 * @updateUser: 吴圣
 * @version: 1.1
 */
public interface FlowService {
	
	/**
	 * 
	 * @description:开启流程，并返回String
	 * @author: 吴圣
	 * @createDate: 2014年6月28日
	 * @param fid ,flowName,userInfoScope
	 * @return:String
	 * @ 
	 */
	//Map<String, Object> startWorkflow(String fid, String flowNameEn, UserInfoScope userInfoScope) ;
	Map<String, Object> startWorkflow(FinanceMain getMain, List<FinanceMainDetail> mainDtlList, String finNameEn, String finTypeEn, UserInfoScope userInfoScope);
	 
  /**
	* 
	* @description:通过流程实例id返回financeFlowMatch匹配数据
	* @author: 吴圣
	* @createDate: 2014年7月17日
	* @param 
	* @return:String
	* @ 
	*/
		
	String getCandidateUsers(String taskId,String loginId);

	/**
	* @description: 绑定子流程
	* @author: 梁兆麟
	* @createDate: 2014年10月9日
	* @param 
	* @return:void
	* @ 
	*/
	void bindSubFlow(String businessId, String processInstId);

//	/**
//	* @description: 获取报销人数
//	* @author: 梁兆麟
//	* @createDate: 2014年10月9日
//	* @param 
//	* @return:void
//	* @ 
//	*/
//	public List<String> getFinUserNum1(Execution execution);

	/**
	* @description: 终止工作流
	* @author: 梁兆麟
	* @createDate: 2014年10月15日
	* @param 
	* @return:
	* @ 
	*/
	void stopWorkFlow(String taskId, String assignee, String owner,
			String message, String businessId);

	/** 
	 * @description: 回滚工作流操作
	 * @author: 890170
	 * @createDate: 2015-1-5
	 */
	String rollbackWorkFlowOpr(String fid) ;
	
	/** 
	 * @description: 废除报销单
	 * @author: 890170
	 * @createDate: 2015-1-7
	 */
	Map<String, Object> abolishFinanceInfo(String fid);
}
