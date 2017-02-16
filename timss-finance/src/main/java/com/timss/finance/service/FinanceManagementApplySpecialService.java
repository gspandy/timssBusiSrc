package com.timss.finance.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.pms.bean.FlowVoidParamBean;
import com.yudean.mvc.bean.userinfo.UserInfoScope;



public interface FinanceManagementApplySpecialService {
    String tmpInsertFinanceManagementApply(FinanceManagementApply financeManagementApply,List<FinanceMainDetail> details);
	
	Map<String, Object> insertFinanceManagementApplyAndStartWorkflow(FinanceManagementApply financeManagementApply,List<FinanceMainDetail> details );
	
	String tmpUpdateFinanceManagementApply(FinanceManagementApply financeManagementApply,List<FinanceMainDetail> details );
	
	Map<String,Object> updateFinanceManagementApplyAndStartWorkflow(FinanceManagementApply financeManagementApply,List<FinanceMainDetail> details );
	
	boolean updateFinanceManagementApplyApproving(FinanceManagementApply financeManagementApply,List<FinanceMainDetail> details);
	
	boolean updateFinanceManagementApplyApproved(FinanceManagementApply financeManagementApply,List<FinanceMainDetail> details);
	
	boolean updateFinanceManagementApplyApproved(String id);
	
	boolean updateFinanceManagementApplyApproving(String id);
	
	boolean deleteFinanceManagementApply(String id);
	
	boolean updateFinanceManageApplyFlowStatus(String id,String flowStatus);
	
	boolean stopWorkflow(String financeManagementApplyId,String taskId);
	
	boolean setWFVariable(String taskId, String processInstId,FinanceManagementApply fma);
	
	boolean voidFlow(FlowVoidParamBean params);
	
	/**
	 * @description:
	 * @author: 王中华
	 * @createDate: 2016-6-6
	 * @param applyId
	 * @param userInfoScope
	 * @param flag: normal/rollback分别代表顺序和退库审批时的修改
	 */
	void updateFinApplyCurrHandlerUser(String applyId, UserInfoScope userInfoScope,String flag);
	
	/**
	 * @description:不弹出审批框之间结束流程
	 * @author: 杨坤
	 * @createDate: 2016-12-30
	 */
	boolean endFlow(FlowVoidParamBean params);
	
	/**
	 * @description: 查询某段时间的某类申请的所有记录
	 * @author: 王中华
	 * @createDate: 2017-1-19
	 * @param type 申请类型
	 * @param beginDate  开始时间
	 * @param endDATE  结束时间
	 * @param siteid  站点
	 * @return:
	 */
	List<FinanceManagementApply> queryApplyList(String type,Date beginDate ,Date endDate,String siteid);
	
	
	
}
