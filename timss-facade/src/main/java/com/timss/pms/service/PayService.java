package com.timss.pms.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.timss.pms.bean.Checkout;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.Invoice;
import com.timss.pms.bean.Pay;
import com.timss.pms.bean.Project;
import com.timss.pms.vo.ContractVo;
import com.timss.pms.vo.PayDtlVo;
import com.timss.pms.vo.PayVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;


public interface PayService extends FlowVoidService{
	void insertPay(Pay pay,List<Invoice> invoices);
	
	/**
	 * 根据合同id，查询新建付款需要的信息
	 * @Title: queryPayByContractId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	PayDtlVo queryPayByContractId(String contractId);
	
	/**
	 * 根据结算id和合同id,查询付款信息
	 * @Title: queryPayByPayplanId
	 * @param payplanId
	 * @param contractId
	 * @return
	 */
	PayDtlVo queryPayByPayplanId(String payplanId,String contractId);
	
	PayDtlVo queryPayById(String payId);
	
	Map tmpInsertPay(Pay pay,List<Invoice> invoices);
	
	void updatePayApproving(Pay pay);
	
	void updatePayApproving(int payId);
	
	void updatePayApproved(Pay pay);
	
	void updatePayApproved(int payId);
	
	void tmpUpdatePay(Pay pay,List<Invoice> invoices);
	
	void deletePay(String id);
	
	/**
	 * 终止付款或者收款流程，并修改收付款状态
	 * @Title: stopWorkflow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param project
	 * @param procInstId
	 * @return
	 */
	int stopWorkflow(Pay pay,String processInstId,String reason);
	
	/**
	 * 
	 * @Title: queryPayList
	 * @Description: 查询结算信息
	 * @param: @param page 查询条件，包括分页信息
	 * @param: @param userInfo 用户信息
	 * @return: Page<PayVo>  包含的分页信息的结算信息
	 * @throws
	 */
	Page<PayVo> queryPayList(Page<PayVo> page,UserInfoScope userInfo);
	/**
         * @Title: updateUndoInfo
         * @Description: 更新退票信息
         * @param pay 结算记录
         * @return Map<String, Object>
         * @throws
         */
	Map<String, Object> updateUndoInfo(Pay pay);
	/**
         * @Title: startUndoFlow
         * @Description: 启动退票信息
         * @param pay 结算记录
         * @return Map<String, Object>
         * @throws
         */
	Map<String, Object> startUndoFlow(Pay pay);
	/**
         * @Title: updateStatus
         * @Description: 更新退票信息状态
         * @param pay 结算记录
         * @return int
         * @throws
         */
	int updateStatus(Pay pay);
	/**
	 * @Title:voidUndoFlow
	 * @Description:作废 退票流程
	 * @param flowVoidParamBean
	 * @return boolean
	 * @throws
	 */
	boolean voidUndoFlow(FlowVoidParamBean flowVoidParamBean);
	
	String queryBusinessIdByUndoFlowId(String undoFlowId);
	
        /**
         * generateNewPaySpNo
         * @Description:生成新的结算编号
         * @param prefix
         * @return String
         * @throws
         */
        String generateNewPaySpNo(String prefix);
        /**
         * isPaySpNoExisted
         * @Description:判断PaySpNo是否存在
         * @param prefix
         * @return String
         * @throws
         */
        boolean isPaySpNoExisted(String paySpNo, String payId);
}
